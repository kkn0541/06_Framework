package edu.kh.project.myPage.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MyPageMapper {

	

	/** 회원정보 수정 
	 * @param inputMember
	 * @return result 
	 */
	int updateInfo(Member inputMember);

}