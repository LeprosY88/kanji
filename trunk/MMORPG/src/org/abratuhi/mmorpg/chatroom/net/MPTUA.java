package org.abratuhi.mmorpg.chatroom.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.abratuhi.mmorpg.chatroom.model.ChatRoom;


public class MPTUA implements Runnable{
	/**/
	public Server server;
	public ArrayList<S_Client> clients = new ArrayList<S_Client>();
	public ArrayList<Message> incoming = new ArrayList<Message>();
	/**/
	public boolean isUp = false;
	public Thread thread; 
	
	/**/
	public MPTUA(){
		setIsUp(true);
		System.out.println("MPTUA is UP.");
	}
	public MPTUA(Server server){
		setIsUp(true);
		this.server = server;
		this.clients = server.clients;
		this.incoming = server.msg_incoming;
		System.out.println("MPTUA is UP.");
	}
	
	/**/
	public void run(){
		while(getIsUp()){
			if(incoming.size()>0){
				Message msg = incoming.remove(0);
				
				System.out.println("MPTUA:\tProceeding message "+msg.toString());
				
				String type = MessageUtil.getType(msg);
				String room = MessageUtil.getRoom(msg);
				String cast = MessageUtil.getCast(msg);
				String from = MessageUtil.getFrom(msg);
				String to = MessageUtil.getTo(msg);
				String text = MessageUtil.getText(msg);
				
				// system messages
				if(room.equals("system")){
					if(type.equals(MessageUtil.MSGTYPE_INIT_CLIENT)){
						updateS_Client(from);
					}
					if(type.equals(MessageUtil.MSGTYPE_DSTR_CLIENT)){
						removeS_Client(from);
					}
					if(type.equals(MessageUtil.MSGTYPE_CREATE_ROOM)){
						if(server.chatroomMap.get(text) == null){
							server.chatroomMap.put(text, new ChatRoom(text));
						}
						else{
							S_Client sclient = findS_Client(from);
							sclient.sendMessage(MessageUtil.createErrorMessage(server, sclient, "Room with this name already exists!"));
						}
					}
					if(type.equals(MessageUtil.MSGTYPE_DELETE_ROOM)){
						if(server.chatroomMap.get(text) != null){
							server.chatroomMap.remove(text);
						}
						else{
							S_Client sclient = findS_Client(from);
							sclient.sendMessage(MessageUtil.createErrorMessage(server, sclient, "Room with this name  doesn't exist!"));
						}
					}
					if(type.equals(MessageUtil.MSGTYPE_JOIN_ROOM)){
						String roomName = text;
						ChatRoom cr = server.chatroomMap.get(roomName);
						
						if(cr != null){
							// add user to room
							if(!cr.users.contains(from)) cr.users.add(from);
							// send user the list of users already in room
							S_Client cSClient = findS_Client(from);
							cSClient.sendMessage(MessageUtil.createUpdateRoomMessage(server, cSClient, roomName));
						}
						else{
							S_Client sclient = findS_Client(from);
							sclient.sendMessage(MessageUtil.createErrorMessage(server, sclient, "Room with this name  doesn't exist!"));
						}
					}
					if(type.equals(MessageUtil.MSGTYPE_LEAVE_ROOM)){
						ChatRoom cr = server.chatroomMap.get(text);
						if(cr != null){
							if(cr.users.contains(from)) cr.users.remove(from);
						}
						else{
							S_Client sclient = findS_Client(from);
							sclient.sendMessage(MessageUtil.createErrorMessage(server, sclient, "Room with this name  doesn't exist!"));
						}
					}
					
					if(type.equals(MessageUtil.MSGTYPE_REQUPDATE_ROOM)){
						String roomName = text;
						ChatRoom cr = server.chatroomMap.get(roomName);
						
						if(cr != null){
							// send user the list of users already in room
							S_Client cSClient = findS_Client(from);
							cSClient.sendMessage(MessageUtil.createUpdateRoomMessage(server, cSClient, roomName));
						}
						else{
							System.out.println("MPTUA:\tReqUpdateRoom() failed: room not found: "+roomName);
						}
					}
					
					// broadcast system messages
					System.out.println("MPTUA:\tBroadcasting system message...");
					for(int i=0; i<clients.size(); i++){
						System.out.println("TO:\t"+clients.get(i).id);
						clients.get(i).sendMessage(msg);
					}
				}
				
				// chat messages
				if(type.equals(MessageUtil.MSGTYPE_CHAT)){
					if(cast!=null && cast.equals(MessageUtil.MSGCAST_BROADCAST)){	// broadcast
						for(int i=0; i<clients.size(); i++){
							clients.get(i).sendMessage(msg);
						}
					}
					if(cast!=null && cast.equals(MessageUtil.MSGCAST_ROOMBROADCAST)){
						ChatRoom cr = server.chatroomMap.get(room);
						if(cr != null){
							for(int i=0; i<cr.users.size(); i++){
								findS_Client(cr.users.get(i)).sendMessage(msg);
							}
						}
					}
					if(cast!=null && cast.equals(MessageUtil.MSGCAST_UNICAST)){	// unicast
						S_Client cS_Client = findS_Client(to);
						if(cS_Client == null){
							System.out.println("MPTUA:\tProceeding of message failed due to bad message syntax (couldn't find client with given name)");
							System.out.println(msg.toString());
							continue;
						}
						cS_Client.sendMessage(msg);
					}
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				this.setIsUp(false);
				System.out.println("MPTUA:\tinterrupted.");
				break;
			}
		}
		System.out.println("MPTUA:\tstopped.");
	}
	
	public void stop(){
		setIsUp(false);
		thread.interrupt();
		//Thread.currentThread().stop();
	}
	
	/*public void close(){
		
	}*/
	
	public S_Client findS_Client(String name){
		S_Client sc = server.clientsMap.get(name);
		
		if(sc == null){
			for(int i=0; i<clients.size(); i++){
				if(clients.get(i).id.equals(name)){
					sc = clients.get(i);
					break;
				}
			}
		}
		
		if(sc == null){
			System.out.println("MPTUA:\t Client not found, id = "+name);
		}
		
		return sc;
	}
	
	public void updateS_Client(String name){
		S_Client fromSClient = findS_Client(name);
		if(fromSClient != null){
			server.clientsMap.put(name, fromSClient);
			fromSClient.sendMessage(MessageUtil.createListRoomMessage(server, fromSClient));
		}
	}
	
	public void removeS_Client(String name){
		S_Client cSClient = findS_Client(name);
		String cSClientId = name;

		if(cSClient != null){
			// stop sclient thread
			cSClient.stop();
			//try {cSClient.clientSocket.shutdownOutput();} catch (IOException e) {e.printStackTrace();}
			
			// remove client from all chatrooms
			Iterator roomIterator = server.chatroomMap.entrySet().iterator();
			while(roomIterator.hasNext()){
				Map.Entry roomEntry = (Entry) roomIterator.next();
				ChatRoom cChatRoom = (ChatRoom) roomEntry.getValue();
				cChatRoom.users.remove(cSClientId);
			}

			//remove client from lists of clients
			server.clients.remove(cSClient);
			server.clientsMap.remove(name);
		}
		else{
			System.out.println("MPTUA:\tCouldn't find sclient to remove for : "+name);
		}
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

}
