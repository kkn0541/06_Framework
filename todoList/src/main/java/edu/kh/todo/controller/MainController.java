package edu.kh.todo.controller;

import java.net.http.HttpRequest;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j //로그 객체 자동 생성
@Controller //요청 / 응답 제어 역할 명시 + Bean 등록 
public class MainController {

	//이제 객체 생성안해도됨 
	//private TodoService service = new TodoService() ;
	
	
	@Autowired // 등록된 bean 중 같은 타입이거나 상속관계 DI (의존성 주입)
	private TodoService service;
	
	@RequestMapping("/")
	public String mainPage(Model model) {
		//edu.kh.todo.model.service.TodoServiceImpl@3b090a6e
		log.debug("service :"+ service);
		
		// todoNo가 1인 todo의 제목 조회하여 request scope에 추가 
		String testTitle = service.testTitle();
		
		//                     key         value
		model.addAttribute("testTitle",testTitle);
		//model은 request scope
		
		//--------------------------------------------------------------
		
		//TB_TODO 테이블에 저장된 전체 할일 목록 조회하기 (LIST)
		// + 완료된 할일 갯수 (INT) 
		// list, int 받아와야 해서 object로 
		
		// Service 메서드 호출 후 결과 반환 받기 
		Map<String, Object> map =service.selectAll();
		
		//map에 담긴 내용 추출
		// 다운캐스팅  --- 
		List<Todo> todoList =(List<Todo>)map.get("todoList");
		int completeCount =(int)map.get("completeCount");
		
		//model을 이용 
		model.addAttribute("todoList",todoList);
		model.addAttribute("completeCount",completeCount);
		
		
		//접두사 :class:path:/templates/
		//접미사 : .html 
		return "common/main"; //forward 
		
	}
	
	@GetMapping("qnadetail")
	public String qnadetail() {
		
		return "common/qnadetail";
	}
	@GetMapping("often")
	public String oftenqna() {
		
		return "common/oftenqna";
	}

	@GetMapping("signup")
	public String signup() {
		
		return "common/signup";
	}
	
	
	@GetMapping("signup8")
	public String signup8() {
		
		return "common/signup8";
	}
	
	
	
	
	
	
	

	
	@GetMapping("login")
	public String loginpopup() {
		
		return "common/login";
	}
	

	@GetMapping("oftenqnadetail")
	public String oftenqnadetail() {
		
		return "common/oftenqnadetail";
	}
	
	@GetMapping("adoptmain")
	public String adoptmain() {
		
		return "common/adoptmain";
	}
	
	@GetMapping("aboutus")
	public String aboutus() {
		
		return "common/aboutus";
	}
	@GetMapping("mainhtml")
	public String mainhtml() {
		
		return "common/mainhtml";
	}
	
	
	@GetMapping("mainmenu")
	public String mainmenu() {
		
		return "common/mainmenu";
	}
	
	@GetMapping("oftenqnadetail2")
	public String oftenqnadetail2() {
		
		return "common/oftenqnadetail2";
	}
	@GetMapping("mypagelist")
	public String mypagelist() {
		
		return "common/mypagelist";
	}
	@GetMapping("mypagelist4")
	public String mypagelist2() {
		
		return "common/mypagelist4";
	}
	
	@GetMapping("teamintro")
	public String teamintro() {
		
		return "common/teamintro";
	}

	@GetMapping("mypagedetail")
	public String mypagedetail() {
		
		return "common/mypagedetail";
	}
	@GetMapping("mainadopt")
	public String mainadopt() {
		
		return "common/mainadopt";
	}
	
	@GetMapping("adoptintro")
	public String adoptintro() {
		
		return "common/adoptintro";
	}


	@GetMapping("mainreviewadopt")
	public String mainreviewadopt() {
		
		return "common/mainreviewadopt";
	}
	@GetMapping("aboutsemi")
	public String aboutsemi() {
		
		return "common/aboutsemi";
	}
	@GetMapping("aboutuspage")
	public String aboutuspage() {
		
		return "common/aboutuspage";
	}
	
	@GetMapping("us")
	public String us() {
		
		return "common/us";
	}
	@GetMapping("us2")
	public String us2() {
		
		return "common/us2";
	}
}
