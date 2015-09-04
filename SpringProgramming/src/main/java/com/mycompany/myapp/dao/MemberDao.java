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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.dto.Member;

@Component
public class MemberDao {
	private Connection conn;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// ��� ���.
	public Integer insert(Member member){
		Integer pk = 0;
		String sql = "INSERT INTO members VALUES(?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement pstmt = conn.prepareStatement(sql, new String[] { "board_no" });
				pstmt.setString(1, member.getId());
				pstmt.setString(2, member.getName());
				pstmt.setString(3, member.getPw());
				pstmt.setInt(4, member.getIsAdmin());
				return pstmt;
			}
		}, keyHolder);
		Number keyNumber = keyHolder.getKey();
		pk = keyNumber.intValue();
		return pk;
	}


	// ��� ��й�ȣ ����
	public Integer updateMemberPassword(Member member) throws SQLException {
		int rows = 0;
		String sql = "UPDATE members SET member_password=? WHERE member_id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, member.getPw());
		pstmt.setString(2, member.getId());
		rows = pstmt.executeUpdate();
		if (rows < 1) {
			System.out.println("��й�ȣ ���� ����");
		} else {
			System.out.println(member.getId() + "���� ��й�ȣ ������ �����߽��ϴ�.");
		}

		pstmt.close();
		return rows;
	}

	// �Է��� ID�� �ش��ϴ� ���
	public Member selectById(String id) throws SQLException {
		Member member = null;
		String sql = "SELECT * FROM members WHERE member_id=?";

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, id);

		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
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
}
