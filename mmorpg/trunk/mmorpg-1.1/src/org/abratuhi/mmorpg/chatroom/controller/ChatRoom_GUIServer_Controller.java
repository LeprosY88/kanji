package org.abratuhi.mmorpg.chatroom.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.abratuhi.mmorpg.chatroom.gui.ChatRoom_GUIServer;

public class ChatRoom_GUIServer_Controller implements ActionListener, WindowListener{
	
	ChatRoom_GUIServer guiChatRoomServer;
	
	public ChatRoom_GUIServer_Controller(ChatRoom_GUIServer s){
		this.guiChatRoomServer = s;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if(cmd.equals(guiChatRoomServer.buttonServerUp.getActionCommand())){
			guiChatRoomServer.chatRoomServer.server.up(9001);
			guiChatRoomServer.chatRoomServer.server.thread = new Thread(guiChatRoomServer.chatRoomServer.server);
			guiChatRoomServer.chatRoomServer.server.thread.start();
		}
		if(cmd.equals(guiChatRoomServer.buttonServerDown.getActionCommand())){
			guiChatRoomServer.chatRoomServer.server.stop();
			guiChatRoomServer.chatRoomServer.server.down();
		}
		if(cmd.equals(guiChatRoomServer.buttonServerSummary.getActionCommand())){
			guiChatRoomServer.textareaServerInfo.setText(guiChatRoomServer.chatRoomServer.server.getSummary());
		}
		if(cmd.equals(guiChatRoomServer.buttonServerQuit.getActionCommand())){
			System.exit(0);
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {System.exit(0);}
	@Override
	public void windowClosing(WindowEvent e) {System.exit(0);}
	@Override
	public void windowDeactivated(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowOpened(WindowEvent e) {}

}
