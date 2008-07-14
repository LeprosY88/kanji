package org.abratuhi.mmorpg.util;

import java.awt.Point;
import java.util.StringTokenizer;

import org.abratuhi.mmorpg.client.MMORPG_Client;
import org.abratuhi.mmorpg.net.messaging.C_Client;
import org.abratuhi.mmorpg.net.messaging.Message;
import org.abratuhi.mmorpg.net.messaging.S_Client;
import org.jdom.Document;
import org.jdom.Element;

public class MessageUtil {
	
	public final static String MSGTYPE_INIT_CLIENT = "initClient";
	
	public final static String MSGCAST_UNICAST = "unicast";
	public final static String MSGCAST_MULTICAST = "multicast";
	public final static String MSGCAST_BROADCAST = "broadcast";
	public final static String MSGCAST_NEIGHCAST = "neighbourcast";
	
	public static Message createInitClientMessage(C_Client cclient){
		Element message = new Element("message")
								.setAttribute("type", MSGTYPE_INIT_CLIENT)
								.setAttribute("forward", "0")
								.addContent(new Element("from")
									.setAttribute("id", cclient.id))
								.addContent(new Element("data")
									.addContent(new Element("position")
										.setAttribute("x", String.valueOf(cclient.position.x))
										.setAttribute("y", String.valueOf(cclient.position.y))));
		return new Message(message);
	}
	
	public static Message createSendPositionMessage(MMORPG_Client client){
		Element message = new Element("message")
								.setAttribute("type", "game")
								.setAttribute("forward", "1")
								.addContent(new Element("from")
									.setAttribute("id", client.hero.name))
								.addContent(new Element("to"))
									.setAttribute("cast", MSGCAST_NEIGHCAST)
								.addContent(new Element("data")
									.addContent(new Element("position")
										.setAttribute("x", String.valueOf(client.hero.p.x))
										.setAttribute("y", String.valueOf(client.hero.p.y))));
		return new Message(message);
	}
	
	public static Message createChatMessage(MMORPG_Client client, String msg, String[] ids){
		String idz = new String();
		for(int i=0; i<ids.length; i++){
			idz += ","+ids[i];
		}
		idz = idz.substring(1);
		
		Element message = new Element("message")
								.setAttribute("type", "chat")
								.setAttribute("forward", "0")
								.addContent(new Element("from")
									.setAttribute("id", client.hero.name))
								.addContent(new Element("to"))
									.setAttribute("cast", MSGCAST_MULTICAST)
									.setAttribute("id", idz)
								.addContent(new Element("data")
									.addContent(new Element("position")
										.setAttribute("x", String.valueOf(client.hero.p.x))
										.setAttribute("y", String.valueOf(client.hero.p.y))));
		return new Message(message);
	}
	
	public static Message neighcast2multicast(S_Client sc, Message m){
		if(getType(m).equals(MSGCAST_NEIGHCAST)){
			String toIds = new String();
			for(int i=0; i<sc.neighbours.size(); i++){
				toIds += ","+sc.neighbours.get(i);
			}
			toIds = toIds.substring(1);
			
			m.d.getRootElement().getChild("to")
					.setAttribute("cast", MSGCAST_MULTICAST)
					.setAttribute("id", toIds);

			return m;
		}
		return m;
	}
	
	public static String getFromId(Message m){
		if(m.d.getRootElement().getChild("from") == null){
			System.out.println("MessageUtil:\t Error proceeding message: no 'from' child found");
			System.out.println(m.toString());
			return null;
		}
		if(m.d.getRootElement().getChild("from").getAttribute("id") == null){
			System.out.println("MessageUtil:\t Error proceeding message: no 'id' attribute in 'from' child found");
			System.out.println(m.toString());
			return null;
		}
		return m.d.getRootElement().getChild("from").getAttributeValue("id");
	}
	
	public static String getToCast(Message m){
		return m.d.getRootElement().getChild("to").getAttributeValue("cast");
	}
	
	public static String[] getToIds(Message m){
		Document d = m.d;
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
			return ids;
		}
		System.out.println("MessageUtil:\t Error proceeding message: 'getToIds' returned 'null'.");
		System.out.println(m.toString());
		return null;
	}
	
	public static String getType(Message m){
		if(m.d.getRootElement().getAttribute("type") == null){
			System.out.println("MessageUtil:\t Error proceeding message: no 'type' attribute found");
			System.out.println(m.toString());
			return null;
		}
		return m.d.getRootElement().getAttributeValue("type");
	}
	
	public static Point getFromPosition(Message m){
		if(m.d.getRootElement().getChild("data") == null){
			System.out.println("MessageUtil:\t Error proceeding message: no 'data' child found");
			System.out.println(m.toString());
			return null;
		}
		if(m.d.getRootElement().getChild("data").getChild("position") == null){
			System.out.println("MessageUtil:\t Error proceeding message: no 'position' child in 'data' child found");
			System.out.println(m.toString());
			return null;
		}
		if(m.d.getRootElement().getChild("data").getChild("position").getAttribute("x") == null){
			System.out.println("MessageUtil:\t Error proceeding message: no 'x' attribute in 'position' child in 'data' child found");
			System.out.println(m.toString());
			return null;
		}
		if(m.d.getRootElement().getChild("data").getChild("position").getAttribute("y") == null){
			System.out.println("MessageUtil:\t Error proceeding message: no 'y' attribute in 'position' child in 'data' child found");
			System.out.println(m.toString());
			return null;
		}
		
		int x = Integer.parseInt(m.d.getRootElement().getChild("data").getChild("position").getAttributeValue("x"));
		int y = Integer.parseInt(m.d.getRootElement().getChild("data").getChild("position").getAttributeValue("y"));
		return new Point(x,y);
	}
	
	public static int getForwarding(Message m){
		if(m.d.getRootElement().getAttribute("forwarding") == null){
			System.out.println("MessageUtil:\t Error proceeding message: no 'forwarding' attribute found");
			System.out.println(m.toString());
			return -1;
		}
		
		return Integer.valueOf(m.d.getRootElement().getAttributeValue("forwarding"));
	}

}
