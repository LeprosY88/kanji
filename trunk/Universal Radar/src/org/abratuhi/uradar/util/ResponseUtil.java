package org.abratuhi.uradar.util;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class ResponseUtil {
	
	public static String convertResultSet2XMLString(ResultSet rs){
		try{
			// init return string
			String out = new String();
			// init temporary values XML
			Element docroot = new Element("uradar_response");
			Document docout = new Document(docroot);
			// init temporary values SQL
			ResultSetMetaData meta = rs.getMetaData();
			int ncolumns = meta.getColumnCount();
			// iterate through result set
			do{
				// init row element
				Element doc_trow = new Element("uradar_row");
				// iterate through columns
				for(int i=1; i<=ncolumns; i++){
					String tcolumn_name = meta.getColumnName(i);
					String tcolumn_value = rs.getString(i);
					Element doc_trow_tcell = new Element(tcolumn_name).setText(tcolumn_value);
					doc_trow.addContent(doc_trow_tcell);
				}
				// add row element
				docroot.addContent(doc_trow);
			}
			while(rs.next());
			// convert JDOM Document to XML String
			XMLOutputter xmlout = new XMLOutputter();
			StringWriter sw = new StringWriter();
			xmlout.output(docout, sw);
			out = sw.toString();
			sw.close();
			// return
			return out;
		} catch(SQLException e){
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return case things go wrong
		return null;
	}

}
