package edu.kh.test.member.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.test.member.model.dto.Member;
import edu.kh.test.member.model.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("member")
@Slf4j
public class MemberController {
	
	private final MemberService service;
	
	
	@ResponseBody
	@RequestMapping("selectAllList")
	public List<Member> selectAllList() {
		
		List<Member> memberList = service.selectAllList();
		
		System.out.println(memberList);
		
		return memberList;
	}

	
}
