package edu.kh.project.myPage.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
import edu.kh.project.myPage.model.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/*
 * @SessionAttributes의 역할 
 * -Model에 추가되 ㄴ속성 중 key값이 일치하는 속성을 session scope로 변경 
 * -SessionStatus 이용시 session에 등록된 완료할 대상을 찾는 용도 
 * 
 * 
 * 
 * 
 * 
 * */



@SessionAttributes({"loginMember"})
@Controller
@RequestMapping("myPage")
@RequiredArgsConstructor   // autowird 자동으로 해줌 
@Slf4j // 로그 객체 자동완성
public class MyPageController {

	private final MyPageService service;

	
	
	
	
	
	/**
	 * @param loginMember 세션에 존재하는 loginMember를 얻어와 매개변수에 대입
	 * @return
	 */
	@GetMapping("info") // myPage/ info Get 방식 요청
	public String info(@SessionAttribute("loginMember") Member loginMember, Model model) {

		// 현재 로그인한 회원의 주소를 꺼내옴
		// 현재 로그인한 회원 정보 -> session에 등록되 상태 (loginMember)
		// memberAddress =13536^^^경기 성남시 분당구 판교역로 4^^^1층 202

		log.debug("loginMember" + loginMember);

		String memberAddress = loginMember.getMemberAddress();

		// 주소가 있을 경우에만 동작
		if (memberAddress != null) {

			// 구분자 "^^^" 를 기준으로
			// memberAddress 값을 쪼개어 Stringp[] 로 반환
			String[] arr = memberAddress.split("\\^\\^\\^");

			// 13536^^^경기 성남시 분당구 판교역로 4^^^1층 202
			// -> ["13536" ,"경기 성남시 분당구 판교역로 4","1층 202]
			// 0 1 2

			model.addAttribute("postcode", arr[0]);
			model.addAttribute("address", arr[1]);
			model.addAttribute("detailAddress", arr[2]);

		}

		return "myPage/myPage-info";

	}

	/**
	 * 회원정보 수정
	 * 
	 * @param inputMember   : 커맨드 객체 (@modelAttribute 생략된 상태 ) 제출된 회원 닉네임 , 전화번호 ,
	 *                      주소
	 * @param loginMember   : 로그인한 회원 정보 (회원번호 사용할 예정 )
	 * @param memberAddress : 주소만 따로 받은 string[]
	 * @param ra            : 리다이렉트시 request scope로 message같은 데이터 전달
	 * @return
	 */
	
	
	
	
	@PostMapping("info")
	public String updateInfo(Member inputMember, @SessionAttribute("loginMember") Member loginMember,
			@RequestParam("memberAddress") String[] memberAddress, RedirectAttributes ra) {

		// inputMember 에 로그인한 회원 번호 추가
		inputMember.setMemberNo(loginMember.getMemberNo());
		// 회원의 닉네임 전화번호 주소 회원번호

		// 회원정보 수정 서비스 호출
		int result = service.updateInfo(inputMember, memberAddress);

		String message = null;

		if (result > 0) {
			message = "회원정보 수정 성공!!";

			// loginMember 새로 세팅
			// 우리가 방금 바꾼 값으로 세팅
			// -> 세션에 저장된 로그인한 회원정보가 저장된 객체를 참고하고 있다.

			// loginMember는 세션에 저장된 로그인한 회원 정보가 저장된 객체를 참조하고 있다.

			// ->loginMember수정하면
			// 세션에 저장된 로그인한 회원 정보가 수정된다.

			// == 세션 데이터와 DB데이터를 맞춤

			loginMember.setMemberNickname(inputMember.getMemberNickname());

			loginMember.setMemberTel(inputMember.getMemberTel());

			loginMember.setMemberAddress(inputMember.getMemberAddress());

		} else {
			message = "회원 정보 수정 실패 ";
		}
		ra.addFlashAttribute("message", message);

		return "redirect:info";
	}

	@GetMapping("profile") // myPage/ info Get 방식 요청
	public String profile() {

		return "myPage/myPage-profile";

	}

