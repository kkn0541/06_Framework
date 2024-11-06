package edu.kh.project.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.model.dto.Member;

@Mapper
public interface MemberMapper {

	
	
	/** 로그인 sql 실행
	 * @param memberEmail
	 * @return loginMember
	 */
	Member login(String memberEmail);

}
