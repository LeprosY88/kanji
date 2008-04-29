package org.abratuhi.uradar.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.abratuhi.uradar.util.ResponseUtil;



public class URadarModule {
	
	String name;	// name of the module, simultaneously name of MySQL table in DB
	String description;	// short description of the module
	URadarModuleModel model;	// model of the module
	Properties props;	// module specific parameters
	Connection connection;	// connection to MySQL DB
	
	/**
	 * Constructor
	 * Notice: doesn't create MySQL tables by init - createTables() must be called after
	 * @param name
	 * @param description
	 * @param props
	 * @param connection
	 */
	public URadarModule(String name, String description, Properties props, Connection connection){
		// perform check of parameters
		if(name==null || name==""){
			System.out.println("URadarModule Constructor:\tbad value of parameter name");
		}
		if(props==null){
			System.out.println("URadarModule Constructor:\tbad value of parameter props");
		}
		if(connection==null){
			System.out.println("URadarModule Constructor:\tbad value of parameter connection");
		}
		// init attributes/values
		this.name = name;
		this.description = description;
		this.props = props;
		this.connection = connection;
	}
	
	/**
	 * Find user in DB using a module-specific id (e.g. Facebook ID, SecondLife ID)
	 * 		a.k.a. findUser(String moduleID)
	 * @param moduleID	-	module-specific ID
	 * @return			-	user's U(niversal)Radar ID, if found in DB
	 * 						null, otherwise
	 */
	public String resolveModuleID(String moduleID){
		// init return/response string
		String out = new String();
		// try to connect to DB for search
		try{
		// create statement
		Statement stmt = connection.createStatement();
		// generate query string
		String sql_check_moduleid = "select uradarid from "+this.name+" where "+this.name+"id='"+moduleID+"';";
		// query DB, check whether moduleID isRegistered
		ResultSet rs_check = stmt.executeQuery(sql_check_moduleid);
		// check size of result -> if resultset is empty no resolution is possible
		if(rs_check.first()){
			// return
			out = rs_check.getString("uradarid");
		}
		else{
			// return
			out = null;
		}
		// close statement, garbage collector is not to be relied upon
		stmt.close();
		// return
		return out;
		
		} catch (SQLException e){
			e.printStackTrace();
		}
		// return null if connection to DB couldn't be established
		return null;
	}
	
