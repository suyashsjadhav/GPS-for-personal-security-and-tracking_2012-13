 <?php
$con = mysql_connect("localhost","root","");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }

mysql_select_db("track", $con);

	$latitude = 5.12;
	$longitude = 4.56;
	$device_id = 39488340020245656;
	$QR = 8867;
	
	$tat = mysql_query("select * from child_device where device_id = $device_id");
	$ti = mysql_fetch_array($tat,MYSQL_NUM);
	
	if($ti[0])
	{
	mysql_query("UPDATE child_device set latitude=$latitude,longitude=$longitude WHERE device_id = $ti[0]");
	$msg = "Coordinates Updated!!".""."Latitude = "."".$latitude.""."Longitude = "."".$longitude;
	echo "updated";
	}
	else
	{
	
	mysql_query("INSERT INTO child_device (device_id,latitude,longitude)VALUES ('$device_id','$latitude','$longitude')");
	$msg = "Coordinates and device inserted!!".""."Latitude = "."".$latitude.""."Longitude = "."".$longitude;
	echo "inserted";
	}
	
	$parid ="APA91bHKxMnQakK5o6lPurVZOnU0DaJNb_MG3DnmVEoEPKfyvnJZWd465znEFL1R_A6woHfpL_uKDW8VfUnpC8zhBKsMymuIs1TGdOr8iEyaF22J2mSjl9BzLHPo4QzSLMb6yyc2SSuUUW2Cis1MqZD1vWURa1pwN-gkpncn_v67OPfSGwnOU6M";
	
	send_msg($parid,$msg);
	function send_msg($parid,$msg)
{
	
	
	$message = $msg;

	//echo $message; */

 	//$regId = $_GET["regId"];
 
   	//$message = $_GET["message"];

//$regId ="APA91bHKxMnQakK5o6lPurVZOnU0DaJNb_MG3DnmVEoEPKfyvnJZWd465znEFL1R_A6woHfpL_uKDW8VfUnpC8zhBKsMymuIs1TGdOr8iEyaF22J2mSjl9BzLHPo4QzSLMb6yyc2SSuUUW2Cis1MqZD1vWURa1pwN-gkpncn_v67OPfSGwnOU6M";
 
	$regId = $parid;

    
    include_once './GCM.php';

    
    $gcm = new GCM();


    
	$registatoin_ids = array($regId);

    	$message = array("price" => $message);


    	$result = $gcm->send_notification($registatoin_ids, $message);


	echo $result;


//send_msg();
}

//send_msg($parid,$msg);
	
	
	
	
	
	
	
	
	
	
	

if(	 (isset($_POST['latitude']) && $_POST['latitude'] != '') && (isset($_POST['longitude']) && $_POST['longitude'] != '')&& (isset($_POST['QR']) && $_POST['QR'] != '') && (isset($_POST['device_id']) && $_POST['device_id'] != '') ) {
   

    $latitude = $_POST['latitude'];
	$longitude = $_POST['longitude'];
	$device_id = $_POST['device_id'];
	$QR = $_POST['QR'];
	
	$tat = mysql_query("select * from child_device where device_id = $device_id");
	$ti = mysql_fetch_array($tat,MYSQL_NUM);
	
	if($ti[0])
	{
	mysql_query("UPDATE child_device set (latitude,longitude)VALUES ('$latitude','$longitude') WHERE device_id = $device_id");
	
	$msg = "Coordinates Updated!!".""."Latitude = "."".$latitude.""."Longitude = "."".$longitude;
	}
	else
	{
	
	mysql_query("INSERT INTO child_device (device_id,latitude,longitude)VALUES ('$device_id','$latitude','$longitude')");
	$msg = "Coordinates and device inserted!!".""."Latitude = "."".$latitude.""."Longitude = "."".$longitude;
	
	}
	
	$data = mysql_query("select p.child_name,p.gcm_regid from parent p where p.child_id=$QR");
	
	$row = mysql_fetch_array($data); 
    	
	$child_name = $row['child_name'];
	
	$parid = $row['gcm_regid'];

	mysql_query("INSERT INTO student (child_id,device_id,child_name) VALUES ('$QR','$device_id','$child_name')");



//send_msg($parid,$msg);


function send_msg($parid,$msg)
{
	
	
	$message = $msg;

	//echo $message; */

 	//$regId = $_GET["regId"];
 
   	//$message = $_GET["message"];

//$regId ="APA91bHKxMnQakK5o6lPurVZOnU0DaJNb_MG3DnmVEoEPKfyvnJZWd465znEFL1R_A6woHfpL_uKDW8VfUnpC8zhBKsMymuIs1TGdOr8iEyaF22J2mSjl9BzLHPo4QzSLMb6yyc2SSuUUW2Cis1MqZD1vWURa1pwN-gkpncn_v67OPfSGwnOU6M";
 
	$regId = $parid;

    
    include_once './GCM.php';

    
    $gcm = new GCM();


    
	$registatoin_ids = array($regId);

    	$message = array("price" => $message);


    	$result = $gcm->send_notification($registatoin_ids, $message);


	echo $result;


//send_msg();
}

send_msg($parid,$msg);
}

mysql_close($con);
?>

