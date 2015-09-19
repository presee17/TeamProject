package com.mycompany.myapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.myapp.dto.Cart;
import com.mycompany.myapp.service.CartService;

@Controller
public class CartController {

	@Autowired
	private CartService cartService;
	
	@RequestMapping("/shoppingmall/cart/cart")
	public String list(String memberId, Model model) {
			
		// 현재 페이지 게시물 리스트
		List<Cart> list = cartService.getPage(memberId);

		// View로 넘길 데이터
		model.addAttribute("list", list);

		return "shoppingmall/cart/cart";
	}

}
