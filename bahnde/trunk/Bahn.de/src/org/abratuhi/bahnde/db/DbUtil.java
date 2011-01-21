package org.abratuhi.bahnde.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.abratuhi.bahnde.model.Station;
import org.apache.log4j.Logger;

import andrei.bratuhin.model.StationProviderStub;

public class DbUtil {
	
	private final static Logger LOG = Logger.getLogger(DbUtil.class);
	
	private final static String DB_NAME = "bahndb";

	public static Connection getConnection() {
		Connection connection = null;
		String driver = "org.apache.derby.jdbc.EmbeddedDriver";

		try {
			Class.forName(driver);
			String connectionURL = "jdbc:derby:" + DB_NAME + ";create=true";
			try {
				connection = DriverManager.getConnection(connectionURL);
			} catch (Throwable e) {
				e.printStackTrace();
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return connection;
	}
   
   public static List<String> generateTrains(){
     List<String> statements = new Vector<String>();
     //List<Station> stations = DbDataGetter.getStations();
     List<Station> stations = StationProviderStub.getStations();
     
     Random generator = new Random();
     
     for(Station station : stations){
         for(Station neighbour : station.getIncidentStations()){
           Calendar now = Calendar.getInstance();
           
           Calendar year0 = Calendar.getInstance();
           year0.set(now.get(Calendar.YEAR), 1-1, 1);
           
           Calendar year1 = Calendar.getInstance();
           year1.set(now.get(Calendar.YEAR), 12-1, 31);
           
           LOG.debug("Starting with: " + new SimpleDateFormat("yyyy-MM-dd").format(year0.getTime()));
           LOG.debug("Ending with: " + new SimpleDateFormat("yyyy-MM-dd").format(year1.getTime()));
           
           while(year0.before(year1)){
             int random1 = generator.nextInt(60);
             int random2 = generator.nextInt(30);
             //System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(year0.getTime()) + "-> " + random1 + ", " + random2);
             year0.add(Calendar.MINUTE, random1+random2);
           }
         }
     }
     
     return statements;
   }

	public static void shutdown() {

		boolean gotSQLExc = false;
		try {
			DriverManager.getConnection("jdbc:derby:" + DB_NAME + ";shutdown=true");
		} catch (SQLException se) {
			if (se.getSQLState().equals("XJ015") || se.getSQLState().equals("08006")) {
				gotSQLExc = true;
			}
		}
		if (!gotSQLExc) {
			System.out.println("Database did not shut down normally");
		} else {
			System.out.println("Database shut down normally");
		}

	}

}
