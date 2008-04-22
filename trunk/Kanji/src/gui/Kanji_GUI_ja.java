package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.Kanji_char;
import model.Kanji_lesson;

public class Kanji_GUI_ja extends JPanel{
	
	Kanji_GUI root;
	Kanji_lesson lesson;
	
	Kanji_GUI_ja_sign sign;
	
	JTextArea reading;
	
	JTextArea example;
	
	public Kanji_GUI_ja(Kanji_GUI r, Kanji_lesson l){
		// super
		super();
		
		// kanji_gui, kanji_lesson
		root = r;
		lesson = l;
		
		// sign
		sign = new Kanji_GUI_ja_sign(root, lesson);
		
		// reading
		reading = new JTextArea("reading here");
		reading.setLineWrap(true);
		reading.setWrapStyleWord(true);
		reading.setRows(5);
		reading.setColumns(20);
		
		// example
		
		// kanji_gui_ja
		setLayout(new GridLayout(2,2, 0,0));
		setVisible(true);
		add(sign);
		add(new JScrollPane(reading));
	}
	
	public void paintChar(Kanji_char c, boolean paintChar, boolean printReading){
		//System.out.println("setting reading");
		// paint char in sign
		if(paintChar) sign.paintChar(c.ch);
		// set reading
		if(printReading) reading.setText(c.reading);
		// set example
		// TODO
		// repaint
		//root.repaint();
	}
	
	/*
	public void paintComponent(Graphics gg){
		// super
		super.paintComponent(gg);
		// cast to graphics2d
		Graphics2D g = (Graphics2D) gg;
		// draw char if lesson loaded
		
	}
	*/

}
