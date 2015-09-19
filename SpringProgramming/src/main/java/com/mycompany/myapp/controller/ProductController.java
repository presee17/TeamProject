package com.mycompany.myapp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mycompany.myapp.dto.Product;
import com.mycompany.myapp.service.ProductService;


@Controller
public class ProductController {
	@Autowired
	private ProductService productService;
	
	@RequestMapping("/shoppingmall/product/product")
	public String list(@RequestParam(defaultValue = "1") int pageNo, Model model, HttpSession session) {
		
		session.setAttribute("pageNo", pageNo);
		
		// 페이징을 위한 변수 선언
		int rowsPerPage = 10;
		int pagesPerGroup = 5;

		// 전체 게시물 수
		int totalProductNo = productService.getTotalProductNo();

		// 전체 페이지 수
		int totalPageNo = totalProductNo / rowsPerPage;
		if (totalProductNo % rowsPerPage != 0) {
			totalPageNo++;
		}

		// 전체 그룹 수
		int totalGroupNo = totalPageNo / pagesPerGroup;
		if (totalPageNo % pagesPerGroup != 0) {
			totalGroupNo++;
		}

		// 현재 그룹번호, 시작페이지번호, 끝페이지번호
		int groupNo = (pageNo - 1) / pagesPerGroup + 1;
		int startPageNo = (groupNo - 1) * pagesPerGroup + 1;
		int endPageNo = startPageNo + pagesPerGroup - 1;
		if (groupNo == totalGroupNo) {
			endPageNo = totalPageNo;
		}

		// 현재 페이지 게시물 리스트
		List<Product> list = productService.getPage(pageNo, rowsPerPage);

		// View로 넘길 데이터
		model.addAttribute("pagesPerGroup", pagesPerGroup);
		model.addAttribute("totalPageNo", totalPageNo);
		model.addAttribute("totalGroupNo", totalGroupNo);
		model.addAttribute("groupNo", groupNo);
		model.addAttribute("startPageNo", startPageNo);
		model.addAttribute("endPageNo", endPageNo);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("list", list);

		return "shoppingmall/product/product";
	}
	
	@RequestMapping("/shoppingmall/product/detail")
	public String productDetail(int productNo,Model model) {
		Product product = productService.getProduct(productNo);
		model.addAttribute("product",product);
		return "shoppingmall/product/detail";
	}
}
