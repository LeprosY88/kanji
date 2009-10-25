package org.abratuhi.mobilekanji.ui;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.TextBox;

import org.abratuhi.mobilekanji.KanjiMidlet;

import com.alsutton.xmlparser.XMLEventListener;
import com.alsutton.xmlparser.objectmodel.Node;
import com.alsutton.xmlparser.objectmodel.TreeBuilder;

import com.alsutton.xmlparser.XMLParser;

public class KanjiCanvas extends Canvas implements CommandListener{
	KanjiMidlet parentmidlet;

	Command exitCmd;

	TextBox textbox;
	
	String currentString;
	
	Node rootNode;

	public KanjiCanvas(KanjiMidlet parent){
		this.parentmidlet = parent;

		this.exitCmd = new Command("Exit", Command.EXIT, 1);
		
		this.textbox = new TextBox("Lesson", "", 300, 0);
		
		this.addCommand(exitCmd);

		this.setCommandListener(this);
		
	}

	protected void paint(Graphics g) {
		/*g.setColor(255, 0, 0);
		g.fillRect(0, 0, getWidth(), getHeight());

		if (currentString != null){
			g.setColor(0, 0, 255);
			g.drawString(currentString, getWidth()/2, getHeight()/2, 
					Graphics.TOP | Graphics.HCENTER);
		}*/
	}

	public void commandAction(Command cmd, Displayable displ) {
		try{
			if(cmd == exitCmd){
				//sparentmidlet.exit();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		repaint();
	}

	protected void keyPressed(int keyCode){
		if(keyCode == -3){	//larrow
			this.currentString = "larrow";
		}
		else if(keyCode == -4){	//rarrow
			this.currentString = "rarrow";
		}

		repaint();
	}

}
