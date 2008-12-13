package org.abratuhi.mmorpg.client;

import java.awt.Point;
import java.util.ArrayList;

import org.abratuhi.mmorpg.game.MMORPG_Game;
import org.abratuhi.mmorpg.model.MMORPG_Hero;
import org.abratuhi.mmorpg.model.MMORPG_Map;
import org.abratuhi.mmorpg.net.C_Client;
import org.abratuhi.mmorpg.net.Message;
import org.abratuhi.mmorpg.util.MessageUtil;

/**
 * MMORPG Game Client.
 * Contains reference to parent wrapper - MMORPG_Game - to be able to access other parts of the game.
 * Works as follows: regulary (with DELAY delay) sends messages about own position and status to others, proceeds messages received, updates its neihgbourhood list using received messages as reference. 
 * Notice: currently loads default map 'maps/map.xml'.
 * @author Alexei Bratuhin
 *
 */
public class MMORPG_Client extends Thread{
	
	public static long DELAY = 250l;
	
	MMORPG_Game game;
	
	public MMORPG_Hero hero = new MMORPG_Hero();
	public ArrayList<MMORPG_Hero> neighbours = new ArrayList<MMORPG_Hero>();
	public MMORPG_Map map = MMORPG_Map.loadMap("maps/map.xml");	
	ArrayList<Message> chatIncoming = new ArrayList<Message>();
	ArrayList<Message> chatOutgoing = new ArrayList<Message>();
	
	public C_Client netclient;
	String host = "localhost";
	String port = "9000";
	
	private boolean runOK = false;
	
	
	public MMORPG_Client(MMORPG_Game game, String heroname){
		this.game = game;
		hero.name = heroname;
		netclient = new C_Client(this);
		netclient.connect(host, Integer.parseInt(port));
		netclient.start();
		setRunOK(true);
	}
	
	public synchronized boolean getRunOK(){
		return this.runOK;
	}
	public synchronized void setRunOK(boolean v){
		this.runOK = v;
	}
	
	public synchronized void switchRunOK(){
		this.runOK = (this.runOK == true)? false : true;
	}
	
	/**
	 * Move unit using specified offset
	 * @param dx	x offset
	 * @param dy	y offset
	 */
	public void move(int dx, int dy){
		hero.to.x = hero.p.x + dx;
		hero.to.y = hero.p.y + dy;
		
		hero.to.x = Math.min(hero.to.x, map.XSIZE);
		hero.to.x = Math.max(hero.to.x, 0);
		hero.to.y = Math.min(hero.to.y, map.YSIZE);
		hero.to.y = Math.max(hero.to.y, 0);
	}
	
	/*public void say(String msg, String[] ids){
		chatOutgoing.add(MessageUtil.createChatMessage(this, msg, ids));
	}*/
	
	public void run(){
		while(getRunOK()){
			//move
			if (hero.p.x != hero.to.x) hero.p.x += hero.speed * (hero.to.x-hero.p.x)/Math.abs(hero.to.x-hero.p.x);				
			if (hero.p.y != hero.to.y) hero.p.y += hero.speed * (hero.to.y-hero.p.y)/Math.abs(hero.to.y-hero.p.y);
			if (Math.abs(hero.p.x - hero.to.x) < hero.speed) hero.p.x = hero.to.x;
			if (Math.abs(hero.p.y - hero.to.y) < hero.speed) hero.p.y = hero.to.y;
			
			// send outgoing chat messages
			int outgoingSize = chatOutgoing.size();
			if(outgoingSize>0){
				for(int i=0; i<outgoingSize; i++){
					netclient.sendMessage(chatOutgoing.remove(0));
				}
			}
			
			//send coordinates to other
			netclient.sendMessage(MessageUtil.createSendPositionMessage(this));
			
			//proceed received messages from other
			if(netclient.msg_incoming.size()>0){
				for(int i=0; i<netclient.msg_incoming.size(); i++){
					Message m = netclient.msg_incoming.get(i);
					
					if(MessageUtil.getType(m).equals("game")){
						m = netclient.msg_incoming.remove(i);
						
						boolean known = false;
						String from = MessageUtil.getFromId(m);
						Point coor = MessageUtil.getFromPosition(m);
						
						if(from.equals(hero.name)) continue; //if echo message, throw message away
						
						// if hero left my range, remove him from neighbours list, set flag known
						if(hero.distance(coor.x, coor.y) > hero.range){
							for(int j=0; j<neighbours.size(); j++){
								if(neighbours.get(j).name.equals(from)){
									known = true;
									neighbours.remove(j);
									
									// proceed to next message
									continue;
								}
							}
						}
						
						// check whether can update hero's data that is in my range, meaning, check whether he is already in neighbours list
						if(!known){	// flag not set ~ message not proceeded yet
							for(int j=0; j<neighbours.size(); j++){
								if(neighbours.get(j).name.equals(from)){
									known = true;
									neighbours.get(j).p.x = coor.x;
									neighbours.get(j).p.y = coor.y;
									// proceed to next message
									continue;
								}
							}
						}
						
						// hero in my range is not in my neighbours' list - correct
						if(!known){	// flag not set ~ message not proceeded yet
							neighbours.add(new MMORPG_Hero(from));						
							neighbours.get(neighbours.size()-1).p.x = coor.x;
							neighbours.get(neighbours.size()-1).p.y = coor.y;
							// proceed to next message
							continue;
						}
						
					}
				}
			}
			try {
				Thread.sleep(DELAY); // 1/40s * (4fps pro move); at median 25-40 fps
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopp() {
		setRunOK(false);
		netclient.stopp();
	}

}
