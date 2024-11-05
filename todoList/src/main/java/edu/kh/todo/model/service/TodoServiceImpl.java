package edu.kh.todo.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.todo.model.dao.TodoDAO;
import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.mapper.TodoMapper;

//@Transactional  
// -트랜잭션 처리를 수행하라고 지시하는 어노테이션 
// 정상 코드 수행 시 COMMIT 
// - 기본값 : Service 내부코드 수행 중 RuntimeException 발생 시 rollback  

//  (rollbackFor=Exception.class)   rollbackFor 속성 : 어떤 예외가 발생했을 떄 
//  rollback 할지 지정 


// 비즈니스 로직(데이터가공 , 트랜잭션 처리) 역할 명시 + bean 등록
@Transactional(rollbackFor=Exception.class)
@Service  
public class TodoServiceImpl implements TodoService{

	//@tododao가 @repository로 선언되있어서 객체생성안해도됨 
	
	@Autowired // TodoDAO와 같은 타입 Bean 의존성 주입(DI)
	private TodoDAO dao;
	
	@Autowired // TodoMapper 인터페이스를 상속받은 자식 객체 의존성 주입(DI)
	private TodoMapper mapper; // 자식 객체가 SQLSESSIONTEMPLATE 를 내부적으로 이용 
	
	
	
	//(TEST) todoNo가 1인 할일 제목 조회 
	@Override
	public String testTitle() {
		return dao.testTitle();
	}

	// 할일 목록 + 완료된 할일 갯수 조회 
	@Override
	public Map<String, Object> selectAll() {
		//1. 할일 목록 조회 매퍼 호출 
		List<Todo> todoList = mapper.selectAll();
		
		//2. 완료될 할일 개수 조회 매퍼 호출
		int completeCount = mapper.getCompleteCount();
		
		//3. 위 두개 결과값을 Map으로 묶어서 반환 
		Map<String, Object> map = new HashMap<>();
		
		map.put("todoList", todoList);
		map.put("completeCount", completeCount);
		
		//DAO 안씀!
		
		return map;
	}

	//할일 추가 
	@Override
	public int addTodo(String todoTitle, String todoContent) {

		//트랜잭션 제어 처리  -> @Transactional 어노테이션 
		
		// 마이바티스에서 sql 에 전달할수 있는 파라미터 개수는 오직 1개 
		
		// todoTitle , todoContent 를 Todo DTO 로 묶어서 전달 
		Todo todo = new Todo();
		todo.setTodoTitle(todoTitle);
		todo.setTodoContent(todoContent);
		
		
		
		return mapper.addTodo(todo);
	}

	@Override
	public Todo todoDetail(int todoNo) {
		// TODO Auto-generated method stub
		return mapper.todoDetail(todoNo);
	}

	//완료 여부 변경 
	@Override
	public int changeComplete(Todo todo) {
		// TODO Auto-generated method stub
		return mapper.changeComplete(todo);
	}

	@Override
	public Todo viewDetail(int todoNo) {
		// TODO Auto-generated method stub
		return mapper.viewDetail(todoNo);
	}

	@Override
	public int updateTodo(String todoTitle, String todoContent, int todoNo) {
		Todo todo = new Todo();
		todo.setTodoTitle(todoTitle);
		todo.setTodoContent(todoContent);
		todo.setTodoNo(todoNo);
		return mapper.updateTodo(todo);
	}

	@Override
	public int todoDelete(int todoNo) {
		
		return mapper.todoDelete(todoNo);
	}

	@Override
	public int getTotalCount() {

		return mapper.getTotalCount();
	}

	@Override
	public int getCompleteCount() {
		// TODO Auto-generated method stub
		return mapper.getCompleteCount();
	}

	
	
	//ajax 할떄 새로만든 메서드
	// return 은  기존에 사용했던 selectAll 가져옴 
	//할일 목록 조회 
	@Override
	public List<Todo> selectList() {
		return mapper.selectAll();
		
	}

	@Override
	public int updateTodo(Todo todo) {
		// TODO Auto-generated method stub
		 return mapper.updateTodo(todo);
	}

	

}
