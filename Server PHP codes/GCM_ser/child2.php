<?php
$con = mysql_connect("localhost","root","");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }
		
	
if( (isset($_POST['device_id']) && $_POST['device_id'] != '') && (isset($_POST['latitude']) && $_POST['latitude'] != '') && (isset($_POST['longitude']) && $_POST['longitude'] != '') ) 
{
   

    $device_id = $_POST['device_id'];
    $latitude = $_POST['latitude'];
	$longitude = $_POST['longitude'];
	
	mysql_select_db( "track", $con );

	$tat = mysql_query("select * from child_device where device_id = $device_id");
	$ti = mysql_fetch_array($tat, MYSQL_NUM);
	if($ti[0])
	{
	mysql_query("UPDATE child_device set latitude=$latitude , longitude=$longitude WHERE device_id = $ti[0]");
	
	echo "updated";
	}
	
	
	$ph =2;
	$msg =$ph." ".$latitude." ".$longitude;
	
	$dat = mysql_query("select child_id from student where device_id = $device_id and status = 1"); 
	
	
	while($ro = mysql_fetch_array($dat,MYSQL_NUM)){
	
	$data = mysql_query("select gcm_regid,child_name from parent where child_id = $ro[0]");	
	
	$row = mysql_fetch_array($data, MYSQL_ASSOC);
	$child_name = $row['child_name'];
	$parid = $row['gcm_regid'];
	send_msg($parid,$msg);
	
	}
	
	
}




function send_msg($parid,$msg)
{
	
	
	$message = $msg;

 
	$regId = $parid;

    
    include_once './GCM.php';

    
    $gcm = new GCM();


    
	$registatoin_ids = array($regId);

    	$message = array("price" => $message);


    	$result = $gcm->send_notification($registatoin_ids, $message);


	echo $result;

}

//send_msg($parid,$msg);


mysql_close($con);
?>

