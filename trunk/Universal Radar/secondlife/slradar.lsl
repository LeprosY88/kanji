// USER-DEFINED PART
string uradarid = ""; //
string uradarpasswd = ""; //

//
// NOTHING TO CHANGE FROM HERE ON, UNLESS YOU ARE AN EXPERIENCED USER
//
string host="http://www.coiouhkc.dyndns.org:8080/uradar/uradar";
float gap = 30.0;   // update intervall
string slid;    // second life id
key http_request_id_own;

// function used to send information about own user
sl_own_info(string status){
	string status = (string) status;
	string name = (string) llKey2Name(llGetOwner());
	string region = (string) llGetRegionName();
	vector pos = llGetPos();
	string posx = (string) ((integer)(pos.x));
	string posy = (string) ((integer)(pos.y));
	string posz = (string) ((integer)(pos.z));
	string info =     "sl_status="+status+
		"&uradarid="+uradarid+
		"&moduleid="+slid+
		"&sl_name="+name+
		"&sl_realm="+region+
		"&sl_position=<"+posx+","+posy+","+posz+">";
	http_request_id_own = llHTTPRequest(host+"?request=true&reqapp=uradar&reqmodule=sl&reqtype=addupdate_user&"+info, [], "");
}

default
{
	state_entry(){
		llSetText("SLRADAR",<255,0,0>,0.1f);
	}

	state_exit(){
		sl_own_info("offline");
	}

	attach(key attached){
		if (attached == NULL_KEY){  // object has been detached
			sl_own_info("offline");    // if object is detached -> logout
		}
		if (attached != NULL_KEY){   // object has been attached
			slid = (string)llGetOwner();
			llSetTimerEvent(gap);
		}
	}

	timer(){    // timer event -> is used for regular updates of inforamtion about oneself as well as retrieving information about facebook friends
		sl_own_info("online");
	}
}