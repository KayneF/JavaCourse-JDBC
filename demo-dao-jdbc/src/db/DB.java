package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	
	private static Connection conn = null;
	
	// JDBC Driver connector
	public static Connection getConnection() {
		System.out.println("Connecting drivers...");
		if (conn == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				conn = DriverManager.getConnection(url, props);
				System.out.println("JDBC drivers connected successfully.\n");
			}
			catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
		return conn;
	}

	// Load connection properties
	private static Properties loadProperties() {
		System.out.println("Loading properties...");
		try (FileInputStream fs = new FileInputStream("db.properties")) {
			Properties props = new Properties();
			props.load(fs);
			System.out.println("Properties loaded successfully.");
			return props;
		}
		catch (IOException e) {
			throw new DBException(e.getMessage());
		}
	}
	
	// Close connection
		public static void closeConnection() {
			System.out.println("\nClosing connection...");
			if (conn != null) {
				try {
					conn.close();
					System.out.println("Connection closed.");
				} catch (SQLException e) {
					throw new DBException(e.getMessage());
				}
			}
		}
	
	// Close Statement
	public static void closeStatement(Statement st) {
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
	}
	
	// Close ResultSet
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DBException(e.getMessage());
			}
		}
	}
}
