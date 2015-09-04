package test.shoppingmall.manager;

import java.util.Scanner;

import test.shoppingmall.service.CartService;
import test.shoppingmall.service.OrderItemService;
import test.shoppingmall.service.OrderService;
import test.shoppingmall.service.ProductService;
import test.shoppingmall.vo.Member;

public class MemberServiceManager {
	Scanner sc;

	public MemberServiceManager(Scanner sc) {
		this.sc = sc;
	}

	public void run(Member member) {
		int selectNo;
		ProductService p = new ProductService(sc);
		CartService c = new CartService(sc);
		OrderService os = new OrderService(sc);
		OrderItemService ois = new OrderItemService(sc);
		boolean flag = true;
		try {
			while (flag) {
				try {
					System.out.println(
							"-----------------------------------------------------------------------------------------");
					System.out
							.println("1.로그아웃 | 2.상품목록 | 3.장바구니넣기 | 4.장바구니보기 | 5.장바구니삭제 | 6.주문하기 | 7.전체주문정보 | 8.주문상세정보");
					System.out.println(
							"-----------------------------------------------------------------------------------------");
					System.out.print("선택 : ");
					selectNo = Integer.parseInt(sc.nextLine());

					if (selectNo == 1) {
						System.out.println("로그아웃 할게용 뿅!!!!");
						flag = false;
						return;

					} else if (selectNo == 2) {
						System.out.println("2.상품목록");
						p.showProduct();
						// 상품 목록 메소드 호출
						continue;

					} else if (selectNo == 3) {
						System.out.println("3. 장바구니 넣기");
						c.insertCart(member.getId());
						// 장바구니에 상품 넣기 메소드 호출
						continue;

					} else if (selectNo == 4) {
						System.out.println("4. 장바구니 보기");
						System.out.println("--------------------------");
						System.out.println("상품번호   상품명   수량  가격");
						System.out.println("--------------------------");
						c.showCart(member.getId());
						// 장바구니 보여주기 메소드 호출
						System.out.println("--------------------------");
						continue;
					} else if (selectNo == 5) {
						boolean flag1 = true;
						while (flag1) {
							System.out.println("5. 장바구니 삭제");
							System.out.println("--------------------------");
							System.out.println("품목 하나 삭제 : 1 / 전체 삭제 : 2");
							System.out.println("--------------------------");
							int selectNo1 = Integer.parseInt(sc.nextLine());
							try {
								if (selectNo1 == 1) {
									c.deleteOneCart(member.getId());
									//장바구니 품목 삭제 메소드 호출
									System.out.println();
									flag1 = false;
								} else if (selectNo1 == 2) {
									c.deleteAllCart(member.getId());
									//장바구니 품목 전체 삭제 메소드 호출
									flag1 = false;
								} else {
									System.out.println("1,2중에 하나 선택하세요");
								}
							} catch (NumberFormatException e) {
								//숫자외 입력 예외 처리
								System.out.println("숫자를 입력하셔야죠");
							}
						}
					} else if (selectNo == 6) {
						System.out.println("6. 주문하기");
						os.cartToOrder(member.getId());
						//주문하기 메소드 호출
						continue;

					} else if (selectNo == 7) {
						System.out.println("7. 전체 주문정보");
						os.showOrder(member.getId());
						//주문정보 보여주기 메소드 호출
						continue;

					} else if (selectNo == 8) {
						System.out.println("8. 주문상세정보");
						System.out.print("주문번호를 선택하세요 : ");
						int No = 0;
						No = Integer.parseInt(sc.nextLine());
						if (ois.idcheck(member.getId(), No)) {
							// 주문한 회원이 맞는지 확인
							ois.ShowDetail(No);
							// 선택한 주문번호의 상제 정보를 출력하는 메소드 호출
							
						} else {
							System.out.println("다른 번호를 입력하세요 ");
						}
						continue;

					} else {
						System.out.println("1~7 숫자중에 선택하세요");
						continue;
					}
				} catch (NumberFormatException e) {
					System.out.println("숫자만 입력 하세요.");
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}