	// 비밀번호 변경 화면 이동
		@GetMapping("changePw") //   /myPage/changePw  GET 방식 요청
		public String changePw() {
			return "myPage/myPage-changePw";
		}
	
	
	
	
	// 비밀번호 변경
	/**
	 * @param paramMap : 모든 파라미터를 맵으로 저장  ( 비밀번호 , 현재비밀번호 , 
	 * @param loginMember : 세션에 등록된 현재 로그인한 회원의 정보
	 * @param ra     ra:리다이렉트시 request scope로 메시지 전달 역할 
	 * @return
	 */
	@PostMapping("changePw") // /myPage/changePw post 매칭 요청 
	//파라미터 묶어서 받아오기 
	public String changePw(@RequestParam Map<String, Object> paramMap,
			//				세션에 로그인한 정보가져오기 
							@SessionAttribute("loginMember") Member loginMember,
			// 				리다이렉트 메세지 
							RedirectAttributes ra
			) {

	log.debug("paramMap"+ paramMap);
	log.debug("loginMember"+ loginMember);
	
	//로그인한 회원 번호 
	int memberNo = loginMember.getMemberNo();
		
	//현재(paramMap) + 새(paramMap) + 회원 번호를 서비스로 전달 
	int result =service.changePw(paramMap,memberNo);
		
	
	String path=null;
	String message = null;
	
	if(result > 0) {
		//변경 성공 시 
		// 메세지 비밀번호 변경되었습니다.
		// 리다이렉트 /myPage/info
		message="비밀번호 변경";
		path ="/myPage/info";
		
	
		
	}else {
		//변경 성공 시 
				// 메세지 비밀번호 변경되었습니다.
				// 리다이렉트 /myPage/info
				message="현재비밀번호가 일치하지 않습니다.";
				path ="/myPage/changePw";
			
		
	}
		
	ra.addFlashAttribute("message",message);
	
		
		return "redirect:"+path;
	}


	
	/** 회원 탈퇴 
	 * @param memberPw : 입력받은 비밀번호 
	 * @param loginMember : 로그인한 회원 정보 (세션) 
	 * @param status :세션 완료 용도의 객체 
	 * 		-> @sessionAttributes 로 지정된 특정 속성을 세션에서 제거 기능 제공 객체 
	 * @return
	 */
	@PostMapping("secession")
	public String secession( @RequestParam("memberPw") String memberPw,
							@SessionAttribute("loginMember") Member loginMember,
							//세션지울때 (로그아웃) 
							SessionStatus status ,
							RedirectAttributes ra ) {

		
		//로그인한 회원의 회원번호 꺼내기 
		int memberNo =loginMember.getMemberNo();
		
		//서비스 호출 ( 입력받은 비밀번호 , 로그인한 회원번호 ) 
		int result =service.secession(memberPw,memberNo);
		
		String message = null;
		String path =null;
		
		if(result >0) {
			message ="탈퇴 되었습니다.";
			path="/";
			
			status.setComplete();//세션 완료 시킴 
			
			
			
		} else {
			
			message ="비밀번호가 일치하지 않습니다.";
			path="secession";
			
			
		}
		
		ra.addFlashAttribute("message+" ,message);
		
		// 탈퇴 성공 : redirect:/ (메인페이지 재요청) 
		// 탈퇴 실패 - redirect:secession(상대경로)
		// 				->myPage(secession)현재경로 
		// 				->myPage(secession)(Get 요청)
		return "redirect:"+path; 
		
	}
	
	
	
	
	// 회원탈퇴
	@GetMapping("secession")
	public String secession() {

		return "myPage/myPage-secession";
	}

	// 파일업로드 
	@GetMapping("fileTest")
	public String fileTest() {

		return "myPage/myPage-fileTest";
	}

	
	/*
	 * Spring 에서 파일 업로드를 처리하는 방법 
	 * 
	 * - encType ="multiPart/form-data"로 클라이언트 요청을 받으면 
	 * (문자 , 숫자 , 파일 등이 섞여있는 요청)
	 * 
	 * 
	 * 이를 MultipartResolver(FileConfig에 정의)를 이용해서 섞여있는 파라미터를 분리
	 * 
	 * 문자열, 숫자 ->String 
	 * 파일         -> MultipartFile
	 * 
	 * 
	 * 
	 * 
	 * 
	 * */
	
	
	

