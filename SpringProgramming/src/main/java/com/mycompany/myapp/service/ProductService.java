package com.mycompany.myapp.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.mycompany.myapp.dao.ProductDao;
import com.mycompany.myapp.dto.ConnectionManager;
import com.mycompany.myapp.dto.Product;

public class ProductService {
	private Connection conn;
	private Scanner sc;
	
	public ProductService(Scanner sc) {
		this.sc = sc;
	}
	// �� ��ǰ ��� �޼ҵ�
	public void insertProduct() {
		try {
			conn = ConnectionManager.getConnection();
			Product product = new Product();
			ProductDao productDao = new ProductDao(conn);
			conn.setAutoCommit(false);
			int rows = 0;
			
			while(rows==0){
				System.out.print("��ǰ �̸�: ");
				product.setpName(sc.nextLine());
				System.out.print("��ǰ ����: ");
				product.setpPrice(Integer.parseInt(sc.nextLine()));
				try {
					rows = productDao.insert(product);//��� ��ǰ ���
					System.out.println(product.getpName() + "�� ��� �Ǿ����ϴ�.");
				} catch (NumberFormatException e) {
					System.out.println("��ǰ ���ݿ� ���ڸ� �Է��ϼ���");
					continue;
				} catch (Exception e) {
					break;
				}
			}
			conn.commit();
			
		} catch (Exception e) {
			System.out.println("�Է� ����... �ٽ� �õ��� �ּ���");
			try{conn.rollback();} catch (SQLException e1) {}
		} finally {
			try {conn.close();} catch (SQLException e) {e.printStackTrace();}
		}
	}
	// ��ǰ ���� ���� ����¡
	public void showProduct() {
		try {
			conn = ConnectionManager.getConnection();
			List<Product> list = new ArrayList<Product>();
			ProductDao productDao = new ProductDao(conn);
			int pageNo = 1;
			int rowsPerPage = 5;
			boolean exit = false;
			
			while(!exit) {
				list = productDao.selectAllByPage(pageNo, rowsPerPage);
				System.out.println("----------------------------------------------------------------------");
				System.out.format(" %-5s| \t     %-4s     \t| %-7s\n", "��ǰ��ȣ","��ǰ��","��ǰ �ܰ�");
				System.out.println("----------------------------------------------------------------------");
				if(!list.isEmpty()) {//�ش� �������� ��ϵ� ��ǰ�� �ִ���
					for(Product product : list) {//������ ��ǰ ���� ���
						System.out.format("%5d%25s%10d\n",product.getpNo(),product.getpName(),product.getpPrice());
					}
				} else {
					System.out.println("��ϵ� ��ǰ�� �����ϴ�.");
					exit = true;
				}
				System.out.println("----------------------------------------------------------------------");
				System.out.println("���� ������: " + pageNo);
				System.out.println("----------------------------------------------------------------------");
				// MemberService�� ����
				if(pageNo <= 1) {
					System.out.println("�Է�(ex: ����������: > | ��������: q");
				} else if((list.size() < rowsPerPage) || 
						productDao.selectAllByPage(pageNo+1, rowsPerPage).isEmpty()) {
					System.out.println("�Է�(ex: ����������: < | ��������: q");
				} else {
					System.out.println("�Է�(ex: ����������: <, ����������: > | ��������: q");
				}
				System.out.print(": ");
				String choice = sc.nextLine();
				if(choice.equals("<")) {
					if(pageNo > 1) {
						pageNo -= 1;
						continue;
					} else {
						System.out.println("����� ó���Դϴ�.");
						continue;
					}
				} else if(choice.equals(">")) {
					if(list.size() < rowsPerPage ||
							productDao.selectAllByPage(pageNo+1, rowsPerPage).isEmpty()) {
						System.out.println("����� ������ �Դϴ�.");
						continue;
					} else {
						pageNo += 1;
						continue;
					}
				} else if(choice.equals("q")) {
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
		} finally {
			try {conn.close();} catch (SQLException e) {e.printStackTrace();}
		}
	}
	// ��ǰ ���� �޼ҵ�
	public void updateProduct() {
		try {
			conn = ConnectionManager.getConnection();
			ProductDao productDao = new ProductDao(conn);
			Product product = new Product();
			conn.setAutoCommit(false);
			
			while(true) {
				try{
					System.out.print("���� �� ��ǰ ��ȣ: ");
					int pNo = Integer.parseInt(sc.nextLine());
					product = productDao.selectByProductNo(pNo);
					if(product != null) {
						// ������ ��ǰ ���� ����
						System.out.println();
						System.out.println("1. ��ǰ �̸� ����  |  2. ��ǰ ���� ����");
						System.out.print(":");
						int choice = Integer.parseInt(sc.nextLine());
						if(choice == 1) {//��ǰ �̸� ����
							System.out.println("���� �� �̸�: " + product.getpName());
							System.out.print("���� �� �̸�: ");
							String pName = sc.nextLine();
							product.setpNo(pNo);
							product.setpName(pName);
							productDao.updateProductName(product);
							break;
						} else if(choice == 2) {// ��ǰ ���� ����
							System.out.println("���� �� ����: " + product.getpPrice());
							System.out.print("���� �� ����: ");
							int pPrice = Integer.parseInt(sc.nextLine());
							product.setpNo(pNo);
							product.setpPrice(pPrice);
							productDao.updateProductPrice(product);
							break;
						} else {
							System.out.println("1-2�� ������ ���ڸ� �Է��ϼ���.");
							continue;
						}
					} else {
						System.out.println("�ش� ��ǰ�� �����ϴ�. �ٽ� �Է��ϼ���.");
						continue;
					}
				} catch(NumberFormatException e) {
					System.out.println("���ڸ� �Է��ϼ���.");
					continue;
				}
			}
		} catch (Exception e) {
			System.out.println("���� ����...");
			try{conn.rollback();} catch (SQLException e1) {}
		} finally {
			try{conn.close();} catch(SQLException e) {}
		}
	}
}
