package org.abratuhi.sqltimetool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class SQLTimeTool {
	
	private final static Logger LOG = Logger.getLogger(SQLTimeTool.class);
	
	private final static String COMMENT = "-";
	private final static String PRAGMA = "#";
	private final static String RUN = "!";
	
	public static void main(String[] args){
		try{
			BasicConfigurator.configure();
			
			String driver = null;
			String url = null;
			String user = null;
			String password = null;

			String log = new String();

			if(args.length < 2){
				System.out.println("Usage: java Measure benchmark.sql logadd");
				return;
			}

			String filename = args[0];
			String logadd = args[1];
			File file = new File(filename);
			if(!file.exists()){
				LOG.error("Benchmark file doesn' exist");
				return;
			}
			LOG.info("file: " + file.getAbsolutePath());

			// load config
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while(line != null && (driver==null || url==null || user==null || password==null) ){
				if(line.startsWith(PRAGMA)){
					line = line.substring(PRAGMA.length());
					
					if(line.startsWith("driver ")){
						driver = line.substring("driver ".length());
					}
					else if(line.startsWith("url ")){
						url = line.substring("url ".length());
					}
					else if(line.startsWith("user ")){
						user = line.substring("user ".length());
					}
					else if(line.startsWith("password ")){
						password = line.substring("password ".length());
					}
				}

				// read next
				line = br.readLine();
			}

			LOG.info("driver = " + driver);
			LOG.info("url = " + url);
			LOG.info("user = " + user);
			LOG.info("password = " + password);
			LOG.info("*");

			// setup connection
			Class.forName(driver);
			Connection conn = DriverManager.getConnection( url, user, password);
			
			conn.setAutoCommit(true);

			// AB: ASDF.SHADOW_ID_TRIGGER in Oracle opens too many cursors -> ORA-01000
			// AB: nCursors = nIterations
			// AB: current test script uses nIterations=100
			//if(driver.matches("OracleDriver")){
			//	Statement s = conn.createStatement();
			//	s.execute("ALTER SYSTEM SET open_cursors=1500");
			//	s.close();
			//}
			
			// measure variables
			long totalBegin = 0l;
			long totalEnd = 0l;
			long measureBegin = 0l;
			long measureEnd = 0l;
			String measureName = null;
			
			// statement variables
			boolean loop = false;
			int loopIterations = 0;
			ArrayList<PreparedStatement> loopStatements = new ArrayList<PreparedStatement>();
			

			// benchmark statements
			br = new BufferedReader(new FileReader(file));
			line = br.readLine();
			while(line != null){
				LOG.debug("proceed: " + line);
				
				if(line.startsWith(COMMENT)){
					line = line.substring(COMMENT.length());
					//TODO?
				}
				else if(line.startsWith(PRAGMA)){
					line = line.substring(PRAGMA.length());
					
					if(line.startsWith("begin measure ")){
						measureName = line.substring("begin measure ".length());
						measureBegin = System.currentTimeMillis();
					}
					else if(line.startsWith("end measure")){
						measureEnd = System.currentTimeMillis();
						LOG.info("MEASURED TIME [ " + measureName + " ] = " + (measureEnd-measureBegin) + " ms");
					}
					else if(line.startsWith("begin loop ")){
						loopStatements.clear();
						loop = true;
						loopIterations = Integer.parseInt(line.substring("begin loop ".length()));
					}
					else if(line.startsWith("end loop")){
						for(int loops = 0; loops < loopIterations; loops++){
							for(Iterator i = loopStatements.iterator(); i.hasNext(); ){
								((PreparedStatement)i.next()).execute();
							}
						}
						
						for(Iterator i = loopStatements.iterator(); i.hasNext(); ){
							((PreparedStatement)i.next()).close();
						}
					}
				}
				else if(line.startsWith(RUN)){
					line = line.substring(RUN.length());
					if(loop){
						PreparedStatement stmt = conn.prepareStatement(line);
						loopStatements.add(stmt);
						stmt.execute();
					}
					else{
						Statement stmt = conn.createStatement();
						stmt.execute(line);
						stmt.close();
					}
				}
				
				line = br.readLine();
			}

			// write log
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("log" + System.currentTimeMillis() + "_" + logadd + ".log")));
			bw.write(log);
			bw.flush();
			bw.close();

			// close connection
			conn.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
