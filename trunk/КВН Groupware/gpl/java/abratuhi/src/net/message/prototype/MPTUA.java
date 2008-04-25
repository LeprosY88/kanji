/**
 * @author Alexei Bratuhin
 * @licence GPLv2
 */

package gpl.java.abratuhi.src.net.message.prototype;

import java.util.ArrayList;

public class MPTUA extends Thread{
	
	/**/
	public ArrayList<Client> clients = new ArrayList<Client>();
	public ArrayList<Message> incoming = new ArrayList<Message>();
	public ArrayList<String> history = new ArrayList<String>();
	/**/
	public boolean isUp = false;
	
	/**/
	public MPTUA(){
		isUp = true;
		//System.out.println("MPTUA is UP.");
	}
	public MPTUA(ArrayList<Client> cls, ArrayList<Message> msgs, ArrayList<String> hist){
		this.isUp = true;
		this.clients = cls;
		this.incoming = msgs;
		this.history = hist;
		//System.out.println("MPTUA is UP.");
	}
	
	/**/
	public void run(){
		while(isUp){
			if(incoming.size()>0){
				Message msg = incoming.remove(0);
				history.add(msg.Message2String());
				//System.out.println("MPTUA status:\thas something to resend:\t"+msg.Message2String());
				if(msg.msg_to.equals(Message.MSG_TO_ALL)){/* Send to all */
					for (int i=0; i<clients.size(); i++){
						if(clients.get(i).isConnected){
							clients.get(i).sendMessage(msg);
						}
					}
				}
				else{/* Send to selected */
					boolean recepient_found = false;
					for (int i=0; i<clients.size(); i++){
						if(clients.get(i).isConnected && clients.get(i).idClient.equals(msg.msg_to)){
							clients.get(i).sendMessage(msg);
							recepient_found = true;
						}
					}
					if(!recepient_found){ /* in case recepient is not online, put message back into the queue */
						incoming.add(msg);
						history.remove(history.size()-1);
					}
				}
			}
		}
	}
	
	public void stopp(){
		this.isUp = false;
	}
	
	/*public void close(){
		
	}*/

}
