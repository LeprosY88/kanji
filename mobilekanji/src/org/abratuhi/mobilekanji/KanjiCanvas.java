package org.abratuhi.mobilekanji;

import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.abratuhi.mobilekanji.model.Lesson;
import org.abratuhi.mobilekanji.model.LessonUnit;
import org.abratuhi.mobilekanji.model.LessonUnitStatus;

class KanjiCanvas extends Canvas implements CommandListener
{

	Command exitCommand = new Command("Exit", Command.EXIT, 0);
	Command loada = new Command("Load LessonA", Command.SCREEN, 1);
	Command loadb = new Command("Load LessonB", Command.SCREEN, 1);
	Command load1 = new Command("Load Lesson1", Command.SCREEN, 1);
	Command load2 = new Command("Load Lesson2", Command.SCREEN, 2);
	Command load3 = new Command("Load Lesson3", Command.SCREEN, 3);
	Command load4 = new Command("Load Lesson4", Command.SCREEN, 4);
	Command load5 = new Command("Load Lesson5", Command.SCREEN, 5);
	Command load6 = new Command("Load Lesson6", Command.SCREEN, 6);

	Lesson lesson = new Lesson();
	Hashtable knowntable = new Hashtable();

	private KanjiMidlet midlet;
	private Image im = null;
	
	private int mode = 0; // 0 - show all, 1 - show kanji, 2 - show translation

	public KanjiCanvas(KanjiMidlet midlet)
	{
		this.midlet = midlet;

		addCommand(exitCommand);
		addCommand(loada);
		addCommand(loadb);
		addCommand(load1);
		addCommand(load2);
		addCommand(load3);
		addCommand(load4);
		addCommand(load5);
		addCommand(load6);

		setCommandListener(this);   
	}
	
	private void showCurrent()
	{
		try
		{
			LessonUnit lu = lesson.getCurrent();
			im = Image.createImage("/" + lu.getUid() + ".png");
			this.repaint();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void paint(Graphics g)
	{
		g.setColor(255, 255, 255);
		g.fillRect(0, 0, getWidth(), getHeight());

		g.setColor(0, 0, 0);
		if (im != null)
		{
			g.drawImage(im, getWidth() / 2, getHeight() / 2, Graphics.VCENTER | Graphics.HCENTER); 
		}
		
		switch(this.mode)
		{
		case 0:
		{
			break;
		}
		case 1:
		{
			g.setColor(0, 0, 0);
			g.fillRect(0, getHeight() * 15/40, getWidth(), getHeight() * 25/40);
			break;
		}
		case 2:
		{
			g.setColor(0, 0, 0);
			g.fillRect(0, 0, getWidth(), getHeight() * 15/40);
			break;
		}
		}
		
		
		LessonUnit lu = lesson.getCurrent();
		if(lu != null)
		{
			LessonUnitStatus lus = lu.getStatus();
			if(lus.isGreen())
			{
				g.setColor(0, 255, 0);
				g.fillRect(getWidth()-50, getHeight()-50, 25, 25);
			}
			else if(lus.isRed())
			{
				g.setColor(255, 0, 0);
				g.fillRect(getWidth()-50, getHeight()-50, 25, 25);
			}
		}
	}

	protected void keyPressed(int keyCode) {
		try{
			switch(keyCode)
			{
			case -3:  case Canvas.LEFT:
			{
				LessonUnit lu = lesson.getPrev();
				im = Image.createImage("/" + lu.getUid() + ".png");
				this.repaint();
				break;
			}
			case -4: case Canvas.RIGHT:
			{
				LessonUnit lu = lesson.getNext();
				im = Image.createImage("/" + lu.getUid() + ".png");
				this.repaint();
				break;
			}
			case Canvas.KEY_NUM5:
			{
				LessonUnit lu = lesson.getCurrent();
				if(lu.getStatus().isRed())
					lu.setStatus(LessonUnitStatus.GREEN_STATUS());
				else if(lu.getStatus().isGreen())
					lu.setStatus(LessonUnitStatus.RED_STATUS());
				
				lesson.order();
				
				this.repaint();
				break;
			}
			case Canvas.KEY_NUM0:
			{
				this.mode = 0;
				
				this.repaint();
				break;
			}
			case Canvas.KEY_NUM1:
			{
				this.mode = 1;
				
				this.repaint();
				break;
			}
			case Canvas.KEY_NUM2:
			{
				this.mode = 2;
				
				this.repaint();
				break;
			}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void commandAction(Command c, Displayable d)
	{

		if(c == exitCommand){
			midlet.destroyApp(false);
			midlet.notifyDestroyed();
		}
		else if(c == loada){
			lesson.load("/mobilekatakana_lesson.xml");
			showCurrent();
		}
		else if(c == loadb){
			lesson.load("/mobilehiragana_lesson.xml");
			showCurrent();
		}
		else if(c == load1){
			lesson.load("/mobilekanji_lesson_1.xml");
			showCurrent();
		}
		else if(c == load2){
			lesson.load("/mobilekanji_lesson_2.xml");
			showCurrent();
		}
		else if(c == load3){
			lesson.load("/mobilekanji_lesson_3.xml");
			showCurrent();
		}
		else if(c == load4){
			lesson.load("/mobilekanji_lesson_4.xml");
			showCurrent();
		}
		else if(c == load5){
			lesson.load("/mobilekanji_lesson_5.xml");
			showCurrent();
		}
		else if(c == load6){
			lesson.load("/mobilekanji_lesson_6.xml");
			showCurrent();
		}

	}
}