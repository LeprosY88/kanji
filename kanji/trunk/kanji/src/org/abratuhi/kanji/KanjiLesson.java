package org.abratuhi.kanji;

import java.io.IOException;
import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class KanjiLesson {
	
	public ArrayList<KanjiChar> chars;
	private int currentChar;
	
	public KanjiLesson(String filename){
		// init
		chars = new ArrayList<KanjiChar>();
		currentChar = 0;
		
		// load signs
		try {
			// load and parse index document
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(filename);
			// get list of all available chars
			Element docroot = doc.getRootElement();
			for(int i=0; i<docroot.getChildren("char").size(); i++){
				KanjiChar tChar = new KanjiChar((Element) docroot.getChildren("char").get(i));
				chars.add(tChar);
			}
				
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setActiveCharNumber(int a){
		if(a>=0 && a<chars.size()) currentChar = a;
	}
	public int getActiveCharNumber(){
		return currentChar;
	}
	public KanjiChar getActiveChar(){
		return chars.get(getActiveCharNumber());
	}
	
	public void prev(){
		int a = getActiveCharNumber();
		setActiveCharNumber(--a);
	}
	public void next(){
		int a = getActiveCharNumber();
		setActiveCharNumber(++a);
	}
	
	public void printInfo(){
		System.out.println("total chars:\t"+chars.size());
		for(int i=0; i<chars.size(); i++){
			chars.get(i).printInfo();
		}
	}

}
