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
	
	//ȸ�� ���� �޼ҵ�
	public void join() {
		try {
			conn = ConnectionManager.getConnection();
			Member member = new Member();
			MemberDao memberDao = new MemberDao(conn);
			conn.setAutoCommit(false);
			int rows = 0;
			while(rows==0){
				// �Է� �����ϸ� while loop Ż��
				System.out.print("ID: ");
				String memberId = sc.nextLine();
				member.setId(memberId);
				if(memberDao.selectById(memberId) != null) {
					System.out.println("�̹� �ִ� ���̵� �Դϴ�.");
					continue;
				}
				System.out.print("�̸�: ");
				member.setName(sc.nextLine());
				System.out.print("PW: ");
				member.setPw(sc.nextLine());
				member.setIsAdmin(0);
				
				try {
					rows = memberDao.insert(member); // memberDao insert �޼ҵ�. �Ű������� member
					System.out.println(member.getId() + "���� �����ϼ̽��ϴ�.");
				} catch (SQLException e) {
					System.out.println("���� ����. �Է� ������ Ȯ���ϼ���.");
				}
			}
			conn.commit();
		} catch (Exception e1) {
			System.out.println("���� ����... �ٽ� �õ��� �ּ���.");
			try{conn.rollback();} catch (SQLException e) {}
		} finally {
			try {conn.close();} catch (Exception e) {}
		}
	}
	
	//�α��� �޼ҵ�
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
					// ����� �ִ��� ������ �˻�
					member = memberDao.selectById(id);
					if(member == null) {//selectById�� null�� �����ϸ� ���� ���̵�
						System.out.println("���̵� �����ϴ�. ���̵� Ȯ���ϼ���.");
						continue; 
					} else {//���̵� ������ �н����� �˻�
						if(member.getPw().equals(pw)) {//�Է¹��� ��й�ȣ�� ��� ��й�ȣ ���ؼ� �α���
							System.out.println("�α��� �ϼ̽��ϴ�.");
							login = true;
						} else {
							System.out.println("��й�ȣ�� Ȯ���ϼ���.");
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
		return member; //member ����. ���� �α��� �� ����� ������ �˱� ����
	}
	
	//��� ����Ʈ ����¡ �޼ҵ�. �� �������� 5��
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
				System.out.println("    ���̵�    |     �̸�     |     ������");
				System.out.println("-----------------------------------------------------");
				if(!memberList.isEmpty()){//�ش� �������� member�� �ִ��� ������ �˻�
					for(Member m : memberList) {// ������ ��� ����Ʈ ���
						System.out.print("     " + m.getId() + "          " + m.getName());
						if(m.getIsAdmin() == 1) {System.out.println("          yes");}
						else {System.out.println("           no");}
					}
				} else {//����� ������...
					System.out.println("����� �����ϴ�.");
					exit = true;
				}
				System.out.println("-----------------------------------------------------");
				System.out.println("���� ������: " + pageNo);
				System.out.println("-----------------------------------------------------");
				// ������ ���� ����
				if(pageNo <= 1) {//ù �������� ��� �Է� ��Ʈ
					System.out.println("�Է�(ex: ����������: > | ��������: q");
				} else if((memberList.size() < rowsPerPage) || 
						memberDao.selectAllByPage(pageNo+1, rowsPerPage).isEmpty()) {
					// ������ �������� ��� �Է� ��Ʈ
					System.out.println("�Է�(ex: ����������: < | ��������: q");
				} else {//�߰� �������� ��� �Է� ��Ʈ
					System.out.println("�Է�(ex: ����������: <, ����������: > | ��������: q");
				}
				System.out.print(": ");
				String choice = sc.nextLine();
				if(choice.equals("<")) {//�� �������� ����
					if(pageNo > 1) {//ó�� �������� �ƴ� ��� �� �������� ����
						pageNo -= 1;
						continue;
					} else {//ù �������� ����ó��
						System.out.println("����� ó���Դϴ�.");
						continue;
					}
				} else if(choice.equals(">")) {// ���� ������ ����
					if(memberList.size() < rowsPerPage ||
							memberDao.selectAllByPage(pageNo+1, rowsPerPage).isEmpty()) {
						// ���� �������� ������ �������̸� ���� ó��
						System.out.println("����� ������ �Դϴ�.");
						continue;
					} else {// ���� �������� �߰� �������� ���� �������� �̵�
						pageNo += 1;
						continue;
					}
				} else if(choice.equals("q")) {//���� ����
					exit = true;
				} else {
					try {
						if(choice.matches(".*[��-����-�Ӱ�-�R]+.*")) {//�ѱ� �Է� ���� ó��
							System.out.println("�ѱ��� �Է����� ������.");
							continue;
						} else {
							int temp = pageNo;
							pageNo = Integer.parseInt(choice);//������ ���ڸ� �Է��ϸ� �ش� �������� �̵�
							if(memberDao.selectAllByPage(pageNo, rowsPerPage).isEmpty()) {
								// �Է��� �������� ����� ���� ��� ���� ó��
								pageNo = temp;
								System.out.println("�ش� �������� �����ϴ�.");
							}
							continue;
						}
					} catch(PatternSyntaxException e) {
						throw new NumberFormatException();
					} catch (NumberFormatException e) {
						System.out.println("�ٽ� �Է��ϼ���.");
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
	
	//��� ���� �޼ҵ�. �����ڸ� ��� ����
	public void deleteMember() {
		try {
			conn = ConnectionManager.getConnection();
			MemberDao memberDao = new MemberDao(conn);
			conn.setAutoCommit(false);
			boolean exit = false;
			
			while(!exit) {
				System.out.print("������ ȸ�� ���̵�(���� ���� = q): ");
				String mId = sc.nextLine();
				if(mId.equals("q")){//���� ����
					exit = true;
				} else {
					memberDao.delete(mId);//��� ����
				}
			}
		} catch (Exception e) {
			System.out.println("��� ���� �߻�...");
			try{conn.rollback();} catch (SQLException e1) {}
		} finally {
			try{conn.close();} catch(SQLException e){}
		}
	}
	
	// ��� ��� ���� �޼ҵ�. �����ڸ� ��� ����.
	public void updateMemberGrade() {
		try {
			conn = ConnectionManager.getConnection();
			Member member = new Member();
			MemberDao memberDao = new MemberDao(conn);
			conn.setAutoCommit(false);
			while(true){
				System.out.print("����� ���� �� ��� ���̵�: ");
				member.setId(sc.nextLine());
				while(true){
					//��� ���� ����. admin���� �����Ϸ��� 1, �Ϲ�ȸ���� 0
					System.out.print("������ ���(admin = 1, member = 0): ");
					try {
						int grade = Integer.parseInt(sc.nextLine());
						if(grade == 1) {//�����ڷ�
							member.setIsAdmin(grade);
							break;
						} else if(grade == 0) {//�Ϲ�ȸ������
							member.setIsAdmin(grade);
							break;
						} else {
							throw new NumberFormatException();
						}
					} catch (NumberFormatException e) {
						System.out.println("���� 0(member) �Ǵ� 1(admin) �� �Է��ϼ���");
						continue;
					}
				}
				int result = memberDao.updateMemberGrade(member);
				if(result < 1) {
					System.out.println("�ش��ϴ� ���̵� �����ϴ�.");
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
	
	//��� ��й�ȣ ���� �޼ҵ�. ��������� ����.
	public void updateMemberPassword(String mId) {
		try {
			conn = ConnectionManager.getConnection();
			MemberDao memberDao = new MemberDao(conn);
			conn.setAutoCommit(false);
			while(true) {
				System.out.print("������ ��й�ȣ: ");
				String pw1 = sc.nextLine();
				System.out.println("��й�ȣ Ȯ��: ");
				String pw2 = sc.nextLine();
				if(pw1.equals(pw2)) {// ��й�ȣ Ȯ��. ������ ��й�ȣ �Է��� ���� ������.
					Member member = new Member();
					member.setId(mId);
					member.setPw(pw1);
					
					int result = memberDao.updateMemberPassword(member);
					if(result < 1) {
						System.out.println("���� ����...�ٽ� �غ�����.");
						continue;
					} else {
						break;
					}
				} else {
					System.out.println("��й�ȣ�� Ȯ���ϼ���.");
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
