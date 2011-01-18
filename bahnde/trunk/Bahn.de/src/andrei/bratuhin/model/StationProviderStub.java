package andrei.bratuhin.model;

import java.util.List;
import java.util.Vector;

import org.abratuhi.bahnde.model.Station;

public class StationProviderStub {
	
	public static List<Station> getStations(){
		Station station1 = new Station();
		station1.setId(1);
		station1.setName("Essen");
		
		Station station2 = new Station();
		station2.setId(2);
		station2.setName("DŸsseldorf");
		
		station1.addIncidentStation(station2);
		station2.addIncidentStation(station1);
		
		List<Station> result = new Vector<Station>();
		result.add(station1);
		result.add(station2);
		return result;
	}

}
