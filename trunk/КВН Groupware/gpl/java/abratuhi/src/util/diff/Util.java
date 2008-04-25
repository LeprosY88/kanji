package gpl.java.abratuhi.src.util.diff;

import java.util.ArrayList;
import java.util.Random;

public class Util {
	
	public static void permuteIntegerArrayList(ArrayList<Integer> l){
		for(int i=0; i<l.size(); i++){
			for(int j=0; j<l.size(); j++){
				int ind = new Random().nextInt(l.size()-1);
				int t_int = l.get(j);
				l.set(j, l.get(ind));
				l.set(ind, t_int);
			}
		}
	}
	public static void permuteIntegerArray(Integer[] l){
		for(int i=0; i<l.length; i++){
			for(int j=0; j<l.length; j++){
				int ind = new Random().nextInt(l.length-1);
				int t_int = l[j];
				l[j] = l[ind];
				l[ind] = t_int;;
			}
		}
	}
	
	public static String generateRandomString(int length){
		char[] alphabet = {'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p',
							'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l',
							'z', 'x', 'c', 'v', 'b', 'n', 'm',
							'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
		char[] res = new char[length];
		for(int i=0; i<length; i++){
			int ind = new Random().nextInt(alphabet.length-1);
			res[i] = alphabet[ind];
		}
		return new String(res);
	}
	
	public static void main(String[] args){
		/*int size = 100;
		Integer[] numbs = new Integer[size];
		for(int i=0; i<size; i++){
			numbs[i] = i;
		}
		permuteIntegerArray(numbs);
		for(int i=0; i<size; i++){
			System.out.print(numbs[i]+", ");
		}*/
		/*int l = 10;
		for(int i=0; i<10; i++){
			System.out.println(generateRandomString(l));
		}*/
	}

}
