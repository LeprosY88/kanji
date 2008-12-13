package org.abratuhi.uradar.util;

public class ArrayUtil<T> {
	
	public boolean contains(T[] array, T object){
		// check NULL
		if(array == null || object == null){
			System.out.println("ArrayUtil<T> contains(T[] array, T object):\tone or more of parameters is NULL");
			return false;
		}
		// check array size
		if(array.length < 1){
			return false;
		}
		// search
		for(int i=0; i<array.length; i++){
			if(array[i] == object){
				return true;	// case found
			}
		}
		return false;	// case nothing found
	}
	
	public Integer containsAt(T[] array, T object){
		// check NULL
		if(array == null || object == null){
			System.out.println("ArrayUtil<T> contains(T[] array, T object):\tone or more of parameters is NULL");
			return -1;
		}
		// check array size
		if(array.length < 1){
			return -1;
		}
		// search
		for(int i=0; i<array.length; i++){
			if(array[i] == object){
				return i;	// case found
			}
		}
		return -1;	// case nothing found
	}

}
