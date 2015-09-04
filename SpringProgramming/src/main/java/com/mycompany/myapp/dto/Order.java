package com.mycompany.myapp.dto;

import java.util.Date;

public class Order {
	
	private int orderNo;
	private String memberId;
	private int orderPrice;
	private String orderDelivery;
	private Date orderDate; 
	
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderDelivery() {
		return orderDelivery;
	}
	public void setOrderDelivery(String orderDelivery) {
		this.orderDelivery = orderDelivery;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(int orderprice) {
		this.orderPrice = orderprice;
	}
	
	
	
}
