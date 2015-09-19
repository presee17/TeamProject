package com.mycompany.myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.dao.CartDao;
import com.mycompany.myapp.dao.OrderDao;
import com.mycompany.myapp.dao.OrderItemDao;
import com.mycompany.myapp.dto.Board;
import com.mycompany.myapp.dto.Cart;
import com.mycompany.myapp.dto.Order;
import com.mycompany.myapp.dto.OrderItem;

@Component
public class OrderService {

	@Autowired
	private OrderDao orderDao;
	private CartDao cartDao;
	private OrderItemDao orderItemDao;

	// 로그인한 아이디, 장바구니에서 선택한 물품을 주문하는 메소드
	public void cartToOrder(String memberId, int cartNo) {

		Cart cart = cartDao.selectByMemberId(memberId);
		if (!list.isEmpty()) { // 장바구니가 비었을 경우 예외처리
			int total = 0; // total orderPrice
			for (Cart cart : list) {
				total += cart.getCartPrice();
			}
			Order order = new Order();
			order.setMemberId(memberId);
			order.setOrderPrice(total);

			int orderNo = orderDao.insert(order); 

			// 주문 상세정보 Dto에 값 입력
			OrderItem orderItem = new OrderItem();
			for (Cart cart : list) {
				orderItem.setOrderItemCount(cart.getCartCount());
				orderItem.setOrderItemPrice(cart.getCartPrice());
				orderItem.setProductNo(cart.getProductNo());
				orderItem.setOrderNo(orderNo);
				orderItemDao.insert(orderItem);
			}
			// 장바구니에서 해당 상품 삭제
			cartDao.deleteOne(cartNo, memberId);
			
		}
	}
	
	// 로그인한 아이디의 주문정보를 보여주는 메소드
	public List<Order> showOrder(String memberId, int pageNo, int rowsPerPage) {
			List<Order> list = orderDao.selectByPage(memberId, pageNo, rowsPerPage);
			return list;
	}
	
	// 상세 정보를 보여주기 위해서 특정 주문 정보를 선택함.
	public Order getOrder(int orderNo) {
		Order order = orderDao.selectByPk(orderNo);
		return order;
	}
	
	// 페이징을 위해서 전체 주문 숫자를 파악할 때 사용.
	public int getTotalOrderNo() {
		int rows = orderDao.selectCount();
		return rows;
	}
}