	/**
	 * Add/Update user information in MySQL table of module using module-specific information from request query
	 * @param myURadarID	-	user's U(niversal)Radar ID
	 * @param reqprops		-	module-specific information sent with the query
	 */
	public void addupdateUser(String myURadarID, Properties reqprops){
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String modulenameid = reqprops.getProperty(this.name+"id"); // second obligatory field
			String sql_addupdate_user = "insert into "+this.name+"(uradarid, "+(this.name+"id")+") values ("+myURadarID+","+modulenameid+");";
			// execute query
			stmt.executeUpdate(sql_addupdate_user);
			// close statement, garbage collector is not to be relied upon
			stmt.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Add/Update user's friend information in MySQL table of module using module-specific information from request query
	 * @param myURadarID	-	user's U(niversal)Radar ID
	 * @param friendURadarID	-	friend's U(niversal)Radar ID
	 * @param reqprops		-	module-specific information sent with the query
	 */
	public void addupdateFriend(String myURadarID, String friendURadarID, Properties reqprops){
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String visibility = reqprops.getProperty("visibility"); // third obligatory field
			String sql_addupdate_friend = "insert into "+this.name+"_friends"+"(uradarid, uradarid_friend, visibility)" +
										" values ("+myURadarID+","+friendURadarID+","+visibility+");";
			// execute query
			stmt.executeUpdate(sql_addupdate_friend);
			// close statement, garbage collector is not to be relied upon
			stmt.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Get module-specific information about user
	 * @param myURadarID	-	user's U(niversal)Radar ID
	 * @return				-	XML-string representing module-specific information about user
	 */
	public String getOwnInfo(String myURadarID){
		// init return/response string
		String out = new String();
		// try to connect to DB for search
		try{
		// create statement
		Statement stmt = connection.createStatement();
		// generate query string
		String sql_get_own_info = "select * from "+this.name+" where uradarid='"+myURadarID+"';";
		// query DB for own info
		ResultSet rs_get_own_info = stmt.executeQuery(sql_get_own_info);
		// check size of result -> if resultset is empty no resolution is possible
		if(rs_get_own_info.first()){
			// generate return/response string
			out = ResponseUtil.convertResultSet2XMLString(rs_get_own_info);
		}
		else{
			// return
			out = null;
		}
		// close statement, garbage collector is not to be relied upon
		stmt.close();
		
		//return
		return out;
		
		} catch (SQLException e){
			e.printStackTrace();
		}
		// return null if connection to DB couldn't be established
		return null;
	}
	
	/**
	 * Get module-specific information about user's friends
	 * @param myURadarID	-	user's U(niversal)Radar ID
	 * @return				-	XML-string representing module-specific information about user's friends
	 */
	public String getFriendsInfo(String myURadarID){
		// init return/response string
		String out = new String();
		// try to connect to DB for search
		try{
		// create statement
		Statement stmt = connection.createStatement();
		// generate query string
		String sql_get_friends_info = "select * from "+(this.name+"_friends")+" where uradarid='"+myURadarID+"';";
		// query DB for own info
		ResultSet rs_get_friends_info = stmt.executeQuery(sql_get_friends_info);
		// check size of result -> if resultset is empty no resolution is possible
		if(rs_get_friends_info.first()){
			// generate return/response string
			out = ResponseUtil.convertResultSet2XMLString(rs_get_friends_info);
		}
		else{
			// return
			out = null;
		}
		// close statement, garbage collector is not to be relied upon
		stmt.close();
		
		//return
		return out;
		
		} catch (SQLException e){
			e.printStackTrace();
		}
		// return null if connection to DB couldn't be established
		return null;
	}
	
	/**
	 * Remove user from users of this module
	 * @param myURadarID	-	user's U(niversal)Radar ID
	 * @param reqprops		-	module-specific information sent with the query
	 */
	public void removeUser(String myURadarID, Properties reqprops){
		// try to connect to DB for search
		try{
		// create statement
		Statement stmt = connection.createStatement();
		// generate query strings
		String sql_delete_user = "delete  from "+this.name+" where uradarid='"+myURadarID+"';";	// clean <modulname> table
		String sql_delete_friends = "delete  from "+this.name+"_friends"+" where uradarid='"+myURadarID+"';";	// clean <modulname>_friends table
		// query DB for own info
		stmt.executeUpdate(sql_delete_friends);
		stmt.executeUpdate(sql_delete_user);
		// close statement, garbage collector is not to be relied upon
		stmt.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Remove user's friend from friends relationship table
	 * @param myURadarID	-	user's U(niversal)Radar ID
	 * @param friendURadarID	-	friend's U(niversal)Radar ID
	 * @param reqprop		-	module-specific information sent with the query
	 */
	public void removeFriend(String myURadarID, String friendURadarID, Properties reqprop){
		// try to connect to DB for search
		try{
		// create statement
		Statement stmt = connection.createStatement();
		// generate query string
		String sql_delete_friend = "delete  from "+this.name+"_friends"+" where uradarid='"+myURadarID+"' and uradarid_friend='"+friendURadarID+"';";
		// query DB for own info
		stmt.executeUpdate(sql_delete_friend);
		// close statement, garbage collector is not to be relied upon
		stmt.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Set model of the module - generally, names and types of fields in MySQL table
	 * @param model		-	table model
	 */
	public void setModel(URadarModuleModel model){
		this.model = model;
	}
	
	/**
	 * Get model of the module
	 * @return	-	table model
	 */
	public URadarModuleModel getModel(){
		return model;
	}
	
	/**
	 * Create MySQL table in DB using the module's model
	 * @return
	 */
	public boolean createTables(){
		return true;
	}
	
	/**
	 * Checks whether all tables from model are present in MySQL DB
	 * @return	true, if all tables are present
	 * 			false, otherwise
	 */
	public boolean checkTables(){
		return true;
	}
	
	/**
	 * Delete MySQL table in DB
	 * @return
	 */
	public boolean deleteTables(){
		return true;
	}

}
