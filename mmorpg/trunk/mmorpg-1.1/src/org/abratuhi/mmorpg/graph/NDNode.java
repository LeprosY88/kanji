package org.abratuhi.mmorpg.graph;

public class NDNode extends Node{
	
	int[] nBefore;
	int[] nAfter;
	int N;	//int dimension;
	NDNode[][] neighbours;
	
	public NDNode(){
		super();
	}
	
	public NDNode(String id){
		super(id);
	}
	
	public NDNode(int dimension){
		super();
		
		this.N = dimension;
		this.neighbours = new NDNode[this.N][2];
		this.nBefore = new int[this.N];
		this.nAfter = new int[this.N];
	}
	
	public NDNode(String id, int dimension){
		super(id);
		
		this.N = dimension;
		this.neighbours = new NDNode[this.N][2];
		this.nBefore = new int[this.N];
		this.nAfter = new int[this.N];
	}
}
