package com.mycompany.myapp.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.mycompany.myapp.dao.CartDao;
import com.mycompany.myapp.dao.ProductDao;
import com.mycompany.myapp.dto.Cart;
import com.mycompany.myapp.dto.ConnectionManager;
import com.mycompany.myapp.dto.Product;

public class CartService {
	Scanner sc;

	public CartService(Scanner sc) {
		this.sc = sc;
	}

	// 회원의 장바구니를 모두 모여준다.
	public void showCart(String memberId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			//conn.setAutoCommit(false);
			CartDao cartDao = new CartDao(conn);
			List<Cart> list = cartDao.selectByMemberId(memberId);
			if (list.isEmpty()) {
				System.out.println();
				System.out.println("장바구니가 비어있습니다.");
				System.out.println();
			} else {
				for (Cart cart : list) {
					System.out.print("상품번호 : " + cart.getProductNo() + "\t");
					System.out.print("상품명 : " + cart.getProductName() + "\t");
					System.out.print("수량 : " + cart.getCartCount() + "\t");
					System.out.println("가격 : " + cart.getCartPrice());
				}
			}
		} catch (Exception e) {
			System.out.println("연결오류");
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}

	// 장바구니에 상품을 넣는다. 만약 회원이 같은 상품을 주문하면 자동으로 update로 넘어간다.
	public void insertCart(String memberId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			// conn.setAutoCommit(false);
			CartDao cartDao = new CartDao(conn);
			ProductDao productDao = new ProductDao(conn);

			Cart cart = new Cart();

			// 주문자
			cart.setMemberId(memberId);

			// 주문할 상품 번호
			System.out.print("주문할 상품 번호 : ");
			Integer productNo = Integer.parseInt(sc.nextLine());
			Product product = productDao.selectByProductNo(productNo);
			if (product != null) {
				cart.setProductNo(productNo);
			} else {
				System.out.println();
				System.out.println("상품 목록을 다시보시고 상품번호를 입력해주세요");
				return; // return을 하더라도 finally는 실행된다.
			}

			boolean isnew = true;
			List<Cart> list = cartDao.selectByMemberId(memberId);
			for (Cart c : list) {
				if (c.getProductNo() == productNo) {
					isnew = false;
					cart = c;
				}
			}

			// 주문수량
			System.out.print("수량 : ");
			String strCount = sc.nextLine();
			if (strCount.length() > 9) {
				System.out.println();
				System.out.println("너무많아서 주문할 수 없어요ㅠㅠ");
			} else {
				Integer cartCount = Integer.parseInt(strCount);
				if (isnew == true) {
					cart.setCartCount(cartCount);
					cart.setCartPrice(cartCount * product.getpPrice());
					cartDao.insert(cart);
					System.out.println();
					System.out.println("상품 장바구니 저장!!");
				} else {
					cart.setCartCount(cart.getCartCount() + cartCount);
					cart.setCartPrice(cart.getCartCount() * product.getpPrice());
					cartDao.update(cart);
					System.out.println();
					System.out.println("이미 장바구니에 넣은 상품이라 수량 업데이트 했습니다.");
				}
			}
		} 
		catch (NumberFormatException e) {
			System.out.println("숫자를 입력하셔야죠~");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("정확한 상품 번호를 입력해주세요.");
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}

	// 회원이 장바구니에 담은 상품하나를 삭제한다.
	public void deleteOneCart(String memberId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			CartDao cartDao = new CartDao(conn);
			System.out.print("삭제할 상품번호 입력 : ");
			int productNo = Integer.parseInt(sc.nextLine());
			int rows = cartDao.deleteOne(productNo, memberId);
			if (rows == 1) {
				System.out.println();
				System.out.println(productNo + " 번 상품이 삭제되었습니다!");
			} else {
				System.out.println();
				System.out.println(productNo + " 번 상품이 존재하지 않습니다!");
			}
			conn.commit();
		} catch (NumberFormatException e) {
			System.out.println("숫자를 입력하셔야죠~!");
		} catch (Exception e) {
			System.out.println("숫자를 입력하셔야죠~!");
			try {
				conn.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}

	// 회원의 장바구니를 비운다.
	public void deleteAllCart(String memberId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			CartDao cartDao = new CartDao(conn);
			cartDao.deleteAll(memberId);
			System.out.println("장바구니를 비웠습니다.");
			conn.commit();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
			}
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}
}
