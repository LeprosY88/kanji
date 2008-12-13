package src.gui;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import src.model.BrainstormServer;

@SuppressWarnings("serial")
public class BrainstormWeb extends HttpServlet{
	
	private static BrainstormWeb instance = null;
	private BrainstormServer server = new BrainstormServer();
	
	public BrainstormWeb(){
		//server = new BrainstormServer();
	}
	
	public static BrainstormWeb getInstance(){
		if(instance == null){
			instance = new BrainstormWeb();
		}
		return instance;
	}
	
	public synchronized boolean addUser(String name) {
		return BrainstormWeb.getInstance().server.loginUser(name);
	}
	public synchronized boolean addTheme(String name, String theme) {
		return BrainstormWeb.getInstance().server.submitTheme(name, theme);
	}
	public synchronized boolean change() {
		//TODO: backup
		return BrainstormWeb.getInstance().server.changeSheets();
	}
	public synchronized boolean restart() {
		return BrainstormWeb.getInstance().server.resetServer();
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		BrainstormWeb.getInstance().restart();
		BrainstormWeb.getInstance().doGetLogin(req, res);
		//doGetMessage(req, res, "Bla-bla!");
		return;
	}
	
	public void doGetLogin(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html;charset=UTF-8");
        res.setHeader("pragma", "no-cache");
        PrintWriter out=res.getWriter();
        out.println("<html>");
        out.println("	<head>");
        out.println("		<title>::BrainStorm::beta</title>");
        out.println("	</head>");
        out.println("	<body>");
        out.println("		<form method=\"post\">");
        out.println("			<table>");
        out.println("				<tr>");
        out.println("					<td align=\"left\" valign=\"top\">");
        out.println("						User:<input name=\"field_account\" type=\"text\"/><br/>");
        out.println("						Status:<input name=\"field_status\" type=\"text\"/><br/>");
        out.println("						<input name=\"button_login\" type=\"submit\" value=\"Login!\"/><br/>");
        out.println("					</td>");
        out.println("					<td align=\"left\" valign=\"top\">");
        out.println("					</td>");
        out.println("					<td align=\"left\" valign=\"top\">");
        out.println("						Users online:<br/>");
        out.println("						-------------<br/>");
        for(int i=0; i<server.users.size(); i++){
        	out.println(server.users.get(i)+" - "+BrainstormWeb.getInstance().server.status.get(i)+"<br/>");
        }
        //out.println("						cOIOuHkc - 1<br/>");
        //out.println("						Krola - 2<br/>");
        //out.println("						Lilah -1<br/>");
        out.println("						<br/>");
        out.println("						<br/>");
        out.println("						<br/>");
        out.println("						Status meaning:<br/>");
        out.println("						--------------<br/>");
        out.println("						0 - offline<br/>");
        out.println("						1 - online<br/>");
        out.println("						2 - theme submitted<br/>");
        out.println("						3 - writing<br/>");
        out.println("						4 - waiting for change<br/>");
        out.println("						5 - finished<br/>");
        out.println("					</td>");
        out.println("				</tr>");
        out.println("			</table>");
        out.println("		</form>");
        out.println("	</body>");
        out.println("</html>");
        out.close();
	}
	
