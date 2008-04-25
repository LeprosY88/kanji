/**
 * @author Alexei Bratuhin
 * @version 1.0
 * @licence GPLv2
 */

package model;

import gui.GUI_SLRadar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.imageio.metadata.IIOMetadataNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class SLRadar {
	
	// some predefined SQL statements for making the initialization and reset of DB easier
	public static final String sql_rmtable_base = "drop table base;";
	public static final String sql_rmtable_sl = "drop table sl;";
	public static final String sql_rmtable_fb = "drop table fb;";
	public static final String sql_rmtable_friends = "drop table friends;";
	
	// BASE TABLE - contains correspondence between DB's unique ID and Facebook ID and Second Life ID
	public static final String sql_crtable_base = "create table base(" +
	"id bigint primary key auto_increment," +
	"fbid varchar(100)," +
	"slid varchar(100));";
	// SL TABLE - contains all information about DB ID in Second Life, references DB ID from BASE TABLE
	public static final String sql_crtable_sl = "create table sl(" +
	"id bigint primary key references base(id) on delete cascade," +
	"name varchar(100)," +
	"status varchar(30)," +
	"region varchar(100)," +
	"position varchar(50)," +
	"velocity varchar(50)," +
	"rotation varchar(50));";
	// FB TABLE - contains all information about DB ID in Facebook, references DB ID from BASE TABLE
	//			- currently empty and unused, since the only request can come from Facebook and the information contained in this table isn't relevant for SL queries
	public static final String sql_crtable_fb = "create table fb(" +
	"id bigint primary key references base(id) on delete cascade," +
	"name varchar(100));";
	// FRIENDS TABLE - contains all information about friendreationship in tables like FB TABLE (in other words, all social networks friendships)
	public static final String sql_crtable_friends = "create table friends(" +
	"id1 bigint references base(id) on delete cascade," +
	"id2 bigint references base(id) on delete cascade);";

	
	static SLRadar instance = null;
	String CONFIG_FILE = "slradar.properties";	// configuration file
	Properties props;	// some configuraton parameters, currently only those related to MySQL DB

	String host;
	String db;
	String user;
	String passwd;

	Connection connection = null;

	/**
	 * The following part makes sure this class is used in Singleton way
	 */
	public SLRadar(){
		loadJDBCDriver();
		loadProperties();
		connectDB();
		/*if(!presentAllTables()){
			dropAllTables();
			createAllTables();
		}*/
	}

	public static SLRadar getInstance(){
		if( instance == null){
			instance = new SLRadar();
		}
		return instance;
	}
	
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException();
	}
	
	/**
	 * Try to load the MySQL DB driver
	 * In case the driver isn't installed or accessible (which shouldn't be the case, since the driver is included in current project's classpath)
	 * 		an exception is thrown and the application is aborted
	 */
	private void loadJDBCDriver(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Function to load the properties file - for better readability
	 */
	private void loadProperties(){
		// load properties file
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
		// read properties
		host = props.getProperty("mysql.host");
		db = props.getProperty("mysql.db");
		user = props.getProperty("mysql.user");
		passwd = props.getProperty("mysql.passwd");
	}
	
	/**
	 * Create a connection to running MySQL DB with parameters from configuration file
	 * In case the connection fails, application is closed
	 */
	private void connectDB(){
		try {
			connection = DriverManager.getConnection("jdbc:mysql://"+host+"/"+db, user, passwd);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * When stopping the server first close connection to MySQL DB
	 */
	private void disconnectDB(){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check, whether all tables needed for proper work of application (BASE, SL, FB, FRIENDS) are present
	 * @return 	true, if all tables needed for proper work of application are present
	 * 			false, all other cases
	 */
	private boolean presentAllTables(){
		try {
			// initialize connection
			connectDB();
			
			Statement stm = connection.createStatement();
			// check, whether tables to be created are already present in db
			DatabaseMetaData md = connection.getMetaData();
			ResultSet rs_base = md.getTables(null, null, "base", null);
			ResultSet rs_sl = md.getTables(null, null, "sl", null);
			ResultSet rs_fb = md.getTables(null, null, "fb", null);
			ResultSet rs_friends = md.getTables(null, null, "friends", null);
			// check whether all tables neede are already in db
			if(rs_base.first() && rs_sl.first() && rs_fb.first() && rs_friends.first())
				return true;
			else return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Reset the MySQL DB by dropping all tables - then no dirty data for sure
	 * TODO: maybe just "DELETE FROM TABLES" ??
	 */
	private void dropAllTables(){	
		try {
			//initialize connection
			connectDB();
			
			// create statement
			Statement stmt = connection.createStatement();

			// load metadata to find out, whether tables to be dropped are present in db
			DatabaseMetaData md = connection.getMetaData();
			ResultSet rs_base = md.getTables(null, null, "base", null);
			ResultSet rs_sl = md.getTables(null, null, "sl", null);
			ResultSet rs_fb = md.getTables(null, null, "fb", null);
			ResultSet rs_friends = md.getTables(null, null, "friends", null);

			// drop tables, if they are present in db
			if(rs_friends.first()) stmt.executeUpdate(sql_rmtable_friends);
			if(rs_sl.first()) stmt.executeUpdate(sql_rmtable_sl);
			if(rs_fb.first()) stmt.executeUpdate(sql_rmtable_fb);
			if(rs_base.first()) stmt.executeUpdate(sql_rmtable_base);

			// close statement, garbage collector is not to be relied upon
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create all tables needed for proper work of application (those are: BASE, SL, FB, FRIENDS)
	 */
	private void createAllTables(){
		try {
			// initialize connection
			connectDB();
			
			// create statement
			Statement stmt = connection.createStatement();

			// check, whether tables to be created are already present in db
			DatabaseMetaData md = connection.getMetaData();
			ResultSet rs_base = md.getTables(null, null, "base", null);
			ResultSet rs_sl = md.getTables(null, null, "sl", null);
			ResultSet rs_fb = md.getTables(null, null, "fb", null);
			ResultSet rs_friends = md.getTables(null, null, "friends", null);

			// create tables, that are not present in db yet
			if(!rs_base.first()) stmt.executeUpdate(sql_crtable_base);
			if(!rs_sl.first()) stmt.executeUpdate(sql_crtable_sl);
			if(!rs_fb.first()) stmt.executeUpdate(sql_crtable_fb);
			if(!rs_friends.first()) stmt.executeUpdate(sql_crtable_friends);

			// close statement, garbage collector is not to be relied upon
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check, whether user with either @fbid or @slid has already received a DB ID
	 * 		if not	-	add user to DB
	 * 		else	-	try to update @fbid or @slid if needed
	 * @param fbid	-	Facebook ID
	 * @param slid	-	Second Life ID
	 */
	public void addupdateUser(String fbid, String slid){
		try{
			// initialize connection
			connectDB();
			
			// create statement
			Statement stmt = connection.createStatement();
			Statement stmt2= connection.createStatement();
			Statement stmt3= connection.createStatement();
			
			// build sql query paying attention to the fact that fbid or slid may be dummy
			String sql_fbid_or_slid = new String();
			if(fbid!=null && fbid!="" && slid!=null && slid!="") sql_fbid_or_slid = "select id from base where fbid='"+fbid+"' or slid='"+slid+"';";
			else{
				if(fbid!=null && fbid!="") sql_fbid_or_slid = "select id from base where fbid='"+fbid+"';";
				else if(fbid!=null && fbid!="" && slid!=null && slid!="") sql_fbid_or_slid = "select id from base where slid='"+slid+"';";
			}
			
			// check whether given fbid or slid are already present in db
			ResultSet rs_fbid_or_slid = stmt.executeQuery(sql_fbid_or_slid);
			
			if(rs_fbid_or_slid.first() ){	// given fbid or slid are already in use
											// no need to add user once more
											// hence -> update user
				// get user id
				int id = rs_fbid_or_slid.getInt("id");
				
				//update base table
				String sql_update_user_base = "UPDATE base SET ";
				if(fbid != null && fbid != "") sql_update_user_base += "fbid='"+fbid+"'";
				if(slid != null && slid != "") sql_update_user_base += ", slid='"+slid+"'";
				sql_update_user_base += " WHERE id="+id+";";
				stmt.executeUpdate(sql_update_user_base);				
			}
			else{	// neither fbid nor slid were found in db
				// insert a record in base table
				String sql_insert_user_base = "insert into base(fbid, slid) " +
											"values ('"+fbid+"', '"+slid+"');";
				stmt.executeUpdate(sql_insert_user_base);
				
				// get the id new user got in base table
				String sql_get_user_id = "select id from base " +
											"where fbid='"+fbid+"' and slid='"+slid+"';";
				ResultSet rs_id = stmt.executeQuery(sql_get_user_id);
				rs_id.first();
				int id = rs_id.getInt("id");
				
				// insert a record into fb table
				String sql_insert_user_fb = "insert into fb(id) " +
												"values('"+id+"');";
				stmt2.executeUpdate(sql_insert_user_fb);
				
				// insert a record into sl table
				String sql_insert_user_sl = "insert into sl(id) " +
												"values('"+id+"');";
				stmt3.executeUpdate(sql_insert_user_sl);
			}
			
			// close statement, garbage collector is not to be relied upon
			stmt.close();
			stmt2.close();
			stmt3.close();
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Add/update Second Life information of user having Facebook ID = @fbid or Second Life ID = @slid to MySQL DB
	 * @param fbid		-	Facebook ID
	 * @param slid		-	Second Life ID
	 * @param reqprop	-	request/query properties/data
	 */
	public void addupdateSLInfo(String fbid, String slid, Properties reqprop){
		try{
			// initialize connection
			connectDB();
			
			// create statement
			Statement stmt = connection.createStatement();

			// get the id new user got in base table
			// build sql query paying attention to the fact that fbid or slid may be dummy
			String sql_fbid_or_slid = new String();
			if(fbid!=null && fbid!="" && slid!=null && slid!="") sql_fbid_or_slid = "select id from base where fbid='"+fbid+"' or slid='"+slid+"';";
			else{
				if(fbid!=null && fbid!="") sql_fbid_or_slid = "select id from base where fbid='"+fbid+"';";
				else if(fbid!=null && fbid!="" && slid!=null && slid!="") sql_fbid_or_slid = "select id from base where slid='"+slid+"';";
			}
			// get response
			ResultSet rs_id = stmt.executeQuery(sql_fbid_or_slid);
			// proceed response if not empty
			if(rs_id.first()){
				int id = rs_id.getInt("id");

				// get required for addupdate info from reqprop 
				String sl_name = reqprop.getProperty("name");
				String sl_status = reqprop.getProperty("status");
				String sl_region = reqprop.getProperty("region");
				String sl_pos = reqprop.getProperty("position");
				String sl_vel = reqprop.getProperty("velocity");
				String sl_rot = reqprop.getProperty("rotation");

				// addupdate information in sl table
				String sql_addupdate_info_sl = "UPDATE sl" +
												" SET name='"+sl_name+"', region='"+sl_region+"', status='"+sl_status + 
												"', position='"+sl_pos+"', velocity='"+sl_vel+"', rotation='"+sl_rot + 
												"' WHERE id="+id+";";
				stmt.executeUpdate(sql_addupdate_info_sl);
			}
			
			// close statement, garbage collector is not to be relied upon
			stmt.close();
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Add @fbid2 DB ID to @fbid1 DB ID as friend in case both fbid's are present in DB
	 * @param fbid1	-	Facebook ID of user
	 * @param fbid2	-	FAcebook ID of user's friend
	 */
	public void addFBFriend(String fbid1, String fbid2){
		try{
			// initialize connection
			connectDB();
			
			// create statement
			Statement stmt = connection.createStatement();
			Statement stmt2= connection.createStatement();

			// check whether they are both present in db
			String sql_get_user_id1 = "select id from base where fbid='"+fbid1+"';";
			String sql_get_user_id2 = "select id from base where fbid='"+fbid2+"';";
			
			ResultSet rs_id1 = stmt.executeQuery(sql_get_user_id1);
			ResultSet rs_id2 = stmt2.executeQuery(sql_get_user_id2);
			
			if(rs_id1.first() && rs_id2.first()){ // both users are present in db
				// extract ids
				//int id1 = rs_id1.getInt("id");
				//int id2 = rs_id2.getInt("id");
				int id1 = rs_id1.getInt("id");
				int id2 = rs_id2.getInt("id");
				
				// check whether they already are friends
				String sql_check_friends = "select * from friends where (id1='"+id1+"' and id2='"+id2+"') or (id1='"+id2+"' and id2='"+id1+"');";
				ResultSet rs_are = stmt.executeQuery(sql_check_friends);
				
				if(rs_are.first()){ // they are already friends, do nothing
					return;
				}
				else{	// they aren't friends yet, so make 'em
					String sql_make_friends = "insert into friends(id1, id2) values ('"+id1+"', '"+id2+"');";
					stmt.executeUpdate(sql_make_friends);
				}
			}
			else{ // one or both of users are not in db, meaning they haven't installed slradar fb application, therefore no need to update friends table
				return;
			}
			
			// close statement, garbage collector is not to be relied upon
			stmt.close();
			stmt2.close();
			
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieve information about oneself in Second Life as CSV-like string
	 * @param fbid	-	Facebook ID of oneself
	 * @param slid	-	Second Life ID of oneself
	 * @return	Second Life information about oneself as string; information pieces are separates with ';'
	 */
	public String getOwnSLInfo(String fbid, String slid) {
		String result = new String();
		try{
			// initialize connection
			connectDB();
			
			// create statement
			Statement stmt = connection.createStatement();

			// check whether this user is present in db
			String sql_fbid_or_slid = new String();
			if(fbid!=null && fbid!="" && slid!=null && slid!="") sql_fbid_or_slid = "select id from base where fbid='"+fbid+"' or slid='"+slid+"';";
			else{
				if(fbid!=null && fbid!="") sql_fbid_or_slid = "select id from base where fbid='"+fbid+"';";
				else if(fbid!=null && fbid!="" && slid!=null && slid!="") sql_fbid_or_slid = "select id from base where slid='"+slid+"';";
			}
			// get response
			ResultSet rs_id = stmt.executeQuery(sql_fbid_or_slid);
			// proceed if response not empty
			if(rs_id.first()){	// user is present in db
				// extract id
				int id = rs_id.getInt("id");
				// query
				String sql_get_own_info = "select fbid, name, status, region, position, velocity, rotation " +
												"from sl natural join base " +
												"where id = " + id + ";";
				ResultSet rs_own_info = stmt.executeQuery(sql_get_own_info);
				
				// check whether result is not empty
				if(!rs_own_info.first()){	// the result set is empty
					result = null;
				}
				else{	//the result set isn't empty				
					// build the output string in CSV-like format
					do{
						// extract info from current result set row
						String fb_id = rs_own_info.getString("fbid");
						String sl_name = rs_own_info.getString("name");
						String sl_status = rs_own_info.getString("status");
						String sl_reg = rs_own_info.getString("region");
						String sl_pos = rs_own_info.getString("position");
						String sl_vel = rs_own_info.getString("velocity");
						String sl_rot = rs_own_info.getString("rotation");
						// put info into output string
						result += fb_id+";"+sl_name+";"+sl_status+";"+sl_reg+";"+sl_pos+";"+sl_vel+";"+sl_rot;
					}
					while(rs_own_info.next());
				}
				
			}
			else{	// user is not present in db
				result = null;
			}
			
			// close statement, garbage collector is not to be relied upon
			stmt.close();
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		// return
		return result;
	}
	
	/**
	 * Retrieve information about oneself's friends in Second Life as CSV-like string
	 * @param fbid	-	Facebook ID of oneself
	 * @param slid	-	Second Life ID of oneself
	 * @return	Second Life information about oneself's friends as string; information pieces are separates with ';'
	 */
	public String getFriendsSLInfo(String fbid, String slid){
		String result = new String();
		try{
			// initialize connection
			connectDB();
			
			// create statement
			Statement stmt = connection.createStatement();

			// check whether this user is present in db
			String sql_fbid_or_slid = new String();
			if(fbid!=null && fbid!="" && slid!=null && slid!="") sql_fbid_or_slid = "select id from base where fbid='"+fbid+"' or slid='"+slid+"';";
			else{
				if(fbid!=null && fbid!="") sql_fbid_or_slid = "select id from base where fbid='"+fbid+"';";
				else if(fbid!=null && fbid!="" && slid!=null && slid!="") sql_fbid_or_slid = "select id from base where slid='"+slid+"';";
			}
			// get response
			ResultSet rs_id = stmt.executeQuery(sql_fbid_or_slid);
			// proceed if response not empty			
			if(rs_id.first()){	// user is present in db
				// extract id
				int id = rs_id.getInt("id");
				// query
				String sql_get_friends_info = "select fbid, name, status, region, position, velocity, rotation " +
												"from sl natural join base " +
												"where id in (select id2 from friends where id1='"+id+"') or " +
													" id in (select id1 from friends where id2='"+id+"') ;";
				ResultSet rs_friends_info = stmt.executeQuery(sql_get_friends_info);
				
				// check whether result is not empty
				if(!rs_friends_info.first()){	// the result set is empty
					//System.out.println("NO FRIENDS, SORRY");
					result = null;
				}
				else{	//the result set isn't empty				
					// build the output string in CSV-like format
					do{
						// extract info from current result set row
						String fb_id = rs_friends_info.getString("fbid");
						String sl_name = rs_friends_info.getString("name");
						String sl_status = rs_friends_info.getString("status");
						String sl_reg = rs_friends_info.getString("region");
						String sl_pos = rs_friends_info.getString("position");
						String sl_vel = rs_friends_info.getString("velocity");
						String sl_rot = rs_friends_info.getString("rotation");
						// put info into output string
						result += fb_id+";"+sl_name+";"+sl_status+";"+sl_reg+";"+sl_pos+";"+sl_vel+";"+sl_rot+"|";
					}
					while(rs_friends_info.next());
				}
				
				// remove last separation char, if result not empty
				if(result != null) result = result.substring(0, result.length()-1);
				
			}
			else{	// user is not present in db
				//System.out.println("YOU ARE NOT IN DB, SORRY");
				result = null;
			}
			
			// close statement, garbage collector is not to be relied upon
			stmt.close();
			
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		// return; a csv-like string or null, if things went wrong
		return result;
	}
	
	
	
	
	/**
	 * Test stub for testing the basic connectivity option with MySQL DB
	 */
	
	public static void main(String[] args){
		// get/create instance, connect to db
		SLRadar slr = SLRadar.getInstance();
		// clear db
		slr.dropAllTables();
		// fill db with empty tables
		slr.createAllTables();
		// disconnect from db
		slr.disconnectDB();
		
	}

}
