package org.abratuhi.mmorpg.pubsub;

import java.util.ArrayList;

/**
 * Part of Mercury implementation. Hub Node.
 * @author Alexei Bratuhin
 *
 */
public class HubNode {
	/** reference to parent hub **/
	public Hub hub;
	
	/** minimal value of responsibility range **/
	public String minimalValue;
	/** maximal value of responsibility range **/
	public String maximalValue;
	
	/** list of stores subscriptions **/
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
	
	/**
	 * 
	 * @param sub	subscription to  (possibly) add
	 * @return	<ul><li>-1, if subscription doesn't have hubnode's attribute</li>
	 * 				<li>2, if already contains this subscription</li>
	 * 				<li>1, if subscription suits hubnode's range </li>
	 * 				<li>0, if subscription doesn't suit hubnode's range</li></ul>
	 */
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
	
	/**
	 * Match given publication against list of stored subscriptions.
	 * @param pub	publication
	 * @return		list of matched subscriptions
	 */
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
