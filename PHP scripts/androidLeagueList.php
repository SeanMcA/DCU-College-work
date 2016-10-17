<?php

$servername = "localhost";
$username = "zelusitc_sean";
$password = "guiness";
$database = "zelusitc_temp";

//$loginID = $_POST['loginID'];
//$loginID = 1;
$loginID = $_GET["loginid"];


$conn = mysql_connect($servername,$username,$password) or trigger_error(mysql_error(),E_USER_ERROR);
mysql_select_db($database,$conn);
$result=mysql_query("SELECT l.league_id, position, prev_position, league_name, start_date, finish_date, type
				FROM league_members AS lm
				JOIN league AS l
				ON lm.league_id = l.league_id
				WHERE user_id = '$loginID'");

$num_rows = mysql_num_rows($result);
while($row = mysql_fetch_array($result))
{

$r[]=$row;
$check=$row['league_id'];
}

if($check==NULL)
{
$r[$num_rows]='Record is not available';
print(json_encode($r));
//echo'{"league_info":[{"league_id":"0","league_name":"You are not in any leagues","type":""}]}';

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