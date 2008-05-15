package gpl.java.abratuhi.src.model;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;

public class MMORPG_Hero extends MMORPG_Unit{
	
	public String name = new String();
	//String passwd;
	public Point to = new Point();
	public double speed = 10;
	public final static int range = 150;
	
	public MMORPG_Hero(){
		super();
	}
	public MMORPG_Hero(String name){
		super();
		this.name = name;
	}
	
	public int distance(int x, int y){
		return (int) p.distance(new Point(x,y));
	}
	
	public void draw(Graphics2D g, ImageObserver obs, Point p){
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 12));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		img_dim = new Dimension(img.getWidth(obs), img.getHeight(obs));
		//g.drawImage(img, x - img_dim.width/2, y - img_dim.height/2, obs);
		//g.drawOval(x-range, y-range,range*2, range*2);
		//g.drawString(name, x - img_dim.width/2, y + img_dim.height/2);
		//g.drawImage(img, p.x - img_dim.width/2, p.y - img_dim.height, obs);
		g.drawImage(img, p.x - img_dim.width/2, p.y - img_dim.height, obs);
		//g.drawOval(p.x-range, p.y-range,range*2, range*2);
		g.drawString(name, p.x - img_dim.width/2, p.y);
	}

}
