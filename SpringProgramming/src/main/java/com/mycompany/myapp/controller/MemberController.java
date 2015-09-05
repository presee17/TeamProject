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
		return "shopping/member/member";
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
			url = "main";
			session.setAttribute("id", member.getId());
			break;
		}
		return url;
	}

	@RequestMapping("/shoppingmall/member/loginfail")
	public String loginfail(String state){
		logger.info("joinForm()"+state);
		return "shoppingmall/member/loginfail";
	}
	
	@RequestMapping("shoppingmall/member/joinForm")
	public String joinForm() {
		logger.info("joinForm()");
		return "shoppingmall/member/joinForm";
	}

	@RequestMapping("/shoppingmall/member/join")
	public String join(Member member) {
		logger.info("join()");
		String url = "";
		boolean possibleJoin = memberService.join(member);
		if (possibleJoin) {
			url="redirect:/shoppingmall/member/menu";
		} else {
			url ="redirect:/shoppingmall/member/join?state=false";
		}
		return url;
	}
}
