package com.mycompany.myapp.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

import com.mycompany.myapp.dao.MemberDao;
import com.mycompany.myapp.dto.ConnectionManager;
import com.mycompany.myapp.dto.Member;

public class MemberService {
	private Connection conn;
	private Scanner sc;
	
	public MemberService(Scanner sc) {
		this.sc = sc;
	}
	
	//È¸¿ø °¡ÀÔ ¸Ş¼Òµå
	public void join() {
		try {
			conn = ConnectionManager.getConnection();
			Member member = new Member();
			MemberDao memberDao = new MemberDao(conn);
			conn.setAutoCommit(false);
			int rows = 0;
			while(rows==0){
				// ÀÔ·Â ¼º°øÇÏ¸é while loop Å»Ãâ
				System.out.print("ID: ");
				String memberId = sc.nextLine();
				member.setId(memberId);
				if(memberDao.selectById(memberId) != null) {
					System.out.println("ÀÌ¹Ì ÀÖ´Â ¾ÆÀÌµğ ÀÔ´Ï´Ù.");
					continue;
				}
				System.out.print("ÀÌ¸§: ");
				member.setName(sc.nextLine());
				System.out.print("PW: ");
				member.setPw(sc.nextLine());
				member.setIsAdmin(0);
				
				try {
					rows = memberDao.insert(member); // memberDao insert ¸Ş¼Òµå. ¸Å°³º¯¼ö·Î member
					System.out.println(member.getId() + "´ÔÀÌ °¡ÀÔÇÏ¼Ì½À´Ï´Ù.");
				} catch (SQLException e) {
					System.out.println("°¡ÀÔ ½ÇÆĞ. ÀÔ·Â ³»¿ëÀ» È®ÀÎÇÏ¼¼¿ä.");
				}
			}
			conn.commit();
		} catch (Exception e1) {
			System.out.println("°¡ÀÔ ½ÇÆĞ... ´Ù½Ã ½ÃµµÇØ ÁÖ¼¼¿ä.");
			try{conn.rollback();} catch (SQLException e) {}
		} finally {
			try {conn.close();} catch (Exception e) {}
		}
	}
	
