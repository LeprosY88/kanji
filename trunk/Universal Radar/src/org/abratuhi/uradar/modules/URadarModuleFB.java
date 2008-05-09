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
		String[] fb_fieldnames = {"uradarid", "moduleid", "fb_name", };
		String[] fb_fieldtypes = {"varchar(100)", "varchar(100)", "varchar(100)"};
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

	public String getFriendsInfoFromModule(String myURadarID, String[] friendsURadarID, String modulename){
		if(modulename.equals("sl")){
			URadarModule module = URadar.getInstance().findModule(modulename);
			if(module != null && (module instanceof URadarModuleSL)){
				return ((URadarModuleSL) module).getFriendsInfo(myURadarID, friendsURadarID);
			}
			else{
				return null;
			}
		}
		else{
			return super.getFriendsInfoFromModule(myURadarID, friendsURadarID, modulename);
		}
	}

}
