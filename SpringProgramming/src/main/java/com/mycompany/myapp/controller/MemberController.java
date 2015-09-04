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

	@RequestMapping("/member/loginForm")
	public String loginForm() {
		logger.info("loginForm()");
		return "member/loginForm";
	}

	@RequestMapping("/member/login")
	public String login(Member member, Model model) {
		logger.info("login()");
		String state = memberService.login(member);
		String url ="";		
		switch (state) {
		case "noId":
			url = "redirect:/member/login?state="+state;
			break;
		case "wrongPw":
			url = "redirect:/member/login?state="+state;
			break;
		case "correct":
			url = "member/menu";
			break;
		}
		return url;
	}

	@RequestMapping("/member/joinForm")
	public String joinForm() {
		logger.info("joinForm()");
		return "member/joinForm";
	}

	@RequestMapping("/member/join")
	public String join() {
		logger.info("joinForm()");
		return "member/join";
	}
}
