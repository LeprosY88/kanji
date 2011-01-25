package org.abratuhi.bahnde.model;

import java.util.List;

import junit.framework.TestCase;

import org.abratuhi.bahnde.db.DbDataGetter;
import org.abratuhi.bahnde.output.Route;
import org.abratuhi.bahnde.util.Dijkstra;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.BasicConfigurator;

public class DijkstraDbTest extends TestCase {
	
	static{
		BasicConfigurator.configure();
	}
	
	public void testGetShortestPath_1_2(){
		List<Station> stations = DbDataGetter.getStations();
		MultiKeyMap edges = DbDataGetter.getRouteEdgesAsMKMap(null);
		Station from = stations.get(0);
		Station to = stations.get(1);
		
		Route route = new Dijkstra().getShortestPath(from, to, stations, edges);
		List<Station> result = route.getStations();
		
		assertEquals(2, result.size());
		assertEquals(from, result.get(0));
		assertEquals(to, result.get(1));
	}
	
	public void testGetShortestPath_4_1(){
		List<Station> stations = DbDataGetter.getStations();
		MultiKeyMap edges = DbDataGetter.getRouteEdgesAsMKMap(null);
		Station from = stations.get(4-1);
		Station to = stations.get(1-1);
		
		Route route = new Dijkstra().getShortestPath(from, to, stations, edges);
		List<Station> result = route.getStations();
		
		assertEquals(4, result.size());
		assertEquals(from, result.get(0));
		assertEquals(stations.get(3-1), result.get(1));
		assertEquals(stations.get(2-1), result.get(2));
		assertEquals(to, result.get(3));
	}
	
	public void testGetShortestPath_6_14(){
		List<Station> stations = DbDataGetter.getStations();
		MultiKeyMap edges = DbDataGetter.getRouteEdgesAsMKMap(null);
		Station from = stations.get(6-1);
		Station to = stations.get(14-1);
		
		Route route = new Dijkstra().getShortestPath(from, to, stations, edges);
		List<Station> result = route.getStations();
		
		assertEquals(5, result.size());
		assertEquals(from, result.get(0));
		assertEquals(stations.get(5-1), result.get(1));
		assertEquals(stations.get(2-1), result.get(2));
		assertEquals(stations.get(8-1), result.get(3));
		assertEquals(to, result.get(4));
	}
}
