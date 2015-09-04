package test.shoppingmall.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

import test.shoppingmall.ConnectionManager;
import test.shoppingmall.dao.MemberDao;
import test.shoppingmall.vo.Member;

public class MemberService {
	private Connection conn;
	private Scanner sc;
	
	public MemberService(Scanner sc) {
		this.sc = sc;
	}
	
	//회원 가입 메소드
	public void join() {
		try {
			conn = ConnectionManager.getConnection();
			Member member = new Member();
			MemberDao memberDao = new MemberDao(conn);
			conn.setAutoCommit(false);
			int rows = 0;
			while(rows==0){
				// 입력 성공하면 while loop 탈출
				System.out.print("ID: ");
				String memberId = sc.nextLine();
				member.setId(memberId);
				if(memberDao.selectById(memberId) != null) {
					System.out.println("이미 있는 아이디 입니다.");
					continue;
				}
				System.out.print("이름: ");
				member.setName(sc.nextLine());
				System.out.print("PW: ");
				member.setPw(sc.nextLine());
				member.setIsAdmin(0);
				
				try {
					rows = memberDao.insert(member); // memberDao insert 메소드. 매개변수로 member
					System.out.println(member.getId() + "님이 가입하셨습니다.");
				} catch (SQLException e) {
					System.out.println("가입 실패. 입력 내용을 확인하세요.");
				}
			}
			conn.commit();
		} catch (Exception e1) {
			System.out.println("가입 실패... 다시 시도해 주세요.");
			try{conn.rollback();} catch (SQLException e) {}
		} finally {
			try {conn.close();} catch (Exception e) {}
		}
	}
	
