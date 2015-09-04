package com.mycompany.myapp.dto;

public class OrderItem {
	private int orderItemNo;
	private int orderNo;
	private int productNo;
	private int productPrice;
	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}
	private int orderItemCount;
	private int orderItemPrice;
	private String productName;
	public int getOrderItemNo() {
		return orderItemNo;
	}
	
	public void setOrderItemNo(int orderItemNo) {
		this.orderItemNo = orderItemNo;
	}
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getOrderNo() {
		return orderNo;
	}
	
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public int getProductNo() {
		return productNo;
	}
	public void setProductNo(int productNo) {
		this.productNo = productNo;
	}
	public int getOrderItemCount() {
		return orderItemCount;
	}
	public void setOrderItemCount(int orderItemCount) {
		this.orderItemCount = orderItemCount;
	}
	public int getOrderItemPrice() {
		return orderItemPrice;
	}
	public void setOrderItemPrice(int orderItemPrice) {
		this.orderItemPrice = orderItemPrice;
	}
	

	
}
