package edu.kh.todo.model.service;

import java.util.Map;

import edu.kh.todo.model.dto.Todo;

public interface TodoService {

	/**(TEST)todoNo가 1인 할일 제목 조회 
	 * @return title 
	 */
	String testTitle();

	/** 할일 목록 + 완료된 할일 갯수 조회 
	 * @return map
	 */
	Map<String, Object> selectAll();

	/** 할 일 추가 
	 * @param todoTitle
	 * @param todoContent
	 * @return result
	 */
	int addTodo(String todoTitle, String todoContent);

	/** 할일 상세 조회 
	 * @param todoNo
	 * @return todo 
	 */
	Todo todoDetail(int todoNo);

	/** 완료 여부 변경 
	 * @param todo
	 * @return
	 */
	int changeComplete(Todo todo);

	
	

	/** 수정페이지로 이동 
	 * @param todoNo
	 * @return
	 */
	Todo viewDetail(int todoNo);

	
	
	/**업데이트
	 * @param todoTitle
	 * @param todoContent
	 * @param todoNo 
	 * @return
	 */
	int updateTodo(String todoTitle, String todoContent, int todoNo);

	
	
	/**삭제
	 * @param todoNo
	 * @return
	 */
	int todoDelete(int todoNo);



	
}
