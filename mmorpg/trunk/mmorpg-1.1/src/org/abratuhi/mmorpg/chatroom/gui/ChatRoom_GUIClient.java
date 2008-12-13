package org.abratuhi.mmorpg.chatroom.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

import org.abratuhi.mmorpg.chatroom.client.ChatRoom_Client;
import org.abratuhi.mmorpg.chatroom.controller.ChatRoom_GUIClient_Controller;
import org.abratuhi.mmorpg.chatroom.model.ChatRoom;
import org.abratuhi.mmorpg.chatroom.util.SpringUtilities;

public class ChatRoom_GUIClient extends JFrame{

	public ChatRoom_Client chatroomClient;

	public JList jlist_chatrooms = new JList(new String[0]);
	public JList jlist_chatroomusers = new JList(new String[0]);
	public JTextArea textarea_chatroommessages = new JTextArea();
	public JTextArea textarea_message = new JTextArea();
	public JButton button_send = new JButton("Send");

	public ChatRoom_GUIClient(){
		super("Chatroom_GUIClient");

		this.chatroomClient = new ChatRoom_Client(this);

		JPanel rooms = new JPanel(new SpringLayout());
		rooms.setBorder(new TitledBorder("rooms:"));
		rooms.add(new JScrollPane(jlist_chatrooms));
		SpringUtilities.makeCompactGrid(rooms, 1, 1, 0, 0, 0, 0);

		JPanel users = new JPanel(new SpringLayout());
		users.setBorder(new TitledBorder("users:"));
		users.add(new JScrollPane(jlist_chatroomusers));
		SpringUtilities.makeCompactGrid(users, 1, 1, 0, 0, 0, 0);

		JPanel send = new JPanel(new SpringLayout());
		send.add(new JScrollPane(textarea_message));
		send.add(button_send);
		SpringUtilities.makeCompactGrid(send, 1, 2, 0, 0, 0, 0);

		JPanel chat = new JPanel(new SpringLayout());
		chat.setBorder(new TitledBorder("chat:"));
		chat.add(new JScrollPane(textarea_chatroommessages));
		chat.add(send);
		SpringUtilities.makeCompactGrid(chat, 2, 1, 0, 0, 0, 0);

		this.getContentPane().setLayout(new SpringLayout());
		/*this.getContentPane().add(new JScrollPane(rooms));
		this.getContentPane().add(new JScrollPane(users));
		this.getContentPane().add(chat);*/
		this.getContentPane().add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, rooms, users), chat));
		//SpringUtilities.makeCompactGrid(this.getContentPane(), 1, 3, 0, 0, 0, 0);*/
		SpringUtilities.makeCompactGrid(this.getContentPane(), 1, 1, 0, 0, 0, 0);

		ChatRoom_GUIClient_Controller controller = new ChatRoom_GUIClient_Controller(this);
		button_send.addActionListener(controller);

		textarea_chatroommessages.setRows(40);
		textarea_chatroommessages.setColumns(40);
		textarea_chatroommessages.setWrapStyleWord(true);
		textarea_chatroommessages.setLineWrap(true);

		textarea_message.setRows(3);
		textarea_message.setColumns(40);
		textarea_message.setWrapStyleWord(true);
		textarea_message.setLineWrap(true);

		this.setVisible(true);
		this.setPreferredSize(new Dimension(800,600));
		this.setLocation(new Point(100,100));

		this.addWindowListener(controller);
		this.addKeyListener(controller);

		textarea_message.addKeyListener(controller);
		jlist_chatrooms.addListSelectionListener(controller);
		//jlist_chatrooms.addMouseListener(controller);
		
		textarea_chatroommessages.setFont(new Font("Courier", Font.PLAIN, 12));
		textarea_message.setFont(new Font("Courier", Font.PLAIN, 12));

		this.pack();
	}
	
	public void updateContent(){
		updateRooms();
		updateUsers();
		repaint();
	}
	
	public void updateRooms(){
		// get selected room and user
		String roomName = (String) jlist_chatrooms.getSelectedValue();
		String userName = (String) jlist_chatroomusers.getSelectedValue();
		Integer roomSelected = jlist_chatrooms.getSelectedIndex();
		Integer userSelected = jlist_chatroomusers.getSelectedIndex();

		// update list of available rooms
		String[] roomNames = new String[chatroomClient.chatrooms.size()];
		Iterator roomIterator = chatroomClient.chatrooms.entrySet().iterator();
		Integer i = 0;
		while(roomIterator.hasNext()){
			Map.Entry roomEntry = (Entry) roomIterator.next();
			roomNames[i] = ((ChatRoom)roomEntry.getValue()).name;
			i++;
		}
		
		jlist_chatrooms.removeAll();
		jlist_chatrooms.setListData(roomNames);
		//jlist_chatrooms.setSelectedIndex(roomSelected);
	}
	
	public void updateUsers(){
		// get selected room and user
		//String roomName = (String) jlist_chatrooms.getSelectedValue();
		String roomName = chatroomClient.activeChatroom;
		//String userName = (String) jlist_chatroomusers.getSelectedValue();
		//Integer roomSelected = jlist_chatrooms.getSelectedIndex();
		//Integer userSelected = jlist_chatroomusers.getSelectedIndex();

		// update list of available users in selected room
		if(roomName!=null && roomName!=new String() && roomName!=""){
			ChatRoom cr = chatroomClient.chatrooms.get(roomName);
			if(cr != null){
				String[] userNames = new String[cr.users.size()];
				for(int j=0; j<chatroomClient.chatrooms.get(roomName).users.size(); j++){
					userNames[j] = chatroomClient.chatrooms.get(roomName).users.get(j);
					System.out.println("GUIClient:\tFound user "+userNames[j]);
				}
				jlist_chatroomusers.removeAll();
				jlist_chatroomusers.setListData(userNames);
				//jlist_chatroomusers.setSelectedIndex(userSelected);
			}
		}
	}
	
	public void exit(){
		chatroomClient.stopdisconnect();
		//this.dispose();
		//System.exit(0);
	}

	public static void main(String[] args){
		ChatRoom_GUIClient guiclient = new ChatRoom_GUIClient();
		guiclient.chatroomClient.startconnect();
	}

}
