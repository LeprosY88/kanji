package gpl.java.abratuhi.src.net.messaging;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{
	
	/**/
	public ServerSocket server;
	public int server_port;
	
	/**/
	public ArrayList<Message> msg_incoming = new ArrayList<Message>();
	
	/**/
	public ArrayList<S_Client> clients = new ArrayList<S_Client>();
	public MPTUA mptua = new MPTUA(clients, msg_incoming);
	
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
		try {
			//while(activeCount()>1){
			//}
			server.close();
			isUp=false;
			isDown=true;
			System.out.println("Server DOWN at port:\t"+server_port+".");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**/
	public void run(){
		/**/
		mptua.start();
		/**/
		while(isUp){
			try {
				/* check old connections */
				/*for(int i=0; i<clients.size(); i++){
					if(!clients.get(i).runOK){
						clients.remove(i);
					}
				}*/
				/* print statistics */
				//printSummary();
				/* wait for new connections*/
				Socket s = server.accept();
				System.out.println("Server status:\tconnection accepted.");
				S_Client tclient = new S_Client(s, msg_incoming);
				tclient.start();
				clients.add(tclient);
				System.out.println("Server status:\tnew client handle added.");
				Thread.sleep(100);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void stopp(){
		this.isUp = false;
	}
	public void close(){
		
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
	
	public static void main(String[] args) {
		
		Server server = new Server();
		server.up(9000);
		server.start();

	}

}
