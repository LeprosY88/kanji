package org.abratuhi.mmorpg.chatroom.net;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.abratuhi.mmorpg.chatroom.model.ChatRoom;
import org.abratuhi.mmorpg.chatroom.net.Message;
import org.jdom.Element;

public class MessageUtil {
	
	public final static String MSGTYPE_INIT_CLIENT = "initClient";
	public final static String MSGTYPE_DSTR_CLIENT = "destroyClient";
	public final static String MSGTYPE_ERROR = "error";
	
	public final static String MSGTYPE_CREATE_ROOM = "createRoom";
	public final static String MSGTYPE_JOIN_ROOM = "joinRoom";
	public final static String MSGTYPE_LEAVE_ROOM = "leaveRoom";;
	public final static String MSGTYPE_LIST_ROOM = "listRooms";;
	public final static String MSGTYPE_DELETE_ROOM = "deleteRoom";
	public final static String MSGTYPE_UPDATE_ROOM = "updateRoom";
	
	public final static String MSGTYPE_REQUPDATE_ROOM = "requestUpdateRoom";
	
	public final static String MSGTYPE_CHAT = "chat";
	
	public final static String MSGCAST_UNICAST = "unicast";
	public final static String MSGCAST_MULTICAST = "multicast";
	public final static String MSGCAST_ROOMBROADCAST = "roombroadcast";
	public final static String MSGCAST_BROADCAST = "broadcast";

	public static Message createInitClientMessage(C_Client client) {
		Element message = new Element("message")
						.setAttribute("type", MSGTYPE_INIT_CLIENT)
						.setAttribute("room", "system")
						.addContent(new Element("from")
							.setAttribute("id", client.id))
						.addContent(new Element("to")
							.setAttribute("cast", MSGCAST_BROADCAST))
						.addContent(new Element("text"));
		return new Message(message);
	}
	
	public static Message createDestroyClientMessage(C_Client client) {
		Element message = new Element("message")
						.setAttribute("type", MSGTYPE_DSTR_CLIENT)
						.setAttribute("room", "system")
						.addContent(new Element("from")
							.setAttribute("id", client.id))
						.addContent(new Element("to")
							.setAttribute("cast", MSGCAST_BROADCAST))
						.addContent(new Element("text"));
		return new Message(message);
	}
	
	public static Message createErrorMessage(Server s, S_Client sclient, String text) {
		Element message = new Element("message")
						.setAttribute("type", MSGTYPE_ERROR)
						.setAttribute("room", "system")
						.addContent(new Element("from")
							.setAttribute("id", ""))
						.addContent(new Element("to")
							.setAttribute("cast", MSGCAST_UNICAST)
							.setAttribute("id", sclient.id))
						.addContent(new Element("text").
								setText(text));
		return new Message(message);
	}
	
	public static Message createCreateRoomMessage(C_Client client, String roomname){
		Element message = new Element("message")
						.setAttribute("type", MSGTYPE_CREATE_ROOM)
						.setAttribute("room", "system")
						.addContent(new Element("from")
							.setAttribute("id", client.id))
						.addContent(new Element("to")
							.setAttribute("cast", MSGCAST_BROADCAST))
						.addContent(new Element("text")
							.setText(roomname));
		return new Message(message);
	}
	public static Message createJoinRoomMessage(C_Client client, String roomname){
		Element message = new Element("message")
						.setAttribute("type", MSGTYPE_JOIN_ROOM)
						.setAttribute("room", "system")
						.addContent(new Element("from")
							.setAttribute("id", client.id))
						.addContent(new Element("to")
							.setAttribute("cast", MSGCAST_BROADCAST))
						.addContent(new Element("text")
							.setText(roomname));
		return new Message(message);
	}
	public static Message createListRoomMessage(Server server, S_Client sclient){
		Element message = new Element("message")
						.setAttribute("type", MSGTYPE_LIST_ROOM)
						.setAttribute("room", "system")
						.addContent(new Element("from")
							.setAttribute("id", "server"))
						.addContent(new Element("to")
							.setAttribute("cast", MSGCAST_UNICAST)
							.setAttribute("id", sclient.id))
						.addContent(new Element("text"));
		Iterator roomIterator = server.chatroomMap.entrySet().iterator();
		while(roomIterator.hasNext()){
			Map.Entry roomEntry = (Entry) roomIterator.next();
			ChatRoom cChatRoom = (ChatRoom) roomEntry.getValue();
			message.getChild("text").addContent(new Element("room").setText(cChatRoom.name));
		}
		return new Message(message);
	}
	public static Message createLeaveRoomMessage(C_Client client, String roomname){
		Element message = new Element("message")
						.setAttribute("type", MSGTYPE_LEAVE_ROOM)
						.setAttribute("room", "system")
						.addContent(new Element("from")
							.setAttribute("id", client.id))
						.addContent(new Element("to")
							.setAttribute("cast", MSGCAST_BROADCAST))
						.addContent(new Element("text")
							.setText(roomname));
		return new Message(message);
	}
	public static Message createDeleteRoomMessage(C_Client client, String roomname){
		Element message = new Element("message")
						.setAttribute("type", MSGTYPE_DELETE_ROOM)
						.setAttribute("room", "system")
						.addContent(new Element("from")
							.setAttribute("id", client.id))
						.addContent(new Element("to")
							.setAttribute("cast", MSGCAST_BROADCAST))
						.addContent(new Element("text")
							.setText(roomname));
		return new Message(message);
	}
	
