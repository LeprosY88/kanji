package gpl.java.abratuhi.src.model;

import gpl.java.abratuhi.src.ads.MMORPG_Ad;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class MMORPG_Map {
	public final static int XSIZE = 10000; // pixelsize
	public final static int YSIZE = 10000; // pixelsize
	
	public final static int NADS = 10;
	
	private ArrayList<MMORPG_Ad> ads = new ArrayList<MMORPG_Ad>();
	
	public MMORPG_Map(){
		addRandomAds();
	}
	
	public void addRandomAds(){
		for(int i=0; i<NADS; i++){
			int x = (int)(Math.random()*XSIZE);
			int y = (int)(Math.random()*YSIZE);
			int dx = (int)(Math.random()*(XSIZE-x)%MMORPG_Ad.MAX_WIDTH);
			int dy = (int)(Math.random()*(YSIZE-y)%MMORPG_Ad.MAX_HEIGHT);
			ads.add(new MMORPG_Ad(new Rectangle(x, y, dx, dy)));
		}
	}
	
	public void draw(Graphics2D g){
		g.setColor(Color.WHITE);
		g.fill(new Rectangle(0,0,XSIZE,YSIZE));
		for(int i=0; i<ads.size(); i++){
			ads.get(i).draw(g);
		}
	}
	
	public void draw(Graphics2D g, Rectangle re, Point m){
		Rectangle r1= new Rectangle();
		Rectangle r2= new Rectangle();
		Rectangle r3= new Rectangle();
		Rectangle r4= new Rectangle();
		//if(re.height/re.width > 1/ratio) r = new Rectangle(new Point(re.x, (int) (re.y+(re.height-re.width/ratio)/2)), new Dimension(re.width, (int) (re.width/ratio)));
		//else r = new Rectangle(new Point((int) (re.x+(re.width-re.height*ratio)/2), re.y), new Dimension((int) (re.height*ratio), re.height));
		if(re.width/2-m.x > 0) r1 = new Rectangle(re.x, re.y, re.width/2-m.x, re.height);
		if(re.height/2-m.y > 0) r2 = new Rectangle(re.x, re.y, re.width, re.height/2-m.y);
		if(re.width/2-(XSIZE-m.x) > 0) r3 = new Rectangle(re.x+re.width/2+(XSIZE-m.x), re.y, re.width/2-(XSIZE-m.x), re.height);
		if(re.height/2-(YSIZE-m.y) > 0) r4 = new Rectangle(re.x, re.y+re.height/2+(YSIZE-m.y), re.width, re.height/2-(YSIZE-m.y));
		
		g.setColor(Color.GRAY);
		g.fill(re);
		
		g.setColor(Color.GREEN);
		g.fill(r1);
		g.fill(r2);
		g.fill(r3);
		g.fill(r4);
		
		g.setColor(Color.BLACK);
		
		//for(int i=0; i<ads.size(); i++){
		//	ads.get(i).draw(g);
		//}
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
