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

	// ������ ����
	public OrderDao(Connection conn) {
		this.conn = conn;
	}

	// ������ ���� �޼ҵ�
	public Integer insert(Order order) throws SQLException { //order��ü�� �Ű������� �޾� ���̺� insert�ϴ� �޼ҵ�
		Integer pk = null; 
		String sql = "insert into orders (member_id, order_price, order_date) values (?,?, NOW())";
		
		PreparedStatement pstmt = conn.prepareStatement(sql, new String[] { "ORDER_NO" });
		//order_no�� �ڵ� �����ϴ� ���� sql�� ����
		
		pstmt.setString(1, order.getMemberId()); //Dto�� ����� ���̵� �������� �޼��� ȣ��
		pstmt.setInt(2, order.getOrderPrice()); //Dto�� ����� �ֹ������� �������� �޼��� ȣ��
		
		// SQL�� �غ�  
		int row = pstmt.executeUpdate(); // ���� ��, ����� �� �� ����
		
		if (row == 1) {
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				pk = rs.getInt(1); 
			}
			rs.close();
		}
		return pk; // insert ���н� null ����
	}
	
	public int update(Order order) throws SQLException { //order��ü�� �Ű������� �޾� ���̺� update�ϴ� �޼ҵ�
		int rows = 0;
		String sql = "update orders set order_delivery=? where order_no = ?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, order.getOrderDelivery()); //Dto�� ����� �ֹ������� �������� �޼��� ȣ��
		pstmt.setInt(2, order.getOrderNo()); //Dto�� ����� �ֹ���ȣ�� �������� �޼��� ȣ��

		rows = pstmt.executeUpdate(); // ���� ��, ����� �� �� ����
		
		pstmt.close();
		
		return rows; //update ���н� 0 ����
	}

	public int delete (int orderNo) throws SQLException {  //orderNo�� �Ű������� �޾� �ش� ���� delete�ϴ� �޼ҵ�
		int rows = 0;
		String sql = "delete from orders where order_no=?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, orderNo); 

		rows = pstmt.executeUpdate(); // ��� ���� ����Ǿ�����
		return rows;
	}

	public Order selectByPK(int orderNo) throws SQLException { //orderNo�� �Ű������� �޾� �ش� ���� select�ϴ� �޼ҵ�
		Order order = null;
		String sql = "select * from orders where order_no=?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, orderNo);
		ResultSet rs = pstmt.executeQuery(); // �־��� SQL���� ����� ���� ResultSet�� ��ȯ�ϴ� �޼ҵ� ȣ��

		if (rs.next()) { // Ŀ���� �������� �ű� �� ���� �� true�� �����ϴ� next()�Լ� ȣ��
			order = new Order(); //���ο� DTO��ü ������ ȣ��
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
	
	//memberId, pagaNo, rowsPerPage�� �Ű������� �ش� ���̵� �ش��ϴ� order list�� �������� �����ϴ� �޼ҵ�
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

		while (rs.next()) { // Ŀ���� �������� �ű� �� ���� �� true�� �����ϴ� next()�Լ� ȣ��
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
	
	//pagaNo, rowsPerPage�� �Ű������� ��� order ������ ����¡�� list�� �����ϴ� �޼ҵ� (�����ڿ�)
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
