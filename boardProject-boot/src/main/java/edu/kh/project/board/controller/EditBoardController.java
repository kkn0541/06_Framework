package edu.kh.project.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.service.EditBoardService;
import edu.kh.project.member.model.dto.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("editBoard")
@RequiredArgsConstructor
public class EditBoardController {

	private final EditBoardService service;

	/**
	 * 게시글 작성 화면 전환
	 * 
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(@PathVariable("boardCode") int boardCode) {

		
		return "board/boardWrite"; //templates/board/boardWrite.html 로 forward 
								// forward할때 boardCode 같이 넘어감 
	}

	/*
	 * 
	 * List<multifile> images 
	 * -5개 모두 업로드 o -> 0~4 번 인덱스 파일 저장됨 
	 * -5개 모두 업로드 x  -> 0~4번 인덱스에 파일 저장 x 
	 * -2번 인덱스만 업로드  -> 2번 인덱스만 파일 저장 ,  0/1/3/4/번 인덱스는 저장 x 
	 * 
	 * [문제점 ]
	 * -파일이 선택되지 않은 input 태그도 제출되고 있음 
	 *  (제출은 되어있는데 데이터는 없음 ) 
	 *  
	 *  
	 *  ->파일 선택이 안된 input 값을 서버에 저장하려고 하면 오류 발생함 
	 *  
	 *  [해결방법]
	 *  무작정 서버에 저장 x 
	 *  -> 제출된 파일이 있는 확인하는 로직을 추가구성 
	 *  ->List의 각 인덱스에 들어있는 MultipartFile에 실제로 
	 *  제출된 파일이 있는지 확인하는 로직을 추가 구성 
	 *  
	 *  +List 요소의 index 번호 == IMG_ORDER와 같음 
	 *  
	 *
	 *   
	 * 
	 * */
	
	// 1. boardCode, 로그인한 회원번호를 inputBoard에 세팅 
	
	
	
	
	
	/**게시글 작성
	 * @param: boardCode : 어떤 게시판에 작성할 글인지 구분(1/2/3)
	 * @param inputBoard :입력된 값 (제목 , 내용 ) 세팅되어있음 (커맨드객체)
	 * @param loginMember : 로긍니한 회원번호를 얻어오는 용도(세션에 등록되어있음
	 * @param images : 제출된  file 타입input 태그가 전달된 데이터들(이미지 파일..
	 * @param 
	 * 
	 * 제목
	 * @throws Exception 
	 */
	
	@PostMapping("{boardCode:[0-9]+}/insert")	
	public String boardInsert(@PathVariable("boardCode") int boardCode,
								@ModelAttribute Board inputBoard,
								@SessionAttribute("loginMember") Member loginMember,
								@RequestParam("images")List<MultipartFile> images,
								RedirectAttributes ra) throws Exception {
		
		
		inputBoard.setBoardCode(boardCode);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		// inputBoard 총 네가지 세팅됨(boardTitle,boardContent, boardCode,memberNo)
		
		//2. 서비스 메서드 호출 후 결과 반환 받기 
		// 성공 시 [상세 조회]를 요청 할수 있도록 
		// 삽입된 게시글 번호를 반환 받기 
		int boardNo = service.boardInsert(inputBoard,images); 
		
		//3. 서비스 결과에 따라 message , 리다이렉트 경로 지정 
		
		String path = null;
		String message = null;
		
		if(boardNo > 0) {
			
			path="/board/"+boardCode+"/" + boardNo;  //board/1/2002 -> 상세조회 
			message ="게시글이 작성되었습니다.";
			
		}else {
			
			path="insert"; // 상대경로쓸때는 '현재위치가 ' 가장 중요 
							//eidtBoard/1/insert 
							// 맨뒤에 insert를 path "insert"로 갈아끼운다 
			message = "게시글 작성실패";
		}
		ra.addFlashAttribute("message",message);
		
		
		return "redirect:"+path;
	}
	

}