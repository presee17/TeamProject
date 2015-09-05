package com.mycompany.myapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	@RequestMapping("/shoppingmall/main")
	public String cart() {
		return "shoppingmall/cart/cart";
	}
	
}
