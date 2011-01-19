package org.abratuhi.bahnde.model;

import java.util.List;
import java.util.Vector;

public class Station {
	/** Unique identifier of the station**/
	private int id;
	
	/** Local name of the station**/
	private String name;
	
	/** Time needed to change the train**/
	private long duration;
	
	/** List of incidents stations - stations reachable with a direct train connection**/
	private List<Station> incidentStations = new Vector<Station>();
	
	/** Coordinates for presentation purposes only.
	 * These coordinates are not used to compute any kind of distance between the stations.
	 * See {@link EdgeRoute#getDuration()}**/
	private List<Double> coordinates;
	
	
	public void addIncidentStation(Station station){
		incidentStations.add(station);
	}
	
	public void setCoordinates(String coordinates){
		String[] coords = coordinates.split(";");
		this.coordinates = new Vector<Double>();
		this.coordinates.add(Double.parseDouble(coords[0]));
		this.coordinates.add(Double.parseDouble(coords[1]));
	}
	
	
	public boolean equals(Object obj){
		return ((Station)obj).getId() == getId();
	}
	
	public int hashCode(){
		return getId();
	}
	
	
	
	
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public List<Station> getIncidentStations() {
		return incidentStations;
	}

	public void setIncidentStations(List<Station> incidentStations) {
		this.incidentStations = incidentStations;
	}

	public List<Double> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Double> coordinates) {
		this.coordinates = coordinates;
	}
}
