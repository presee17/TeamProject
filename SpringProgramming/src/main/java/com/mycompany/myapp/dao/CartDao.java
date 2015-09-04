package test.shoppingmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import test.shoppingmall.vo.Cart;


public class CartDao {
	private Connection conn;

	public CartDao(Connection conn) {
		this.conn = conn;
	}

	//cart에 데이터를 넣어준다.
	public Integer insert(Cart cart) throws SQLException {
		Integer pk = null;
		String sql = "insert into carts (member_id,product_no,cart_count, cart_price) values(?,?,?,?)";
		//member_id, product_no, cart_count, cart_price 순으로 입력하면 insert해준다.
		PreparedStatement pstmt = conn.prepareStatement(sql, new String[] {"cart_no"});

		pstmt.setString(1, cart.getMemberId());
		pstmt.setInt(2, cart.getProductNo());
		pstmt.setInt(3, cart.getCartCount());
		pstmt.setInt(4, cart.getCartPrice());
		int row = pstmt.executeUpdate();

		if (row == 1) {
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();

		}
		pstmt.close();
		return pk;
	}
	
	//한 상품을 삭제한다
	public int deleteOne(int productNo, String memberId) throws SQLException {
		int rows = 0;
		String sql = "delete from carts where product_no=? and member_id=?";
		//해당회원이 삭제하고자 하는 상품 번호를 받은 후 삭제한다.
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, productNo);		
		pstmt.setString(2, memberId);
		rows = pstmt.executeUpdate();
		pstmt.close();
		return rows;
	}
	
	//장바구니에 있는 모든 상품을 삭제한다.
	public void deleteAll(String memberId) throws SQLException {
		String sql = "delete from carts where member_id=?";
		//해당 회원의 장바구니를 전체삭제한다.
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, memberId);
		pstmt.executeUpdate();
		pstmt.close();
	}
	
	//회원이 같은 상품을 또 장바구니에 넣으면 count와 price를 새로 set한다.
	public int update(Cart cart) throws SQLException {
		int rows = 0;
		String sql = "update carts set cart_count=?,cart_price=? where product_no=? and member_id=?";

		PreparedStatement pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, cart.getCartCount());
		pstmt.setInt(2, cart.getCartPrice());
		pstmt.setInt(3, cart.getProductNo());
		pstmt.setString(4, cart.getMemberId());
		rows = pstmt.executeUpdate();

		pstmt.close();
		return rows;
	}

	// 쓸지안쓸지모르지만 일단 만들었어용
	public List<Cart> selectByPage(String memberId, int pageNo, int rowsPerPage) throws SQLException {
		List<Cart> list = new ArrayList<Cart>();
		String sql = "";
		sql += "select rn, product_no, product_name, cart_count, cart_price ";
		sql += "from ";
		sql += "( ";
		sql += "select rownum rn, product_no, product_name, cart_count, cart_price ";
		sql += "from ";
		sql += "( ";
		sql += "select c.product_no, p.product_name, c.cart_count, c.cart_price ";
		sql += "from carts c, products p ";
        sql += "where c.product_no=p.product_no and c.member_id=? ";
		sql += "order by c.product_no desc ";
		sql += ") ";
		sql += "where rownum<=? ";
		sql += ") ";
		sql += "where rn>=? ";

		PreparedStatement pstmt = conn.prepareStatement(sql);

        pstmt.setString(1, memberId);
		pstmt.setInt(2, pageNo * rowsPerPage);
		pstmt.setInt(3, (pageNo - 1) * rowsPerPage + 1);
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			Cart carts = new Cart();
			carts.setProductNo(rs.getInt("product_no"));
			carts.setProductName(rs.getString("product_name"));
			carts.setCartCount(rs.getInt("cart_count"));
			carts.setCartPrice(rs.getInt("cart_price"));
			list.add(carts);
		}
		rs.close();
		pstmt.close();
		return list;
	}

	//회원이 산 장바구니 목록을 보여준다.
	public List<Cart> selectByMemberId(String memberId) throws SQLException {

		List<Cart> list = new ArrayList<Cart>();
		
		//carts 테이블과 product 테이블을 조인해서 값을 가져옴!
		String sql = "select p.product_no, p.product_name, c.cart_count, c.cart_price ";
        sql += "from carts c, products p ";
        sql += "where p.product_no=c.product_no and member_id=?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, memberId);
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			Cart carts = new Cart();
			carts.setProductNo(rs.getInt("product_no"));
			carts.setProductName(rs.getString("product_name"));
			carts.setCartCount(rs.getInt("cart_count"));
			carts.setCartPrice(rs.getInt("cart_price"));
			carts.setMemberId(memberId);

			list.add(carts);
		}
		rs.close();
		pstmt.close();
		return list;
	}

	public List<Cart> selectProductNo(String memberId) throws SQLException{
		List<Cart> list = new ArrayList<Cart>();
		String sql="select product_no from products where member_id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, memberId);
		ResultSet rs = pstmt.executeQuery();
		
		while (rs.next()) {
			Cart carts = new Cart();
			carts.setProductNo(rs.getInt("product_no"));
			carts.setMemberId(memberId);

			list.add(carts);
		}
		rs.close();
		pstmt.close();
		return list;
	}

}
