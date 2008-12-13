package org.abratuhi.mmorpg.chatroom.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.abratuhi.mmorpg.chatroom.gui.ChatRoom_GUIClient;
import org.abratuhi.mmorpg.chatroom.net.C_Client;
import org.abratuhi.mmorpg.chatroom.net.MessageUtil;

public class ChatRoom_GUIClient_Controller implements ActionListener, WindowListener, ListSelectionListener, MouseListener, KeyListener{
	
	ChatRoom_GUIClient guiChatroomClient;
	
	public ChatRoom_GUIClient_Controller(ChatRoom_GUIClient cl){
		this.guiChatroomClient = cl;
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		
	}

	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {
		guiChatroomClient.exit();
	}
	@Override
	public void windowClosing(WindowEvent e) {
		guiChatroomClient.exit();
	}
	@Override
	public void windowDeactivated(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(e.getSource().equals(guiChatroomClient.jlist_chatrooms)){
			String roomSelected = (String) guiChatroomClient.jlist_chatrooms.getSelectedValue();
			if(	roomSelected != null && 
				roomSelected != "" && 
				roomSelected.equals(guiChatroomClient.chatroomClient.activeChatroom)){
				C_Client netcl = guiChatroomClient.chatroomClient.netclient;
				netcl.sendMessage(MessageUtil.createReqUpdateRoomMessage(netcl, roomSelected));
				guiChatroomClient.updateUsers();
			}
			
			if(roomSelected != null && 
					roomSelected != ""	) guiChatroomClient.chatroomClient.activeChatroom = roomSelected;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {
		boolean isEnter = (Character.codePointAt(String.valueOf(e.getKeyChar()), 0) == 10) ? true : false;
		boolean isCTRLModified = (e.getModifiers() == KeyEvent.CTRL_MASK) ? true : false;
		
		if(isEnter && isCTRLModified){
			String roomName = guiChatroomClient.chatroomClient.activeChatroom;
			String messageText = guiChatroomClient.textarea_message.getText();
			guiChatroomClient.textarea_message.setText("");
			
			if(messageText.startsWith("\\")){	//it's a command
				String[] command = messageText.split(" ");
				if(command[0].equals("\\reconnect")){
					String username = command[1];
					String userpass = command[2];
					guiChatroomClient.chatroomClient.startconnect(username, userpass);
				}
				if(command[0].equals("\\createroom")){
					guiChatroomClient.chatroomClient.createRoom(command[1]);
				}
				if(command[0].equals("\\deleteroom")){
					guiChatroomClient.chatroomClient.deleteRoom(command[1]);
				}
				if(command[0].equals("\\joinroom")){
					guiChatroomClient.chatroomClient.joinRoom(command[1]);
				}
				if(command[0].equals("\\leaveroom")){
					guiChatroomClient.chatroomClient.leaveRoom(command[1]);
				}
				if(command[0].equals("\\quit") || command[0].equals("\\exit")){
					guiChatroomClient.exit();
				}
			}
			else{	// it's a text message
				guiChatroomClient.chatroomClient.send(roomName, messageText);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
            int index = guiChatroomClient.jlist_chatrooms.locationToIndex(e.getPoint());
            String roomSelected = (String) guiChatroomClient.jlist_chatrooms.getModel().getElementAt(index);
			if(	roomSelected != null && 
				roomSelected != "" && 
				roomSelected.equals(guiChatroomClient.chatroomClient.activeChatroom)){
				C_Client netcl = guiChatroomClient.chatroomClient.netclient;
				netcl.sendMessage(MessageUtil.createReqUpdateRoomMessage(netcl, roomSelected));
				guiChatroomClient.updateUsers();
			}
			
			if(roomSelected != null && 
					roomSelected != ""	) guiChatroomClient.chatroomClient.activeChatroom = roomSelected;
         }
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
}
