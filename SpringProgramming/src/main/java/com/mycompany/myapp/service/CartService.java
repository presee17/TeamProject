package com.mycompany.myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.dao.CartDao;
import com.mycompany.myapp.dto.Cart;

@Component
public class CartService {
	@Autowired
	private CartDao cartDao;
	// 회원의 장바구니를 모두 모여준다.
	public List<Cart>  getPage(String memberId,int pageNo, int rowsPerPage) {
		List<Cart> list = cartDao.selectByMemberId(memberId,pageNo, rowsPerPage);
		return list;
	}
	
	// 장바구니에 상품을 넣는다. 만약 회원이 같은 상품을 주문하면 자동으로 update로 넘어간다.
	public void add(Cart cart) {
		cartDao.insert(cart);
	}
	
	// 회원이 장바구니에 담은 상품하나를 삭제한다.
	public void deleteOne(int productNo,String memberId) {
		cartDao.deleteOne(productNo,memberId);
	}

	// 회원의 장바구니를 비운다.
	public void deleteAllCart(String memberId) {
		cartDao.deleteAll(memberId);
	}
}
