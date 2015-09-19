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

//github.com/presee17/TeamProject.git
import com.mycompany.myapp.dto.Cart;


@Component
public class CartDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//cart에 데이터를 넣어준다.
	public Integer insert(Cart cart){
		Integer pk = null;
		String sql = "insert into carts (member_id,product_no,product_name,cart_count,cart_price) values(?,?,?,?,?)";
		//member_id, product_no, cart_count, cart_price 순으로 입력하면 insert해준다.
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"cart_no"});
				pstmt.setString(1, cart.getMemberId());
				pstmt.setInt(2, cart.getProductNo());
				pstmt.setString(3, cart.getProductName());
				pstmt.setInt(4, cart.getCartCount());
				pstmt.setInt(5, cart.getCartPrice());
				return pstmt;
			}
		}, keyHolder);
		Number keyNumber = keyHolder.getKey();
		pk = keyNumber.intValue();
		return pk;
	}
	
	//한 상품을 삭제한다
	public int deleteOne(int cartNo, String memberId){
		String sql = "delete from carts where cart_no=? and member_id=?";
		int rows = jdbcTemplate.update(sql,cartNo,memberId);
		return rows;
	}

	//장바구니에서 정해진 카트번호에 해당하는 목록을 삭제한다.
		public int deleteByPk(String memberId, int cartNo){
			String sql = "delete from carts where member_id=? and cart_no=?";
			//해당 회원의 장바구니를 전체삭제한다.
			int rows = jdbcTemplate.update(sql,memberId, cartNo);
			return rows;
		}
		
	//회원이 같은 상품을 또 장바구니에 넣으면 count와 price를 새로 set한다.
	public int update(Cart cart){
		String sql = "update carts set cart_count=?,cart_price=? where product_no=? and member_id=?";
		int rows = jdbcTemplate.update(sql, cart.getCartCount(), cart.getCartPrice(), cart.getProductNo(),cart.getMemberId());
		return rows;
	}

	//회원이 산 장바구니 목록을 보여준다.
	public List<Cart> selectByMemberId(String memberId){
		//carts 테이블과 product 테이블을 조인해서 값을 가져옴!
		String sql = "select p.product_no, p.product_name, c.cart_count, c.cart_price ";
        sql += "from carts c, products p ";
        sql += "where p.product_no=c.product_no and c.member_id=? ";
    	sql += "order by p.product_no desc ";
        
		List<Cart> list = jdbcTemplate.query(sql, new Object[] {memberId},
				new RowMapper<Cart>() {
					@Override
					public Cart mapRow(ResultSet rs, int rowNum) throws SQLException {
						Cart cart = new Cart();
						cart.setProductNo(rs.getInt("product_no"));
						cart.setProductName(rs.getString("product_name"));
						cart.setCartCount(rs.getInt("cart_count"));
						cart.setCartPrice(rs.getInt("cart_price"));
						return cart;
					}
				});
		return list;
	}

	//회원이 산 상품번호 
	public List<Cart> selectProductNo(String memberId){
		String sql="select product_no,cart_count,cart_price from carts where member_id=?";
		List<Cart> list = jdbcTemplate.query(sql, new Object[] {memberId},
				new RowMapper<Cart>() {
					@Override
					public Cart mapRow(ResultSet rs, int rowNum) throws SQLException {
						Cart cart = new Cart();
						cart.setProductNo(rs.getInt("product_no"));
						cart.setCartCount(rs.getInt("cart_count"));
						cart.setCartPrice(rs.getInt("cart_price"));

						cart.setMemberId(memberId);
						return cart;
					}
				});
		return list;
	}

	public int selectCount() {
		String sql = "select count(*) from carts";
		int rows = jdbcTemplate.queryForObject(sql, Integer.class);
		return rows;
	}
}
