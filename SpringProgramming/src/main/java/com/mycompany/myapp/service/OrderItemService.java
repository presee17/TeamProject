package com.mycompany.myapp.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mycompany.myapp.dao.OrderDao;
import com.mycompany.myapp.dao.OrderItemDao;
import com.mycompany.myapp.dto.ConnectionManager;
import com.mycompany.myapp.dto.Order;
import com.mycompany.myapp.dto.OrderItem;

public class OrderItemService {
	Scanner sc;

	public OrderItemService(Scanner sc) {
		this.sc = sc;
	}

	public boolean idcheck(String memberid, int orderNo) {
		Connection conn = null;
		boolean check = false;
		try {
			conn = ConnectionManager.getConnection();
			OrderDao orderdao = new OrderDao(conn);
			Order order = new Order();
			order = orderdao.selectByPK(orderNo);
			if (order == null) {
				System.out.println("해당 주문 번호가 없습니다.");
				return check;
			}
			if (memberid.equals(order.getMemberId())) {
				check = true;

			}
		} catch (Exception e) {
			System.out.println("오류가 발생했습니다.");
			return check;
		}
		return check;
	}

	public void ShowDetail(int orderNo) {
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection();
			OrderItemDao orderitemDao = new OrderItemDao(conn);
			List<OrderItem> list = new ArrayList<OrderItem>();
			Scanner sc = new Scanner(System.in);
			int pageNo = 1;
			int rowsPerPage = 5;
			boolean exit = false;

			while (!exit) {
				list = orderitemDao.selectByorderNo(orderNo, pageNo,
						rowsPerPage);
				if (!list.isEmpty()) {
					System.out
							.println("-------------------------------------------------------------------------------------------------------");
					System.out
							.println("    주문번호     |    상품번호     |    상품이름    |    상품가격    |   상품수량    |    주문가격 ");
					System.out
							.println("------------------------------------------------------------------------------------------------------");
					for (OrderItem orderitem : list) {
						System.out.println(+orderitem.getOrderNo() + "\t\t"
								+ orderitem.getProductNo() + "\t\t"
								+ orderitem.getProductName() + "\t\t"
								+ orderitem.getProductPrice() + "\t\t"
								+ orderitem.getOrderItemCount() + "\t\t"
								+ orderitem.getOrderItemPrice());
					}
				} else {
					System.out.println("등록된 주문이 없습니다.");
					exit = true;
				}
				System.out
						.println("----------------------------------------------------------------------");
				System.out.println("현재 페이지: " + pageNo);
				System.out
						.println("----------------------------------------------------------------------");
				if (pageNo <= 1) {
					System.out.println("입력(ex: 다음페이지: > | 보기종료: q");
				} else if ((list.size() < rowsPerPage)
						|| orderitemDao.selectByorderNo(orderNo, pageNo + 1,
								rowsPerPage).isEmpty()) {
					System.out.println("입력(ex: 이전페이지: < | 보기종료: q");
				} else {
					System.out.println("입력(ex: 이전페이지: <, 다음페이지: > | 보기종료: q");
				}

				System.out.print(": ");
				String choice = sc.nextLine();

				if (choice.equals("<")) {
					if (pageNo > 1) {
						pageNo -= 1;
						continue;
					} else {
						System.out.println("목록의 처음입니다.");
						continue;
					}
				} else if (choice.equals(">")) {
					if (list.size() < rowsPerPage
							|| orderitemDao.selectByorderNo(orderNo,
									pageNo + 1, rowsPerPage).isEmpty()) {
						System.out.println("목록의 마지막 입니다.");
						continue;
					} else {
						pageNo += 1;
						continue;
					}
				} else if (choice.equals("q")) {
					exit = true;
				} else {
					try {
						pageNo = Integer.parseInt(choice);
						continue;
					} catch (NumberFormatException e) {
						System.out.println("다시 입력하세요.");
						continue;
					}
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
