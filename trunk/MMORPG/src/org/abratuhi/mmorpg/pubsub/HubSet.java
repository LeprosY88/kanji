package org.abratuhi.mmorpg.pubsub;

import java.util.ArrayList;

/**
 * Part of Mercury implementation. Hub Collection.
 * Used as server for incoming publications and subscriptions.
 * @author Alexei Bratuhin
 *
 */
public class HubSet {
	
	/** List of attribute hubs **/
	ArrayList<Hub> hubs = new ArrayList<Hub>();
	
	public HubSet(){
		
	}
	
	/**
	 * Add attribute hub to collection of hubs.
	 * @param h	attribute hub
	 */
	public void addHub(Hub h){
		for(int i=0; i<hubs.size(); i++){
			if(hubs.get(i).attributeName.equals(h.attributeName)){
				return;
			}
		}
		hubs.add(h);
	}
	
	/**
	 * Add new subscription.
	 * The given subscription will be stored at every attribute hub corresponding to each of subscription's attribute
	 * @param sub	subscription to add
	 */
	public void addSubscription(Subscription sub){
		for(int i=0; i<hubs.size(); i++){
			hubs.get(i).addSubscription(sub);
		}
	}
	
	/** 
	 * Match a publication against all stored subscriptions.
	 * @param pub	publication to match
	 * @return	list of matched subscriptions
	 */
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
	
	/**
	 * Build intersection between two list of matched subscriptions
	 * @param l1	list of matched subscriptions
	 * @param l2	another list of matched subscriptions
	 * @return		intersection of both given lists
	 */
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
