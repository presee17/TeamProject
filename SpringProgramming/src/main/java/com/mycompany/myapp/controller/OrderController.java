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
import com.mycompany.myapp.service.OrderService;

@Controller
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private OrderService orderService;

	@RequestMapping("/order/list")
	public String list(@RequestParam(defaultValue = "1") int pageNo, Model model, HttpSession session) {
		logger.info("pageNo: "+pageNo);
		
		session.setAttribute("pageNo", pageNo);
		
		// 페이징을 위한 변수 선언
		int rowsPerPage = 10;
		int pagesPerGroup = 5;
		String memberId = null;
		
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

		// 현재 페이지 게시물 리스트
		List<Order> list = orderService.getPage(memberId, pageNo, rowsPerPage);

		// View로 넘길 데이터
		model.addAttribute("pagesPerGroup", pagesPerGroup);
		model.addAttribute("totalPageNo", totalPageNo);
		model.addAttribute("totalGroupNo", totalGroupNo);
		model.addAttribute("groupNo", groupNo);
		model.addAttribute("startPageNo", startPageNo);
		model.addAttribute("endPageNo", endPageNo);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("list", list);

		return "order/list";
	}

	/*@RequestMapping("/order/writeForm")
	public String writeForm() {
		logger.info("writeForm()");
		return "order/writeForm";
	}

	@RequestMapping("/order/updateForm")
	public String updateForm(int orderNo,Model model) {
		Order order=orderService.getOrder(orderNo);
		model.addAttribute("order",order);
		return "order/updateForm";
	}

	@RequestMapping("/order/write")
	public String write(Order order,HttpSession session){
		logger.info("write()");
	
		//파일 정보 얻기
		ServletContext application=session.getServletContext();
		String dirPath=application.getRealPath("/resources/uploadfiles");
		String originalFilename=order.getAttach().getOriginalFilename();
		String filesystemName=System.currentTimeMillis()+"-"+originalFilename;
		String contentType=order.getAttach().getContentType();
	
		if(!order.getAttach().isEmpty()){
			//파일에 저장하기
			try {
				order.getAttach().transferTo(new File(dirPath+"/"+filesystemName));
			}catch (Exception e) {e.printStackTrace();}
		}
		//데이터베이스에 게시물 정보 저장

		if(!order.getAttach().isEmpty()){
			order.setOriginalFileName(originalFilename);
			order.setFilesystemName(filesystemName);
			order.setContentType(contentType);
		}
		orderService.add(order);

		return "redirect:/order/list";
	}

	@RequestMapping("/order/update")
	public String update(Order order) {
		orderService.modify(order);
		return "redirect:/order/detail?orderNo="+order.getNo();
	}*/

	/*@RequestMapping("/order/detail")
	public String detail(int orderNo, Model model) {
		orderService.addHitcount(orderNo);
		Order order = orderService.getOrder(orderNo);
		model.addAttribute("order", order);
		return "order/detail";
	}
	*/
	/*@RequestMapping("/order/delete")
	public String delete(int orderNo) {
		orderService.remove(orderNo);
		return "redirect:/order/list";
	}*/
	
	
}
