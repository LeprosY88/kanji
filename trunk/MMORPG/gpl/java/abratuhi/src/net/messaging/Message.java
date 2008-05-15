/**
 * Message
 * <message type="login/logout/..." text=...>
 * 		<from type="..." id="..." />
 * 		<to type="unicast/multicast/broadcast" gid="..." id="..." />
 * 		<position x="..." y="..." />
 * 		<player class="..." strength="..." />
 * < /message>
 */

package gpl.java.abratuhi.src.net.messaging;

import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

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
		//System.out.println("creating root element");
		Element root = new Element("message");
		//System.out.println("creatiing doc from root element");
		d = new Document(root);
	}

	public Message(String type, String text, String from_id, String to_type, String to_id){		
		Element root = new Element("message");
		d = new Document(root);
		root.setAttribute("type", type);
		root.setAttribute("text", text);
		Element from = new Element("from");
		from.setAttribute("id", from_id);
		root.addContent(from);
		Element to = new Element("to");
		to.setAttribute("type", to_type);
		to.setAttribute("id", to_id);
		root.addContent(to);
	}

	public Message(String type, String text, String from_id, String to_type, String to_id, Point p){
		Element root = new Element("message");
		d = new Document(root);
		root.setAttribute("type", type);
		root.setAttribute("text", text);
		Element from = new Element("from");
		from.setAttribute("id", from_id);
		root.addContent(from);
		Element to = new Element("to");
		to.setAttribute("type", to_type);
		to.setAttribute("id", to_id);
		root.addContent(to);
		Element pos = new Element("position");
		pos.setAttribute("x", String.valueOf(p.x));
		pos.setAttribute("y", String.valueOf(p.y));
		root.addContent(pos);
	}

	public static Message createMessage(){
		//System.out.println("MESSAGE: createMessage");
		return new Message();
	}
	public static Message createMessage(String type, String text, String from_id, String to_type, String to_id){
		return new Message(type, text, from_id, to_type, to_id);
	}

	public String getType(){
		return d.getRootElement().getAttributeValue("type");
	}
	public String getText(){
		return d.getRootElement().getAttributeValue("text");
	}
	public String getFromId(){
		return d.getRootElement().getChild("from").getAttributeValue("id");
	}
	public String getToType(){
		return d.getRootElement().getChild("to").getAttributeValue("type");
	}
	public String getToId(){
		return d.getRootElement().getChild("to").getAttributeValue("id");
	}
	public Point getPosition(){
		return new Point(Integer.valueOf(d.getRootElement().getChild("position").getAttributeValue("x")),
				Integer.valueOf(d.getRootElement().getChild("position").getAttributeValue("y")));
	}

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

}
