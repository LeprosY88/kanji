/**
 * @author Alexei Bratuhin
 * @licence GPLv2
 */

/**
 * TODO: use User stats to notify newly connected clients about all kind of status
 * TODO: 
*/
package gpl.java.abratuhi.src.gui.blocks;

import gpl.java.abratuhi.src.model.Brainstorm;
import gpl.java.abratuhi.src.model.KVNGroupware;
import gpl.java.abratuhi.src.model.User;
import gpl.java.abratuhi.src.net.message.prototype.Client;
import gpl.java.abratuhi.src.net.message.prototype.Message;
import gpl.java.abratuhi.src.net.message.prototype.Server;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class KVNGroupwareGUI extends JFrame implements ActionListener, Runnable{
	/**/
	public boolean runOK = false;
	
	/**/
	//public Server s = new Server();
	//public Client c = new Client();
	
	/**/
	//public Brainstorm b = new Brainstorm(c);
	//public User u = new User();
	public KVNGroupware kvn = new KVNGroupware();
	
	/**/
	public MenuRow mr = new MenuRow(this);
	public StartupWindow suw = new StartupWindow(this);
	public BrainstormWindow bw = new BrainstormWindow(this);
	public StatusWindow sw = new StatusWindow();
	public ChatWindow cw = new ChatWindow(this);
	
	/**/
	public KVNGroupwareGUI(){
		//
		super(".:: КВН Groupware ::.");
		setLayout(new GridLayout(2, 2, 0, 0));
		setLocation(150,50);
		setSize(new Dimension(800,600));
		setResizable(true);
		setVisible(true);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				/*if(c.isConnected){
					c.stopp();
					c.logout();
					c.close();
				}*/
				/*if(s.isUp){
					s.stopp();
					s.close();
				}*/
				kvn.stopp();
				if(kvn.cl.isConnected) kvn.disconnect();
				kvn.logout();
				runOK = false;
				System.exit(0);
			}
		});
		//
		runOK = true;
		kvn.start();
		//
		bw.setEnabled(false);
		sw.setEnabled(false);
		cw.setEnabled(false);
		suw.setEnabled(true);
		//
		setJMenuBar(mr.menu);
		add(bw);
		add(sw);
		add(cw);
		add(suw);
		//
		paintAll(getGraphics());		
	}
	

	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		/* menubar */
		if(cmd.equals("Login")){
			String nick = suw.field_nickname.getText();
			kvn.login(nick);
		}
		if(cmd.equals("Logout")){
			kvn.logout();
		}
		if(cmd.equals("Connect")){
			String host = suw.field_client_host_ip.getText();
			int port = Integer.parseInt(suw.field_client_host_port.getText());
			kvn.connect(host, port);
		}
		if(cmd.equals("Disconnect")){
			kvn.disconnect();
		}
		if(cmd.equals("Join Chat")){
			//c.sendMessage(new Message(Message.MSG_TYPE_STATUS, c.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_STATUS_CHAT, Message.MSG_BODY_LOGIN));
			kvn.joinChat();
		}
		if(cmd.equals("Leave Chat")){
			//c.sendMessage(new Message(Message.MSG_TYPE_STATUS, c.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_STATUS_CHAT, Message.MSG_BODY_LOGOUT));
			kvn.leaveChat();
		}
		if(cmd.equals("Join Brainstorm")){
			//c.sendMessage(new Message(Message.MSG_TYPE_STATUS, c.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_STATUS_BRAINSTORM, Message.MSG_BODY_LOGIN));
			kvn.joinBrainstorm();
		}
		if(cmd.equals("Leave Brainstorm")){
			//c.sendMessage(new Message(Message.MSG_TYPE_STATUS, c.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_STATUS_BRAINSTORM, Message.MSG_BODY_LOGOUT));
			kvn.leaveBrainstorm();
		}
		/* initial phase */
		if(cmd.equals("Proceed!")){
			/* manage networkcity */
			/*if(suw.net_options_rb[0].isSelected()){
				System.out.println("Host at " + suw.field_server_host_port.getText());
				String nick = suw.field_nickname.getText();
				String host = "localhost";
				int port = Integer.parseInt(suw.field_server_host_port.getText());
				s.up(port);
				s.start();
				u.nickname = nick;
				c.idClient = nick;
				c.connect(host, port);
				c.start();
				c.sendMessage(new Message(Message.MSG_TYPE_STATUS, c.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_STATUS_GENERAL, Message.MSG_BODY_LOGIN));
			}*/
			if(suw.net_options_rb[1].isSelected()){
				System.out.println("Client connected to " + suw.field_client_host_ip.getText() + " at " + suw.field_client_host_port.getText());
				String nick = suw.field_nickname.getText();
				String host = suw.field_client_host_ip.getText();
				int port = Integer.parseInt(suw.field_client_host_port.getText());
				//u.nickname = nick;
				//c.idClient = nick;
				//c.connect(host, port);
				//c.start();
				//c.sendMessage(new Message(Message.MSG_TYPE_STATUS, c.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_STATUS_GENERAL, Message.MSG_BODY_LOGIN));
				kvn.login(nick);
				kvn.connect(host, port);
			}
			/* manage GUI */
			suw.setEnabled(false);
			bw.setEnabled(true);
			sw.setEnabled(true);
			cw.setEnabled(true);
		}
		/* chat phase */
		if(cmd.equals("Send")){
			//System.out.println("Sending");
			String msg = cw.msg.getText();
			cw.msg.setText("");//clean input field
			/*c.sendMessage(new Message(Message.MSG_TYPE_CHAT, c.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_CHAT_MESSAGE, msg));*/
			kvn.c.sendMessage(Message.MSG_TO_ALL, msg);
		}
		/* brainstorm phase */
		if(cmd.equals("Submit theme!")){
			
		}
		if(cmd.equals("Change sheet!")){
			/*if(b.round == 0){
				String msg = bsw.theme.getText();
				bsw.theme.setText("");//clean input field
				c.sendMessage(new Message(Message.MSG_TYPE_BRAINSTORM, c.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_BRAINSTORM_THEME, msg));
				//c.sendMessage(new Message(Message.MSG_TYPE_STATUS, c.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_THEMESUBMIT, ""));
			}
			if(b.round>0 && b.round<=b.users.size()){
				String msg = bsw.text.getText();
				bsw.text.setText("");//clean input field
				c.sendMessage(new Message(Message.MSG_TYPE_BRAINSTORM, c.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_BRAINSTORM_SHEET, msg));
				//c.sendMessage(new Message(Message.MSG_TYPE_STATUS, c.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_WAITING, ""));
			}
			if(b.round > b.users.size()){
				bsw.text.setText("Brainstrom is already over."+"\n"+b.finalsheet);
			}*/
			//String msg = cw.msg.getText();
			//c.sendMessage(new Message(Message.MSG_TYPE_CHAT, c.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_MESSAGE, msg));
			if(kvn.b.round == 0){
				String th = bw.theme.getText();
				kvn.b.sendTheme(th);
			}
			if(kvn.b.round > 0){
				String sh = bw.text.getText();
				kvn.b.sendSheet(sh);
			}
		}
		/**/
		//refresh();
		//repaint();
	}
	
	public void refresh(){
		sw.refresh(kvn.users);
		cw.refresh(kvn.c);
		bw.refresh(kvn.b);
	}
	
	public static void main(String[] args){
		new KVNGroupwareGUI().run();
		//new KVNGroupwareGUI();
	}


	public void run() {
		runOK = true;
		while(runOK){
			refresh();
			repaint();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}