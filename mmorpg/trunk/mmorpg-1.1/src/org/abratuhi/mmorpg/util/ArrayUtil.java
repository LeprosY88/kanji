package org.abratuhi.mmorpg.util;

public class ArrayUtil {
	
	public static String toString(int[] list){
		String out = new String();
		for(int i=0; i<list.length; i++) out += (i)+":" + list[i] + " ";
		return out;
	}
	public static String toString(double[] list){
		String out = new String();
		for(int i=0; i<list.length; i++) out += (i)+":" + list[i] + " ";
		return out;
	}

}
