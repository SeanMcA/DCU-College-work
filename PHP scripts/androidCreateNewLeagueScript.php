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

$leagueName = $_POST['leagueName'];
$leagueType = $_POST['leagueType'];
$start_Date = $_POST['sDate'];
$end_date = $_POST['eDate'];
$userId = $_POST['userId'];



$sql = "INSERT INTO league (league_name, start_date, finish_date, type, created_by) 
				values('$leagueName', '$start_Date', '$end_date', '$leagueType' , '$userId')";
//echo $sql;
//echo "<br>";


if ($conn->query($sql) === TRUE) {
    echo "New league created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}

$conn->close();
?>