package org.abratuhi.bahnde.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.abratuhi.bahnde.model.RouteEdge;
import org.abratuhi.bahnde.model.Station;
import org.abratuhi.bahnde.output.Route;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;

public abstract class Algorithm {
	private final static Logger LOG = Logger.getLogger(Algorithm.class);
	
	public abstract Route getShortestPath(Station from, Station to, Date starting, List<Station> nodes, MultiKeyMap edges);
	
	protected static RouteEdge getLightestKnownEdge(Station from, Station to, Date starting, MultiKeyMap edges){
		LOG.debug("getLightestKnownEdge: from="+from.getId() + ", to=" + to.getId());
		RouteEdge result = RouteEdge.MAX_VALUE;
		
		List<RouteEdge> edgs = (List<RouteEdge>) edges.get(from, to);
		LOG.debug("getLightestKnownEdge: Proceeding " + edges.size() + " edges.");
		if (edgs != null) {
			for (RouteEdge edge : edgs) {
				if (edge.getDeparture().after(starting) && result.getCost() > edge.getCost()) {
					result = edge;
					LOG.debug("getLightestKnownEdge: Found " + edge.getId() + " with cost " + edge.getCost() + " closer and after " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(starting) + ".");
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