	public void doGetTheme(HttpServletRequest req, HttpServletResponse res, String name) throws ServletException, IOException {
		res.setContentType("text/html;charset=UTF-8");
        res.setHeader("pragma", "no-cache");
        PrintWriter out=res.getWriter();
        out.println("<html>");
        out.println("	<head>");
        out.println("		<title>::BrainStorm::beta</title>");
        out.println("	</head>");
        out.println("	<body>");
        out.println("		<form method=\"post\">");
        out.println("			<table>");
        out.println("				<tr>");
        out.println("					<td align=\"left\" valign=\"top\">");
        int i;
        for(i=0; i<BrainstormWeb.getInstance().server.users.size(); i++){
        	if(name.equalsIgnoreCase(BrainstormWeb.getInstance().server.users.get(i))) break;
        }
        out.println("						User:<input name=\"field_account\" type=\"text\" value=\""+BrainstormWeb.getInstance().server.users.get(i)+"\"/><br/>");
        out.println("						Status:<input name=\"field_status\" type=\"text\" value=\""+BrainstormWeb.getInstance().server.status.get(i)+"\"/><br/>");
        out.println("						Status:<input name=\"button_reload\" type=\"submit\" value=\"Reload!\"/><br/>");
        out.println("					</td>");
        out.println("					<td align=\"left\" valign=\"top\">");
        out.println("						Theme:<input name=\"field_theme\" type=\"text\"/><br/>");
        out.println("						Use template:<input name=\"field_template\" type=\"text\"/><input name=\"button_browse_template\" type=\"submit\" value=\"Browse\"/><br/>");
        out.println("						<input name=\"button_submit_theme\" type=\"submit\" value=\"Submit theme!\"/></br>");
        out.println("					</td>");
        out.println("					<td align=\"left\" valign=\"top\">");
        out.println("						Users online:<br/>");
        out.println("						-------------<br/>");
        for(i=0; i<BrainstormWeb.getInstance().server.users.size(); i++){
        	out.println(BrainstormWeb.getInstance().server.users.get(i)+" - "+BrainstormWeb.getInstance().server.status.get(i)+"<br/>");
        }
        out.println("						<br/>");
        out.println("						<br/>");
        out.println("						<br/>");
        out.println("						Status meaning:<br/>");
        out.println("						--------------<br/>");
        out.println("						0 - offline<br/>");
        out.println("						1 - online<br/>");
        out.println("						2 - theme submitted<br/>");
        out.println("						3 - writing<br/>");
        out.println("						4 - waiting for change<br/>");
        out.println("						5 - finished<br/>");
        out.println("					</td>");
        out.println("				</tr>");
        out.println("			</table>");
        out.println("		</form>");
        out.println("	</body>");
        out.println("</html>");
        out.close();
	}
	
	public void doGetWaiting(HttpServletRequest req, HttpServletResponse res, String name) throws ServletException, IOException {
		res.setContentType("text/html;charset=UTF-8");
        res.setHeader("pragma", "no-cache");
        PrintWriter out=res.getWriter();
        out.println("<html>");
        out.println("	<head>");
        out.println("		<title>::BrainStorm::beta</title>");
        out.println("	</head>");
        out.println("	<body>");
        out.println("		<form method=\"post\">");
        out.println("			<table>");
        out.println("				<tr>");
        out.println("					<td align=\"left\" valign=\"top\">");
        int i;
        for(i=0; i<BrainstormWeb.getInstance().server.users.size(); i++){
        	if(name.equalsIgnoreCase(BrainstormWeb.getInstance().server.users.get(i))) break;
        }
        out.println("						User:<input name=\"field_account\" type=\"text\" value=\""+BrainstormWeb.getInstance().server.users.get(i)+"\"/><br/>");
        out.println("						Status:<input name=\"field_status\" type=\"text\" value=\""+BrainstormWeb.getInstance().server.status.get(i)+"\"/><br/>");
        //out.println("						<input name=\"button_restart\" type=\"submit\" value=\"Restart!\"/><br/>");
        out.println("					</td>");
        out.println("					<td align=\"left\" valign=\"top\">");
        //out.println("						Theme:<input name=\"field_theme\" type=\"text\" value=\""+BrainstormWeb.getInstance().server.getCollectiveTheme()+"\"/><br/>");
        //out.println("						Text:<br/><textarea name=\"field_sheet\" cols=\"80\" rows=\"80\">"+BrainstormWeb.getInstance().server.sheets.get(i)+"</textarea><br/>");
        out.println("						<input name=\"button_next\" type=\"submit\" value=\"Next!\"/></br>");
        out.println("					</td>");
        out.println("					<td align=\"left\" valign=\"top\">");
        out.println("						Users online:<br/>");
        out.println("						-------------<br/>");
        for(i=0; i<BrainstormWeb.getInstance().server.users.size(); i++){
        	out.println(BrainstormWeb.getInstance().server.users.get(i)+" - "+BrainstormWeb.getInstance().server.status.get(i)+"<br/>");
        }
        out.println("						<br/>");
        out.println("						<br/>");
        out.println("						<br/>");
        out.println("						Status meaning:<br/>");
        out.println("						--------------<br/>");
        out.println("						0 - offline<br/>");
        out.println("						1 - online<br/>");
        out.println("						2 - theme submitted<br/>");
        out.println("						3 - writing<br/>");
        out.println("						4 - waiting for change<br/>");
        out.println("						5 - finished<br/>");
        out.println("					</td>");
        out.println("				</tr>");
        out.println("			</table>");
        out.println("		</form>");
        out.println("	</body>");
        out.println("</html>");
        out.close();
	}
	
