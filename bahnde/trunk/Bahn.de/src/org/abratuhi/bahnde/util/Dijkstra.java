package org.abratuhi.bahnde.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.abratuhi.bahnde.model.RouteEdge;
import org.abratuhi.bahnde.model.Station;
import org.abratuhi.bahnde.output.Route;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;

public class Dijkstra extends Algorithm {
	
	private final static Logger LOG = Logger.getLogger(Dijkstra.class);
	
	public Route getShortestPath(Station from, Station to, Date starting, List<Station> nodes, MultiKeyMap edges){
		List<Station> result = new Vector<Station>();
		List<RouteEdge> route = new Vector<RouteEdge>();
		
		List<Station> q = new Vector<Station>(nodes);
		Map<Station, Integer> dist = new HashMap<Station, Integer>();
		Map<Station, Station> previous = new HashMap<Station, Station>();
		Map<Station, Date> departures = new HashMap<Station, Date>();
		
		MultiKeyMap connections = new MultiKeyMap();
		
		for(Station node : q){
			dist.put(node, Integer.MAX_VALUE);
			previous.put(node, null);
			
//			Calendar calendar = Calendar.getInstance();
//			calendar.add(Calendar.YEAR, 1);
//			departures.put(node, calendar.getTime());
			departures.put(node, starting);
		}
		
		dist.put(from, 0);
		//previous.put(from, from);
		
		
		while(!q.isEmpty()){
			Station u = getClosest(q, dist);
			q.remove(u);

			LOG.debug("Dijkstra: proceed on node " + u.getName() + "(" + u.getId() + ")");
			
			List<Station> neighbours = u.getIncidentStations();
			for(Station neighbour : neighbours){
				RouteEdge edge = getLightestKnownEdge(u, neighbour, departures.get(u), edges);
				if(edge != null && edge.getCost() != Integer.MAX_VALUE){
					int alt = dist.get(u) + edge.getCost();
					if(alt < dist.get(neighbour)){
						dist.put(neighbour, alt);
						previous.put(neighbour, u);
						connections.put(u, neighbour, edge);
						
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(edge.getDeparture());
						calendar.add(Calendar.MINUTE, edge.getDuration());
						departures.put(neighbour, calendar.getTime());
						
						LOG.debug("Dijkstra: prev[" + neighbour.getId() + "]="+u.getId());
					}
				}
			}
			
			LOG.debug("Dijkstra: ------------");
			for(Station sttn : nodes){
				if(previous.get(sttn) != null){
					LOG.debug("Dijkstra: prev[" + sttn.getId() + "]="+previous.get(sttn).getId());
					LOG.debug("Dijkstra: dist[" + sttn.getId() + "]="+dist.get(sttn));
				}
			}
			LOG.debug("Dijkstra: ------------");
		}
		
		Station last = to;
		while(!last.equals(from)){
			LOG.debug("Dijkstra: backtrack on node " + last.getName() + "(" + last.getId() + ")");
			
			result.add(0, last);
			
			route.add(0, (RouteEdge) connections.get(previous.get(last), last));
			
			last = previous.get(last);
		}
		result.add(0, last); // ~ result.add(0, from);
		
		for(int i=0; i<route.size(); i++){
			RouteEdge edge = route.get(i);
			LOG.debug("Routing " + edge.getDepartureStation().getId() + " -> " + edge.getArrivalStation().getId());
		}
		
		
		return new Route(result ,route);
	}

}
