package com.mycompany.myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.dao.CartDao;
import com.mycompany.myapp.dao.ProductDao;
import com.mycompany.myapp.dto.Cart;
import com.mycompany.myapp.dto.Product;

@Component
public class CartService {
	@Autowired
	private CartDao cartDao;
	@Autowired
	private ProductDao productDao;
	
	// 회원의 장바구니를 모두 모여준다.
	public List<Cart>  getPage(String memberId) {
		List<Cart> list = cartDao.selectByMemberId(memberId);
		return list;
	}
	
	// 장바구니에 상품을 넣는다. 만약 회원이 같은 상품을 주문하면 자동으로 update로 넘어간다.
	public void add(int productNo, int cartCount, String memberId){
		//멤버아이디
		Cart cart = new Cart();
		cart.setMemberId(memberId);
		cart.setCartCount(cartCount);
		cart.setProductNo(productNo);
		cart.setProductName(productDao.selectByProductNoReturnName(productNo));
		cart.setCartPrice(cartCount*productDao.selectByProductNoReturnPrice(productNo));
		List<Cart> list = cartDao.selectProductNo(cart.getMemberId());
		boolean i=false;
		for(Cart c : list){
			if(c.getProductNo()==cart.getProductNo()){
				cart.setCartCount(c.getCartCount()+cart.getCartCount());
				cart.setCartPrice(c.getCartPrice()+cart.getCartPrice());
				i=true;
			}
		}
		if(i){
			cartDao.update(cart);
		}else{
			cartDao.insert(cart);
		}
	}
	
	// 회원이 장바구니에 담은 상품하나를 삭제한다.
	public void deleteOne(int cartNo,String memberId) {
		cartDao.deleteOne(cartNo,memberId);
	}

	// 회원의 장바구니를 비운다.
	public void deleteAllCart(String memberId) {
		cartDao.deleteAll(memberId);
	}

	public int getTotalCartNo() {
		int rows = cartDao.selectCount();
		return rows;
	}
}
