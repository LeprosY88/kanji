package org.abratuhi.mmorpg.chatroom.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import org.abratuhi.mmorpg.chatroom.client.ChatRoom_Client;
import org.abratuhi.mmorpg.chatroom.model.ChatRoom;


public class C_Client implements Runnable{
	/**/
	public Socket clientSocket;
	public boolean runOK = false;
	public Thread thread;

	/**/
	public ArrayList<Message> msg_incoming = new ArrayList<Message>();
	public String id = new String();

	public ChatRoom_Client chatroomClient;


	/**/
	public C_Client(ChatRoom_Client cl){
		this.chatroomClient = cl;
	}

	/**/
	public void connect(String h, int hp){
		try {
			clientSocket = new Socket(h, hp);
			setRunOK(true);
			sendMessage(MessageUtil.createInitClientMessage(this));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**/
	public void sendMessage(Message msg){
		try {
			new DataOutputStream(clientSocket.getOutputStream()).writeUTF(msg.toString());
		} catch (IOException e) {
			if(e instanceof SocketException || 
					e instanceof EOFException){
				e.printStackTrace();
			}
			else{
				e.printStackTrace();
			}
		}
	}
	public Message receiveMessage(){
		String strMsg = null;
		try {
			strMsg = new DataInputStream(clientSocket.getInputStream()).readUTF();
			if(strMsg != null && strMsg != ""){
				Message msg = new Message();
				msg.fromString(strMsg);
				return msg;
			}
			else{
				return null;
			}
		} catch (IOException e) {
			if(e instanceof SocketException || 
					e instanceof EOFException){
				e.printStackTrace();
			}
			else{
				e.printStackTrace();
			}
		}
		return null;
	}

	/**/
	public void run(){
		while(getRunOK()){
			//System.out.println("C_Client:\tRunning...");
			try {
				//
				Message msg = null;
				if(clientSocket!=null && clientSocket.isClosed()==false && clientSocket.isConnected()==true) msg = receiveMessage();
				if(msg == null) continue;


				msg_incoming.add(msg);

				System.out.println("C_Client:\tProceeding message"+msg.toString());

				// proceed message
				String type = MessageUtil.getType(msg);
				String room = MessageUtil.getRoom(msg);
				String cast = MessageUtil.getCast(msg);
				String from = MessageUtil.getFrom(msg);
				String to = MessageUtil.getTo(msg);
				String text = MessageUtil.getText(msg);

				// system messages
				if(room.equals("system")){
					if(type.equals(MessageUtil.MSGTYPE_INIT_CLIENT)){
						chatroomClient.guiclient.textarea_chatroommessages.append("["+from+" joined program]"+"\n");
					}
					if(type.equals(MessageUtil.MSGTYPE_DSTR_CLIENT)){
						chatroomClient.guiclient.textarea_chatroommessages.append("["+from+" left program]"+"\n");
					}
					if(type.equals(MessageUtil.MSGTYPE_ERROR)){
						chatroomClient.guiclient.textarea_chatroommessages.append("ERROR: "+text+"\n");
						this.disconnect();
					}
					if(type.equals(MessageUtil.MSGTYPE_CREATE_ROOM)){
						chatroomClient.addRoom(text);
					}
					if(type.equals(MessageUtil.MSGTYPE_DELETE_ROOM)){
						chatroomClient.removeRoom(text);
					}
					if(type.equals(MessageUtil.MSGTYPE_JOIN_ROOM)){
						chatroomClient.addUser(text, from);
					}
					if(type.equals(MessageUtil.MSGTYPE_LEAVE_ROOM)){
						chatroomClient.removeUser(text, from);
					}
					if(type.equals(MessageUtil.MSGTYPE_LIST_ROOM)){
						String[] rooms = MessageUtil.getRoomsFromText(msg);
						for(int i=0; i<rooms.length; i++){
							chatroomClient.addRoom(rooms[i]);
						}
					}
					if(type.equals(MessageUtil.MSGTYPE_UPDATE_ROOM)){
						String roomName = MessageUtil.getRoomsFromText(msg)[0];
						String[] users = MessageUtil.getUsersFromText(msg);
						if(roomName !=null && users.length > 0){
							for(int i=0; i<users.length; i++){
								chatroomClient.addUser(roomName, users[i]);
							}
						}
					}
				}

				// chat messages
				if(type.equals(MessageUtil.MSGTYPE_CHAT)){
					chatroomClient.chatrooms.get(room).messages.add(msg);
					chatroomClient.guiclient.textarea_chatroommessages.append("["+room+"/"+from+"]: "+text+"\n");
				}

				// update gui
				chatroomClient.guiclient.updateContent();

				// sleep
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.out.println("C_Client:\tinterrupted.");
				this.setRunOK(false);
				break;
			}
		}
		System.out.println("C_Client:\tstopped.");
		return;
	}

	public void disconnect(){
		try {
			thread.interrupt();
			//Thread.currentThread().stop();
			sendMessage(MessageUtil.createDestroyClientMessage(this));
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

}
