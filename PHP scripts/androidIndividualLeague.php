<?php

$servername = "localhost";
$username = "zelusitc_sean";
$password = "guiness";
$database = "zelusitc_temp";

//$loginID = $_POST['loginID'];
//$loginID = 1;
//$individualLeagueStartDate = '2016-03-22';
//$individualLeagueEndDate = '2016-05-22';

$individualLeagueID = $_GET["individualLeagueID"];
$individualLeagueType = $_GET["individualLeagueType"];
$individualLeagueStartDate = $_GET["individualLeagueStartDate"];
$individualLeagueEndDate = $_GET["individualLeagueEndDate"];

//echo "Start date: " . $individualLeagueStartDate;
//echo "<br>";
//echo "End date: " .$individualLeagueEndDate;
//echo "<br>";




$conn = mysql_connect($servername,$username,$password) or trigger_error(mysql_error(),E_USER_ERROR);
mysql_select_db($database,$conn);
$result=mysql_query("SELECT p.name AS Name, lm.user_id, SUM($individualLeagueType) AS score
						FROM league_members AS lm
						JOIN player AS p 
						ON lm.user_id = p.player_id						
						JOIN round AS r
						ON r.user_id = lm.user_id
						WHERE lm.league_id = '$individualLeagueID'
						AND start_date >= '$individualLeagueStartDate'
						AND start_date <= '$individualLeagueEndDate'
						GROUP BY r.user_id
						ORDER BY score desc");

$num_rows = mysql_num_rows($result);
while($row = mysql_fetch_array($result))
{

$r[]=$row;
$check=$row['user_id'];
}

if($check==NULL)
{
$r[$num_rows]='Record is not available';
print(json_encode($r));
}
else
{
	echo'{"league_info":';
//$r[$num_rows]="success";
print(json_encode($r));

echo "}";
}

mysql_close($conn);
?>