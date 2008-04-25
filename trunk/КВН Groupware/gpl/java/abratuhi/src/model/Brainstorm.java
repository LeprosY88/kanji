package gpl.java.abratuhi.src.model;

import gpl.java.abratuhi.src.net.message.prototype.Client;
import gpl.java.abratuhi.src.net.message.prototype.Message;

import java.util.ArrayList;

public class Brainstorm {
	
	public Client c;
	public ArrayList<String> sheets = new ArrayList<String>();
	public ArrayList<String> themes = new ArrayList<String>();
	public ArrayList<User> users = new ArrayList<User>();
	//public ArrayList<String> status = new ArrayList<String>();
	public String finaltheme = new String();
	public String finalsheet = new String();
	public String reservesheet = new String();
	public int round = 0;
	public boolean isOver = false;
	
	/**/
	public Brainstorm(Client cl){
		this.c = cl;
	}
	public void resetAll(){
		sheets = new ArrayList<String>();
		themes = new ArrayList<String>();
		users = new ArrayList<User>();
		//status = new ArrayList<String>();
		finaltheme = new String();
		finalsheet = new String();
		reservesheet = new String();
		round = 0;
	}
	
	/**/
	public void generateFinalTheme(){
		this.finaltheme = new String();
		for(int i=0; i<this.themes.size(); i++){
			this.finaltheme += this.themes.get(i) + ", ";
		}
	}
	public void generateFinalSheet(){
		this.finalsheet = new String();
		this.finalsheet += "Results:"+"\n"+"--------------"+"\n";
		for(int i=0; i<this.sheets.size(); i++){
			this.finalsheet += this.sheets.get(i) + "\n";
		}
	}
	
	/**/
	public void addUser(User u){
		u.setStatus(Message.MSG_HEAD_STATUS_BRAINSTORM, Message.MSG_BODY_LOGIN);
		users.add(u);
		themes.add(new String(""));
		//status.add(new String(""));
		sheets.add(new String(""));
	}
	public void removeUser(User u){
		u.setStatus(Message.MSG_HEAD_STATUS_BRAINSTORM, Message.MSG_BODY_LOGOUT);
		int index = findUser(u);
		if(index != -1){
			reservesheet = sheets.remove(index);
			users.remove(index);
			//status.remove(index);			
		}
	}
	public int findUser(User u){
		for(int i=0; i<users.size(); i++){
			if(u.equals(users.get(i))){
				return i;
			}
		}
		return -1;
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
	public void submitTheme(User u, String theme){
		int index = findUser(u);
		if(index != -1){
			themes.set(index, theme);
			//status.set(index, Message.MSG_HEAD_BRAINSTORM_THEME);
			users.get(index).setStatus(Message.MSG_HEAD_STATUS_BRAINSTORM, Message.MSG_BODY_THEMESUBMITTED);
		}
		else{
			System.out.println("Error: user not found in brainstorm\t"+u.nickname);
		}
	}
	public void submitSheet(User u, String sheet){
		int index = findUser(u);
		if(index != -1){
			sheets.set(index, sheet);
			//status.set(index, Message.MSG_HEAD_BRAINSTORM_SHEET);
			users.get(index).setStatus(Message.MSG_HEAD_STATUS_BRAINSTORM, Message.MSG_BODY_WAITING);
		}
		else{
			System.out.println("Error: user not found in brainstorm\t"+u.nickname);
		}
	}
	
	/**/
	public boolean startBrainstormPossible(){
		if(users.size() == 0) return false;
		for(int i=0; i<users.size(); i++){
			if(!users.get(i).getStatus(Message.MSG_HEAD_STATUS_BRAINSTORM).equals(Message.MSG_BODY_THEMESUBMITTED)){
				return false;
			}
		}
		return true;
	}
	public boolean changeSheetPossible(){
		if(users.size() == 0) return false;
		for(int i=0; i<users.size(); i++){
			if(!users.get(i).getStatus(Message.MSG_HEAD_STATUS_BRAINSTORM).equals(Message.MSG_BODY_WAITING)){
				return false;
			}
		}
		System.out.println("Brainstorm: Changing sheet possible");
		return true;
	}
	public void changeSheet(){
		/**/
		System.out.println("Brainstorm: Changing sheet, Round "+round);
		/* exchange sheets */
		String tsheet = sheets.get(0);
		for(int i=0; i<sheets.size()-1; i++){
			sheets.set(i, sheets.get(i+1));
		}
		sheets.set(sheets.size()-1, tsheet);
		/* change status */
		for(int i=0; i<users.size(); i++){
			users.get(i).setStatus(Message.MSG_HEAD_STATUS_BRAINSTORM, Message.MSG_BODY_TYPING);
		}
		/* proceed to next round */
		round++;
	}
	public boolean endBrainstormPossible(){
		if(users.size() == 0) return false;
		if(round >= users.size()) {System.out.println("End brainstorm possible"); return true;}
		return false;
	}
	
	/**/
	public void startBrainstorm(){
		generateFinalTheme();
		round++;
		/**/
		for(int i=0; i<users.size(); i++){
			users.get(i).setStatus(Message.MSG_HEAD_STATUS_BRAINSTORM, Message.MSG_BODY_TYPING);
		}
	}
	public void endBrainstrom(){
		/**/
		this.isOver = true;
		generateFinalTheme();
		generateFinalSheet();
		/**/
		for(int i=0; i<users.size(); i++){
			users.get(i).setStatus(Message.MSG_HEAD_STATUS_BRAINSTORM, Message.MSG_BODY_DONE);
		}
		/**/
		//this.round = 0; //for GUI purposes - to enable redrawing
	}
	
	/**/
	public void sendTheme(String th){
		c.sendMessage(new Message(Message.MSG_TYPE_BRAINSTORM, c.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_BRAINSTORM_THEME, th));
	}
	public void sendSheet(String sh){
		c.sendMessage(new Message(Message.MSG_TYPE_BRAINSTORM, c.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_BRAINSTORM_SHEET, sh));
	}
	
	/**/
	public void parseIncomingMessage(User u, Message m){
		if(m.msg_type.equals(Message.MSG_TYPE_BRAINSTORM)){//additional check, is also performed in KVNGroupware
			if(m.msg_head.equals(Message.MSG_HEAD_STATUS_BRAINSTORM)){
				if(m.msg_body.equals(Message.MSG_BODY_LOGIN)){
					addUser(u);
				}
				if(m.msg_body.equals(Message.MSG_BODY_LOGOUT)){
					removeUser(u);
					if(users.size() == 0) resetAll();
				}
				return;
			}
			if(m.msg_head.equals(Message.MSG_HEAD_BRAINSTORM_THEME)){
				submitTheme(u, m.msg_body);
				if(startBrainstormPossible()){
					startBrainstorm();					
				}
				return;
			}
			if(m.msg_head.equals(Message.MSG_HEAD_BRAINSTORM_SHEET)){
				submitSheet(u, m.msg_body);
				if(changeSheetPossible()){
					changeSheet();
					if(endBrainstormPossible()){
						endBrainstrom();
						//return;
					}
				}
				return;
			}
		}
	}

}

	