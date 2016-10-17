<?php

$servername = "localhost";
$username = "zelusitc_sean";
$password = "guiness";
$database = "zelusitc_temp";


//$receivedUsername = "Sean McAteer";
//$receivedPassword = "guiness";



$receivedUsername = $_POST['sentUsername'];
$receivedPassword = $_POST['sentPassword'];
$receivedEmail = $_POST['sentEmail'];



//encrypt the password
$password_hash = crypt($receivedPassword);


// Create connection
$conn = new mysqli($servername, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Check to see if name is already in use
$stmt = $conn->prepare("SELECT name FROM player WHERE name = ?");
$stmt->bind_param("s", $receivedUsername);

 /* execute query */
    $stmt->execute();
	
	 /* bind result variables */
    $stmt->bind_result($name);

$result = $stmt->fetch();


//if a name is returned then the username the user tried is already in use
if( !empty( $result ))
{
	echo 'Username is not available';
} 
else
{
	//check if the email has already been used.
	$stmt2 = $conn->prepare("SELECT email FROM player WHERE email = ?");
	$stmt2->bind_param("s", $receivedEmail);

	 /* execute query */
		$stmt2->execute();
		
		 /* bind result variables */
		$stmt2->bind_result($name);

	$result2 = $stmt2->fetch();


	//if an email is returned then the email the user tried is already in use
	if( !empty( $result2 ))
	{
		echo 'Email is not available';
	} 
	else
	{
		//Username and email are not in use so register user.
			$stmt2 = $conn->prepare("INSERT INTO player (name, password, email) values(?,?,?)");
			$stmt2->bind_param("sss", $receivedUsername, $password_hash, $receivedEmail);
			$stmt2->execute();
			echo "New record created successfully";
	}
		
		
		
}






mysql_close($conn);
?>