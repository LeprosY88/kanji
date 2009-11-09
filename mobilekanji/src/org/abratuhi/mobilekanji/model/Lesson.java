package org.abratuhi.mobilekanji.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import com.alsutton.xmlparser.objectmodel.Node;
import com.alsutton.xmlparser.objectmodel.TreeBuilder;
import com.sun.perseus.platform.BufferedInputStream;

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
		//TODO
	}
	
	public void order()
	{
		for(int i=0; i<units.size(); i++)
		{
			for(int j=i; j<units.size(); j++)
			{
				LessonUnit lu_i = (LessonUnit) units.elementAt(i);
				LessonUnit lu_j = (LessonUnit) units.elementAt(j);
				if(lu_i.compareTo(lu_j) < 0)
				{
					units.setElementAt(lu_i, j);
					units.setElementAt(lu_j, i);
				}
			}
			
		}
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

	public void loadKnownTable(String url, Hashtable table)
	{
		try
		{
			FileConnection fconn = (FileConnection)Connector.open(url, Connector.READ_WRITE);
			if (!fconn.exists()) 
			{
				fconn.create();
			}
			
			table.clear();

			InputStream is = fconn.openInputStream();
			Reader r = new InputStreamReader(is, "UTF-8");
			String line = null;
			while((line = readLine(r)) != null && line.length()>0)
			{
				String key = line.substring(0, line.indexOf(" "));
				String value = line.substring(line.indexOf(" ") + 1);
				System.out.println("Read: " + key + ":" + value);
				table.put(key, value);
			}

			r.close();
			is.close();
			fconn.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public String readLine(Reader stream) throws IOException
	{
		char c;
		StringBuffer line = new StringBuffer();
		while (true)
		{
			int n = stream.read();
			if(n == -1)
			{
				return null;
			}
			else
			{
				c = (char) n;
				if (c == '\n'){
					break;
				}

				line.append(c);
			}
		}
		return line.toString();
	}


	public void saveKnownTable(String url, Hashtable table)
	{
		try
		{
			FileConnection fconn = (FileConnection)Connector.open(url, Connector.READ_WRITE);
			if (!fconn.exists()) 
			{
				fconn.create();
			}
			OutputStream ops = fconn.openOutputStream();
			Enumeration e = table.keys();
			while(e.hasMoreElements())
			{
				String key = (String) e.nextElement();
				String value = (String) table.get(key);
				ops.write((key + " " + value + "\n").getBytes());
				System.out.println("Written: " + key + ":" + value);
			}
			ops.flush();
			ops.close();
			fconn.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
