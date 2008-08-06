package org.abratuhi.mmorpg.model;

import org.jdom.Element;

public class MMORPG_Terrain {
	/**/
	//public final static String[] types = {"plain", "forest", "moor", "water", "wall", "snow", "sand"};
	
	/**/
	public String type;
	//int hp;
	//int height;
	public int x;
	public int y;
	//int qsize;
	
	public MMORPG_Terrain(String type, int x, int y){
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
	public static MMORPG_Terrain fromElement(Element terrainElement){
		String terraintType = terrainElement.getAttributeValue("type");
		int posX = Integer.parseInt(terrainElement.getAttributeValue("x"));
		int posY = Integer.parseInt(terrainElement.getAttributeValue("y"));
		return new MMORPG_Terrain(terraintType, posX, posY);
	}
	
	public Element toElement(){
		Element celement = new Element("terrain");
		celement.setAttribute("type", type);
		celement.setAttribute("x", String.valueOf(x));
		celement.setAttribute("y", String.valueOf(y));
		return celement;
	}

}
