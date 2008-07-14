package org.abratuhi.mmorpg.net.messaging;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.abratuhi.mmorpg.model.MMORPG_Hero;
import org.abratuhi.mmorpg.util.MessageUtil;

public class C_Client extends Thread{
	/**/
	public Socket s;
	public boolean runOK = false;
	
	/**/
	public ArrayList<Message> msg_incoming = new ArrayList<Message>();
	
	/**/
	public MMORPG_Hero hero;
	
	/**/
	public String id = new String();
	public Point position = new Point();
	
	
	/**/
	public C_Client(MMORPG_Hero heroo){
		this.hero = heroo;
	}
	
	/**/
	public void connect(String h, int hp){
		try {
			s = new Socket(h, hp);
			setRunOK(true);
			sendMessage(MessageUtil.createInitClientMessage(this));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		try {
			Thread.sleep(1000);
			s.close();
			System.out.println("Client disconnected.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
				Message msg = new Message();
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
			msg_incoming.add(msg);
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
	public void printSummary(){
		System.out.println("*************** Client Statistics *****************");
		//System.out.println("Client to:\t"+host+":"+host_port);
		if(runOK) System.out.println("Client is running:\ttrue");
		else System.out.println("Client is running:\tfalse");
		System.out.println("Client's # of incoming messages:\t"+msg_incoming.size());
		//System.out.println("Client's # of outcoming messages:\t"+msg_outcoming.size());
	}

}
