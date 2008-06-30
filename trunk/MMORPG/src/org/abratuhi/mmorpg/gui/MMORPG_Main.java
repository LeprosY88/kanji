package org.abratuhi.mmorpg.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.abratuhi.mmorpg.graphics.MMORPG_GraphicsEngine;
import org.abratuhi.mmorpg.model.MMORPG_Client;
import org.abratuhi.mmorpg.model.MMORPG_Map;

public class MMORPG_Main extends JPanel implements MouseListener{
	
	MMORPG_GraphicsEngine ge = new MMORPG_GraphicsEngine();
	
	MMORPG_Map map = new MMORPG_Map();
	
	MMORPG_Client client;
	
	MMORPG_Main_sub sub = new MMORPG_Main_sub();
	
	public MMORPG_Main(String name){
		super(new BorderLayout());
		//setSize(800, 600);
		setVisible(true);

		client = new MMORPG_Client(name);
		client.start();
		
		//sub.setSize(new Dimension(MMORPG_Map.XSIZE, MMORPG_Map.YSIZE));
		sub.setPreferredSize(new Dimension(800,600));
		sub.setDoubleBuffered(true);
		sub.setBackground(Color.white);
		sub.addMouseListener(this);
		
		JScrollPane scroller = new JScrollPane(sub);
		//scroller.setPreferredSize(new Dimension(map.XSIZE, map.YSIZE));
		//scroller.setPreferredSize(new Dimension(800, 600));
		add(scroller, BorderLayout.CENTER);
		
		addMouseListener(this);
	}
	
	public class MMORPG_Main_sub extends JPanel{
		public void paintComponent(Graphics gg){
			super.paintComponents(gg);
			//
			Graphics2D g = (Graphics2D) gg;
			//clear
			g.clearRect(0, 0, getWidth(), getHeight());
			//draw map
			ge.drawMap(g, map, this.getBounds(), client.hero.p);
			//draw myself
			ge.drawHero(g, this, client.hero, this.getBounds(), client.hero.p);
			//draw others
			for(int i=0; i<client.neighbours.size(); i++){
				//heroes.get(i).draw(g, this);
				ge.drawUnit(g, this, client.neighbours.get(i), this.getBounds(), client.hero.p);
			}

		}
	}

	public void mouseClicked(MouseEvent me){
		client.hero.to.x = client.hero.p.x + me.getX() - getWidth()/2;
		client.hero.to.y = client.hero.p.y + me.getY() - getHeight()/2;
		client.hero.to.x = Math.min(client.hero.to.x, map.XSIZE);
		client.hero.to.x = Math.max(client.hero.to.x, 0);
		client.hero.to.y = Math.min(client.hero.to.y, map.YSIZE);
		client.hero.to.y = Math.max(client.hero.to.y, 0);
		repaint();
	}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}

}
