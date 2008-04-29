package org.abratuhi.uradar.model;

import java.util.ArrayList;

import org.abratuhi.uradar.util.ArrayUtil;

public class URadarModuleModel {
	
	public final static boolean CHECK_TYPES = true;	// flag to check whether all incoming types are contained in predefined array of valid types
	public final static boolean CHECK_OBLIGATORY_FIELDS = true;	// flas to check whether all predefined obligatory fields contained in predefined array of obligatory fields are present
	
	public static final String[] TYPES = {"varchar(10)", "varchar(20)", "varchar(50)",
											"varchar(100)", "varchar(1000)"};	// predefined array of valid types
	public static final String[] OBLIGATORY_FIELDS = {"uradarid"};	// predefined array of fields obligatory to be present
	
	String[] fieldnames;	// MySQL table's fields' names
	String[] fieldtypes;	// MySQL table's fields' types
	String sql_query_create;		// SQL query to create MySQL table for a module using this model
	String sql_query_delete;		// SQL query to create MySQL table for a module using this model
	
	public URadarModuleModel(String[] fields, String[] types){
		// arrays must have the same length
		if(fields.length != types.length){
			System.out.println("URadarModuleModel Constructor:\tparameter arrays must have the same same size.");
			return;
		}

		// check type if CHECK_TYPES flag set
		if(CHECK_TYPES){
			// init utiler
			ArrayUtil<String> arrayutiler = new ArrayUtil<String>();
			// check
			for(int i=0; i<fields.length; i++){
				if(!arrayutiler.contains(TYPES, types[i])){
					System.out.println("URadarModuleModel Constructor:\tparameter array types contains undefined value:\t"+types[i]);
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
					System.out.println("URadarModuleModel Constructor:\tparameter array fields doesn't contains obligatory field:\t"+OBLIGATORY_FIELDS[i]);
				}
			}
		}

		// copy values to model
		fieldnames = new String[fields.length];
		fieldtypes = new String[types.length];		
		fieldnames = fields.clone();
		fieldtypes = types.clone();
	}
	
	public void generateCreateSQLQuery(String tablename){
		// init query string
		this.sql_query_create = new String();
		
		// register tables in table list
		this.sql_query_create += "insert into uradar_tables(tablename, tablegroup) values("+tablename+","+tablename+");\n";
		this.sql_query_create += "insert into uradar_tables(tablename, tablegroup) values("+tablename+"_friends,"+tablename+");\n";
		// create tablename
		// head
		this.sql_query_create += "create table "+tablename+" ( ";
		// body
		for(int i=0; i<fieldnames.length; i++){
			this.sql_query_create += fieldnames[i]+" "+fieldtypes[i]+" , ";			
		}
		// tail
		this.sql_query_create += ");\n";
		// create tablename_friends
		// head
		this.sql_query_create += "create table "+tablename+"_friends"+" ( ";		
		// body
		this.sql_query_create += "uradarid varchar(100), ";
		this.sql_query_create += "uradarid_friend varchar(100), ";
		this.sql_query_create += "visibility varchar(10) ";
		// tail
		this.sql_query_create += ");\n";
		
		// return
		return;
	}
	
	public void generateDeleteSQLQuery(String tablename){
		// init
		this.sql_query_delete = new String();
		// tablename head, body, tail
		this.sql_query_delete += "drop table "+tablename+";\n";
		// tablename_friends head, body, tail
		this.sql_query_delete += "drop table "+tablename+"_friends;\n";
		// unregister tablename, tablename_friends
		this.sql_query_delete += "delete from uradar_tables where tablegroup='"+tablename+"' ;";
	}
}
