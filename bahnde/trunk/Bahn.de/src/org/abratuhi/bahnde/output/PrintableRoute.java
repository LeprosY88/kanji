package org.abratuhi.bahnde.output;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;

import org.abratuhi.bahnde.model.Station;

public class PrintableRoute implements Printable{
	
	private List<Station> stations;
	
	public PrintableRoute(List<Station> stations){
		this.stations = stations;
	}

	@Override
	public int print(Graphics g, PageFormat pageFormat, int arg2)
			throws PrinterException {
		
		Graphics2D gg = (Graphics2D) g;
		
		gg.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		
		gg.setFont(new Font("Helvetica", Font.PLAIN, 20));
	    gg.setPaint(Color.BLACK);
	    gg.setColor(Color.BLACK);
		
	    for(int i=0; i<stations.size(); i++){
	    	Station station = stations.get(i);
	    	gg.drawString(station.getName(), 10, 20 * (i+1));
	    }
		
		return PAGE_EXISTS;
	}

}
