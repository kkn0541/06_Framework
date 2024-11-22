package edu.kh.project.board.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.member.model.dto.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("board")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

// /board/1 
// /board/2
// /board/3

// 다 이쪽으로 매핑됨 
// /board/insert 도 이쪽으로 매핑되기 때문에 뭔가방지해야함

	private final BoardService service;

	/**
	 * 
	 * 
	 * @param boardCode
	 * @return
	 * 
	 *         {boardCode}
	 * 
	 *         -/board/xxx
	 * 
	 *         /board 이하 1레벨 자리에 어떤 주소값이 들어오든 모두 이메서드 매핑
	 * 
	 *         /board 이하 1레벨 자리에 숫자로된 요청 주소가 작성되어 있을때만 동작-> [0-9] :한칸에 0~9 사이 숫자 입력
	 *         가능 + : 하나 이상
	 * 
	 *         [0-9]+ : 모든 숫자
	 * 
	 * 
	 * 
	 */

	/**
	 * 게시글 목록 조회
	 * 
	 * @param boardCode :게시판 종류 구분 번호 (1/2/3..)
	 * @param cp        :현재 조회 요청한 페이지 번호 ( 없으면 1 )
	 * @param model
	 * @param  paramMap(검색할때 추가) : 제출된 파라미터가 모두 지정된 Map(검색 시 ,key 와 query 담겨있음)
	 * 							ex)	{key=t,query=짱구}
	 * @return
	 */
	//원래는 게시글목록조회만 
	//11/22  검색조건 추가 ( 파라미터 추가 @RequestParam Map<String, Object> paramMap)
	@GetMapping("{boardCode:[0-9]+}")
	public String selectBoardList(@PathVariable("boardCode") int boardCode,
			@RequestParam(value = "cp", required = false, defaultValue = "1") int cp, Model model,
			@RequestParam Map<String, Object> paramMap
			) {
		// 조회 서비스 호출 후 결과 반환
		Map<String, Object> map = null;

		//검색이 아닌 경우  --> paramaMap {}  이런형태로 넘어옴 
		//아무것도 없으니까 
		//param map의 key 
		if(paramMap.get("key") == null) {

			// 게시글 목록 조회 서비스 호출
			map = service.selectBoardList(boardCode, cp);
			
		}else {
			//검색인 경우      --> paramMap {key=t,query=짱구}
			
			//boardCode를 paramMap에 추가 
			paramMap.put("boardCode", boardCode);
			//-->paramMap {key=t,query=짱구, boradCode=1}
			
			//검색 서비스 호출 
			map = service.searchList(paramMap,cp);
			
		}
		
		
		

		// model에 반환 받은 값을 등록
		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("boardList", map.get("boardList"));

		// forward :boardList.html

		return "board/boardList";
	};

	// 상세조회 요청 주소
	// board/1/2001?cp=1
	// board/2/19630?cp=2

	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}")
	public String boardDetail(@PathVariable("boardCode") int boardCode, @PathVariable("boardNo") int boardNo,
			Model model, RedirectAttributes ra,@SessionAttribute(value="loginMember",required = false)Member loginMember,
			HttpServletRequest req, // 요청에 담긴 쿠키 얻어오기 
			HttpServletResponse resp // 새로운 쿠키 만들어서 응답하기 
			) {

		// 게시글 상세 조회 서비스 호출

		// 1)Map 으로 전달할 파라미터 묶기
		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);

		//로그인 상태인 경우에만 memberNo 추가 
		if(loginMember != null) {
			map.put("memberNo", loginMember.getMemberNo());
		}
		
		// 2) 서비스 호출
		Board board = service.selectOne(map);

		String path = null;

		if (board == null) {
			path = "redirect:/board/" + boardCode; // 목록 재요청
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다.");

		} else {
			/*---------------쿠키를 이용한 조회 수 증가 -------------*/
			
			// 비회원 또는 로그인한 회원의 글이 아닌 경우 (==글쓴이를 뺀 다른 사람) 
			if(loginMember==null ||
					loginMember.getMemberNo() !=board.getMemberNo()) {
			// 글쓴이가 아니거나 비ㅇ회원인경우에만 조회수 증가 해주겠다!
			
				//요청에 담겨있는 모든 쿠키 얻어오기 
				Cookie[] cookies = req.getCookies();
				
				Cookie c = null;
				
				for(Cookie temp : cookies) {
					
					//요청에 담긴 쿠키에 "readBoardNo" 가 존재할때 
					if(temp.getName().equals("readBoardNo")){
						c=temp;
						break;
					}
				}
				
				int result =0; // 조회수 증가 결과를 저장할 변수 
				
				//"readBoardNo"가 쿠키에 없을떄 
				
				
				if( c== null) {
					
					// 새 쿠키 생성("readBoardNo",[게시글번호])
					c= new Cookie("readBoardNo", "["+ boardNo +"]");
					result = service.updateReadCount(boardNo);
					
				}else {
					// "readBoardNo"가 쿠키에 있을 때
					// "readBoardNo" : [2][30][400][2000][4000]
					
					// 현재 글을 처음 읽는 경우 
					if(c.getValue().indexOf("["+ boardNo +"]") == -1) {
						
						
						//해당 글 번호를 쿠키에 누적 + 서비스 호출 
					c.setValue(c.getValue()+ "["+ boardNo +"]");
						//[2][30][400][2000][4000]+"["+ boardNo +"]"
					result = service.updateReadCount(boardNo);
					}
					
					
				}
				
				//조회수 증가 성공 / 조회 성공 시 
				if(result > 0 ) {
					
					//먼저 조회된 board의 readCount 필드값을 
					//result 값을 다시 세팅 
					board.setReadCount(result);
					c.setPath("/"); // "/"이하 경로 요청 시 쿠키서버로 전달 
				
					//쿠키 수명 지정 
					//현재 시간을 얻어오기 
					LocalDateTime now =LocalDateTime.now();
					
					//다음날 자정 지정
					LocalDateTime nextDayMidnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
				
					//다음날 자정까지 남은 시간 계산 (초단위)
					long secondsUntilNextDay= Duration.between(now, nextDayMidnight).getSeconds();
					
					//쿠키 수명 설정 
					c.setMaxAge((int)secondsUntilNextDay);
				
					resp.addCookie(c); // 응답객체를 이용해서 클라이언트에게 전달 
				}
			}
			
			/*------쿠키를 이용한 조회수 증가 끝-------- */
			
			
			
			// 조회 결과가 있는 경우
			path = "board/boardDetail"; // boardDetail.html로 forward

			// board -게시글 일반 내용 +imageList+ commentList
			model.addAttribute("board", board);

			// 조회된 이미지 목록 (imageList)가 있을 경우
			if (!board.getImageList().isEmpty()) {

				BoardImg thumbnail = null;

				// imageList의 0번인덱스 ==가장 빠른 순서(imgOrder)

				// 만약 이미지 목록의 첫번째 행의 순서가 0 == 썸네일 인 경우
				if (board.getImageList().get(0).getImgOrder() == 0) {
					thumbnail = board.getImageList().get(0);

				}

				model.addAttribute("thumbnail", thumbnail);
				model.addAttribute("start", thumbnail != null ? 1 : 0);

				System.out.println(model);

			}

		}
		return path;
	}

	@ResponseBody
	@GetMapping("memberList")
	public List<Member> memberList() {
		List<Member> memberList = service.memberList();

		return memberList;
	}

	@ResponseBody
	@GetMapping("resetPw")
	public int resetPw(@RequestParam("resetMemberNo") String resetMemberNo) {
		int memNo = service.resetPw(resetMemberNo);

		log.debug("asd" + memNo);

		return memNo;
	}
	
	
	/**게시글 좋아요 체크 / 해제 
	 * @param map
	 * @return count
	 */
	@ResponseBody
	@PostMapping("like")
	public int boardLike(@RequestBody Map<String, Integer> map) {
		return service.boardLike(map) ;
	}
	
	
}