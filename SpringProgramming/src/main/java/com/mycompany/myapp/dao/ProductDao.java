package test.shoppingmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import test.shoppingmall.vo.Product;

public class ProductDao {

	private Connection conn;
	
	public ProductDao(Connection conn) {
		this.conn = conn;
	}
	//�� ��ǰ ���
	public Integer insert(Product product) throws SQLException {
		int rows = 0;
		String sql = "INSERT INTO products(product_name, product_price) "
				+ "VALUES(?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, product.getpName());
		pstmt.setInt(2, product.getpPrice());
		
		rows = pstmt.executeUpdate();
		if(rows < 1) {
			System.out.println("�Է� ����");
		}
		
		pstmt.close();
		return rows;
	}
	//��ǰ �̸� ����
	public Integer updateProductName(Product product) throws SQLException {
		int rows = 0;
		String sql = "UPDATE products SET product_name=? WHERE product_no=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, product.getpName());
		pstmt.setInt(2, product.getpNo());
		
		rows = pstmt.executeUpdate();
		if(rows < 1) {
			System.out.println("�Է� ����");
		}
		
		pstmt.close();
		return rows;
	}
	//��ǰ ���� ����
	public Integer updateProductPrice(Product product) throws SQLException {
		int rows = 0;
		String sql = "UPDATE products SET product_price=? WHERE product_no=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(1, product.getpPrice());
		pstmt.setInt(2, product.getpNo());
		
		rows = pstmt.executeUpdate();
		if(rows < 1) {
			System.out.println("�Է� ����");
		}
		
		pstmt.close();
		return rows;
	}
	//��ǰ ����
	public Integer deleteByProductNo(int no) throws SQLException {
		int rows = 0;
		String sql = "DELETE FROM products WHERE product_no=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(1, no);
		rows = pstmt.executeUpdate();
		if(rows < 1) {
			System.out.println("��ǰ ���� ����");
		} else {
			System.out.println(no + "�� ��ǰ�� �����Ǿ����ϴ�.");
		}
		
		pstmt.close();
		return rows;
	}
	//��ϵ� ��ü ��ǰ ����
	public Integer deleteAll() throws SQLException {
		int rows = 0;
		String sql = "DELETE FROM products";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		rows = pstmt.executeUpdate();
		if(rows < 1) {
			System.out.println("��ǰ ���� �����߽��ϴ�.");
		} else {
			System.out.println("��� ��ǰ�� �����߽��ϴ�.");
		}
		
		pstmt.close();
		return rows;
	}
	//�ش� ��ǰ��ȣ�� �ش��ϴ� ��ǰ ����
	public Product selectByProductNo(int no) throws SQLException {
		Product product = null;
		String sql = "SELECT * FROM products WHERE product_no=?";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, no);
		
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			product = new Product();
			product.setpNo(rs.getInt("product_no"));
			product.setpName(rs.getString("product_name"));
			product.setpPrice(rs.getInt("product_price"));
		}
		
		rs.close();
		pstmt.close();
		
		return product;
	}
	//��ü ��ǰ���� ����¡
	public List<Product> selectAllByPage(int pageNo, int rowsPerPage) throws SQLException {
		List<Product> list = new ArrayList<Product>();
		String sql = "SELECT * FROM products LIMIT ?,?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, (pageNo-1)*rowsPerPage);
		pstmt.setInt(2, rowsPerPage);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Product product = new Product(); 
			product.setpNo(rs.getInt("product_no"));
			product.setpName(rs.getString("product_name"));
			product.setpPrice(rs.getInt("product_price"));
			list.add(product);
		}
		
		rs.close();
		pstmt.close();
		
		return list;
	}
	//��ü ��ǰ���� ����¡ ����
	public List<Product> selectAll() throws SQLException {
		List<Product> list = new ArrayList<Product>();
		String sql = "SELECT * FROM products";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Product product = new Product();
			product.setpNo(rs.getInt("product_no"));
			product.setpName(rs.getString("product_name"));
			product.setpPrice(rs.getInt("product_price"));
			list.add(product);
		}
		
		rs.close();
		pstmt.close();
		return list;
	}
}
