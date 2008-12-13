package org.abratuhi.mmorpg.demo;

public class Message {
	
	public Unit from;
	public Unit to;
	
	public Integer nhops;
	public Integer rhops;
	public String cmd;
	
	public Message(Unit f, Unit t, Integer n, Integer r, String c){
		from = f;
		to = t;
		nhops = n;
		rhops = r;
		cmd = c;
	}
	
	
	public Message clone(){
		return new Message(this.from, this.to, this.nhops, this.rhops, this.cmd);
	}
	
	public double probabilityToBeForwarded(){
		return rhops / nhops;
	}

}
