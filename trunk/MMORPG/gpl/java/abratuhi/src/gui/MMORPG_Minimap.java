package gpl.java.abratuhi.src.gui;

import gpl.java.abratuhi.src.model.MMORPG_Hero;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MMORPG_Minimap extends JPanel implements Runnable{
	
	MMORPG_Hero hero;
	ArrayList<MMORPG_Hero> heroes;
	int r = 2;
	
	boolean runOK = false;
	
	public MMORPG_Minimap(MMORPG_Hero hero, ArrayList<MMORPG_Hero> heroes){
		super();
		this.runOK = true;
		this.hero = hero;
		this.heroes = heroes;
		setSize(new Dimension(150, 100));
		//setLocation(new Point(800, 100));
		setVisible(true);
		repaint();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Point p = this.getLocation();
		Dimension d = this.getSize();
		Dimension delta = new Dimension(20, 20);
		//Point c = new Point(p.x+d.width/2, p.y+d.height/2);
		g.setColor(Color.BLACK);
		//g.drawOval(0, 0, d.width, d.height);
		//System.out.println(String.valueOf(0+delta.width) +","+ String.valueOf(0+delta.height) +","+ String.valueOf(d.width-delta.width) +","+ String.valueOf(d.height-d.height));
		g.drawOval(0+delta.width, 0+delta.height, d.width-2*delta.width, d.height-2*delta.height);
		g.drawString("N", d.width/2 -2, delta.height);		
		//g.drawOval(0+20, 0+20, d.width-50, d.height-50);
		g.fillOval(d.width/2-r, d.height/2-r, 2*r, 2*r);
		for(int i=0; i<heroes.size(); i++){
			MMORPG_Hero h = heroes.get(i);
			if(h.range > (Math.sqrt(Math.pow(h.p.x-hero.p.x,2)+Math.pow(h.p.y-hero.p.y,2)))) continue;
			int xcoor = d.width/2 + (h.p.x-hero.p.x);//*d.width/2/hero.range;
			int ycoor = d.height/2 + (h.p.y-hero.p.y);//*d.height/2/hero.range;
			//System.out.println("drawing hero at: "+xcoor+", "+ycoor);
			g.fillOval(xcoor-r, ycoor-r, 2*r, 2*r);
		}
	}

	public void run() {
		System.out.println("running minimap");
		this.runOK = true;
		while(runOK){
			repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/*public static void main(String[] args){
		JFrame f = new JFrame(".::MMORPG_Chat");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(200, 200));
		f.setLocation(new Point(200, 100));
		f.setVisible(true);
		f.add(new MMORPG_Minimap());
		f.repaint();
	}*/
	
}
