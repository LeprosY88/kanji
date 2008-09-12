package org.abratuhi.mmorpg.pubsub;

import java.util.Date;

public class Publication {
	
	public String id;
	public String[] attributes;
	public String[] values;
	
	public Long creationDate;
	public Long expirationDate;
	
	public Publication(String id, String[] attributez, String[] valuez, Long ttl){
		this.id = id;
		this.attributes = attributez;
		this.values = valuez;
		this.creationDate = new Date().getTime();
		this.expirationDate = this.creationDate + ttl;
	}
	
	public Integer getAttributeIndex(String attribute){
		for(int i=0; i<attributes.length; i++){
			if(attributes[i].equals(attribute)){
				return i;
			}
		}
		return -1;
	}
	
	public static boolean less(String value1, String value2){
		return (Integer.valueOf(value1) < Integer.valueOf(value2)) ? true: false;
	}
	public static boolean lessorequal(String value1, String value2){
		return (Integer.valueOf(value1) <= Integer.valueOf(value2)) ? true: false;
	}
	public static String sum(String value1, String value2){
		return String.valueOf(Integer.valueOf(value1)+Integer.valueOf(value2));
	}
	public static String divide(String value1, String value2){
		return String.valueOf(Integer.valueOf(value1)/Integer.valueOf(value2));
	}
	
	public String toString(){
		String result = new String();
		for(int i=0; i<attributes.length; i++){
			result += " & "+attributes[i]+"="+values[i];
		}
		result = result.substring(2);
		result = id+": " + result;
		return result;
	}

}
