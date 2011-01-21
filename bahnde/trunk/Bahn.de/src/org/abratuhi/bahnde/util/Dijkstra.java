package org.abratuhi.bahnde.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.abratuhi.bahnde.model.RouteEdge;
import org.abratuhi.bahnde.model.Station;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;

public class Dijkstra extends Algorithm {
	
	private final static Logger LOG = Logger.getLogger(Dijkstra.class);
	
	public List<Station> getShortestPath(Station from, Station to, List<Station> nodes, MultiKeyMap edges){
		List<Station> result = new Vector<Station>();
		List<RouteEdge> route = new Vector<RouteEdge>();
		
		List<Station> q = new Vector<Station>(nodes);
		Map<Station, Integer> dist = new HashMap<Station, Integer>();
		Map<Station, Station> previous = new HashMap<Station, Station>();
		
		MultiKeyMap connections = new MultiKeyMap();
		
		for(Station node : q){
			dist.put(node, Integer.MAX_VALUE);
			previous.put(node, null);
		}
		
		dist.put(from, 0);
		//previous.put(from, from);
		
		
		while(!q.isEmpty()){
			Station u = getClosest(q, dist);
			q.remove(u);

			//LOG.debug("Dijkstra: proceed on node " + u.getName() + "(" + u.getId() + ")");
			
			for(Station neighbour : u.getIncidentStations()){
				RouteEdge edge = getLightestKnownEdge(u, neighbour, edges);
				int alt = dist.get(u) + edge.getCost();
				if(alt < dist.get(neighbour)){
					dist.put(neighbour, alt);
					previous.put(neighbour, u);
					connections.put(u, neighbour, edge); 
				}
			}
		}
		
		Station last = to;
		while(!last.equals(from)){
			result.add(0, last);
			
			route.add(0, (RouteEdge) connections.get(previous.get(last), last));
			
			last = previous.get(last);
		}
		result.add(0, last); // ~ result.add(0, from);
		
		for(int i=0; i<route.size(); i++){
			RouteEdge edge = route.get(i);
			LOG.debug("Routing " + edge.getDepartureStation().getId() + " -> " + edge.getArrivalStation().getId());
		}
		
		
		return result;
	}

}
