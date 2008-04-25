package gpl.java.abratuhi.src.net.messaging;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class S_Client extends Thread{
	public final static boolean S_CLIENT_DEBUG = true;
	public final static boolean S_CLIENT_CHECK_RANGE = false;

	/**/
	public Socket s;
	public boolean runOK = false;
	
	/**/
	public ArrayList<Message> msg_incoming = new ArrayList<Message>();
	//public ArrayList<Message> msg_outcoming = new ArrayList<Message>();
	
	/**/
	public String idClient;
	//public Point pos;
	public Point pos = new Point(0,0);
	
	
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
			if (S_CLIENT_DEBUG) System.out.println("to java.net.SocketException: \nMessage = "+msg.toString());
		}
	}
	public Message receiveMessage(){
		String strMsg = null;
		try {
			strMsg = new DataInputStream(s.getInputStream()).readUTF();
			if(strMsg != null && strMsg != ""){
				Message msg = Message.createMessage();
				msg.fromString(strMsg);
				if(S_CLIENT_DEBUG) System.out.println(strMsg);
				return msg;
			}
			else{
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (S_CLIENT_DEBUG) System.out.println("to java.net.SocketException: \nMessage = "+strMsg);
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
			if(msg.getType().equals("login")){
				idClient = msg.getFromId();
			}
			if(msg.getFromId().equals(idClient) && msg.getType().equals("logout")){
				stopp();
				break;
			}
			if(S_CLIENT_CHECK_RANGE && msg.getFromId().equals(idClient) && msg.getType().equals("position")){
				pos = msg.getPosition();
				if(S_CLIENT_DEBUG) System.out.println("S_Client: new coordinates set for "+idClient + " ("+pos.x+", "+pos.y+")");
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