	public void doGetBrainstorm(HttpServletRequest req, HttpServletResponse res, String name) throws ServletException, IOException {
		res.setContentType("text/html;charset=UTF-8");
        res.setHeader("pragma", "no-cache");
        PrintWriter out=res.getWriter();
        out.println("<html>");
        out.println("	<head>");
        out.println("		<title>::BrainStorm::beta</title>");
        out.println("	</head>");
        out.println("	<body>");
        out.println("		<form method=\"post\">");
        out.println("			<table>");
        out.println("				<tr>");
        out.println("					<td align=\"left\" valign=\"top\">");
        int i;
        for(i=0; i<BrainstormWeb.getInstance().server.users.size(); i++){
        	if(name.equalsIgnoreCase(BrainstormWeb.getInstance().server.users.get(i))) break;
        }
        out.println("						User:<input name=\"field_account\" type=\"text\" value=\""+BrainstormWeb.getInstance().server.users.get(i)+"\"/><br/>");
        out.println("						Status:<input name=\"field_status\" type=\"text\" value=\""+BrainstormWeb.getInstance().server.status.get(i)+"\"/><br/>");
        //out.println("						<input name=\"button_restart\" type=\"submit\" value=\"Restart!\"/><br/>");
        out.println("						Round:<input name=\"field_round\" type=\"text\" value=\""+BrainstormWeb.getInstance().server.curr_stage+"\"/><br/>");
        out.println("					</td>");
        out.println("					<td align=\"left\" valign=\"top\">");
        out.println("						Theme:<input name=\"field_theme\" type=\"text\" value=\""+BrainstormWeb.getInstance().server.getCollectiveTheme()+"\"/><br/>");
        out.println("						Text:<br/><textarea name=\"field_sheet\" cols=\"80\" rows=\"80\">"+BrainstormWeb.getInstance().server.sheets.get(i)+"</textarea><br/>");
        out.println("						<input name=\"button_request_change\" type=\"submit\" value=\"Change sheet!\"/></br>");
        out.println("					</td>");
        out.println("					<td align=\"left\" valign=\"top\">");
        out.println("						Users online:<br/>");
        out.println("						-------------<br/>");
        for(i=0; i<BrainstormWeb.getInstance().server.users.size(); i++){
        	out.println(BrainstormWeb.getInstance().server.users.get(i)+" - "+BrainstormWeb.getInstance().server.status.get(i)+"<br/>");
        }
        out.println("						<br/>");
        out.println("						<br/>");
        out.println("						<br/>");
        out.println("						Status meaning:<br/>");
        out.println("						--------------<br/>");
        out.println("						0 - offline<br/>");
        out.println("						1 - online<br/>");
        out.println("						2 - theme submitted<br/>");
        out.println("						3 - writing<br/>");
        out.println("						4 - waiting for change<br/>");
        out.println("						5 - finished<br/>");
        out.println("					</td>");
        out.println("				</tr>");
        out.println("			</table>");
        out.println("		</form>");
        out.println("	</body>");
        out.println("</html>");
        out.close();
	}
	
