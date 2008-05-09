package org.abratuhi.uradar.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public abstract class URadarModule {
	
	public final static String OK = "ok";
	public final static String CANCEL = "cancel";
	public final static String INVALID_MODULE_ID = "invalid_module_id";
	public final static String INVALID_ID = "invalid_id";
	public final static String USER_OK = "user_ok";
	public final static String USER_CANCEL = "user cancel";
	
	public String name;	// name of the module, simultaneously name of MySQL tablegroup in DB
	String description;	// short description of the module
	public ArrayList<URadarModuleModel> models;	// model of the module
	Properties props;	// module specific parameters
	protected Connection connection;	// connection to MySQL DB
	
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
	
	public abstract String proceedRequest(Properties props);
	
	public abstract String resolveModuleID(Properties reqprops);
	
	/**
	 * Set model of the module - generally, names and types of fields in MySQL table
	 * @param model		-	table model
	 */
	public void setModel(ArrayList<URadarModuleModel> models){
		// set model
		this.models = (ArrayList<URadarModuleModel>) models.clone();
		// generate sql queries for each module
		for (int i=0; i<this.models.size(); i++){
			this.models.get(i).generateCreateSQLQuery(this.name);
			this.models.get(i).generateDeleteSQLQuery(this.name);
		}
	}
	
	/**
	 * Get model of the module
	 * @return	-	table model
	 */
	public ArrayList<URadarModuleModel> getModel(){
		return models;
	}
	
	/**
	 * Create MySQL tables in DB using the module's model
	 * @return
	 */
	public boolean createTables(){
		if(models != null){
			for(int i=0; i<models.size(); i++){
				try{
					// create statement
					Statement stmt = connection.createStatement();
					// System.out
					System.out.println("Executing SQL query:\t"+models.get(i).sql_query_create);
					// create tables in DB
					for(int j=0; j<models.get(i).sql_query_create.size(); j++){
						stmt.executeUpdate(models.get(i).sql_query_create.get(j));
					}
					// close statement, garbage collector is not to be relied upon
					stmt.close();
				} catch (SQLException e){
					e.printStackTrace();
				}
			}
			// return
			return true;
		}
		// case things went wrong
		return false;
	}
	
	/**
	 * Checks whether all tables from model are present in MySQL DB
	 * @return	true, if all tables are present
	 * 			false, otherwise
	 */
	public boolean checkTables(){
		// init return/response variable
		boolean result = false;
		if(models != null){
			for(int i=0; i<models.size(); i++){
				try {
					// init statement
					Statement stmt = connection.createStatement();
					// check, whether tables are present in db
					DatabaseMetaData md = connection.getMetaData();
					ResultSet modulename = md.getTables(null, null, models.get(i).name, null);
					// check whether all tables are already in db
					if(modulename.first()){
						result = true;
					}
					else{
						result = false;
						return result;
					}
					// close statement, garbage collector is not to be relied upon
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		// return
		return result;
	}
	
	/**
	 * Delete MySQL tables in DB
	 * @return
	 */
	public boolean deleteTables(){
		if(models != null){
			for(int i=0; i<models.size(); i++){
				try{
					// create statement
					Statement stmt = connection.createStatement();
					// create tables in DB
					for(int j=0; j<models.get(i).sql_query_delete.size(); j++){
						stmt.executeUpdate(models.get(i).sql_query_delete.get(j));
					}
					// close statement, garbage collector is not to be relied upon
					stmt.close();
				} catch (SQLException e){
					e.printStackTrace();
				}
			}
			// return
			return true;
		}
		// case things went wrong
		return false;
	}
}
