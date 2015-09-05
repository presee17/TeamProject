package com.mycompany.myapp.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.myapp.dto.Member;
import com.mycompany.myapp.service.MemberService;

@Controller
public class MemberController {
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	@Autowired
	private MemberService memberService;

	@RequestMapping("/shoppingmall/member/member")
	public String loginForm() {
		logger.info("loginForm()");
		return "shoppingmall/member/member";
	}

	@RequestMapping("/shoppingmall/member/login")
	public String login(Member member, HttpSession session) {
		logger.info("login()");
		String state = memberService.login(member);
		String url = "";
		switch (state) {
		case "noId":
			url = "redirect:/shoppingmall/member/loginfail?state=" + state;
			break;
		case "wrongPw":
			url = "redirect:/shoppingmall/member/loginfail?state=" + state;
			break;
		case "correct":
			url = "shoppingmall/main";
			session.setAttribute("memberId", member.getId());
			break;
		}
		return url;
	}

	@RequestMapping("/shoppingmall/member/loginfail")
	public String loginfail(String state){
		logger.info("joinForm()"+state);
		return "shoppingmall/member/loginfail";
	}
	
	@RequestMapping("shoppingmall/member/joinform")
	public String joinForm() {
		logger.info("joinForm()");
		return "shoppingmall/member/joinform";
	}

	@RequestMapping("/shoppingmall/member/join")
	public String join(Member member) {
		logger.info("join()");
		String url = "";
		boolean possibleJoin = memberService.join(member);
		if (possibleJoin) {
			logger.info("회원가입 성공");
			url="redirect:/shoppingmall/member/member";
		} else {
			logger.info("회원가입 실패");
			url ="redirect:/shoppingmall/member/joinform";
		}
		return url;
	}
}
