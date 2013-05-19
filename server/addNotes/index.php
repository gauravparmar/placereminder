<?php
	header('Content-Type:application/json;charset=utf-8');

	//pre-defined
	$NOTSET_RESULT["status"]=2;
	$NOTSET_RESULT["description"]="Missing Parameters";
	$NOTSET_RESULT["data"]=null;

	$lat;
	$lon;
	$content;
	$usr=0; //optional, 0 for default
	$tfrom=0;
	$tto=0;

	if(is_array($_GET)&&count($_GET)>0)
	{
		if(isset($_GET["lat"])) $lat = $_GET["lat"];
		else die(json_encode($NOTSET_RESULT));

		if(isset($_GET["lon"])) $lon = $_GET["lon"];
		else die(json_encode($NOTSET_RESULT));

		if(isset($_GET["con"])) $content = $_GET["con"];
		else die(json_encode($NOTSET_RESULT));

		//not necessary "usr"
		if(isset($_GET["usr"])) $usr = $_GET["usr"];

		if(isset($_GET["tfrom"])) $tfrom = $_GET["tfrom"];
		if(isset($_GET["tto"])) $tto = $_GET["tto"];
	}
	else
	{
		die(json_encode($NOTSET_RESULT));
	}


	$con = @mysql_connect(SAE_MYSQL_HOST_M.":".SAE_MYSQL_PORT,SAE_MYSQL_USER,SAE_MYSQL_PASS);
	if(!$con)
	{
		$standard_result["status"]=1;
		$standard_result["description"]="Database Not Connected";
		$standard_result["data"]=null;
		die(json_encode($standard_result));
	}
	mysql_select_db(SAE_MYSQL_DB,$con);
	mysql_query("SET NAMES UTF8");
	mysql_query("INSERT INTO notes (latitude,longitude,content,tfrom,tto,user) VALUES ('$lat','$lon','$content','$tfrom','$tto','$usr')");
	mysql_close($con);
	
	$standard_result["status"]=0;
	$standard_result["description"]="OK";
	$standard_result["data"]=null;
	echo json_encode($standard_result);