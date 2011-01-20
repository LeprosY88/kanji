package org.abratuhi.bahnde.util;

import java.util.List;
import java.util.Map;

import org.abratuhi.bahnde.model.EdgeRoute;
import org.abratuhi.bahnde.model.Station;

public abstract class Algorithm {
	public abstract List<Station> getShortestPath(Station from, Station to, List<Station> nodes, List<EdgeRoute> edges);
	
	protected static int distance(Station from, Station to, List<EdgeRoute> edges){
		int result = Integer.MAX_VALUE;
		for(EdgeRoute edge : edges){
			if(edge.getDepartureStation().equals(from) && edge.getArrivalStation().equals(to)){
				if(result > edge.getDuration()){
					result = edge.getDuration();
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
