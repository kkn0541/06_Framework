package edu.kh.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.demo.model.dto.Student;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller //요청 / 응답 제어 역할 명시 +bean 등록 
@RequestMapping("example") //example 로 시작하는 주소를 해당 컨트롤러에 매핑 

@Slf4j //lombok 라이브러리가 제공하는 로그 객체 자동생성 어노테이션 
public class ExampleController {

	/*Model 
	 *- Spring 에서 데이터 전달 역할을 하는 객체 
	 *
	 * - org.springframework.ui
	 *
	 * - 기본 scope : request 
	 * 
	 * -@SessionAttribute 와 함께 사용 시 session scope 변환
	 * 
	 * [기본 사용법]
	 * Model.addattribute("key", value); 
	 *
	 * 
	 * 
	 * 
	 * 
	 * */
	
	
	//a 태그 ->get요청 
	//  /example/ex1 GET 방식 요청을 매핑 
	@GetMapping("ex1")
	public String ex1(HttpServletRequest req,Model model) {
		
		//Servlet/jsp 내장 객체 (scope)
		//page <request <session <application
		
		
		//request scope 
		req.setAttribute("test1", "HttpServletRequst 로 전달한 값 ");
		model.addAttribute("test2","Model로 전달한 값 ");
		
		//단일 값 (숫자 , 문자열 ) Model을 이용해서 html로 전달 
		model.addAttribute("productName","종이컵");
		model.addAttribute("price",2000);
		
		//복수 값(배열 , List) Model을 이용해서 html로 전달 
		List<String> fruitList = new ArrayList<>();
		
		fruitList.add("사과");
		fruitList.add("딸기");
		fruitList.add("바나나");
		
		model.addAttribute("fruitList",fruitList);
	
		
		
		//dto 객체 Model을 이요해서 html로 전달 
		Student std = new Student();
		std.setStudentNo("1234");
		std.setName("홍길동");
		std.setAge(22);
		
		model.addAttribute("std",std);
				
	
		//List<Studnet> 객체 Model을 이요해서 html로 전달 
		List<Student> stdList = new ArrayList<>();
		
		stdList.add(new Student("1111","김일번",20));
		stdList.add(new Student("2222","최이번",23));
		stdList.add(new Student("3333","홍삼번",22));
		
		model.addAttribute("stdList",stdList);
		
		
		
		//templates/example/ex1.html 요청 위임 
		return"example/ex1";
	}
	
}
