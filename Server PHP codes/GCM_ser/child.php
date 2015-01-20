 <?php
 echo "Happy bday";
$con = mysql_connect("localhost","root","");
if (!$con)
  {
  die('Could not connect: ' . mysql_error());
  }
		
	
if( (isset($_POST['device_id']) && $_POST['device_id'] != '') && (isset($_POST['latitude']) && $_POST['latitude'] != '') && (isset($_POST['longitude']) && $_POST['longitude'] != '')&&(isset($_POST['QR']) && $_POST['QR'] != '') ) {
   
echo "Happy bday";
    $device_id = $_POST['device_id'];
    $latitude = $_POST['latitude'];
	$longitude = $_POST['longitude'];
	$QR = $_POST['QR'];
	mysql_select_db( "track", $con );

	//mysql_query("INSERT INTO child_device (device_id,latitude,longitude)VALUES ('$device_id','$latitude','$longitude')");
	$tat = mysql_query("select * from child_device where device_id = $device_id");
	$ti = mysql_fetch_array($tat, MYSQL_NUM);
	if($ti[0])
	{
	mysql_query("UPDATE child_device set latitude=$latitude , longitude=$longitude WHERE device_id = $ti[0]");
	
	echo "updated";
	}
	else
	{
	mysql_query("INSERT INTO child_device (device_id,latitude,longitude)VALUES ('$device_id','$latitude','$longitude')");
	
	echo "inserted";
	}
	
	$tata = mysql_query("select * from student where child_id = $QR");
	$titi = mysql_fetch_array($tata, MYSQL_NUM);
	if($titi[0])
	{
		echo "Happy bday";
		$tr = mysql_query("select status from student where child_id = $QR");
		$yh = mysql_fetch_array($tr, MYSQL_ASSOC);
		
		$ry = mysql_query("select device_id from student where child_id= $QR");
		$yr = mysql_fetch_array($tr, MYSQL_NUM);
	
	
		if($device_id != $yr[0])
		{ 	
			if($yh['status']==0)
			{
				mysql_query("DELETE FROM student where child_id = $QR");
				mysql_query("INSERT INTO student (child_id,device_id,child_name,status) VALUES ('$QR','$device_id','$child_name',1)");
			}
			else{}		
		}
		else
		{
			if($yh['status']==0)
			{
				$ph = 1; 
			}
			else
			{
				$ph = 0;
			}
			
			mysql_query("UPDATE student SET  status = $ph WHERE child_id = $QR");
		
		}
	}
	else
	{
		$tg = mysql_query("select child_name from parent where child_id = $QR");
		
		if($gt = mysql_fetch_array($tg,MYSQL_NUM))
		{
		$riti = mysql_query("INSERT INTO student (child_id,device_id,child_name,status) VALUES ('$QR','$device_id',$gt[0],1)");
		}
	}
	
	$msg =$ph." ".$latitude." ".$longitude;
	$data = mysql_query("select gcm_regid from parent where child_id = (select child_id from child_device where device_id = $device_id) and child_id = $QR ");
	$row = mysql_fetch_array($data,MYSQL_ASSOC);
  	$parid = $row['gcm_regid'];
	send_msg($parid,$msg);
	
	
	
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

