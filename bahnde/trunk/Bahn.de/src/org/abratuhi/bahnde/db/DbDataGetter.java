package org.abratuhi.bahnde.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import org.abratuhi.bahnde.model.RouteEdge;
import org.abratuhi.bahnde.model.Station;

public class DbDataGetter {
	
	public static Station getStation(int id){
		Station result = null;
		
		try{
			Connection connection = DbUtil.getConnection();
			
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery("" +
					"SELECT \"id\", \"name\", \"duration\", \"coordinates\" " +
					"FROM stations " +
					"WHERE \"id\" = " + id);
			
			while(rs.next()){
				String name = rs.getString("name");
				long duration = rs.getLong("duration");
				String coordinates = rs.getString("coordinates");
				
				result = new Station();
				result.setId(id);
				result.setName(name);
				result.setDuration(duration);
				result.setCoordinates(coordinates);
				
				System.out.println("Station, id="+id + ", name=" + name + ", duration="+duration + ", coordinates=" + coordinates);
				
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
	
	public static Station getStation(String name){
		Station result = null;
		
		try{
			Connection connection = DbUtil.getConnection();
			
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery("" +
					"SELECT \"id\", \"name\", \"duration\", \"coordinates\" " +
					"FROM stations " +
					"WHERE \"name\" = '" + name + "'");
			
			while(rs.next()){
				int id = rs.getInt("id");
				long duration = rs.getLong("duration");
				String coordinates = rs.getString("coordinates");
				
				result = new Station();
				result.setId(id);
				result.setName(name);
				result.setDuration(duration);
				result.setCoordinates(coordinates);
				
				System.out.println("Station, id="+id + ", name=" + name + ", duration="+duration + ", coordinates=" + coordinates);
				
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
	
	public static List<Station> getStations(){
		List<Station> result = new Vector<Station>();
		
		try{
			Connection connection = DbUtil.getConnection();
			
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery("" +
					"SELECT \"id\", \"name\", \"duration\", \"coordinates\" " +
					"FROM stations");
			
			while(rs.next()){
				int id = rs.getInt("id");
				String name = rs.getString("name");
				long duration = rs.getLong("duration");
				String coordinates = rs.getString("coordinates");
				
				Station station = new Station();
				station.setId(id);
				station.setName(name);
				station.setDuration(duration);
				station.setCoordinates(coordinates);
				
				System.out.println("Station, id="+id + ", name=" + name + ", duration="+duration + ", coordinates=" + coordinates);
				
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
	
	public static List<RouteEdge> getRouteEdges(){
		List<RouteEdge> result = new Vector<RouteEdge>();
		
		try{
			Connection connection = DbUtil.getConnection();
			
			Statement stmt = connection.createStatement();
			
			ResultSet rs = stmt.executeQuery("" +
					"SELECT routes.\"id\", inci_st.\"id_start\", inci_st.\"id_end\", routes.\"start\", routes.\"duration\", routes.\"type\" " +
					"FROM \"incident_stations\" inci_st, \"routes\" routes " +
					"WHERE inci_st.\"id\" = routes.\"edge_id\"");
			
			while(rs.next()){
				int id = rs.getInt("routes.id");
				int id_start = rs.getInt("inci_st.id_start");
				int id_end = rs.getInt("inci_st.id_end");
				String start = rs.getString("routes.start");
				int duration = rs.getInt("routes.duration");
				String type = rs.getString("routes.type");
				
				RouteEdge edge = new RouteEdge();
				edge.setId(id);
				edge.setDepartureStation(getStation(id_start));
				edge.setArrivalStation(getStation(id_end));
				edge.setDuration(duration);
				edge.setType(type);
				
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
}
