<?php

$servername = "localhost";
$username = "zelusitc_sean";
$password = "guiness";
$database = "zelusitc_temp";


//$joinedGreenCoordinates = "53.654654,-6.554654,53.246846,-6.365464";
//$courseIDString = "1";
//$courseID = (int)$courseIDString;

$joinedGreenCoordinates = $_POST['joinedCoordinatesString'];
$courseIDString = $_POST['courseIDString'];
$courseID = (int)$courseIDString;


// Create connection
$conn = new mysqli($servername, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// prepare and bind
	
		$stmt = $conn->prepare("UPDATE course SET pins = ? WHERE course_id = ?");
		$stmt->bind_param("si", $joinedGreenCoordinates,$courseID);
		$stmt->execute();
		echo "Successful";







mysql_close($conn);
?>