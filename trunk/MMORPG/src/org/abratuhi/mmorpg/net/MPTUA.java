package org.abratuhi.mmorpg.net;

import java.util.ArrayList;

import org.abratuhi.mmorpg.util.MessageUtil;

/**
 * MPTUA (Message Processing/Transporting Unit/Agent) MMORPG class.
 * Used for delivering and forwarding messages from one client to (an)other(s).
 * @author Alexei Bratuhin
 *
 */
public class MPTUA extends Thread{
	/** Reference to list of clients, connected to server **/
	public ArrayList<S_Client> clients = new ArrayList<S_Client>();
	/** Reference to list of messages sent overall by clients **/
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
	
	/** Main functioning loop: get message from list, check recepients, forward message, proceed next message from list **/
	public void run(){
		while(getIsUp()){
			if(incoming.size()>0){
				Message msg = incoming.remove(0);
				if(MessageUtil.getForwarding(msg)>0) MessageUtil.decrementForwardingNumber(msg);
				
				String[] ids = MessageUtil.getToIds(msg);
				
				if(ids == null){
					System.out.println("MPTUA:\tProceeding of message failed due to bad message syntax (bad 'to' ids )");
					System.out.println(msg.toString());
					continue;
				}
				
				if(ids.length == 0){	// broadcast
					for(int i=0; i<clients.size(); i++){
						clients.get(i).sendMessage(msg);
					}
				}
				if(ids.length == 1){	// unicast
					S_Client cS_Client = findS_Client(ids[0]);
					if(cS_Client == null){
						System.out.println("MPTUA:\tProceeding of message failed due to bad message syntax (couldn't find client with given name)");
						System.out.println(msg.toString());
						continue;
					}
					cS_Client.sendMessage(msg);
				}
				if(ids.length >= 2){	// multicast
					for(int i=0; i<ids.length; i++){
						S_Client cS_Client = findS_Client(ids[i]);
						if(cS_Client == null){
							System.out.println("MPTUA:\tProceeding of message failed due to bad message syntax (couldn't find client with given name)");
							System.out.println(msg.toString());
							continue;
						}
						cS_Client.sendMessage(msg);
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
	
	/** 
	 * Find server side client using id.
	 */
	public S_Client findS_Client(String name){
		for (int i=0; i<clients.size(); i++){			
			if(clients.get(i).id.equals(name)){
				return clients.get(i);
			}
		}
		System.out.println("MPTUA:\tError - couldn't find client named <"+name+">");
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
