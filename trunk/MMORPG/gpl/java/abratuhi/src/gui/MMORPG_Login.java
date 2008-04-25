package gpl.java.abratuhi.src.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MMORPG_Login extends JFrame implements ActionListener{
	
	JPanel p = new JPanel();
	JTextField login = new JTextField();
	JPasswordField passwd = new JPasswordField();
	JButton go = new JButton("Go!");
	
	public MMORPG_Login(){
		super("Login to MMORPG");
		setSize(new Dimension(300,200));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		p.setLayout(new GridLayout(3,1,0,0));
		p.add(login);
		p.add(passwd);
		p.add(go);
		go.addActionListener(this);
		add(p);
		pack();
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(go.getActionCommand())){
			//
			String login_out = login.getText();
			String passwd_out = passwd.getText();
			//
			new MMORPG_Game(login_out);
			this.dispose();
		}
	}
	
	public static void main (String[] args){
		new MMORPG_Login();
	}

}
