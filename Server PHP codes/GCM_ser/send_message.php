<?php

/*
 * To change this template, choose Tools |
 Templates
 * and open the template in the editor.
 */

$con = mysql_connect("localhost","root","");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

//mysql_select_db("student_info", $con);

mysql_select_db("track", $con);

function send_msg()
{

$max = mysql_query("select device_id from child_device");
while($xam = mysql_fetch_array($max,MYSQL_NUM)){
$data = mysql_query("select child_id from student where device_id=$xam[0] ");
	
	while ($row = mysql_fetch_array($data, MYSQL_NUM)) {
    	
	$wer = mysql_query("select p.gcm_regid from parent p where p.child_id = $row[0]");
 	
	$lang = mysql_query(" select c.latitude,c.longitude from child_device C,student S where child_id = $row[0] and c.device_id= s.device_id");

	//new
	$la=mysql_fetch_array($lang,MYSQL_NUM);
	
//	$rt=mysql_fetch_array($wer,MYSQL_NUM);
	
//	$latitude ="lattitude = ". " " .$la[0];
	//$longitude ="longitude= ". " " .$la[1];
	$qt= 1;
	$lat12 =$qt." ".$la[0]." ".$la[1];
	//$tata = implode(',', $rt);
	
	while ($rt=mysql_fetch_array($wer,MYSQL_NUM)){
	//$regId = $tata;
 	//$regId = "APA91bFR5ju1S0nZpsQHQJ1GVHXUJ5kO53bG2N6A3h_5ZeO71vv6atDpTXBN5JuuLuIfcAB8MthXfF7qc-y_wt0N6s4siTft4dTq7Xv3nQCzVDihWsiNzJVOcUf6OQmRFxjVZyrUsMYtB15T8-SVwNzTTHGneE4x04HnEQlowSOdIW3CkQJesLk";
	include_once './GCM.php';

    
    $gcm = new GCM();
	//$registatoin_ids = array($regId);
	$registatoin_ids = array($rt[0]);

    	//$message = array("price" => $latitude,"price1"=>$longitude);
		$message = array("price" => $lat12);
		
    	$result = $gcm->send_notification($registatoin_ids, $message);
		
		echo $message;

	echo $result;
	

	
	}
	}

}
}
send_msg();

?>