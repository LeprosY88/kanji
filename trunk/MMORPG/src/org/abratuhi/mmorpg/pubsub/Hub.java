package org.abratuhi.mmorpg.pubsub;

import java.util.ArrayList;

public class Hub {
	
	public String attributeName = new String();
	public ArrayList<HubNode> attributeNodes = new ArrayList<HubNode>();
	
	public Hub(String attribute){
		this.attributeName = attribute;
	}
	
	public ArrayList<Subscription> match(Publication pub){
		Integer attributeIndex = pub.getAttributeIndex(attributeName);
		String pubvalue = pub.values[attributeIndex];
		for(int i=0; i<attributeNodes.size(); i++){
			String hnmin = (String) attributeNodes.get(i).minimalValue;
			String hnmax = (String) attributeNodes.get(i).maximalValue;
			if(Publication.less(hnmin, pubvalue) &&
					Publication.less(pubvalue, hnmax)){
				return attributeNodes.get(i).match(pub);
			}
		}
		return null;
	}
	
	public void addSubscription(Subscription sub){
		for(int i=0; i<attributeNodes.size(); i++){
			HubNode hn = attributeNodes.get(i);
			if(hn.acceptsSubscription(sub) == 1) hn.addSubscription(sub);
			while (needsBalanceLoad(hn)) performBalanceLoad(hn);
		}
	}
	
	public boolean needsBalanceLoad(HubNode hn){
		if(attributeNodes.size() < 2) {
			System.out.println("Hub contains single node: no load balancing possible");
			return false;
		}
		
		Integer hnOverloadedIndex = attributeNodes.indexOf(hn);
		Integer hnHelperIndex = (hnOverloadedIndex==0)? hnOverloadedIndex+1 : hnOverloadedIndex-1;
		
		HubNode hnHelper = attributeNodes.get(hnHelperIndex);
		
		if(hnHelper.subscriptions.size()*2 < hn.subscriptions.size()) {
			System.out.println("Load balancing possible:\n overloaded node: "+hn.toString()+"\n underloaded node: "+hnHelper.toString());
			return true;
		}
		else {
			System.out.println("Load balancing not possible:\n overloaded node: "+hn.toString()+"\n loaded node: "+hnHelper.toString());
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void performBalanceLoad(HubNode hnOverloaded){
		if(attributeNodes.size() < 2) return;
		
		Integer hnOverloadedIndex = attributeNodes.indexOf(hnOverloaded);
		Integer hnHelperIndex = (hnOverloadedIndex==0)? hnOverloadedIndex+1 : hnOverloadedIndex-1;
		
		HubNode hnHelper = attributeNodes.get(hnHelperIndex);
		
		String newBound = Publication.divide(Publication.sum(hnOverloaded.maximalValue, hnOverloaded.minimalValue), "2");
		if(hnOverloadedIndex < hnHelperIndex) {
			hnOverloaded.maximalValue = newBound;
			hnHelper.minimalValue = newBound;
		}
		else{
			hnOverloaded.minimalValue = newBound;
			hnHelper.maximalValue = newBound;
		}
		System.out.println(" new bound = "+newBound);
		
		ArrayList<Subscription> subs = (ArrayList<Subscription>) hnOverloaded.subscriptions.clone();
		hnOverloaded.subscriptions.clear();
		
		for(int i=0; i<subs.size(); i++){
			Subscription sub = subs.get(i);
			if(hnHelper.acceptsSubscription(sub) == 1) hnHelper.addSubscription(sub);
			if(hnOverloaded.acceptsSubscription(sub) == 1) hnOverloaded.addSubscription(sub);
		}
	}

}
