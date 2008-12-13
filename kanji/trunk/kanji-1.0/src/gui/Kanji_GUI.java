package gui;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import model.Kanji_lesson;

public class Kanji_GUI extends JFrame{
	
	Kanji_lesson lesson;
	
	Kanji_GUI_ja ja;
	Kanji_GUI_en en;
	Kanji_GUI_control control;
	
	boolean paintChar = true;
	boolean printReading = true;
	boolean printTranslation = true;
	boolean printExample = true;
	
	public Kanji_GUI(){
		// super
		super();
		
		// window event listener
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		
		// init
		lesson = null;
		ja = new Kanji_GUI_ja(this, lesson);
		en = new Kanji_GUI_en(this, lesson);
		control = new Kanji_GUI_control(this, lesson);
		
		// kanji_gui
		setLayout(new GridLayout(1,3, 0,0));
		setVisible(true);
		setLocation(300, 200);
		setTitle("Learning Kanji");
		
		add(ja);
		add(en);
		add(control);
		
		pack();
	}
	
	public void paint(Graphics g){
		// super
		super.paint(g);
		// custom
		ja.paintChar(lesson.getActiveChar(), paintChar, printReading);
		en.paintChar(lesson.getActiveChar(), printTranslation);
	}
	
	public void showView(String view){
		// proceed view command
		if(view.equals("SHOW_ALL")){
			paintChar = true;
			printReading = true;
			printTranslation = true;
			printExample = true;
		}
		if(view.equals("SHOW_EN")){
			paintChar = false;
			printReading = false;
			printTranslation = true;
			printExample = false;
		}
		if(view.equals("SHOW_JA")){
			paintChar = true;
			printReading = true;
			printTranslation = false;
			printExample = true;
		}
		if(view.equals("SHOW_JA_DRAW")){
			paintChar = true;
			printReading = false;
			printTranslation = true;
			printExample = false;
		}
		if(view.equals("SHOW_JA_READ")){
			paintChar = false;
			printReading = true;
			printTranslation = true;
			printExample = false;
		}
		
		// repaint
		repaint();
	}
	
	public void loadLesson(String path){
		lesson = new Kanji_lesson(path);
		//System.out.println("lesson loaded:\t"+path);
		//lesson.printInfo();
	}
	
	
	public static void main(String[] args){
		new Kanji_GUI();
	}
	

}
