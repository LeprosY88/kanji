package gpl.java.abratuhi.src.graphics;

import gpl.java.abratuhi.src.model.MMORPG_Hero;
import gpl.java.abratuhi.src.model.MMORPG_Map;
import gpl.java.abratuhi.src.model.MMORPG_Unit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class MMORPG_GraphicsEngine {
	
	public MMORPG_GraphicsEngine(){
		
	}
	
	@SuppressWarnings("static-access")
	public void drawMap(Graphics2D g, MMORPG_Map map, Rectangle r, Point p){
		// compute middle point of r
		Point m = new Point(r.x+r.width/2, r.y+r.height/2);
		System.out.println("GE:\tMiddle point computed:\t"+"("+m.x+","+m.y+")");
		
		// fill screen Rectangle with background color, not reffering to map
		g.setColor(Color.GRAY);
		g.fill(r);
		
		// compute rectangle of map relatively to standing point of hero
		Point screenmap_corner_ul = new Point();
		if(p.x-r.width/2 > 0) screenmap_corner_ul.x = r.x; else screenmap_corner_ul.x = r.x+(r.width/2-p.x);
		if(p.y-r.height/2 > 0) screenmap_corner_ul.y = r.y; else screenmap_corner_ul.y = r.y+(r.height/2-p.y);
		System.out.println("GE:\tMap upper left point computed:\t"+"("+screenmap_corner_ul.x+","+screenmap_corner_ul.y+")");
		
		Dimension screenmap_dim_from_m = new Dimension();
		if(p.x+r.width/2 < map.XSIZE) screenmap_dim_from_m.width = r.width/2; else screenmap_dim_from_m.width = map.XSIZE-p.x;
		if(p.y+r.height/2 < map.YSIZE) screenmap_dim_from_m.height = r.height/2; else screenmap_dim_from_m.height = map.YSIZE-p.y;
		System.out.println("GE:\tDimesion of map from middle point computed:\t"+screenmap_dim_from_m.width+"x"+screenmap_dim_from_m.height);
		
		Dimension screenmap_dim_from_ul = new Dimension();
		screenmap_dim_from_ul.width = m.x-screenmap_corner_ul.x+screenmap_dim_from_m.width;
		screenmap_dim_from_ul.height = m.y-screenmap_corner_ul.y+screenmap_dim_from_m.height;
		System.out.println("GE:\tDimesion of map from upper left corner computed:\t"+screenmap_dim_from_ul.width+"x"+screenmap_dim_from_ul.height);
		
		Rectangle map_on_screen = new Rectangle(screenmap_corner_ul, screenmap_dim_from_ul);
		
		// fill rectangle of map with background map color
		g.setColor(Color.WHITE);
		g.fill(map_on_screen);
		
		// draw meridians and parallels of map with XSTEP and YSTEP respectively
		int nparallels = screenmap_dim_from_ul.height / map.XSTEP;
		int nmeridians = screenmap_dim_from_ul.width / map.YSTEP;		
		System.out.println("GE:\t#meridian = "+nmeridians+", #parallel = "+nparallels);
		
		int xoffset = screenmap_corner_ul.x + ((p.x-(m.x-screenmap_corner_ul.x))/map.XSTEP+1)*map.XSTEP-(p.x-m.x);
		int yoffset = screenmap_corner_ul.y + ((p.y-(m.y-screenmap_corner_ul.y))/map.YSTEP+1)*map.YSTEP-(p.y-m.y);
		if(screenmap_corner_ul.x != 0) xoffset = screenmap_corner_ul.x;
		if(screenmap_corner_ul.y != 0) yoffset = screenmap_corner_ul.y;
		System.out.println("GE:\tmeridian/parallel starting point:\t"+"("+xoffset+","+yoffset+")");

		g.setColor(Color.BLACK);
		
		for(int i=0; i<nmeridians+1; i++){
			g.drawLine(xoffset+i*map.XSTEP, map_on_screen.y, xoffset+i*map.XSTEP, map_on_screen.y+map_on_screen.height);
			System.out.println("GE:\tdrawLine:\t"+"("+(xoffset+i*map.XSTEP)+","+map_on_screen.y+")-("+(xoffset+i*map.XSTEP)+","+(map_on_screen.y+map_on_screen.height)+")");
		}
		for(int j=0; j<nparallels+1; j++){
			g.drawLine(map_on_screen.x, yoffset+j*map.YSTEP, map_on_screen.x+map_on_screen.width, yoffset+j*map.YSTEP);
			System.out.println("GE:\tdrawLine:\t"+"("+map_on_screen.x+","+(yoffset+j*map.YSTEP)+")-("+(map_on_screen.x+map_on_screen.width)+","+(yoffset+j*map.YSTEP)+")");
		}
		
	}
	
	public void drawHero(Graphics2D g, MMORPG_Hero hero, Rectangle r, Point p){
		
	}
	
	public void drawUnit(Graphics2D g, MMORPG_Unit unit, Rectangle r, Point p){
		
	}

}