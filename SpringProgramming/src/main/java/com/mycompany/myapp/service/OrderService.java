package test.shoppingmall.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import test.shoppingmall.ConnectionManager;
import test.shoppingmall.dao.CartDao;
import test.shoppingmall.dao.OrderDao;
import test.shoppingmall.dao.OrderItemDao;
import test.shoppingmall.vo.Cart;
import test.shoppingmall.vo.Order;
import test.shoppingmall.vo.OrderItem;

public class OrderService {
   Connection conn;
   Scanner sc;

   public OrderService(Scanner sc) {
      this.sc = sc;
   }

   // �α����� ���̵� �ش��ϴ� ��ٱ��� ��ǰ�� �ֹ��ϴ� �޼ҵ�
   public void cartToOrder(String memberId) {
      try {
         conn = ConnectionManager.getConnection();

         // �ֹ��� �����ߴµ��� ��ٱ��ϸ� ���� ���� �����ϱ� ���� transaction ó��
         conn.setAutoCommit(false);

         CartDao cartDao = new CartDao(conn);

         List<Cart> list = cartDao.selectByMemberId(memberId);
         if (list.isEmpty()) { // ��ٱ��ϰ� ����� ��� ����ó��
            System.out.println("��ٱ��ϰ� ������ϴ�.");
         } else {
            int total = 0; // total orderPrice

            for (Cart cart : list) {
               total += cart.getCartPrice();
            }

            Order order = new Order();
            order.setMemberId(memberId);
            order.setOrderPrice(total);
            OrderDao orderDao = new OrderDao(conn);
            int orderNo = orderDao.insert(order);

            // �ֹ� ������ Dto�� �� �Է�
            OrderItemDao orderitemDao = new OrderItemDao(conn);
            OrderItem orderitem = new OrderItem();
            for (Cart cart : list) {
               orderitem.setOrderItemCount(cart.getCartCount());
               orderitem.setOrderItemPrice(cart.getCartPrice());
               orderitem.setProductNo(cart.getProductNo());
               orderitem.setOrderNo(orderNo);
               orderitemDao.insert(orderitem);
            }

            // ��ٱ��� ����
            cartDao.deleteAll(memberId);

            conn.commit();
            conn.close();
            System.out.println("�ֹ��� �Ϸ�Ǿ����ϴ�.");
         }
      } catch (Exception e) {
         try {
            conn.rollback();
         } catch (SQLException e1) {
         }
         System.out.println("�ֹ��� �����Ͽ����ϴ�. �����ڿ��� �������ּ���.");
         e.printStackTrace();
      } finally {
         try {
            conn.close();
         } catch (Exception e) {
         }
      }
   }

   public void deliveryOrder() {
      int update = 0; // update ���� ���� Ȯ�ο�
      Order order = null; // select ���� ���� Ȯ�ο�

      try {
         conn = ConnectionManager.getConnection();
         OrderDao orderDao = new OrderDao(conn);

         System.out.println("----------------- ��� ó�� �޴�----------------");
         System.out.println("�ֹ���ȣ�� �Է����ּ���.");
         int orderNumber = Integer.parseInt(sc.nextLine());
         order = orderDao.selectByPK(orderNumber); // �ش� ���̵� �ش��ϴ� �ֹ����� ��������

         if (order != null) { // �ֹ������� ���� ��� ����
            while (true) { // �߸��� ���� �Է��߰ų� ���� ���н� ���Է��� ���� while�� ó��
               System.out.println("���� ��ۻ��� : " + order.getOrderDelivery());
               System.out.println("��ۿϷ� ó���Ͻðڽ��ϱ�?  Yes:1 or No:2");
               int choice = Integer.parseInt(sc.nextLine());

               if (choice == 1) {
                  order.setOrderDelivery("��ۿϷ�"); // DTO�� ������� ����
                  update = orderDao.update(order); // update ����
                  if (update == 1) { // update�� ����� ���
                     System.out.println(order.getOrderDelivery() + "ó�� �Ǿ����ϴ�.");
                     break;
                  } else { // update�� ������ ���
                     System.out.println("������ �����Ͽ����ϴ�");
                     break;
                  }
               } else if (choice == 2) {
                  System.out.println("�޴��� ���ư��ϴ�.");
                  break;
               } else { // 1�� 2�� ���� �� ����ó��
                  System.out.println("�߸��� �Է°��Դϴ�. �ٽ� �Է����ּ���.");
                  continue;
               }
            }
         }
      } catch (NumberFormatException e) { // ���ڸ� ������ ���Է� ����ó��
         System.out.println("�߸��� �Է°��Դϴ�. �ٽ� �Է����ּ���.");
         e.printStackTrace();
      } catch (SQLException e) {
         System.out.println("DB���� �߿� ������ ������ϴ�");
         e.printStackTrace();
      } catch (ClassNotFoundException e) {
      } finally {
         try {
            conn.close();
         } catch (SQLException e) {
            System.out.println("DB�� �ݴ� �߿� ������ ������ϴ�");
         }
      }
   }

