package net.strong.util;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * 回收数据对象
 * <p>Title: Helper.java</p>
 * <p>Description: Development Lib For CRM</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company:  Strong Software International CO,.LTD   </p>
 * @Date 2007-3-17
 * @author Strong Yuan
 * @version 1.0
 */

public final class Helper {

	private static Logger log = Logger.getLogger("Helper");

	public final static void cleanup(Statement stmt){
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		}catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	public final static void cleanup(PreparedStatement pstmt){
		try {
			if (pstmt != null) {
				pstmt.close();
				pstmt= null;
			}
		}catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	public final static void cleanup( Connection con){
		try {
			if (con != null && !con.isClosed()) {
				con.setAutoCommit(true);
				con.close();
				if(!con.isClosed()){
					log.error("数据库还是没有关闭");
				}
				con = null;
			}
		}catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	public final static void cleanup(ResultSet rs){
		try {
			if (rs != null) {
				rs.close();
				rs=null;
			}
		}
		catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

	/**
	 * 回收 Statement 和 ResultSet
	 * @param rs ResultSet
	 * @param conn Connection
	 */
	public final static void cleanup(Statement stmt, ResultSet rs) {
		cleanup(rs);		
		cleanup(stmt);
	}	
	/**
	 * 回收 Statement 和 Connection
	 * @param stmt Statement
	 * @param conn Connection
	 */
	public final static void cleanup(Statement stmt, Connection con) {
		cleanup(stmt);
		cleanup(con);
	}

	/**
	 * 回收 PreparedStatement 和 Connection
	 * @param pstmt  PreparedStatement
	 * @param conn Connection
	 */
	public final static void cleanup(PreparedStatement pstmt, Connection con) {
		cleanup(pstmt);
		cleanup(con);
	}

	/**
	 * 收回 Statement,Connection 和 ResultSet
	 * @param stmt Statement
	 * @param conn Connection
	 * @param rs ResultSet
	 */

	public final static void cleanup(Statement stmt, Connection con, ResultSet rs) {
		cleanup(rs);
		cleanup(stmt,con);
	}

	/**
	 * 收回 PreparedStatement,Connection 和 ResultSet
	 * @param pstmt PreparedStatement
	 * @param conn Connection
	 * @param rs ResultSet
	 */

	public final static void cleanup(PreparedStatement pstmt, Connection con, ResultSet rs) {
		cleanup(rs);
		cleanup(pstmt,con);		
	}

}