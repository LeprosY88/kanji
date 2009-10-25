package org.abratuhi.kanji;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.abratuhi.kanji.KanjiChar;
import org.abratuhi.kanji.KanjiLesson;


public class Kanji_GUI_en extends JPanel{
	
	Kanji_GUI root;
	KanjiLesson lesson;
	
	JTextArea translation;
	
	public Kanji_GUI_en(Kanji_GUI r, KanjiLesson l){
		// super
		super();
		
		// kanji_gui, kanji_lesson
		root = r;
		lesson = l;
		
		// set translation
		translation = new JTextArea("translation here");
		translation.setLineWrap(true);
		translation.setWrapStyleWord(true);
		translation.setRows(5);
		translation.setColumns(20);
		
		// kanji_gui_en
		setLayout(new BorderLayout());
		setVisible(true);
		
		add(new JScrollPane(translation), BorderLayout.CENTER);
	}
	
	public void paintChar(KanjiChar c, boolean printTranslation){
		//System.out.println("setting translation");
		// set translation
		if(printTranslation) translation.setText(c.translation);
		// repaint
		repaint();
	}

}
