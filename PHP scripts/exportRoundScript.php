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

$userID = $_POST['userId'];
$course_name = $_POST['course_name'];
$round_date = $_POST['roundDate'];
$sg_driving = $_POST['sg_driving'];
$sg_long_game = $_POST['sg_long_game'];
$sg_short_game = $_POST['sg_short_game'];
$sg_putting = $_POST['sg_putting'];


//$userID = '1';
//$course_name = 'TEST';
//$start_date = '2016-03-22';
//$sg_driving = '1.11';
//$sg_long_game = '2.22';
//$sg_short_game = '3.33';
//$sg_putting = '4.44';



$sql = "INSERT INTO round (user_id, course, start_date, sg_driving, sg_long_game, sg_short_game, sg_putting) values('$userID', '$course_name', '$round_date', '$sg_driving', '$sg_long_game', '$sg_short_game', '$sg_putting')";
echo $sql;
echo "<br>";


if ($conn->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();
?>