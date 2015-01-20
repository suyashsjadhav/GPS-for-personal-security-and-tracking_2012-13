<?php

// response json
$json = array();

/**
 * Registering a user device
 * Store reg id in users table
 */
if (isset($_POST["name"]) && isset($_POST["email"]) && isset($_POST["regId"]) && isset($_POST["child_name"]) && isset($_POST["child_id"])) {
    $name = $_POST["name"];
    $email = $_POST["email"];
    $child_name = $_POST["child_name"];
    $child_id = $_POST["child_id"];
    $gcm_regid = $_POST["regId"]; // GCM Registration ID
    // Store user details in db
    include_once './db_functions.php';
    include_once './GCM.php';

    $db = new db_functions();
    $gcm = new GCM();

    $res = $db->storeUser($name, $email, $child_name, $child_id, $gcm_regid);

    $registatoin_ids = array($gcm_regid);
    $message = array("product" => "shirt");

    $result = $gcm->send_notification($registatoin_ids, $message);

    echo $result;
} else {
    // user details missing
}
?>