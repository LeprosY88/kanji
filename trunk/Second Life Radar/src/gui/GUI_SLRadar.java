/**
 * @author Alexei Bratuhin
 * @version 1.0
 * @licence GPLv2
 */

package gui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SLRadar;

import util.SLRadarUtil;


public class GUI_SLRadar extends HttpServlet{
	
	//
	private static GUI_SLRadar instance = null;
	private SLRadar slradar = SLRadar.getInstance();
	
	
	/**
	 * The following part enables use of the servlet as "Singleton"
	 */
	public GUI_SLRadar(){
		
	}	
	public static synchronized GUI_SLRadar getIntance(){
		if( instance == null){
			instance = new GUI_SLRadar();
		}
		return instance;
	}
	public Object clone() throws CloneNotSupportedException{
		throw new CloneNotSupportedException();
	}
	
	/**
	 * Receiving a HTTPRequest, take the query part of it and proceed:
	 * SL - refers to Second Life part of possible incoming request
	 * 	sl_login - 				SL script contacts the server, and says, the scripted object is attached, thus enabling the SL Radar feature
	 * 	sl_own_info - 			SL script contacts the server, and sends the information about the SL character - name,region,position,velocity
	 * 	sl_request_friends - 	SL script contacts the server, and requests information about the SL character's friends in FB
	 * 	sl_logout - 			SL script contacts the server, and says, the scripted object is detached, thus disabling the SL Radar feature
	 * FB - refers to Facebook part of possible incoming request
	 * 	fb_login - 	SLRadar application is added to fbid's account
	 * 	fb_add_friend - 	SLRadar application is opened in FB page
	 * 	fb_request_own	-	SLRadar application requests SL inforamtion about FB user
	 * 	fb_request_friends - 	SLRadar application requests fbid's FB friends info in SL regulary to update the SLRadar page
	 * 
	 * @param req
	 * @param res
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// get request string - string after '?'
		String request = req.getQueryString();	
		// transform request string to request string
		Properties reqprop = SLRadarUtil.request2properties(request);
		// get basic properties of request
		String reqtype = reqprop.getProperty("reqtype");
		String fbid = reqprop.getProperty("fbid");
		String slid = reqprop.getProperty("slid");
		
		// proceed queries relating to SL
		if(reqtype.equals("sl_login")){	// SL script started working - it's trying to login/add SL data to FB data in DB
			//System.out.println(request);
			slradar.addupdateUser(fbid, slid);
		}
		if(reqtype.equals("sl_own_info")){	// SL script submits information about itself's avatar in SL
			//System.out.println(request);
			slradar.addupdateSLInfo(fbid, slid, reqprop);
		}
		if(reqtype.equals("sl_request_friends")){	// SL script requests information about its avatar FB friends in SL
			//System.out.println(request);
			// build response string
			String resultResponse = slradar.getFriendsSLInfo(fbid, slid);
			// send response string over existing opened HTTP connection to SL script
			PrintWriter out = res.getWriter();
			out.println(resultResponse);
			out.close();
		}
		if(reqtype.equals("sl_logout")){
			System.out.println(request);
			slradar.addupdateSLInfo(fbid, slid, reqprop);
		}
		
		// proceed queries realting to FB
		if(reqprop.getProperty("reqtype").equals("fb_login")){	// FB app was opened(FB app URL was requested by FB page)
			//System.out.println(request);
			slradar.addupdateUser(fbid, slid);		
		}
		if(reqprop.getProperty("reqtype").equals("fb_add_friend")){	// FB app is trying to submit a FB friendrelationship to DB
			//System.out.println(request);
			String fbid_friend = reqprop.getProperty("fbid_friend");
			slradar.addFBFriend(fbid, fbid_friend);
		}
		if(reqprop.getProperty("reqtype").equals("fb_request_own")){	// FB app requests information about its SL avatar
			//System.out.println(request);
			// build response string
			String resultResponse = slradar.getOwnSLInfo(fbid, slid);
			// send response string
			PrintWriter out = res.getWriter();
			out.println(resultResponse);
			out.close();
		}
		if(reqprop.getProperty("reqtype").equals("fb_request_friends")){	// FB app requests information about its users friends in SL
			//System.out.println(request);
			// build response string
			String resultResponse = slradar.getFriendsSLInfo(fbid, slid);
			// add response string
			PrintWriter out = res.getWriter();
			out.println(resultResponse);
			out.close();
		}
	}
	
}
