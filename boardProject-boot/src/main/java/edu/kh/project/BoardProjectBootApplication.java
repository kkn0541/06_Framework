package edu.kh.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 스프링에서 제공하는 스케줄러를 이용학 위한 활성화 어노테이션 
@SpringBootApplication(exclude= {SecurityAutoConfiguration.class}) 
//spring security 에서 기본 제공하는 로그인페이지를 이용 안하겠다. 
//spring security 암호화 시 사용 
public class BoardProjectBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardProjectBootApplication.class, args);
		//특정 시기에 -> 오래된 로그를 삭제한다  -> 스프링에게 시킴 
		
		
	}

}
