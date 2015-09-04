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
							.println("1.�α׾ƿ� | 2.��ǰ��� | 3.��ٱ��ϳֱ� | 4.��ٱ��Ϻ��� | 5.��ٱ��ϻ��� | 6.�ֹ��ϱ� | 7.��ü�ֹ����� | 8.�ֹ�������");
					System.out.println(
							"-----------------------------------------------------------------------------------------");
					System.out.print("���� : ");
					selectNo = Integer.parseInt(sc.nextLine());

					if (selectNo == 1) {
						System.out.println("�α׾ƿ� �ҰԿ� ��!!!!");
						flag = false;
						return;

					} else if (selectNo == 2) {
						System.out.println("2.��ǰ���");
						p.showProduct();
						// ��ǰ ��� �޼ҵ� ȣ��
						continue;

					} else if (selectNo == 3) {
						System.out.println("3. ��ٱ��� �ֱ�");
						c.insertCart(member.getId());
						// ��ٱ��Ͽ� ��ǰ �ֱ� �޼ҵ� ȣ��
						continue;

					} else if (selectNo == 4) {
						System.out.println("4. ��ٱ��� ����");
						System.out.println("--------------------------");
						System.out.println("��ǰ��ȣ   ��ǰ��   ����  ����");
						System.out.println("--------------------------");
						c.showCart(member.getId());
						// ��ٱ��� �����ֱ� �޼ҵ� ȣ��
						System.out.println("--------------------------");
						continue;
					} else if (selectNo == 5) {
						boolean flag1 = true;
						while (flag1) {
							System.out.println("5. ��ٱ��� ����");
							System.out.println("--------------------------");
							System.out.println("ǰ�� �ϳ� ���� : 1 / ��ü ���� : 2");
							System.out.println("--------------------------");
							int selectNo1 = Integer.parseInt(sc.nextLine());
							try {
								if (selectNo1 == 1) {
									c.deleteOneCart(member.getId());
									//��ٱ��� ǰ�� ���� �޼ҵ� ȣ��
									System.out.println();
									flag1 = false;
								} else if (selectNo1 == 2) {
									c.deleteAllCart(member.getId());
									//��ٱ��� ǰ�� ��ü ���� �޼ҵ� ȣ��
									flag1 = false;
								} else {
									System.out.println("1,2�߿� �ϳ� �����ϼ���");
								}
							} catch (NumberFormatException e) {
								//���ڿ� �Է� ���� ó��
								System.out.println("���ڸ� �Է��ϼž���");
							}
						}
					} else if (selectNo == 6) {
						System.out.println("6. �ֹ��ϱ�");
						os.cartToOrder(member.getId());
						//�ֹ��ϱ� �޼ҵ� ȣ��
						continue;

					} else if (selectNo == 7) {
						System.out.println("7. ��ü �ֹ�����");
						os.showOrder(member.getId());
						//�ֹ����� �����ֱ� �޼ҵ� ȣ��
						continue;

					} else if (selectNo == 8) {
						System.out.println("8. �ֹ�������");
						System.out.print("�ֹ���ȣ�� �����ϼ��� : ");
						int No = 0;
						No = Integer.parseInt(sc.nextLine());
						if (ois.idcheck(member.getId(), No)) {
							// �ֹ��� ȸ���� �´��� Ȯ��
							ois.ShowDetail(No);
							// ������ �ֹ���ȣ�� ���� ������ ����ϴ� �޼ҵ� ȣ��
							
						} else {
							System.out.println("�ٸ� ��ȣ�� �Է��ϼ��� ");
						}
						continue;

					} else {
						System.out.println("1~7 �����߿� �����ϼ���");
						continue;
					}
				} catch (NumberFormatException e) {
					System.out.println("���ڸ� �Է� �ϼ���.");
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}