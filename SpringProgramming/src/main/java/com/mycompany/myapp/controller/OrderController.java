package com.mycompany.myapp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mycompany.myapp.dto.Order;
import com.mycompany.myapp.dto.OrderItem;
import com.mycompany.myapp.service.OrderItemService;
import com.mycompany.myapp.service.OrderService;

@Controller
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private OrderService orderService;
	private OrderItemService orderitemService;
	
	
	//주문 버튼을 누르면 실행
	@RequestMapping("/shoppingmall/order/insert")
	public String order(@RequestParam(defaultValue = "1") int pageNo, 
			Model model, HttpSession session, int[] cartNo) {
		
		
		// 페이징을 위한 변수 선언
		int rowsPerPage = 10;
		int pagesPerGroup = 5;
		
		// 전체 주문 수
		int totalOrderNo = orderService.getTotalOrderNo();

		// 전체 페이지 수
		int totalPageNo = totalOrderNo / rowsPerPage;
		if (totalOrderNo % rowsPerPage != 0) {
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
		
		// 세션에서 아이디, 체크한 상품 cartNo 받기
		String memberId = (String) session.getAttribute("memberId");
		for(int i=0; i<cartNo.length;i++){
			orderService.cartToOrder(memberId,cartNo[i]);
			
		}
				
		OrderItem orderitem =new OrderItem ();
		int orderNo = orderitem.getOrderNo();
		
		//현재 주문 내역
		List<OrderItem> list =  orderitemService.getPage(orderNo, pageNo, rowsPerPage);
		
		// View로 넘길 데이터
		model.addAttribute("pagesPerGroup", pagesPerGroup);
		model.addAttribute("totalPageNo", totalPageNo);
		model.addAttribute("totalGroupNo", totalGroupNo);
		model.addAttribute("groupNo", groupNo);
		model.addAttribute("startPageNo", startPageNo);
		model.addAttribute("endPageNo", endPageNo);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("list", list);
		
		return "redirect:/shoppingmall/order/orderresult";
	}
	
	//해당 ID에 해당하는 주문 리스트를 페이징 처리해서 보여줌
	@RequestMapping("/shoppingmall/order/orderlist")
	public String list(@RequestParam(defaultValue = "1") int pageNo, Model model, HttpSession session) {
		
		//데이터 넘어왔는지 확인
		logger.info("pageNo:"+ pageNo);
				
		//현재 페이지 session에 저장
		session.setAttribute("pageNo", pageNo);
		
		// 페이징을 위한 변수 선언
		int rowsPerPage = 10;
		int pagesPerGroup = 5;
		
		// 전체 주문 수
		int totalOrderNo = orderService.getTotalOrderNo();

		// 전체 페이지 수
		int totalPageNo = totalOrderNo / rowsPerPage;
		if (totalOrderNo % rowsPerPage != 0) {
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
		
		String memberId = (String) session.getAttribute("memberId");
		
		// 현재 주문 리스트
		List<Order> list = orderService.showOrder(memberId, pageNo, rowsPerPage);

		// View로 넘길 데이터
		model.addAttribute("pagesPerGroup", pagesPerGroup);
		model.addAttribute("totalPageNo", totalPageNo);
		model.addAttribute("totalGroupNo", totalGroupNo);
		model.addAttribute("groupNo", groupNo);
		model.addAttribute("startPageNo", startPageNo);
		model.addAttribute("endPageNo", endPageNo);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("list", list);

		return "/shoppingmall/order/orderlist";
	}
}
