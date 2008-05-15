package org.abratuhi.uradar.modules;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.abratuhi.uradar.model.URadarModule;
import org.abratuhi.uradar.model.URadarModuleModel;
import org.abratuhi.uradar.util.ResponseUtil;

public class URadarModuleSL extends URadarModule{

	public URadarModuleSL(String name, String description, Properties props,
			Connection connection) {
		// super
		super("sl", "Second Life update module", props, connection);
		// custom
		// specify the corresponding table
		String sl_name = "sl";
		String[] sl_fieldnames = {"uradarid", "moduleid", "sl_name", "sl_realm", "sl_position", "sl_status"};
		String[] sl_fieldtypes = {"varchar(100)", "varchar(100)", "varchar(100)", "varchar(100)", "varchar(100)", "varchar(100)"};
		URadarModuleModel sl_model = new URadarModuleModel(sl_name, sl_fieldnames, sl_fieldtypes);
		// add
		ArrayList<URadarModuleModel> modelz = new ArrayList<URadarModuleModel>();
		modelz.add(sl_model);
		this.setModel(modelz);
		// create tables if needed
		if(!this.checkTables()) this.createTables();
	}

	@Override
	public String proceedRequest(Properties reqprops) {
		// get request type
		String reqtype = reqprops.getProperty("reqtype");
		// switch
		if(reqtype.equals("resolve_module_id")){
			return resolveModuleID(reqprops);
		}
		if(reqtype.equals("addupdate_user")){
			return addupdateUser(reqprops);
		}
		//return
		return null;
		
	}
	
