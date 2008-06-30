package org.abratuhi.mmorpg.model;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

/**
 * 
 * @author cOIOuHkc
 *
 */

public class MMORPG_Unit {
	/**/
	//public static final String[] types = {"infantry", "archer", "knight"};
	
	/**/
	public String name;
	//String type; /**/
	int hp; /*hit points*/
	int mp;
	double maxhp;
	//int range; /*hitting distance of weapon*/
	double exp;
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
	double ar; /*chance to hit*/
	double dr; /*chance to be hit*/
	public Point p = new Point();
	
	public String img_src = "img\\hero_small.png";
	public Image img = Toolkit.getDefaultToolkit().getImage(img_src);
	public Dimension img_dim = new Dimension();
	
	public MMORPG_Unit(){
		
	}

}