	public static Message createUpdateRoomMessage(Server server, S_Client sclient, String roomName){
		Element message = new Element("message")
						.setAttribute("type", MSGTYPE_UPDATE_ROOM)
						.setAttribute("room", "system")
						.addContent(new Element("from")
							.setAttribute("id", "server"))
						.addContent(new Element("to")
							.setAttribute("cast", MSGCAST_UNICAST)
							.setAttribute("id", sclient.id))
						.addContent(new Element("text")
							.addContent(new Element("room")
								.setText(roomName)));
		
		ChatRoom cChatRoom = server.chatroomMap.get(roomName);
		for(int i=0; i<cChatRoom.users.size(); i++){
			message.getChild("text").getChild("room").addContent(new Element("user").setText((cChatRoom.users.get(i))));
		}
		
		return new Message(message);
	}
	
	public static Message createReqUpdateRoomMessage(C_Client client, String roomname){
		Element message = new Element("message")
						.setAttribute("type", MSGTYPE_REQUPDATE_ROOM)
						.setAttribute("room", "system")
						.addContent(new Element("from")
							.setAttribute("id", client.id))
						.addContent(new Element("to")
							.setAttribute("cast", MSGCAST_BROADCAST))
						.addContent(new Element("text")
							.setText(roomname));
		return new Message(message);
	}
	
	
	public static Message createChatMessageBroadcast(C_Client client, String text){
		Element message = new Element("message")
						.setAttribute("type", MSGTYPE_CHAT)
						.setAttribute("room", "")
						.addContent(new Element("from")
							.setAttribute("id", client.id))
						.addContent(new Element("to")
							.setAttribute("cast", MSGCAST_BROADCAST))
						.addContent(new Element("text")
							.setText(text));
		return new Message(message);
	}
	
	public static Message createChatMessageRoomBroadcast(C_Client client, String room, String text){
		Element message = new Element("message")
						.setAttribute("type", MSGTYPE_CHAT)
						.setAttribute("room", room)
						.addContent(new Element("from")
							.setAttribute("id", client.id))
						.addContent(new Element("to")
							.setAttribute("cast", MSGCAST_ROOMBROADCAST))
						.addContent(new Element("text")
							.setText(text));
		return new Message(message);
	}
	
	public static Message createChatMessageUnicast(C_Client client, String room, String to, String text){
		Element message = new Element("message")
						.setAttribute("type", MSGTYPE_CHAT)
						.setAttribute("room", room)
						.addContent(new Element("from")
							.setAttribute("id", client.id))
						.addContent(new Element("to")
							.setAttribute("cast", MSGCAST_UNICAST)
							.setAttribute("id", to))
						.addContent(new Element("text")
							.setText(text));
		return new Message(message);
	}
	
	public static String getType(Message m){
		if(m.d.getRootElement().getAttributeValue("type") == null){
			System.out.println("MessageUtil:\tError in getType() in message:\n"+m.toString());
		}
		return m.d.getRootElement().getAttributeValue("type");
	}
	
	public static String getRoom(Message m){
		return m.d.getRootElement().getAttributeValue("room");
	}
	
	public static String getFrom(Message m){
		return m.d.getRootElement().getChild("from").getAttributeValue("id");
	}
	
	public static String getCast(Message m){
		return m.d.getRootElement().getChild("to").getAttributeValue("cast");
	}
	
	public static String getTo(Message m){
		return m.d.getRootElement().getChild("to").getAttributeValue("id");
	}
	
	public static String getText(Message m){
		return m.d.getRootElement().getChild("text").getText();
	}
	
	public static String[] getRoomsFromText(Message m){
		List<Element> rooms = m.d.getRootElement().getChild("text").getChildren("room");
		String[] roomNames = new String[rooms.size()];
		for(int i=0; i<rooms.size(); i++){
			roomNames[i] = rooms.get(i).getText();
		}
		return roomNames;
	}
	
	public static String[] getUsersFromText(Message m){
		List<Element> users = m.d.getRootElement().getChild("text").getChild("room").getChildren("user");
		String[] userNames = new String[users.size()];
		for(int i=0; i<users.size(); i++){
			userNames[i] = users.get(i).getText();
		}
		return userNames;
	}

}