	//·Î±×ÀÎ ¸Ş¼Òµå
	public Member login() {
		Member member = null;
		try {
			conn = ConnectionManager.getConnection();
			MemberDao memberDao = new MemberDao(conn);
			boolean login = false;
			
			while(!login) {
				System.out.print("ID: ");
				String id = sc.nextLine();
				System.out.print("PW: ");
				String pw = sc.nextLine();
				try {
					// ¸â¹ö°¡ ÀÖ´ÂÁö ¾ø´ÂÁö °Ë»ç
					member = memberDao.selectById(id);
					if(member == null) {//selectById°¡ nullÀ» ¸®ÅÏÇÏ¸é ¾ø´Â ¾ÆÀÌµğ
						System.out.println("¾ÆÀÌµğ°¡ ¾ø½À´Ï´Ù. ¾ÆÀÌµğ¸¦ È®ÀÎÇÏ¼¼¿ä.");
						continue; 
					} else {//¾ÆÀÌµğ°¡ ÀÖÀ¸¸é ÆĞ½º¿öµå °Ë»ç
						if(member.getPw().equals(pw)) {//ÀÔ·Â¹ŞÀº ºñ¹Ğ¹øÈ£¶û µğºñ ºñ¹Ğ¹øÈ£ ºñ±³ÇØ¼­ ·Î±×ÀÎ
							System.out.println("·Î±×ÀÎ ÇÏ¼Ì½À´Ï´Ù.");
							login = true;
						} else {
							System.out.println("ºñ¹Ğ¹øÈ£¸¦ È®ÀÎÇÏ¼¼¿ä.");
							continue;
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {conn.close();} catch (SQLException e) {}
		}
		return member; //member ¸®ÅÏ. ÇöÀç ·Î±×ÀÎ ÇÑ ¸â¹ö°¡ ´©±ºÁö ¾Ë±â À§ÇØ
	}
	
	//¸â¹ö ¸®½ºÆ® ÆäÀÌÂ¡ ¸Ş¼Òµå. ÇÑ ÆäÀÌÁö¿¡ 5¸í¾¿
	public void showMemberList() {
		try {
			conn = ConnectionManager.getConnection();
			List<Member> memberList = new ArrayList<Member>();
			MemberDao memberDao = new MemberDao(conn);
			int pageNo = 1;
			int rowsPerPage = 5;
			boolean exit = false;
			
			while(!exit) {
				memberList = memberDao.selectAllByPage(pageNo, rowsPerPage);
				System.out.println("-----------------------------------------------------");
				System.out.println("    ¾ÆÀÌµğ    |     ÀÌ¸§     |     °ü¸®ÀÚ");
				System.out.println("-----------------------------------------------------");
				if(!memberList.isEmpty()){//ÇØ´ç ÆäÀÌÁö¿¡ member°¡ ÀÖ´ÂÁö ¾ø´ÂÁö °Ë»ç
					for(Member m : memberList) {// ÀÖÀ¸¸é ¸â¹ö ¸®½ºÆ® Ãâ·Â
						System.out.print("     " + m.getId() + "          " + m.getName());
						if(m.getIsAdmin() == 1) {System.out.println("          yes");}
						else {System.out.println("           no");}
					}
				} else {//¸â¹ö°¡ ¾øÀ¸¸é...
					System.out.println("¸â¹ö°¡ ¾ø½À´Ï´Ù.");
					exit = true;
				}
				System.out.println("-----------------------------------------------------");
				System.out.println("ÇöÀç ÆäÀÌÁö: " + pageNo);
				System.out.println("-----------------------------------------------------");
				// ÆäÀÌÁö º¯°æ °ü·Ã
				if(pageNo <= 1) {//Ã¹ ÆäÀÌÁöÀÎ °æ¿ì ÀÔ·Â ÈùÆ®
					System.out.println("ÀÔ·Â(ex: ´ÙÀ½ÆäÀÌÁö: > | º¸±âÁ¾·á: q");
				} else if((memberList.size() < rowsPerPage) || 
						memberDao.selectAllByPage(pageNo+1, rowsPerPage).isEmpty()) {
					// ¸¶Áö¸· ÆäÀÌÁöÀÎ °æ¿ì ÀÔ·Â ÈùÆ®
					System.out.println("ÀÔ·Â(ex: ÀÌÀüÆäÀÌÁö: < | º¸±âÁ¾·á: q");
				} else {//Áß°£ ÆäÀÌÁöÀÎ °æ¿ì ÀÔ·Â ÈùÆ®
					System.out.println("ÀÔ·Â(ex: ÀÌÀüÆäÀÌÁö: <, ´ÙÀ½ÆäÀÌÁö: > | º¸±âÁ¾·á: q");
				}
				System.out.print(": ");
				String choice = sc.nextLine();
				if(choice.equals("<")) {//¾Õ ÆäÀÌÁö·Î °¡±â
					if(pageNo > 1) {//Ã³À½ ÆäÀÌÁö°¡ ¾Æ´Ò °æ¿ì ¾Õ ÆäÀÌÁö·Î °¡±â
						pageNo -= 1;
						continue;
					} else {//Ã¹ ÆäÀÌÁö¸é ¿¹¿ÜÃ³¸®
						System.out.println("¸ñ·ÏÀÇ Ã³À½ÀÔ´Ï´Ù.");
						continue;
					}
				} else if(choice.equals(">")) {// ´ÙÀ½ ÆäÀÌÁö °¡±â
					if(memberList.size() < rowsPerPage ||
							memberDao.selectAllByPage(pageNo+1, rowsPerPage).isEmpty()) {
						// ÇöÀç ÆäÀÌÁö°¡ ¸¶Áö¸· ÆäÀÌÁöÀÌ¸é ¿¹¿Ü Ã³¸®
						System.out.println("¸ñ·ÏÀÇ ¸¶Áö¸· ÀÔ´Ï´Ù.");
						continue;
					} else {// ÇöÀç ÆäÀÌÁö°¡ Áß°£ ÆäÀÌÁö¸é ´ÙÀ½ ÆäÀÌÁö·Î ÀÌµ¿
						pageNo += 1;
						continue;
					}
				} else if(choice.equals("q")) {//º¸±â Á¾·á
					exit = true;
				} else {
					try {
						if(choice.matches(".*[¤¡-¤¾¤¿-¤Ó°¡-ÆR]+.*")) {//ÇÑ±Û ÀÔ·Â ¿¹¿Ü Ã³¸®
							System.out.println("ÇÑ±ÛÀº ÀÔ·ÂÇÏÁö ¸¶¼¼¿ä.");
							continue;
						} else {
							int temp = pageNo;
							pageNo = Integer.parseInt(choice);//ÆäÀÌÁö ¼ıÀÚ¸¦ ÀÔ·ÂÇÏ¸é ÇØ´ç ÆäÀÌÁö·Î ÀÌµ¿
							if(memberDao.selectAllByPage(pageNo, rowsPerPage).isEmpty()) {
								// ÀÔ·ÂÇÑ ÆäÀÌÁö¿¡ ¸â¹ö°¡ ¾ø´Â °æ¿ì ¿¹¿Ü Ã³¸®
								pageNo = temp;
								System.out.println("ÇØ´ç ÆäÀÌÁö°¡ ¾ø½À´Ï´Ù.");
							}
							continue;
						}
					} catch(PatternSyntaxException e) {
						throw new NumberFormatException();
					} catch (NumberFormatException e) {
						System.out.println("´Ù½Ã ÀÔ·ÂÇÏ¼¼¿ä.");
						continue;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{conn.close();} catch(SQLException e) {}
		}
	}
	
	//¸â¹ö °­Åğ ¸Ş¼Òµå. °ü¸®ÀÚ¸¸ »ç¿ë °¡´É
	public void deleteMember() {
		try {
			conn = ConnectionManager.getConnection();
			MemberDao memberDao = new MemberDao(conn);
			conn.setAutoCommit(false);
			boolean exit = false;
			
			while(!exit) {
				System.out.print("»èÁ¦ÇÒ È¸¿ø ¾ÆÀÌµğ(»èÁ¦ Á¾·á = q): ");
				String mId = sc.nextLine();
				if(mId.equals("q")){//»èÁ¦ Á¾·á
					exit = true;
				} else {
					memberDao.delete(mId);//¸â¹ö »èÁ¦
				}
			}
		} catch (Exception e) {
			System.out.println("µğºñ ¿¡·¯ ¹ß»ı...");
			try{conn.rollback();} catch (SQLException e1) {}
		} finally {
			try{conn.close();} catch(SQLException e){}
		}
	}
	
	// ¸â¹ö µî±Ş ¼öÁ¤ ¸Ş¼Òµå. °ü¸®ÀÚ¸¸ »ç¿ë °¡´É.
	public void updateMemberGrade() {
		try {
			conn = ConnectionManager.getConnection();
			Member member = new Member();
			MemberDao memberDao = new MemberDao(conn);
			conn.setAutoCommit(false);
			while(true){
				System.out.print("µî±ŞÀ» º¯°æ ÇÒ ¸â¹ö ¾ÆÀÌµğ: ");
				member.setId(sc.nextLine());
				while(true){
					//µî±Ş º¯°æ ¼±ÅÃ. adminÀ¸·Î º¯°æÇÏ·Á¸é 1, ÀÏ¹İÈ¸¿øÀº 0
					System.out.print("º¯°æÇÒ µî±Ş(admin = 1, member = 0): ");
					try {
						int grade = Integer.parseInt(sc.nextLine());
						if(grade == 1) {//°ü¸®ÀÚ·Î
							member.setIsAdmin(grade);
							break;
						} else if(grade == 0) {//ÀÏ¹İÈ¸¿øÀ¸·Î
							member.setIsAdmin(grade);
							break;
						} else {
							throw new NumberFormatException();
						}
					} catch (NumberFormatException e) {
						System.out.println("¼ıÀÚ 0(member) ¶Ç´Â 1(admin) ¸¸ ÀÔ·ÂÇÏ¼¼¿ä");
						continue;
					}
				}
				int result = memberDao.updateMemberGrade(member);
				if(result < 1) {
					System.out.println("ÇØ´çÇÏ´Â ¾ÆÀÌµğ°¡ ¾ø½À´Ï´Ù.");
					continue;
				} else {
					break;
				}
			}
		} catch (Exception e) {
			try{conn.rollback();} catch (SQLException e1) {}
		} finally {
			try{conn.close();} catch (SQLException e) {}
		}
	}
	
	//¸â¹ö ºñ¹Ğ¹øÈ£ º¯°æ ¸Ş¼Òµå. »ç¿ëÇÏÁö´Â ¾ÊÀ½.
	public void updateMemberPassword(String mId) {
		try {
			conn = ConnectionManager.getConnection();
			MemberDao memberDao = new MemberDao(conn);
			conn.setAutoCommit(false);
			while(true) {
				System.out.print("º¯°æÇÒ ºñ¹Ğ¹øÈ£: ");
				String pw1 = sc.nextLine();
				System.out.println("ºñ¹Ğ¹øÈ£ È®ÀÎ: ");
				String pw2 = sc.nextLine();
				if(pw1.equals(pw2)) {// ºñ¹Ğ¹øÈ£ È®ÀÎ. º¯°æÇÒ ºñ¹Ğ¹øÈ£ ÀÔ·ÂÀÌ ¼­·Î °°ÀºÁö.
					Member member = new Member();
					member.setId(mId);
					member.setPw(pw1);
					
					int result = memberDao.updateMemberPassword(member);
					if(result < 1) {
						System.out.println("º¯°æ ½ÇÆĞ...´Ù½Ã ÇØº¸¼¼¿ä.");
						continue;
					} else {
						break;
					}
				} else {
					System.out.println("ºñ¹Ğ¹øÈ£¸¦ È®ÀÎÇÏ¼¼¿ä.");
					continue;
				}
			}
		} catch (Exception e) {
			try{conn.rollback();} catch (SQLException e1) {}
		} finally {
			try{conn.close();} catch (SQLException e) {}
		}
	}
}
