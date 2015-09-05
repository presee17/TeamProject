package com.mycompany.myapp.controller;

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

	@RequestMapping("/shoppingmall/member")
	public String loginForm() {
		logger.info("loginForm()");
		return "shoppingmall/member";
	}

	@RequestMapping("/shoppingmall/login")
	public String login(Member member) {
		logger.info("login()");
		String state = memberService.login(member);
		String url = "";
		switch (state) {
		case "noId":
			url = "redirect:member/login?state=" + state;
			break;
		case "wrongPw":
			url = "redirect:member/login?state=" + state;
			break;
		case "correct":
			url = "member/menu";
			break;
		}
		return url;
	}

	@RequestMapping("/shoppingmall/joinForm")
	public String joinForm() {
		logger.info("joinForm()");
		return "shoppingmall/joinForm";
	}

	@RequestMapping("/shoppingmall/join")
	public String join(Member member) {
		logger.info("join()");
		String url = "";
		boolean possibleJoin = memberService.join(member);
		if (possibleJoin) {
			url="redirect:shoppingmall/menu";
		} else {
			url ="redirect:shoppingmall/join?state=false";
		}
		return url;
	}
}
