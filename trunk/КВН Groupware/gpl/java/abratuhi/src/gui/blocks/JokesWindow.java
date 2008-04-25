package gpl.java.abratuhi.src.gui.blocks;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class JokesWindow extends JPanel{
	private final static String[] colors = {"green", "blue", "red", "gray", "black"};
	private final static Color[] colorz = {Color.green, Color.blue, Color.red, Color.gray, Color.black};
	
	public ActionListener listener;
	
	public String[] jokes;
	public String[][] comments;
	public JTextArea list = new JTextArea();
	public JTextArea add = new JTextArea();
	public JTextField nr_comment = new JTextField("#");
	public JTextField nr_rate = new JTextField("#");
	public JButton button_add = new JButton("Add Joke");
	public JButton button_comment = new JButton("Comment Joke Nr.:");
	public JButton button_rate = new JButton("Rate Joke Nr.:");
	public JComboBox combo_rate = new JComboBox(colors);
	
	public JokesWindow(ActionListener al){
		listener = al;
		//Box box1 = new Box(BoxLayout.Y_AXIS);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		Box box2 = new Box(BoxLayout.X_AXIS);
		Box box3 = new Box(BoxLayout.X_AXIS);
		Box box4 = new Box(BoxLayout.X_AXIS);
		Box box5 = new Box(BoxLayout.X_AXIS);
		
		list.setEditable(false);
		list.setRows(25);
		list.setColumns(50);
		add.setRows(3);
		add.setColumns(35);
		
		//
		button_add.addActionListener(listener);
		button_comment.addActionListener(listener);
		button_rate.addActionListener(listener);
		combo_rate.addActionListener(listener);
		
		//box3.setLayout(new GridLayout(1, 3));
		
		box2.add(new JScrollPane(list));
		box3.add(button_rate);
		box3.add(nr_rate);
		box3.add(combo_rate);
		box4.add(button_comment);
		box4.add(nr_comment);
		box5.add(button_add);
		box5.add(new JScrollPane(add));
		
		add(box2);
		add(box3);
		add(box4);
		add(box5);
	}

}
