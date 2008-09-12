package org.abratuhi.mmorpg.pubsub;

public class Subscription {
	
	public String id;
	public String[] attributes;
	public String[] minimums;
	public String[] maximums;
	
	public Subscription(String id, String[] attributes, String[] mins, String[] maxs){
		this.id = id;
		this.attributes = attributes;
		this.minimums = mins;
		this.maximums = maxs;
	}
	
	public Integer getAttributeIndex(String attribute){
		for(int i=0; i<attributes.length; i++){
			if(attributes[i].equals(attribute)){
				return i;
			}
		}
		return -1;
	}
	
	public boolean match(Publication pub){
		for(int i=0; i<pub.attributes.length; i++){
			int attributeIndex = -1;
			attributeIndex = this.getAttributeIndex(pub.attributes[i]);
			if(attributeIndex == -1) continue;
			else{
				if(Publication.lessorequal(minimums[attributeIndex], maximums[attributeIndex])){
					if(Publication.lessorequal(minimums[attributeIndex], pub.values[i]) && 
							Publication.lessorequal(pub.values[i], maximums[attributeIndex])){
						continue;
					}
					else{
						return false;
					}
				}
				else{
				if(Publication.lessorequal(maximums[attributeIndex], minimums[attributeIndex])){
					if(Publication.lessorequal(minimums[attributeIndex], pub.values[i]) || 
							Publication.lessorequal(pub.values[i], maximums[attributeIndex])){
						continue;
					}
					else{
						return false;
					}
				}
				}
			}
		}
		return true;
	}
	
	public String toString(){
		 String result = new String();
		 for(int i=0; i<attributes.length; i++){
			 result += " & "+minimums[i]+"<"+attributes[i]+"<"+maximums[i];
		 }
		 result = result.substring(2);
		 result = id+": " + result;
		 return result;
	}

}
