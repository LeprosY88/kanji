package andrei.bratuhin.model;

import java.util.Collections;
import java.util.List;

import org.abratuhi.bahnde.model.RouteEdge;
import org.abratuhi.bahnde.model.Station;
import org.apache.commons.collections.map.MultiKeyMap;

public class EdgeRouteProviderStub {
	
	public static MultiKeyMap getEdges(){
		MultiKeyMap result = new MultiKeyMap();
		
		List<Station> stations = StationProviderStub.getStations();
		
		int counter = 1;
		for(Station station : stations){
			for(Station neighbour : station.getIncidentStations()){
				if(station.equals(neighbour)){
					continue;
				}
				
				RouteEdge edge = new RouteEdge();
				edge.setId(counter++);
				edge.setDepartureStation(station);
				edge.setArrivalStation(neighbour);
				edge.setDuration(station.getId() + neighbour.getId());
				edge.setCost(station.getId() + neighbour.getId());
				
				result.put(station, neighbour, Collections.singletonList(edge));
			}
		}
		
		return result;
	}

}
