package org.abratuhi.mmorpg.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * @author Alexei Bratuhin
 *
 */
public class MMORPG_Map {
	/** Map width **/
	public final static int XSIZE = 1000; // pixelsize
	/** Map height **/
	public final static int YSIZE = 1000; // pixelsize
	/** Map width between orientation meridians **/
	public final static int XSTEP = 200;
	/** Map height between orientation parallels **/
	public final static int YSTEP = 200;
	
	/** List of terrain 'units' **/
	public ArrayList<MMORPG_Terrain> terrains = new ArrayList<MMORPG_Terrain>();
	
	public MMORPG_Map(){
	}
	
	/**
	 * Store map as XML Document
	 * @param resource	path to file
	 */
	public void saveMap(String resource){
		// init xml document
		Document doc = new Document(new Element("mmorpg_map"));
		Element docroot = doc.getRootElement();
		
		//
		for (int i=0; i<terrains.size(); i++){
			docroot.addContent(terrains.get(i).toElement());
		}
		
		// write xml document to file
		XMLOutputter xmloutputter = new XMLOutputter();
		xmloutputter.setFormat(Format.getPrettyFormat());
		try {
			xmloutputter.output(doc, new FileOutputStream(new File(resource)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Load map from XML Document
	 * @param resource	path to file, storing XML Document of map
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static MMORPG_Map loadMap(String resource){
		// initialize map
		MMORPG_Map cmap = new MMORPG_Map();
		
		// load xml document
		SAXBuilder saxbuilder = new SAXBuilder();
		Document doc = null;
		try {
			doc = saxbuilder.build(new File(resource));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// parse terrain elements and add them to map
		List<Element> terrainElements = doc.getRootElement().getChildren("terrain");
		for(int i=0; i<terrainElements.size(); i++){
			MMORPG_Terrain cterrain = MMORPG_Terrain.fromElement(terrainElements.get(i));
			cmap.terrains.add(cterrain);
		}
		
		//
		return cmap;
	}
	
	public Point convert(Point p){
		double phi_x = Math.PI/6;
		double phi_y = Math.PI/3;
		Point pout = new Point();
		Point px = new Point((int)(p.x*Math.cos(phi_x)), (int)(-p.x*Math.sin(phi_x)));
		Point py = new Point((int)(p.y*Math.sin(phi_y)), (int)(p.y*Math.cos(phi_y)));
		pout.x = px.x + py.x;
		pout.y = px.y + py.y;
		System.out.println("POINT CONVERT:\t"+"("+p.x+","+p.y+")"+" -> "+"("+pout.x+","+pout.y+")");
		return pout;
	}
	
	public Point deconvert(Point p){
		double phi_x = Math.PI/6;
		double phi_y = Math.PI/3;
		Point pout = new Point();
		double c = Math.cos(phi_x)*Math.cos(phi_y)+Math.sin(phi_x)*Math.sin(phi_y);
		pout.x = (int)((Math.cos(phi_y)*p.x-Math.sin(phi_x)*p.y)/c);
		pout.y = (int)((Math.sin(phi_y)*p.x+Math.cos(phi_x)*p.y)/c);
		System.out.println("POINT DECONVERT:\t"+"("+p.x+","+p.y+")"+" -> "+"("+pout.x+","+pout.y+")");
		return pout;
	}

}
