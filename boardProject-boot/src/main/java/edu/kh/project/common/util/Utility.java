package edu.kh.project.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

//프로그램 전체적으로 사용한 유능한 기능 모음 
public class Utility {
	
	//스태틱메서에서 사용하려면 
	//필드도 static
	
	public static int seqNum=1; //1~9999반복 
	
	
	
	public static String fileRename(String originalFileName) {
		
		// 2024111200105_00004.jpg
		
		// SimpleDateFormat :시간을 원하는 형태의 문자열로 간단히 변경 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		// java.util.Data() :현재 시간을 저장한 자바 객체 
		String date = sdf.format(new Date());
		
		// 0000d  빈자리는 sequNum으로 
		String number = String.format("%05d", seqNum);
	
		seqNum++; //1증가 
		if(seqNum == 100000) {
			seqNum=1;
		}
		
		// 확장자 구하기 
		// "문자열.substring(인덱스)"
		// -문자열을 인덱스부터 끝까지 잘라낸 결과를 반환 
		
		// "문자열".langIndexof(".")
		// -문자열에서 마지막 "."의 인덱스를 반환 
		
		String ext = 
				originalFileName.substring(originalFileName.lastIndexOf("."));
		
		//originalFileName ==짱구.jpg
		// ext == .jpg 
		
		
		return date +"_"+number+ext;
		
		
	}
}
