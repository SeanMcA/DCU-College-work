<?php
$servername = "localhost";
$username = "zelusitc_sean";
$password = "guiness";
$dbname = "zelusitc_temp";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$testString = "test";


$result = mysqli_query($conn,"SELECT sent FROM network_conn_test where received='test'");
$row = mysqli_fetch_array($result);
$data = $row[0];


if($data){
echo $data;
}
mysqli_close($con);
?>