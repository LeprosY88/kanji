package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import model.Kanji_lesson;

public class Kanji_GUI_ja_sign extends JPanel{
	
	Kanji_GUI root;
	Kanji_lesson lesson;
	
	Dimension d = new Dimension(150, 150);
	int t = 4;	// width of line drawn
	
	public Kanji_GUI_ja_sign(Kanji_GUI r, Kanji_lesson l){
		// super
		super();
		
		// kanji_gui, kanji_lesson
		root = r;
		lesson = l;
		
		// mouse listener
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				
			}
			public void mouseReleased(MouseEvent e){
				
			}
			public void mouseClicked(MouseEvent e){
				if(e.getButton() == MouseEvent.BUTTON3){
					// init graphics
					Graphics2D g = (Graphics2D) e.getComponent().getGraphics();
					// get rectangle to clear
					Rectangle area = e.getComponent().getBounds(); 
					// clear
					g.clearRect(area.x, area.y, area.width, area.height);
					// add border that disappeared after clear
					((JPanel)e.getComponent()).setBorder(new TitledBorder(""));
					// add char that dissapeared after clear
					if(root.lesson!=null) paintChar(root.lesson.getActiveChar().ch);
				}
			}
		});
		
		// mouse motion listener
		addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e){
				if(e.getModifiers()==MouseEvent.BUTTON1_MASK){
					// init graphics
					Graphics2D g = (Graphics2D) e.getComponent().getGraphics();
					// draw
					Point p = e.getPoint();
					g.fillOval(p.x-t, p.y-t, 2*t, 2*t);
				}
			}
		});
		
		// kanji_gui_ja_sign
		setBorder(new TitledBorder(""));
		setPreferredSize(d);
		setVisible(true);
		
	}
	
	public void paintChar(String ch){
		// get graphics
		Graphics2D g = (Graphics2D) getGraphics();
		// clear
		Rectangle area = new Rectangle(getX(), getY(), getWidth(), getHeight());
		g.clearRect(area.x, area.y, area.width, area.height);
		// draw char
		g.setColor(Color.GREEN);
		g.setFont(g.getFont().deriveFont((float)Math.min(getHeight(), getWidth())));
		int font_ascent = g.getFontMetrics().getAscent();
		int font_descent = g.getFontMetrics().getDescent();
		int font_height = g.getFontMetrics().getHeight();
		int font_leading = g.getFontMetrics().getLeading();
		g.drawString(ch, getX()+getWidth()*3/40, getY()+font_height*25/40);
	}

}
