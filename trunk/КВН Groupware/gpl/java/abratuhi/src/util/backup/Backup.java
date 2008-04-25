package gpl.java.abratuhi.src.util.backup;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Backup {
	
	public static void writeToFile(String pathname, ArrayList<String> in){
		File f = new File(pathname);
		try {
			FileOutputStream fos=new FileOutputStream(f);
			BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			
			for(int i=0; i<in.size(); i++){
				try {
					bw.write(in.get(i)+"\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("Wrote to file:\t"+f.getAbsolutePath());
			
			try {
				bw.flush();
				bw.close();
				fos.flush();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	//diff
	public static  String getTomcatDir(){
		String rundir=new String();
		String tomcatdir=new String();
		File f=new File("");
		try {
			rundir=f.getCanonicalPath();
			tomcatdir=rundir.substring(0, rundir.length()-"\\bin".length());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tomcatdir;
	}

	//help-operations
	public String dirUp(String path){
		int l=path.length();
		while(path.charAt(--l)!=File.separatorChar){
			path=path.substring(0, --l);
		}
		//return path.subSequence(0, --l).toString();
		return path.toString();
	}

	public String dirDown(String path, String dir){
		return path.concat(dir+File.separator);
	}

}
