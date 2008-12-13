package org.abratuhi.mmorpg.graph;

import java.util.ArrayList;

import org.abratuhi.mmorpg.util.ArrayUtil;

public class Graph {
	
	String id = new String();
	
	boolean isDigraph = false;
	
	ArrayList<Node> nodes = new ArrayList<Node>();
	ArrayList<Edge> edges = new ArrayList<Edge>();
	
	public Graph(){
		
	}
	
	public Graph(String id){
		this.id = id;
	}
	
	public Graph(int n, int m){
		this.nodes.ensureCapacity(n);
		this.edges.ensureCapacity(m);
	}
	
	public Graph(String id, int n, int m){
		this.id = id;
		this.nodes.ensureCapacity(n);
		this.edges.ensureCapacity(m);
	}
	
	public boolean isDigraph(){
		return this.isDigraph;
	}
	
	public void setIsDigraph(boolean isDigraph){
		this.isDigraph = isDigraph;
	}
	
	public void addNode(Node node){
		nodes.add(node);
	}
	
	public void addEdge(Edge edge){
		if(!nodes.contains(edge.getS())) return;
		if(!nodes.contains(edge.getT())) return;
		edges.add(edge);
	}
	
	Node[] getNodesAsArray(){
		return nodes.toArray(new Node[nodes.size()]);
	}
	
	public NodePath getShortestPathDijkstra(Node n1, Node n2){
		//Dijkstra
		
		Integer size = nodes.size();
		double[] d = new double[size];
		int[] pi= new int[size];
		ArrayList<Node> s = new ArrayList<Node>();
		ArrayList<Node> q = new ArrayList<Node>();
		
		for(int i=0; i<size; i++) d[i] = Double.MAX_VALUE;
		for(int i=0; i<size; i++) pi[i]= -1;
		d[nodes.indexOf(n1)] = 0.0;
		q.addAll(nodes);
		
		while(q.size() > 0){
			//System.out.println("dijkstra, graph = "+toString(false));
			//System.out.println("dijkstra, q.size = "+q.size());
			
			int indexOfMin = -1;
			double valueOfMin = Double.MAX_VALUE;
			for(int i=0; i<d.length; i++){
				if(q.contains(nodes.get(i)) && d[i] < valueOfMin){
					valueOfMin = d[i];
					indexOfMin = i;
				}
			}
			
			Node nodeOfMin = nodes.get(indexOfMin);
			s.add(q.remove(q.indexOf(nodeOfMin)));
			
			ArrayList<Edge> edgesToNeighs = nodeOfMin.getEdges();
			for(int i=0; i<edgesToNeighs.size(); i++){
				Edge edgeToNeigh = edgesToNeighs.get(i);
				Node neigh = edgeToNeigh.getT();
				
				if(nodeOfMin.equals(neigh) && !isDigraph) neigh = edgeToNeigh.getS();
				
				//System.out.println("dijkstra, correct("+nodeOfMin.id+", "+neigh.id+")");
				
				int indexOfNeigh = nodes.indexOf(neigh);
				if(q.contains(neigh)){
					if(d[indexOfNeigh] > d[indexOfMin] + edgeToNeigh.getCost()) {
						d[indexOfNeigh] = d[indexOfMin] + edgeToNeigh.getCost();
						pi[indexOfNeigh] = indexOfMin;
					}
				}
			}
			
			//System.out.println("dijkstra, d = "+ArrayUtil.toString(d));
			//System.out.println("dijkstra, pi = "+ArrayUtil.toString(pi));
		}
		
		NodePath shortPath = new NodePath();
		int indexOfn2 = nodes.indexOf(n2);
		int indexOfCurrent = indexOfn2;
		while(indexOfCurrent != -1){
			shortPath.addAtBegin(nodes.get(indexOfCurrent));
			indexOfCurrent = pi[indexOfCurrent];
		}
		
		return shortPath;
	}
	
