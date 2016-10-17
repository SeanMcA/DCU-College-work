<?php

$servername = "localhost";
$username = "zelusitc_sean";
$password = "guiness";
$database = "zelusitc_temp";


$latitude = $_GET['latitude'];
$longitude = $_GET['longitude'];

//$latitude = 53.31822909;
//$longitude = -6.25564142;



$conn = mysql_connect($servername,$username,$password) or trigger_error(mysql_error(),E_USER_ERROR);
mysql_select_db($database,$conn);
$result=mysql_query("SELECT name, facility_name, course_id, pins,
    SQRT(POW((69.1 * (latitude - '$latitude')) , 2 ) + 
    POW((53 * (longitude - '$longitude')), 2)) AS distance 
FROM course
ORDER BY distance ASC 
LIMIT 10");

$num_rows = mysql_num_rows($result);
while($row = mysql_fetch_array($result))
{

$r[]=$row;
$check=$row['facility_name'];
}

if($check==NULL)
{
$r[$num_rows]='Record is not available';
print(json_encode($r));
}
else
{
	echo'{"course_info":';
//$r[$num_rows]="success";
print(json_encode($r));

echo "}";
}

mysql_close($conn);
?>