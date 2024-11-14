package edu.kh.project.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.PageNation;
import edu.kh.project.board.model.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;





@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor


public class BoardServiceImpl implements BoardService{

	private final BoardMapper mapper;

	//게시판 종류 조회 
	@Override
	public List<Map<String, Object>> selectBoardTypeList() {
		return mapper.selectBoardTypeList();
		
	}

	// 특정게시판의 지정된 페이지 목록 조회 
	@Override
	public Map<String, Object> selectBoardList(int boardCode, int cp) {

		//1. 지정된 게시판(boardCode)에서 
		// 삭제되지 않은 게시글 수를 조회 
		int listCount = mapper.getListCount(boardCode);
		
		// 2. 1번의 결과 +cp를 이용해서 
		// Pagination 객체를 생성 
		// *Pagination 객체 : 게시글 목록 구성에 필요한 값을 저장한 객체 
		PageNation pagination = new PageNation(cp, listCount);
		
		
		
		// 3. 특정 게시판의 지정된 페이지 목록 조회 
		/* ROWBOUNDS 객체(MyBatis 제공 객체) 
		 * -지정된 크기 만큼 건너뛰고 (offset)
		 * 제한된 크기만큼(limit의 행을 조회하는 객체 
		 * 
		 * ->페이징 처리가 굉장히 간단해짐 
		 * 
		 * 
		 * */
		int limit = pagination.getLimit();
		int offset= (cp-1)* limit;
		RowBounds rowBounds = new RowBounds(offset,limit);
		
		//Mapper 메서드 호출 시 원래 전달 할수 있는 매개변수 1개 
		// -> 2개를 전달할수 있는 경우가 있음 
		// RowBounds 를 이용할때 
		// -> 첫번째 매개변수 -> sql 에 전달할 파라미터 
		// -> 두번째 매개변수 -> RowBounds 객체 전달 
		List<Board> boardList = mapper.selectBoardList(boardCode,rowBounds);
		
		
		// 4. 목록조회 결과 +Pagination 객체를 Map으로 묶음 
		Map<String, Object> map =new HashMap<>();
		
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		
		
		// 5. 결과 반환 
		return map;
	}
	
}
