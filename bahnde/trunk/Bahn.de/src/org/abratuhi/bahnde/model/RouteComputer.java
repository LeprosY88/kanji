package org.abratuhi.bahnde.model;

import java.util.Date;
import java.util.List;

import org.abratuhi.bahnde.db.DbDataGetter;
import org.abratuhi.bahnde.output.Route;
import org.abratuhi.bahnde.util.Dijkstra;
import org.apache.commons.collections.map.MultiKeyMap;

public class RouteComputer {
	
	public Route getRoute(Station from, Station to, Date start, Date end, String types){
		List<Station> stations = DbDataGetter.getStations();
		MultiKeyMap edges = DbDataGetter.getRouteEdgesAsMKMap(start);
		
		Route result = new Dijkstra().getShortestPath(from, to, stations, edges);
		
		return result;
	}

}
