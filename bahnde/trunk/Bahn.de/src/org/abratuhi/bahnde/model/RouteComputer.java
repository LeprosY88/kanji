package org.abratuhi.bahnde.model;

import java.util.Date;
import java.util.List;

import org.abratuhi.bahnde.db.DbDataGetter;
import org.abratuhi.bahnde.output.Route;

public class RouteComputer {
	
	public Route getRoute(Station from, Station to, Date start, Date end, String types){
		List<Station> stations = DbDataGetter.getStations();
		List<RouteEdge> edges = DbDataGetter.getRouteEdges();
		
		Route result = null;
		
		return result;
	}

}
