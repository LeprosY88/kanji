package org.abratuhi.uradar.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.abratuhi.uradar.util.ResponseUtil;



public class URadarUpdateModule extends URadarModule{

	/**
	 * 
	 * @param name
	 * @param description
	 * @param props
	 * @param connection
	 */
	public URadarUpdateModule(String name, String description,
			Properties props, Connection connection) {
		// super
		super(name, description, props, connection);
	}

	/**
	 * Find user in DB using a module-specific id (e.g. Facebook ID, SecondLife ID)
	 * 		a.k.a. findUser(String moduleID)
	 * @param moduleID	-	module-specific ID
	 * @return			-	user's U(niversal)Radar ID, if found in DB
	 * 						null, otherwise
	 */
	public String resolveModuleID(Properties reqprops){
		// init return/response string
		String out = new String();
		// get request details
		String moduleID = reqprops.getProperty("moduleid");
		// try to connect to DB for search
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String sql_check_moduleid = "select uradarid from "+this.name+" where moduleid='"+moduleID+"';";
			// query DB, check whether moduleID isRegistered
			ResultSet rs_check = stmt.executeQuery(sql_check_moduleid);
			// check size of result -> if resultset is empty no resolution is possible
			if(rs_check.first()){
				// return
				out = rs_check.getString("uradarid");
			}
			else{
				// return
				out = INVALID_ID;
			}
			// close statement, garbage collector is not to be relied upon
			stmt.close();
			// return
			return out;

		} catch (SQLException e){
			e.printStackTrace();
		}
		// return null if connection to DB couldn't be established
		return out;
	}
	
	public String resolveModuleID(String moduleid){
		// init return/response string
		String out = new String();
		// get request details
		String moduleID = moduleid;
		// try to connect to DB for search
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String sql_check_moduleid = "select uradarid from "+this.name+" where moduleid='"+moduleID+"';";
			// query DB, check whether moduleID isRegistered
			ResultSet rs_check = stmt.executeQuery(sql_check_moduleid);
			// check size of result -> if resultset is empty no resolution is possible
			if(rs_check.first()){
				// return
				out = rs_check.getString("uradarid");
			}
			else{
				// return
				out = INVALID_ID;
			}
			// close statement, garbage collector is not to be relied upon
			stmt.close();
			// return
			return out;

		} catch (SQLException e){
			e.printStackTrace();
		}
		// return null if connection to DB couldn't be established
		return out;
	}

	/**
	 * Add/Update user information in MySQL table of module using module-specific information from request query
	 * @param myURadarID	-	user's U(niversal)Radar ID
	 * @param reqprops		-	module-specific information sent with the query
	 */
	public String addupdateUser(Properties reqprops){
		//
		String myURadarID = reqprops.getProperty("uradarid");
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String modulenameid = reqprops.getProperty("moduleid"); // second obligatory field
			String sql_addupdate_user = "insert into "+this.name+"(uradarid, moduleid) values ('"+myURadarID+"','"+modulenameid+"');";
			System.out.println("URadarUpdateModule addupdateUser():\t"+sql_addupdate_user);
			// execute query
			stmt.executeUpdate(sql_addupdate_user);
			// close statement, garbage collector is not to be relied upon
			stmt.close();
			// return
			return OK;
		}catch (SQLException e){
			e.printStackTrace();
		}
		return CANCEL;
	}

	/**
	 * Add/Update user's friend information in MySQL table of module using module-specific information from request query
	 * @param myURadarID	-	user's U(niversal)Radar ID
	 * @param friendURadarID	-	friend's U(niversal)Radar ID
	 * @param reqprops		-	module-specific information sent with the query
	 */
	public String addupdateFriend(Properties reqprops){
		// get request needed properties
		String myURadarID;// = reqprops.getProperty("uradarid");
		String friendURadarID;// = reqprops.getProperty("uradarid_friend");
		// uradarid
		if(reqprops.containsKey(new String("uradarid"))){
			myURadarID = reqprops.getProperty("uradarid");
		}
		else{
			myURadarID = resolveModuleID(reqprops.getProperty("moduleid"));
		}
		//uradarid friend
		if(reqprops.containsKey("uradarid_friend")){
			friendURadarID = reqprops.getProperty("uradarid_friend");
		}
		else{
			friendURadarID = resolveModuleID(reqprops.getProperty("moduleid_friend"));
		}
		
		// check resolution
		if(myURadarID.equals(INVALID_ID) || friendURadarID.equals(INVALID_ID)){
			return FAIL;
		}
		
		// proceed request
		try{
			// check whether add or update
			boolean add = false;
			boolean update = false;
			Statement stmt0 = connection.createStatement();
			String sql_check = "select * " +
								"from "+this.name+"_friends " +
								"where (uradarid='"+myURadarID+"' and uradarid_friend='"+friendURadarID+"');";
			ResultSet rs_check = stmt0.executeQuery(sql_check);
			if(rs_check.first()){
				update = true;
			}
			else{
				add=true;
			}
			stmt0.close();
			// addupdate
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String visibility = reqprops.getProperty("visibility"); // third obligatory field
			String sql_addupdate_friend = new String();
			if(add) sql_addupdate_friend = "insert into "+this.name+"_friends"+"(uradarid, uradarid_friend, visibility)" +
											" values ('"+myURadarID+"','"+friendURadarID+"','"+visibility+"');";
			if(update) sql_addupdate_friend = "update "+this.name+"_friends " +
												"set visibility='"+visibility+"' " +
												"where (uradarid='"+myURadarID+"' and uradarid_friend='"+friendURadarID+"');";
			// execute query
			stmt.executeUpdate(sql_addupdate_friend);
			// close statement, garbage collector is not to be relied upon
			stmt.close();
			// 
			return OK;
		}catch (SQLException e){
			e.printStackTrace();
		}
		//
		return FAIL;
	}

	/**
	 * Get module-specific information about user
	 * @param myURadarID	-	user's U(niversal)Radar ID
	 * @return				-	XML-string representing module-specific information about user
	 */
	public String getOwnInfo(Properties reqprops){
		// init return/response string
		String out = new String();
		//
		String myURadarID = reqprops.getProperty("uradarid");
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
	
	public String getOwnInfoFromModule(Properties reqprops){
		// get request info
		String modulename = reqprops.getProperty("reqmodulefrom");
		// proceed request
		URadarModule module = URadar.getInstance().findModule(modulename);
		if(module != null && (module instanceof URadarUpdateModule)){
			return ((URadarUpdateModule) module).getOwnInfo(reqprops);
		}
		return null;
	}
	
	public String[] getFriendsURadarID(String myURadarID){
		// init return/response string
		ArrayList<String> out = new ArrayList<String>();
		// try to connect to DB for search
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			//String sql_get_friends = "select distinct uradarid_friend as uradarid from "+(this.name+"_friends")+" where uradarid='"+myURadarID+"' or select distinct uradarid from "+(this.name+"_friends")+" where uradarid_friend='"+myURadarID+"');";
			String sql_get_friends = "select distinct uradarid_friend as uradarid from "+(this.name+"_friends")+" where uradarid='"+myURadarID+"';";
			// query DB for own info
			ResultSet rs_get_friends = stmt.executeQuery(sql_get_friends);
			// check size of result -> if resultset is empty no resolution is possible
			if(rs_get_friends.first()){
				// generate return/response string
				out.add(rs_get_friends.getString("uradarid"));
			}
			else{
				// return
				out = null;
			}
			// close statement, garbage collector is not to be relied upon
			stmt.close();

			//return
			//System.out.println("URadarUpdateModule found friends:\t"+out.toString());
			return out.toArray(new String[out.size()]);

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
	public String getFriendsInfo(Properties reqprops){
		// init return/response string
		String out = new String();
		//
		String myURadarID = reqprops.getProperty("uradarid");
		// try to connect to DB for search
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String sql_get_friends_info = "select * from "+this.name+" where uradarid in (select distinct uradarid_friend from "+(this.name+"_friends")+" where uradarid='"+myURadarID+"' or select distinct uradarid from "+(this.name+"_friends")+" where uradarid_friend='"+myURadarID+"');";
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
	
	
	public String getFriendsInfo(String myURadarID, String[] friendsURadarID){
		// init return/response string
		String out = new String();
		// try to connect to DB for search
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String sql_get_friends_info = "";
			sql_get_friends_info += "select * from "+this.name+" where uradarid in ('";
			for(int i=0; i<friendsURadarID.length-1; i++){
				sql_get_friends_info += friendsURadarID[i] + "', '";
			}
			sql_get_friends_info += friendsURadarID[friendsURadarID.length-1];
			sql_get_friends_info += "');";
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
	
	public String getFriendsInfoFromModule(Properties reqprops){
		// get request info
		String myURadarID = reqprops.getProperty("uradarid");
		String[] friendsURadarID = getFriendsURadarID(myURadarID);
		String modulename = reqprops.getProperty("reqmodulefrom");
		// proceed request
		URadarModule module = URadar.getInstance().findModule(modulename);
		if(module != null && (module instanceof URadarUpdateModule)){
			return ((URadarUpdateModule) module).getFriendsInfo(myURadarID, friendsURadarID);
		}
		return null;
	}

	/**
	 * Remove user from users of this module
	 * @param myURadarID	-	user's U(niversal)Radar ID
	 * @param reqprops		-	module-specific information sent with the query
	 */
	public String removeUser(Properties reqprops){
		//
		String myURadarID = reqprops.getProperty("uradarid");
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
			// return 
			return OK;
		} catch (SQLException e){
			e.printStackTrace();
		}
		// return
		return null;
	}

	/**
	 * Remove user's friend from friends relationship table
	 * @param myURadarID	-	user's U(niversal)Radar ID
	 * @param friendURadarID	-	friend's U(niversal)Radar ID
	 * @param reqprop		-	module-specific information sent with the query
	 */
	public String removeFriend(Properties reqprops){
		//
		String myURadarID = reqprops.getProperty("uradarid");
		String friendURadarID = reqprops.getProperty("uradarid_friend");
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
			// 
			return OK;
		} catch (SQLException e){
			e.printStackTrace();
		}
		// return
		return null;
	}

	@Override
	public String proceedRequest(Properties reqprops) {
		// init response string
		String response = new String();
		// get request type
		String reqtype = reqprops.getProperty("reqtype");
		// switch
		if(reqtype.equals("resolve_module_id")){
			response = resolveModuleID(reqprops);
			return response;
		}
		if(reqtype.equals("addupdate_user")){
			response = addupdateUser(reqprops);
			return response;
		}
		if(reqtype.equals("addupdate_friend")){
			response = addupdateFriend(reqprops);
			return response;
		}
		if(reqtype.equals("get_own_info")){
			response = getOwnInfo(reqprops);
			return response;
		}
		if(reqtype.equals("get_friends_info")){
			response = getFriendsInfo(reqprops);
			return response;
		}
		if(reqtype.equals("get_own_info_from_module")){
			return getOwnInfo(reqprops);
		}
		if(reqtype.equals("get_friends_info_from_module")){
			return getFriendsInfoFromModule(reqprops);
		}
		if(reqtype.equals("remove_user")){
			response = removeUser(reqprops);
			return response;
		}
		if(reqtype.equals("remove_friend")){
			response = removeFriend(reqprops);
			return response;
		}
		// if no match in switch block		
		response = null;
		// return 
		return response;
	}

}
