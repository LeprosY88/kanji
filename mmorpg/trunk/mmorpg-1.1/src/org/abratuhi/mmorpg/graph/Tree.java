package org.abratuhi.mmorpg.graph;

public class Tree extends Graph{
	
	TreeNode root;
	
	public Tree(){
		super();
	}
	
	public Tree(String id){
		super(id);
	}
	
	public Tree(TreeNode root){
		super();
		this.root = root;
	}
	
	public Tree(String id, TreeNode root){
		super(id);
		this.root = root;
	}
	
	public void add(TreeNode to, TreeNode what){
		super.addNode(what);
		super.addEdge(new Edge(to, what));
		to.getChildren().get(to.getChildren().size()-1).setNext(what);
		to.addChild(what);
	}

}
