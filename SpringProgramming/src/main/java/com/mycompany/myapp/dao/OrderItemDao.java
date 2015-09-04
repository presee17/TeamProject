package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.myapp.dto.OrderItem;

public class OrderItemDao {
	private Connection conn;
	
	public OrderItemDao(Connection conn){
		this.conn = conn;
	}
	public Integer insert(OrderItem orderitem) throws SQLException{
		Integer pk = null;
		String sql = "insert into orderitems(order_no,product_no,orderitem_count,orderitem_price) "
				+ "values(?,?,?,?)";
		PreparedStatement pstmt= conn.prepareStatement(sql,new String[] {"orderitem_no"});
		
		pstmt.setInt(1,orderitem.getOrderNo() );
		pstmt.setInt(2, orderitem.getProductNo());
		pstmt.setInt(3, orderitem.getOrderItemCount());
		pstmt.setInt(4, orderitem.getOrderItemPrice());
		int rows = pstmt.executeUpdate();
		if (rows == 1) {
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
		}

		pstmt.close();
		return pk;
	}

	public int update(OrderItem orderitem) throws SQLException {
		int rows = 0;
		String sql = "update orderitems set order_no=? ,product_no=?, orderitem_count=?,orderitem_price=? where orderitem_no=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, orderitem.getOrderNo());
		pstmt.setInt(2, orderitem.getProductNo());
		pstmt.setInt(3, orderitem.getOrderItemCount());
		pstmt.setInt(4, orderitem.getOrderItemPrice());
		pstmt.setInt(5, orderitem.getOrderItemNo());
		rows = pstmt.executeUpdate();
		pstmt.close();
		return rows;
	}

	public int delete(int orderItemNo) throws SQLException {
		int rows = 0;
		String sql = "delete from orderitems where orderitem_no=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, orderItemNo);
		rows = pstmt.executeUpdate();
		pstmt.close();
		return rows;
	}

	public List<OrderItem> selectByorderNo(int orderNo, int pageNo,
			int rowsPerPage) throws SQLException {
		List<OrderItem> list = new ArrayList<OrderItem>();
		String sql = " SELECT a.order_no, a.product_no, a.orderitem_count, a.orderitem_price,c.product_price, c.product_name "
				+ " FROM orderitems a, products c "
				+ " WHERE a.product_no=c.product_no AND a.order_no = ? ORDER BY a.orderitem_no "
				+ " limit ?,? ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, orderNo);
		pstmt.setInt(2, (pageNo-1)*rowsPerPage);
		pstmt.setInt(3, rowsPerPage);
		
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			OrderItem orderitem = new OrderItem();
			orderitem.setOrderNo(rs.getInt("a.order_no"));
			orderitem.setOrderItemCount(rs.getInt("a.orderitem_count"));
			orderitem.setOrderItemPrice(rs.getInt("a.orderitem_price"));
			orderitem.setProductPrice(rs.getInt("c.product_price"));
			orderitem.setProductNo(rs.getInt("a.product_no"));
			orderitem.setProductName(rs.getString("c.product_name"));
			// orderitem.setOrderItemNo( rs.getInt("orderitem_no") );
			list.add(orderitem);
		}	
	
		rs.close();	
	
		pstmt.close();	
		return list;
	
	}

	public List<OrderItem> selectAll() throws SQLException {
		List<OrderItem> list = new ArrayList<OrderItem>();
		String sql = "select b.order_no, b.orderitem_count, b.orderitem_price, b.product_no, c.product_name "
				+ "from orderitems b, products c "
				+ "where b.product_no=c.product_no ";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			OrderItem orderitem = new OrderItem();
			orderitem.setOrderNo(rs.getInt("order_no"));
			orderitem.setOrderItemCount(rs.getInt("orderitem_count"));
			orderitem.setOrderItemPrice(rs.getInt("product_price"));
			orderitem.setProductNo(rs.getInt("product_no"));
			orderitem.setProductName(rs.getString("product_name"));

			list.add(orderitem);
		}
		rs.close();
		pstmt.close();

		return list;
	}

}
