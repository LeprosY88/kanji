package db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class InitSLRadarDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		FileInputStream fis;
	    Properties props = new Properties();
		try {
			fis = new FileInputStream("slradar.properties");
			props.load(fis);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Connection connection = null;
		//String db = "jdbc:mysql://localhost/slradar";
		//String user = "lindenuser";
		//String passwd = "linden";
		String db = "jdbc:mysql://"+props.getProperty("mysql.host")+"/"+props.getProperty("mysql.db");
		String user = props.getProperty("mysql.user");
		String passwd = props.getProperty("mysql.passwd");
		
		try {
			connection = DriverManager.getConnection(db, user, passwd);
			
			String sql_rmtable_base = "drop table base;";
			String sql_rmtable_sl = "drop table sl;";
			String sql_rmtable_fb = "drop table fb;";
			String sql_rmtable_friends = "drop table friends;";
			
			String sql_crtable_base = "create table base(" +
										"id integer primary key auto_increment," +
										"fbid varchar(100)," +
										"slid varchar(100));";
			String sql_crtable_sl = "create table sl(" +
										"slid varchar(100) references base on delete cascade," +
										"name varchar(100)," +
										"region varchar(100)," +
										"position varchar(50)," +
										"velocity varchar(30)," +
										"rotation varchar(30));";
			String sql_crtable_fb = "create table fb(" +
										"fbid varchar(100) references base on delete cascade," +
										"name varchar(100));";
			String sql_crtable_friends = "create table friends(" +
										"id1 integer references base(id) on delete cascade," +
										"id2 integer references base(id) on delete cascade);";
			
			boolean drop_tables_first = false;
			if(drop_tables_first){
				Statement stmt;
				
				stmt = connection.createStatement();
				stmt.executeUpdate(sql_rmtable_friends);
				
				stmt = connection.createStatement();
				stmt.executeUpdate(sql_rmtable_sl);
				
				stmt = connection.createStatement();
				stmt.executeUpdate(sql_rmtable_fb);
				
				stmt = connection.createStatement();
				stmt.executeUpdate(sql_rmtable_base);
			}
			
			Statement stm = connection.createStatement();
			DatabaseMetaData md = connection.getMetaData();
			ResultSet rs_base = md.getTables(null, null, "base", null);
			ResultSet rs_sl = md.getTables(null, null, "sl", null);
			ResultSet rs_fb = md.getTables(null, null, "fb", null);
			ResultSet rs_friends = md.getTables(null, null, "friends", null);
			if(!rs_base.next()) stm.executeUpdate(sql_crtable_base);
			if(!rs_sl.next()) stm.executeUpdate(sql_crtable_sl);
			if(!rs_fb.next()) stm.executeUpdate(sql_crtable_fb);
			if(!rs_friends.next()) stm.executeUpdate(sql_crtable_friends);
			
			
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}

	}

}
