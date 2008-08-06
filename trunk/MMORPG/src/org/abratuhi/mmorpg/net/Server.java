package org.abratuhi.mmorpg.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.abratuhi.mmorpg.model.MMORPG_Map;

public class Server extends Thread{
	
	public final static double DIST = 100.0;
	
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
	
	/**/
	public Server(){
		
	}

	/**/
	public void up(int port){
		try {
			this.server_port = port;
			server = new ServerSocket(server_port);
			setIsUp(true);
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
			setIsUp(false);
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
		while(getIsUp()){
			try {
				/* wait for new connections*/
				Socket s = server.accept();
				System.out.println("Server status:\tconnection accepted.");
				S_Client tclient = new S_Client(this, s, msg_incoming);
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
		setIsUp(false);
	}
	public void close(){
		
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
	
	/**
	 * Prim's algorithm for building Minimum Spanning Trees
	 */
	public void buildNeighboursMST(){
		ArrayList<S_Client> l1 = new ArrayList<S_Client>();
		ArrayList<S_Client> l2 = (ArrayList<S_Client>) clients.clone();
		
		l2.get(0).mstneighbours.clear();
		l1.add(l2.remove(0));
		
		while(l2.size() > 0){
			int ind1=-1, ind2=-1;
			
			double mindist = MMORPG_Map.XSIZE + MMORPG_Map.YSIZE;
			double curdist = 0;
			for(int i=0; i<l1.size(); i++){
				for(int j=0; j<l2.size(); j++){
					curdist = l1.get(i).distance(l2.get(j));
					if(curdist < mindist) {mindist = curdist; ind1 = i; ind2 = j;}
				}
			}
			
			
			l2.get(ind2).mstneighbours.clear();
			l1.get(ind1).mstneighbours.add(l2.get(ind2).id);
			l2.get(ind2).mstneighbours.add(l1.get(ind1).id);
			l1.add(l2.remove(ind2));
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
	}
	
	public static void main(String[] args) {
		
		Server server = new Server();
		server.up(9000);
		server.start();

	}

}
