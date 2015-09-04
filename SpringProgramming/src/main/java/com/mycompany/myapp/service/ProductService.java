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
	// 새 상품 등록 메소드
	public void insertProduct() {
		try {
			conn = ConnectionManager.getConnection();
			Product product = new Product();
			ProductDao productDao = new ProductDao(conn);
			conn.setAutoCommit(false);
			int rows = 0;
			
			while(rows==0){
				System.out.print("상품 이름: ");
				product.setpName(sc.nextLine());
				System.out.print("상품 가격: ");
				product.setpPrice(Integer.parseInt(sc.nextLine()));
				try {
					rows = productDao.insert(product);//디비에 상품 등록
					System.out.println(product.getpName() + "이 등록 되었습니다.");
				} catch (NumberFormatException e) {
					System.out.println("상품 가격에 숫자만 입력하세요");
					continue;
				} catch (Exception e) {
					break;
				}
			}
			conn.commit();
			
		} catch (Exception e) {
			System.out.println("입력 실패... 다시 시도해 주세요");
			try{conn.rollback();} catch (SQLException e1) {}
		} finally {
			try {conn.close();} catch (SQLException e) {e.printStackTrace();}
		}
	}
	// 상품 정보 보기 페이징
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
				System.out.format(" %-5s| \t     %-4s     \t| %-7s\n", "상품번호","상품명","상품 단가");
				System.out.println("----------------------------------------------------------------------");
				if(!list.isEmpty()) {//해당 페이지에 등록된 상품이 있는지
					for(Product product : list) {//있으면 상품 정보 출력
						System.out.format("%5d%25s%10d\n",product.getpNo(),product.getpName(),product.getpPrice());
					}
				} else {
					System.out.println("등록된 상품이 없습니다.");
					exit = true;
				}
				System.out.println("----------------------------------------------------------------------");
				System.out.println("현재 페이지: " + pageNo);
				System.out.println("----------------------------------------------------------------------");
				// MemberService와 동일
				if(pageNo <= 1) {
					System.out.println("입력(ex: 다음페이지: > | 보기종료: q");
				} else if((list.size() < rowsPerPage) || 
						productDao.selectAllByPage(pageNo+1, rowsPerPage).isEmpty()) {
					System.out.println("입력(ex: 이전페이지: < | 보기종료: q");
				} else {
					System.out.println("입력(ex: 이전페이지: <, 다음페이지: > | 보기종료: q");
				}
				System.out.print(": ");
				String choice = sc.nextLine();
				if(choice.equals("<")) {
					if(pageNo > 1) {
						pageNo -= 1;
						continue;
					} else {
						System.out.println("목록의 처음입니다.");
						continue;
					}
				} else if(choice.equals(">")) {
					if(list.size() < rowsPerPage ||
							productDao.selectAllByPage(pageNo+1, rowsPerPage).isEmpty()) {
						System.out.println("목록의 마지막 입니다.");
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
						System.out.println("다시 입력하세요.");
						continue;
					}
				}
			}
		} catch (Exception e) {
		} finally {
			try {conn.close();} catch (SQLException e) {e.printStackTrace();}
		}
	}
	// 상품 수정 메소드
	public void updateProduct() {
		try {
			conn = ConnectionManager.getConnection();
			ProductDao productDao = new ProductDao(conn);
			Product product = new Product();
			conn.setAutoCommit(false);
			
			while(true) {
				try{
					System.out.print("수정 할 상품 번호: ");
					int pNo = Integer.parseInt(sc.nextLine());
					product = productDao.selectByProductNo(pNo);
					if(product != null) {
						// 수정할 상품 정보 선택
						System.out.println();
						System.out.println("1. 상품 이름 수정  |  2. 상품 가격 수정");
						System.out.print(":");
						int choice = Integer.parseInt(sc.nextLine());
						if(choice == 1) {//상품 이름 수정
							System.out.println("수정 전 이름: " + product.getpName());
							System.out.print("수정 할 이름: ");
							String pName = sc.nextLine();
							product.setpNo(pNo);
							product.setpName(pName);
							productDao.updateProductName(product);
							break;
						} else if(choice == 2) {// 상품 가격 수정
							System.out.println("수정 전 가격: " + product.getpPrice());
							System.out.print("수정 할 가격: ");
							int pPrice = Integer.parseInt(sc.nextLine());
							product.setpNo(pNo);
							product.setpPrice(pPrice);
							productDao.updateProductPrice(product);
							break;
						} else {
							System.out.println("1-2번 사이의 숫자만 입력하세요.");
							continue;
						}
					} else {
						System.out.println("해당 상품이 없습니다. 다시 입력하세요.");
						continue;
					}
				} catch(NumberFormatException e) {
					System.out.println("숫자만 입력하세요.");
					continue;
				}
			}
		} catch (Exception e) {
			System.out.println("변경 실패...");
			try{conn.rollback();} catch (SQLException e1) {}
		} finally {
			try{conn.close();} catch(SQLException e) {}
		}
	}
}
