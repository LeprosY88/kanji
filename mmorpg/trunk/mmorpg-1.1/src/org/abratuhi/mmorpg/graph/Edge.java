package org.abratuhi.mmorpg.graph;

public class Edge {
	
	String id = new String();
	
	Node s;
	Node t;
	
	double cost;
	
	double capacity;
	double minflow;
	double flow;
	
	public Edge(){
		
	}
	
	public Edge(String id){
		this.id = id;
	}
	
	public Edge(Node startNode, Node endNode){
		this.s = startNode;
		this.t = endNode;
		startNode.addEdge(this);
		endNode.addEdge(this);
	}
	
	public Edge(Node startNode, Node endNode, double cost){
		this.s = startNode;
		this.t = endNode;
		startNode.addEdge(this);
		endNode.addEdge(this);
		this.cost = cost;
	}
	
	public Node getS(){
		return this.s;
	}
	
	public Node getT(){
		return this.t;
	}
	
	public double getCost(){
		return this.cost;
	}

}
