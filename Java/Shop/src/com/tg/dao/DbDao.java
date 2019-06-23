package com.tg.dao;
/**
 * 
 * 
 */
import java.sql.*;

public class DbDao{
	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/shop?useUnicode=true&characterEncoding=utf-8";
	private static String user = "root";
	private static String pass = "123456";
	private Connection con;
	protected Statement sta;
	protected ResultSet rs;
	protected PreparedStatement ps;

	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void openCon() throws SQLException {
		con = DriverManager.getConnection(url, user, pass);
	}
	public void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (sta != null) {
				sta.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet query(String sql) throws SQLException {
		openCon();
		sta = con.createStatement();
		rs = sta.executeQuery(sql);
		return rs;
	}

	public int update(String sql) {
		int cnt = -1;
		try {
			openCon();
			sta = con.createStatement();
			cnt = sta.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return cnt;
	}

	public boolean update(String[] sqls) {
		int cnt = -1;
		try {
			openCon();
			con.setAutoCommit(false);
			sta = con.createStatement();
			for (int i = 0; i < sqls.length; i++) {
				sta.executeUpdate(sqls[i]);
			}
			con.commit();
			return true;
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			close();
		}
		return false;
	}

	/**
	 * 
	 * @param sql
	 * @param param
	 * @return
	 */
	public int update(String sql, Object[] param) {
		int cnt = -1;
		try {
			openCon();
			ps = con.prepareStatement(sql);
			if (param != null && param.length > 0) {
				for (int i = 0; i < param.length; i++) {
					ps.setObject(i + 1, param[i]);
				}
			}
			cnt = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return cnt;
	}
	
}
