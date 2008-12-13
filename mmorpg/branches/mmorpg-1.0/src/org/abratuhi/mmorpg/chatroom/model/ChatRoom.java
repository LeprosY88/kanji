package org.abratuhi.mmorpg.chatroom.model;

import java.util.ArrayList;

import org.abratuhi.mmorpg.chatroom.net.Message;

public class ChatRoom {
	
	public String name = new String();
	public ArrayList<Message> messages = new ArrayList<Message>();
	public ArrayList<String> users = new ArrayList<String>();
	
	public ChatRoom(String roomname){
		this.name = roomname;
	}

}
