package org.abratuhi.mmorpg.util;

public class MathUtil {
	
	@SuppressWarnings("unchecked")
	public static int indexOfMin(Comparable[] list){
		int indexOfMin = 0;
		Comparable valueOfMin = list[0];
		for(int i=0; i<list.length; i++){
			if(list[i].compareTo(valueOfMin) < 0){
				indexOfMin = i;
				valueOfMin = list[i];
			}
		}
		return indexOfMin;
	}

}
