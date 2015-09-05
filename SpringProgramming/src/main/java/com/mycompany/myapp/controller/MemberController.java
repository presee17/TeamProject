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

	@RequestMapping("/member/member")
	public String loginForm() {
		logger.info("loginForm()");
		return "member/member";
	}

	@RequestMapping("/member/login")
	public String login(Member member, HttpSession session) {
		logger.info("login()");
		String state = memberService.login(member);
		String url = "";
		switch (state) {
		case "noId":
			url = "redirect:/member/loginfail?state=" + state;
			break;
		case "wrongPw":
			url = "redirect:/member/loginfail?state=" + state;
			break;
		case "correct":
			url = "member/main";
			session.setAttribute("id", member.getId());
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
	public String join(Member member) {
		logger.info("join()");
		String url = "";
		boolean possibleJoin = memberService.join(member);
		if (possibleJoin) {
			url="redirect:member/menu";
		} else {
			url ="redirect:member/join?state=false";
		}
		return url;
	}
}
