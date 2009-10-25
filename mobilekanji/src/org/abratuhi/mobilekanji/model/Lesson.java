package org.abratuhi.mobilekanji.model;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import com.alsutton.xmlparser.objectmodel.Node;
import com.alsutton.xmlparser.objectmodel.TreeBuilder;

public class Lesson 
{
	Vector units = new Vector();
	LessonUnit currentUnit;
	
	public Lesson()
	{
		
	}
	
	public LessonUnit getFirst()
	{
		this.currentUnit = (LessonUnit) units.firstElement();
		return this.currentUnit;
	}
	
	public LessonUnit getLast()
	{
		this.currentUnit = (LessonUnit) units.lastElement();
		return this.currentUnit;
	}
	
	public LessonUnit getCurrent()
	{
		return currentUnit;
	}
	
	public LessonUnit getNext()
	{
		if(this.currentUnit.equals(units.lastElement()))
			this.currentUnit = (LessonUnit) units.lastElement();
		else
			this.currentUnit = (LessonUnit) units.elementAt(units.indexOf(currentUnit) + 1);
		
		return this.currentUnit;
	}
	
	public LessonUnit getPrev()
	{
		if(this.currentUnit.equals(units.firstElement()))
			this.currentUnit = (LessonUnit) units.firstElement();
		else
			this.currentUnit = (LessonUnit) units.elementAt(units.indexOf(currentUnit) - 1);
		
		return this.currentUnit;
	}
	
	public void randomize()
	{
		
	}
	
	public Lesson load(String resourse)
	{
		units.removeAllElements();
		currentUnit = null;
		
		try{
			Node lessonNode = new TreeBuilder().createTree(this.getClass().getResourceAsStream(resourse));
			
			Vector lessonUnitNodes = lessonNode.getChildren();
			
			for(Enumeration e = lessonUnitNodes.elements(); e.hasMoreElements(); )
			{
				Node lessonUnitNode = (Node) e.nextElement();
				
				String original = lessonUnitNode.getAttribute("o");
				String uid = lessonUnitNode.getAttribute("f");
				String transcription = lessonUnitNode.getAttribute("t");
				String meaning = lessonUnitNode.getAttribute("m");
				
				units.addElement(new LessonUnit(original, uid, transcription, meaning));
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		currentUnit = (LessonUnit) units.firstElement();
		
		return null;
	}
}
