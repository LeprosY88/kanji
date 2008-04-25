/**
 * @author Alexei Bratuhin
 * @licence GPLv2
 */

package gpl.java.abratuhi.src.net.message.prototype;

import java.util.ArrayList;

import gpl.java.abratuhi.src.util.string.StringUtils;

public class Message {
	
	/**/
	public final static String MSG_SEPARATOR = "|";
	public final static String MSG_OK = "MSG_OK";
	
	public final static String MSG_TYPE_CHAT = "MSG_TYPE_CHAT";
	public final static String MSG_TYPE_BRAINSTORM = "MSG_TYPE_BRAINSTORM";
	public final static String MSG_TYPE_STATUS = "MSG_TYPE_STATUS";
	
	public final static String MSG_TO_ALL = "TO_ALL";
	
	/**/
	public final static String MSG_HEAD_STATUS_GENERAL = "MSG_HEAD_STATUS_GENERAL";
	public final static String MSG_HEAD_STATUS_CHAT = "MSG_HEAD_STATUS_CHAT";
	public final static String MSG_HEAD_STATUS_BRAINSTORM = "MSG_HEAD_STATUS_BRAINSTORM";
	
	/* Status */
	/*public final static String MSG_BODY_LOGIN = "MSG_BODY_LOGIN";
	public final static String MSG_BODY_IDLE = "MSG_BODY_IDLE";
	public final static String MSG_BODY_LOGOUT = "MSG_BODY_LOGOUT";
	public final static String MSG_BODY_THEMESUBMITTED = "MSG_BODY_THEMESUBMIT";
	public final static String MSG_BODY_TYPING = "MSG_BODY_TYPING";
	public final static String MSG_BODY_WAITING = "MSG_BODY_WAITING";
	public final static String MSG_BODY_DONE = "MSG_BODY_DONE";*/
	public final static String MSG_BODY_LOGIN = "online";
	public final static String MSG_BODY_IDLE = "idle";
	public final static String MSG_BODY_LOGOUT = "offline";
	public final static String MSG_BODY_THEMESUBMITTED = "theme submitted";
	public final static String MSG_BODY_TYPING = "jokes typing";
	public final static String MSG_BODY_WAITING = "waiting";
	public final static String MSG_BODY_DONE = "done";
	
	/* Chat */
	public final static String MSG_HEAD_CHAT_MESSAGE = "MSG_HEAD_CHAT_MESSAGE";
	
	/* Brainstorm */
	public final static String MSG_HEAD_BRAINSTORM_THEME = "MSG_HEAD_THEME";
	public final static String MSG_HEAD_BRAINSTORM_SHEET = "MSG_HEAD_SHEET";
	
	
	/**/
	public int idSender;
	public int idReceiver;
	
	/**/
	public String msg_type = new String();
	public String msg_from = new String();
	public String msg_to = new String();
	public String msg_head = new String();
	public String msg_body = new String();
	
	/**/
	public Message(){
		
	}
	
	public Message(String t, String h, String b){
		this.msg_type = t;
		this.msg_head = h;
		this.msg_body = b;
	}
	
	public Message(String t, String from, String to, String h, String b){
		this.msg_type = t;
		this.msg_from = from;
		this.msg_to = to;
		this.msg_head = h;
		this.msg_body = b;
	}
	
	/**/
	public String Message2String(){
		return this.msg_type + MSG_SEPARATOR +
				this.msg_from + MSG_SEPARATOR + 
				this.msg_to + MSG_SEPARATOR +
				this.msg_head + MSG_SEPARATOR + 
				this.msg_body;
	}
	public void String2Message(String msg){
		/*int sep1 = msg.indexOf(MSG_SEPARATOR);
		int sep2 = msg.lastIndexOf(MSG_SEPARATOR); //TODO: iterative computation of next separator, likeint sep2 = sep1 + (msg.substring(sep1)).indexOf(MSG_SEPARATOR); 
		this.msg_type = msg.substring(0, sep1);
		this.msg_head = msg.substring(sep1+1, sep2);
		this.msg_body = msg.substring(sep2+1);*/
		ArrayList<String> res = new ArrayList<String>();
		StringUtils.String2ArrayListOfString(msg, res, MSG_SEPARATOR, 5);
		msg_type = res.remove(0);
		msg_from = res.remove(0);
		msg_to = res.remove(0);
		msg_head = res.remove(0);
		msg_body = res.remove(0);
	}
	/*
	public static void main(String[] args){
		Message msg1 = new Message(MSG_TYPE_CHAT, "coiouhkc", "hi all!");
		Message msg2 = new Message();
		
		System.out.println(msg1.Message2String());		
		msg2.String2Message(msg1.Message2String());
		System.out.println(msg2.Message2String());
	}
	*/
	
	/**/
	public void createStandardMessage(String msg_t){
		this.msg_type = msg_t;
	}
	public boolean isStandardMessage(String msg_t){
		if(this.msg_type.equalsIgnoreCase(msg_t)){
			return true;
		}
		return false;
	}

}
