package gpl.java.abratuhi.src.net.message.prototype;

public class ChatServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Server server = new Server();
		server.up(9000);
		server.start();

	}

}
