package org.abratuhi.mmorpg.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.abratuhi.mmorpg.game.MMORPG_Game;

public class MMORPG_GUI extends JFrame implements Runnable{
	
	MMORPG_Game game;
	
	MMORPG_Main main;
	MMORPG_Minimap map;
	
	boolean runOK = false;
	
	public MMORPG_GUI(MMORPG_Game game){
		super(".::MMORPG_Game::.");
		
		this.game = game;
		this.main = new MMORPG_Main(this);
		
		setRunOK(true);
		
		add(new JScrollPane(main));
		
		setSize(new Dimension(800, 600));
		setVisible(true);
		setLayout(new GridLayout(1,1,0,0));
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				exit();
			}
		});
		
		pack();
	}
	
	synchronized void setRunOK(boolean value){
		this.runOK = value;
	}
	synchronized boolean getRunOK(){
		return this.runOK;
	}

	@Override
	public void run() {
		while(getRunOK()){
			repaint();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop(){
		setRunOK(false);
	}
	
	public void exit(){
		game.client.stopp();
		System.exit(0);
	}

}
