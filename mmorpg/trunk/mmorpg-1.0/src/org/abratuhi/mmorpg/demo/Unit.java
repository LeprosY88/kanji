package org.abratuhi.mmorpg.demo;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Unit{
	
	private Server s;
	public String id;
	public Point pos;
	private Point to;
	double range;
	private double speed;
	private ArrayList<Unit> neighs;
	public ArrayList<Message> incoming;

	public Unit(Server ts, String id, Double range, Double speed){
		this.s = ts;
		this.id = id;
		//id = ++iidd;
		this.pos = new Point((int)(Math.random()*s.fieldDimension.width), (int)(Math.random()*s.fieldDimension.height));
		this.to = new Point((int)(Math.random()*s.fieldDimension.width), (int)(Math.random()*s.fieldDimension.height));
		this.range = range;
		this.speed = speed;
		//range = Math.random()*RANGE;
		//speed = Math.random()*SPEED;
		//pos = new Point(0,0);
		//to = new Point(0,0);
		neighs = new ArrayList<Unit>();
		incoming = new ArrayList<Message>();
	}
	
	public Unit clone(){
		Unit out = new Unit(s, this.id, this.range, this.speed);
		out.pos = new Point(pos);
		out.to = new Point(to);
		out.range = range;
		out.speed = speed;
		/*for(int i=0; i<neighs.size(); i++){
			out.neighs.add(neighs.get(i));
		}
		for(int i=0; i<incoming.size(); i++){
			out.incoming.add(incoming.get(i));
		}*/
		return out;
	}

	public synchronized ArrayList<Message> getIncoming(){
		return incoming;
	}
	public synchronized ArrayList<Unit> getNeighbours(){
		return neighs;
	}

	public void addNeighbour(Unit t){
		if(t.id == this.id) return;
		if(!t.isNeighbour(this)) t.getNeighbours().add(this);
		if(!isNeighbour(t)) getNeighbours().add(t);
	}

	public boolean isNeighbour(Unit t){
		return getNeighbours().contains(t);
	}

	public void updateNeighbour(Unit t){

	}

	public void removeNeighbour(Unit t){		
		if(t.isNeighbour(this)) t.getNeighbours().remove(this);
		if(isNeighbour(t)) getNeighbours().remove(t);
	}

	public void sendMessage(Message m){
		if(m!=null && m.from!=null && m.to!=null && m.from!=m.to && m.to.getIncoming()!=null) {
			synchronized(m.to.getIncoming()){
				m.to.getIncoming().add(m);
			}
			s.nMessages++;
		}
	}

	public void broadcast(){
		for(int i=0; i<s.getUnits().size(); i++){
			if(!s.getUnits().get(i).id.equals(this.id)){
				sendMessage(new Message(this, s.getUnits().get(i), 0, 0, null));
			}
		}
	}
	public void forwardTo(Message m, Unit neighbour){
		Message msgg = m.clone();
		msgg.to = neighbour;
		msgg.rhops--; 
		sendMessage(msgg);
	}

	public void proceedMessage(Message msg){
		//
		if(msg == null) return;
		// forwarding as prelast step in forwarding chain
		if(msg.rhops==1 && getNeighbours().size()>0){
			for(int i=0; i<getNeighbours().size(); i++){
				if(dist(msg.from, getNeighbours().get(i)) < this.range){
					forwardTo(msg, getNeighbours().get(i));
				}
			}
		}
		// forwarding as middle step in forwarding chain
		if(msg.rhops>1 && getNeighbours().size()>0){
			for(int i=0; i<getNeighbours().size(); i++){
				if(Math.random()<msg.probabilityToBeForwarded()){
					forwardTo(msg, getNeighbours().get(i));
				}
			}
		}
		// managing neighbourlist
		if( ! (dist(msg.from, msg.to) < s.range)){
			if(isNeighbour(msg.from)){
				if(s.PROBABILITY_ON){
					Double rRandom = Math.random();
					Double rProbability = probabilityToBeRemoved();
					if(rRandom < rProbability) removeNeighbour(msg.from);
				}
				else{
					removeNeighbour(msg.from);
				}
			}
			else{
				if(s.PROBABILITY_ON){
					Double aRandom = Math.random();
					Double aProbability = probabilityToBeAdded();
					if(aRandom < aProbability) addNeighbour(msg.from);
				}
			}
		}
		else{
			if(isNeighbour(msg.from)) updateNeighbour(msg.from);
			else{
				addNeighbour(msg.from);
			}
		}

	}

	public String toMyString(){
		//return this.toString().substring(this.toString().indexOf("@"));
		return "";
	}

	public double dist(Unit t){
		return pos.distance(t.pos);
	}
	
	public static double dist(Unit t1, Unit t2){
		return t1.pos.distance(t2.pos);
	}

	public void draw(Graphics2D g){
		int eps = 3;
		//g.drawOval(pos.x-eps, pos.y-eps, 2*eps, 2*eps);
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		g.fillOval(pos.x-eps, pos.y-eps, 2*eps, 2*eps);
		//g.drawString(this.toString().substring(this.toString().indexOf("@")), pos.x, pos.y);
		g.drawString(toMyString()+"("+getNeighbours().size()+")", pos.x, pos.y);
		for(int i=0; i<getNeighbours().size(); i++){
			Unit t = getNeighbours().get(i);
			if(!t.equals(this)){
				g.drawLine(pos.x, pos.y, t.pos.x, t.pos.y);
				g.setFont(new Font("Arial", Font.PLAIN, 8));
				g.drawString(String.valueOf((int) dist(this, t)), t.pos.x/2+pos.x/2, t.pos.y/2+pos.y/2);
			}
		}
	}

	public void move(Point to){
		this.to = new Point(to);
	}

	public Point generateRandomPoint(Dimension r){
		//System.out.println("new random point generated");
		return new Point((int)(Math.random()*r.width), (int)(Math.random()*r.height));
	}
	
	public double probabilityToBeRemoved(){
		return 1.0 - 1.0/Math.max(probSteps()-(s.currentStep-s.lastBuildMSTStep), 1);
	}
	public double probabilityToBeAdded(){
		return 1.0 - 1.0/Math.max(probSteps()-(s.currentStep-s.lastBuildMSTStep), 1);
	}
	public Integer probSteps(){
		Double r = (double)(s.fieldDimension.width) / s.fieldDimension.height;
		Double sq = (double) s.fieldDimension.width * s.fieldDimension.height;
		Double psq = sq / s.nUnits;
		Double y = Math.sqrt(psq/(1+Math.pow(r, 2)));
		Double x = y * r;
		Double mr = Math.sqrt(x*x + y*y);
		Double dr = mr - this.range;
		Integer t = (int) (dr / this.speed);
		return t;
	}

	public void step(){
		// #if already reached destination point -> select new destination
		if(pos.distance(to)<1) move(generateRandomPoint(s.fieldDimension));
		
		// #move towards destination with own speed
		if(pos.x != to.x){
			if(pos.x < to.x) pos.x += Math.min(speed, to.x-pos.x);
			else pos.x -= Math.min(speed, pos.x-to.x);
		}
		if(pos.y != to.y){
			if(pos.y < to.y) pos.y += Math.min(speed, to.y-pos.y);
			else pos.y -= Math.min(speed, pos.y-to.y);
		}
		
		// #proceed incoming
		for(int i=0; i<getIncoming().size(); i++){
			Message msg = getIncoming().remove(0);
			proceedMessage(msg);
		}
		
		// #send outgoing
		// broadcast
		if(s.BROADCAST) broadcast();
		
		// neighcast
		if(s.MYALGO){
			if(!s.isConnected()) s.buildMST();
			for(int i=0; i<neighs.size(); i++){
				Message msg = new Message(this, neighs.get(i), 1, 1, null);
				sendMessage(msg);
			}
		}
		
		// neighcast with closed world assumption observation
		if(s.MYALGO_CLOSEDWORLD){
			if(!s.isConnected()) s.buildMST();
			for(int i=0; i<neighs.size(); i++){
				Integer nhopz = Math.max(1, (int) Math.log(s.nUnits)-neighs.size());
				Message msg = new Message(this, neighs.get(i), nhopz, nhopz, null);
				sendMessage(msg);
			}
		}
	}
	
	public void step1(){
		// #if already reached destination point -> select new destination
		if(pos.distance(to)<1) move(generateRandomPoint(s.fieldDimension));
		
		// #move towards destination with own speed
		if(pos.x != to.x){
			if(pos.x < to.x) pos.x += Math.min(speed, to.x-pos.x);
			else pos.x -= Math.min(speed, pos.x-to.x);
		}
		if(pos.y != to.y){
			if(pos.y < to.y) pos.y += Math.min(speed, to.y-pos.y);
			else pos.y -= Math.min(speed, pos.y-to.y);
		}
	}
	public void step2(){
		// #proceed incoming
		for(int i=0; i<getIncoming().size(); i++){
			Message msg = getIncoming().remove(0);
			proceedMessage(msg);
		}
	}
	public void step3(){
		// #send outgoing
		// broadcast
		if(s.BROADCAST) broadcast();
		
		// neighcast
		if(s.MYALGO){
			if(!s.isConnected()) s.buildMST();
			for(int i=0; i<neighs.size(); i++){
				Message msg = new Message(this, neighs.get(i), 1, 1, null);
				sendMessage(msg);
			}
		}
		
		// neighcast with closed world assumption observation
		if(s.MYALGO_CLOSEDWORLD){
			if(!s.isConnected()) s.buildMST();
			for(int i=0; i<neighs.size(); i++){
				Integer nhopz = Math.max(1, (int) Math.log(s.nUnits)-neighs.size());
				Message msg = new Message(this, neighs.get(i), nhopz, nhopz, null);
				sendMessage(msg);
			}
		}
	}
}
