package org.abratuhi.mmorpg.net;

/**
 * Message
 * <message type="login/logout/..." id=...>
 * 		<to cast="unicast/multicast/broadcast" gid="..." id(s)="..." />
 * 		<from type="..." id="..." />
 * 		<data>
 * 			<position x="..." y="..." />
 * 			<player class="..." strength="..." />
 * 		</data>
 * < /message>
 */

import java.awt.Point;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.abratuhi.mmorpg.model.MMORPG_Hero;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class Message {

	/**/
	public Document d;

	/**/
	public Message(){
		Element root = new Element("message");
		d = new Document(root);
	}
	
	public Message(Element message){
		d = new Document(message);
	}
	
	public static Message createMessage(){
		return new Message();
	}
	
	/*public static Message createUnicastMessage(MMORPG_Hero heroFrom, MMORPG_Hero heroTo, String type, String forwarding){
		Element root = new Element("message")
						.setAttribute("type", type)
						.setAttribute("forward", forwarding)
						.addContent(new Element("from")
							.setAttribute("id", heroFrom.name))
						.addContent(new Element("to")
							.setAttribute("cast", "unicast")
							.setAttribute("id", heroTo.name))
						.addContent(new Element("data")
							.addContent(new Element("position")
								.setAttribute("x", String.valueOf(heroFrom.p.x))
								.setAttribute("y", String.valueOf(heroFrom.p.y))))
							.addContent(new Element("chat")
								.setText(heroFrom.messageChat));
		return new Message(root);
	}*/
	/*public static Message createBroadcastMessage(MMORPG_Hero heroFrom, String type, String forwarding){
		Element root = new Element("message")
						.setAttribute("type", type)
						.setAttribute("forward", forwarding)
						.addContent(new Element("from")
							.setAttribute("id", heroFrom.name))
						.addContent(new Element("to")
							.setAttribute("cast", "broadcast")
							.setAttribute("id", ""))
						.addContent(new Element("data")
							.addContent(new Element("position")
								.setAttribute("x", String.valueOf(heroFrom.p.x))
								.setAttribute("y", String.valueOf(heroFrom.p.y))))
							.addContent(new Element("chat")
								.setText(heroFrom.messageChat));
		return new Message(root);
	}*/
	/*public static Message createMulticastMessage(MMORPG_Hero heroFrom, ArrayList<MMORPG_Hero> heroesTo, String type, String forwarding){
		String ids = new String();
		for(int i=0; i<heroesTo.size(); i++){
			ids += "," + heroesTo.get(i).name;
		}
		ids.replaceFirst(",", "");
		
		Element root = new Element("message")
						.setAttribute("type", type)
						.setAttribute("forward", forwarding)
						.addContent(new Element("from")
							.setAttribute("id", heroFrom.name))
						.addContent(new Element("to")
							.setAttribute("cast", "unicast")
							.setAttribute("id", ids))
						.addContent(new Element("data")
							.addContent(new Element("position")
								.setAttribute("x", String.valueOf(heroFrom.p.x))
								.setAttribute("y", String.valueOf(heroFrom.p.y))))
							.addContent(new Element("chat")
								.setText(heroFrom.messageChat));
		return new Message(root);
	}*/
	
	public String toString(){
		XMLOutputter xmlout = new XMLOutputter();
		StringWriter sw = new StringWriter();
		try {
			xmlout.output(d, sw);
			return sw.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void fromString(String s){
		SAXBuilder saxb = new SAXBuilder();
		StringReader sr = new StringReader(s);
		try {
			d = saxb.build(sr);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*public String[] getToIds(){
		String cast = d.getRootElement().getChild("to").getAttributeValue("cast");
		if(cast.equals("broadcast")){
			return new String[0];
		}
		if(cast.equals("unicast")){
			String id = d.getRootElement().getChild("to").getAttributeValue("id");
			return new String[]{id};
		}
		if(cast.equals("multicast")){
			String id = d.getRootElement().getChild("to").getAttributeValue("id");
			StringTokenizer st = new StringTokenizer(id, ",");
			int nids = st.countTokens();
			String[] ids = new String[nids];
			int i = 0;
			while(st.hasMoreTokens()){
				ids[i] = st.nextToken();
				i++;
			}
		}
		return null;
	}*/
	
	/*public Point getFromPosition(){
		String x = d.getRootElement().getChild("data").getChild("position").getAttributeValue("x");
		String y = d.getRootElement().getChild("data").getChild("position").getAttributeValue("y");
		return new Point(Integer.parseInt(x), Integer.parseInt(y));
	}*/
	
	/*public String getFromId(){
		return d.getRootElement().getChild("from").getAttributeValue("id");
	}*/
	
	/*public String getType(){
		return d.getRootElement().getAttributeValue("type");
	}*/
	
	/*public String getChatMessage(){
		return d.getRootElement().getChild("data").getChild("chat").getText();
	}*/

}
