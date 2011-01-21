package org.abratuhi.bahnde.output;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.abratuhi.bahnde.model.RouteEdge;
import org.abratuhi.bahnde.model.Station;
import org.apache.log4j.Logger;

public class Route implements Printable{
	
	private final static Logger LOG = Logger.getLogger(Route.class);
	
	private final List<Station> stations;
	private final List<RouteEdge> route;
	
	public Route(List<Station> stations, List<RouteEdge> route){
		this.stations = stations;
		this.route = route;
	}

	@Override
	public int print(Graphics g, PageFormat pageFormat, int pp)
			throws PrinterException {
		
		if(pp > 0){
			return NO_SUCH_PAGE;
		}
		else{
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
	
	public String getText(){
		StringBuffer result = new StringBuffer();
		
		for(int i=0; i<route.size(); i++){
			RouteEdge edge = route.get(i);
			result.append(i + ") " + edge.getDepartureStation().getName() + " at " + new SimpleDateFormat("HH:mm").format(edge.getDeparture()) + " --- " + edge.getDuration() + " --> " + edge.getArrivalStation().getName() + "\n");
		}
		
		return result.toString();
	}

	public List<Station> getStations() {
		return stations;
	}

	public List<RouteEdge> getRoute() {
		return route;
	}

}
