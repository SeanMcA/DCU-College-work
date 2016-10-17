<?php

$servername = "localhost";
$username = "zelusitc_sean";
$password = "guiness";
$database = "zelusitc_temp";


//$userEmail = "test@test.com";
//$newPassword = "guiness";



$userEmail = $_POST['userEmail'];
$newPassword = $_POST['newPassword'];

//encrypt the password
$password_hash = crypt($newPassword);




// Create connection
$conn = new mysqli($servername, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Get username that matches email address
$stmt = $conn->prepare("SELECT name FROM player WHERE email = ?");
$stmt->bind_param("s", $userEmail);

 /* execute query */
    $stmt->execute();
	
	 /* bind result variables */
    $stmt->bind_result($name);

$result = $stmt->fetch();


	//if a name is returned then the email address is on record.
	if(!empty( $result ))
	{
	//Username and email are not in use so register user.
		
	// Create connection
	$conn = new mysqli($servername, $username, $password, $database);

	// Check connection
	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);
	}
				$stmt2 = $conn->prepare("UPDATE player SET password = ? WHERE email = ?");
				$stmt2->bind_param("ss", $password_hash, $userEmail);
				$stmt2->execute();
				echo $name;	
				//echo "all good";
	} 
	else
	{
		echo 'That email address is not on record.';	
	}






mysql_close($conn);
?>