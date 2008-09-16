package org.abratuhi.mmorpg.model;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

/**
 * MMORPG Unit class.
 * Used to store information about each unit on map.
 * Used as parent class for further better-(closer-)specified unit types.
 * 
 * @author Alexei Bratuhin
 *
 */

public class MMORPG_Unit {
	/**/
	//public static final String[] types = {"infantry", "archer", "knight"};
	
	/**/
	public String name;
	//String type; /**/
	/** current hit points **/
	int hp;
	/** current mana points **/
	int mp;
	/** maximal hit points **/
	double maxhp;
	//int range; /*hitting distance of weapon*/
	/** current experience points **/
	double exp;
	/** current level **/
	int level; /*level*/
	//int strength; /**/
	//int perception; /**/
	//int endurance; /**/
	//int charisma; /**/
	//int intelligence; /**/
	//int agility; /**/
	//int luck; /**/
	//double velocity; /**/
	//String army; /*to which army belongs*/
	/** attack rating (chance to hit opponent)**/
	double ar; /*chance to hit*/
	/** defense rating (chance not to be hit by opponent)**/
	double dr; /*chance to be hit*/
	/** current position **/
	public Point p = new Point();
	
	//public String img_src = "img\\hero_small.png";
	//public Image img = Toolkit.getDefaultToolkit().getImage(img_src);
	//public Dimension img_dim = new Dimension();
	
	public MMORPG_Unit(){
		
	}

}
