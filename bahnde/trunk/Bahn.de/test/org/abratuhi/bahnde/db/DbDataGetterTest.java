package org.abratuhi.bahnde.db;

import java.util.List;

import junit.framework.TestCase;

import org.abratuhi.bahnde.model.RouteEdge;
import org.abratuhi.bahnde.model.Station;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.BasicConfigurator;

public class DbDataGetterTest extends TestCase {
	
	static{
		BasicConfigurator.configure();
	}
	
	public void testGetStations(){
		List<Station> stations = DbDataGetter.getStations();
		assertEquals(14, stations.size());
	}
	public void testGetStationOnId(){
		Station station = DbDataGetter.getStation(1);
		assertNotNull(station);
		assertEquals("Bochum", station.getName());
	}
	public void testGetStationOnName(){
		Station station = DbDataGetter.getStation("Bochum");
		assertNotNull(station);
		assertEquals(1, station.getId());
	}
	public void testGetStationsGetNeighbours(){
		List<Station> stations = DbDataGetter.getStations();
		assertEquals(14, stations.size());
		for(Station station : stations){
			for(Station neighbour : station.getIncidentStations()){
				assertNotSame(station, neighbour);
			}
		}
	}
	public void testGetRouteEdgesAsList(){
		List<RouteEdge> edges = DbDataGetter.getRouteEdges();
		assertNotNull(edges);
		assertEquals(4088, edges.size());
		
		for(RouteEdge edge : edges){
			assertTrue(edge.getId() > 0);
			assertTrue(edge.getCost() > 0);
			assertTrue(edge.getDepartureStation().getId() != edge.getArrivalStation().getId());
		}
	}
	public void testGetRouteEdgesAsMap(){
		List<Station> stations = DbDataGetter.getStations();
		MultiKeyMap map = DbDataGetter.getRouteEdgesAsMKMap();
		assertNotNull(map);
		
		assertNotNull(map.get(stations.get(1-1), stations.get(2-1)));
		assertNotNull(map.get(stations.get(2-1), stations.get(3-1)));
		assertNotNull(map.get(stations.get(3-1), stations.get(4-1)));
		assertNotNull(map.get(stations.get(4-1), stations.get(5-1)));
		assertNotNull(map.get(stations.get(5-1), stations.get(6-1)));
		assertNotNull(map.get(stations.get(6-1), stations.get(7-1)));
	}
}
