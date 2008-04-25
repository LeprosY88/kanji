package gpl.java.abratuhi.src.util.string;

import java.util.ArrayList;

public class StringUtils {
	
	public static void String2ArrayListOfString(String str, ArrayList<String> als, String sep, int cnt){
		/**/
		String str_t = new String(str);	
		/**/
		for(int i=0; i<cnt; i++){
			if(str_t.indexOf(sep) != -1) als.add(new String(str_t.substring(0, str_t.indexOf(sep))));
			else als.add(new String(str_t));
			str_t = str_t.substring(str_t.indexOf(sep)+1);
		}
	}
	
	/*public static void main(String[] args){
		String sp = "|";
		String s = "hi"+sp+"all"+sp+"of"+sp+"you,"+sp+"friends"+sp+"and"+sp+"folks!";
		ArrayList<String> al = new ArrayList<String>();
		StringUtils.String2ArrayListOfString(s, al, sp, 7);
		for(int i=0; i<al.size(); i++){
			System.out.println(al.get(i));
		}
	}*/

}
