package org.abratuhi.mmorpg.game;

import org.abratuhi.mmorpg.client.MMORPG_Client;
import org.abratuhi.mmorpg.graphics.MMORPG_GraphicsEngine;
import org.abratuhi.mmorpg.gui.MMORPG_GUI;

/**
 * Wrapper class, containig references to all 'main' parts of the application: gui, netclient, graphics engine.
 * 
 * @author Alexei Bratuhin
 *
 */
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
	
	/**
	 * 
	 * @param args	<ul><li>args[0] - login</li>
	 * 					<li>args[1] - password</li></ul>
	 */
	public static void main(String[] args){
		new MMORPG_Game(args[0], args[1]);
	}

}
