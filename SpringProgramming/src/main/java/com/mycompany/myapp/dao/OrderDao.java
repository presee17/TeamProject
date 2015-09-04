package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.myapp.dto.Order;

public class OrderDao {
	private Connection conn;

	// 생성자 주입
	public OrderDao(Connection conn) {
		this.conn = conn;
	}

	// 데이터 접근 메소드
	public Integer insert(Order order) throws SQLException { //order객체를 매개변수로 받아 테이블에 insert하는 메소드
		Integer pk = null; 
		String sql = "insert into orders (member_id, order_price, order_date) values (?,?, NOW())";
		
		PreparedStatement pstmt = conn.prepareStatement(sql, new String[] { "ORDER_NO" });
		//order_no가 자동 증가하는 열로 sql문 생성
		
		pstmt.setString(1, order.getMemberId()); //Dto에 저장된 아이디를 가져오는 메서드 호출
		pstmt.setInt(2, order.getOrderPrice()); //Dto에 저장된 주문가격을 가져오는 메서드 호출
		
		// SQL문 준비  
		int row = pstmt.executeUpdate(); // 실행 후, 실행된 행 수 리턴
		
		if (row == 1) {
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				pk = rs.getInt(1); 
			}
			rs.close();
		}
		return pk; // insert 실패시 null 리턴
	}
	
	public int update(Order order) throws SQLException { //order객체를 매개변수로 받아 테이블에 update하는 메소드
		int rows = 0;
		String sql = "update orders set order_delivery=? where order_no = ?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, order.getOrderDelivery()); //Dto에 저장된 주문정보를 가져오는 메서드 호출
		pstmt.setInt(2, order.getOrderNo()); //Dto에 저장된 주문번호를 가져오는 메서드 호출

		rows = pstmt.executeUpdate(); // 실행 후, 실행된 행 수 리턴
		
		pstmt.close();
		
		return rows; //update 실패시 0 리턴
	}

	public int delete (int orderNo) throws SQLException {  //orderNo를 매개변수로 받아 해당 열을 delete하는 메소드
		int rows = 0;
		String sql = "delete from orders where order_no=?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, orderNo); 

		rows = pstmt.executeUpdate(); // 몇개의 행이 실행되었는지
		return rows;
	}

	public Order selectByPK(int orderNo) throws SQLException { //orderNo를 매개변수로 받아 해당 열을 select하는 메소드
		Order order = null;
		String sql = "select * from orders where order_no=?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, orderNo);
		ResultSet rs = pstmt.executeQuery(); // 주어진 SQL문을 결과로 만든 ResultSet을 반환하는 메소드 호출

		if (rs.next()) { // 커서를 다음으로 옮길 수 있을 때 true를 리턴하는 next()함수 호출
			order = new Order(); //새로운 DTO객체 생성자 호출
			order.setOrderNo(rs.getInt("order_no"));
			order.setMemberId(rs.getString("member_id"));
			order.setOrderPrice(rs.getInt("order_price"));
			order.setOrderDate(rs.getDate("order_date"));
			order.setOrderDelivery(rs.getString("order_delivery"));
		}
		rs.close();
		pstmt.close();
		return order;
	}
	
	//memberId, pagaNo, rowsPerPage를 매개변수로 해당 아이디에 해당하는 order list를 페이지로 생성하는 메소드
	public List<Order> selectByPage(String memberId, int pageNo, int rowsPerPage) throws SQLException {
		List<Order> list = new ArrayList<Order>();
		String sql = "SELECT * FROM "
				+ "orders "
				+ "WHERE member_id=? ORDER BY order_no DESC "
				+ "limit ?,? ";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, memberId);
		pstmt.setInt(2, (pageNo - 1) * rowsPerPage);
		pstmt.setInt(3, rowsPerPage);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) { // 커서를 다음으로 옮길 수 있을 때 true를 리턴하는 next()함수 호출
			Order order = new Order();
			order.setOrderNo(rs.getInt("order_No"));
			order.setMemberId(rs.getString("member_id"));
			order.setOrderPrice(rs.getInt("order_price"));
			order.setOrderDate(rs.getDate("order_date"));
			order.setOrderDelivery(rs.getString("order_delivery"));

			list.add(order);
		}
		rs.close();
		pstmt.close();
		return list;
	}
	
	//pagaNo, rowsPerPage를 매개변수로 모든 order 정보를 페이징한 list로 생성하는 메소드 (관리자용)
	public List<Order> selectAllByPage(int pageNo, int rowsPerPage) throws SQLException {
		List<Order> list = new ArrayList<Order>();
		String sql = "SELECT * FROM "
				+ "orders "
				+ "ORDER BY order_no DESC "
				+ "limit ?,? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, (pageNo-1)*rowsPerPage);
		pstmt.setInt(2, rowsPerPage);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()){
			Order order = new Order();
			order.setOrderNo(rs.getInt("order_no"));
			order.setMemberId(rs.getString("member_id"));
			order.setOrderPrice(rs.getInt("order_price"));
			order.setOrderDate(rs.getDate("order_date"));
			order.setOrderDelivery(rs.getString("order_delivery"));
			list.add(order);
		}
		rs.close();
		pstmt.close();
		return list;
	}
}
