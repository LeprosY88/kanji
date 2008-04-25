package gpl.java.abratuhi.src.gui.blocks;

import gpl.java.abratuhi.src.model.Brainstorm;

import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class BrainstormWindow extends JPanel{
	//
	public ActionListener listener;
	//
	public Box box1 = new Box(BoxLayout.Y_AXIS);
	public Box box2 = new Box(BoxLayout.X_AXIS);
	public JTextField theme = new JTextField();
	public JTextField round = new JTextField();	
	public JTextArea text = new JTextArea();
	public JButton button_change = new JButton("Change sheet!");
	
	public BrainstormWindow(ActionListener al){
		listener = al;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new TitledBorder("Brainstorm"));
		//
		theme.setEditable(true);
		theme.setColumns(40);
		round.setEditable(false);
		round.setColumns(5);
		text.setEditable(true);
		text.setRows(15);
		text.setColumns(40);
		//
		//theme.setText("Enter theme here.");
		//text.setText("Enter jokes here.");
		//
		button_change.addActionListener(listener);
		//
		box2.add(new JLabel("Theme:"));
		box2.add(theme);
		box2.add(new JLabel("Round:"));
		box2.add(round);
		box2.add(button_change);
		box1.add(box2);
		box1.add(new JScrollPane(text));
		add(box1);
	}
	
	public void refresh(Brainstorm b){
		//round.setText(String.valueOf(b.round));
		if(b.round>0){
			theme.setText(b.finaltheme);
			round.setText(String.valueOf(b.round));
			//if(!b.isOver) text.setText(b.sheets.get(b.findUser(b.c.idClient)));
			//if(b.isOver) text.setText(b.finalsheet);
			if(Integer.parseInt(round.getText())!=b.round || b.isOver){
				if(!b.isOver) text.setText(b.sheets.get(b.findUser(b.c.idClient)));
				else text.setText(b.finalsheet);
			}
		}
	}

}
