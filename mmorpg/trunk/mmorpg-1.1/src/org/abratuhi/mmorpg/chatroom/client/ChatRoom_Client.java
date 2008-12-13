package org.abratuhi.mmorpg.chatroom.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Pattern;

import org.abratuhi.mmorpg.chatroom.gui.ChatRoom_GUIClient;
import org.abratuhi.mmorpg.chatroom.model.ChatRoom;
import org.abratuhi.mmorpg.chatroom.net.C_Client;
import org.abratuhi.mmorpg.chatroom.net.Message;
import org.abratuhi.mmorpg.chatroom.net.MessageUtil;

public class ChatRoom_Client {

	public ChatRoom_GUIClient guiclient;
	public C_Client netclient;
	public HashMap<String, ChatRoom> chatrooms = new HashMap<String, ChatRoom>();

	public String activeChatroom = new String();

	Properties props = new Properties();

	public ChatRoom_Client(ChatRoom_GUIClient guic){
		loadProperties();
		//netclient = new C_Client(this);
		guiclient = guic;
	}

	public void loadProperties(){
		try {
			//System.out.println(new File("path.temp").getAbsolutePath());
			props.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addRoom(String roomName){
		if(chatrooms.get(roomName) == null){
			chatrooms.put(roomName, new ChatRoom(roomName));
		}
	}
	public void removeRoom(String roomName){
		if(chatrooms.get(roomName) != null){
			chatrooms.remove(roomName);
		}
	}
	public void addUser(String roomName, String userName){
		if(chatrooms.get(roomName)!=null && !chatrooms.get(roomName).users.contains(userName)){
			chatrooms.get(roomName).users.add(userName);
		}
	}
	public void removeUser(String roomName, String userName){
		if(chatrooms.get(roomName)!=null && chatrooms.get(roomName).users.contains(userName)){
			chatrooms.get(roomName).users.remove(userName);
		}
	}

	public void startconnect(){
		// get stored values
		String host = props.getProperty("host");
		Integer port = Integer.valueOf(props.getProperty("port"));
		String user = props.getProperty("user");
		String passwd = props.getProperty("password");

		// renew netclient(to avoid socket problems) and connect
		netclient = new C_Client(this);
		netclient.id = user;
		netclient.connect(host, port);
		
		// start netclient
		netclient.setRunOK(true);
		netclient.thread = new Thread(netclient);
		netclient.thread.start();
	}
	public void startconnect(String username, String userpass){
		// get stored values
		String host = props.getProperty("host");
		Integer port = Integer.valueOf(props.getProperty("port"));
		String user = props.getProperty("user");
		String passwd = props.getProperty("password");
		
		// eventually substitute them
		user = username;
		passwd = userpass;
		
		// renew netclient(to avoid socket problems) and connect
		netclient = new C_Client(this);
		netclient.id = user;
		netclient.connect(host, port);
		
		// start netclient
		netclient.setRunOK(true);
		netclient.thread = new Thread(netclient);
		netclient.thread.start();
	}

	public void stopdisconnect(){
		netclient.disconnect();
	}

	public void createRoom(String roomName){
		netclient.sendMessage(MessageUtil.createCreateRoomMessage(netclient, roomName));
	}

	public void joinRoom(String roomName){
		if(!roomName.equals(activeChatroom)){
			if(activeChatroom != null && activeChatroom.equals("")  && activeChatroom.equals(new String())){
				netclient.sendMessage(MessageUtil.createLeaveRoomMessage(netclient, activeChatroom));
			}
			netclient.sendMessage(MessageUtil.createJoinRoomMessage(netclient, roomName));
			activeChatroom = roomName;
		}
	}

	public void leaveRoom(String roomName){
		netclient.sendMessage(MessageUtil.createLeaveRoomMessage(netclient, roomName));
		activeChatroom = "";
	}

	public void deleteRoom(String roomName){
		netclient.sendMessage(MessageUtil.createDeleteRoomMessage(netclient, roomName));
	}

	public void send(String roomName, String messageText){
		String text = messageText;
		netclient.sendMessage(MessageUtil.createChatMessageRoomBroadcast(netclient, roomName, text));
	}

}
