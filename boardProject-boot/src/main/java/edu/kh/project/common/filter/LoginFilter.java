package edu.kh.project.common.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/*
 * Filter :요청 , 응답 시 컬러내거나 추가 할수 있는 객체 
 * 
 * [필터 클래스 생성 방법]
 * 1. jakarta.servlet.Filter 인터페이스 상속 받기 
 * 2. doFilter() 메서드 오버라이딩 
 * 
 * 
 * */

// 로그인이 되어있지 않은 경우 특정 페이지로 돌아가게함 
public class LoginFilter implements Filter {

	//펄터 동작을 정의하는 메서드 
	@Override
	public void doFilter(ServletRequest request,
			ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		//servletRequest : HttpservletRequest 의 부모타입
		//servletResponse : HttpservletResponse 의 부모타입

		// Session 필요함 -> 왜 -> loginMember session에 담김 
		
		// Http 통신이 가능한 형태로 (자식형태) 다운캐스팅 
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		//Session 얻어오기 
		HttpSession session = req.getSession();
		
		// 세션에서 로그인한 회원 정보를 얻어옴
		// 얻어왔으나 , 없을때 -> 로그인이 되어있지 않은 상태
		if(session.getAttribute("loginMember")== null) {
			
			// loginError 재요청 
			// resp를 이용해서 원하는 곳으로 리다이렉트 
			resp.sendRedirect("/loginError");
			
			
		}else {
			// 로그인이 되어 있는 경우 
			
			// FilterChain 
			// - 다음 필터 또는 Dispatcher Servlet 과 연결되 객체 
			
			// 다음 필터로 요청 / 응답 객체 전달 
			// 만약에 다음 필터가 없으면 Dispathcher Servlet 으로 request, response 전달 
			chain.doFilter(request, response);
			
		}
		
		
		
		
		
	}

	
	
	
}