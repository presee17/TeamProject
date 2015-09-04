package com.mycompany.myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.dao.ProductDao;
import com.mycompany.myapp.dto.Product;

@Component
public class ProductService {
	
	@Autowired
	private ProductDao	productdao;
	
	public List<Product> getPage(int pageNo, int rowsPerPage){
		List<Product> list = productdao.selectAllByPage(pageNo, rowsPerPage);
		return list;
	}
	public Product getProduct(int productNo){
		Product product = productdao.selectByProductNo(productNo);
		return product;
	}
	public int getTotalProductNo(){
		int rows=productdao.selectCount();
		return rows;
	}
}