	public void doGetFinal(HttpServletRequest req, HttpServletResponse res, String name) throws ServletException, IOException {
		res.setContentType("text/html;charset=UTF-8");
        res.setHeader("pragma", "no-cache");
        PrintWriter out=res.getWriter();
        out.println("<html>");
        out.println("	<head>");
        out.println("		<title>::BrainStorm::beta</title>");
        out.println("	</head>");
        out.println("	<body>");
        out.println("		<form method=\"post\">");
        out.println("			<table>");
        out.println("				<tr>");
        out.println("					<td align=\"left\" valign=\"top\">");
        int i;
        for(i=0; i<BrainstormWeb.getInstance().server.users.size(); i++){
        	if(name.equalsIgnoreCase(BrainstormWeb.getInstance().server.users.get(i))) break;
        }
        out.println("						User:<input name=\"field_account\" type=\"text\" value=\""+BrainstormWeb.getInstance().server.users.get(i)+"\"/><br/>");
        out.println("						Status:<input name=\"field_status\" type=\"text\" value=\""+BrainstormWeb.getInstance().server.status.get(i)+"\"/><br/>");
        out.println("						<input name=\"button_restart\" type=\"submit\" value=\"Restart!\"/><br/>");
        out.println("					</td>");
        out.println("					<td align=\"left\" valign=\"top\">");
        out.println("						Theme:<input name=\"field_theme\" type=\"text\" value=\""+BrainstormWeb.getInstance().server.getCollectiveTheme()+"\"/><br/>");
        out.println("						Text:<br/><textarea name=\"field_sheet\" cols=\"80\" rows=\"80\">"+BrainstormWeb.getInstance().server.finalsheet+"</textarea><br/>");
        //out.println("						<input name=\"button_request_change\" type=\"submit\" value=\"Change sheet!\"/></br>");
        out.println("						Round:<input name=\"field_round\" type=\"text\" value=\""+BrainstormWeb.getInstance().server.curr_stage+"\"/><br/>");
        out.println("					</td>");
        out.println("					<td align=\"left\" valign=\"top\">");
        out.println("						Users online:<br/>");
        out.println("						-------------<br/>");
        for(i=0; i<BrainstormWeb.getInstance().server.users.size(); i++){
        	out.println(BrainstormWeb.getInstance().server.users.get(i)+" - "+BrainstormWeb.getInstance().server.status.get(i)+"<br/>");
        }
        out.println("						<br/>");
        out.println("						<br/>");
        out.println("						<br/>");
        out.println("						Status meaning:<br/>");
        out.println("						--------------<br/>");
        out.println("						0 - offline<br/>");
        out.println("						1 - online<br/>");
        out.println("						2 - theme submitted<br/>");
        out.println("						3 - writing<br/>");
        out.println("						4 - waiting for change<br/>");
        out.println("						5 - finished<br/>");
        out.println("					</td>");
        out.println("				</tr>");
        out.println("			</table>");
        out.println("		</form>");
        out.println("	</body>");
        out.println("</html>");
        out.close();
	}
	