	//로그인 메소드
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
					// 멤버가 있는지 없는지 검사
					member = memberDao.selectById(id);
					if(member == null) {//selectById가 null을 리턴하면 없는 아이디
						System.out.println("아이디가 없습니다. 아이디를 확인하세요.");
						continue; 
					} else {//아이디가 있으면 패스워드 검사
						if(member.getPw().equals(pw)) {//입력받은 비밀번호랑 디비 비밀번호 비교해서 로그인
							System.out.println("로그인 하셨습니다.");
							login = true;
						} else {
							System.out.println("비밀번호를 확인하세요.");
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
		return member; //member 리턴. 현재 로그인 한 멤버가 누군지 알기 위해
	}
	
	//멤버 리스트 페이징 메소드. 한 페이지에 5명씩
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
				System.out.println("    아이디    |     이름     |     관리자");
				System.out.println("-----------------------------------------------------");
				if(!memberList.isEmpty()){//해당 페이지에 member가 있는지 없는지 검사
					for(Member m : memberList) {// 있으면 멤버 리스트 출력
						System.out.print("     " + m.getId() + "          " + m.getName());
						if(m.getIsAdmin() == 1) {System.out.println("          yes");}
						else {System.out.println("           no");}
					}
				} else {//멤버가 없으면...
					System.out.println("멤버가 없습니다.");
					exit = true;
				}
				System.out.println("-----------------------------------------------------");
				System.out.println("현재 페이지: " + pageNo);
				System.out.println("-----------------------------------------------------");
				// 페이지 변경 관련
				if(pageNo <= 1) {//첫 페이지인 경우 입력 힌트
					System.out.println("입력(ex: 다음페이지: > | 보기종료: q");
				} else if((memberList.size() < rowsPerPage) || 
						memberDao.selectAllByPage(pageNo+1, rowsPerPage).isEmpty()) {
					// 마지막 페이지인 경우 입력 힌트
					System.out.println("입력(ex: 이전페이지: < | 보기종료: q");
				} else {//중간 페이지인 경우 입력 힌트
					System.out.println("입력(ex: 이전페이지: <, 다음페이지: > | 보기종료: q");
				}
				System.out.print(": ");
				String choice = sc.nextLine();
				if(choice.equals("<")) {//앞 페이지로 가기
					if(pageNo > 1) {//처음 페이지가 아닐 경우 앞 페이지로 가기
						pageNo -= 1;
						continue;
					} else {//첫 페이지면 예외처리
						System.out.println("목록의 처음입니다.");
						continue;
					}
				} else if(choice.equals(">")) {// 다음 페이지 가기
					if(memberList.size() < rowsPerPage ||
							memberDao.selectAllByPage(pageNo+1, rowsPerPage).isEmpty()) {
						// 현재 페이지가 마지막 페이지이면 예외 처리
						System.out.println("목록의 마지막 입니다.");
						continue;
					} else {// 현재 페이지가 중간 페이지면 다음 페이지로 이동
						pageNo += 1;
						continue;
					}
				} else if(choice.equals("q")) {//보기 종료
					exit = true;
				} else {
					try {
						if(choice.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {//한글 입력 예외 처리
							System.out.println("한글은 입력하지 마세요.");
							continue;
						} else {
							int temp = pageNo;
							pageNo = Integer.parseInt(choice);//페이지 숫자를 입력하면 해당 페이지로 이동
							if(memberDao.selectAllByPage(pageNo, rowsPerPage).isEmpty()) {
								// 입력한 페이지에 멤버가 없는 경우 예외 처리
								pageNo = temp;
								System.out.println("해당 페이지가 없습니다.");
							}
							continue;
						}
					} catch(PatternSyntaxException e) {
						throw new NumberFormatException();
					} catch (NumberFormatException e) {
						System.out.println("다시 입력하세요.");
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
	
	//멤버 강퇴 메소드. 관리자만 사용 가능
	public void deleteMember() {
		try {
			conn = ConnectionManager.getConnection();
			MemberDao memberDao = new MemberDao(conn);
			conn.setAutoCommit(false);
			boolean exit = false;
			
			while(!exit) {
				System.out.print("삭제할 회원 아이디(삭제 종료 = q): ");
				String mId = sc.nextLine();
				if(mId.equals("q")){//삭제 종료
					exit = true;
				} else {
					memberDao.delete(mId);//멤버 삭제
				}
			}
		} catch (Exception e) {
			System.out.println("디비 에러 발생...");
			try{conn.rollback();} catch (SQLException e1) {}
		} finally {
			try{conn.close();} catch(SQLException e){}
		}
	}
	
	// 멤버 등급 수정 메소드. 관리자만 사용 가능.
	public void updateMemberGrade() {
		try {
			conn = ConnectionManager.getConnection();
			Member member = new Member();
			MemberDao memberDao = new MemberDao(conn);
			conn.setAutoCommit(false);
			while(true){
				System.out.print("등급을 변경 할 멤버 아이디: ");
				member.setId(sc.nextLine());
				while(true){
					//등급 변경 선택. admin으로 변경하려면 1, 일반회원은 0
					System.out.print("변경할 등급(admin = 1, member = 0): ");
					try {
						int grade = Integer.parseInt(sc.nextLine());
						if(grade == 1) {//관리자로
							member.setIsAdmin(grade);
							break;
						} else if(grade == 0) {//일반회원으로
							member.setIsAdmin(grade);
							break;
						} else {
							throw new NumberFormatException();
						}
					} catch (NumberFormatException e) {
						System.out.println("숫자 0(member) 또는 1(admin) 만 입력하세요");
						continue;
					}
				}
				int result = memberDao.updateMemberGrade(member);
				if(result < 1) {
					System.out.println("해당하는 아이디가 없습니다.");
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
	
	//멤버 비밀번호 변경 메소드. 사용하지는 않음.
	public void updateMemberPassword(String mId) {
		try {
			conn = ConnectionManager.getConnection();
			MemberDao memberDao = new MemberDao(conn);
			conn.setAutoCommit(false);
			while(true) {
				System.out.print("변경할 비밀번호: ");
				String pw1 = sc.nextLine();
				System.out.println("비밀번호 확인: ");
				String pw2 = sc.nextLine();
				if(pw1.equals(pw2)) {// 비밀번호 확인. 변경할 비밀번호 입력이 서로 같은지.
					Member member = new Member();
					member.setId(mId);
					member.setPw(pw1);
					
					int result = memberDao.updateMemberPassword(member);
					if(result < 1) {
						System.out.println("변경 실패...다시 해보세요.");
						continue;
					} else {
						break;
					}
				} else {
					System.out.println("비밀번호를 확인하세요.");
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
