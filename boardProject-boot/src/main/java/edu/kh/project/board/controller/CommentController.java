package edu.kh.project.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.board.model.dto.Comment;
import edu.kh.project.board.model.service.CommentService;
import lombok.RequiredArgsConstructor;

/*
 * @RestController (REST API 구축을 위해서 사용하는 컨트롤러 
 * 
 * ==> @controller(요청/ 응답 제어 역할 명시 + bean 등록) 
 * 		+@responsboy (응답 본문으로 응답데이터 자체를 반환) 
 * 		
 * -> 모든 요청에 대한 응답을 응답 본문 으로 반환하는 컨트롤러 
 * 
 * */





//전부다 비동기 요청만 받을 클래스를 만들경우 @RestController  를쓰면 
// @responsebody 를 사용하지 않아도됨 
//@Controller 작성 x

@RestController  // @controller대신 작성
@RequestMapping("comment")
@RequiredArgsConstructor
public class CommentController {

	// fetch - 비동기 요청 
	// "/comment" 요청이 오면 해당 컨트롤러에서 잡아서 처리함 
	//  @Responsebody 를 매번 메서드에 추가 ..
	
	
	private final CommentService service;
	
	/** 댓글 목록 조회 
	 * @param boardNo
	 * @return
	 */
	// /commnet 요청만 오고 있어서 빈문자열"" 넣어주면됨 
	@GetMapping("")
	//파라미터중 키가 boardNo 다  /commnet?boardNo="+
	public List<Comment> select(@RequestParam("boardNo")int boardNo){
		
		//변환은 HttpMessageConverter가 해줌  
		//List- > JSON(문자열)로 변환해서 응답  ->JS 
		// LIST -> 문자 -> JS객체 
		return service.select(boardNo);
	}
	
	
	/**댓글 답글 등록 
	 * @return
	 */
	@PostMapping("")
	public int insert(@RequestBody Comment comment) {
		return service.insert(comment);
	}


	@DeleteMapping("")
	public int delete(@RequestBody int commentNo) {
		return service.delete(commentNo);
	}

	@PutMapping("")
	public int update(@RequestBody Comment comment) {
		return service.update(comment);
	}

}
