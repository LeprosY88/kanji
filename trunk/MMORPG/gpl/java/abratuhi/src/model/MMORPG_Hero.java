package gpl.java.abratuhi.src.model;

import java.awt.Point;

public class MMORPG_Hero extends MMORPG_Unit{
	
	public Point to = new Point();
	public double speed = 10;
	public final static int range = 150;
	
	public MMORPG_Hero(){
		super();
	}
	public MMORPG_Hero(String name){
		super();
		this.name = name;
	}
	
	public int distance(int x, int y){
		return (int) p.distance(new Point(x,y));
	}

}
