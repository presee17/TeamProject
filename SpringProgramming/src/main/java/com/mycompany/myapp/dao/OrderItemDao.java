package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.dto.Order;
import com.mycompany.myapp.dto.OrderItem;

@Component
public class OrderItemDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Integer insert(OrderItem orderitem){
		Integer pk = null;
		String sql = "insert into orderitems(order_no,product_no,orderitem_count,orderitem_price) "
				+ "values(?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement pstmt = conn.prepareStatement(sql,
						new String[]{"orderitem_no"	});
				pstmt.setInt(1, orderitem.getOrderNo());
				pstmt.setInt(2, orderitem.getProductNo());
				pstmt.setInt(3, orderitem.getOrderItemCount());
				pstmt.setInt(4, orderitem.getOrderItemPrice());
				return pstmt;
			}
			
		},keyHolder);
		Number keyNumber = keyHolder.getKey();
		pk = keyNumber.intValue();
		return pk;
	}

	public int update(OrderItem orderitem)  {
		String sql = "update orderitems set order_no=? ,product_no=?, orderitem_count=?,orderitem_price=? where orderitem_no=?";
		int rows = jdbcTemplate.update(sql, orderitem.getOrderNo(),
				orderitem.getProductNo(),orderitem.getOrderItemCount(),orderitem.getOrderItemPrice(),orderitem.getOrderItemNo());
		return rows;
	}

	public int delete(int orderNo) {
		String sql = "delete from orderitems where order_no=?";
		int rows= jdbcTemplate.update(sql,orderNo);
		return rows;
	}

		public List<OrderItem> selectByPage(int orderNo, int pageNo,
				int rowsPerPage){
			
			String sql = " SELECT a.order_no, a.product_no, a.orderitem_count, a.orderitem_price,c.product_price, c.product_name "
					+ " FROM orderitems a, products c "
					+ " WHERE a.product_no=c.product_no AND a.order_no = ? ORDER BY a.orderitem_no "
					+ " limit ?,? ";
			List<OrderItem> list = jdbcTemplate.query(sql, new Object[]{orderNo,
					((pageNo-1) * rowsPerPage),rowsPerPage},new RowMapper<OrderItem>() {

						@Override
						public OrderItem mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							OrderItem orderitem = new OrderItem();
							orderitem.setOrderNo(rs.getInt("order_no"));
							orderitem.setProductNo(rs.getInt("product_no"));
							orderitem.setOrderItemCount(rs.getInt("orderitem_count"));
							orderitem.setOrderItemPrice(rs.getInt("orderitem_price"));
							orderitem.setProductPrice(rs.getInt("product_price"));
							orderitem.setProductName(rs.getString("product_name"));
							

							return orderitem;
						}
			});
			
			return list;
	
	}
	//愿�由ъ옄瑜� 援ы쁽 �븞�븯�땲源� 紐⑤뱺 二쇰Ц�긽�꽭�젙蹂대뒗 �븞�븯�뒗嫄몃줈
	/* public List<OrderItem> selectAll() throws SQLException {
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
*/
	public int selectCount(int orderNo){
		String sql = "select count(*) from orderitems where order_no=?";
		int rows=jdbcTemplate.queryForObject(sql,new Object[]{orderNo},Integer.class);
		return rows;
	}
}
