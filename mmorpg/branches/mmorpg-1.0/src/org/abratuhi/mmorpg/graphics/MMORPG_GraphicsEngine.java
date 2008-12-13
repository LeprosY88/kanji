package org.abratuhi.mmorpg.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import org.abratuhi.mmorpg.game.MMORPG_Game;
import org.abratuhi.mmorpg.model.MMORPG_Hero;
import org.abratuhi.mmorpg.model.MMORPG_Map;
import org.abratuhi.mmorpg.model.MMORPG_Terrain;
import org.abratuhi.mmorpg.model.MMORPG_Unit;

/**
 * Graphics Engine.
 * Uses primitive 2D to present unit'S position on map.
 * Notice: contains link to parent wrapper - MMORPG_Game - to access other parts of the game. 
 * @author Alexei Bratuhin
 *
 */
public class MMORPG_GraphicsEngine {
	
	MMORPG_Game game;
	Properties geProperties = new Properties();
	HashMap<String, Image> type2image = new HashMap<String, Image>();
	
	public MMORPG_GraphicsEngine(){
		// load properties from ge.properties
		try {
	        geProperties.load(new FileInputStream("ge.properties"));
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    
	    // initialize type2image HashMap
	    type2image.put("pine", loadImage("ge.image.pine"));
	    type2image.put("tree", loadImage("ge.image.tree"));
	    type2image.put("water", loadImage("ge.image.water"));
	}
	
	public MMORPG_GraphicsEngine(MMORPG_Game game){
		// initialize connection with parent game
		this.game = game;
		
		// load properties from ge.properties
		try {
	        geProperties.load(new FileInputStream("ge.properties"));
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    
	    // initialize type2image HashMap
	    type2image.put("pine", loadImage("ge.image.pine"));
	    type2image.put("tree", loadImage("ge.image.tree"));
	    type2image.put("water", loadImage("ge.image.water"));
	}
	
	/**
	 * Help function to load image of specified type from filesystem. Each type corresponds to an image. Linking type<->imagefile is done in graphics engine's .properties file.
	 * Currently supported types: hero, pine, tree, water
	 * @param type	-	image type
	 * @return			image of specified type
	 */
	public Image loadImage(String type){
		return Toolkit.getDefaultToolkit().getImage(geProperties.getProperty(type));
	}
	
	/**
	 * Draw map in the to parent wrapper's MMORPG_Game attached MMORPG_GUI window
	 * @param g		graphics context
	 * @param obs	graphics observer
	 * @param map	map object
	 * @param r		client's screen rectangle
	 * @param p		client's position in MMORPG world
	 */
	@SuppressWarnings("static-access")
	public void drawMap(Graphics2D g, ImageObserver obs, MMORPG_Map map, Rectangle r, Point p){
		// compute middle point of r
		Point m = new Point(r.x+r.width/2, r.y+r.height/2);
		//System.out.println("GE:\tMiddle point computed:\t"+"("+m.x+","+m.y+")");
		
		// fill screen Rectangle with background color, not reffering to map
		g.setColor(Color.GRAY);
		g.fill(r);
		
		// compute rectangle of map relatively to standing point of hero
		Point screenmap_corner_ul = new Point();
		if(p.x-r.width/2 > 0) screenmap_corner_ul.x = r.x; else screenmap_corner_ul.x = r.x+(r.width/2-p.x);
		if(p.y-r.height/2 > 0) screenmap_corner_ul.y = r.y; else screenmap_corner_ul.y = r.y+(r.height/2-p.y);
		//System.out.println("GE:\tMap upper left point computed:\t"+"("+screenmap_corner_ul.x+","+screenmap_corner_ul.y+")");
		
		Dimension screenmap_dim_from_m = new Dimension();
		if(p.x+r.width/2 < map.XSIZE) screenmap_dim_from_m.width = r.width/2; else screenmap_dim_from_m.width = map.XSIZE-p.x;
		if(p.y+r.height/2 < map.YSIZE) screenmap_dim_from_m.height = r.height/2; else screenmap_dim_from_m.height = map.YSIZE-p.y;
		//System.out.println("GE:\tDimesion of map from middle point computed:\t"+screenmap_dim_from_m.width+"x"+screenmap_dim_from_m.height);
		
		Dimension screenmap_dim_from_ul = new Dimension();
		screenmap_dim_from_ul.width = m.x-screenmap_corner_ul.x+screenmap_dim_from_m.width;
		screenmap_dim_from_ul.height = m.y-screenmap_corner_ul.y+screenmap_dim_from_m.height;
		//System.out.println("GE:\tDimesion of map from upper left corner computed:\t"+screenmap_dim_from_ul.width+"x"+screenmap_dim_from_ul.height);
		
		Rectangle map_on_screen = new Rectangle(screenmap_corner_ul, screenmap_dim_from_ul);
		
		// fill rectangle of map with background map color
		g.setColor(Color.WHITE);
		g.fill(map_on_screen);
		
		// draw meridians and parallels of map with XSTEP and YSTEP respectively
		int nparallels = screenmap_dim_from_ul.height / map.XSTEP;
		int nmeridians = screenmap_dim_from_ul.width / map.YSTEP;
		
		int xoffset = screenmap_corner_ul.x + ((p.x-(m.x-screenmap_corner_ul.x))/map.XSTEP+1)*map.XSTEP-(p.x-m.x);
		int yoffset = screenmap_corner_ul.y + ((p.y-(m.y-screenmap_corner_ul.y))/map.YSTEP+1)*map.YSTEP-(p.y-m.y);
		if(screenmap_corner_ul.x != 0) xoffset = screenmap_corner_ul.x;
		if(screenmap_corner_ul.y != 0) yoffset = screenmap_corner_ul.y;

		g.setColor(Color.LIGHT_GRAY);
		
		for(int i=0; i<nmeridians+1; i++){
			g.drawLine(xoffset+i*map.XSTEP, map_on_screen.y, xoffset+i*map.XSTEP, map_on_screen.y+map_on_screen.height);
		}
		for(int j=0; j<nparallels+1; j++){
			g.drawLine(map_on_screen.x, yoffset+j*map.YSTEP, map_on_screen.x+map_on_screen.width, yoffset+j*map.YSTEP);
		}
		
		// draw terrains
		for(int k=0; k<map.terrains.size(); k++){
			MMORPG_Terrain cterrain = map.terrains.get(k);
			if(cterrain.x < p.x+r.width || cterrain.x > p.x-r.width ||
					cterrain.y < p.y+r.height || cterrain.y > p.y-r.height){
				g.drawImage(type2image.get(cterrain.type), cterrain.x-p.x+m.x, cterrain.y-p.y+m.y, obs);
			}
		}
		
	}
	
	/**
	 * Draw client's avatar unit in the to parent wrapper's MMORPG_Game attached MMORPG_GUI window
	 * @param g
	 * @param obs
	 * @param hero - hero to draw
	 * @param r	-	screen rectangle
	 * @param p - main hero position
	 */
	public void drawHero(Graphics2D g, ImageObserver obs, MMORPG_Hero hero, Rectangle r, Point p){
		Point m = new Point(r.x+r.width/2, r.y+r.height/2);
		Image heroImage = loadImage("ge.image.hero");
		Dimension heroImageDimension = new Dimension(heroImage.getWidth(obs), heroImage.getHeight(obs));
		Integer heroFontSize = Integer.parseInt(geProperties.getProperty("ge.font.size"));
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, heroFontSize));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g.drawImage(heroImage, m.x - heroImageDimension.width/2, m.y - heroImageDimension.height, obs);
		g.drawString(hero.name, m.x - heroImageDimension.width/2, m.y);
		
		g.setColor(Color.GREEN);
		g.drawLine(m.x, m.y, m.x+hero.to.x-hero.p.x, m.y+hero.to.y-hero.p.y);
		
		//System.out.println("GE:\tdrawing hero at ("+p.x+", "+p.y+")");
	}
	
	/**
	 * Draw unit in the to parent wrapper's MMORPG_Game attached MMORPG_GUI window
	 * @param g
	 * @param obs
	 * @param unit - unit to draw
	 * @param rect - screen rectangle
	 * @param p - main hero position
	 */
	public void drawUnit(Graphics2D g, ImageObserver obs, MMORPG_Unit unit, Rectangle rect, Point p){
		if(unit.p.distance(p) > MMORPG_Hero.range) return;
		
		Point m = new Point(rect.x+rect.width/2, rect.y+rect.height/2);
		Point u = unit.p;
		Point r = new Point(u.x-p.x+m.x, u.y-p.y+m.y);
		Image unitImage = loadImage("ge.image.hero");
		Dimension unitImageDimension = new Dimension(unitImage.getWidth(obs), unitImage.getHeight(obs));
		Integer unitFontSize = Integer.parseInt(geProperties.getProperty("ge.font.size"));
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, unitFontSize));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		g.drawImage(unitImage, r.x - unitImageDimension.width/2, r.y - unitImageDimension.height, obs);
		g.drawString(unit.name, r.x - unitImageDimension.width/2, r.y);
	}

}
