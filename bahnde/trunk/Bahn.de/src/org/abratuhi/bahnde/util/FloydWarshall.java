package org.abratuhi.bahnde.util;

import java.util.List;
import java.util.Vector;

import org.abratuhi.bahnde.model.RouteEdge;
import org.abratuhi.bahnde.model.Station;
import org.abratuhi.bahnde.output.Route;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;

public class FloydWarshall extends Algorithm{
	private final static Logger LOG = Logger.getLogger(Dijkstra.class);

	@Override
	public Route getShortestPath(Station from, Station to,
			List<Station> nodes, MultiKeyMap edges) {
		
		
		MultiKeyMap path = new MultiKeyMap();
		MultiKeyMap next = new MultiKeyMap();
		
		for(Station station1 : nodes){
			for(Station station2 : nodes){
				RouteEdge edge = getLightestKnownEdge(station1, station2, edges);
				Integer dist = edge.getCost();
				path.put(station1, station2, dist);
				
				//if(station1.getIncidentStations().contains(station2)){
					next.put(station1, station2, null);
				//}
			}
		}
		
		for(Station station1 : nodes){
			for(Station station2 : nodes){
				for(Station station3 : nodes){
					Integer dist1 = (Integer)path.get(station2, station1);
					Integer dist2 = (Integer)path.get(station1, station3);
					Integer dist = (Integer)path.get(station2, station3);
					
					if(dist1 < Integer.MAX_VALUE && dist2 < Integer.MAX_VALUE && dist1 + dist2 < dist){
						path.put(station2, station3, dist1 + dist2);
						next.put(station2, station3, station1);
					}
				}
			}
		}
		
		/*for(Station station1 : nodes){
			for(Station station2 : nodes){
				Integer dist = (Integer)path.get(station1, station2);
				LOG.debug("dist[" + station1.getId() + "][" + station2.getId() + "] = " + dist);
			}
		}*/
		
		List<Station> result = getPathFromNext(from, to, next);
		
		
		return new Route(result, null); //FIXME
	}
	
	
	private List<Station> getPathFromNext(Station from, Station to, MultiKeyMap next){

		List<Station> result = new Vector<Station>();

		if(from == null || to == null){
			//if(from == null) LOG.debug("from == null && to="+to.getId());
			//if(to == null) LOG.debug("to == null && from="+from.getId());
			result.add(from == null ? to : from);
		}
		else{
			Station intermediate = (Station) next.get(from, to);
			if(intermediate == null){
				//if(intermediate == null) LOG.debug("intermediate == null && from="+from.getId() + " && to="+to.getId());
				result.add(0, to);
				result.add(0, from);
			}
			else{
				//LOG.debug("intermediate="+intermediate.getId()+" && from="+from.getId() + " && to="+to.getId());
				List<Station> left = getPathFromNext(from, intermediate, next);
				List<Station> right = getPathFromNext(intermediate, to, next);
				result.addAll(0, right);
				result.add(0, intermediate);
				result.addAll(0, left);
			}
		}
		
		List<Station> resultWoDups = new Vector<Station>();
		for (int i=result.size()-1; i>=0; i--){
			if(!resultWoDups.contains(result.get(i))){
				resultWoDups.add(0, result.get(i));
			}
		}
		
		return resultWoDups;
	}
	
	
}
