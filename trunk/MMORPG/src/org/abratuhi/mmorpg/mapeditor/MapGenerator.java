package org.abratuhi.mmorpg.mapeditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class MapGenerator {
	
	public int mapxsize;
	public int mapysize;
	public int ntree;
	public int npine;
	public int nwater;
	public int qtilesize;
	
	public String mapdir = "maps\\";
	public String mapfile;
	
	public MapGenerator(int mapwidth, int mapheigth, int ntrees, int npines, int nwaters, int quadrat, String out){
		this.mapxsize = mapwidth;
		this.mapysize = mapheigth;
		this.ntree = ntrees;
		this.npine = npines;
		this.nwater = nwaters;
		this.qtilesize = quadrat;
		this.mapfile = out;
	}
	
	public void generate(){
		// init xml document
		Document doc = new Document(new Element("mmorpg_map"));
		Element docroot = doc.getRootElement();
		
		// generate trees
		for(int i=0; i<ntree; i++){
			int x = (int) (Math.random() * mapxsize);
			int y = (int) (Math.random() * mapysize);
			Element ctree = new Element("terrain")
								.setAttribute("type", "tree")
								.setAttribute("x", String.valueOf(x))
								.setAttribute("y", String.valueOf(y));
			docroot.addContent(ctree);
		}
		
		// generate pines
		for(int i=0; i<npine; i++){
			int x = (int) (Math.random() * mapxsize);
			int y = (int) (Math.random() * mapysize);
			Element ctree = new Element("terrain")
								.setAttribute("type", "pine")
								.setAttribute("x", String.valueOf(x))
								.setAttribute("y", String.valueOf(y));
			docroot.addContent(ctree);
		}
		
		// generate water
		for(int i=0; i<nwater; i++){
			int x = (int) (Math.random() * mapxsize);
			int y = (int) (Math.random() * mapysize);
			Element ctree = new Element("terrain")
								.setAttribute("type", "water")
								.setAttribute("x", String.valueOf(x))
								.setAttribute("y", String.valueOf(y));
			docroot.addContent(ctree);
		}
		
		// write xml document to file
		XMLOutputter xmloutputter = new XMLOutputter();
		xmloutputter.setFormat(Format.getPrettyFormat());
		try {
			xmloutputter.output(doc, new FileOutputStream(new File(mapdir+mapfile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		MapGenerator mapgenerator = new MapGenerator(1000, 1000, 50, 50, 50, 30, "map.xml");
		mapgenerator.generate();
	}

}
