package org.abratuhi.uradar.model;

import java.util.ArrayList;

import org.abratuhi.uradar.util.ArrayUtil;

public class URadarModuleModel {
	
	public static boolean CHECK_TYPES = true;	// flag to check whether all incoming types are contained in predefined array of valid types
	public static boolean CHECK_OBLIGATORY_FIELDS = false;	// flag to check whether all predefined obligatory fields contained in predefined array of obligatory fields are present
	
	public static final String[] TYPES = {"varchar(10)", "varchar(20)", "varchar(50)",
											"varchar(100)", "varchar(1000)"};	// predefined array of valid types
	public static final String[] OBLIGATORY_FIELDS = {"uradarid"};	// predefined array of fields obligatory to be present
	
	public String name;			// MySQL table name
	String[] fieldnames;	// MySQL table's fields' names
	String[] fieldtypes;	// MySQL table's fields' types
	ArrayList<String> sql_query_create = new ArrayList<String>();		// SQL query to create MySQL table for a module using this model
	ArrayList<String> sql_query_delete = new ArrayList<String>();		// SQL query to create MySQL table for a module using this model
	
	public URadarModuleModel(String name, String[] fields, String[] types){
		// arrays must have the same length
		if(fields.length != types.length){
			System.out.println("URadarModuleCustomModel Constructor:\tparameter arrays must have the same same size.");
			return;
		}

		// check type if CHECK_TYPES flag set
		if(CHECK_TYPES){
			// init utiler
			ArrayUtil<String> arrayutiler = new ArrayUtil<String>();
			// check
			for(int i=0; i<fields.length; i++){
				if(!arrayutiler.contains(TYPES, types[i])){
					System.out.println("URadarModuleCustomModel Constructor:\tparameter array types contains undefined value:\t"+types[i]);
					return;
				}
			}
		}
		
		// check whether obligatory fields (e.g. U(niversal)RadarID) are present
		if(CHECK_OBLIGATORY_FIELDS){
			// init utiler
			ArrayUtil<String> arrayutiler = new ArrayUtil<String>();
			// check
			for(int i=0; i<OBLIGATORY_FIELDS.length; i++){
				if(!arrayutiler.contains(fields, OBLIGATORY_FIELDS[i])){
					System.out.println("URadarModuleCustomModel Constructor:\tparameter array fields doesn't contains obligatory field:\t"+OBLIGATORY_FIELDS[i]);
				}
			}
		}

		// copy values to model
		this.fieldnames = new String[fields.length];
		this.fieldtypes = new String[types.length];
		
		this.name = name;
		this.fieldnames = fields.clone();
		this.fieldtypes = types.clone();
		
	}
	
	public void generateCreateSQLQuery(String module){
		// init query string
		String sql = new String();
		
		// create tablename
		// head
		sql += "create table "+name+" ( \n";
		// body
		for(int i=0; i<fieldnames.length-1; i++){
			sql += fieldnames[i]+" "+fieldtypes[i]+" , \n";			
		}
		sql += fieldnames[fieldnames.length-1]+" "+fieldtypes[fieldnames.length-1]+" \n";	
		// tail
		sql += ");\n";
		
		sql_query_create.add(sql);

		// init query string
		sql = new String();
		// register tables in table list
		sql += "insert into uradar_tables(uradar_tablename, uradar_tablegroup) \nvalues('"+name+"', '"+module+"');\n";
		
		sql_query_create.add(sql);

		
		// System.out
		//System.out.println(sql_query_create);
		
		// return
		return;
	}
	
	public void generateDeleteSQLQuery(String module){
		// init
		String sql = new String();
		// tablename head, body, tail
		sql = "drop table "+name+";\n";
		sql_query_delete.add(sql);
		// unregister tablename, tablename_friends
		sql = new String();
		sql = "delete from uradar_tables where uradar_tablename='"+name+"' and uradar_tablegroup='"+module+"';";
		sql_query_delete.add(sql);
		// System.out
		//System.out.println(sql_query_delete);
	}
}
