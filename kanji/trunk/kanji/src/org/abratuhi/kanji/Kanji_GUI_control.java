package org.abratuhi.kanji;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


import org.abratuhi.kanji.KanjiLesson;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class Kanji_GUI_control extends JPanel implements ActionListener{
	
	Kanji_GUI root;
	KanjiLesson lesson;
	
	JPanel select_lesson;
	JPanel options;
	JPanel controls;
	
	ButtonGroup bg;
	JRadioButton jrb_all;
	JRadioButton jrb_en;
	JRadioButton jrb_ja;
	JRadioButton jrb_ja_reading;
	JRadioButton jrb_ja_draw;
	
	JButton jb_prev;
	JButton jb_all;
	JButton jb_next;
	
	JComboBox jcb_select_lesson;
	
	
	public Kanji_GUI_control(Kanji_GUI r, KanjiLesson l){
		// super
		super();
		
		// kanji_gui, kanji_lesson
		root = r;
		lesson = l;
		
		// select lesson		
		jcb_select_lesson = new JComboBox();
		jcb_select_lesson.addActionListener(this);
		
		select_lesson = new JPanel();
		select_lesson.add(jcb_select_lesson);
		
		loadListOfLessons();
		
		// options
		jrb_all = new JRadioButton("SHOW ALL");
		jrb_en = new JRadioButton("SHOW EN");
		jrb_ja = new JRadioButton("SHOW JA");
		jrb_ja_draw = new JRadioButton("SHOW JA DRAW");
		jrb_ja_reading = new JRadioButton("SHOW JA READ");
		
		jrb_all.addActionListener(this);
		jrb_en.addActionListener(this);
		jrb_ja.addActionListener(this);
		jrb_ja_draw.addActionListener(this);
		jrb_ja_reading.addActionListener(this);
		
		bg = new ButtonGroup();
		bg.add(jrb_all);
		bg.add(jrb_en);
		bg.add(jrb_ja);
		bg.add(jrb_ja_draw);
		bg.add(jrb_ja_reading);
		
		options = new JPanel(new GridLayout(3,2, 0,0));
		options.add(jrb_all);
		options.add(jrb_ja_draw);
		options.add(jrb_en);
		options.add(jrb_ja_reading);
		options.add(jrb_ja);
		
		// control
		jb_prev = new JButton("<");
		jb_all = new JButton("#");
		jb_next = new JButton(">");
		
		jb_prev.addActionListener(this);
		jb_all.addActionListener(this);
		jb_next.addActionListener(this);
		
		controls = new JPanel(new GridLayout(1,3, 0,0));
		controls.add(jb_prev);
		controls.add(jb_all);
		controls.add(jb_next);
		
		// kanji_gui_control
		setLayout(new GridLayout(3,1, 0,0));
		setVisible(true);
		
		add(select_lesson);
		add(options);
		add(controls);
		
	}
	
	private void loadListOfLessons(){
		try {
			// load and parse index document
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build("xml\\index.xml");
			// get list of all available lessons
			ArrayList<String> lessons = new ArrayList<String>();
			Element docroot = doc.getRootElement();
			for(int i=0; i<docroot.getChildren("chapter").size(); i++){
				Element chapter = (Element) docroot.getChildren("chapter").get(i);
				for(int j=0; j<chapter.getChildren("lesson").size(); j++){
					Element lesson = (Element) chapter.getChildren("lesson").get(j);
					//lessons.add(lesson.getAttributeValue("name"));
					lessons.add(lesson.getText());
				}
			}
			// fill jcombo with lessons possible to select
			for(int i=0; i<lessons.size(); i++){
				jcb_select_lesson.addItem(lessons.get(i));
			}
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(jcb_select_lesson)){
			// perform action
			root.loadLesson((String) jcb_select_lesson.getSelectedItem());
			// repaint, since context changed
			root.repaint();
		}
		if(e.getSource().equals(jrb_all)){
			//System.out.println(jrb_all.getText());
			root.showView("SHOW_ALL");
		}
		if(e.getSource().equals(jrb_en)){
			//System.out.println(jrb_en.getText());
			root.showView("SHOW_EN");
		}
		if(e.getSource().equals(jrb_ja)){
			//System.out.println(jrb_ja.getText());
			root.showView("SHOW_JA");
		}
		if(e.getSource().equals(jrb_ja_draw)){
			//System.out.println(jrb_ja_draw.getText());
			root.showView("SHOW_JA_DRAW");
		}
		if(e.getSource().equals(jrb_ja_reading)){
			root.showView("SHOW_JA_READ");
		}
		if(e.getSource().equals(jb_prev)){
			//System.out.println(jb_prev.getText());
			if(root.lesson != null){
				root.lesson.prev();
				//root.ja.paintChar(root.lesson.getActiveChar(), root.paintChar, root.printReading);
				//root.en.paintChar(root.lesson.getActiveChar(), root.printTranslation);
				root.repaint();
			}
			else{
				System.out.println("lesson==null");
			}
		}
		if(e.getSource().equals(jb_all)){//TODO: not working right now, is to be corrected
			//System.out.println(jb_all.getText());
			root.showView("SHOW_ALL");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if(jrb_en.isSelected()) root.showView("SHOW_EN");
			if(jrb_ja.isSelected()) root.showView("SHOW_JA");
		}
		if(e.getSource().equals(jb_next)){
			//System.out.println(jb_next.getText());
			if(root.lesson != null){
				root.lesson.next();
				//root.ja.paintChar(root.lesson.getActiveChar(), root.paintChar, root.printReading);
				//root.en.paintChar(root.lesson.getActiveChar(), root.printTranslation);
				root.repaint();
			}
			else{
				System.out.println("lesson==null");
			}
		}
	}

}
