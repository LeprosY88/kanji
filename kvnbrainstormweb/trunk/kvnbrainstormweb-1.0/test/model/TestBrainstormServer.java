package test.model;

import java.util.ArrayList;

public class TestBrainstormServer {

	/**
	 * @param args
	 */
	public TestBrainstormServer(){
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> sheets = new ArrayList<String>();
		
		sheets.add("Sheet0");
		sheets.add("Sheet1");
		sheets.add("Sheet2");
		sheets.add("Sheet3");
		sheets.add("Sheet4");
		sheets.add("Sheet5");
		
		TestBrainstormServer.printArrayList(sheets);
		TestBrainstormServer.rotateArrayListBackward(sheets);
		TestBrainstormServer.printArrayList(sheets);

	}
	
	public static void rotateArrayListBackward(ArrayList<String> lst){		
		String tlst = lst.get(0);
		for(int i=0; i<lst.size()-1; i++){
			lst.set(i, lst.get(i+1));
		}
		lst.set(lst.size()-1, tlst);
	}
	public static void printArrayList(ArrayList<String> lst){
		for(int i=0; i<lst.size(); i++){
			System.out.println(i + " : "+lst.get(i));
		}
	}

}
