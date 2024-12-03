package edu.kh.project.common.aop;

import org.aspectj.lang.annotation.Pointcut;

//PointCut :실제 advice 가 적용될 지점 
//PointCut 모아두는 클래스 


public class PointCutBundle {

	//작성하기 어려운 PointCut 을 미리 작성해두고 
	// 필요한 곳에서 클래스명.메서드명()으로 호출해서 사용 가능 
	
	
	@Pointcut("execution(* edu.kh.project..*Controller*.*(..))")
	public void controllerPointCut() {
		
		
	}
	@Pointcut("execution(* edu.kh.project..*ServiceImpl*.*(..))")
	public void serviceImplPointCut() {
		
	}
}