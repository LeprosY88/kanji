/**
 * @author Alexei Bratuhin
 * @licence GPLv2
 */

package gpl.java.abratuhi.src.model;

import gpl.java.abratuhi.src.net.message.prototype.Message;

import java.awt.Color;
import java.awt.Font;

import com.ringlord.xml.Node;

public class User {
	
	public final static String prefs = "preferences.xml";
	
	//public static Integer currNumber = 0;
	
	public String[][] stats = { {Message.MSG_HEAD_STATUS_GENERAL, ""},
								{Message.MSG_HEAD_STATUS_CHAT, ""},
								{Message.MSG_HEAD_STATUS_BRAINSTORM, ""}	};
	
	//public String id;
	public String name = new String();
	public String surname = new String();
	public String nickname = new String();//must be unique
	public String alias = new String();
	public String email = new String();
	public String homedir = new String();
	
	public Font font_menu = new Font("Times New Roman", Font.PLAIN, 10);
	public Font font_chat = new Font("Courier", Font.PLAIN, 10);
	public Font font_main = new Font("Arial", Font.PLAIN, 12);
	
	public Color chat_color = Color.black;
	
	public User(){
		
	}
	
	public User(String nick){
		this.nickname = nick;
	}
	
	public void printUserInfo(){
		//System.out.println("id = " + id);
		System.out.println("name = " + name);
		System.out.println("surname = " + surname);
		System.out.println("nichname = " + nickname);
		System.out.println("alias = " + alias);
		System.out.println("email = " + email);
		System.out.println("homedir = " + homedir);
	}
	
	public String getStatus(String head){
		int index = -1;
		for(int i=0; i<stats.length; i++){
			if(head.equals(stats[i][0])){
				index = i;
				break;
			}
		}
		if(index != -1){
			return stats[index][1];
		}
		return null;
	}
	public void setStatus(String head, String st){
		int index = -1;
		for(int i=0; i<stats.length; i++){
			if(head.equals(stats[i][0])){
				index = i;
				break;
			}
		}
		if(index != -1){
			stats[index][1] = st;
		}
	}
	
	public Node toXMLNode(){
		return null; //TODO:
	}
	
	public void fromXMLNode(Node n){
		
	}

}
