package org.abratuhi.mmorpg.pubsub;

/**
 * Part of Mercury implementation. Subscription.
 * Note: minimums[i]>maximum[i] is possible.
 * @author Alexei Bratuhin
 *
 */
public class Subscription {
	
	/** unique id of subscription **/
	public String id;
	/** attribute names **/
	public String[] attributes;
	/** attribute minimum values **/
	public String[] minimums;
	/** attribute maximum values **/
	public String[] maximums;
	
	public Subscription(String id, String[] attributes, String[] mins, String[] maxs){
		this.id = id;
		this.attributes = attributes;
		this.minimums = mins;
		this.maximums = maxs;
	}
	
	/** 
	 * Get index of attribute, given an attribute's name/id 
	 * @param attribute	attribute name/id
	 * @return	attribute's index
	 */
	public Integer getAttributeIndex(String attribute){
		for(int i=0; i<attributes.length; i++){
			if(attributes[i].equals(attribute)){
				return i;
			}
		}
		return -1;
	}
	
	/** 
	 * Check, whether this subscription matches given publication.
	 * @param pub	publication to match
	 * @return	<ul><li>true, match case</li>
	 * 			<li>false, otherwise</li></ul>
	 */
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
