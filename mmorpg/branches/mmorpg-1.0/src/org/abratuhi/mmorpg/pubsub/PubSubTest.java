package org.abratuhi.mmorpg.pubsub;

import java.util.ArrayList;

public class PubSubTest {
	
	public static void main(String[] args){
		HubSet hubset = new HubSet();
		
		Hub hubx = new Hub("x");
		Hub huby = new Hub("y");
		
		Integer nhubnodesx = 3;
		HubNode[] hnsx = new HubNode[nhubnodesx];
		for(int i=0; i<nhubnodesx; i++){
			hnsx[i] = new HubNode(hubx, String.valueOf(i*10), String.valueOf((i+1)*10));
			hubx.attributeNodes.add(hnsx[i]);
		}
		
		Integer nhubnodesy = 2;
		HubNode[] hnsy = new HubNode[nhubnodesy];
		for(int i=0; i<nhubnodesy; i++){
			hnsy[i] = new HubNode(huby, String.valueOf(i*15), String.valueOf((i+1)*15));
			huby.attributeNodes.add(hnsy[i]);
		}
		
		hubset.addHub(hubx);
		hubset.addHub(huby);
		
		Subscription sub1 = new Subscription("sub1", new String[]{"x", "y"}, new String[]{"0", "0"}, new String[]{"3", "3"});
		Subscription sub2 = new Subscription("sub2", new String[]{"x", "y"}, new String[]{"2", "10"}, new String[]{"4", "14"});
		Subscription sub3 = new Subscription("sub3", new String[]{"x", "y"}, new String[]{"3", "14"}, new String[]{"6", "30"});
		Subscription sub4 = new Subscription("sub4", new String[]{"x", "y"}, new String[]{"7", "16"}, new String[]{"8", "2"});
		
		hubset.addSubscription(sub1);
		hubset.addSubscription(sub2);
		hubset.addSubscription(sub3);
		hubset.addSubscription(sub4);
		
		System.out.println(hubset.toString());
		
		Publication pub1 = new Publication("pub1", new String[]{"x", "y"}, new String[]{"17", "17"}, 100l);
		Publication pub2 = new Publication("pub2", new String[]{"x", "y"}, new String[]{"2", "2"}, 100l);
		Publication pub3 = new Publication("pub3", new String[]{"x", "y"}, new String[]{"25", "25"}, 100l);
		Publication pub4 = new Publication("pub4", new String[]{"x", "y"}, new String[]{"9", "9"}, 100l);
		
		System.out.println("MATCHING:");
		
		System.out.println(pub1.toString());
		ArrayList<Subscription> matched1 = hubset.match(pub1);
		if(matched1 != null) for(int i=0; i<matched1.size(); i++){
			System.out.println("\t: "+matched1.get(i).toString());
		}
		
		System.out.println(pub2.toString());
		ArrayList<Subscription> matched2 = hubset.match(pub2);
		if(matched2 != null) for(int i=0; i<matched2.size(); i++){
			System.out.println("\t: "+matched2.get(i).toString());
		}
		
		System.out.println(pub3.toString());
		ArrayList<Subscription> matched3 = hubset.match(pub3);
		if(matched3 != null) for(int i=0; i<matched3.size(); i++){
			System.out.println("\t: "+matched3.get(i).toString());
		}
		
		System.out.println(pub4.toString());
		ArrayList<Subscription> matched4 = hubset.match(pub4);
		if(matched4 != null) for(int i=0; i<matched4.size(); i++){
			System.out.println("\t: "+matched4.get(i).toString());
		}
	}

}
