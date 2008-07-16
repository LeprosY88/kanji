package org.abratuhi.mmorpg.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.abratuhi.mmorpg.client.MMORPG_Client;
import org.abratuhi.mmorpg.graphics.MMORPG_GraphicsEngine;
import org.abratuhi.mmorpg.gui.MMORPG_GUI;

public class MMORPG_Game{
	
	public MMORPG_GUI gui;
	public MMORPG_Client client;
	public MMORPG_GraphicsEngine ge;
	
	public MMORPG_Game(String accountName, String accountPassword){
		this.ge = new MMORPG_GraphicsEngine(this);
		this.client = new MMORPG_Client(this, accountName);
		this.gui = new MMORPG_GUI(this);
		
		this.client.start();
		new Thread(this.gui).start();
	}
	
	public static void main(String[] args){
		new MMORPG_Game(args[0], args[1]);
	}

}
