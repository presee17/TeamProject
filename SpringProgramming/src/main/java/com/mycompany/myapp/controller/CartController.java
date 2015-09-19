package com.mycompany.myapp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.myapp.dto.Cart;
import com.mycompany.myapp.service.CartService;

@Controller
public class CartController {
	private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	private CartService cartService;
	@RequestMapping("/shoppingmall/cart/insert")
	public String insert(int productNo, int count, HttpSession session){
		logger.info("insert()");
		String memberId=(String) session.getAttribute("memberId");
		//카트 서비스에서 회원 아이디와 상품갯수,번호를 넣으면 장바구니에 추가하는 메서드 추가
		
		cartService.add(productNo, count, memberId);
		return "redirect:/shoppingmall/cart/cart";
	}
	
	@RequestMapping("/shoppingmall/cart/delete")
	public String delete(int cartNo, HttpSession session){
		logger.info("insert()");
		String memberId=(String) session.getAttribute("memberId");
	
		//카트 서비스에서 회원 아이디와 상품갯수,번호를 넣으면 장바구니에 추가하는 메서드 추가
		
		cartService.deleteOne(cartNo,memberId);
		return "redirect:shoppingmall/cart/cart";
	}
	
	
	@RequestMapping("/shoppingmall/cart/cart")
	public String list(HttpSession session, Model model) {

		String memberId = (String) session.getAttribute("memberId");
		// 현재 페이지 게시물 리스트
		List<Cart> list = cartService.getPage(memberId);
		// View로 넘길 데이터

		model.addAttribute("list", list);
		return "shoppingmall/cart/cart";
	}
}
