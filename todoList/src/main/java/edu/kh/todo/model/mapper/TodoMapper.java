package edu.kh.todo.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todo.model.dto.Todo;

/*
 * @Mapper
 * -Mybatis 제공 어노테이션 
 * 
 * -Mybatis 가 해당 인터페이스를 DAO로 인식하여 sql 매핑을 처리 
 * 
 * -해당 어노테이션이 작성된 인터페이스는 
 *  namespace에 해당 인터페이스가 작성된 
 *  mapper.xml과 연결되어 sql 호출/수행/결과 반환 가능 
 *  
 * 
 * -Mybatis 에서 제공하는 mapper 상속 개체가 bean 으로 등록됨 
 * 
 * -@Mapper 를 사용할때 @Repository 는 필요하지 않음.
 * 
 *  -> Mybatis가 DAO 객체로 Mapper 인터페이스를 관리하기 때문.
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * */



@Mapper
public interface TodoMapper {

	/*
	 * Mapper의 메서드명 == mapper.xml 파일 내 태그의 id 
	 * 
	 * (메서드명과 id가 같은 태그가 서로 연결된다)
	 * 
	 * 
	 * */
	
	
	
	
	/**(TEST)todoNo가 1인 할일 제목 조회 
	 * @return
	 */
	//todo-mapper.xml에 id와 메서드명일치해야함 
	String testTitle();

	/**할일 목록 조회
	 * @return todoList
	 */
	List<Todo> selectAll();
	
	
	/** 완료된 할일 개수 조회
	 * @return completeCount
	 */
	int getCompleteCount();
	
	
	/** 할 일 추가
	 * @param todo
	 * @return result
	 */
	int addTodo(Todo todo);
	
}
