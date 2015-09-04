package com.mycompany.myapp.controller;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.myapp.dto.Board;
import com.mycompany.myapp.dto.Product;
import com.mycompany.myapp.service.ProductService;

@Controller
public class ProductController {
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService productService;

	@RequestMapping("/product/list")
	public String list(@RequestParam(defaultValue = "1") int pageNo, Model model) {
		logger.info("list()");

		int rowsPerPage = 10;
		int pagesPerGroup = 5;

		// ��ü �Խù� ��
		int totalProductNo = productService.getTotalProductNo();

		// ��ü ������ ��
		int totalPageNo = totalProductNo / rowsPerPage;
		if (totalProductNo % rowsPerPage != 0) {
			totalPageNo++;
		}
		// ��ü �׷� ��
		int totalGroupNo = totalPageNo / pagesPerGroup;
		if (totalPageNo % pagesPerGroup != 0) {
			totalGroupNo++;
		}

		// ���� �׷��ȣ, ������������ȣ, ����������ȣ
		int groupNo = (pageNo - 1) / pagesPerGroup + 1;
		int startPageNo = (groupNo - 1) * pagesPerGroup + 1;
		int endPageNo = startPageNo + pagesPerGroup - 1;
		if (groupNo == totalGroupNo) {
			endPageNo = totalPageNo;
		}

		// ���� ������ �Խù� ����Ʈ
		List<Product> list = productService.getPage(pageNo, rowsPerPage);
		model.addAttribute("list", list);

		// View�� �ѱ� ������
		model.addAttribute("pagesPerGroup", pagesPerGroup);
		model.addAttribute("totalPageNo", totalPageNo);
		model.addAttribute("totalGroupNo", totalGroupNo);
		model.addAttribute("groupNo", groupNo);
		model.addAttribute("startPageNo", startPageNo);
		model.addAttribute("endPageNo", endPageNo);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("list", list);

		return "product/list";
	}

	@RequestMapping("/product/writeForm")
	public String writeForm() {
		logger.info("productForm()");
		return "product/writeForm";
	}

	@RequestMapping("/product/updateForm")
	public String updateForm() {
		logger.info("updateForm()");
		return "product/updateForm";
	}

	@RequestMapping("/product/write")
	public String write(String name, int price, int stock, String detail, MultipartFile attach, HttpSession session) {
		logger.info("write()");
		
		ServletContext application = session.getServletContext();
		String dirpath = application.getRealPath("/resources/uploadfiles");
		String originalFilename = attach.getOriginalFilename();
		String filesystemName = System.currentTimeMillis() + "-" + originalFilename;
		String contentType = attach.getContentType();

		if (!attach.isEmpty()) {
			// 파일에 저장하기
			try {
				attach.transferTo(new File(dirpath + "/" + filesystemName));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setStock(stock);
		product.setDetail(detail);
		if(!attach.isEmpty()){
			product.setOriginalFileName(originalFilename);
			product.setFilesystemName(filesystemName);
			product.setContentType(contentType);
			productService.add(product);
		}
		return "redirect:/product/list";
	}

	@RequestMapping("/product/update")
	public String update() {
		logger.info("update()");
		return "redirect:/product/list";
	}
	
	@RequestMapping("/product/detail")
	public String detail(int productNo, Model model) {
		Product product = productService.getProduct(productNo);
		model.addAttribute("product", product);
		return "product/detail";
	}
}
