package org.abratuhi.mmorpg.net.messaging;

import java.util.ArrayList;

import org.abratuhi.mmorpg.util.MessageUtil;

public class MPTUA extends Thread{
	/**/
	public ArrayList<S_Client> clients = new ArrayList<S_Client>();
	public ArrayList<Message> incoming = new ArrayList<Message>();
	/**/
	public boolean isUp = false;
	
	/**/
	public MPTUA(){
		setIsUp(true);
		System.out.println("MPTUA is UP.");
	}
	public MPTUA(ArrayList<S_Client> cls, ArrayList<Message> msgs){
		setIsUp(true);
		this.clients = cls;
		this.incoming = msgs;
		System.out.println("MPTUA is UP.");
	}
	
	/**/
	public void run(){
		while(getIsUp()){
			if(incoming.size()>0){
				Message msg = incoming.remove(0);
				String[] ids = MessageUtil.getToIds(msg);
				
				if(ids.length == 0){	// broadcast
					for(int i=0; i<clients.size(); i++){
						clients.get(i).sendMessage(msg);
					}
				}
				if(ids.length == 1){	// unicast
					findS_Client(ids[0]).sendMessage(msg);
				}
				if(ids.length >= 2){	// multicast
					for(int i=0; i<ids.length; i++){
						findS_Client(ids[i]).sendMessage(msg);
					}
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stopp(){
		setIsUp(false);
	}
	
	/*public void close(){
		
	}*/
	
	private S_Client findS_Client(String name){
		for (int i=0; i<clients.size(); i++){			
			if(clients.get(i).id.equals(name)){
				return clients.get(i);
			}
		}
		return null;
	}
	
	/**/

	
	public synchronized boolean getIsUp(){
		return this.isUp;
	}
	
	public synchronized void setIsUp(boolean run){
		this.isUp = run;
	}
	
	public synchronized void switchIsUp(){
		this.isUp = (this.isUp == true)? false:true;
	}

}
