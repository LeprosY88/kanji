package org.abratuhi.mmorpg.chatroom.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.abratuhi.mmorpg.chatroom.net.Server;

public class ChatRoom_Server {
	
	public Server server;
	
	public ChatRoom_Server(){
		server = new Server();
	}

}
