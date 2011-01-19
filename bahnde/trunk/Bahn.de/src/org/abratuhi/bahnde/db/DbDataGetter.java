package org.abratuhi.bahnde.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import org.abratuhi.bahnde.model.Station;

public class DbDataGetter {
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
}
