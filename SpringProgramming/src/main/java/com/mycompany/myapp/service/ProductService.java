package com.mycompany.myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.dao.ProductDao;
import com.mycompany.myapp.dto.Product;

@Component
public class ProductService {

	@Autowired
	private ProductDao productDao;

	public void add(Product product) {
		productDao.insert(product);
	}

	public List<Product> getPage(int pageNo, int rowsPerPage) {
		List<Product> list = productDao.selectByPage(pageNo, rowsPerPage);
		return list;
	}

	public Product getProduct(int ProductNo) {
		Product Product = productDao.selectByPk(ProductNo);
		return Product;
	}

	public void modify(Product Product) {
		productDao.update(Product);
	}

	public void remove(int ProductNo) {
		productDao.delete(ProductNo);

	}

	public void addHitcount(long ProductNo) {
		productDao.updateHitcount(ProductNo);
	}
	
	public int getTotalProductNo() {
		int rows = productDao.selectCount();
		return rows;
	}
}