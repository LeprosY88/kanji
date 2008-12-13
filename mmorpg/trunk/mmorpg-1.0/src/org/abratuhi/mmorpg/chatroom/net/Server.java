package org.abratuhi.mmorpg.chatroom.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.abratuhi.mmorpg.chatroom.model.ChatRoom;


public class Server implements Runnable{
	
	public final static Integer TIMEOUT = 1000;
	public final static Integer PAUSE = 100;
	
	/**/
	public ServerSocket serverSocket;
	public int server_port;
	
	/**/
	public ArrayList<Message> msg_incoming = new ArrayList<Message>();
	public ArrayList<S_Client> clients = new ArrayList<S_Client>();
	
	/**/
	public HashMap<String, S_Client> clientsMap = new HashMap<String, S_Client>();
	public HashMap<String, ChatRoom> chatroomMap = new HashMap<String, ChatRoom>();
	
	
	public MPTUA mptua;
	
	/**/
	public boolean isUp = false;
	public Thread thread;
	
	/**/
	public Server(){
		
	}

	/**/
	public synchronized void up(int port){
		if(getIsUp() == true) return;
		try {
			server_port = port;
			serverSocket = new ServerSocket(server_port);
			//serverSocket.setSoTimeout(TIMEOUT);
			setIsUp(true);
			
			mptua = new MPTUA(this);
			
			System.out.println("Server UP at port:\t"+server_port+".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void down(){
		try {
			setIsUp(false);
			serverSocket.close();
			System.out.println("Server DOWN at port:\t"+server_port+".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**/
	public void run(){
		/**/
		mptua.thread = new Thread(mptua);
		mptua.thread.start();
		/**/
		while(getIsUp()){
			try {
				/* wait for new connections*/
				Socket s;
				if(serverSocket != null && !serverSocket.isClosed()){
					//server.setSoTimeout(100);
					s = serverSocket.accept();
					System.out.println("Server status:\tconnection accepted.");
					S_Client tclient = new S_Client(this, s, msg_incoming);
					tclient.thread = new Thread(tclient);
					tclient.thread.start();
					clients.add(tclient);
					System.out.println("Server status:\tnew client handle added.");
				}
				Thread.sleep(PAUSE);
			} catch (IOException e) {
				e.printStackTrace();
				if(e instanceof SocketTimeoutException ||
						e instanceof SocketException ||
						e instanceof EOFException){
					//e.printStackTrace();
				}
				else{
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				this.setIsUp(false);
				System.out.println("Server:\tinterrupted.");
				break;
			}
		}
		System.out.println("Server:\tstopped.");
	}
	public void stop(){
		mptua.stop();
		for(int i=0; i<clients.size(); i++){
			clients.get(i).stop();
		}
		setIsUp(false);
		thread.interrupt();
		//Thread.currentThread().stop();
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
	
	/**/
	public void printSummary(){
		System.out.println("*************** Server Statistics *****************");
		System.out.println("Server on port:\t"+server_port);
		System.out.println("Server is up:\t"+String.valueOf(getIsUp()));
		System.out.println("Server's # of clients:\t"+clients.size());
		System.out.println("Server's # of incoming messages:\t"+msg_incoming.size());
		System.out.println("Server's # of chatrooms:\t"+chatroomMap.size());
		
		Iterator chatroomsIterator = chatroomMap.keySet().iterator();
		while(chatroomsIterator.hasNext()){
			ChatRoom cr = chatroomMap.get((String)chatroomsIterator.next());
			System.out.println("Chatroom: "+cr.name+"; Users: "+cr.users.toString());
		}
		
		for (int i=0; i<msg_incoming.size(); i++){
			System.out.println("Message:\t"+msg_incoming.get(i).toString());
		}
	}
	
	public String getSummary(){
		StringWriter sw = new StringWriter();
		sw.append("**************************************************"+"\n");
		sw.append("*************** SERVER STATISTICS: ***************"+"\n");
		sw.append("**************************************************"+"\n");
		sw.append("Server on port:\t"+server_port+"\n");
		sw.append("Server is up:\t"+String.valueOf(getIsUp())+"\n");
		sw.append("Server's # of clients:\t"+clients.size()+"\n");
		sw.append("Server's # of incoming messages:\t"+msg_incoming.size()+"\n");
		sw.append("Server's # of chatrooms:\t"+chatroomMap.size()+"\n");
		
		Iterator chatroomsIterator = chatroomMap.keySet().iterator();
		while(chatroomsIterator.hasNext()){
			ChatRoom cr = chatroomMap.get((String)chatroomsIterator.next());
			sw.append("Chatroom: "+cr.name+"; Users: "+cr.users.toString()+"\n");
		}
		
		for (int i=0; i<msg_incoming.size(); i++){
			sw.append("Message:\t"+msg_incoming.get(i).toString()+"\n");
		}
		
		return sw.toString();
	}

}
