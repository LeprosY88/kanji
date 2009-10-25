package org.abratuhi.mobilekanji;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.abratuhi.mobilekanji.model.Lesson;
import org.abratuhi.mobilekanji.model.LessonUnit;

public class KanjiMidlet extends MIDlet implements CommandListener{
	private Display  display;     // The display
	private KanjiCanvas canvas;   // Canvas 

	public KanjiMidlet(){
		display = Display.getDisplay(this);
		canvas  = new KanjiCanvas(this);

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
