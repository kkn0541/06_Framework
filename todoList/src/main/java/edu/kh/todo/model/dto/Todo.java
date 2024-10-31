package edu.kh.todo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

	private int todoNo; //할일 번호
	private String todoTitle; // 할일 제목
	private String todoContent; // 할일 내용
	private String complete; // 할일 완료 여부 ("y" "n")
	private String regDate; // 할 일 등록일
	
}