	/**파일테스트 1
	 * @param uploadFile : 업로드한 파일 + 파일에 대한 내용 및 설정 내용 
	 * @return 
	 */
	@PostMapping("file/test1") // /myPage/file/test1 POST 요청 매핑 
	public String fileUpload1(
			@RequestParam("uploadFile") MultipartFile uploadFile,
			RedirectAttributes ra
			) throws Exception{
		
		String path =service.fileUpload1(uploadFile);
		
		//파일이 저장되어 웹에서 접근할수 있는 경로가 반환되었을떄 
		if(path !=null) {
			ra.addFlashAttribute("path",path);
			// addAttribute는 값을 지속적으로 사용해야할때 addFlashAttribute는
			//일회성으로 사용해야할때 사용해야합니다.
			
			
		}
		
		
		return"redirect:/myPage/fileTest";
		//redirect는  :/myPage/fileTest 이경로로 get요청 
	}
	
	@PostMapping("file/test2")
	public String fileUpload2(@RequestParam("uploadFile")MultipartFile uploadFile,
							@SessionAttribute("loginMember") Member loginMember,
							RedirectAttributes ra) throws Exception { 
		
		// 로그인한 회원의 번호 얻어오기 (누가 업로드 했는가 ) 

		int memberNo = loginMember.getMemberNo();
		
		//업로드된 파일 정보를 DB에 INSERT 후 결과 행의 개수 반환 받을 예정 
		int result= service.fileUpload2(uploadFile,memberNo);
		
		String message = null;
		
		if(result >0) {
			message ="파일 업로드 성공";
		} else {
			message ="파일 업로드 실패";
		}
		
		ra.addFlashAttribute("message",message);
		
		//RedirectAttributes 원래 requst 객체 
		
		
		return "redirect:/myPage/fileTest"; //myPage/fileTest GET 방식 재요청 
											//위에 @(fileTest) 
		
		
	}

	// 파일 목록 조회 화면 이동 
	/** 파일 목록 조회화여 응답화면으로 이동 
	 * @param  model :값 전달용 객체 (기본 request scope
	 * @param loginMember :현재 로그인한 회원의 정보 
	 * @return  
	 * 
	 */
	@GetMapping("fileList")			// /myPage/fileList Get방식 요청 
	public String fileList(Model model,
							@SessionAttribute("loginMember") Member loginMember) {
	
		//파일 목록 조회 서비스 호출 (현재 로그인한 회원이 올린 이미지만 
		int memberNo = loginMember.getMemberNo();
		List<UploadFile> list = service.fileList(memberNo);
		
		//model에 list 담아서 forward
		model.addAttribute("list",list);
		
		
		// template/myPage/myPage-fileList.html
		// list 가지고 아래 파일주소로 넘어감 
		return "myPage/myPage-fileList";
	}
	
	@PostMapping("file/test3")
	public String fileUpload3(@RequestParam("aaa") List<MultipartFile> aaaList,
			@RequestParam("bbb") List<MultipartFile> bbbList,
			@SessionAttribute("loginMember")Member loginMember,
			RedirectAttributes ra) throws Exception {
			
		
		// aaa 파일 미제출 시 
		// 0번 1번 인덱스 파일이 모두 비어있음 
		
		// bbb(multiple) 파일 미제출 시 
		// -> 0번 인덱스 파일이 비어있음 
		
		
		
	//	log.debug("aaaList:" +aaaList);
	//	log.debug("bbbList: "+ bbbList);
	
	// 여러파일을 업로드 파일 서비스 호출 
		// 업로드한 사람 넘버
		int memberNo = loginMember.getMemberNo();
		
		int result = service.fileUpload3(aaaList,bbbList ,memberNo);
		//result == 업로드된 파일 개수 
		
		String message = null;
		
		if(result ==0) {
			message ="업로드된 파일이 없습니다.";
			
		}else {
			message = result +"개의 파일이 업로드 되었습니다.";
		}
	
		
		ra.addFlashAttribute("message",message);
		
		
		
		return "redirect:/myPage/fileTest";
		
	}
	
	
	/** 프로필 이미지 변경 요청 
	 * @param profileImg
	 * @param loginMember
	 * @param ra
	 * @return
	 */
	@PostMapping("profile")
	public String profile(@RequestParam("profileImg")MultipartFile profileImg,
						@SessionAttribute("loginMember") Member loginMember,
						RedirectAttributes ra )throws Exception {
		
		//서비스 호출 
		int result = service.profile(profileImg,loginMember);
		
		String message = null;
		
		if(result>0) {
			message ="변경성공";
		}else {
			message = "변경 실패ㅠㅠㅜㅠㅜㅠㅜㅠㅜ";
		}
		
		ra.addFlashAttribute("message" ,message);
		
		
		return"redirect:profile"; // 리다이렉트 - /myPage/profile Get요청(상대경로)
	}
	
	
}