	public void doGetMessage(HttpServletRequest req, HttpServletResponse res, String msg) throws ServletException, IOException {
		res.setContentType("text/html;charset=UTF-8");
        res.setHeader("pragma", "no-cache");
        PrintWriter out=res.getWriter();
        out.println("<html>");
        out.println("	<head>");
        out.println("		<title>::BrainStorm::beta</title>");
        out.println("	</head>");
        out.println("	<body>");
        out.println(msg);
        out.println("	</body>");
        out.println("</html>");
        out.close();
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		if(req.getParameter("button_login")!=null && req.getParameter("button_login").equals("Login!")){
			String login = req.getParameter("field_account");
			if(login != null && login != ""){
				if(BrainstormWeb.getInstance().addUser(login)){
					BrainstormWeb.getInstance().doGetTheme(req, res, login);
					return;
				}
				else{
					BrainstormWeb.getInstance().doGetMessage(req, res, "Error adding user.");
					return;
				}
			}
		}
		if(req.getParameter("button_reload")!=null && req.getParameter("button_reload").equals("Reload!")){
			String login = req.getParameter("field_account");
			int index = BrainstormWeb.getInstance().server.getIndex(login);
			int stat = BrainstormWeb.getInstance().server.status.get(index);
			switch(stat){
			case 1:{
				BrainstormWeb.getInstance().doGetTheme(req, res, login);
				return;
			}
			default:{
				BrainstormWeb.getInstance().doGetMessage(req,res,"Error reloading page - Nothing happened.");
				return;
			}
			}
		}
		if(req.getParameter("button_submit_theme")!=null && req.getParameter("button_submit_theme").equals("Submit theme!")){
			String login = req.getParameter("field_account");
			String theme = req.getParameter("field_theme");
			if(login != null && login != "" && theme != null ){
				BrainstormWeb.getInstance().addTheme(login, theme);
				BrainstormWeb.getInstance().doGetWaiting(req, res, login);
				return;
			}
		}
		if(req.getParameter("button_request_change")!=null && req.getParameter("button_request_change").equals("Change sheet!")){
			String login = req.getParameter("field_account");
			String text = req.getParameter("field_sheet");
			if(login!=null && login!="" && text != null){
				int index = BrainstormWeb.getInstance().server.getIndex(login);
				BrainstormWeb.getInstance().server.status.set(index, new Integer(4));
				BrainstormWeb.getInstance().server.sheets.set(index, text);
				BrainstormWeb.getInstance().doGetWaiting(req, res, login);
				return;
			}
		}
		if(req.getParameter("button_next")!=null && req.getParameter("button_next").equals("Next!")){
			String login = req.getParameter("field_account");
			int index = BrainstormWeb.getInstance().server.getIndex(login);
			int stat = BrainstormWeb.getInstance().server.status.get(index);
			switch(stat){
			case 0:{
				break;
			}				
			case 1:{
				break;
			}
			case 2:{
				boolean not_all_themes_submitted = false;
				boolean exist_writing = false;
				for(int i=0; i<BrainstormWeb.getInstance().server.status.size(); i++){
					if(BrainstormWeb.getInstance().server.status.get(i)==1){
						not_all_themes_submitted=true;
						break;
					}
				}
				for(int i=0; i<BrainstormWeb.getInstance().server.status.size(); i++){
					if(BrainstormWeb.getInstance().server.status.get(i)==3){
						exist_writing=true;
						break;
					}
				}
				if(not_all_themes_submitted){
					BrainstormWeb.getInstance().doGetWaiting(req, res, login);
					return;
				}
				if(BrainstormWeb.getInstance().server.getStatus()==2){
					BrainstormWeb.getInstance().server.status.set(index, new Integer(3));
					BrainstormWeb.getInstance().server.initSheets();
					BrainstormWeb.getInstance().doGetBrainstorm(req, res, login);
					return;
				}
				if(exist_writing){
					BrainstormWeb.getInstance().server.status.set(index, new Integer(3));
					BrainstormWeb.getInstance().doGetBrainstorm(req, res, login);
					return;
				}
				break;
			}
			case 3:{
				BrainstormWeb.getInstance().doGetBrainstorm(req, res, login);
				return;
			}
			case 4:{
				boolean exist_writing = false;
				boolean has_finished = false;
				for(int i=0; i<BrainstormWeb.getInstance().server.status.size(); i++){
					if(BrainstormWeb.getInstance().server.status.get(i)==3){
						exist_writing=true;
						break;
					}
				}
				for(int i=0; i<BrainstormWeb.getInstance().server.status.size(); i++){
					if(BrainstormWeb.getInstance().server.status.get(i)==5){
						has_finished=true;
						break;
					}
				}
				if(BrainstormWeb.getInstance().server.status.get(index)==4 && exist_writing){
					BrainstormWeb.getInstance().doGetWaiting(req, res, login);
					return;
				}
				if(BrainstormWeb.getInstance().server.getStatus()==4 || has_finished){
					if(BrainstormWeb.getInstance().server.curr_brainstorm_stage < BrainstormWeb.getInstance().server.users.size()-1){
						BrainstormWeb.getInstance().change(); //also write backup log of jokes list in each step
						BrainstormWeb.getInstance().server.setStatus(new Integer(3));
						BrainstormWeb.getInstance().doGetBrainstorm(req, res, login);
						return;
					}
					else{
						BrainstormWeb.getInstance().server.setStatus(new Integer(5));
						BrainstormWeb.getInstance().server.finalizeSheets();
						BrainstormWeb.getInstance().doGetFinal(req, res, login);
						return;
					}
				}
				break;
			}
			case 5:{
				BrainstormWeb.getInstance().doGetFinal(req, res, login);
				return;
			}
			default:{
				break;
			}
			}
		}
		if(req.getParameter("button_restart")!=null && req.getParameter("button_restart").equals("Restart!")){
			doGet(req, res);
			return;
		}
		BrainstormWeb.getInstance().doGetMessage(req,res,"Nothing happened.");
		return;
	}

}
