<?php
	# include facebook client library
	include_once 'facebook-platform/client/facebook.php';
	include_once 'facebook-platform/client/facebookapi_php5_restlib.php';
	
	# include some application-specific properties
	include_once 'config.php';
	
	# init facebook client
	$facebook = new Facebook($api_key, $secret);
	$facebook->require_frame();
	$user = $facebook->require_add();
	$uradarid = "";
?> 