package org.abratuhi.uradar.modules;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.abratuhi.uradar.model.URadarModule;
import org.abratuhi.uradar.model.URadarModuleModel;

public class URadarModuleRegistration extends URadarModule{

	public URadarModuleRegistration(String name, String description,
			Properties props, Connection connection) {
		// super
		super(name, description, props, connection);
		// add registration module specific models
		// tables list
		String model_tablelist_name = new String("uradar_tables");
		String[] model_tablelist_fields = {"uradar_tablename", "uradar_tablegroup"};
		String[] model_tablelist_types = {"varchar(100)", "varchar(100)"};
		URadarModuleModel model_tablelist = new URadarModuleModel(model_tablelist_name, model_tablelist_fields, model_tablelist_types);
		// users list
		String model_users_name = new String("uradar_users");
		String[] model_users_fields = {"uradarid", "uradarpasswd"};
		String[] model_users_types = {"varchar(100)", "varchar(100)"};
		URadarModuleModel model_users = new URadarModuleModel(model_users_name, model_users_fields, model_users_types);
		// add
		ArrayList<URadarModuleModel> modelz = new ArrayList<URadarModuleModel>();
		modelz.add(model_tablelist);
		modelz.add(model_users);
		this.setModel(modelz);
		// create tables if needed
		if(!this.checkTables()) this.createTables();

	}

	@Override
	public String proceedRequest(Properties reqprops) {
		String reqtype = reqprops.getProperty("reqtype");
		if(reqtype.equals("addupdate_user")){
			return addupdateUser(reqprops);
		}
		if(reqtype.equals("remove_user")){
			return removeUser(reqprops);
		}
		return INVALID_REQUEST_ID;
	}

	/**
	 * Add/Update user information in MySQL table of module using module-specific information from request query
	 * @param myURadarID	-	user's U(niversal)Radar ID
	 * @param reqprops		-	module-specific information sent with the query
	 * @return				-	OK, if successfully added/updates user info
	 * 						-	CANCEL, otherwise
	 */
	public String addupdateUser(Properties reqprops){
		try{
			// get information from request
			String myURadarID = reqprops.getProperty("uradarid");
			String myURadarPasswd = reqprops.getProperty("uradarpasswd");
			// check whether user with this login is already registered, meaning
			// 		login present in db
			//		login<->password don't match
			Statement stmt1 = connection.createStatement();
			String sql_check_id = "select * from uradar_users where uradarid='"+myURadarID+"';";
			ResultSet rs_check_id = stmt1.executeQuery(sql_check_id);
			if(rs_check_id.first()){	// uradarid is already in use
				stmt1.close();
				return CANCEL;
			}
			else{	// uradarid is free to use
				// create statement
				Statement stmt = connection.createStatement();
				// generate query string
				String sql_addupdate_user = "insert into uradar_users(uradarid, uradarpasswd) values ('"+myURadarID+"', '"+myURadarPasswd+"');";
				// execute query
				stmt.executeUpdate(sql_addupdate_user);
				// close statement, garbage collector is not to be relied upon
				stmt.close();
				// return 
				return OK;
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		// extra return statement for db error cases
		return null;
	}
	
	/**
	 * Check whether user with uradaraid is already registered in DB.
	 * Purpose: avoid non-unique uradarids in DB
	 * 
	 * @param reqprops
	 * @return 	-	USER_OK, case login not present yet
	 * 			-	USER_CANCEL, case login already present
	 * 			-	NULL, otherwise
	 */
	public String checkUser(Properties reqprops){
		try{
			// get information from request
			String myURadarID = reqprops.getProperty("uradarid");
			String myURadarPasswd = reqprops.getProperty("uradarpasswd");
			// check whether user with this login is already registered, meaning
			// 		login present in db
			//		login<->password don't match
			Statement stmt1 = connection.createStatement();
			String sql_check_id = "select * from uradar_users where (uradarid='"+myURadarID+"' and uradarpasswd='"+myURadarPasswd+"');";
			ResultSet rs_check_id = stmt1.executeQuery(sql_check_id);
			if(rs_check_id.first()){	// uradarid is already in use
				stmt1.close();
				return USER_OK;
			}
			else{
				return USER_CANCEL;
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		// extra return statement for db error cases
		return null;
	}

	/**
	 * Remove user from users of this module
	 * @param myURadarID	-	user's U(niversal)Radar ID
	 * @param reqprops		-	module-specific information sent with the query
	 */
	public String removeUser(Properties reqprops){
		// try to connect to DB for search
		try{
			// get info
			String myURadarID = reqprops.getProperty("uradarid");
			// create statement
			Statement stmt = connection.createStatement();
			// generate query strings
			String passwd = reqprops.getProperty("passwd");
			String sql_delete_user = "delete  from "+this.name+" where uradarid='"+myURadarID+"'and uradadpasswd='"+passwd+"';";	// clean <modulname> table
			// query DB for own info
			stmt.executeUpdate(sql_delete_user);
			// close statement, garbage collector is not to be relied upon
			stmt.close();
			// return
			return OK;
		} catch (SQLException e){
			e.printStackTrace();
		}
		// return
		return CANCEL;
	}

	/*public void addModule(URadarModule module) {
		// try to connect to DB for search
		try{
			for(int i=0; i<module.models.size(); i++){
				// create statement
				Statement stmt = connection.createStatement();
				// generate query strings
				String sql_add_module = "insert  into uradar_tables(uradar_tablename, uradar_tablegroup) values('"+module.models.get(i).name+"', '"+module.name+"');";
				// query DB for own info
				stmt.executeUpdate(sql_add_module);
				// close statement, garbage collector is not to be relied upon
				stmt.close();
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
	}*/

	/*public void removeModule(URadarModule module) {
		// try to connect to DB for search
		try{
			// drop tables
			for(int i=0; i<module.models.size(); i++){
				// create statement
				Statement stmt = connection.createStatement();
				// generate query strings
				String sql_remove_module = "drop table "+module.models.get(i).name+";";
				// query DB for own info
				stmt.executeUpdate(sql_remove_module);
				// close statement, garbage collector is not to be relied upon
				stmt.close();
			}
			// delete corresponding rows in index table
			// create statement
			Statement stmt = connection.createStatement();
			// generate query strings
			String sql_remove_module = "delete from uradar_tables where uradar_tablegroup='"+module.name+"';";
			// query DB for own info
			stmt.executeUpdate(sql_remove_module);
			// close statement, garbage collector is not to be relied upon
			stmt.close();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}*/

	@Override
	public String resolveModuleID(Properties reqprops) {
		return reqprops.getProperty("uradarid");
	}

}
