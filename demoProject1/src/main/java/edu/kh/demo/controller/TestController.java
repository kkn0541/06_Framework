package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



//java 객체  :  new 연산자에 의해 heap 영역에 
//				클래스에 작성된 내용대로 생성된 것 

// instatnce : 개발자가 직접 만들고 , 관리하는 객체 

//Bean : Spring Container가 만들고 , 관리하는 객체 


//@RequestMapping("/todo")
@Controller // 요청 / 응답을 제어할 컨트롤러 역할임을 명시 
			// +Bean 등록 
public class TestController {
	
	//기존 servlet : 클래스 단위로 하나의 요청(==요청주소)만 처리 가능 
	//Spring : 메서드 단위로 요청 처리 가능 
	
	
	//@RequestMapping("요청주소")
	// - 요청 주소를 처리할 메서드를 매핑하는 어노테이션 
	
	//1) 메서드에 작성 :
	//- 요청 주소와 해당 메서드를 매핑 
	// -Get/ post 가리지 않고 매핑 (속성을 통해서 지정 가능 or 다른 어노테이션을 이용해서 가능)
/*		
	@RequestMapping(value="/hello",method=RequestMethod.POST) //  /hello라는 요청이오면 아래 메서드 실행
	public String hello() {
		// 메서드 로직 
				
		return"경로..";
		
	}
	
*/
	
	//2) 클래스에 작성 
	// - 공통 주소를 매핑 
	//ex) /todo/insert, todo/select, todo/update ...
	
/*	
	//@RequestMapping("/todo")
	public class 클래스명{
	
	       				/todo/insert  매핑
		@RequestMapping("/insert")
		public String 메서드명() {}
		
	 	 				/todo/select 매핑
		@RequestMapping("/select")
		public String 메서드명() {}
	
 						/todo/update 매핑  
		@RequestMapping("/update")
		public String 메서드명() {}
		
	
	}
	*/

	@RequestMapping("/test") // test요청시 처리할 메서드 매핑 
	public String testMethod() {
		
		System.out.println("/test 요청 받음");
		
		/*
		 * Controller 메서드의 반환형이 String인 이유 
		 * - 메서드에서 반환되는 문자열이 
		 *  forward 할 html 파일의 경로가 되기 때문! 
		 *  
		 *  Thymeleaf : jsp 대신 사용하는 탬플릿 엔진(html 형태) 
		 *  
		 *  classpath == src/main/resources/templates
		 *  
		 *  접두사 : classpath: /templates /   (prefix)
		 *  접미사 : .html  (suffix)   
		 *  
		 * 
		 * */
		
		// src/main/resources/templates/test.html
		return "test";  //접두사 +반환값 + 접미사 경로의 html로 forward
		
		/* 접두사 , 접미사 ,forward 설정은 viewResolver 객체가 담당 */
		
	}

	



}
