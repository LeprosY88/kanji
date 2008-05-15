<?php

	include_once 'slradar.php';
	include_once 'requests.php';
	include_once 'prints.php';
	
	
	function fb_add_own(){
		echo file_get_contents("login.html");
	}
	
	function start(){
		global $uradarid, $user;
		
		$m_res_login = fb_login($uradarid, $user);
		
		if($m_res_login == 'invalid_id'){
			fb_add_own();
			return;
		}
		else{
			if(strlen($m_res_login)>0){
				$uradarid = $m_res_login;
				main();
				return;
			}
			else{
				echo '<p>'.'URadar: UNKNOWN ERROR'.'</p>';
				return;
			}
		}
	}
	
	function main(){
		global $facebook, $user;
		
		$appfriends = $facebook->api_client->friends_getAppUsers();
		
		# check  whether all of Facebook user's friends are in DB, if not and friends itself is in DB-> add
		if(sizeof($appfriends)>0){
			foreach($appfriends as $appfriend){
				fb_add_friend($appfriend);
			}
		}
		
		#
		$response_own = fb_request_own_info_from_sl();
		$response_friends = fb_request_friends_info_sl();
		
		$xmldocroot_own = parse_response($response_own);
		$xmldocroot_friends = parse_response($response_friends);
		
		$html_canvas = '';
		$html_profile = '';
		
		$html_canvas .= response_header();
		$html_profile .= response_header();
		
		foreach ($xmldocroot_own->childNodes AS $item){
			$html_canvas .= response_row2html($item);
			$html_profile .= response_row2html($item);
		}
		
		foreach ($xmldocroot_friends->childNodes AS $item){
			$html_canvas .= response_row2html($item);
		}
		
		$html_canvas = wrap_rows_to_table($html_canvas);
		$html_profile = wrap_rows_to_table($html_profile);
		
		#
		$facebook->api_client->profile_setFBML('', $user, $html_profile);
		echo $html_canvas;
		
		#
		return;
		
	}
	
	start();

?> 