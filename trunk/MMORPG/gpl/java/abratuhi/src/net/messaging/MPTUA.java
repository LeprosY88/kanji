package gpl.java.abratuhi.src.net.messaging;

import gpl.java.abratuhi.src.model.MMORPG_Hero;

import java.util.ArrayList;

public class MPTUA extends Thread{
	public final static boolean MPTUA_CHECK_RANGE = false;
	
	/**/
	public ArrayList<S_Client> clients = new ArrayList<S_Client>();
	public ArrayList<Message> incoming = new ArrayList<Message>();
	/**/
	public boolean isUp = false;
	
	/**/
	public MPTUA(){
		isUp = true;
		System.out.println("MPTUA is UP.");
	}
	public MPTUA(ArrayList<S_Client> cls, ArrayList<Message> msgs){
		this.isUp = true;
		this.clients = cls;
		this.incoming = msgs;
		System.out.println("MPTUA is UP.");
	}
	
	/**/
	public void run(){
		while(isUp){
			if(incoming.size()>0){
				Message msg = incoming.remove(0);
				//System.out.println("MPTUA status:\thas something to resend:\t"+msg.Message2String());
				for (int i=0; i<clients.size(); i++){
					if(MPTUA_CHECK_RANGE){ // if not broadcasting
						S_Client sc = findS_Client(msg.getFromId());
						if(sc!=null && sc.pos!=null && clients.get(i).pos!=null && sc.pos.distance(clients.get(i).pos)<MMORPG_Hero.range){ //check range
							if(clients.get(i).runOK){ //in range -> send message to client
								clients.get(i).sendMessage(msg);
							}							
						}
						else{ //else -> proceed to next client
							System.out.println("MPTUA(S_Client Side): MAX range exceeded - not transmitting message from "+msg.getFromId()+" to "+clients.get(i).idClient+"; "+sc.pos.distance(clients.get(i).pos)+" instead of "+MMORPG_Hero.range);;
							continue;
						}
					}
					else{ // else broadcasting
						if(clients.get(i).runOK){
							clients.get(i).sendMessage(msg);
						}
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
		this.isUp = false;
	}
	
	/*public void close(){
		
	}*/
	
	private S_Client findS_Client(String name){
		for (int i=0; i<clients.size(); i++){			
			if(clients.get(i).idClient.equals(name)){
				return clients.get(i);
			}
		}
		return null;
	}

}
