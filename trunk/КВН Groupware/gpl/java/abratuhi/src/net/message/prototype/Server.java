package gpl.java.abratuhi.src.net.message.prototype;

import gpl.java.abratuhi.src.util.backup.Backup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class Server extends Thread{
	
	/**/
	public ServerSocket server;
	public int server_port;
	
	/**/
	public ArrayList<Message> msg_incoming = new ArrayList<Message>();
	public ArrayList<String> msg_history = new ArrayList<String>();
	
	/**/
	public ArrayList<Client> clients = new ArrayList<Client>();
	//public int nActiveThreads = 0;
	public MPTUA mptua = new MPTUA(clients, msg_incoming, msg_history);
	
	/**/
	public boolean isUp = false;
	public boolean isDown = true;
	
	/**/
	public Server(){
		
	}
	public Server(int port){
		this.server_port=port;
	}

	/**/
	public void up(){
		try {
			server = new ServerSocket(server_port);
			isUp = true;
			isDown = false;
			System.out.println("Server UP at port:\t"+server_port+".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void up(int port){
		try {
			this.server_port = port;
			server = new ServerSocket(server_port);
			isUp = true;
			isDown = false;
			System.out.println("Server UP at port:\t"+server_port+".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void down(){
		//while(activeCount()>1){
		//}
		stopp();
		close();
		System.out.println("Server DOWN at port:\t"+server_port+".");
	}
	
	/**/
	public void run(){
		/**/
		mptua.start();
		/**/
		while(isUp){
			try {
				/* check old connections */
				for(int i=0; i<clients.size(); i++){
					if(clients.get(i).isClosed){
						clients.remove(i);
						//nActiveThreads--;
					}
				}
				/* print statistics */
				printSummary();
				/* wait for new connections*/
				Socket s = server.accept();
				System.out.println("Server status:\tconnection accepted.");
				Client tclient = new Client(s, msg_incoming);
				tclient.start();
				clients.add(tclient);
				System.out.println("Server status:\tnew client handle added.");
				//nActiveThreads++;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/**/
		mptua.stopp();
	}
	public void stopp(){
		this.isUp = false;
		this.isDown = true;
	}
	public void close(){
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**/
	public void printSummary(){
		System.out.println("*************** Server Statistics *****************");
		System.out.println("Server on port:\t"+server_port);
		if(isUp) System.out.println("Server is up:\ttrue");
		else System.out.println("Server is up:\tfalse");
		System.out.println("Server's # of active threads:\t"+clients.size());
		System.out.println("Server's # of incoming messages:\t"+msg_incoming.size());
		//System.out.println("Server's # of outcoming messages:\t"+msg_outcoming.size());
	}
	public void printHistory(){
		for(int i=0; i<msg_history.size(); i++){
			System.out.println(msg_history.get(i));
		}
	}
	public void backup(){
		Backup.writeToFile(new String(String.valueOf(new Date().getTime())+".kvnlog"), msg_history);
	}
	
	/**/
	public static void main(String[] args){
		Server s = new Server();		
		int port = 9000;
		boolean runOK = true;
		
		while(runOK){
			System.out.print("> ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				String msg = br.readLine();
				if(msg.equalsIgnoreCase("up")) {
					s.up(port);
					s.start();
				}
				/*if(msg.equalsIgnoreCase("start")) {
					s.start();
				}*/
				/*if(msg.equalsIgnoreCase("stop")) {
					s.stopp();
				}*/
				if(msg.equalsIgnoreCase("down")) {
					s.down();
				}
				if(msg.equalsIgnoreCase("info")) {
					s.printSummary();
				}
				if(msg.equalsIgnoreCase("history")) {
					s.printHistory();
				}
				if(msg.equalsIgnoreCase("backup")) {
					s.backup();
				}
				if(msg.equalsIgnoreCase("quit")) {
					runOK = false;
					break;
				}
			} catch (IOException e) {				
				e.printStackTrace();
			}			
		}
	}

}
