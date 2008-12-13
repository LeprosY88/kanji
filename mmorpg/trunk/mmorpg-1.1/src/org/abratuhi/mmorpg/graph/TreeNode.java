package org.abratuhi.mmorpg.graph;

import java.util.ArrayList;

public class TreeNode extends Node{
	
	Edge parentEdge = null;
	Edge previousSiblingEdge = null;
	Edge nextSiblingEdge = null;
	ArrayList<Edge> childrenEdges = new ArrayList<Edge>();
	
	public TreeNode(){
		super();
	}
	
	public TreeNode(String id){
		super(id);
	}
	
	
	public void setParent(TreeNode parentTreenode){
		parentEdge = new Edge(this, parentTreenode);
	}
	
	public void setPrevious(TreeNode prevTreenode){
		previousSiblingEdge = new Edge(this, prevTreenode);
	}
	
	public void setNext(TreeNode nextTreenode){
		previousSiblingEdge = new Edge(this, nextTreenode);
	}
	
	public void addChild(TreeNode childTreenode){
		this.addEdge(new Edge(this, childTreenode));
	}
	
	public TreeNode getParent(){
		return (TreeNode) this.parentEdge.getS();
	}
	
	public Edge getParentEdge(){
		return this.parentEdge;
	}
	
	public TreeNode getPrevious(){
		return (TreeNode) this.previousSiblingEdge.getS();
	}
	public Edge getPreviousEdge(){
		return this.previousSiblingEdge;
	}
	
	public ArrayList<TreeNode> getChildren(){
		ArrayList<TreeNode> children = new ArrayList<TreeNode>();
		for(int i=0; i<childrenEdges.size(); i++) children.add((TreeNode) childrenEdges.get(i).getT());
		return children;
	}
	public ArrayList<Edge> getChildrenEdges(){
		return this.childrenEdges;
	}

}
