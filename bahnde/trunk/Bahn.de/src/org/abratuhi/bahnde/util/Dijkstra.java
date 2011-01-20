package org.abratuhi.bahnde.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.abratuhi.bahnde.model.EdgeRoute;
import org.abratuhi.bahnde.model.Station;
import org.apache.log4j.Logger;

public class Dijkstra extends Algorithm {
	
	private final static Logger LOG = Logger.getLogger(Dijkstra.class);
	
	public List<Station> getShortestPath(Station from, Station to, List<Station> nodes, List<EdgeRoute> edges){
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

			LOG.debug("Dijkstra: proceed on node " + u.getName() + "(" + u.getId() + ")");
			
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

}
