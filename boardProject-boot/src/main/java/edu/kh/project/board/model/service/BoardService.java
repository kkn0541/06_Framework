package edu.kh.project.board.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.member.model.dto.Member;

public interface BoardService {

	/** 게시판 종류 조회
	 * @return boardTypeList
	 */
	List<Map<String, Object>> selectBoardTypeList();

	
	
	/** 특정 게시판의 지정된 페이지 목록 조회 서비스 
	 * @param boardCode
	 * @param cp
	 * @return
	 */
	Map<String, Object> selectBoardList(int boardCode, int cp);



	/**게시글 상세조회
	 * @param map
	 * @return board
	 */
	Board selectOne(Map<String, Integer> map);



	/**비동기 목록조회 
	 * @return
	 */
	
	List<Member> memberList();


	

	/** 비동기 회원번호 초기화 
	 * @param resetMemberNo
	 * @return
	 */
	int resetPw(String resetMemberNo);




}
