package org.abratuhi.mmorpg.pubsub;

import java.util.ArrayList;

public class HubSet {
	
	ArrayList<Hub> hubs = new ArrayList<Hub>();
	
	public HubSet(){
		
	}
	
	public void addHub(Hub h){
		for(int i=0; i<hubs.size(); i++){
			if(hubs.get(i).attributeName.equals(h.attributeName)){
				return;
			}
		}
		hubs.add(h);
	}
	
	public void addSubscription(Subscription sub){
		for(int i=0; i<hubs.size(); i++){
			hubs.get(i).addSubscription(sub);
		}
	}
	
	public ArrayList<Subscription> match(Publication pub){
		ArrayList<Subscription> result = new ArrayList<Subscription>();
		if(hubs.size() > 0){
			result = hubs.get(0).match(pub);
			if(hubs.size() > 1){
				for(int i=1; i<hubs.size(); i++){
					result = intersection(result, hubs.get(i).match(pub));
				}
			}
		}
		return result;
	}
	
	public ArrayList<Subscription> intersection(ArrayList<Subscription> l1, ArrayList<Subscription> l2){
		ArrayList<Subscription> result = new ArrayList<Subscription>();
		for(int i=0; i<l1.size(); i++){
			if(l2.contains(l1.get(i))){
				result.add(l1.get(i));
			}
		}
		return result;
	}
	
	public String toString(){
		String out = new String();
		out += "\n";
		out += "HUBSET:\n";
		for(int k=0; k<hubs.size(); k++){
			Hub hubk = hubs.get(k);
			out += "HUB: " + hubk.attributeName + "\n";
			for(int i=0; i<hubk.attributeNodes.size(); i++){
				HubNode hn = hubk.attributeNodes.get(i);
				out += "hubnode #"+i;
				out += "; "+hn.minimalValue+"<"+hubk.attributeName+"<"+hn.maximalValue+"\n";
				for(int j=0; j<hn.subscriptions.size(); j++){
					out += hn.subscriptions.get(j) + "\n";
				}
			}
		}
		return out;
	}

}
