package gpl.java.abratuhi.src.model;

import gpl.java.abratuhi.src.net.message.prototype.Client;
import gpl.java.abratuhi.src.net.message.prototype.Message;

import java.util.ArrayList;

public class Chat {
	
	public Client c;
	public ArrayList<User> users = new ArrayList<User>();
	public ArrayList<String> incoming = new ArrayList<String>();
	public ArrayList<String> history = new ArrayList<String>();
	
	public Chat(Client cl){
		this.c = cl;
	}
	
	public void addUser(User u){
		u.setStatus(Message.MSG_HEAD_STATUS_CHAT, Message.MSG_BODY_LOGIN);
		users.add(u);
	}
	public void removeUser(User u){
		u.setStatus(Message.MSG_HEAD_STATUS_CHAT, Message.MSG_BODY_LOGOUT);
		users.remove(u);
	}
	public int findUser(User u){
		return users.indexOf(u);
	}
	
	public void sendMessage(String to, String msgBody){
		c.sendMessage(new Message(Message.MSG_TYPE_CHAT, c.idClient, to, Message.MSG_HEAD_CHAT_MESSAGE, msgBody));
	}
	
	public void parseIncomingMessage(User u, Message msg){
		if(msg.msg_type.equals(Message.MSG_TYPE_CHAT)){//additional check, is also performed in KVNGroupware
			history.add(msg.Message2String());//save message in history
			if(msg.msg_head.equals(Message.MSG_HEAD_STATUS_CHAT)){//is status message
				int index = findUser(u);
				if(index==-1 && msg.msg_body.equals(Message.MSG_BODY_LOGIN)){
					addUser(u);
				}
				if(index!=-1 && msg.msg_body.equals(Message.MSG_BODY_LOGOUT)){
					removeUser(u);
				}
			}
			if(msg.msg_head.equals(Message.MSG_HEAD_CHAT_MESSAGE)){//is chat message
				incoming.add("<"+msg.msg_from+">"+": "+msg.msg_body+"\n");
			}
		}
	}

}
