<?php
	include_once 'slradar.php';
	
	function wrap_rows_to_table($rows){
		return  "<table align=\"center\" cellspacing=\"10px\">".$rows."</table>";
	}
	
	function response_header(){
		$fbml ="";
		$fbml .= "<tr>";		
		$fbml .= "	<th>URADAR</th>";
		$fbml .= "	<th>FB</th>";
		$fbml .= "	<th>SL</th>";
		$fbml .= "	<th>SL:Island</th>";
		$fbml .= "	<th>SL:Position</th>";
		$fbml .= "	<th>SL:Status</th>";
		$fbml .= "</tr>";
		return $fbml;
	}
	
	function parse_response($response_string){
		$xmlDoc = new DOMDocument();
		$xmlDoc -> loadXML($response_string);
		return $xmlDoc -> documentElement;
	}
	
	function response_row2html($child){
		$fbml = '';
		$uradid = $child -> getElementsByTagName('uradarid') -> item(0) -> nodeValue;
		$slid = $child -> getElementsByTagName('moduleid') -> item(0) -> nodeValue;
		$fbid = $child -> getElementsByTagName('moduleid') -> item(1) -> nodeValue;
		$slname = $child -> getElementsByTagName('sl_name') -> item(0) -> nodeValue;
		$status = $child -> getElementsByTagName('sl_status') -> item(0) -> nodeValue;
		if($status == 'offline'){
			$fbml .= "<tr>";
			$fbml .= "	<td>".$uradid."</td>";
			$fbml .= "	<td><fb:name uid=\"".$fbid."\"/></td>";
			$fbml .= "	<td>".$slname."</td>";
			$fbml .= "	<td></td>";
			$fbml .= "	<td></td>";
			//$fbml .= "	<td>".$status."</td>";
			$fbml .= "	<td><img src=\"http://www.coiouhkc.dyndns.org:8088/facebook/img/status_offline.png\"/></td>";
			$fbml .= "</tr>";
		}
		elseif($status == 'online'){
			$realm = $child -> getElementsByTagName('sl_realm') -> item(0) -> nodeValue;
			$position = $child -> getElementsByTagName('sl_position') -> item(0) -> nodeValue;
			$fbml .= "<tr>";
			$fbml .= "	<td>".$uradid."</td>";
			$fbml .= "	<td><fb:name uid=\"".$fbid."\"/></td>";
			$fbml .= "	<td>".$slname."</td>";
			$fbml .= "	<td>".$realm."</td>";
			$fbml .= "	<td>".$position."</td>";
			//$fbml .= "	<td>".$status."</td>";
			$fbml .= "	<td><img src=\"http://www.coiouhkc.dyndns.org:8088/facebook/img/status_online.png\"/></td>";
			$fbml .= "</tr>";
		}
		return $fbml;
	}
	
?> 