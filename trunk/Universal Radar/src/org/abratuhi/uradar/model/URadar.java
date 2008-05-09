package org.abratuhi.uradar.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.abratuhi.uradar.modules.URadarModuleFB;
import org.abratuhi.uradar.modules.URadarModuleRegistration;
import org.abratuhi.uradar.modules.URadarModuleSL;
import org.abratuhi.uradar.util.RequestUtil;

public class URadar extends HttpServlet{
	static URadar instance;
	
	Properties props;
	Connection connection;
	ArrayList<URadarModule> modules = new ArrayList<URadarModule>();
	
	String host;
	String db;
	String user;
	String passwd;
	
	/**
	 * Singleton: begin
	 */
	public URadar(){
		// load props
		loadProperties();
		// open connection
		connectDB();
		// init uradarregistration
		URadarModuleRegistration uradarregistration = new URadarModuleRegistration("base", "table containing list of all user in system", props, connection);
		modules.add(uradarregistration);
		// init fb
		URadarModuleFB fb = new URadarModuleFB(null, null, props, connection);
		addModule(fb);
		// init sl
		URadarModuleSL sl = new URadarModuleSL(null, null, props, connection);
		addModule(sl);
		
	}

	public static URadar getInstance(){
		if( instance == null){
			instance = new URadar();
		}
		return instance;
	}
	
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException();
	}
	/**
	 * Singleton: end
	 */
	
	
	/*public void addModule(URadarModule module){
		if(uradarregistration != null && module != null){
			modules.add(module);
			uradarregistration.addModule(module);
		}
	}*/
	
	/*public void removeModule(URadarModule module){
		if(uradarregistration != null && module != null){
			modules.remove(module);
			uradarregistration.removeModule(module);
		}
	}*/
	
	/*public String proceedRequest(Properties reqprops){
		String out = new String();
		String reqapp = reqprops.getProperty("reqapp");
		String reqmodule = reqprops.getProperty("reqmodule");
		if(reqapp.equals("uradar")){
			URadarModule tmodule = findModule(reqmodule);
			if(tmodule != null){
				out = tmodule.proceedRequest(reqprops);
			}
		}
		return out;
	}*/
	
	public void addModule(URadarModule module){
		modules.add(module);
	}
	
	public void removeModule(URadarModule module){
		modules.remove(module);
	}
	
	public URadarModule findModule(String modulename){
		for(int i=0; i<modules.size(); i++){
			if(modules.get(i).name.equals(modulename)){
				return modules.get(i);
			}
		}
		return null;
	}
	/**
	 * Function to load the properties file - for better readability
	 */
	private void loadProperties(){
		// load properties file
		FileInputStream fis;
		Properties props = new Properties();
		try {
			fis = new FileInputStream("uradar.properties");
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
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// init response string
		String response = new String();
		// get request string - string after '?'
		String request = req.getQueryString();
		System.out.println(request);
		// transform request string to request string
		Properties reqprops = RequestUtil.request2properties(request);
		// get basic properties of request
		String reqtype = reqprops.getProperty("reqtype");
		String reqapp = reqprops.getProperty("reqapp");
		String reqmod = reqprops.getProperty("reqmodule");
		// find module to proceed request
		URadarModule moduleused = findModule(reqmod);
		if(moduleused!=null){
			// send request to corresponding module, get response
			response = moduleused.proceedRequest(reqprops);
			// send response string over existing opened HTTP connection
			PrintWriter out = res.getWriter();
			out.println(response);
			out.close();
		}
		else{
			// print error message
			System.out.println("Module not found:\t"+reqmod);
			// send response string over existing opened HTTP connection
			PrintWriter out = res.getWriter();
			out.println(response);
			out.close();
		}
	}
	
	/**
	 * Extending the inherited destroy()-method with additional features
	 * 		- disconnecting from DB
	 */
	public void destroy(){
		// close connectoin to MySQL DB
		disconnectDB();
		// super
		super.destroy();
	}
	
}
