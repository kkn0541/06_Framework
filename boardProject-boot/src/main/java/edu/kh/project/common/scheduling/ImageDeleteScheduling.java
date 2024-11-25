package edu.kh.project.common.scheduling;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import edu.kh.project.board.model.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component // Bean 등록 
@PropertySource("classpath:/config.properties")
@RequiredArgsConstructor
public class ImageDeleteScheduling {

	
	private final BoardService service;
	
	
	//회원프로필 이미지 파일 저장 경로
	@Value("${my.profile.folder-path}")
	private String profileFolderPath; //C:/uploadFiles/profile/
	
	@Value("${my.board.foler-path}")
	//게시판 이미지 파일 저장경로 
	private String boardFolderPath; //C:/uploadFiles/board/
	
	
	//cron ="초 분 시 일 월 요일 [년도]" -요일 :1(sun)~7(SAT)
	
	
	
//	@Scheduled(cron = "0,30 * * * * *") //시계 초 단위가 0,30 인 경우 수행
//	@Scheduled(cron = "0,15,30,45 * * * * *") // 시계 초 단위가 0,15,30,45인 경우 수행
//  @Scheduled(cron = "0 0  * * * *") // 매 시간(정시마다 수행)
//  @Scheduled(cron = "0 0 0 * * *") //자정
//  @Scheduled(cron = "0 0 12 * * *") //정오
  @Scheduled(cron = "0 0 0 1 * *") //매달 1일 
	
	public void scheduling() {
		log.info("스케줄러 동작!");
		
		//DB , 서버 파일 목록 비교 후 DB에 없는 
		// 서버 이미지 파일 삭제 동작 
		
		
		//1. 서버 파일 목록 조회하기 
		File boardFolder = new File(boardFolderPath); // uploadfile board 폴더 참조
		File memberFolder = new File(profileFolderPath); //uploadfile profile 폴더 참조 
		
		//참조하는 폴더에 존재하는 파일목록 얻어오기 
		File[] boardArr = boardFolder.listFiles();
		File[] memberArr = memberFolder.listFiles();
		
		// 두 배열을 하나로 합침 
		// imageArr 라는 빈 배열을 boardArr 와 memberArr 길이 만큼의 크기로 만든다. 
		File[] imageArr = new File[boardArr.length+ memberArr.length];
		
		//배열 내용 복사 (깊은 복사) 
		// System.arraycopy(복사할배열 , 몇번인덱스부터 복사할지 ,
		//새로운배열 , 새로운배열의 몇번부터 넣을지 인덱스 , 복사를 어디까지 할건지)
		System.arraycopy(memberArr, 0, imageArr, 0, memberArr.length);
		System.arraycopy(boardArr, 0, imageArr,memberArr.length,boardArr.length);
		
		//배열 -> 리스트 변환 (다루기 쉽도록 ) 
		List<File> serverImageList= Arrays.asList(imageArr);
		//imageArr은 일반 list (컬렉션으로 변환되었음)
		
		//2. DB이미지 파일 이름만 모두 조회 
		List<String> dbImageList = service.selectDbImageList();
	
		//3. 서버 , db 이미지 파일명을 비교하여 
		// 서버에만 있고 db에 없는 파일을 서버에서 삭제 
		if(!serverImageList.isEmpty()) {//서버에 이미지가 있을경우에만 
	
			for(File serverImage:serverImageList) {
				
				//File.getName(): 서버 파일 이름 
				
				//List.indexof(객체) : 
				//List에 객체가 존재하면 존재하는 index 반환
				//존재하지 않으면 -1반환하는 메서드 
				if(dbImageList.indexOf(serverImage.getName())== -1) {
					log.info(serverImage.getName()+"  삭제");
					serverImage.delete(); //파일삭제 
				}
			}
		}
	}
	
}

/*
* @Scheduled
*
* * Spring에서 제공하는 스케줄러 : 시간에 따른 특정 작업(Job)의 순서를 지정하는 방법.
*
* 설정 방법
* 1) XXXAPPlication.java 파일에 @EnableScheduling 어노테이션 추가
* 2) 스케쥴링 동작을 위한 클래스 작성
*
*
* @Scheduled 속성
*  - fixedDelay : 이전 작업이 끝난 시점으로 부터 고정된 시간(ms)을 설정.
*    @Scheduled(fixedDelay = 10000) // 이전 작업이 끝난 후 10초 뒤에 실행
*   
*  - fixedRate : 이전 작업이 수행되기 시작한 시점으로 부터 고정된 시간(ms)을 설정.
*    @Scheduled(fixedRate  = 10000) // 이전 작업이 시작된 후 10초 뒤에 실행
*  
*   
*   
* * cron 속성 : UNIX계열 잡 스케쥴러 표현식으로 작성 - cron="초 분 시 일 월 요일 [년도]" - 요일 : 1(SUN) ~ 7(SAT)
* ex) 2019년 9월 16일 월요일 10시 30분 20초 cron="20 30 10 16 9 2 " // 연도 생략 가능
*
* - 특수문자
* * : 모든 수.
* - : 두 수 사이의 값. ex) 10-15 -> 10이상 15이하
* , : 특정 값 지정. ex) 3,4,7 -> 3,4,7 지정
* / : 값의 증가. ex) 0/5 -> 0부터 시작하여 5마다
* ? : 특별한 값이 없음. (월, 요일만 해당)
* L : 마지막. (월, 요일만 해당)
* @Scheduled(cron="0 * * * * *") // 모든 0초 마다 -> 매 분마다 실행
*
*/