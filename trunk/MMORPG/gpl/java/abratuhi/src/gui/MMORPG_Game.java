package gpl.java.abratuhi.src.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class MMORPG_Game extends JFrame{
	private final static boolean FULL_GUI = false;
	
	MMORPG_Main main;
	MMORPG_Minimap map;
	MMORPG_Chat chat;
	
	public MMORPG_Game(String nick){
		super(".::MMORPG_Game::.");
		setSize(new Dimension(800, 600));
		//setLocation(new Point(200, 100));
		//setResizable(true);
		setVisible(true);
		
		main = new MMORPG_Main(nick);
		if(FULL_GUI) map = new MMORPG_Minimap(main.hero, main.heroes);
		if(FULL_GUI) chat = new MMORPG_Chat(main.client);
		
		//setLayout(new BorderLayout());
		if(FULL_GUI) setLayout(new GridLayout(2,2,0,0));
		else setLayout(new GridLayout(1,1,0,0));
		add(new JScrollPane(main));
		if(FULL_GUI) add(new JScrollPane(map));
		if(FULL_GUI) add(new JScrollPane(chat));
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				main.client.stop();
				main.client.disconnect();
				//main.runOK = false;
				//map.runOK = false;
				//chat.runOK = false;
				System.exit(0);
			}
		});
		
		new Thread(main).start();
		if(FULL_GUI) new Thread(map).start();
		if(FULL_GUI) new Thread(chat).start();
		
		pack();
	}

}