	public String addupdateUser(Properties props){
		// init response string
		String response = new String();
		// get info from obligatory fields
		String myURadarID = props.getProperty("uradarid");
		String mySLID = props.getProperty("moduleid");
		// get rest of info
		String mySLName = props.getProperty("sl_name");
		String mySLRealm = props.getProperty("sl_realm");
		String mySLPosition = props.getProperty("sl_position");
		String mySLStatus = props.getProperty("sl_status");
		// update MySQL DB
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String sql_addupdate_user = null;
			String resolved = resolveModuleID(props);
			if(resolved.equals(INVALID_ID) || resolved.equals(FAIL)){
			if(resolved.equals(INVALID_ID)){
				sql_addupdate_user = "insert into sl(uradarid, moduleid, sl_name, sl_realm, sl_position, sl_status)" +
										" values ('"+myURadarID+"','"+mySLID+"','"+ mySLName+"','"+mySLRealm+"','"+mySLPosition+"','"+mySLStatus+"');";
			}
			if(resolved.equals(FAIL)){
				response = resolved;
			}
			}
			else{
				sql_addupdate_user = "update sl set sl_realm='"+mySLRealm+"', sl_position='"+mySLPosition+"', sl_status='"+mySLStatus+"'" +
				" where uradarid='"+myURadarID+"';";
			}
			
			// execute query
			if(sql_addupdate_user!= null) stmt.executeUpdate(sql_addupdate_user);
			// close statement, garbage collector is not to be relied upon
			stmt.close();
			// return
			response = OK;
		}catch (SQLException e){
			e.printStackTrace();
			//
			response = CANCEL;
		}
		// return
		return response;
	}
	
	public String getOwnInfo(String myURadarID){
		// init return/response string
		String out = new String();
		// try to connect to DB for search
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String sql_get_own_info = "select * from sl where uradarid='"+myURadarID+"';";
			// query DB for own info
			ResultSet rs_get_own_info = stmt.executeQuery(sql_get_own_info);
			// check size of result -> if resultset is empty no resolution is possible
			//if(rs_get_own_info.first()){
				// generate return/response string
				out = ResponseUtil.convertResultSet2XMLString(rs_get_own_info);
			//}
			//else{
				// return
			//	out = null;
			//}
			// close statement, garbage collector is not to be relied upon
			stmt.close();

			//return
			return out;

		} catch (SQLException e){
			e.printStackTrace();
		}
		// return null if connection to DB couldn't be established
		return FAIL;
	}
	
	public String getOwnInfoWithFB(String myURadarID){
		// init return/response string
		String out = new String();
		// try to connect to DB for search
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String sql_get_own_info = "select sl.uradarid, sl.moduleid as slid, fb.moduleid as fbid, sl_name, sl_status, sl_realm, sl_position " +
								"from sl inner join fb on sl.uradarid = fb.uradarid " +
								"where sl.uradarid='"+myURadarID+"';";
			// query DB for own info
			ResultSet rs_get_own_info = stmt.executeQuery(sql_get_own_info);
			// check size of result -> if resultset is empty no resolution is possible
			//if(rs_get_own_info.first()){
				// generate return/response string
				out = ResponseUtil.convertResultSet2XMLString(rs_get_own_info);
			//}
			//else{
				// return
			//	out = null;
			//}
			// close statement, garbage collector is not to be relied upon
			stmt.close();

			//return
			return out;

		} catch (SQLException e){
			e.printStackTrace();
		}
		// return null if connection to DB couldn't be established
		return FAIL;
	}


	public String getFriendsInfo(String[] friendsURadarID){
		// init return/response string
		String out = new String();
		// try to connect to DB for search
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String sql_get_friends_info = "";
			sql_get_friends_info += "select * from sl where uradarid in ('";
			for(int i=0; i<friendsURadarID.length-1; i++){
				sql_get_friends_info += friendsURadarID[i] + "', '";
			}
			sql_get_friends_info += friendsURadarID[friendsURadarID.length-1];
			sql_get_friends_info += "');";
			// query DB for own info
			ResultSet rs_get_friends_info = stmt.executeQuery(sql_get_friends_info);
			// check size of result -> if resultset is empty no resolution is possible
			//if(rs_get_friends_info.first()){
				// generate return/response string
				out = ResponseUtil.convertResultSet2XMLString(rs_get_friends_info);
			//}
			//else{
				// return
			//	out = null;
			//}
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
	
	public String getFriendsInfoWithFB(String[] friendsURadarID){
		// init return/response string
		String out = new String();
		// try to connect to DB for search
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String sql_get_friends_info = "";
			sql_get_friends_info += "select sl.uradarid, sl.moduleid as slid, fb.moduleid as fbid, sl_name, sl_status, sl_realm, sl_position " +
					"from sl inner join fb on sl.uradarid = fb.uradarid " +
					"where sl.uradarid in ('";
			for(int i=0; i<friendsURadarID.length-1; i++){
				sql_get_friends_info += friendsURadarID[i] + "',' ";
			}
			sql_get_friends_info += friendsURadarID[friendsURadarID.length-1];
			sql_get_friends_info += "');";
			// query DB for own info
			ResultSet rs_get_friends_info = stmt.executeQuery(sql_get_friends_info);
			// check size of result -> if resultset is empty no resolution is possible
			//if(rs_get_friends_info.first()){
				// generate return/response string
				out = ResponseUtil.convertResultSet2XMLString(rs_get_friends_info);
			//}
			//else{
				// return
			//	out = null;
			//}
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

	
	
	public void removeUser(String myURadarID, Properties reqprops){
		// try to connect to DB for search
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query strings
			String sql_delete_user = "delete  from sl where uradarid='"+myURadarID+"';";	// clean <modulname> table
			// query DB for own info
			stmt.executeUpdate(sql_delete_user);
			// close statement, garbage collector is not to be relied upon
			stmt.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	@Override
	public String resolveModuleID(Properties reqprops) {
		// init return/response string
		String out = new String();
		// get request details
		String moduleID = reqprops.getProperty("moduleid");
		// try to connect to DB for search
		try{
			// create statement
			Statement stmt = connection.createStatement();
			// generate query string
			String sql_check_moduleid = "select uradarid from sl where moduleid='"+moduleID+"';";
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
		return FAIL;
	}
}
