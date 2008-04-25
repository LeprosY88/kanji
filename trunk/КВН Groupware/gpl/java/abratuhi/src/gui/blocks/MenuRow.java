package gpl.java.abratuhi.src.gui.blocks;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuRow {
	ActionListener listener;
	
	public JMenuBar menu = new JMenuBar();
	
	public JMenu file = new JMenu("File");
	public JMenu edit = new JMenu("Edit");
	public JMenu view = new JMenu("View");
	public JMenu net = new JMenu("Net");
	public JMenu chat = new JMenu("Chat");
	public JMenu brainstorm = new JMenu("Brainstorm");
	public JMenu help = new JMenu("Help");
	
	public JMenuItem file_new = new JMenuItem("New");
	public JMenuItem file_open = new JMenuItem("Open");
	public JMenuItem file_close = new JMenuItem("Close");
	public JMenuItem file_exit = new JMenuItem("Exit");
	public JMenuItem edit_prefs = new JMenuItem("Preferences");
	public JMenuItem net_connect = new JMenuItem("Connect");
	public JMenuItem net_login = new JMenuItem("Login");
	public JMenuItem net_logout = new JMenuItem("Logout");
	public JMenuItem net_disconnect = new JMenuItem("Disconnect");
	public JMenuItem chat_join = new JMenuItem("Join Chat");
	public JMenuItem chat_leave = new JMenuItem("Leave Chat");
	public JMenuItem brainstorm_join = new JMenuItem("Join Brainstorm");
	public JMenuItem brainstorm_leave = new JMenuItem("Leave Brainstorm");
	public JMenuItem help_about = new JMenuItem("About");
	public JMenuItem help_help = new JMenuItem("Help");
	
	public MenuRow(ActionListener al){
		//
		listener = al;
		//
		file_new.addActionListener(listener);
		file_open.addActionListener(listener);
		file_close.addActionListener(listener);
		file_exit.addActionListener(listener);
		edit_prefs.addActionListener(listener);
		net_connect.addActionListener(listener);
		net_login.addActionListener(listener);
		net_logout.addActionListener(listener);
		net_disconnect.addActionListener(listener);
		chat_join.addActionListener(listener);
		chat_leave.addActionListener(listener);
		brainstorm_join.addActionListener(listener);
		brainstorm_leave.addActionListener(listener);
		help_about.addActionListener(listener);
		help_help.addActionListener(listener);
		//
		file.add(file_new);
		file.add(file_open);
		file.add(file_close);
		file.add(file_exit);
		edit.add(edit_prefs);
		net.add(net_connect);
		net.add(net_login);
		net.add(net_logout);
		net.add(net_disconnect);
		chat.add(chat_join);
		chat.add(chat_leave);
		brainstorm.add(brainstorm_join);
		brainstorm.add(brainstorm_leave);
		help.add(help_about);
		help.add(help_help);
		//
		menu.add(file);
		menu.add(edit);
		menu.add(view);
		menu.add(net);
		menu.add(chat);
		menu.add(brainstorm);
		menu.add(help);
	}
}
