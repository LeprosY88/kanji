package org.abratuhi.bahnde.util;

import java.util.List;
import java.util.Map;

import org.abratuhi.bahnde.model.RouteEdge;
import org.abratuhi.bahnde.model.Station;
import org.apache.commons.collections.map.MultiKeyMap;

public abstract class Algorithm {
	public abstract List<Station> getShortestPath(Station from, Station to, List<Station> nodes, MultiKeyMap edges);
	
	protected static RouteEdge getLightestKnownEdge(Station from, Station to, MultiKeyMap edges){
		RouteEdge result = RouteEdge.MAX_VALUE;
		
		List<RouteEdge> edgs = (List<RouteEdge>) edges.get(from, to);
		if (edgs != null) {
			for (RouteEdge edge : edgs) {
				if (result.getCost() > edge.getCost()) {
					result = edge;
				}
			}
		}
		
		return result;
	}
	
	protected static Station getClosest(List<Station> q, Map<Station, Integer> nodes){
		int min = Integer.MAX_VALUE;
		Station station = null;
		
		for(Station node : q){
			if(nodes.get(node) < min){
				min = nodes.get(node);
				station = node;
			}
		}
		
		return station;
	}
}
