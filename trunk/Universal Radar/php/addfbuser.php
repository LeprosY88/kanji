<?php
	include_once 'config.php';
	include_once 'index.php';
	
	$r = new HttpRequest($host, HttpRequest::METH_GET);
	$r -> addQueryData(array(	'request'=>'true',
								'reqapp'=>'uradar',
								'reqmodule'=>'fb',
								'reqtype'=>'addupdate_user',
								'uradarid'=>$_GET['uradarid'],
								'uradarpasswd'=>$_GET['uradarpasswd'],
								'moduleid'=>$user));
	$r -> send();
	$resp = $r -> getResponseMessage() -> getBody();
	
	if(trim($resp) == "ok"){
		return;
	}
	else{
		echo "ERROR";
		return;
	}
?>