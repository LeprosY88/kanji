package gpl.java.abratuhi.src.gui.blocks;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class LogWindow extends JPanel{
	
	public final String title = "Log:";
	public final String testString = "test\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\ntest\n";
	public JTextArea log;
	public ArrayList<String> messages = new ArrayList<String>();
	
	public LogWindow(){
		super();
		log = new JTextArea();
		log.setLineWrap(true);
		log.setName(title);
		log.setRows(15);
		//log.setColumns(40);
		log.setText(title+"\n"+testString);
		log.setEditable(false);
		add(new JScrollPane(log));
	}
	
	public void addLogMessage(String msg){
		messages.add(msg);
		log.append(msg + "\n");
		repaint();
	}

}
