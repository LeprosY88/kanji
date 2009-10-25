package org.abratuhi.kanji.imagegenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.abratuhi.kanji.KanjiLesson;
import org.abratuhi.kanji.KanjiChar;

public class ImageGenerator {
	
	public void lesson2images(KanjiLesson lesson, String dir) throws IOException, FontFormatException
	{
		int x = 320;
		int y = 240;
		int padx = 0;
		int pady = y/10;
		int strlen = 25;
		int minxy = Math.min(x, y);
		int kanay = minxy/4;
		int hiraganay = x/strlen;
		int translationy = x/strlen;
		int padrow = 2;
		
		Font font = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("font/arialuni.ttf"));
		Font kanafont = font.deriveFont((float) kanay);
		Font hiraganafont = font.deriveFont((float) hiraganay);
		Font translatiofont = font.deriveFont((float) translationy);
		
		for(Iterator<KanjiChar> i = lesson.chars.iterator(); i.hasNext(); )
		{
			KanjiChar kanachar = i.next();
			
			System.out.println("Proceeding: " + kanachar.ch);
			
			BufferedImage bimage = getImage(x, y);
			
			Graphics2D g2d = (Graphics2D) bimage.createGraphics();
			
			g2d.setColor(Color.GRAY);
			g2d.drawRect(0, pady, kanay, kanay);
			
			g2d.setFont(kanafont);
			g2d.setColor(Color.BLACK);
			g2d.drawString(kanachar.ch, 0, pady + kanay - kanay/10);
			
			g2d.setFont(hiraganafont);
			
			int currenty = pady + kanay + 2*padrow + hiraganay;
			
			String readingon = kanachar.reading.substring(0, kanachar.reading.indexOf("KUN"));
			String readingkun = kanachar.reading.substring(kanachar.reading.indexOf("KUN"));
			
			
			for(int j=0; j<readingon.length()/strlen +1; j++)
			{
				if(j<readingon.length()/strlen){
					g2d.drawString(readingon.substring(j*strlen, (j+1)*strlen), 0, currenty);
				}
				else
				{
					g2d.drawString(readingon.substring(j*strlen), 0, currenty);
				}
				currenty += hiraganay + padrow;
			}
			
			for(int j=0; j<readingkun.length()/strlen +1; j++)
			{
				if(j<readingkun.length()/strlen){
					g2d.drawString(readingkun.substring(j*strlen, (j+1)*strlen), 0, currenty);
				}
				else
				{
					g2d.drawString(readingkun.substring(j*strlen), 0, currenty);
				}
				currenty += hiraganay + padrow;
			}
			
			currenty += padrow;
			
			g2d.setFont(translatiofont);
			for(int j=0; j<kanachar.translation.length()/strlen +1; j++)
			{
				if(j<kanachar.translation.length()/strlen){
					g2d.drawString(kanachar.translation.substring(j*strlen, (j+1)*strlen), 0, currenty);
				}
				else
				{
					g2d.drawString(kanachar.translation.substring(j*strlen), 0, currenty);
				}
				currenty += hiraganay + padrow;
			}
			
			ImageIO.write(bimage, "PNG", new File(dir + File.separator + kanachar.uid + ".png"));
		}
	}
	
	public BufferedImage getImage(int width, int height)
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice gs = ge.getDefaultScreenDevice();
	    GraphicsConfiguration gc = gs.getDefaultConfiguration();
	    
	    // Create an image that does not support transparency
	    BufferedImage bimage = gc.createCompatibleImage(width, height, Transparency.BITMASK);
	    
	    return bimage;

	}
	
	
	public static void main(String[] args)
	{
		KanjiLesson la = new KanjiLesson("xml/hiragana_lesson.xml");
		KanjiLesson lb = new KanjiLesson("xml/katakana_lesson.xml");
		KanjiLesson l1 = new KanjiLesson("xml/kanji_lesson_1.xml");
		KanjiLesson l2 = new KanjiLesson("xml/kanji_lesson_2.xml");
		KanjiLesson l3 = new KanjiLesson("xml/kanji_lesson_3.xml");
		KanjiLesson l4 = new KanjiLesson("xml/kanji_lesson_4.xml");
		KanjiLesson l5 = new KanjiLesson("xml/kanji_lesson_5.xml");
		KanjiLesson l6 = new KanjiLesson("xml/kanji_lesson_6.xml");
		
		ImageGenerator imggen = new ImageGenerator();
		
		try
		{
			/*imggen.lesson2images(la, "img/la");
			imggen.lesson2images(lb, "img/lb");
			imggen.lesson2images(l1, "img/l1");
			imggen.lesson2images(l2, "img/l2");
			imggen.lesson2images(l3, "img/l3");
			imggen.lesson2images(l4, "img/l4");
			imggen.lesson2images(l5, "img/l5");
			imggen.lesson2images(l6, "img/l6");*/
			
			imggen.lesson2images(la, "img");
			imggen.lesson2images(lb, "img");
			imggen.lesson2images(l1, "img");
			imggen.lesson2images(l2, "img");
			imggen.lesson2images(l3, "img");
			imggen.lesson2images(l4, "img");
			imggen.lesson2images(l5, "img");
			imggen.lesson2images(l6, "img");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
