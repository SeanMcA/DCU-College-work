<?php
$servername = "localhost";
$username = "zelusitc_sean";
$password = "guiness";
$dbname = "zelusitc_temp";


$userID = $_POST['userID'];
$leagueCode = $_POST['leagueCode'];

//$userID = 1;
//$leagueCode = 90;


$conn = mysql_connect($servername,$username,$password) or trigger_error(mysql_error(),E_USER_ERROR);
mysql_select_db($dbname,$conn);

				
$result=mysql_query("SELECT league_name FROM league WHERE league_id = '$leagueCode'");

$num_rows = mysql_num_rows($result);
while($row = mysql_fetch_array($result))
{

$r[]=$row;
$check=$row['league_name'];
}

if($check==NULL)
{
	echo 'Unable to join this league.';
}
else
{

//echo 'League exists';


		$sql =mysql_query("INSERT INTO league_members (league_id, user_id) values('$leagueCode', '$userID')");
			//echo $sql;
			//echo "<br>";
			
				echo "You have joined this league.";
			
	
}

mysql_close($conn);
?>