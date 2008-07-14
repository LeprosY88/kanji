package org.abratuhi.mmorpg.client;

import java.awt.Point;
import java.util.ArrayList;

import org.abratuhi.mmorpg.model.MMORPG_Hero;
import org.abratuhi.mmorpg.net.messaging.C_Client;
import org.abratuhi.mmorpg.net.messaging.Message;
import org.abratuhi.mmorpg.util.MessageUtil;

public class MMORPG_Client extends Thread{
	
	public static long DELAY = 250l;
	
	private boolean runOK = false;
	
	public MMORPG_Hero hero = new MMORPG_Hero();
	public ArrayList<MMORPG_Hero> neighbours = new ArrayList<MMORPG_Hero>();
	
	ArrayList<Message> chatIncoming = new ArrayList<Message>();
	ArrayList<Message> chatOutgoing = new ArrayList<Message>();
	
	public C_Client client = new C_Client(hero);
	String host = "localhost";
	String port = "9000";
	
	
	public MMORPG_Client(String heroname){
		hero.name = heroname;
		client.connect(host, Integer.parseInt(port));
		client.start();
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
	
	public void move(int dx, int dy){
		hero.to.x += dx;
		hero.to.y += dy;
	}
	
	public void say(String msg, String[] ids){
		chatOutgoing.add(MessageUtil.createChatMessage(this, msg, ids));
	}
	
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
					client.sendMessage(chatOutgoing.remove(0));
				}
			}
			
			//send coordinates to other
			client.sendMessage(MessageUtil.createSendPositionMessage(this));
			
			//proceed received messages from other
			if(client.msg_incoming.size()>0){
				for(int i=0; i<client.msg_incoming.size(); i++){
					Message m = client.msg_incoming.get(i);
					
					if(MessageUtil.getType(m).equals("game")){
						m = client.msg_incoming.remove(i);
						
						boolean known = false;
						String from = MessageUtil.getFromId(m);
						Point coor = MessageUtil.getFromPosition(m);
						
						if(from.equals(client.hero.name)) continue; //if echo message, throw message away
						
						
						System.out.println(hero.name+": "+"distance to "+from+" = "+hero.distance(coor.x, coor.y));
						
						// if hero left my range, remove him from neighbours list, set flag known
						if(hero.distance(coor.x, coor.y) > hero.range){
							for(int j=0; j<neighbours.size(); j++){
								if(neighbours.get(j).name.equals(from)){
									known = true;
									neighbours.remove(j);
									System.out.println("MMORPG_Main(C_Client Side): MAX range exceeded - not transmitting message from "+from+" to "+client.hero.name);
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
							System.out.println(this.hero.name+"got new neighbour:\t"+from+", "+coor.x+","+coor.y);
							neighbours.add(new MMORPG_Hero(from));						
							neighbours.get(neighbours.size()-1).p.x = coor.x;
							neighbours.get(neighbours.size()-1).p.y = coor.y;
							// proceed to next message
							continue;
						}
						
					}
					if(MessageUtil.getType(m).equals("chat")){
						chatIncoming.add(m);
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

}
