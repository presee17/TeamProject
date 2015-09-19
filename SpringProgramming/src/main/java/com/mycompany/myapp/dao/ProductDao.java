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

import com.mycompany.myapp.dto.Board;
import com.mycompany.myapp.dto.Product;

@Component
public class ProductDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Integer insert(Product product) {
		Integer pk = null;
		String sql = "INSERT INTO products(product_name, product_price,product_detail) "
				+ "VALUES(?,?,?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn)
					throws SQLException {
				PreparedStatement pstmt = conn.prepareStatement(sql,
						new String[] { "product_no" });
				pstmt.setString(1, product.getName());
				pstmt.setInt(2, product.getPrice());
				pstmt.setString(3, product.getDetail());
				return pstmt;
			}
		}, keyHolder);
		Number keyNumber = keyHolder.getKey();
		pk = keyNumber.intValue();
		return pk;

	}

	public Integer updateProductName(Product product) {
		String sql = "UPDATE products SET product_name=? WHERE product_no=?";
		int rows = jdbcTemplate.update(sql, product.getName(),
				product.getNo());
		return rows;
	}

	public Integer updateProductPrice(Product product)  {
		String sql = "UPDATE products SET product_name=? WHERE product_no=?";
		int rows = jdbcTemplate.update(sql, product.getPrice(),
				product.getNo());
		return rows;

	}

	public Integer deleteByProductNo(int no)  {
		String sql = "DELETE FROM products WHERE product_no=?";

		int rows = jdbcTemplate.update(sql, no);
		return rows;
	}

	public Integer deleteAll() {
		String sql = "DELETE FROM products";
		int rows = jdbcTemplate.update(sql);

		return rows;
	}

	public Product selectByProductNo(int pNo){
		String sql = "SELECT * FROM products WHERE product_no=?";
		Product product = jdbcTemplate.queryForObject(sql,
				new Object[] { pNo }, new RowMapper<Product>() {
					@Override
					public Product mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Product product = new Product();
						product.setNo(rs.getInt("product_no"));
						product.setName(rs.getString("product_name"));
						product.setPrice(rs.getInt("product_price"));
						product.setDetail(rs.getString("product_detail"));
						return product;
					}
				});
		return product;
	}

	public String selectByProductNoReturnName(int pNo){
		String sql = "SELECT * FROM products WHERE product_no=?";
		Product product = jdbcTemplate.queryForObject(sql,
				new Object[] { pNo }, new RowMapper<Product>() {
					@Override
					public Product mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Product product = new Product();
						product.setNo(rs.getInt("product_no"));
						product.setName(rs.getString("product_name"));
						product.setPrice(rs.getInt("product_price"));
						product.setDetail(rs.getString("product_detail"));
						return product;
					}
				});
		return product.getName();
	}
	
	public int selectByProductNoReturnPrice(int pNo){
		String sql = "SELECT * FROM products WHERE product_no=?";
		Product product = jdbcTemplate.queryForObject(sql,
				new Object[] { pNo }, new RowMapper<Product>() {
					@Override
					public Product mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Product product = new Product();
						product.setNo(rs.getInt("product_no"));
						product.setName(rs.getString("product_name"));
						product.setPrice(rs.getInt("product_price"));
						product.setDetail(rs.getString("product_detail"));
						return product;
					}
				});
		return product.getPrice();
	}
	
	public List<Product> selectAllByPage(int pageNo, int rowsPerPage){
		String sql = "SELECT * FROM products LIMIT ?,?";
		List<Product> list = jdbcTemplate.query(sql, new Object[] {
				(pageNo - 1) * rowsPerPage, rowsPerPage },
				new RowMapper<Product>() {
					@Override
					public Product mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Product product = new Product();
						product.setNo(rs.getInt("product_no"));
						product.setName(rs.getString("product_name"));
						product.setPrice(rs.getInt("product_price"));
						product.setDetail(rs.getString("product_detail"));
						return product;
					}
				});
		return list;
	}

	public int selectCount() {
		String sql = "select count(*) from products";
		int rows = jdbcTemplate.queryForObject(sql, Integer.class);
		return rows;
	}
}
