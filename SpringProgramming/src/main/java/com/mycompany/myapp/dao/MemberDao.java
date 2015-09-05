package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.dto.Member;

@Component
public class MemberDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// ��� ���.
	public String insert(Member member){
		String sql = "insert into members values(?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, member.getId());
				pstmt.setString(2, member.getName());
				pstmt.setString(3, member.getPw());
				return pstmt;
			}
		}, keyHolder);
		return member.getId();
	}
	 
	public Member selectById(String id){
		String sql = "SELECT * FROM members WHERE member_id=?";
		Member member = jdbcTemplate.queryForObject(sql, new Object[] { id }, new RowMapper<Member>() {
			@Override
			public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
				Member member = new Member();
				member.setId(rs.getString("member_id"));
				member.setPw(rs.getString("member_password"));
				member.setName(rs.getString("member_name"));
				return member;
			}
		});
		return member;
	}
}
