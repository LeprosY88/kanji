package gpl.java.abratuhi.src.net.message.prototype;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Client client = new Client();
		client.connect("localhost", 9000);
		client.start();
		
		while(true){
			System.out.print("type>\t");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				String msg = br.readLine();
				if(msg.equalsIgnoreCase("quit")) {
					client.stopp();
					client.logout();
					client.close();
					break;
				}
				else{
					client.sendMessage(new Message(Message.MSG_TYPE_CHAT, client.idClient, Message.MSG_TO_ALL, Message.MSG_HEAD_CHAT_MESSAGE, msg));
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}

	}

}
