package com.mycompany.myapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.dao.MemberDao;
import com.mycompany.myapp.dto.Member;

@Component
public class MemberService {

	@Autowired
	private MemberDao memberDao;

	// 회원가입
	public boolean join(Member member) {
		boolean join;
		if (memberDao.selectNoById(member.getId()) == null) {
			join = true;
			memberDao.insert(member);
		} else {
			join = false;
		}
		return join;
	}

	// 로그인
	public String login(Member member) {
		String state = "";
		if(memberDao.selectNoById(member.getId()) == null){
			state = "noId";
		}else{
			if(memberDao.selectById(member.getId()).getPw()!=member.getPw()){
				state = "wrongPw";
			} else{
				state = "correct";
			}
		}
		return state;
	}
}
