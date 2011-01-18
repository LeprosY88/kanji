package org.abratuhi.bahnde.model;

import java.util.Date;

public class EdgeRoute {
	/** Unique identifier of the station**/
	private int id;
	
	/** Local name of the station**/
	private String name;
	
	/** Departure time of the train**/
	private Date departure;
	
	/** Duration of the train trip**/
	private long duration;
	
	/** Departure station**/
	private Station departureStation;
	
	/** Arrival station**/
	private Station arrivalStation;
	
	
	
	
	
	

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

	public Date getDeparture() {
		return departure;
	}

	public void setDeparture(Date departure) {
		this.departure = departure;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Station getDepartureStation() {
		return departureStation;
	}

	public void setDepartureStation(Station departureStation) {
		this.departureStation = departureStation;
	}

	public Station getArrivalStation() {
		return arrivalStation;
	}

	public void setArrivalStation(Station arrivalStation) {
		this.arrivalStation = arrivalStation;
	}

}
