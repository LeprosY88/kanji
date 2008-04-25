package gpl.java.abratuhi.src.model;

import java.util.ArrayList;
import java.util.Date;

import com.ringlord.xml.Node;

public class Team {
	
	public String id = new String();
	public String name = new String();
	public String from = new String();
	public String date = new String();
	
	public String league = new String();
	
	public ArrayList<User> users = new ArrayList<User>();
	public ArrayList<Joke> jokes = new ArrayList<Joke>();
	
	public Team(String name, String from){
		this.name = name;
		this.from = from;
		this.date = String.valueOf(new Date().getTime());
		this.id = this.name + this.date;
	}
	
	public Node toXMLNode(){
		Node res = new Node("team");
		res.setAttribute("id", this.id);
		res.setAttribute("name", this.name);
		res.setAttribute("from", this.from);
		res.setAttribute("date", this.date);
		res.setAttribute("league", this.league);
		for(int i=0; i<users.size(); i++){
			res.addChild(users.get(i).toXMLNode());
		}
		for(int i=0; i<jokes.size(); i++){
			res.addChild(jokes.get(i).toXMLNode());
		}
		return res;
	}
	
	public void fromXMLNode(Node n){
		this.id = n.getAttributeValue("id");
		this.name = n.getAttributeValue("name");
		this.from = n.getAttributeValue("from");
		this.date = n.getAttributeValue("date");
		this.league = n.getAttributeValue("league");
		for(int i=0; i<n.getChildCount(); i++){
			if(((Node)n.getChild(i)).getName().equals("user")){
				User u = new User();
				u.fromXMLNode((Node)n.getChild(i));
				users.add(u);
			}
			if(((Node)n.getChild(i)).getName().equals("joke")){
				Joke j = new Joke(null, null);
				j.fromXMLNode((Node)n.getChild(i));
				jokes.add(j);
			}
		}
	}

}
