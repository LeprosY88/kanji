package org.abratuhi.bahnde.model;

import java.util.List;

import org.abratuhi.bahnde.util.Dijkstra;

import junit.framework.TestCase;
import andrei.bratuhin.model.EdgeRouteProviderStub;
import andrei.bratuhin.model.StationProviderStub;

public class DijkstraTest extends TestCase {
	public void testGetShortestPath(){
		List<Station> stations = StationProviderStub.getStations();
		List<EdgeRoute> edges = EdgeRouteProviderStub.getEdges();
		Station from = stations.get(0);
		Station to = stations.get(1);
		
		List<Station> result = Dijkstra.getShortestPath(from, to, stations, edges);
	}
}
