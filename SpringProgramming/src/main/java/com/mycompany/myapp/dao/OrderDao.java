package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.dto.Order;

@Component
public class OrderDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 데이터 접근 메소드
	public Integer insert(Order order) { // order객체를 매개변수로 받아 테이블에 insert하는 메소드
		Integer pk = null;
		String sql = "insert into orders (member_id, order_price, order_date) values (?,?, NOW())";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				// order_no가 자동 증가하는 열로 sql문 생성
				PreparedStatement pstmt = conn.prepareStatement(sql, new String[] { "ORDER_NO" });
				pstmt.setString(1, order.getMemberId()); // Dto에 저장된 아이디를 가져오는
															// 메서드 호출
				pstmt.setInt(2, order.getOrderPrice()); // Dto에 저장된 주문가격을 가져오는
														// 메서드 호출
				return pstmt;
			}
		}, keyHolder);
		Number keyNumber = keyHolder.getKey();
		pk = keyNumber.intValue();
		return pk;
	}

	/*public int update(Order order) { // order객체를 매개변수로 받아
										// 테이블에 update하는 메소드
		String sql = "update orders set order_delivery=? where order_no = ?";
		int rows = jdbcTemplate.update(sql, order.getOrderDelivery(), order.getOrderNo());
		return rows;
	}*/

	public int delete(int orderNo) { // orderNo를 매개변수로 받아 해당
										// 열을 delete하는 메소드
		String sql = "delete from orders where order_no=?";
		int rows = jdbcTemplate.update(sql, orderNo);
		return rows;
	}

	public Order selectByPK(int orderNo) throws SQLException { // orderNo를 매개변수로
																// 받아 해당 열을
																// select하는 메소드
		String sql = "select * from orders where order_no=?";
		Order order = jdbcTemplate.queryForObject(sql, new Object[] { orderNo }, new RowMapper<Order>() {
			@Override
			public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
				Order order = new Order(); // 새로운 DTO객체 생성자 호출
				order.setOrderNo(rs.getInt("order_no"));
				order.setMemberId(rs.getString("member_id"));
				order.setOrderPrice(rs.getInt("order_price"));
				order.setOrderDate(rs.getDate("order_date"));
				order.setOrderDelivery(rs.getString("order_delivery"));
				return order;
			}
		});
		return order;
	}

	// memberId, pagaNo, rowsPerPage를 매개변수로 해당 아이디에 해당하는 order list를 페이지로 생성하는
	// 메소드
	public List<Order> selectByPage(String memberId, int pageNo, int rowsPerPage) {
		String sql = "SELECT * FROM " + "orders " + "WHERE member_id=? ORDER BY order_no DESC " + "limit ?,? ";

		List<Order> list = jdbcTemplate.query(sql, new Object[] { (pageNo - 1) * rowsPerPage, rowsPerPage },
				new RowMapper<Order>() {
					@Override
					public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
						Order order = new Order();
						order.setOrderNo(rs.getInt("order_No"));
						order.setMemberId(rs.getString("member_id"));
						order.setOrderPrice(rs.getInt("order_price"));
						order.setOrderDate(rs.getDate("order_date"));
						order.setOrderDelivery(rs.getString("order_delivery"));
						return order;
					}
				});
		return list;
	}

	// pagaNo, rowsPerPage를 매개변수로 모든 order 정보를 페이징한 list로 생성하는 메소드 (관리자용)
	public List<Order> selectAllByPage(int pageNo, int rowsPerPage) {
		String sql = "SELECT * FROM " + "orders " + "ORDER BY order_no DESC " + "limit ?,? ";
		List<Order> list = jdbcTemplate.query(sql, new Object[] { (pageNo - 1) * rowsPerPage, rowsPerPage },
				new RowMapper<Order>() {
					@Override
					public Order mapRow(ResultSet rs, int rownum) throws SQLException {
						Order order = new Order();
						order.setOrderNo(rs.getInt("order_no"));
						order.setMemberId(rs.getString("member_id"));
						order.setOrderPrice(rs.getInt("order_price"));
						order.setOrderDate(rs.getDate("order_date"));
						order.setOrderDelivery(rs.getString("order_delivery"));
						return order;
					}
				});
		return list;
	}
}
