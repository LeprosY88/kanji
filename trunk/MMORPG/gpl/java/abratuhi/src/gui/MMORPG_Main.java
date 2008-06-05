package gpl.java.abratuhi.src.gui;

import gpl.java.abratuhi.src.graphics.MMORPG_GraphicsEngine;
import gpl.java.abratuhi.src.model.MMORPG_Hero;
import gpl.java.abratuhi.src.model.MMORPG_Map;
import gpl.java.abratuhi.src.net.messaging.C_Client;
import gpl.java.abratuhi.src.net.messaging.Message;

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

public class MMORPG_Main extends JPanel implements MouseListener, Runnable{
	
	public final static boolean CLIENT_CHECK_RANGE = true;
	public final static long DELAY = 250l;
	
	C_Client client = new C_Client();
	MMORPG_GraphicsEngine ge = new MMORPG_GraphicsEngine();
	
	MMORPG_Map map = new MMORPG_Map();
	MMORPG_Hero hero = new MMORPG_Hero();
	ArrayList<MMORPG_Hero> heroes = new ArrayList<MMORPG_Hero>();
	
	MMORPG_Main_sub sub = new MMORPG_Main_sub();
	
	boolean runOK = false;
	
	public MMORPG_Main(String name){
		super(new BorderLayout());
		//setSize(800, 600);
		setVisible(true);

		runOK = true;
		hero.name =name;
		client.idClient = hero.name;
		client.connect("localhost", 9000);
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
			ge.drawMap(g, map, this.getBounds(), hero.p);
			//draw myself
			ge.drawHero(g, this, hero, this.getBounds(), hero.p);
			//draw others
			for(int i=0; i<heroes.size(); i++){
				//heroes.get(i).draw(g, this);
				ge.drawUnit(g, this, heroes.get(i), this.getBounds(), hero.p);
			}

		}
	}
	
	public void run() {
		System.out.println("running game");
		while(runOK){
			//move
			if (hero.p.x != hero.to.x) hero.p.x += hero.speed * (hero.to.x-hero.p.x)/Math.abs(hero.to.x-hero.p.x);				
			if (hero.p.y != hero.to.y) hero.p.y += hero.speed * (hero.to.y-hero.p.y)/Math.abs(hero.to.y-hero.p.y);
			if (Math.abs(hero.p.x - hero.to.x) < hero.speed) hero.p.x = hero.to.x;
			if (Math.abs(hero.p.y - hero.to.y) < hero.speed) hero.p.y = hero.to.y;
			//send coordinates to other
			//if(hero.x!=hero.tox || hero.y!=hero.toy) client.sendMessage(new Message(new String[]{hero.name, Message.MSG_TO_ALL, Message.MSG_MOVE, hero.x+","+hero.y}));
			//client.sendMessage(new Message(new String[]{hero.name, Message.MSG_TO_ALL, Message.MSG_POS, hero.x+","+hero.y}));
			client.sendMessage(new Message("position", "", hero.name, "broadcast", "", hero.p));
			//proceed received coordinates from other
			if(client.msg_incoming.size()>0){
				for(int i=0; i<client.msg_incoming.size(); i++){
					Message m = client.msg_incoming.get(i);
					if(m.getType().equals("position")){
						m = client.msg_incoming.remove(i);
						boolean known = false;
						String from = m.getFromId();
						Point coor = m.getPosition();
						if(from.equals(client.idClient)) continue; //if echo message MSG_MOVE, throw message away
						if(CLIENT_CHECK_RANGE){ //check range attribute
							System.out.println(hero.name+": "+"distance to "+from+" = "+hero.distance(coor.x, coor.y));
							if(hero.distance(coor.x, coor.y) > hero.range){ //if outranged, throw message away
								for(int j=0; j<heroes.size(); j++){
									if(heroes.get(j).name.equals(from)){
										known = true;
										heroes.remove(j); //hero left my range, remove him from visible/drawable list
										System.out.println("MMORPG_Main(C_Client Side): MAX range exceeded - not transmitting message from "+from+" to "+client.idClient);
										break;
									}
								}
								continue; //proceed to next message
							}
							for(int k=0; k<heroes.size(); k++){ //remove all heroes that became unvisible/undrawble because of own movement
								if(heroes.get(k).distance(hero.p.x, hero.p.y) > hero.range){
									heroes.remove(k);
									k--;
								}
							}
						}
						if(!known){
							for(int j=0; j<heroes.size(); j++){
								if(heroes.get(j).name.equals(from)){
									known = true;
									heroes.get(j).p.x = coor.x;
									heroes.get(j).p.y = coor.y;
									break;
								}
							}
						}
						if(!known){
							System.out.println(from+", "+coor.x+","+coor.y);
							heroes.add(new MMORPG_Hero(from));						
							heroes.get(heroes.size()-1).p.x = coor.x;
							heroes.get(heroes.size()-1).p.y = coor.y;
						}
						//break;
						continue;
					}
					else{
						continue;
					}
				}
			}
			//repaint
			repaint();
			try {
				Thread.sleep(DELAY); // 1/40s * (4fps pro move); at median 25-40 fps
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void mouseClicked(MouseEvent me){
		//mouse_clicked = me.getPoint();
		hero.to.x = hero.p.x + me.getX() - getWidth()/2;
		hero.to.y = hero.p.y + me.getY() - getHeight()/2;
		hero.to.x = Math.min(hero.to.x, map.XSIZE);
		hero.to.x = Math.max(hero.to.x, 0);
		hero.to.y = Math.min(hero.to.y, map.YSIZE);
		hero.to.y = Math.max(hero.to.y, 0);
		//System.out.println("new waypoint");;
		//System.out.println("MMORPG_Main:: ("+hero.x+", "+hero.y+") -> ("+hero.tox+", "+hero.toy+")");
		repaint();
	}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}

}
