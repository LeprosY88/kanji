package org.abratuhi.bahnde.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.abratuhi.bahnde.model.RouteEdge;
import org.abratuhi.bahnde.model.Station;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;

public class DbDataGetter {
	
	private final static Logger LOG = Logger.getLogger(DbDataGetter.class);
	
	public static Station getStation(int id){
		List<Station> stations = getStations();
		
		for(Station station : stations){
			if(station.getId() == id){
				return station;
			}
		}
		
		return null;
	}
	
	public static Station getStation(String name){
		List<Station> stations = getStations();
		
		for(Station station : stations){
			if(station.getName().equals(name)){
				return station;
			}
		}
		
		return null;
	}
	
	public static Map<Integer, Station> getStationsAsMap(){
		Map<Integer, Station> result = new HashMap<Integer, Station>();
		List<Station> stations = getStations();
		for(Station station : stations){
			result.put(station.getId(), station);
		}
		return result;
	}
	
	public static List<Station> getStations(){
		List<Station> result = new Vector<Station>();
		Map<Integer, Station> map = new HashMap<Integer, Station>();
		
		try{
			Connection connection = DbUtil.getConnection();
			
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery("" +
					"SELECT \"id\", \"name\", \"duration\", \"coordinates\" " +
					"FROM \"stations\"");
			
			while(rs.next()){
				int id = rs.getInt("id");
				String name = rs.getString("name");
				long duration = rs.getLong("duration");
				String coordinates = rs.getString("coordinates");
				
				Station station = new Station();
				station.setId(id);
				station.setName(name);
				station.setDuration(duration);
				station.setCoordinatesFromString(coordinates);
				
				result.add(station);
				map.put(id, station);
				
			}
			
			rs.close();
			
			stmt.close();
			
			

			Statement substmt = connection.createStatement();
			ResultSet subrs = substmt.executeQuery("SELECT \"id_start\", \"id_end\" FROM \"incident_stations\"");
			while(subrs.next()){
				int id1 = subrs.getInt(1);
				int id2 = subrs.getInt(2);
				map.get(id1).addIncidentStation(map.get(id2));
			}
			subrs.close();
			substmt.close();
			
			connection.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static List<RouteEdge> getRouteEdges(Map<Integer, Station> stations, Date startingFrom, String types){
		List<RouteEdge> result = new Vector<RouteEdge>();
		
		try{
			Connection connection = DbUtil.getConnection();
			
			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			String stmtString = "" +
			"SELECT routes.\"id\", inci_st.\"id_start\", inci_st.\"id_end\", routes.\"start\", routes.\"duration\", routes.\"type\" " +
			"FROM \"incident_stations\" inci_st, \"routes\" routes " +
			"WHERE inci_st.\"id\" = routes.\"edge_id\"";
			
			if(types != null){
				stmtString += " AND routes.\"type\" in (" + types + ")";
			}
			
			ResultSet rs = stmt.executeQuery(stmtString);
			
			while(rs.next()){
				int id = rs.getInt("id");
				int id_start = rs.getInt("id_start");
				int id_end = rs.getInt("id_end");
				String start = rs.getString("start");
				int duration = rs.getInt("duration");
				String type = rs.getString("type");
				
				Date departure = null;
				try{
					departure = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(start);
				}
				catch(ParseException e){
					e.printStackTrace();
					// do nothing
				}
				
				RouteEdge edge = new RouteEdge();
				edge.setId(id);
				edge.setDepartureStation(stations.get(id_start));
				edge.setArrivalStation(stations.get(id_end));
				edge.setDeparture(departure);
				edge.setDuration(duration);
				edge.setType(type);
				edge.setCost((int) (duration + (departure.getTime() - startingFrom.getTime())/1000/60));
				
				if(startingFrom != null && departure.after(startingFrom)){
					result.add(edge);
				}
				
			}
			
			rs.close();
			
			stmt.close();
			
			connection.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static List<RouteEdge> getRouteEdges(Date start, String types){
		return getRouteEdges(getStationsAsMap(), start, types);
	}
	
	
	public static MultiKeyMap getRouteEdgesAsMKMap(Date start, String types){
		List<RouteEdge> edges = getRouteEdges(start, types);
		
		MultiKeyMap result = new MultiKeyMap();
		
		for(RouteEdge edge : edges){
			List<RouteEdge> ledges = (List<RouteEdge>) result.get(edge.getDepartureStation(), edge.getArrivalStation());
			if(ledges == null){
				ledges = new Vector<RouteEdge>();
				result.put(edge.getDepartureStation(), edge.getArrivalStation(), ledges);
			}
			ledges.add(edge);
		}
		
		return result;
	}
}
