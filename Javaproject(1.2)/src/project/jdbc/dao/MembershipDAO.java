package project.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.jdbc.vo.BookVO;
import project.jdbc.vo.MembershipVO;
import project.jdbc.vo.RentalInfoVO;

public class MembershipDAO {

	Connection con;
	
	public MembershipDAO() {
	}

	public MembershipDAO(Connection con) {
		super();
		this.con = con;
	}
	
	public MembershipVO selectOne(String loginId) {
		MembershipVO membership = null;
		String sql = "SELECT * FROM membership WHERE mid=?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, loginId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				membership = new MembershipVO(rs.getString("mid"),rs.getString("mpw"),rs.getString("mname"),rs.getString("mbirth"),rs.getString("mphonenumber"),rs.getString("memail"));
			}
			rs.close();
			pstmt.close();
			
		} catch (SQLException e) {
		} 
		return membership;
		
	}
	
	//로그인 - MyBatis 적용
	private SqlSessionFactory factory;
	public MembershipDAO(SqlSessionFactory factory) {
		this.factory = factory;
	}
	
	public MembershipVO select(String loginId, String loginpw) {
		MembershipVO membership = null;
		SqlSession session = factory.openSession();
		membership = session.selectOne("project.myMember.select",loginId);
		return membership;
	}
	
//	public HashMap<String, String> select(String loginId, String loginpw){
//		HashMap<String, String> membership = null;
//		SqlSession session = factory.openSession();
//		membership = session.selectOne("project.myMember.select", loginId); // 원래 가지고 있는 method
//		for(String key : membership.keySet()) {
//			System.out.println(key + ", "+membership.get(key));
//		}
//		return membership;
//	}
	
//	public MembershipVO select(String loginId, String loginpw) {
//		
//		MembershipVO membership=null;
//		
//		String sql = "SELECT mid, mpw FROM membership WHERE mid = ?";
//		try {
//			PreparedStatement pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, loginId);
//			ResultSet rs = pstmt.executeQuery();
//			if(rs.next()) {
//				membership = new MembershipVO(rs.getString("mid"),rs.getString("mpw"));
//			}
//			rs.close();
//			pstmt.close();
//			
//		} catch (SQLException e) {
//			//e.printStackTrace();
//		} return membership;
//		
//	}
	
	public RentalInfoVO selectmanager(String managerId) {
		
		RentalInfoVO manager = null;
		String sql = "SELECT managerid, managerpw FROM manager WHERE managerid = ?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, managerId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				manager = new RentalInfoVO(rs.getString("managerid"),rs.getString("managerpw"));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return manager;
	}
	
	public void insert(String id, String pw, String name, String birth, String phone, String email, Date regdate) {

	    String sql = "INSERT INTO membership VALUES (?,?,?,?,?,?,?,3000)";
	    
	    try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.setString(4, birth);
			pstmt.setString(5, phone);
			pstmt.setString(6, email);
			pstmt.setDate(7, (java.sql.Date) regdate);
			
			int Count = pstmt.executeUpdate();
			
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 	    
	}

	public ObservableList<MembershipVO> selectAll(String text) {
		ObservableList<MembershipVO> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * ");
		sql.append("FROM membership ");
		sql.append("WHERE mid like ? ");
		
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setString(1, "%"+text+"%");
			
			ResultSet rs = pstmt.executeQuery();
			list = FXCollections.observableArrayList();
			while (rs.next()) {
				MembershipVO book = new MembershipVO(rs.getString("mid"),rs.getString("mname"),rs.getString("mbirth"),rs.getString("mphonenumber"),rs.getString("memail"),rs.getDate("mregdate"));
	            list.add(book);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
		
	}

	public int delete(String loginId) {
	
		String sql = "DELETE FROM membership WHERE mid = ?";
		int count = 0;
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, loginId);
			
			count = pstmt.executeUpdate();
		
			pstmt.close();
			
		} catch (Exception e1) {
		
		}
		return count;
	}

	public int updateM(String id, String pw, String name, String birth, String phonenumber, String email, String loginId) {

		String sql = "UPDATE membership SET mid=?, mpw=?, mname=?, mbirth=?, mphonenumber=?, memail=? WHERE mid = ?";
		int count=0;
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setString(3, name);
			pstmt.setString(4, birth);
			pstmt.setString(5, phonenumber);
			pstmt.setString(6, email);
			pstmt.setString(7, loginId);
		
			int Count = pstmt.executeUpdate();
			
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int updatePP(String loginId) {
		
		String sql = "UPDATE membership SET mpoint = mpoint + 700 WHERE mid = ?";
		int count=0;
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, loginId);
		    
			int Count = pstmt.executeUpdate();
			
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int updateMP(String loginId) {
		String sql = "UPDATE membership SET mpoint = mpoint - 700 WHERE mid = ?";
		int count=0;
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, loginId);
		   
			int Count = pstmt.executeUpdate();
			
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int selectP(String loginId) {
		int point = 0;
		String sql = "SELECT mpoint FROM membership WHERE mid = ?";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, loginId);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				point = rs.getInt("mpoint");
			}
			rs.close();
			pstmt.close();
			
		} catch (SQLException e) {
			//e.printStackTrace();
		} return point;
	}

	



	
	
	
}
