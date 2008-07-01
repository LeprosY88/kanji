package org.abratuhi.mmorpg.net.messaging;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class S_Client extends Thread{
	/**/
	public Socket s;
	public boolean runOK = false;
	
	/**/
	public ArrayList<Message> msg_incoming = new ArrayList<Message>();
	
	/**/
	String id;
	Point position;
	
	
	/**/
	public S_Client(Socket s, ArrayList<Message> incoming){
		this.s = s;
		this.msg_incoming = incoming;
		this.runOK = true;
	}
	
	/**/
	public void sendMessage(Message msg){
		try {
			new DataOutputStream(s.getOutputStream()).writeUTF(msg.toString());
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
		while(this.runOK){
			//
			Message msg = receiveMessage();
			
			// initialize id of sclient, that is the id of corresponding MMORPG_Hero in cclient
			if(msg.d.getRootElement().getAttributeValue("type")!="initSClient"){
				this.id = msg.d.getRootElement().getChild("from").getAttributeValue(id);
				if(msg.d.getRootElement().getChild("from") == null){
					System.out.println("Error: problem with getting named children in JDOM.");
				}
			}
			
			// otherwise just forward the message
			else{
				msg_incoming.add(msg);
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
		this.runOK = false;
		this.stop();
		//System.out.println("Client was stopped.");
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

