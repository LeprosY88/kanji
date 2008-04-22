package model;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;

public class Kanji_char {
	
	public String ch = new String();
	public String reading = new String();
	public String translation = new String();
	
	ArrayList<String> examples_text = new ArrayList<String>();
	ArrayList<String> examples_reading = new ArrayList<String>();
	ArrayList<String> examples_translation = new ArrayList<String>();
	
	public Kanji_char(Element e){
		ch = e.getAttributeValue("ch");
		//reading = e.getChildText("reading");
		translation = e.getChildText("translation");
		
		List<Element> readings = e.getChildren("reading");
		for(int i=0; i<readings.size(); i++){
			reading += readings.get(i).getAttributeValue("type").toUpperCase()+": "+readings.get(i).getText()+"\n";
		}
	}
	
	public void printInfo(){
		System.out.println("char:\t"+ch);
		System.out.println("reading:\t"+reading);
		System.out.println("translation:\t"+translation);
	}

}
