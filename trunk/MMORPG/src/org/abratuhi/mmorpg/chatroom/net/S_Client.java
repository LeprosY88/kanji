package org.abratuhi.mmorpg.chatroom.net;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.abratuhi.mmorpg.chatroom.model.ChatRoom;


public class S_Client implements Runnable{
	/**/
	public Server server;
	public Socket clientSocket;
	public boolean runOK = false;
	public Thread thread;
	
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
		this.clientSocket = s;
		this.msg_incoming = incoming;
		setRunOK(true);
	}
	
	/**/
	public void sendMessage(Message msg){
		try {
			// send message
			new DataOutputStream(clientSocket.getOutputStream()).writeUTF(msg.toString());
		} catch (IOException e) {
			if(e instanceof SocketException || 
					e instanceof EOFException){
				
			}
			else{
				e.printStackTrace();
			}
		}
	}
	public Message receiveMessage(){
		String strMsg = null;
		try {
			if(clientSocket != null && !clientSocket.isClosed()) strMsg = new DataInputStream(clientSocket.getInputStream()).readUTF();
			if(strMsg != null && strMsg != ""){
				Message msg = Message.createMessage();
				msg.fromString(strMsg);
				return msg;
			}
			else{
				return null;
			}
		} catch (IOException e) {
			if(e instanceof SocketException || 
					e instanceof EOFException){
				
			}
			else{
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**/
	public void run(){
		while(getRunOK()){
			try {
				//
				Message msg = receiveMessage();

				if(msg==null){
					System.out.println("NET:\tS_Client received NULL message.");
					break;
				}

				String messageType = MessageUtil.getType(msg);
				String messageCast = MessageUtil.getCast(msg);

				if(messageType==null || messageType=="" ||
						messageCast==null || messageCast==""){
					System.out.println("NET:\tS_Client received message, where messageType or messageCast don't have proper format.");
					System.out.println(msg.toString());
					continue;
				}

				// initialize id of sclient, that is the id of corresponding MMORPG_Hero in cclient
				if(messageType.equals(MessageUtil.MSGTYPE_INIT_CLIENT)){
					boolean updated = updateIdFromMessage(msg);
					if(!updated){
						sendMessage(MessageUtil.createErrorMessage(server, this, "Error initialising/updating sclient: probably ID selected is already in use"));
						server.clients.remove(this);
						this.setRunOK(false);
						this.stop();
						break;
					}
					//System.out.println("NET:\tS_Client was initialised, id="+this.id);
				}
				else{
					// remove sclient from list of server's sclients, since the corresponding cclient sent destroy message
					if(messageType.equals(MessageUtil.MSGTYPE_DSTR_CLIENT)){
						//remove user from all chatrooms
					}
					else{
						//System.out.println("NET:\tS_Client received message, id="+this.id);
					}
				}

				// place message to pool for further proceeding
				msg_incoming.add(msg);

				// sleep
				Thread.sleep(100);
				
			} catch (InterruptedException e) {
				this.setRunOK(false);
				System.out.println("S_Client:\tinterrupted.");
				break;
			}
		}
		System.out.println("S_Client:\tstopped.");
	}
	
	public void stop(){
		setRunOK(false);
		thread.interrupt();
		//Thread.currentThread().stop();
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
	public boolean updateIdFromMessage(Message m){
		String identificator = MessageUtil.getFrom(m);
		if(identificator != null && 
			identificator.equals(new String())==false && 
			identificator.equals("")==false ){
			if(server.clientsMap.get(identificator)!=null){
				return false;
			}
			else{
				this.id = identificator;
				return true;
			}
		}
		return false;
	}
	

}

