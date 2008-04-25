package gpl.java.abratuhi.src.gui.blocks;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class StartupWindow extends JPanel /*implements ActionListener*/{
	
	//
	ActionListener al;
	//
	ButtonGroup net_options_bg = new ButtonGroup();
	JRadioButton[] net_options_rb = new JRadioButton[3];
	//JTextField field_server_host_ip = new JTextField("enter host here");
	//JTextField field_server_host_port = new JTextField("enter port here");
	//JTextField field_client_host_ip = new JTextField("enter host here");
	//JTextField field_client_host_port = new JTextField("enter port here");
	//JTextField field_nickname = new JTextField("enter nickname here");
	JTextField field_server_host_ip = new JTextField("enter host here");
	JTextField field_server_host_port = new JTextField("9000");
	JTextField field_client_host_ip = new JTextField("localhost");
	JTextField field_client_host_port = new JTextField("9000");
	JTextField field_nickname = new JTextField("ohlamon");
	//JTextField field_name = new JTextField("enter name here");
	//JTextField field_surname = new JTextField("enter surname here");
	JButton button_proceed = new JButton("Proceed!");
	
	public StartupWindow(ActionListener als){
		//super("Connect");
		setLayout(new GridLayout(3,0, 5,5));
		setBorder(new TitledBorder("Connection"));
		//setSize(new Dimension(300,200));
		//setVisible(true);
		//setResizable(true);
		//setLocation(300, 300);
		//
		al = als;
		//
		net_options_rb[0] = new JRadioButton("Host");
		net_options_rb[1] = new JRadioButton("Connect to host");
		net_options_rb[2] = new JRadioButton("Work offline");
		for(int i=0; i<3; i++){
			net_options_bg.add(net_options_rb[i]);
			net_options_rb[i].addActionListener(al);
		}
		field_server_host_ip.addActionListener(al);
		field_server_host_port.addActionListener(al);
		field_client_host_ip.addActionListener(al);
		field_client_host_port.addActionListener(al);
		field_nickname.addActionListener(al);
		button_proceed.addActionListener(al);
		//
		//Box box = new Box(BoxLayout.X_AXIS);
		//box.add(field_host_ip);
		//box.add(field_host_port);
		add(net_options_rb[0]);
		add(field_server_host_ip);
		add(field_server_host_port);
		add(net_options_rb[1]);
		add(field_client_host_ip);
		add(field_client_host_port);
		//add(box);
		add(net_options_rb[2]);
		add(field_nickname);
		add(button_proceed);
		//
		paintAll(getGraphics());
	}

	/*public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		if(cmd != null && cmd.equals("Proceed!")){
			if(net_options_rb[0].isSelected()){
				System.out.println("Host at " + field_server_host_port.getText());
			}
			if(net_options_rb[1].isSelected()){
				System.out.println("Client connected to " + field_client_host_ip.getText() + " at " + field_client_host_port.getText());
			}
			dispose();
		}
	}*/
	
	/*public static void main(String[] args){
		new StartupWindow();
	}*/
}
