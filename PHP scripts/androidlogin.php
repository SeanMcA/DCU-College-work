<?php
$servername = "localhost";
$username = "zelusitc_sean";
$password = "guiness";
$database = "zelusitc_temp";



$username1 = $_POST['username'];
$password1 = $_POST['password'];

//$username1 = "John99";
//$password1 = "mypassword";






// Create connection
$conn = new mysqli($servername, $username, $password, $database);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
	}
	
	
	
	
	// prepare and bind
$stmt = $conn->prepare("SELECT password FROM player WHERE name = ? ");
$stmt->bind_param("s", $username1);


	/* execute query */
    $stmt->execute();
	
	 /* bind result variables */
    $stmt->bind_result($passwordRetrieved);
	
	$result = $stmt->fetch();
//echo "Rtteieved pw is:" . $passwordRetrieved;



if(crypt($password1, $passwordRetrieved) == $passwordRetrieved)
{
	//echo "yeah";

	// Create connection
	$conn = new mysqli($servername, $username, $password, $database);

	// Check connection
	if ($conn->connect_error) {
		die("Connection failed: " . $conn->connect_error);
		}

		// prepare and bind
		$stmt2 = $conn->prepare("SELECT player_id FROM player WHERE name = ? ");
		$stmt2->bind_param("s", $username1);

	 /* execute query */
		$stmt2->execute();
		
		 /* bind result variables */
		$stmt2->bind_result($playerID);
		
		$result = $stmt2->fetch();

	if( !empty( $result ))
	{	
		printf ("%s", $playerID);	
	} 
	else
	{
		echo "0";
	}
	
}//if
else
{
	echo "0";
}

mysqli_close($conn);
?>