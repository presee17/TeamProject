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

	// ȸ���� ��ٱ��ϸ� ��� ���ش�.
	public void showCart(String memberId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			//conn.setAutoCommit(false);
			CartDao cartDao = new CartDao(conn);
			List<Cart> list = cartDao.selectByMemberId(memberId);
			if (list.isEmpty()) {
				System.out.println();
				System.out.println("��ٱ��ϰ� ����ֽ��ϴ�.");
				System.out.println();
			} else {
				for (Cart cart : list) {
					System.out.print("��ǰ��ȣ : " + cart.getProductNo() + "\t");
					System.out.print("��ǰ�� : " + cart.getProductName() + "\t");
					System.out.print("���� : " + cart.getCartCount() + "\t");
					System.out.println("���� : " + cart.getCartPrice());
				}
			}
		} catch (Exception e) {
			System.out.println("�������");
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}

	// ��ٱ��Ͽ� ��ǰ�� �ִ´�. ���� ȸ���� ���� ��ǰ�� �ֹ��ϸ� �ڵ����� update�� �Ѿ��.
	public void insertCart(String memberId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			// conn.setAutoCommit(false);
			CartDao cartDao = new CartDao(conn);
			ProductDao productDao = new ProductDao(conn);

			Cart cart = new Cart();

			// �ֹ���
			cart.setMemberId(memberId);

			// �ֹ��� ��ǰ ��ȣ
			System.out.print("�ֹ��� ��ǰ ��ȣ : ");
			Integer productNo = Integer.parseInt(sc.nextLine());
			Product product = productDao.selectByProductNo(productNo);
			if (product != null) {
				cart.setProductNo(productNo);
			} else {
				System.out.println();
				System.out.println("��ǰ ����� �ٽú��ð� ��ǰ��ȣ�� �Է����ּ���");
				return; // return�� �ϴ��� finally�� ����ȴ�.
			}

			boolean isnew = true;
			List<Cart> list = cartDao.selectByMemberId(memberId);
			for (Cart c : list) {
				if (c.getProductNo() == productNo) {
					isnew = false;
					cart = c;
				}
			}

			// �ֹ�����
			System.out.print("���� : ");
			String strCount = sc.nextLine();
			if (strCount.length() > 9) {
				System.out.println();
				System.out.println("�ʹ����Ƽ� �ֹ��� �� �����Ф�");
			} else {
				Integer cartCount = Integer.parseInt(strCount);
				if (isnew == true) {
					cart.setCartCount(cartCount);
					cart.setCartPrice(cartCount * product.getpPrice());
					cartDao.insert(cart);
					System.out.println();
					System.out.println("��ǰ ��ٱ��� ����!!");
				} else {
					cart.setCartCount(cart.getCartCount() + cartCount);
					cart.setCartPrice(cart.getCartCount() * product.getpPrice());
					cartDao.update(cart);
					System.out.println();
					System.out.println("�̹� ��ٱ��Ͽ� ���� ��ǰ�̶� ���� ������Ʈ �߽��ϴ�.");
				}
			}
		} 
		catch (NumberFormatException e) {
			System.out.println("���ڸ� �Է��ϼž���~");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("��Ȯ�� ��ǰ ��ȣ�� �Է����ּ���.");
		} finally {
			try {
				conn.close();
			} catch (Exception e) {
			}
		}
	}

	// ȸ���� ��ٱ��Ͽ� ���� ��ǰ�ϳ��� �����Ѵ�.
	public void deleteOneCart(String memberId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			CartDao cartDao = new CartDao(conn);
			System.out.print("������ ��ǰ��ȣ �Է� : ");
			int productNo = Integer.parseInt(sc.nextLine());
			int rows = cartDao.deleteOne(productNo, memberId);
			if (rows == 1) {
				System.out.println();
				System.out.println(productNo + " �� ��ǰ�� �����Ǿ����ϴ�!");
			} else {
				System.out.println();
				System.out.println(productNo + " �� ��ǰ�� �������� �ʽ��ϴ�!");
			}
			conn.commit();
		} catch (NumberFormatException e) {
			System.out.println("���ڸ� �Է��ϼž���~!");
		} catch (Exception e) {
			System.out.println("���ڸ� �Է��ϼž���~!");
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

	// ȸ���� ��ٱ��ϸ� ����.
	public void deleteAllCart(String memberId) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			conn.setAutoCommit(false);
			CartDao cartDao = new CartDao(conn);
			cartDao.deleteAll(memberId);
			System.out.println("��ٱ��ϸ� ������ϴ�.");
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
