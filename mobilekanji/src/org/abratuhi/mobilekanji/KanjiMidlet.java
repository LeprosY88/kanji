package org.abratuhi.mobilekanji;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.abratuhi.mobilekanji.model.Lesson;

public class KanjiMidlet extends MIDlet implements CommandListener{
	private Display  display;     // The display
	private KanjiCanvas canvas;   // Canvas 

	public KanjiMidlet(){
		display = Display.getDisplay(this);
		canvas  = new KanjiCanvas(this);
		
		/*Hashtable table = new Hashtable();
		table.put("1", "1");
		table.put("2", "1");
		table.put("3", "1");
		table.put("4", "1");
		table.put("5", "1");
		
		new Lesson().saveKnownTable("file:///root1/photos/table.txt", table);
		
		Hashtable table2 = new Hashtable();
		
		new Lesson().loadKnownTable("file:///root1/photos/table.txt", table2);
		
		System.out.println(table2.toString());*/
        

	}

	protected void destroyApp(boolean arg0) {
	}

	protected void pauseApp() {
	}

	protected void startApp() throws MIDletStateChangeException {
		display.setCurrent( canvas );
	}

	public void commandAction(Command c, Displayable d) {


	}


}
