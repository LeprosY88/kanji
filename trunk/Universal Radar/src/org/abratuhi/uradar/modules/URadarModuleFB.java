package org.abratuhi.uradar.modules;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;

import org.abratuhi.uradar.model.URadar;
import org.abratuhi.uradar.model.URadarModule;
import org.abratuhi.uradar.model.URadarModuleModel;
import org.abratuhi.uradar.model.URadarUpdateModule;

public class URadarModuleFB extends URadarUpdateModule{

	public URadarModuleFB(String name, String description, Properties props,
			Connection connection) {
		// super
		super("fb", "Facebook update module", props, connection);
		// custom
		// specify the corresponding table
		// fb
		String fb_name = "fb";
		String[] fb_fieldnames = {"uradarid", "moduleid" };
		String[] fb_fieldtypes = {"varchar(100)", "varchar(100)"};
		URadarModuleModel fb_model = new URadarModuleModel(fb_name, fb_fieldnames, fb_fieldtypes);
		// fb_friends
		String fb_friends_name = "fb_friends";
		String[] fb_friends_fieldnames={"uradarid", "uradarid_friend", "visibility"};
		String[] fb_friends_fieldtypes = {"varchar(100)", "varchar(100)", "varchar(100)"};
		URadarModuleModel fb_friends_model = new URadarModuleModel(fb_friends_name, fb_friends_fieldnames, fb_friends_fieldtypes);
		// add
		ArrayList<URadarModuleModel> modelz = new ArrayList<URadarModuleModel>();
		modelz.add(fb_model);
		modelz.add(fb_friends_model);
		this.setModel(modelz);
		// create tables if needed
		if(!this.checkTables()) this.createTables();
	}
	
	public String getOwnInfoFromModule(Properties reqprops){
		// get request info
		String modulename = reqprops.getProperty("reqmodulefrom");
		String myURadarID = reqprops.getProperty("uradarid");
		// proceed request
		if(modulename.equals("sl")){
			URadarModule module = URadar.getInstance().findModule(modulename);
			if(module != null && (module instanceof URadarModuleSL)){
				//System.out.println("URadarModuleFB, getOwnInfoFromModule:\tloading info from module "+module.name);
				//System.out.println("getowninfofroommoduleresponse=\""+((URadarModuleSL) module).getOwnInfo(myURadarID)+"\"");
				return ((URadarModuleSL) module).getOwnInfoWithFB(myURadarID);
			}
			else{
				return null;
			}
		}
		else{
			return super.getOwnInfoFromModule(reqprops);
		}
	}

	public String getFriendsInfoFromModule(Properties reqprops){
		// get request info
		String modulename = reqprops.getProperty("reqmodulefrom");
		String myURadarID = reqprops.getProperty("uradarid");
		String[] friendsURadarID = super.getFriendsURadarID(myURadarID);
		// proceed request
		if(modulename.equals("sl")){
			URadarModule module = URadar.getInstance().findModule(modulename);
			if(module != null && (module instanceof URadarModuleSL)){
				//System.out.println("URadarModuleFB, getFriendsInfoFromModule:\tloading info from module "+module.name);
				return ((URadarModuleSL) module).getFriendsInfoWithFB(friendsURadarID);
			}
			else{
				return null;
			}
		}
		else{
			return super.getFriendsInfoFromModule(reqprops);
		}
	}
	
	public String proceedRequest(Properties reqprops){
		// init response string
		String response = new String();
		// get request type
		String reqtype = reqprops.getProperty("reqtype");
		// switch
		if(reqtype.equals("get_own_info_from_module")){
			response = getOwnInfoFromModule(reqprops);
		}
		if(reqtype.equals("get_friends_info_from_module")){
			response = getFriendsInfoFromModule(reqprops);
		}
		// return, pay attention to super
		if(response == null || response.equals(new String()) || response.length()<1){	// in case URadarModuleFB couldn't proceedrequest itself, ask help from superclass
			response = super.proceedRequest(reqprops);
		}
		return response;
	}

}
