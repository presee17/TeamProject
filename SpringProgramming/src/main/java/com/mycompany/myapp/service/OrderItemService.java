package com.mycompany.myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.dao.OrderDao;
import com.mycompany.myapp.dao.OrderItemDao;
import com.mycompany.myapp.dto.OrderItem;

@Component
public class OrderItemService {
	
	@Autowired

	private OrderDao orderdao;
	
	@Autowired
	private OrderItemDao orderItemDao;

	public void add(OrderItem orderItem) {
		orderItemDao.insert(orderItem);
					
	}
	public List<OrderItem> getPage(int orderNo,int pageNo,int rowsPerPage ){
		List<OrderItem> list = orderItemDao.selectByPage(orderNo, pageNo, rowsPerPage);
		return list;
	}
	
	public void remove(int orderNo){
		orderItemDao.delete(orderNo);
	}
	public int getTotalOrderItemNo(int orderNo){
		int rows=orderItemDao.selectCount(orderNo);
		return rows;
	}
}
