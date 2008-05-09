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
		return null;
	}

	/**
	 * Add/Update user information in MySQL table of module using module-specific information from request query
	 * @param myURadarID	-	user's U(niversal)Radar ID
	 * @param reqprops		-	module-specific information sent with the query
	 */
	public String addupdateUser(String myURadarID, Properties reqprops){
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String modulenameid = reqprops.getProperty(this.name+"id"); // second obligatory field
			String sql_addupdate_user = "insert into "+this.name+"(uradarid, moduleid) values ("+myURadarID+","+modulenameid+");";
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
	public void addupdateFriend(String myURadarID, String friendURadarID, Properties reqprops){
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String visibility = reqprops.getProperty("visibility"); // third obligatory field
			String sql_addupdate_friend = "insert into "+this.name+"_friends"+"(uradarid, uradarid_friend, visibility)" +
			" values ("+myURadarID+","+friendURadarID+","+visibility+");";
			System.out.println("URadarUpdateModule addupdateFriend():\t"+sql_addupdate_friend);
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
	
	public String[] getFriendsURadarID(String myURadarID){
		// init return/response string
		ArrayList<String> out = new ArrayList<String>();
		// try to connect to DB for search
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String sql_get_friends = "select distinct uradarid_friend as uradarid from "+(this.name+"_friends")+" where uradarid='"+myURadarID+"' or select distinct uradarid from "+(this.name+"_friends")+" where uradarid_friend='"+myURadarID+"');";
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
			System.out.println("URadarUpdateModule found friends:\t"+out.toString());
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
	public String getFriendsInfo(String myURadarID){
		// init return/response string
		String out = new String();
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
			sql_get_friends_info += "select * from "+this.name+" where uradarid in (";
			for(int i=0; i<friendsURadarID.length-1; i++){
				sql_get_friends_info += friendsURadarID[i] + ", ";
			}
			sql_get_friends_info += friendsURadarID[friendsURadarID.length-1];
			sql_get_friends_info += ");";
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
	
	public String getFriendsInfoFromModule(String myURadarID, String[] friendsURadarID, String modulename){
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
	public void removeFriend(String myURadarID, String friendURadarID, Properties reqprops){
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

	@Override
	public String proceedRequest(Properties reqprops) {
		// get requester id
		String myURadarID = reqprops.getProperty("uradarid");
		// get request type
		String reqtype = reqprops.getProperty("reqtype");
		// switch
		if(reqtype.equals("resolve_module_id")){
			resolveModuleID(reqprops);
			return OK;
		}
		if(reqtype.equals("addupdate_user")){
			addupdateUser(myURadarID, reqprops);
			return OK;
		}
		if(reqtype.equals("addupdate_friend")){
			String friendURadarID = reqprops.getProperty("uradarid_friend");
			addupdateFriend(myURadarID, friendURadarID, reqprops);
			return OK;
		}
		if(reqtype.equals("get_own_info")){
			return getOwnInfo(myURadarID);
		}
		if(reqtype.equals("get_friends_info")){
			return getFriendsInfo(myURadarID);
		}
		if(reqtype.equals("remove_user")){
			removeUser(myURadarID, reqprops);
			return OK;
		}
		if(reqtype.equals("remove_friend")){
			String friendURadarID = reqprops.getProperty("uradarid_friend");
			removeFriend(myURadarID, friendURadarID, reqprops);
			return OK;
		}
		// return if no match in switch block
		return null;
	}

}
