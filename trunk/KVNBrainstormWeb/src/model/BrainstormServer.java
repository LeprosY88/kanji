package src.model;

import java.util.ArrayList;
import java.util.Date;

import src.util.SheetLog;

public class BrainstormServer {
	
	public ArrayList<String> users = new ArrayList<String>();
	public ArrayList<Integer> status = new ArrayList<Integer>();
	public ArrayList<String> themes = new ArrayList<String>();
	public ArrayList<String> sheets = new ArrayList<String>();
	public String finalsheet = new String();
	public int users_number = 0;
	public int curr_stage = 0;
	public int curr_brainstorm_stage = 0;
	
	public BrainstormServer(){
		resetServer();
	}
	
	public boolean loginUser(String name){
		for(int i=0; i<users.size(); i++){
			if(name.equals(users.get(i))) return false;
		}
		users.add(name);
		status.add(new Integer(1));
		users_number++;
		curr_stage=1;
		return true;
	}
	
	public boolean submitTheme(String name, String theme){
		for(int i=0; i<users.size(); i++){
			if(name.equals(users.get(i))) {
				//themes.set(i, theme);
				themes.add(theme);
				status.set(i, new Integer(2));
				break;
			}
		}
		return true;
	}
	
	public String getCollectiveTheme(){
		String msg = new String();
		for(int i=0; i<themes.size()-1; i++){
			msg+=themes.get(i)+", ";
		}
		msg+=themes.get(themes.size()-1);
		return msg;
	}
	
	public boolean initSheets(){
		for(int i=0; i<users.size(); i++){
			sheets.add(new String());
		}
		return true;
	}
	
	public boolean changeSheets(){
		/**/
		String tsheet = sheets.get(0);
		for(int i=0; i<sheets.size()-1; i++){
			sheets.set(i, sheets.get(i+1));
		}
		sheets.set(sheets.size()-1, tsheet);
		curr_brainstorm_stage++;
		/* backup */
		String tomcatpath = SheetLog.getTomcatDir();
		String backuppath = tomcatpath+"\\webapps\\abratuhi\\WEB-INF\\classes\\backup\\";
		Date date = new Date();
		String file = String.valueOf(date.getTime());
		SheetLog.writeToFile(backuppath+file, sheets);
		/**/
		return true;
	}
	
	public boolean finalizeSheets(){
		finalsheet = new String();
		for(int i=0; i<sheets.size(); i++){
			finalsheet+="Sheet " + i + ":\n"+sheets.get(i)+"\n";
		}
		return true;
	}
	
	public boolean resetServer(){
		users = new ArrayList<String>();
		themes = new ArrayList<String>();
		sheets = new ArrayList<String>();
		finalsheet = new String();
		users_number = 0;
		curr_stage = 0;
		curr_brainstorm_stage = 0;
		status = new ArrayList<Integer>();
		return true;
	}
	
	public int getIndex(String name){
		for(int i=0; i<users.size(); i++){
			if(name.equals(users.get(i))) return i;
		}
		return -1;
	}
	
	public int getStatus(){
		int t = status.get(0);
		for(int i=1; i<status.size(); i++){
			if(t!=status.get(i)) return -1;
		}
		return t;
	}
	public void setStatus(int s){
		for(int i=0; i<users.size(); i++){
			status.set(i,s);
		}
	}

}
