package org.abratuhi.bahnde.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DbUtil {
	
	private final static Logger LOG = Logger.getLogger(DbUtil.class);
	
	public static String DB_NAME = "bahndb";

	public static Connection getConnection() {
		Connection connection = null;
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";

		try {
			Class.forName(driver);
			String connectionURL = "jdbc:derby:" + DB_NAME + ";create=true";
			try {
				connection = DriverManager.getConnection(connectionURL);
			} catch (Throwable e) {
				e.printStackTrace();
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return connection;
	}

	public static void shutdown() {

		boolean gotSQLExc = false;
		try {
			DriverManager.getConnection("jdbc:derby:" + DB_NAME + ";shutdown=true");
		} catch (SQLException se) {
			if (se.getSQLState().equals("XJ015") || se.getSQLState().equals("08006")) {
				gotSQLExc = true;
			}
		}
		if (!gotSQLExc) {
			System.out.println("Database did not shut down normally");
		} else {
			System.out.println("Database shut down normally");
		}

	}

}
