package org.abratuhi.mmorpg.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.abratuhi.mmorpg.model.MMORPG_Client;
import org.abratuhi.mmorpg.net.messaging.C_Client;
import org.abratuhi.mmorpg.net.messaging.Message;

public class MMORPG_Chat extends JPanel implements Runnable, ActionListener{
	
	MMORPG_Client client;
	
	public Box box1 = new Box(BoxLayout.Y_AXIS);
	public Box box2 = new Box(BoxLayout.X_AXIS);
	public JTextArea msg = new JTextArea();
	public JTextArea msgs = new JTextArea();
	public JButton send = new JButton("Send");
	
	boolean runOK = false;
	
	public MMORPG_Chat(MMORPG_Client client2){
		super();
		setPreferredSize(new Dimension(150, 100));
		setVisible(true);

		this.runOK = true;
		this.client = client2;
		
		msgs.setLineWrap(true);
		msgs.setWrapStyleWord(true);
		msgs.setRows(15);
		msgs.setColumns(40);
		msgs.setEditable(false);
		//
		msg.setLineWrap(true);
		msg.setWrapStyleWord(true);
		msg.setRows(4);
		msg.setColumns(30);
		//for test purposes only
		//text.setText("coiouhkc: text1\nKrola: text2\n");
		//msg.setText("current message");
		//
		send.addActionListener(this);
		//
		setBorder(new TitledBorder("Chat"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		box1.add(new JScrollPane(msgs));
		box2.add(new JScrollPane(msg));
		box2.add(send);
		box1.add(box2);
		add(box1);
	}
	public void run() {
		System.out.println("running chat");
		runOK = true;
		while (runOK){
			if(client.client.msg_incoming.size() > 0){
				for(int i=0; i<client.client.msg_incoming.size(); i++){
					Message m = client.client.msg_incoming.get(i);
					if(m.getType().equals("chat")){
						m = client.client.msg_incoming.remove(i);
						msgs.append("<"+m.getFromId()+">: "+m.getChatMessage()+"\n");
						break;
					}
					else{
						continue;
					}
				}
			}
			repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		if(cmd.equals("Send")){
			client.client.sendMessage(Message.createBroadcastMessage(client.hero, "chat", "false"));
			msg.setText(new String());
			repaint();
		}
		
	}
	
	/*public static void main(String[] args){
		JFrame f = new JFrame(".::MMORPG_Chat");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(200, 200));
		f.setLocation(new Point(200, 100));
		f.setVisible(true);
		f.add(new MMORPG_Chat());
		f.repaint();
	}*/

}