   // �α����� ���̵��� �ֹ������� �����ִ� �޼ҵ�
   public void showOrder(String memberId) {
      try {
         conn = ConnectionManager.getConnection();
         List<Order> orderList = new ArrayList<Order>();
         OrderDao orderDao = new OrderDao(conn);

         int pageNo = 1;
         int rowsPerPage = 5;
         boolean exit = false;

         while (!exit) {
            orderList = orderDao.selectByPage(memberId, pageNo, rowsPerPage);

            System.out.println("-------------------------------------------------------------------");
            System.out.println("    �ֹ���ȣ    |     �ֹ���     |      �ֹ��ݾ�     |  ��ۻ���      |  �ֹ���¥");
            System.out.println("-------------------------------------------------------------------");
            if (!orderList.isEmpty()) { // �ֹ��� �����ϴ��� Ȯ��
               for (Order order : orderList) {
                  System.out.println("" + order.getOrderNo() + " " + order.getMemberId() + "  "
                        + order.getOrderPrice() + " " + order.getOrderDelivery()+"\t"+order.getOrderDate());
               }
            } else {
               System.out.println("�ش� ���̵��� �ֹ������� �����ϴ�.");
               exit = true; // while�� ����
            }

            System.out.println("-------------------------------------------------------------------");
            System.out.println("���� ������: " + pageNo);
            System.out.println("-------------------------------------------------------------------");
            if (pageNo <= 1) {
               System.out.println("�Է�(ex: ����������: > | ��������: q");
            } else
               if (orderList.size() < rowsPerPage || orderDao.selectAllByPage(pageNo + 1, rowsPerPage).isEmpty()) {
               // ������ ������ ���� Ȯ��
               System.out.println("�Է�(ex: ����������: < | ��������: q");
            } else {
               System.out.println("�Է�(ex: ����������: <, ����������: > | ��������: q");
            }
            System.out.print(": ");
            String choice = sc.nextLine();
            if (choice.equals("<")) {
               if (pageNo > 1) {
                  pageNo -= 1;
                  continue;
               } else {
                  System.out.println("����� ó���Դϴ�.");
                  continue;
               }
            } else if (choice.equals(">")) {
               if (orderList.size() < rowsPerPage || orderDao.selectAllByPage(pageNo + 1, rowsPerPage).isEmpty()) {
                  // ������ ������ ���� Ȯ��
                  System.out.println("����� ������ �Դϴ�.");
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
                  System.out.println("�ٽ� �Է��ϼ���.");
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

   // ��� �ֹ� ������ �����ִ� �޼ҵ� (�����ڿ�)
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
            System.out.println("    �ֹ���ȣ    |     �ֹ���     |      �ֹ��ݾ�     |  ��ۻ���");
            System.out.println("-------------------------------------------------------------------");
            if (!orderList.isEmpty()) { // �ֹ��� �����ϴ��� Ȯ��
               for (Order order : orderList) {
                  System.out.println("" + order.getOrderNo() + " " + order.getMemberId() + "  "
                        + order.getOrderPrice() + " " + order.getOrderDelivery());
               }
            } else {
               System.out.println("�ش� ���̵��� �ֹ������� �����ϴ�.");
               exit = true; // while�� ����
            }

            System.out.println("-------------------------------------------------------------------");
            System.out.println("���� ������: " + pageNo);
            System.out.println("-------------------------------------------------------------------");
            if (pageNo <= 1) {
               System.out.println("�Է�(ex: ����������: > | ��������: q");
            } else
               if (orderList.size() < rowsPerPage || orderDao.selectAllByPage(pageNo + 1, rowsPerPage).isEmpty()) {
               // ������ ������ ���� Ȯ��
               System.out.println("�Է�(ex: ����������: < | ��������: q");
            } else {
               System.out.println("�Է�(ex: ����������: <, ����������: > | ��������: q");
            }
            System.out.print(": ");
            String choice = sc.nextLine();
            if (choice.equals("<")) {
               if (pageNo > 1) {
                  pageNo -= 1;
                  continue;
               } else {
                  System.out.println("����� ó���Դϴ�.");
                  continue;
               }
            } else if (choice.equals(">")) {
               if (orderList.size() < rowsPerPage || orderDao.selectAllByPage(pageNo + 1, rowsPerPage).isEmpty()) {
                  // ������ ������ ���� Ȯ��
                  System.out.println("����� ������ �Դϴ�.");
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
                  System.out.println("�ٽ� �Է��ϼ���.");
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
}
