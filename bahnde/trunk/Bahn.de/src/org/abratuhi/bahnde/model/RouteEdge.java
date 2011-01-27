package org.abratuhi.bahnde.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RouteEdge {
	public final static RouteEdge MAX_VALUE = valued(Integer.MAX_VALUE);
	public final static RouteEdge ZERO_VALUE = valued(0);
	
	
	
	/** Unique identifier of the station**/
	private int id;
	
	/** Local name of the station**/
	private String name;
	
	/** Departure time of the train**/
	private Date departure;
	
	/** Duration of the train trip**/
	private int duration;
	
	/** Departure station**/
	private Station departureStation;
	
	/** Arrival station**/
	private Station arrivalStation;
	
	private int cost;
	
	private String type;
	
	
	
	
	public int getCostFromDate(Date date){
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(departure);
		
		int idle = (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / 1000 / 60);
		
		return duration + idle;
	}
	
	public String getText(){
		return getDepartureStation().getName() + 
		" at " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(getDeparture()) + 
		" --- " + getDuration() + "min --> " + 
		getArrivalStation().getName() + 
		" using [" + getType() + "]";
	}
	
	
	
	
	private static RouteEdge valued(Integer i){
		RouteEdge edge = new RouteEdge();
		edge.cost = i;
		return edge;
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

	public Date getDeparture() {
		return departure;
	}

	public void setDeparture(Date departure) {
		this.departure = departure;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
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




	public int getCost() {
		return cost;
	}




	public void setCost(int cost) {
		this.cost = cost;
	}




	public String getType() {
		return type;
	}




	public void setType(String type) {
		this.type = type;
	}

}
