package org.abratuhi.mmorpg.pubsub;

import java.util.ArrayList;

public class HubNode {
	public Hub hub;
	
	public String minimalValue;
	public String maximalValue;
	
	ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();
	
	public HubNode(Hub hub, String min, String max){
		this.hub = hub;
		this.minimalValue = min;
		this.maximalValue = max;
	}
	
	public void addSubscription(Subscription sub){
		if(!containsSubscription(sub)) this.subscriptions.add(sub);
	}
	public Subscription getSubscription(String subscriptionId){
		for(int i=0; i<this.subscriptions.size(); i++){
			if(this.subscriptions.get(i).id.equals(subscriptionId)) return this.subscriptions.get(i);
		}
		return null;
	}
	public boolean containsSubscription(Subscription sub){
		return this.subscriptions.contains(sub);
	}
	public boolean containsSubscriptionId(String subscriptionId){
		for(int i=0; i<this.subscriptions.size(); i++){
			if(this.subscriptions.get(i).id.equals(subscriptionId)) return true;
		}
		return false;
	}
	public Integer acceptsSubscription(Subscription sub){
		Integer attributeIndex = sub.getAttributeIndex(hub.attributeName);
		if(attributeIndex == -1) return -1;
		
		if(containsSubscription(sub)) return 2;
		
		String submin = sub.minimums[attributeIndex];
		String submax = sub.maximums[attributeIndex];
		
		String hnmin = minimalValue;
		String hnmax = maximalValue;
		
		if(Publication.lessorequal(submin, submax)){
			boolean allLess = ( Publication.less(submin, hnmin) &&
					Publication.less(submax, hnmin) ) ? true : false;
			boolean allGreater = (Publication.lessorequal(hnmax, submin) &&
					Publication.less(hnmax, submax)) ? true: false;
			if( !( allLess || allGreater) ){
				return 1;
			}
			else{
			}
		}
		else{
		if(Publication.less(submax, submin)){
			boolean minGreater = (Publication.less(submax, hnmin)) ? true: false;
			boolean maxLess = (Publication.less(hnmax, submin)) ? true : false;
			if(! ( minGreater && maxLess ) ){
				return 1;
			}
			else{
			}
		}
		}
		return 0;
	}
	public void removeSubscription(Subscription sub){
		this.subscriptions.remove(sub);
	}
	public void removeSubscription(String subscriptionId){
		Subscription sub = this.getSubscription(subscriptionId);
		if(sub != null) this.removeSubscription(sub);
	}
	
	public ArrayList<Subscription> match(Publication pub){
		ArrayList<Subscription> result = new ArrayList<Subscription>();
		for(int i=0; i<subscriptions.size(); i++){
			if(subscriptions.get(i).match(pub)){
				result.add(subscriptions.get(i));
			}
		}
		return result;
	}
	
	public String toString(){
		return "hubnode: "+minimalValue+" < "+hub.attributeName+" < "+maximalValue;
	}

}
