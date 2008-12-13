package org.abratuhi.mmorpg.graph;

import java.util.ArrayList;

public class NodePath {
	
	ArrayList<Node> nodes = new ArrayList<Node>();
	
	public NodePath(){
		
	}
	
	public Node[] asArray(){
		return nodes.toArray(new Node[nodes.size()]);
	}
	
	public int length(){
		return nodes.size();
	}
	
	public void addAtEnd(Node n){
		nodes.add(n);
	}
	
	public void addAtBegin(Node n){
		nodes.add(0, n);
	}
	
	public void add(Node n, int index){
		nodes.add(index, n);
	}
	
	public void remove(Node n){
		nodes.remove(n);
	}
	
	public void remove(int index){
		nodes.remove(index);
	}
	
	public boolean contains(Node node){
		return nodes.contains(node);
	}
	
	public String toString(){
		String np = new String();
		int i=0;
		for(i=0; i<nodes.size()-1; i++){
			np += nodes.get(i).id+" -> ";
		}
		np += nodes.get(i).id;
		return np;
	}

}