	public NodePath getShortestPathAStar(Node n1, Node n2, float approximation){
		//Nicholson ~ Dijkstra(n1) + Dijkstra(n2) until intersection
		
		Integer size = nodes.size();
		
		double[] d1 = new double[size];
		int[] pi1 = new int[size];
		ArrayList<Node> s1 = new ArrayList<Node>();
		ArrayList<Node> q1 = new ArrayList<Node>();
		for(int i=0; i<size; i++) d1[i] = Double.MAX_VALUE;
		for(int i=0; i<size; i++) pi1[i]= -1;
		d1[nodes.indexOf(n1)] = 0.0;
		q1.addAll(nodes);
		
		double[] d2 = new double[size];
		int[] pi2 = new int[size];
		ArrayList<Node> s2 = new ArrayList<Node>();
		ArrayList<Node> q2 = new ArrayList<Node>();
		for(int i=0; i<size; i++) d2[i] = Double.MAX_VALUE;
		for(int i=0; i<size; i++) pi2[i]= -1;
		d2[nodes.indexOf(n2)] = 0.0;
		q2.addAll(nodes);
		
		boolean intersectionFound = false;
		Node n12 = null; // intersection node
		int step = 0;
		
		while((q1.size() > 0 || q2.size() > 0) && !intersectionFound){
			step ++;
			System.out.println("a-star, step = "+step);
			// dijkstra(n1)
			// find least distanced neighbour
			int indexOfMin1 = -1;
			double valueOfMin1 = Double.MAX_VALUE;
			for(int i=0; i<d1.length; i++){
				if(q1.contains(nodes.get(i)) && d1[i] < valueOfMin1){
					valueOfMin1 = d1[i];
					indexOfMin1 = i;
				}
			}
			
			Node nodeOfMin1 = nodes.get(indexOfMin1);
			s1.add(q1.remove(q1.indexOf(nodeOfMin1)));
			
			// correct(u, least distanced neighbour)
			ArrayList<Edge> edgesToNeighs1 = nodeOfMin1.getEdges();
			for(int i=0; i<edgesToNeighs1.size(); i++){
				Edge edgeToNeigh = edgesToNeighs1.get(i);
				Node neigh = edgeToNeigh.getT();
				
				if(nodeOfMin1.equals(neigh) && !isDigraph) neigh = edgeToNeigh.getS();
				System.out.println("a-star, correct1("+nodeOfMin1.id+", "+neigh.id+")");
				
				int indexOfNeigh = nodes.indexOf(neigh);
				if(q1.contains(neigh)){
					if(d1[indexOfNeigh] > d1[indexOfMin1] + edgeToNeigh.getCost()) {
						d1[indexOfNeigh] = d1[indexOfMin1] + edgeToNeigh.getCost();
						pi1[indexOfNeigh] = indexOfMin1;
					}
				}
			}
			
			// intersection check
			if(s2.contains(nodeOfMin1) && (d1[indexOfMin1] + d2[indexOfMin1] < approximation * n1.distanceTo(n2))){
				intersectionFound = true;
				if(n12 == null) n12 = nodeOfMin1;
			}
			
			System.out.println("a-star, d_1 = "+ArrayUtil.toString(d1));
			System.out.println("a-star, pi_1 = "+ArrayUtil.toString(pi1));
			
			// dijkstra(n2)
			// find least distanced neighbour
			int indexOfMin2 = -1;
			double valueOfMin2 = Double.MAX_VALUE;
			for(int i=0; i<d2.length; i++){
				if(q2.contains(nodes.get(i)) && d2[i] < valueOfMin2){
					valueOfMin2 = d2[i];
					indexOfMin2 = i;
				}
			}
			
			Node nodeOfMin2 = nodes.get(indexOfMin2);
			s2.add(q2.remove(q2.indexOf(nodeOfMin2)));
			
			// correct(u, least distanced neighbour)
			ArrayList<Edge> edgesToNeighs2 = nodeOfMin2.getEdges();
			for(int i=0; i<edgesToNeighs2.size(); i++){
				Edge edgeToNeigh = edgesToNeighs2.get(i);
				Node neigh = edgeToNeigh.getT();
				
				if(nodeOfMin2.equals(neigh) && !isDigraph) neigh = edgeToNeigh.getS();
				System.out.println("a-star, correct2("+nodeOfMin2.id+", "+neigh.id+")");
				
				int indexOfNeigh = nodes.indexOf(neigh);
				if(q2.contains(neigh)){
					if(d2[indexOfNeigh] > d2[indexOfMin2] + edgeToNeigh.getCost()) {
						d2[indexOfNeigh] = d2[indexOfMin2] + edgeToNeigh.getCost();
						pi2[indexOfNeigh] = indexOfMin2;
					}
				}
			}
			
			// intersection check
			if(s1.contains(nodeOfMin2) && (d1[indexOfMin2] + d2[indexOfMin2] < approximation * n1.distanceTo(n2))){
				intersectionFound = true;
				if(n12 == null) n12 = nodeOfMin2;
			}
			
			System.out.println("a-star, d_2 = "+ArrayUtil.toString(d2));
			System.out.println("a-star, pi_2 = "+ArrayUtil.toString(pi2));
		}
		
		NodePath shortPath = new NodePath();
		int indexOfn12 = nodes.indexOf(n2);
		
		int indexOfCurrent = indexOfn12;
		while(indexOfCurrent != -1){
			shortPath.addAtBegin(nodes.get(indexOfCurrent));
			indexOfCurrent = pi1[indexOfCurrent];
		}
		
		indexOfCurrent = pi2[indexOfn12]; // not adding intersection node twice
		while(indexOfCurrent != -1){
			shortPath.addAtEnd(nodes.get(indexOfCurrent));
			indexOfCurrent = pi2[indexOfCurrent];
		}
		
		return shortPath;
	}
	
	public String toString(){
		String out = new String();
		for(int i=0; i<nodes.size(); i++){
			out += nodes.get(i).id + " -> ";
			ArrayList<Node> neighs = isDigraph ? nodes.get(i).getOutNeighbours() : nodes.get(i).getAllNeighbours();
			for(int j=0; j<neighs.size(); j++)
				out += neighs.get(j).id + ", ";
			out += "\n";
		}
		return out;
	}

}
