<?php
$servername = "localhost";
$username = "zelusitc_sean";
$username1 = "blah";
$password = "guiness";
$dbname = "zelusitc_login_system";
$dbname2 = "zelusitc_temp";
$db_table_prefix = "uc_";
$mysqli = new mysqli($servername, $username, $password, $dbname);





$usernameposted = $_POST['username'];
$passwordposted = $_POST['password'];

//$usernameposted = "seanmcateer";
//$passwordposted = "guiness42";

$userdetails = fetchUserDetails($usernameposted);
$entered_pass = generateHash($passwordposted,$userdetails["password"]);

if($entered_pass != $userdetails["password"])
	{
		echo "0";
	}
		else
		{
			
			
			$conn = new mysqli($servername, $username, $password, $dbname);
			// Check connection
			if ($conn->connect_error) 
			{
   				 die("Connection failed: " . $conn->connect_error);
			}
			
			$result = mysqli_query($conn,"SELECT id FROM uc_users where 
			user_name='$usernameposted' AND password = '$entered_pass'");
			//echo $result;
			//echo"Hello";
			$row = mysqli_fetch_array($result);
			$data = $row[0];

			if($data)
			{
				echo $data;				
			}
			else
			{
				echo "noLoginID";
				}
			//echo "Correct";
		}




function generateHash($plainText, $salt = null)
{
	if ($salt === null)
	{
		$salt = substr(md5(uniqid(rand(), true)), 0, 25);
	}
	else
	{
		$salt = substr($salt, 0, 25);
	}
	
	return $salt . sha1($salt . $plainText);
}

//Retrieve complete user information by username, token or ID
function fetchUserDetails($username=NULL,$token=NULL, $id=NULL)
{
	global $mysqli,$db_table_prefix; 
	if($username!=NULL) 
	{  
		$stmt = $mysqli->prepare("SELECT 
			id,
			user_name,
			display_name,
			password,
			email,
			activation_token,
			last_activation_request,
			lost_password_request,
			active,
			title,
			sign_up_stamp,
			last_sign_in_stamp
			FROM ".$db_table_prefix."users
			WHERE
			user_name = ?
			LIMIT 1");
		$stmt->bind_param("s", $username);
	}
	elseif($id!=NULL)
	{
		$stmt = $mysqli->prepare("SELECT 
			id,
			user_name,
			display_name,
			password,
			email,
			activation_token,
			last_activation_request,
			lost_password_request,
			active,
			title,
			sign_up_stamp,
			last_sign_in_stamp
			FROM ".$db_table_prefix."users
			WHERE
			id = ?
			LIMIT 1");
		$stmt->bind_param("i", $id);
	}
	else
	{
		$stmt = $mysqli->prepare("SELECT 
			id,
			user_name,
			display_name,
			password,
			email,
			activation_token,
			last_activation_request,
			lost_password_request,
			active,
			title,
			sign_up_stamp,
			last_sign_in_stamp
			FROM ".$db_table_prefix."users
			WHERE
			activation_token = ?
			LIMIT 1");
		$stmt->bind_param("s", $token);
	}
	$stmt->execute();
	$stmt->bind_result($id, $user, $display, $password, $email, $token, $activationRequest, $passwordRequest, $active, $title, $signUp, $signIn);
	while ($stmt->fetch()){
		$row = array('id' => $id, 'user_name' => $user, 'display_name' => $display, 'password' => $password, 'email' => $email, 'activation_token' => $token, 'last_activation_request' => $activationRequest, 'lost_password_request' => $passwordRequest, 'active' => $active, 'title' => $title, 'sign_up_stamp' => $signUp, 'last_sign_in_stamp' => $signIn);
	}
	$stmt->close();
	return ($row);
}
?>