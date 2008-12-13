package org.abratuhi.mmorpg.graph;


public class Digraph extends Graph{
	
	public Digraph(){
		super();
		setIsDigraph(true);
	}
	
	public Digraph(String id){
		super(id);
		setIsDigraph(true);
	}
	
	public Digraph(int n, int m){
		super(n, m);
		setIsDigraph(true);
	}
	
	public Digraph(String id, int n, int m){
		super(id, n, m);
		setIsDigraph(true);
	}

}
