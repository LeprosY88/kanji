package org.abratuhi.bahnde.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.abratuhi.bahnde.model.EdgeRoute;
import org.abratuhi.bahnde.model.Station;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class Dijkstra {
	
	private final static Logger LOG = Logger.getLogger(Dijkstra.class);
	
	public static List<Station> getShortestPath(Station from, Station to, List<Station> nodes, List<EdgeRoute> edges){
		BasicConfigurator.configure();
		
		List<Station> result = new Vector<Station>();
		
		List<Station> q = new Vector<Station>(nodes);
		Map<Station, Integer> dist = new HashMap<Station, Integer>();
		Map<Station, Station> previous = new HashMap<Station, Station>();
		
		for(Station node : q){
			dist.put(node, Integer.MAX_VALUE);
			previous.put(node, null);
		}
		
		dist.put(from, 0);
		previous.put(from, from);
		
		
		while(!q.isEmpty()){
			Station u = getClosest(q, dist);
			q.remove(u);

			LOG.debug("Dikstra: proceed on node " + u.getName() + "(" + u.getId() + ")");
			
			for(Station neighbour : u.getIncidentStations()){
				int alt = dist.get(u) + distance(u, neighbour, edges);
				if(alt < dist.get(neighbour)){
					dist.put(neighbour, alt);
					previous.put(neighbour, u);
				}
			}
		}
		
		Station last = to;
		while(!last.equals(from)){
			result.add(0, last);
			last = previous.get(last);
		}
		result.add(0, last); // ~ result.add(0, from);
		
		
		return result;
	}
	
	
	private static int distance(Station from, Station to, List<EdgeRoute> edges){
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
	
	private static Station getClosest(List<Station> q, Map<Station, Integer> nodes){
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
