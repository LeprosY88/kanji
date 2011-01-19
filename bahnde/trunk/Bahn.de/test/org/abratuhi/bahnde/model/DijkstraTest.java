package org.abratuhi.bahnde.model;

import java.util.List;

import org.abratuhi.bahnde.util.Dijkstra;

import junit.framework.TestCase;
import andrei.bratuhin.model.EdgeRouteProviderStub;
import andrei.bratuhin.model.StationProviderStub;

public class DijkstraTest extends TestCase {
	public void testGetShortestPath_1_2(){
		List<Station> stations = StationProviderStub.getStations();
		List<EdgeRoute> edges = EdgeRouteProviderStub.getEdges();
		Station from = stations.get(0);
		Station to = stations.get(1);
		
		List<Station> result = Dijkstra.getShortestPath(from, to, stations, edges);
		
		assertEquals(2, result.size());
		assertEquals(from, result.get(0));
		assertEquals(to, result.get(1));
	}
	
	public void testGetShortestPath_4_1(){
		List<Station> stations = StationProviderStub.getStations();
		List<EdgeRoute> edges = EdgeRouteProviderStub.getEdges();
		Station from = stations.get(4-1);
		Station to = stations.get(1-1);
		
		List<Station> result = Dijkstra.getShortestPath(from, to, stations, edges);
		
		assertEquals(4, result.size());
		assertEquals(from, result.get(0));
		assertEquals(stations.get(3-1), result.get(1));
		assertEquals(stations.get(2-1), result.get(2));
		assertEquals(to, result.get(3));
	}
	
	public void testGetShortestPath_6_14(){
		List<Station> stations = StationProviderStub.getStations();
		List<EdgeRoute> edges = EdgeRouteProviderStub.getEdges();
		Station from = stations.get(6-1);
		Station to = stations.get(14-1);
		
		List<Station> result = Dijkstra.getShortestPath(from, to, stations, edges);
		
		assertEquals(5, result.size());
		assertEquals(from, result.get(0));
		assertEquals(stations.get(5-1), result.get(1));
		assertEquals(stations.get(2-1), result.get(2));
		assertEquals(stations.get(8-1), result.get(3));
		assertEquals(to, result.get(4));
	}
}
