package andrei.bratuhin.model;

import java.util.List;
import java.util.Vector;

import org.abratuhi.bahnde.model.Station;

public class StationProviderStub {
	
	public static List<Station> getStations(){
		Station station1 = new Station();
		station1.setId(1);
		station1.setName("Bochum");
		station1.setCoordinates("0;0");
				
		Station station2 = new Station();
		station2.setId(2);
		station2.setName("Essen");
		station2.setCoordinates("-19;2");
		
		Station station3 = new Station();
		station3.setId(3);
		station3.setName("MuelheimRuhr");
		station3.setCoordinates("-25;-4");
		
		Station station4 = new Station();
		station4.setId(4);
		station4.setName("Duisburg");
		station4.setCoordinates("-33;-5");
		
		Station station5 = new Station();
		station5.setId(5);
		station5.setName("Oberhausen");
		station5.setCoordinates("-28;9");
		
		Station station6 = new Station();
		station6.setId(6);
		station6.setName("Bottrop");
		station6.setCoordinates("-23;16");
		
		Station station7 = new Station();
		station7.setId(7);
		station7.setName("Gelsenkirchen");
		station7.setCoordinates("-11;-9");
		
		Station station8 = new Station();
		station8.setId(8);
		station8.setName("Hattingen");
		station8.setCoordinates("1;-11");
		
		Station station9 = new Station();
		station9.setId(9);
		station9.setName("Witten");
		station9.setCoordinates("8;-2");
		
		Station station10 = new Station();
		station10.setId(10);
		station10.setName("Dortmund");
		station10.setCoordinates("17;6");
		
		Station station11 = new Station();
		station11.setId(10);
		station11.setName("Unna");
		station11.setCoordinates("30;6");
		
		Station station12 = new Station();
		station12.setId(12);
		station12.setName("Herne");
		station12.setCoordinates("1;9");
		
		Station station13 = new Station();
		station13.setId(13);
		station13.setName("Schwerte");
		station13.setCoordinates("26;2");
		
		Station station14 = new Station();
		station14.setId(14);
		station14.setName("Hagen");
		station14.setCoordinates("21;-11");
		
		station1.addIncidentStation(station2);
		station2.addIncidentStation(station1);
		
		List<Station> result = new Vector<Station>();
		result.add(station1);
		result.add(station2);
		return result;
	}

}
