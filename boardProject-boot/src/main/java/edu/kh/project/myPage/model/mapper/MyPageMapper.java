package edu.kh.project.myPage.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper
public interface MyPageMapper {

	/**
	 * 회원정보 수정
	 * 
	 * @param inputMember
	 * @return result
	 */
	int updateInfo(Member inputMember);

	/**
	 * @param memberNo
	 * @return 암호화된 비밀번호 
	 */
	String selectPw(int memberNo);

	
	
	/**비밀번호 변경 
	 * @param paramMap
	 * @return
	 */
	int changePw(Map<String, Object> paramMap);

	
	
	
	/**회원탈퇴 
	 * @param memberNo
	 * @return result 
	 */
	int secession(int memberNo);

}
