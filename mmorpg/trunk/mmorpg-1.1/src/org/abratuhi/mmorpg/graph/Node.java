package org.abratuhi.mmorpg.graph;

import java.util.ArrayList;

import org.abratuhi.mmorpg.coordinates.Coordinate;

public class Node {
	
	String id = new String();
	Coordinate coordinate;
	ArrayList<Edge> edges = new ArrayList<Edge>();
	
	public Node(){
		
	}
	
	public Node(String id){
		this.id = id;
	}
	
	public Node(Coordinate c){
		this.coordinate = c;
	}
	
	public Node(String id, Coordinate c){
		this.id = id;
		this.coordinate = c;
	}
	
	
	public void addEdge(Edge edge){
		edges.add(edge);
	}
	
	public void addNeighbour(Node node){
		edges.add(new Edge(this, node));
	}
	
	public ArrayList<Edge> getEdges(){
		return this.edges;
	}
	
	public Coordinate getCoordinate(){
		return this.coordinate;
	}
	
	public ArrayList<Node> getAllNeighbours(){
		ArrayList<Node> neighbours = new ArrayList<Node>();
		for(int i=0; i<edges.size(); i++) neighbours.add(edges.get(i).getS().equals(this) ? edges.get(i).getT() : edges.get(i).getS());
		return neighbours;
	}
	
	public ArrayList<Node> getInNeighbours(){
		ArrayList<Node> neighbours = new ArrayList<Node>();
		for(int i=0; i<edges.size(); i++)
			if (edges.get(i).getT().equals(this))
					neighbours.add(edges.get(i).getS());
		return neighbours;
	}
	
	public ArrayList<Node> getOutNeighbours(){
		ArrayList<Node> neighbours = new ArrayList<Node>();
		for(int i=0; i<edges.size(); i++)
			if (edges.get(i).getS().equals(this))
					neighbours.add(edges.get(i).getT());
		return neighbours;
	}
	
	public int distanceTo(Node n){
		return coordinate.getChebyshevDistanceTo(n.getCoordinate());
	}

}
