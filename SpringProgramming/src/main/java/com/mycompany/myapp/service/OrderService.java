package com.mycompany.myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.dao.CartDao;
import com.mycompany.myapp.dao.OrderDao;
import com.mycompany.myapp.dao.OrderItemDao;
import com.mycompany.myapp.dto.Cart;
import com.mycompany.myapp.dto.Order;
import com.mycompany.myapp.dto.OrderItem;

@Component
public class OrderService {

	@Autowired
	private OrderDao orderDao;
	private CartDao cartDao;
	private OrderItemDao orderItemDao;

	// 로그인한 아이디에 해당하는 장바구니 물품을 주문하는 메소드
	public void cartToOrder(String memberId, int pageNo, int rowsPerPage) {

		List<Cart> list = cartDao.selectByMemberId(memberId, pageNo, rowsPerPage);
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
			// 장바구니 비우기
			cartDao.deleteAll(memberId);
		}
	}

	/*public void deliveryOrder() {
		int update = 0; // update 실행 여부 확인용
		Order order = null; // select 실행 여부 확인용

		try {
			conn = ConnectionManager.getConnection();
			OrderDao orderDao = new OrderDao(conn);

			System.out.println("----------------- 배송 처리 메뉴----------------");
			System.out.println("주문번호를 입력해주세요.");
			int orderNumber = Integer.parseInt(sc.nextLine());
			order = orderDao.selectByPK(orderNumber); // 해당 아이디에 해당하는 주문정보 가져오기

			if (order != null) { // 주문정보가 있을 경우 실행
				while (true) { // 잘못된 값을 입력했거나 수정 실패시 재입력을 위한 while문 처리
					System.out.println("현재 배송상태 : " + order.getOrderDelivery());
					System.out.println("배송완료 처리하시겠습니까?  Yes:1 or No:2");
					int choice = Integer.parseInt(sc.nextLine());

					if (choice == 1) {
						order.setOrderDelivery("배송완료"); // DTO의 배송정보 변경
						update = orderDao.update(order); // update 실행
						if (update == 1) { // update가 실행된 경우
							System.out.println(order.getOrderDelivery() + "처리 되었습니다.");
							break;
						} else { // update가 실패한 경우
							System.out.println("수정에 실패하였습니다");
							break;
						}
					} else if (choice == 2) {
						System.out.println("메뉴로 돌아갑니다.");
						break;
					} else { // 1과 2를 제외 값 예외처리
						System.out.println("잘못된 입력값입니다. 다시 입력해주세요.");
						continue;
					}
				}
			}
		} catch (NumberFormatException e) { // 숫자를 제외한 오입력 예외처리
			System.out.println("잘못된 입력값입니다. 다시 입력해주세요.");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("DB접근 중에 문제가 생겼습니다");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println("DB를 닫는 중에 문제가 생겼습니다");
			}
		}
	}
*/
	// 로그인한 아이디의 주문정보를 보여주는 메소드
	public List<Order> showOrder(String memberId, int pageNo, int rowsPerPage) {
		List<Order> list = orderDao.selectByPage(memberId, pageNo, rowsPerPage);
		return list;
	} 
}

	/*// 모든 주문 정보를 보여주는 메소드 (관리자용)
	public void showAllOrder() {
		try {
			conn = ConnectionManager.getConnection();
			List<Order> orderList = new ArrayList<Order>();
			OrderDao orderDao = new OrderDao(conn);

			int pageNo = 1;
			int rowsPerPage = 5;
			boolean exit = false;

			while (!exit) {
				orderList = orderDao.selectAllByPage(pageNo, rowsPerPage);

				System.out.println("-------------------------------------------------------------------");
				System.out.println("    주문번호    |     주문자     |      주문금액     |  배송상태");
				System.out.println("-------------------------------------------------------------------");
				if (!orderList.isEmpty()) { // 주문이 존재하는지 확인
					for (Order order : orderList) {
						System.out.println("" + order.getOrderNo() + " " + order.getMemberId() + "  "
								+ order.getOrderPrice() + " " + order.getOrderDelivery());
					}
				} else {
					System.out.println("해당 아이디의 주문정보가 없습니다.");
					exit = true; // while문 종료
				}

				System.out.println("-------------------------------------------------------------------");
				System.out.println("현재 페이지: " + pageNo);
				System.out.println("-------------------------------------------------------------------");
				if (pageNo <= 1) {
					System.out.println("입력(ex: 다음페이지: > | 보기종료: q");
				} else
					if (orderList.size() < rowsPerPage || orderDao.selectAllByPage(pageNo + 1, rowsPerPage).isEmpty()) {
					// 마지막 페이지 인지 확인
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
					if (orderList.size() < rowsPerPage || orderDao.selectAllByPage(pageNo + 1, rowsPerPage).isEmpty()) {
						// 마지막 페이지 인지 확인
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
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
}*/
