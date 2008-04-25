package gpl.java.abratuhi.src.model;

import java.util.ArrayList;
import java.util.Date;

import com.ringlord.xml.Node;

public class Joke {
	
	public String text = new String();
	public String rate = new String();
	public String date = new String();
	public String id = new String();
	public String author_id = new String();
	public String section_id = new String();
	public ArrayList<String> comments = new ArrayList<String>();
	
	public Joke(String text, String author_id){
		this.text = text;
		this.date = String.valueOf(new Date().getTime());
		this.author_id = author_id;
		this.id = this.author_id + this.date;
	}
	
	public Node toXMLNode(){
		Node res = new Node("joke");
		res.setAttribute("text", this.text);
		res.setAttribute("rate", this.rate);
		res.setAttribute("date", this.date);
		res.setAttribute("id", this.id);
		res.setAttribute("author_id", this.author_id);
		res.setAttribute("section_id", this.section_id);
		for(int i=0; i<comments.size(); i++){
			Node c = new Node("comment");
			c.setAttribute("text", this.comments.get(i));
			res.addChild(c);
		}
		return res;
	}
	
	public void fromXMLNode(Node n){
		this.text = n.getAttributeValue("text");
		this.rate = n.getAttributeValue("rate");
		this.date = n.getAttributeValue("date");
		this.id = n.getAttributeValue("id");
		this.author_id = n.getAttributeValue("author_id");
		this.section_id = n.getAttributeValue("section_id");
		for(int i=0; i<n.getChildCount(); i++){
			this.comments.add(n.getChild("comment", i).getAttributeValue("text"));
		}
	}
	
	/*public static void main(String[] args){
		System.out.println(new Joke("some_text", "some_author_id").toXMLNode().toString());
	}*/

}
