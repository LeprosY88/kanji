package gpl.java.abratuhi.src.model;

import gpl.java.abratuhi.src.net.message.prototype.Client;
import gpl.java.abratuhi.src.net.message.prototype.Message;

import java.util.ArrayList;

public class KVNGroupware extends Thread{
	
	//public Server s = new Server();
	public Client cl = new Client();
	public ArrayList<User> users = new ArrayList<User>();
	public Brainstorm b = new Brainstorm(cl);
	public Chat c = new Chat(cl);
	
	public boolean runOK = false;
	
	public KVNGroupware(){
		
	}
	
	/**/
	public void login(String nick){
		cl.idClient = nick;
		User u = new User(nick);
		addUser(u);		
	}
	public void logout(){
		if(findUser(cl.idClient) != -1) removeUser(users.get(findUser(cl.idClient)));
		reset();
	}
	public void connect(String addr, int port){
		//cl.startt();
		cl.connect(addr, port);
		cl.start();
		cl.login();
	}
	public void disconnect(){
		cl.stopp();
		cl.logout();
		cl.disconnect();
	}
	
	/**/
	public void joinBrainstorm(){
		cl.sendMessage(new Message(Message.MSG_TYPE_BRAINSTORM, cl.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_STATUS_BRAINSTORM, Message.MSG_BODY_LOGIN));
	}
	public void leaveBrainstorm(){
		cl.sendMessage(new Message(Message.MSG_TYPE_BRAINSTORM, cl.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_STATUS_BRAINSTORM, Message.MSG_BODY_LOGOUT));
	}
	
	/**/
	public void joinChat(){
		cl.sendMessage(new Message(Message.MSG_TYPE_CHAT, cl.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_STATUS_CHAT, Message.MSG_BODY_LOGIN));
	}
	public void leaveChat(){
		cl.sendMessage(new Message(Message.MSG_TYPE_CHAT, cl.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_STATUS_CHAT, Message.MSG_BODY_LOGOUT));
	}
	
	/**/
	public void addUser(User u){
		if(findUser(u) == -1){
			u.setStatus(Message.MSG_HEAD_STATUS_GENERAL, Message.MSG_BODY_LOGIN);
			users.add(u);
		}
	}
	public void removeUser(User u){
		if(findUser(u) != -1){
			u.setStatus(Message.MSG_HEAD_STATUS_GENERAL, Message.MSG_BODY_LOGOUT);
			u.setStatus(Message.MSG_HEAD_STATUS_CHAT, Message.MSG_BODY_LOGOUT);
			u.setStatus(Message.MSG_HEAD_STATUS_BRAINSTORM, Message.MSG_BODY_LOGOUT);
			users.remove(u);
		}
	}
	public int findUser(User u){
		return users.indexOf(u);
	}
	public int findUser(String nick){
		for(int i=0; i<users.size(); i++){
			if(users.get(i).nickname.equals(nick)){
				return i;
			}
		}
		return -1;
	}
	
	/**/
	public void reset(){
		
	}
	
	
	/**/
	public void run(){
		this.runOK = true;
		while(runOK){
			if(cl.msg_incoming.size()>0){
				Message m = cl.msg_incoming.remove(0);
				if(m!=null && m.msg_type.equals(Message.MSG_TYPE_STATUS)){
					int index = findUser(m.msg_from);
					if(index==-1 && m.msg_head.equals(Message.MSG_HEAD_STATUS_GENERAL) && m.msg_body.equals(Message.MSG_BODY_LOGIN)){// new user is added to list of online users
						addUser(new User(m.msg_from));
					}
					if(index==-1 && m.msg_head.equals(Message.MSG_HEAD_STATUS_GENERAL) && !m.msg_from.equals(cl.idClient) && m.msg_body.equals(Message.MSG_BODY_LOGIN)){
						cl.sendMessage(new Message(Message.MSG_TYPE_STATUS, cl.idClient, m.msg_from, Message.MSG_HEAD_STATUS_GENERAL, users.get(findUser(cl.idClient)).getStatus(Message.MSG_HEAD_STATUS_GENERAL)));
						cl.sendMessage(new Message(Message.MSG_TYPE_STATUS, cl.idClient, m.msg_from, Message.MSG_HEAD_STATUS_CHAT,  users.get(findUser(cl.idClient)).getStatus(Message.MSG_HEAD_STATUS_CHAT)));
						cl.sendMessage(new Message(Message.MSG_TYPE_STATUS, cl.idClient, m.msg_from, Message.MSG_HEAD_STATUS_BRAINSTORM,  users.get(findUser(cl.idClient)).getStatus(Message.MSG_HEAD_STATUS_BRAINSTORM)));
					}
					if(index!=-1 && m.msg_head.equals(Message.MSG_HEAD_STATUS_GENERAL) && m.msg_body.equals(Message.MSG_BODY_LOGOUT)){
						//removeUser(users.get(index));
						disconnect();
					}
				}
				if(m.msg_type.equals(Message.MSG_TYPE_CHAT)){
					c.parseIncomingMessage(users.get(findUser(m.msg_from)), m);
				}
				if(m.msg_type.equals(Message.MSG_TYPE_BRAINSTORM)){
					b.parseIncomingMessage(users.get(findUser(m.msg_from)), m);
				}
			}
		}
	}
	
	public void stopp(){
		this.runOK = false;
	}

}
