package org.abratuhi.mmorpg.model;

import org.jdom.Element;

/**
 * MMORPG Terrain class.
 * Each terrain has a certain type and is positioned due to its (x,y) coordinates of left upper corner of corresponding image tile.
 * @author Alexei Bratuhin
 *
 */
public class MMORPG_Terrain {
	/**/
	//public final static String[] types = {"plain", "forest", "moor", "water", "wall", "snow", "sand"};
	
	/** type of terrain**/
	public String type;
	//int hp;
	//int height;
	/** upper left corner - x coordinate **/
	public int x;
	/** upper left corner - y coordinate **/
	public int y;
	//int qsize;
	
	public MMORPG_Terrain(String type, int x, int y){
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
	/** Create object from org.jdom.Element from XML document of the map**/
	public static MMORPG_Terrain fromElement(Element terrainElement){
		String terraintType = terrainElement.getAttributeValue("type");
		int posX = Integer.parseInt(terrainElement.getAttributeValue("x"));
		int posY = Integer.parseInt(terrainElement.getAttributeValue("y"));
		return new MMORPG_Terrain(terraintType, posX, posY);
	}
	
	/** Convert to orj.jdom.Element for XML document of the map **/
	public Element toElement(){
		Element celement = new Element("terrain");
		celement.setAttribute("type", type);
		celement.setAttribute("x", String.valueOf(x));
		celement.setAttribute("y", String.valueOf(y));
		return celement;
	}

}
