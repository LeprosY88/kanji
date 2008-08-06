package org.abratuhi.mmorpg.net;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.abratuhi.mmorpg.util.MessageUtil;

public class S_Client extends Thread{
	/**/
	public Server server;
	public Socket s;
	public boolean runOK = false;
	
	/**/
	public ArrayList<Message> msg_incoming = new ArrayList<Message>();
	
	/**/
	String id = new String();
	Point position = new Point();
	public ArrayList<String> neighbours = new ArrayList<String>();
	ArrayList<String> mstneighbours = new ArrayList<String>();
	
	
	/**/
	public S_Client(Server serv, Socket s, ArrayList<Message> incoming){
		this.server = serv;
		this.s = s;
		this.msg_incoming = incoming;
		setRunOK(true);
	}
	
	/**/
	public void sendMessage(Message msg){
		try {
			String fromId = MessageUtil.getFromId(msg);
			S_Client fromSClient = server.mptua.findS_Client(fromId);
			boolean fromFound = false;
			boolean isNeighbour = false;
			for(int i=0; i<neighbours.size(); i++){
				if(neighbours.get(i).equals(fromId)){
					fromFound = true;
					break;
				}
			}
			
			// check range
			isNeighbour = (distance(fromSClient) < Server.DIST) ? true : false;
			
			// send message
			new DataOutputStream(s.getOutputStream()).writeUTF(msg.toString());
			
			// all sender must be a neighbour, check this fact when sending message
			if(!fromFound && isNeighbour){
				neighbours.add(fromId);
			}
			else{
				neighbours.remove(fromId);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Message receiveMessage(){
		String strMsg = null;
		try {
			strMsg = new DataInputStream(s.getInputStream()).readUTF();
			if(strMsg != null && strMsg != ""){
				Message msg = Message.createMessage();
				msg.fromString(strMsg);
				return msg;
			}
			else{
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**/
	public void run(){
		while(getRunOK()){
			//
			Message msg = receiveMessage();
			
			String messageType = MessageUtil.getType(msg);
			String messageCast = MessageUtil.getToCast(msg);
			
			if(messageType==null || messageType=="" ||
					messageCast==null || messageCast==""){
				System.out.println("NET:\tS_Client found message, where messageType or messageCast don't have proper format.");
				System.out.println(msg.toString());
				continue;
			}
			
			if(messageCast.equals(MessageUtil.MSGCAST_NEIGHCAST)){
				if(neighbours.size() == 0){
					requestNeighbours();
				}
				msg = MessageUtil.neighcast2multicast(this, msg);
			}
			
			// initialize id of sclient, that is the id of corresponding MMORPG_Hero in cclient
			if(messageType.equals(MessageUtil.MSGTYPE_INIT_CLIENT)){
				updateIdFromMessage(msg);
				updatePositionFromMessage(msg);
				
				System.out.println("NET:\tS_Client was initialised, id="+this.id);
			}
			else{
				// remove sclient from list of server's sclients, since the corresponding cclient sent destroy message
				if(messageType.equals(MessageUtil.MSGTYPE_DSTR_CLIENT)){
					server.clients.remove(this);
					msg_incoming.add(msg);
					
					System.out.println("NET:\tS_Client was destroyed, id="+this.id);
				}
				else{
					updatePositionFromMessage(msg);
					msg_incoming.add(msg);
					
					System.out.println("NET:\tS_Client received message, id="+this.id);
				}
			}
			
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//return;
	}
	
	public void stopp(){
		setRunOK(false);
		this.stop();
		//System.out.println("Client was stopped.");
	}
	
	/**/
	
	public synchronized boolean getRunOK(){
		return this.runOK;
	}
	
	public synchronized void setRunOK(boolean run){
		this.runOK = run;
	}
	
	public synchronized void switchRunOK(){
		this.runOK = (this.runOK == true)? false:true;
	}
	
	
	/**/
	public void updateIdFromMessage(Message m){
		String identificator = MessageUtil.getFromId(m);
		if(id != null) this.id = identificator;
	}
	
	public void updatePositionFromMessage(Message m){
		Point pos = MessageUtil.getFromPosition(m);
		if (pos != null) this.position = pos;
	}
	
	/**/
	public void requestNeighbours(){
		server.buildNeighboursMST();
		neighbours = (ArrayList<String>) mstneighbours.clone();
	}
	
	public double distance(S_Client sc){
		return this.position.distance(sc.position);
	}
	
	/**/
	public void printSummary(){
		System.out.println("*************** Client Statistics *****************");
		//System.out.println("Client to:\t"+host+":"+host_port);
		if(runOK) System.out.println("Client is running:\ttrue");
		else System.out.println("Client is running:\tfalse");
		System.out.println("Client's # of incoming messages:\t"+msg_incoming.size());
		//System.out.println("Client's # of outcoming messages:\t"+msg_outcoming.size());
	}

}

