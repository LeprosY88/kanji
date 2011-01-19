package andrei.bratuhin.model;

import java.util.List;
import java.util.Vector;

import org.abratuhi.bahnde.model.EdgeRoute;
import org.abratuhi.bahnde.model.Station;

public class EdgeRouteProviderStub {
	
	public static List<EdgeRoute> getEdges(){
		List<EdgeRoute> result = new Vector<EdgeRoute>();
		
		List<Station> stations = StationProviderStub.getStations();
		
		int counter = 0;
		for(Station station : stations){
			for(Station neighbour : station.getIncidentStations()){
				EdgeRoute edge = new EdgeRoute();
				edge.setId(counter++);
				edge.setDepartureStation(station);
				edge.setArrivalStation(neighbour);
				edge.setDuration(station.getId() + neighbour.getId());
				result.add(edge);
			}
		}
		
		return result;
	}

}
