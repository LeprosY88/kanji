<?php
include_once 'config.php';
#########################################
function showErrorPassword($div){
	echo file_get_contents("registration-errorpasswd.html");
}
function showErrorLogin($div){
	echo file_get_contents("registration-errorlogin.html");
}
function showError(){
	echo file_get_contents("registration-errorunknown.html");
}
function showSuccess(){
	echo file_get_contents("registration-success.html");
}
function registerURadarID(){
	# in case entered passwords didn't match
	if($_GET['uradarpasswd'] != $_GET['uradarpasswdconfirmed']){
		showErrorPassword();
	}
	# in case entered passwords match
	global $host;
	$r = new HttpRequest($host, HttpRequest::METH_GET);
	$r -> addQueryData(array('request'=>'true', 'reqapp'=>'uradar', 'reqmodule'=>'base', 'reqtype'=>'addupdate_user', 'uradarid'=>$_GET['uradarid'], 'uradarpasswd'=>$_GET['uradarpasswd']));
	$r -> send();
	$resp = $r -> getResponseMessage() -> getBody();
	
	# in case entered login is "busy"
	if(trim($resp) == "ok"){
		showSuccess(null);
	}
	else{
		if(trim($resp) == "cancel"){
			showErrorLogin(null);
		}
		else{
			showError();
		}
	}
}
#########################################


$uradarid = $_GET['uradarid'];
$uradarpasswd = $_GET['uradarpasswd'];
$uradarpasswdconfirmed = $_GET['uradarpasswdconfirmed'];

if($uradarpasswd != $uradarpasswdconfirmed){
	showErrorPassword(null);
	return;
}
registerURadarID();

?> 