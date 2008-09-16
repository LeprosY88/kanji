package org.abratuhi.mmorpg.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Evaluator {
	
	ArrayList<ArrayList<String>> evaluationData = new ArrayList<ArrayList<String>>();
	
	public static ArrayList<String> parseLogFile(File f){
		System.out.println("parsing file:\t"+f.getAbsolutePath());
		ArrayList<String> out = new ArrayList<String>();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			
			br.readLine();
			br.readLine();
			
			String algorithm = br.readLine().substring("Algorithm:	".length());
			String heuristics = br.readLine().substring("with Probability Heuristics:	".length());
			String dimension = br.readLine().substring("Dimension:	".length());
			String nnodes = br.readLine().substring("# Nodes:	".length());
			String nsteps = br.readLine().substring("# Steps:	".length());
			String range = br.readLine().substring("Range:	".length());
			String speed = br.readLine().substring("Speed:	".length());
			
			br.readLine();
			br.readLine();
			
			String nmessages = br.readLine().substring("# Messages:	".length());
			String coeffneigh = br.readLine().substring("# Neighbourcast:	".length());
			String nconnectedchecks = br.readLine().substring("# isConnected Checks:	".length());
			String nbuildmst = br.readLine().substring("# BuildMST:	".length());
			
			out.add(algorithm);
			out.add(heuristics);
			out.add(dimension);
			out.add(nnodes);
			out.add(nsteps);
			out.add(range);
			out.add(speed);
			out.add(nmessages);
			out.add(coeffneigh);
			out.add(nconnectedchecks);
			out.add(nbuildmst);
			
			String error = br.readLine();
			while(error!=null && error.startsWith("# errors")){
				String cerror = error.substring("# errors, corrected in ".length());
				cerror = cerror.replaceAll("\t", "");
				out.add("error:"+cerror);
				
				error = br.readLine();
			}
			
			String bug = error;
			while(bug!=null && bug.startsWith("# bugs")){
				String cbug = bug.substring("# bugs, not corrected in ".length());
				cbug = cbug.replaceAll("\t", "");
				out.add("bug:"+cbug);
				
				bug = br.readLine();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public static void evaluateLogDataset(File dir){
		if(dir.isDirectory()){
			try {
				OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File(dir.getName()+"_EvaluationResults.mmorpgeval")));
				File[] files = dir.listFiles();
				for(int i=0; i<files.length; i++){
					ArrayList<String> cResult = parseLogFile(files[i]);
					for(int j=0; j<cResult.size(); j++){
						osw.append(cResult.get(j)+";");
					}
					osw.append("\n");
				}
				osw.flush();
				osw.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void prepareForGnuplot(File dir){
		if(dir.isDirectory()){
			try{
				BufferedReader br;
				
				OutputStreamWriter osw_nc_0 = new OutputStreamWriter(new FileOutputStream(new File("nc_0.dat")));
				OutputStreamWriter osw_nc_1 = new OutputStreamWriter(new FileOutputStream(new File("nc_1.dat")));
				OutputStreamWriter osw_nccwa_0 = new OutputStreamWriter(new FileOutputStream(new File("nccwa_0.dat")));
				OutputStreamWriter osw_nccwa_1 = new OutputStreamWriter(new FileOutputStream(new File("nccwa_1.dat")));
				
				osw_nc_0.write("#range\tspeed\tnccoeff\tncheck\tnbuild\tnerror1\tnerror2\tnerror3\tnerror4\tnerror5\n");
				osw_nc_1.write("#range\tspeed\tnccoeff\tncheck\tnbuild\tnerror1\tnerror2\tnerror3\tnerror4\tnerror5\n");
				osw_nccwa_0.write("#range\tspeed\tnccoeff\tncheck\tnbuild\tnerror1\tnerror2\tnerror3\tnerror4\tnerror5\n");
				osw_nccwa_1.write("#range\tspeed\tnccoeff\tncheck\tnbuild\tnerror1\tnerror2\tnerror3\tnerror4\tnerror5\n");
				
				OutputStreamWriter osw_c;
				
				File[] files = dir.listFiles();
				for(int i=0; i<files.length; i++){
					br =new BufferedReader(new InputStreamReader(new FileInputStream(files[i])));
					String cLine = br.readLine();
					while(cLine != null){
						String[] cLineSplitted = cLine.split(";");
						if(cLineSplitted[0].equals("neighbourcast")){
							if(cLineSplitted[1].equals("false")) osw_c = osw_nc_0;
							else osw_c = osw_nc_1;
						}
						else{
							if(cLineSplitted[1].equals("false")) osw_c = osw_nccwa_0;
							else osw_c = osw_nccwa_1;
						}
						
						String range = cLineSplitted[5];
						String speed = cLineSplitted[6];
						String coeff = cLineSplitted[8];
						Double nccoeff = Double.valueOf(coeff.split("/")[0]) / Double.valueOf(coeff.split("/")[1]);
						String ncheck = cLineSplitted[9];
						String nbuild = cLineSplitted[10];
						
						Integer nerror1 = 0;
						Integer nerror2 = 0;
						Integer nerror3 = 0;
						Integer nerror4 = 0;
						Integer nerror5 = 0;
						
						for(int j=10; j<cLineSplitted.length; j++){
							if(cLineSplitted[j].startsWith("error")){
								String[] errorSplitted = cLineSplitted[j].split(":");
								if(errorSplitted[1].equals("1")) nerror1 = Integer.valueOf(errorSplitted[2]);
								if(errorSplitted[1].equals("2")) nerror2 = Integer.valueOf(errorSplitted[2]);
								if(errorSplitted[1].equals("3")) nerror3 = Integer.valueOf(errorSplitted[2]);
								if(errorSplitted[1].equals("4")) nerror4 = Integer.valueOf(errorSplitted[2]);
								if(errorSplitted[1].equals("5")) nerror5 = Integer.valueOf(errorSplitted[2]);
							}
						}
						
						osw_c.write(range+"\t"+speed+"\t"+nccoeff+"\t"+ncheck+"\t"+nbuild+"\t"+nerror1+"\t"+nerror2+"\t"+nerror3+"\t"+nerror4+"\t"+nerror5+"\n");
						
						cLine = br.readLine();
					}
				}
				
				osw_nc_0.flush();
				osw_nc_0.close();
				osw_nc_1.flush();
				osw_nc_1.close();
				osw_nccwa_0.flush();
				osw_nccwa_0.close();
				osw_nccwa_1.flush();
				osw_nccwa_1.close();
			}catch (FileNotFoundException e){
				
			}catch (IOException e){
				
			}
		}
	}
	
	public static void prepareForLatex(File f){
		BufferedReader br;
		try{
			ArrayList<Integer> ranges = new ArrayList<Integer>();
			ArrayList<Integer> speeds = new ArrayList<Integer>();
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String strline = new String();
			strline = br.readLine();
			strline = br.readLine();
			while (strline != null){
				String[] strlineSplit = strline.split("\t");
				Integer crange = Integer.parseInt(strlineSplit[0]);
				Integer cspeed = Integer.parseInt(strlineSplit[1]);
				if (! ranges.contains(crange)) ranges.add(crange);
				if (! speeds.contains(cspeed)) speeds.add(cspeed);
				strline = br.readLine();
			}
			br.close();
			
			sortInc(ranges);
			sortInc(speeds);
			
			String[][] out = new String[speeds.size()+1][ranges.size()+1];
			for(int i=0; i<ranges.size(); i++){
				out[0][i+1] = String.valueOf(ranges.get(i));
			}
			for(int j=0; j<speeds.size(); j++){
				out[j+1][0] = String.valueOf(speeds.get(j));
			}
			
			// ratio
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			strline = br.readLine();
			strline = br.readLine();
			while (strline != null){
				String[] strlineSplit = strline.split("\t");
				Integer crange = Integer.parseInt(strlineSplit[0]);
				Integer cspeed = Integer.parseInt(strlineSplit[1]);
				Integer crangeIndex = ranges.indexOf(crange) +1;
				Integer cspeedIndex = speeds.indexOf(cspeed) +1;
				String  cell = strlineSplit[2];
				if (cell.contains(".")) cell = cell.substring(0, cell.indexOf(".")+Math.min(4, cell.length()-cell.indexOf(".")));
				out[cspeedIndex][crangeIndex] = cell;
				strline = br.readLine();
			}
			br.close();
			
			System.out.println("\\begin{landscape}");
			System.out.println("\\begin{table}");
			System.out.println("\\caption{"+f.getName().split("_")[0]+"; Ratio}");
			System.out.println("\\begin{center}");
			System.out.println("\\begin{tiny}");
			System.out.print("\\begin{tabular}");
			System.out.print("{");
			for(int k=0; k<ranges.size()+1; k++) System.out.print("c ");
			System.out.println("}");
			int i=0, j=0;
			for(i=0; i<out.length; i++){
				for(j=0; j<out[i].length-1; j++){
					if(out[i][j] != null) System.out.print(out[i][j]+"&\t");
					else System.out.print("-"+"&\t");
				}
				if(out[i][j] != null) System.out.println(out[i][j]+"\\\\");
				else System.out.println("-"+"\\\\");
			}
			System.out.println("\\end{tabular}");
			System.out.println("\\end{tiny}");
			System.out.println("\\end{center}");
			System.out.println("\\end{table}");
			System.out.println("\\end{landscape}");
			
			//mst
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			strline = br.readLine();
			strline = br.readLine();
			while (strline != null){
				String[] strlineSplit = strline.split("\t");
				Integer crange = Integer.parseInt(strlineSplit[0]);
				Integer cspeed = Integer.parseInt(strlineSplit[1]);
				Integer crangeIndex = ranges.indexOf(crange) +1;
				Integer cspeedIndex = speeds.indexOf(cspeed) +1;
				String  cell = strlineSplit[4];
				if (cell.contains(".")) cell = cell.substring(0, cell.indexOf(".")+Math.min(4, cell.length()-cell.indexOf(".")));
				out[cspeedIndex][crangeIndex] = cell;
				strline = br.readLine();
			}
			br.close();
			
			System.out.println("\\begin{landscape}");
			System.out.println("\\begin{table}");
			System.out.println("\\caption{"+f.getName().split("_")[0]+"; Number of MST Builds}");
			System.out.println("\\begin{center}");
			System.out.println("\\begin{tiny}");
			System.out.print("\\begin{tabular}");
			System.out.print("{");
			for(int k=0; k<ranges.size()+1; k++) System.out.print("c ");
			System.out.println("}");
			for(i=0; i<out.length; i++){
				for(j=0; j<out[i].length-1; j++){
					if(out[i][j] != null) System.out.print(out[i][j]+"&\t");
					else System.out.print("-"+"&\t");
				}
				if(out[i][j] != null) System.out.println(out[i][j]+"\\\\");
				else System.out.println("-"+"\\\\");
			}
			System.out.println("\\end{tabular}");
			System.out.println("\\end{tiny}");
			System.out.println("\\end{center}");
			System.out.println("\\end{table}");
			System.out.println("\\end{landscape}");
			
			//error
			br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			strline = br.readLine();
			strline = br.readLine();
			while (strline != null){
				String[] strlineSplit = strline.split("\t");
				Integer crange = Integer.parseInt(strlineSplit[0]);
				Integer cspeed = Integer.parseInt(strlineSplit[1]);
				Integer crangeIndex = ranges.indexOf(crange) +1;
				Integer cspeedIndex = speeds.indexOf(cspeed) +1;
				Integer scaleFactor = 100;
				String  cell = Integer.parseInt(strlineSplit[5])/scaleFactor+"/"+
								Integer.parseInt(strlineSplit[6])/scaleFactor+"/"+
								Integer.parseInt(strlineSplit[7])/scaleFactor+"/"+
								Integer.parseInt(strlineSplit[8])/scaleFactor+"/"+
								Integer.parseInt(strlineSplit[9])/scaleFactor+"/";
				if (cell.contains(".")) cell = cell.substring(0, cell.indexOf(".")+Math.min(4, cell.length()-cell.indexOf(".")));
				out[cspeedIndex][crangeIndex] = cell;
				strline = br.readLine();
			}
			br.close();
			
			System.out.println("\\begin{landscape}");
			System.out.println("\\begin{table}");
			System.out.println("\\caption{"+f.getName().split("_")[0]+"; Number of Inconsistencies x100}");
			System.out.println("\\begin{center}");
			System.out.println("\\begin{tiny}");
			System.out.print("\\begin{tabular}");
			System.out.print("{");
			for(int k=0; k<ranges.size()+1; k++) System.out.print("c ");
			System.out.println("}");
			for(i=0; i<out.length; i++){
				for(j=0; j<out[i].length-1; j++){
					if(out[i][j] != null) System.out.print(out[i][j]+"&\t");
					else System.out.print("-"+"&\t");
				}
				if(out[i][j] != null) System.out.println(out[i][j]+"\\\\");
				else System.out.println("-"+"\\\\");
			}
			System.out.println("\\end{tabular}");
			System.out.println("\\end{tiny}");
			System.out.println("\\end{center}");
			System.out.println("\\end{table}");
			System.out.println("\\end{landscape}");
			
		}catch (FileNotFoundException e){

		}catch (IOException e){

		}	
	}
	
	public static void sortInc(ArrayList<Integer> list){
		for(int i=0; i<list.size()-1; i++){
			for(int j=i+1; j<list.size(); j++){
				if(list.get(i) > list.get(j)){
					Integer t = list.get(j);
					list.set(j, list.get(i));
					list.set(i, t);
				}
			}
		}
	}
	
	public static void main (String [] args){
		//evaluateLogDataset(new File("data\\home-2008-08-12\\"));
		/*evaluateLogDataset(new File("data\\home-2008-08-13\\"));
		evaluateLogDataset(new File("data\\kde45-2008-08-14\\"));
		evaluateLogDataset(new File("data\\kde45-2008-08-25\\"));
		evaluateLogDataset(new File("data\\kde45-2008-08-25-1\\"));
		evaluateLogDataset(new File("data\\kde45-2008-08-25-2\\"));
		evaluateLogDataset(new File("data\\kde45-2008-08-25-3\\"));
		evaluateLogDataset(new File("data\\kde45-2008-08-26\\"));
		evaluateLogDataset(new File("data\\kde45-2008-08-26-1\\"));*/
		//prepareForGnuplot(new File("data\\evaluation\\"));
		prepareForLatex(new File("data/gnuplot/nc_0.dat"));
		prepareForLatex(new File("data/gnuplot/nccwa_0.dat"));
		prepareForLatex(new File("data/gnuplot/nc_1.dat"));
	}

}
