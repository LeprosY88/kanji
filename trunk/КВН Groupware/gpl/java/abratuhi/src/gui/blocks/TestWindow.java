package gpl.java.abratuhi.src.gui.blocks;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class TestWindow extends JFrame implements ActionListener{
	
	StatusWindow status = new StatusWindow();
	LogWindow log = new LogWindow();
	BrainstormWindow brainstorm = new BrainstormWindow(this);
	ChatWindow chat = new ChatWindow(this);
	JokesWindow jokes = new JokesWindow(this);
	MenuRow menu = new MenuRow(this);
	
	public TestWindow(){
		super(".:: КВН Groupware ::.");
		setLayout(new FlowLayout());
		setLocation(150,50);
		setSize(new Dimension(800,600));
		setResizable(true);
		setVisible(true);
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		//add(status);
		//add(log);
		//add(brainstorm);
		//add(chat);
		add(jokes);
		//setJMenuBar(menu.menu);
		paintAll(getGraphics());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestWindow();
	}

	public void actionPerformed(ActionEvent ae) {
		String cmd = ae.getActionCommand();
		if(cmd!=null && cmd.equals("New")){
			System.out.println("cmd = _file_:_new_");
		}
		if(cmd!=null && cmd.equals("Open")){
			System.out.println("cmd = _file_:_open_");
		}
		if(cmd!=null && cmd.equals("Close")){
			System.out.println("cmd = _file_:_close_");
		}
		if(cmd!=null && cmd.equals("Exit")){
			System.out.println("cmd =_file_: _exit_");
			System.exit(0);
		}
		if(cmd!=null && cmd.equals("Preferences")){
			System.out.println("cmd = _edit_:_preferences_");
		}
		if(cmd!=null && cmd.equals("Connect")){
			System.out.println("cmd = _net_:_connect_");
			//new StartupWindow();
		}
		if(cmd!=null && cmd.equals("About")){
			System.out.println("cmd = _help_:_about_");
		}
		if(cmd!=null && cmd.equals("Help")){
			System.out.println("cmd = _help_:_help_");
		}
	}

}
