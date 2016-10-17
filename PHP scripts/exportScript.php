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

$latitude = $_POST['latitude'];
$longitude = $_POST['longitude'];
$hit_from = $_POST['hit_from'];
$shot_score = $_POST['shot_score'];
$hole = $_POST['hole'];
$shot_number = $_POST['shot_number'];
$shot_distance = $_POST['shot_distance'];
$round_id = $_POST['round_id'];
$userId = $_POST['userId'];



$sql = "INSERT INTO shot (latitude, longitude, hit_from, shot_score, hole, shot_number, shot_distance, round_id, user_id) values('$latitude', '$longitude', '$hit_from', '$shot_score', '$hole', '$shot_number', '$shot_distance', '$round_id', '$userId')";
echo $sql;
echo "<br>";


if ($conn->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();
?>