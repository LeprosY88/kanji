package gpl.java.abratuhi.src.net.messaging;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class C_Client extends Thread{
	public final static boolean C_CLIENT_DEBUG = true;
	public final static boolean C_CLIENT_CHECK_RANGE = false;

	/**/
	public Socket s;
	public boolean runOK = false;
	//public boolean is
	
	/**/
	public ArrayList<Message> msg_incoming = new ArrayList<Message>();
	//public ArrayList<Message> msg_outcoming = new ArrayList<Message>();
	
	/**/
	public String idClient = new String();
	public Point pos;
	//public String idServer;
	
	
	/**/
	public C_Client(){
		
	}
	
	/**/
	public void connect(String h, int hp){
		try {
			s = new Socket(h, hp);
			this.runOK = true;
//			sendMessage(new Message(new String[]{idClient, Message.MSG_TO_ALL, Message.MSG_CONNECT, ""}));
			sendMessage(new Message("login", "", idClient, "broadcast", ""));
			System.out.println("Client connected to:\t"+h+":"+hp+".");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		try {
//			sendMessage(new Message(new String[]{idClient, Message.MSG_TO_ALL, Message.MSG_DISCONNECT, ""}));
			sendMessage(new Message("logout", "", idClient, "broadcast", ""));
			//s.getInputStream().close();
			//s.getOutputStream().flush();
			//s.getOutputStream().close();
			Thread.sleep(1000);
			s.close();
			System.out.println("Client disconnected.");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void login(String nick){
		this.idClient = nick;
		System.out.println("Client logged in.");
	}
	
	public void logout(){
		
	}
	
	/**/
	public void sendMessage(Message msg){
		try {
			new DataOutputStream(s.getOutputStream()).writeUTF(msg.toString());
		} catch (IOException e) {
			e.printStackTrace();
			if (C_CLIENT_DEBUG) System.out.println("to java.net.SocketException: \nMessage = "+msg.toString());
		}
	}
	public Message receiveMessage(){
		String strMsg = null;
		try {
			strMsg = new DataInputStream(s.getInputStream()).readUTF();
			if(strMsg != null && strMsg != ""){
				Message msg = new Message();
				msg.fromString(strMsg);
				if(C_CLIENT_DEBUG) System.out.println(strMsg);
				return msg;
			}
			else{
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (C_CLIENT_DEBUG) System.out.println("to java.net.SocketException: \nMessage = "+strMsg);
		}
		return null;
	}
	
	/**/
	public void run(){
		while(this.runOK){
			//
			Message msg = receiveMessage();
			msg_incoming.add(msg);
			//
			/*if(	C_CLIENT_CHECK_RANGE &&
				!msg.data[1][0].equals(idClient) && 
				msg.data[1][2].equals(Message.MSG_MOVE)){
				
			}*/
			if(msg.getFromId().equals(idClient) && msg.getType().equals("logout")){
				stopp();
				break;
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
