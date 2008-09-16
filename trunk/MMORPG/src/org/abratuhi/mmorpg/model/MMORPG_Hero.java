package org.abratuhi.mmorpg.model;

import java.awt.Point;

/**
 * MMORPG Hero Class.
 * Used for storing the avatar/character data.
 * @author Alexei Bratuhin
 *
 */
public class MMORPG_Hero extends MMORPG_Unit{
	
	/** Destination point if moving **/
	public Point to = new Point();
	/** Speed of moving (px/step) **/
	public double speed = 10;
	/** Sight range (px) **/
	public final static int range = 150;
	
	public String messageChat = new String();
	
	public MMORPG_Hero(){
		super();
	}
	public MMORPG_Hero(String name){
		super();
		this.name = name;
	}
	
	/**
	 * Shortcut for standard Java function
	 * @param x		x coordinate
	 * @param y		y coordinate
	 * @return		distance from current position to given coordinates
	 */
	public int distance(int x, int y){
		return (int) p.distance(new Point(x,y));
	}

}
