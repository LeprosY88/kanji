package org.abratuhi.mmorpg.chatroom.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.abratuhi.mmorpg.chatroom.controller.ChatRoom_GUIServer_Controller;
import org.abratuhi.mmorpg.chatroom.server.ChatRoom_Server;
import org.abratuhi.mmorpg.chatroom.util.SpringUtilities;

public class ChatRoom_GUIServer extends JFrame{
	
	public ChatRoom_Server chatRoomServer;
	
	public JButton buttonServerUp = new JButton("Up");
	public JButton buttonServerDown = new JButton("Down");
	JButton buttonServerStart = new JButton("Start");
	JButton buttonServerStop =  new JButton("Stop");
	public JButton buttonServerSummary = new JButton("Summary");
	public JButton buttonServerQuit = new JButton("Quit");
	
	public JTextArea textareaServerInfo = new JTextArea();
	
	public ChatRoom_GUIServer(){
		super("ChatRoom GUI Server");
		
		this.chatRoomServer = new ChatRoom_Server();
		
		JPanel buttons = new JPanel(new SpringLayout());
		buttons.setBorder(new TitledBorder("buttons"));
		buttons.add(buttonServerUp);
		buttons.add(buttonServerDown);
		buttons.add(buttonServerSummary);
		buttons.add(buttonServerQuit);
		SpringUtilities.makeCompactGrid(buttons, 1,4, 0,0,0,0);
		
		JPanel info = new JPanel(new SpringLayout());
		info.setBorder(new TitledBorder("info"));
		info.add(textareaServerInfo);
		SpringUtilities.makeCompactGrid(info, 1,1, 0,0,0,0);
		
		ChatRoom_GUIServer_Controller controller = new ChatRoom_GUIServer_Controller(this);
		buttonServerUp.addActionListener(controller);
		buttonServerDown.addActionListener(controller);
		buttonServerSummary.addActionListener(controller);
		buttonServerQuit.addActionListener(controller);
		
		this.getContentPane().setLayout(new SpringLayout());
		this.getContentPane().add(buttons);
		this.getContentPane().add(new JScrollPane(info));
		SpringUtilities.makeCompactGrid(this.getContentPane(), 2, 1, 0, 0, 0, 0);
		
		this.setVisible(true);
		this.setPreferredSize(new Dimension(400,300));
		this.setLocation(new Point(100,100));
		
		this.addWindowListener(controller);
		
		info.setFont(new Font("Courier", Font.PLAIN, 12));
		
		this.pack();
	}
	
	public static void main(String[] args){
		new ChatRoom_GUIServer();
	}
	

}
