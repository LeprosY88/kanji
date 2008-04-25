/**
 * @author Alexei Bratuhin
 * @version 1.0
 * @licence GPLv2
 */

package util;

import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class SLRadarUtil {
	
	/**
	 * Transform a CSV-like concatenation of properties in query part of HTTPRequest to Java Properties
	 * @param req	-	String representation of query part of HttpRequest
	 * @return	-	Properties representation of query part of HttpRequest
	 */
	public static Properties request2properties(String req){
		Properties prop = new Properties();
		if(req.startsWith("request=true&")){
			req = req.substring("request=true&".length());
			while(req.indexOf("&") > 0){
				String t_string = req.substring(0, req.indexOf("&"));
				prop.put(t_string.substring(0, t_string.indexOf("=")), t_string.substring(t_string.indexOf("=")+1));
				req = req.substring(req.indexOf("&")+1);
			}
			prop.put(req.substring(0, req.indexOf("=")), req.substring(req.indexOf("=")+1));
		}
		else{
			System.out.println("unrecognized query string:\t"+req);
		}
		return prop;
	}
	
	/**
	 * Transform a JDOM Document to String
	 * @param doc	-	JDOM Document
	 * @return	-	String representation of JDOM Document
	 */
	public static String doc2string(Document doc){
		XMLOutputter outp = new XMLOutputter(Format.getPrettyFormat());
		//outp.setIndent("\t");
		//outp.setNewlines(true);
		return outp.outputString(doc);
	}
	
	/**
	 * Transform response JDOM Document to String
	 * @param doc
	 * @return
	 */
	public static String response2string(Document doc){
		String res = new String();
		for(int i=0; i<doc.getRootElement().getChildren("user").size(); i++){
			Element u = (Element) doc.getRootElement().getChildren("user").get(i);
			try{
			//res += "fbid="+u.getAttributeValue("fbid")+",";
			//res += "slid="+u.getAttributeValue("slid")+",";
			res += "fbname="+u.getChild("fb").getChildText("name")+",";
			res += "slname="+u.getChild("sl").getChildText("name")+",";
			res += "region="+u.getChild("sl").getChildText("region")+",";
			res += "position="+u.getChild("sl").getChildText("position")+",";
			res += ";";
			}
			catch(NullPointerException e){
				e.printStackTrace();
			}
		}
		return res;
	}
	

}
