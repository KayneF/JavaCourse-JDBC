package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DBException;

public class Program {

	public static void main(String[] args) {

		Connection conn = null;
		@SuppressWarnings("unused")
		PreparedStatement pst = null;
		Statement st = null;
		
		try {
			conn = DB.getConnection();
			conn.setAutoCommit(false);
			
			st = conn.createStatement();
			
			int rows1 = st.executeUpdate(
					"UPDATE seller SET BaseSalary = 2090 "
							+ "WHERE DepartmentId = 1 "
					);
//			int x = 1;
//			if (x < 2) {
//				throw new SQLException("Fake error!");
//			}
			int rows2 = st.executeUpdate(
					"UPDATE seller SET BaseSalary = 3090 "
					+ "WHERE DepartmentId = 2 "
					);
			
			conn.commit();
			System.out.println("rows1: " + rows1);
			System.out.println("rows2: " + rows2);
		}
		catch (SQLException e) {
			try {
				conn.rollback();
				throw new DBException(
						"Transaction failed, rolling back changes...");
			} catch (SQLException e1) {
				throw new DBException(
						"An unexpected error has ocurred. Caused by: "
						+ e1.getMessage());
			}
		}
		finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
}
