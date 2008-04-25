package gpl.java.abratuhi.src.gui.blocks;

import gpl.java.abratuhi.src.model.Chat;

import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class ChatWindow extends JPanel{
	
	public ActionListener listener;
	
	public Box box1 = new Box(BoxLayout.Y_AXIS);
	public Box box2 = new Box(BoxLayout.X_AXIS);
	public JTextArea msg = new JTextArea();
	public JTextArea text = new JTextArea();
	public JButton send = new JButton("Send");
	
	public ChatWindow(ActionListener al){
		listener = al;
		//
		text.setLineWrap(true);
		text.setWrapStyleWord(true);
		text.setRows(15);
		text.setColumns(40);
		//
		msg.setLineWrap(true);
		msg.setWrapStyleWord(true);
		msg.setRows(4);
		msg.setColumns(30);
		text.setEditable(false);
		//for test purposes only
		//text.setText("coiouhkc: text1\nKrola: text2\n");
		//msg.setText("current message");
		//
		setBorder(new TitledBorder("Chat"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		box1.add(new JScrollPane(text));
		box2.add(new JScrollPane(msg));
		box2.add(send);
		box1.add(box2);
		add(box1);
		//
		send.addActionListener(listener);
	}
	
	public void refresh(Chat c){
		for(int i=0; i<c.incoming.size(); i++){
			text.append(c.incoming.remove(0));
		}
	}

}
