package org.abratuhi.bahnde.db;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import org.abratuhi.bahnde.model.RouteEdge;
import org.abratuhi.bahnde.model.Station;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.BasicConfigurator;

import andrei.bratuhin.model.EdgeRouteProviderStub;
import andrei.bratuhin.model.StationProviderStub;

public class DbUtilTest extends TestCase {

	static {
		BasicConfigurator.configure();
	}

	public void testGenerateStationsSqlStatements() {
		List<Station> stations = StationProviderStub.getStations();

		Random generator = new Random();
		
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("data.sql"), true)));
			
			for (Station station : stations) {
				bw.write(String.format(
						"INSERT INTO \"stations\" (\"id\", \"name\", \"duration\", \"coordinates\") "
								+ "VALUES (%d, '%s', %d, '%s');\n", station.getId(),
						station.getName(), generator.nextInt(10),
						station.getCoordinatesAsString()));
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			try{bw.flush();} catch(Throwable t){};
			try{bw.close();} catch(Throwable t){};
		}
	}

	public void testGenerateRoutesSqlStatements() {
		int counter = 1;
		MultiKeyMap medges = EdgeRouteProviderStub.getEdges();
		
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("data.sql"), true)));
			for (List<RouteEdge> edges : ((Collection<List<RouteEdge>>) medges
					.values())) {
				for (RouteEdge edge : edges) {
					bw.write(String.format(
							"INSERT INTO \"incident_stations\"(\"id\", \"id_start\", \"id_end\") "
									+ "VALUES(%d, %d, %d);\n", counter++, edge
									.getDepartureStation().getId(), edge
									.getArrivalStation().getId()));
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			try{bw.flush();} catch(Throwable t){};
			try{bw.close();} catch(Throwable t){};
		}
		
		
		
		
	}

	public void testGenerateTrainsSqlScript() {
		int counter = 1;
		Random generator = new Random();
		MultiKeyMap medges = EdgeRouteProviderStub.getEdges();
		
		BufferedWriter bw = null;
		try{
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("data.sql"), true)));
			
			for (List<RouteEdge> edges : ((Collection<List<RouteEdge>>) medges
					.values())) {
				for (RouteEdge edge : edges) {

					Calendar now = Calendar.getInstance();

					Calendar year0 = Calendar.getInstance();
					year0.set(now.get(Calendar.YEAR), 1 - 1, 1);

					Calendar year1 = Calendar.getInstance();
					year1.set(now.get(Calendar.YEAR), 1 - 1, 8);

					

					while (year0.before(year1)) {
						String[] types = {"S", "RE", "IRE", "IC", "EC", "ICE"};
						
						int random1 = generator.nextInt(60) + 30;
						int random2 = generator.nextInt(60) + 30;
						int random3 = generator.nextInt(6);
						
						bw.write(String.format("INSERT INTO \"routes\"(\"id\", \"edge_id\", \"start\", \"duration\", \"type\") " +
								"VALUES(%d, %d, '%s', %d, '%s');\n", counter, edge.getId(), new SimpleDateFormat("yyyy-MM-dd HH:mm").format(year0.getTime()), random1, types[random3]));
						
						counter ++;
						year0.add(Calendar.MINUTE, random1 + random2);
					}
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			try{bw.flush();} catch(Throwable t){};
			try{bw.close();} catch(Throwable t){};
		}

		
	}

}
