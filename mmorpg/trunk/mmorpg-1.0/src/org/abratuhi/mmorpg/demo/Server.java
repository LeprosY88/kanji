package org.abratuhi.mmorpg.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Server /*extends JPanel*/ implements Runnable{
	public boolean HISTORY_ON = false;
	public boolean CHECK_ON = false;
	public boolean GRAPHICS_ON = false;
	public long DELAY = 200;
	
	boolean BROADCAST = false;
	boolean MYALGO = false;
	boolean MYALGO_CLOSEDWORLD = false;
	
	public String algorithm;
	public boolean PROBABILITY_ON=false;
	public Point fieldLocation = new Point(50,50);
	public Dimension fieldDimension = new Dimension(1100, 500);
	
	ArrayList<Unit> units;
	private ArrayList<ArrayList<Unit>> history;
	private Integer[][] consistency;
	public Integer currentStep = 0;
	public Integer maxSteps;
	public Integer nUnits = 30;
	public Double range;
	public Double speed;
	public Integer lastBuildMSTStep = 0;
	
	OutputStreamWriter osw = null;
	
	Integer nMessages = 0;
	Integer nConnectedChecks = 0;
	Integer nBuildMST = 0;
	Integer[] nErrors;
	Integer[] nBugs;
	
	JFrame illustrationFrame = null;
	JFrame controlFrame = null;
	
	boolean runOK = false;
	
	Thread thread = null;
	
	public Server(String algo,
					boolean probalgo,
					Integer maxsteps,
					Point location, 
					Dimension dimension, 
					Integer nunitz, 
					Double rangeCoeff, 
					Double speedCoeff,
					boolean historyOn,
					boolean checkOn,
					boolean graphicsOn){
		super();
		
		switchAlgorithm(algo);
		this.algorithm = algo;
		this.PROBABILITY_ON = probalgo;
		this.maxSteps = maxsteps;
		this.fieldLocation = location;
		this.fieldDimension = dimension;
		this.nUnits = nunitz;
		//this.range = rangeCoeff * Math.min(dimension.width, dimension.height);
		this.range = rangeCoeff;
		//this.speed = this.range * speedCoeff;
		this.speed = speedCoeff;
		this.consistency = new Integer[nUnits][nUnits];
		this.nErrors = new Integer[maxSteps];
		this.nBugs = new Integer[maxSteps];
		
		this.HISTORY_ON = historyOn;
		this.CHECK_ON = checkOn;
		this.GRAPHICS_ON = graphicsOn;
		
		units = new ArrayList<Unit>();
		if(HISTORY_ON) history = new ArrayList<ArrayList<Unit>>();
		nUnits = nunitz;
		
		for(int i=0; i<nUnits; i++){
			for(int j=0; j<nUnits; j++){
				consistency[i][j] = -1;
			}
		}
		
		for(int i=0; i<maxSteps; i++){
			nErrors[i] = 0;
			nBugs[i] = 0;
		}
		
		for(int i=0; i<nUnits; i++){
			Unit t = new Unit(this, String.valueOf(i), this.range, this.speed);
			units.add(t);
		}
		
	}
	
	public synchronized ArrayList<Unit> getUnits(){
		return units;
	}
	
	synchronized boolean checkRunOK(){
		return (runOK && (currentStep<maxSteps));
	}
	synchronized void setRunOK(boolean r){
		this.runOK = r;
	}
	
	boolean createLog(){
		String filename = 	algorithm+"!"+
							PROBABILITY_ON+"!"+
							fieldDimension.width+"!"+
							fieldDimension.height+"!"+
							nUnits+"!"+
							maxSteps+"!"+
							range.intValue()+"!"+
							speed.intValue()+"!"+
							".mmorpglog";
		try {
			new FileInputStream(filename);
			System.out.println("File already exists:\t"+filename);
			return false;
		} catch (FileNotFoundException e1) {
			try {
				osw = new OutputStreamWriter(new FileOutputStream(new File(filename)));
				System.out.println("Proceeding configuration:\t"+filename+";\t"+Thread.currentThread().getName());
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	void writeLog(String message){
		try {
			osw.write(message+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void writeLog(String message, Integer ind1, Integer ind2, Integer step){
		try {
			osw.write(message+", "+ind1+", "+ind2+", "+step+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void closeLog(){
		try {
			osw.flush();
			osw.close();
			osw = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void switchAlgorithm(String algo){
		BROADCAST = false;
		MYALGO = false;
		MYALGO_CLOSEDWORLD = false;
		if(algo.equals("broadcast")) BROADCAST = true;
		if(algo.equals("neighbourcast")) MYALGO = true;
		if(algo.equals("neighbourcast_cwa")) MYALGO_CLOSEDWORLD = true;
	}
	
	public synchronized void buildMST(){
		//Prim2
		Integer lastAddedToMSTIndex = 0;
		Integer candidateToMSTIndex = 0;
		Double maxDistance = (double) fieldDimension.height+fieldDimension.width;
		Integer[] isInMST = new Integer[units.size()];
		Integer[] MSTNeighbour = new Integer[units.size()];
		Double[] minDistanceToMST = new Double[units.size()];

		
		// start form 0
		lastAddedToMSTIndex = 0;
		
		for(int i=0; i<minDistanceToMST.length; i++){
			isInMST[i] = 0;
			MSTNeighbour[i] = lastAddedToMSTIndex;
			minDistanceToMST[i] = units.get(lastAddedToMSTIndex).pos.distance(units.get(i).pos);
		}
		
		isInMST[lastAddedToMSTIndex] = 1;
		MSTNeighbour[lastAddedToMSTIndex] = -1;
		minDistanceToMST[lastAddedToMSTIndex] = 0.0;
		
		// n-1 times
		for(int i=1; i<minDistanceToMST.length; i++){
			
			// check whether distance minimal to last node added to mst OR to mst in previous step
			for(int j=0; j<minDistanceToMST.length; j++){
				Double currentDistance = units.get(lastAddedToMSTIndex).pos.distance(units.get(j).pos);
				if(currentDistance < minDistanceToMST[j]) {
					minDistanceToMST[j] = currentDistance;
					MSTNeighbour[j] = lastAddedToMSTIndex;
				}
			}
			
			// find node with  minimal distance to mst
			Double minimalDistance = maxDistance;
			for(int j=0; j<minDistanceToMST.length; j++){
				if(isInMST[j]==0 && minDistanceToMST[j]<minimalDistance) {
					minimalDistance = minDistanceToMST[j];
					candidateToMSTIndex = j;
				}
			}
			
			// update mst
			units.get(candidateToMSTIndex).addNeighbour(units.get(MSTNeighbour[candidateToMSTIndex]));
			units.get(MSTNeighbour[candidateToMSTIndex]).addNeighbour(units.get(candidateToMSTIndex));
			
			lastAddedToMSTIndex = candidateToMSTIndex;
			isInMST[lastAddedToMSTIndex] = 1;
			MSTNeighbour[lastAddedToMSTIndex] = -1;
			minDistanceToMST[lastAddedToMSTIndex] = 0.0;
		}
		
		//
		lastBuildMSTStep = currentStep;
		nBuildMST++;
	}
	
	public synchronized boolean isConnected(){
		// init phase
		ArrayList<Unit> newunits = new ArrayList<Unit>();
		ArrayList<Unit> queue = new ArrayList<Unit>();
		Unit init = units.get((int) (Math.random()*units.size()));
		queue.add(init);
		
		// proceed bfs
		while(queue.size()>0){
			Unit current = queue.remove(0);
			if(!newunits.contains(current)){
				newunits.add(current);
				for(int i=0; i<current.getNeighbours().size(); i++){
					if(!newunits.contains(current.getNeighbours().get(i)) &&
							!queue.contains(current.getNeighbours().get(i)))
						queue.add(current.getNeighbours().get(i));
				}
			}
		}
		
		//
		nConnectedChecks++;
		// conclude
		if(newunits.size() < units.size()) return false;
		else return true;
	}
	
	public void checkConsistency(){
		for(int i=0; i<getUnits().size(); i++){
			for(int j=0; j<getUnits().size(); j++){
				if(i != j){
					Unit unitI = getUnits().get(i);
					Unit unitJ = getUnits().get(j);
					if(Unit.dist(unitI, unitJ) < this.range &&
							!unitI.getNeighbours().contains(unitJ) ){
						if(consistency[i][j] == -1) {
							consistency[i][j] = currentStep;
						}
					}
					else{
						if(consistency[i][j] != -1) {
							Integer correctationGap = currentStep - consistency[i][j];
							consistency[i][j] = -1;
							nErrors[correctationGap-1]++;
						}
					}
				}
			}
		}
	}
	public void checkBugs(){
		for(int i=0; i<getUnits().size(); i++){
			for(int j=0; j<getUnits().size(); j++){
				if(consistency[i][j] != -1) {
					Integer correctationGap = currentStep - consistency[i][j];
					consistency[i][j] = -1;
					nBugs[correctationGap-1]++;
				}
			}
		}
	}
	
	public String connectivity(){
		String out = new String();
		for(int i=0; i<getUnits().size(); i++){
			out += getUnits().get(i).id+": ";
			for(int j=0; j<getUnits().get(i).getNeighbours().size(); j++){
				out += getUnits().get(i).getNeighbours().get(j).id+", ";
			}
			out += "\n";
		}
		return out;
	}
	
	public synchronized ArrayList<Unit> snapshot(){
		ArrayList<Unit> out = new ArrayList<Unit>();
		for(int i=0; i<getUnits().size(); i++){
			out.add(getUnits().get(i).clone());
		}
		for(int i=0; i<getUnits().size(); i++){
			for(int j=0; j<getUnits().get(i).getNeighbours().size(); j++){
				int i1 = getUnits().indexOf(getUnits().get(i).getNeighbours().get(j));
				out.get(i).getNeighbours().add(out.get(i1));
			}
		}
		for(int i=0; i<getUnits().size(); i++){
			for(int j=0; j<getUnits().get(i).getIncoming().size(); j++){
				Message t = getUnits().get(i).getIncoming().get(j);
				int i1 = getUnits().indexOf(t.from);
				int i2 = getUnits().indexOf(t.to);
				out.get(i).getIncoming().add(new Message(out.get(i1), out.get(i2), t.nhops, 0, t.cmd));
			}
		}
		return out;
	}
	
	public void step(){
		/*for(int i=0; i<units.size(); i++){
			units.get(i).step();
		}*/
		for(int i=0; i<units.size(); i++){
			units.get(i).step1();
		}
		for(int i=0; i<units.size(); i++){
			units.get(i).step2();
		}
		for(int i=0; i<units.size(); i++){
			units.get(i).step3();
		}
		
		if(HISTORY_ON) history.add(snapshot());
		if(CHECK_ON) checkConsistency();
		currentStep++;
		/*repaint();*/
	}
	
	public void unstep(){
		if(HISTORY_ON) if(history.size()>0) units = history.remove(history.size()-1);
		currentStep--;
		/*repaint();*/
	}

	public void run() {
		if(CHECK_ON && createLog()){
			writeLog("*** MMORPG NETWORK COMMUNICATION DEMO LOG ***");
			writeLog("*********************************************");
			writeLog("Algorithm:\t"+algorithm);
			writeLog("with Probability Heuristics:\t"+PROBABILITY_ON);
			writeLog("Dimension:\t"+fieldDimension.width+"x"+fieldDimension.height);
			writeLog("# Nodes:\t"+nUnits);
			writeLog("# Steps:\t"+maxSteps);
			writeLog("Range:\t"+range.intValue());
			writeLog("Speed:\t"+speed.intValue());
			writeLog("******************");
		}

		if(GRAPHICS_ON && illustrationFrame==null){
			illustrationFrame = new GUIServer(this).createFrame();
			controlFrame = new GUIController(this).createFrame();
		}

		setRunOK(true);
		while(checkRunOK()){
			step();
			if(GRAPHICS_ON && illustrationFrame!=null) illustrationFrame.repaint();
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(CHECK_ON) checkBugs();

		if(GRAPHICS_ON && illustrationFrame!=null){
			//illustrationFrame.dispose();
			//controlFrame.dispose();
		}

		if(CHECK_ON && osw != null){
			writeLog("******************");
			writeLog("# Messages:\t"+nMessages+"/"+(nUnits-1)*(nUnits)*maxSteps);
			writeLog("# Neighbourcast:\t"+roundToSignsAfterComma(Double.valueOf(nMessages)/(nUnits)/maxSteps, (int)Math.log10(nUnits) +1)+"/"+(nUnits-1));
			writeLog("# isConnected Checks:\t"+nConnectedChecks);
			writeLog("# BuildMST:\t"+nBuildMST);
			for(int i=0; i<maxSteps; i++){
				if(nErrors[i] > 0){
					writeLog("# errors, corrected in "+(i+1)+":\t"+nErrors[i]);
				}
			}
			for(int i=0; i<maxSteps; i++){
				if(nBugs[i] > 0){
					writeLog("# bugs, not corrected in "+(i+1)+":\t"+nBugs[i]);
				}
			}
			writeLog("******************");
			closeLog();
		}
	}
	
	public void play(){
		thread = new Thread(this);
		thread.start();
	}
	
	public void pause(){
		setRunOK(false);
	}
	
	static Double roundToSignsAfterComma(Double d, Integer p){
		Double dp = d * Math.pow(10, p);
		Double d2 = Double.valueOf(dp.intValue());
		return d2/Math.pow(10, p);
	}
	
	public static void main(String[] args){
		Integer[] arrayNUnits = new Integer[]{100};
		Integer[] arrayDims = new Integer[]{1000};
		Double[] arraySpeedCoeffs = new Double[]{100.0, 90.0, 80.0, 70.0, 60.0, 50.0};
		Double[] arrayRangeCoeffs = new Double[]{1.0, 2.0, 3.0};
		String[] arrayAlgorithms = new String[]{"neighbourcast", "neighbourcast_cwa"};
		boolean[] arrayAlgorithmProbabilityModifiers = new boolean[]{true,false};
		Integer[] arrayMaxsteps = new Integer[]{1000};
		
		
		Server s1 = new Server(	"neighbourcast",
				false,
				10000,
				new Point(50,50),
				new Dimension(800, 600),
				100,
				50.0,
				1.0,
				true,
				false,
				true);

		new Thread(s1).start();
		
		/*ArrayList<Thread> threads = new ArrayList<Thread>();
		Integer maxThreads = 4;
		
		for(int i=0; i<arrayAlgorithms.length; i++){
		for(int b=0; b<arrayAlgorithmProbabilityModifiers.length;b++){
		for(int j=0; j<arrayMaxsteps.length; j++){
		for(int k=0; k<arrayDims.length; k++){
		for(int m=0; m<arrayNUnits.length; m++){
		for(int n=0; n<arrayRangeCoeffs.length; n++){
		for(int p=0; p<arraySpeedCoeffs.length; p++){
			Server s1 = new Server(	arrayAlgorithms[i],
					arrayAlgorithmProbabilityModifiers[b],
					arrayMaxsteps[j],
					new Point(50,50),
					new Dimension(arrayDims[k], arrayDims[k]),
					arrayNUnits[m],
					arrayRangeCoeffs[n],
					arraySpeedCoeffs[p],
					false,
					true,
					false);

			Thread cThread = new Thread(s1);
			cThread.start();
			threads.add(cThread);
			
			while(threads.size()>=maxThreads){
				for(int z=0; z<threads.size(); z++){
					Thread zThread = threads.get(z);
					if(!zThread.isAlive()){
						threads.remove(zThread);
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}}}}}}}*/
		
	}

}
