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

import org.abratuhi.mmorpg.game.MMORPG_Game;
import org.abratuhi.mmorpg.graphics.MMORPG_GraphicsEngine;
import org.abratuhi.mmorpg.client.MMORPG_Client;
import org.abratuhi.mmorpg.model.MMORPG_Map;

/**
 * GUI part responsible for presenting the play field.
 * 
 * @author Alexei Bratuhin
 *
 */
public class MMORPG_Main extends JPanel implements MouseListener{
	MMORPG_GUI gui;
		
	MMORPG_Main_sub sub = new MMORPG_Main_sub();
	
	public MMORPG_Main(MMORPG_GUI gui){
		super(new BorderLayout());
		//setSize(800, 600);
		setVisible(true);
		
		this.gui = gui;

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
	
	
	/**
	 * Subclass representing used for drawing of the play field.
	 * 
	 * @author Alexei Bratuhin
	 *
	 */
	public class MMORPG_Main_sub extends JPanel{
		public void paintComponent(Graphics gg){
			super.paintComponents(gg);
			//
			Graphics2D g = (Graphics2D) gg;
			//clear
			g.clearRect(0, 0, getWidth(), getHeight());
			//draw map
			gui.game.ge.drawMap(g, this, gui.game.client.map, this.getBounds(), gui.game.client.hero.p);
			//draw myself
			gui.game.ge.drawHero(g, this, gui.game.client.hero, this.getBounds(), gui.game.client.hero.p);
			//draw others
			for(int i=0; i<gui.game.client.neighbours.size(); i++){
				//heroes.get(i).draw(g, this);
				gui.game.ge.drawUnit(g, this, gui.game.client.neighbours.get(i), this.getBounds(), gui.game.client.hero.p);
			}

		}
	}

	public void mouseClicked(MouseEvent me){
		Integer dx = me.getX() - getWidth()/2;
		Integer dy = me.getY() - getHeight()/2;
		
		gui.game.client.move(dx, dy);
		
		repaint();
	}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}

}
