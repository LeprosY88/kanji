package gpl.java.abratuhi.src.ads;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MMORPG_Ad {
	
	public final static float MAX_FONT_SIZE = 50.0f;
	public final static int MAX_WIDTH = 100;
	public final static int MAX_HEIGHT = 50;
	
	public static String[] ADS = {"mmorpg_ad_1", "mmorpg_ad_2", "mmorpg_ad_3", "mmorpg_ad_4", "mmorpg_ad_5"};
	public static Color[] COLORS = {Color.BLACK, Color.CYAN, Color.MAGENTA, Color.PINK};
	
	private String adText = new String();
	private Font adFont = new Font("Courier New", Font.ITALIC, 10);
	private Rectangle adArea = new Rectangle();
	private float adAlpha = 1.0f;
	private Color adColor;
	private int adTTL = 0;
	
	public MMORPG_Ad(Rectangle r){
		this.adArea = r;
		this.adText = ADS[(int)(Math.random()*ADS.length)];
		this.adColor = COLORS[(int)(Math.random()*COLORS.length)];
		this.adFont = adFont.deriveFont(Math.min((float)(adArea.height + adArea.width), MAX_FONT_SIZE));
		//if(adFont == null) {System.out.println("Bad font!!"); System.exit(-1);}
		System.out.println("Ad created; "+"text="+adText+", in area=("+adArea.x+"|"+adArea.y+")"+", size="+adArea.width+"x"+adArea.height);
	}
	
	public void changeAdText(){
		this.adText = ADS[(int)(Math.random()*ADS.length)];
		this.adColor = COLORS[(int)(Math.random()*COLORS.length)];
	}
	
	public void draw(Graphics2D g){
		adTTL++;
		if(adTTL % 10 == 0) { changeAdText(); adTTL = 0;}
		
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, adAlpha);
		g.setComposite(ac);
		g.setColor(adColor);
		g.setFont(adFont);
		g.drawString(this.adText, this.adArea.x, this.adArea.y);
	}

}
