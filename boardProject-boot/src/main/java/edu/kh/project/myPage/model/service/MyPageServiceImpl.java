package edu.kh.project.myPage.model.service;

import java.io.File;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class) // 모든 예외 발생 시 롤백 / 아니면 커밋
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService {

	private final MyPageMapper mapper;

	// Bcrypt 암호화 객체 의존성 주입 (securityConfig 참고 )
	private final BCryptPasswordEncoder bcrypt;

// 회원정보 수정 
	@Override
	public int updateInfo(Member inputMember, String[] memberAddress) {

		// 입력된 주소가 있을 경우
		// memberAddress를 A^^^B^^^C형태로 가공

		// 주소 입력 X -> inputMember.getMemberAddress() ->",,"
		if (inputMember.getMemberAddress().equals(",,")) {

			// 주소에 null 대입
			inputMember.setMemberAddress(null);
		} else { // 주소 입력 o

			String address = String.join("^^^", memberAddress);
			inputMember.setMemberAddress(address);

		}

		return mapper.updateInfo(inputMember);
	}

	// 비밀번호 변경 서비스
	@Override
	public int changePw(Map<String, Object> paramMap, int memberNo) {

		// 1. 현재 로그인한 비밀번호가 일치하는지 확인하기
		// - 현재 로그인한 회원의 암호화된 비밀번호를 db에서 조회
		String originPw = mapper.selectPw(memberNo);

		// 입력받은 현재비밀번호와 (평문)
		// DB에서 조회한 비밀번호(암호화)를 비교 ->

		// 입력받은 현재 비밀번호 (평문)
		// DB에서 조회한 비밀번호(암호화를) 비교
		// BCRYPTPASSWORDENCODER

		// 다를 경우
		if (!bcrypt.matches((String) paramMap.get("currentPw"), originPw)) {

			return 0;

			// 얼리리턴패턴
			// 효울증가

		}

		// 2. 같을 경우 새 비밀번호를 암호화 (bcyrptPassworEncoder.encode
		// 새비밀번호를 암호화 (bcrptpasswordEncoder.encde(평문)
		String encPw = bcrypt.encode((String) paramMap.get("newPw"));

		// 진행 후 db업데이트
		// SQL에 전달해야하는 2개 (암호화한 새비밀번호 , 회원번호)
		// ->SQL 전달인가 1개
		// -> 묶어서 전달 (paramMap)

		paramMap.put("encPw", encPw);
		paramMap.put("memberNo", memberNo);

		return mapper.changePw(paramMap);
	}

	// 회원 탈퇴
	@Override
	public int secession(String memberPw, int memberNo) {
	
		
		//현재 로그인한 회원의 암호화된 비밀번호를 db에서 조회 
		String originPw = mapper.selectPw(memberNo);
		
		//다를 경우 
		//                 평문        암호화 
		if(!bcrypt.matches(memberPw, originPw)) {
		
		return 0;
	}

	//같은경우
	return mapper.secession(memberNo);
}
	
	
	
	// 파일 업로드 테스트 1
	@Override
	public String fileUpload1(MultipartFile uploadFile) throws Exception{

		// MultipartFile이  제공하는 메서드 
		// - getSize() :  파일크기
		// - isEmpty();  업로드한 파일이 없을 경우 true / 있다면 false
		// -getOriginalFileName() : 원본 파일명
		// - transferTo(경로) : 
		//   메모리 또는 임시저장경로에 업로드된 파일을 
		// 원하는 경로에 실제로 전송(서버 어떤 폴더에 지정할지 지정 )
		
		if(uploadFile.isEmpty()) { //업로드한 파일이 없을 경우 
			return null; 
			
		}
		
		// 업로드한 파일이 있을 경우 
		// c:/uploadFiles/test/파일명 으로 서버에 저장 
		uploadFile.transferTo(new File("C:/uploadFiles/test/"+ uploadFile.getOriginalFilename()));
		
		// 웹 에서 해당 파일에 접근 할수 있는 경로를 반환 
		
		// 서버 : C:/uploadFiles/test/A.jpg
		// 웹 접근 주소 :/myPage/file/A.jpg
		
		
		
		return "/myPage/file/"+uploadFile.getOriginalFilename();
	}
	
}