<?php
	include_once 'slradar.php';
	
	function fb_login(){
		global $host, $user;	
		$r_login = new HttpRequest($host, HttpRequest::METH_GET);	
		$r_login -> addQueryData(array(	'request'=>'true',
										'reqapp'=>'uradar',
										'reqmodule'=>'fb',
										'reqtype'=>'resolve_module_id',
										'moduleid'=>$user));	
		$r_login -> send();	
		$m_res_login = $r_login -> getResponseMessage() -> getBody();	
		return trim($m_res_login);
	}
	
	function fb_add_friend($moduleid_friend){
		global $host, $uradarid;	
		$r_add_friends = new HttpRequest($host, HttpRequest::METH_GET);	
		$r_add_friends -> addQueryData(array(	'request'=>'true',
												'reqapp'=>'uradar',
												'reqmodule'=>'fb',
												'reqtype'=>'addupdate_friend',
												'uradarid'=>$uradarid,
												'moduleid_friend'=>$moduleid_friend,
												'visiblity'=>'true'));	
		$r_add_friends -> send();
	}
	
	
	
	function fb_request_own_info_from_sl(){
		global $host, $facebook, $user, $uradarid;
		$r_req_own_info = new HttpRequest($host, HttpRequest::METH_GET);
		$r_req_own_info -> addQueryData(array(	'request'=>'true',
												'reqapp'=>'uradar',
												'reqmodule'=>'fb',
												'reqtype'=>'get_own_info_from_module', 
												'uradarid'=>$uradarid,
												'reqmodulefrom'=>'sl'));
		$r_req_own_info -> send();
		$m_res_own_info = $r_req_own_info -> getResponseMessage() -> getBody();
		
		return trim($m_res_own_info);
	}
	
	
	function fb_request_friends_info_sl(){
		global $host, $uradarid;
		$r_req_friends_info = new HttpRequest($host, HttpRequest::METH_GET);
		$r_req_friends_info -> addQueryData(array(	'request'=>'true',
													'reqapp'=>'uradar',
													'reqmodule'=>'fb',
													'reqtype'=>'get_friends_info_from_module',
													'uradarid'=>$uradarid,
													'reqmodulefrom'=>'sl'));
		$r_req_friends_info -> send();
		
		$m_res_friends_info = $r_req_friends_info -> getResponseMessage() -> getBody();
		
		
		return trim($m_res_friends_info);
	}
?> 