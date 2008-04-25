package gpl.java.abratuhi.src.gui.blocks;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ConnectWindow extends JFrame implements ActionListener{
	
	ButtonGroup net_options_bg = new ButtonGroup();
	JRadioButton[] net_options_rb = new JRadioButton[3];
	JTextField field_host_ip = new JTextField("enter host here");
	JTextField field_host_port = new JTextField("enter port here");
	JButton button_proceed = new JButton("Proceed!");
	
	public ConnectWindow(){
		super("Connect");
		setLayout(new FlowLayout());
		setSize(new Dimension(300,200));
		setVisible(true);
		setResizable(true);
		setLocation(300, 300);
		//
		net_options_rb[0] = new JRadioButton("Host");
		net_options_rb[1] = new JRadioButton("Connect to host");
		net_options_rb[2] = new JRadioButton("Work offline");
		for(int i=0; i<3; i++){
			net_options_bg.add(net_options_rb[i]);
			net_options_rb[i].addActionListener(this);
		}
		field_host_ip.addActionListener(this);
		field_host_port.addActionListener(this);
		button_proceed.addActionListener(this);
		//
		//Box box = new Box(BoxLayout.X_AXIS);
		//box.add(field_host_ip);
		//box.add(field_host_port);
		add(field_host_ip);
		add(field_host_port);
		add(net_options_rb[0]);
		add(net_options_rb[1]);
		//add(box);
		add(net_options_rb[2]);	
		add(button_proceed);
		//
		paintAll(getGraphics());
	}

	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		if(cmd != null && cmd.equals("Proceed!")){
			dispose();
		}
	}
	
	public static void main(String[] args){
		new ConnectWindow();
	}
}
