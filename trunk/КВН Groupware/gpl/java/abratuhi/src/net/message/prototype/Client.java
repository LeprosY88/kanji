package gpl.java.abratuhi.src.net.message.prototype;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client extends Thread{

	/**/
	public Socket s;
	public boolean isConnected = false;
	public boolean isClosed = false;
	public boolean isClientSide = false;
	public boolean isServerSide = false;
	//public boolean is
	
	/**/
	public ArrayList<Message> msg_incoming = new ArrayList<Message>();
	//public ArrayList<Message> msg_outcoming = new ArrayList<Message>();
	
	/**/
	public String idClient = new String();
	public String idServer = new String();
	
	
	/**/
	public Client(){
		
	}
	public Client(String id){
		this.idClient = id;
	}
	/*public Client(Socket s){
		this.s = s;
		this.isConnected = true;
	}*/
	public Client(Socket s, ArrayList<Message> incoming){
		this.s = s;
		this.msg_incoming = incoming;
		this.isConnected = true;
		this.isServerSide = true;
	}
	
	/**/
	public void connect(String h, int hp){
		try {
			s = new Socket(h, hp);
			this.isConnected = true;
			this.isClientSide = true;
			System.out.println("Client connected to:\t"+h+":"+hp+".");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		try {
			this.isConnected = false;
			s.getInputStream().close();
			s.getOutputStream().flush();
			s.getOutputStream().close();
			s.close();
			System.out.println("Client disconnected.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void login(){
		sendMessage(new Message(Message.MSG_TYPE_STATUS, idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_STATUS_GENERAL, Message.MSG_BODY_LOGIN));
	}
	
	public void logout(){
		sendMessage(new Message(Message.MSG_TYPE_STATUS, idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_STATUS_GENERAL, Message.MSG_BODY_LOGOUT));
	}
	
	/**/
	public void sendMessage(Message msg){
		//TODO: server-sided and client-sided Clients must have the same idClient
		//msg.msg_from = idClient;
		//
		try {
			new DataOutputStream(s.getOutputStream()).writeUTF(msg.Message2String());
			//System.out.println("Client status:\t sent message:\t"+msg.Message2String());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Message receiveMessage(){
		String strMsg;
		try {
			strMsg = new DataInputStream(s.getInputStream()).readUTF();
			if(strMsg != null && strMsg != ""){
				Message msg = new Message();
				msg.String2Message(strMsg);
				//System.out.println("Client status:\t received message:\t"+strMsg);
				System.out.println(strMsg);
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
		this.isConnected = true;
		while(this.isConnected){
			//
			Message msg = receiveMessage();
			msg_incoming.add(msg);
			//
			if(isServerSide && msg.msg_type.equals(Message.MSG_TYPE_STATUS) && msg.msg_head.equals(Message.MSG_HEAD_STATUS_GENERAL) && msg.msg_body.equals(Message.MSG_BODY_LOGIN)){
				idClient = msg.msg_from;
			}
			if(isServerSide && msg.msg_type.equals(Message.MSG_TYPE_STATUS) && msg.msg_head.equals(Message.MSG_HEAD_STATUS_GENERAL) && msg.msg_body.equals(Message.MSG_BODY_LOGOUT)){
				stopp();
				logout();
				close();
				//System.out.println("Client has received a logout message.");
			}
			//msg_incoming.add(receiveMessage());
		}
		//System.out.println("Client NOT running.");
		//return;
	}
	
	public void startt(){
		this.isConnected = true;
	}
	public void stopp(){
		this.isConnected = false;
		//System.out.println("Client was stopped.");
	}
	
	public void close(){
		try {
			this.s.close();
			this.isClosed = true;
		} catch (IOException e) {
			//Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**/
	public void printSummary(){
		System.out.println("*************** Client Statistics *****************");
		//System.out.println("Client to:\t"+host+":"+host_port);
		if(isConnected) System.out.println("Client is connected:\ttrue");
		else System.out.println("Client is connected:\tfalse");
		System.out.println("Client's # of incoming messages:\t"+msg_incoming.size());
		//System.out.println("Client's # of outcoming messages:\t"+msg_outcoming.size());
	}

}
