package org.abratuhi.mmorpg.graph;

import org.abratuhi.mmorpg.coordinates.Coordinate;

public class GraphTest {
	
	public static void main(String[] args){
		Graph g = new Graph();
		Node n1 = new Node("1", new Coordinate(2, new int[]{1, 1}));
		Node n2 = new Node("2", new Coordinate(2, new int[]{1, 2}));
		Node n3 = new Node("3", new Coordinate(2, new int[]{2, 3}));
		Node n4 = new Node("4", new Coordinate(2, new int[]{2, 1}));
		Node n5 = new Node("5", new Coordinate(2, new int[]{3, 2}));
		Node n6 = new Node("6", new Coordinate(2, new int[]{2, 3}));
		
		/*new Edge(n1, n2, 1);
		new Edge(n1, n4, 10);
		new Edge(n2, n3, 1);
		new Edge(n2, n4, 10);
		new Edge(n2, n6, 10);
		new Edge(n3, n4, 1);
		new Edge(n3, n5, 10);
		new Edge(n3, n6, 10);
		new Edge(n4, n5, 1);
		new Edge(n5, n6, 1);*/
		
		new Edge(n1, n2, 1);
		new Edge(n1, n4, 1);
		new Edge(n2, n3, 1);
		new Edge(n2, n4, 1);
		new Edge(n2, n6, 1);
		new Edge(n3, n4, 1);
		new Edge(n3, n5, 1);
		new Edge(n3, n6, 1);
		new Edge(n4, n5, 1);
		new Edge(n5, n6, 1);
		
		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);
		g.addNode(n4);
		g.addNode(n5);
		g.addNode(n6);
		
		//NodePath np = g.getShortestPathDijkstra(n1, n6);
		NodePath np = g.getShortestPathAStar(n1, n6, 1.5f);
		System.out.println(np.toString());
	}

}
