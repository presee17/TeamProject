package test.shoppingmall.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import test.shoppingmall.vo.Member;

public class MemberDao {
	private Connection conn;
	
	public MemberDao(Connection conn) {
		this.conn = conn;
	}
	
	// 멤버 등록.
	public Integer insert(Member member) throws SQLException{
		int rows = 0;
		String sql = "INSERT INTO members VALUES(?,?,?,?)";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, member.getId());
		pstmt.setString(2, member.getName());
		pstmt.setString(3, member.getPw());
		pstmt.setInt(4, member.getIsAdmin());
		
		rows = pstmt.executeUpdate();
		
		pstmt.close();
		return rows;
	}
	//멤버 등급 수정
	public Integer updateMemberGrade(Member member) throws SQLException {
		int rows = 0;
		String sql = "UPDATE members SET member_isadmin=? WHERE member_id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(1, member.getIsAdmin());
		pstmt.setString(2, member.getId());
		rows = pstmt.executeUpdate();
		if(rows < 1) {
			System.out.println("변경 실패");
		} else {
			System.out.println(member.getId() + "님이 등급이 변경되었습니다.");
		}
		
		pstmt.close();
		return rows;
	}
	//멤버 비밀번호 수정
	public Integer updateMemberPassword(Member member) throws SQLException {
		int rows = 0;
		String sql = "UPDATE members SET member_password=? WHERE member_id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, member.getPw());
		pstmt.setString(2, member.getId());
		rows = pstmt.executeUpdate();
		if(rows < 1) {
			System.out.println("비밀번호 변경 실패");
		} else {
			System.out.println(member.getId() + "님의 비밀번호 변경이 성공했습니다.");
		}
		
		pstmt.close();
		return rows;
	}
	//멤버 삭제
	public Integer delete(String id) throws SQLException {
		int rows = 0;
		String sql = "DELETE FROM members WHERE member_id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setString(1, id);
		rows = pstmt.executeUpdate();
		if(rows < 1) {
			System.out.println("삭제 실패");
		} else {
			System.out.println(id + "가 삭제 되었습니다.");
		}
		
		pstmt.close();
		return rows;
	}
	//입력한 ID에 해당하는 멤버
	public Member selectById(String id) throws SQLException {
		Member member = null;
		String sql = "SELECT * FROM members WHERE member_id=?";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, id);
		
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			member = new Member();
			member.setId(rs.getString("member_id"));
			member.setPw(rs.getString("member_password"));
			member.setName(rs.getString("member_name"));
			member.setIsAdmin(rs.getInt("member_isadmin"));
		}
		
		rs.close();
		pstmt.close();
		
		return member;
	}
	//전체 멤버 리스트 페이징
	public List<Member> selectAllByPage(int pageNo, int rowsPerPage) throws SQLException {
		List<Member> list = new ArrayList<Member>();
		String sql = "SELECT member_id, member_name, member_isadmin "
				+ "FROM members LIMIT ?,?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(1, (pageNo-1)*rowsPerPage);
		pstmt.setInt(2, rowsPerPage);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Member member = new Member();
			member.setId(rs.getString("member_id"));
			member.setName(rs.getString("member_name"));
			member.setIsAdmin(rs.getInt("member_isadmin"));
			list.add(member);
		}
		
		rs.close();
		pstmt.close();
		return list;
	}
	//전체 멤버 리스트 페이징 없이
	public List<Member> selectAll() throws SQLException {
		List<Member> list = new ArrayList<Member>();
		String sql = "SELECT * FROM members";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Member member = new Member();
			member.setId(rs.getString("member_id"));
			member.setPw(rs.getString("member_password"));
			member.setName(rs.getString("member_name"));
			member.setIsAdmin(rs.getInt("member_isadmin"));
		}
		
		rs.close();
		pstmt.close();
		return list;
	}
}
