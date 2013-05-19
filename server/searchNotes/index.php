<?php
	header('Content-Type:application/json;charset=utf-8');

	//pre-defined
	$NOTSET_RESULT["status"]=2;
	$NOTSET_RESULT["description"]="Missing Parameters";
	$NOTSET_RESULT["data"]=null;

	$lat;
	$lon;
	$rho=0.001; //optional, 0.1 for default

	if(is_array($_GET)&&count($_GET)>0)
	{
		if(isset($_GET["lat"])) $lat = $_GET["lat"];
		else die(json_encode($NOTSET_RESULT));

		if(isset($_GET["lon"])) $lon = $_GET["lon"];
		else die(json_encode($NOTSET_RESULT));

		//not necessary "rho"
		if(isset($_GET["rho"])) $rho = $_GET["rho"];
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
	$res = mysql_query("SELECT * FROM notes WHERE latitude>=$lat-$rho AND latitude<=$lat+$rho AND longitude>=$lon-$rho AND longitude<=$lon+$rho AND ((UNIX_TIMESTAMP()>=UNIX_TIMESTAMP(tfrom) AND UNIX_TIMESTAMP()<=UNIX_TIMESTAMP(tto)) OR UNIX_TIMESTAMP(tto)=0)");
	$n=0;
	$standard_result["status"]=0;
	$standard_result["description"]="OK";
	$standard_result["data"]=null;
	while($row = mysql_fetch_array($res))
	{
		$standard_result["data"][$n]["nid"]=$row["nid"];
		$standard_result["data"][$n]["latitude"]=$row["latitude"];
		$standard_result["data"][$n]["longitude"]=$row["longitude"];
		$standard_result["data"][$n]["content"]=$row["content"];
		$standard_result["data"][$n]["tfrom"]=$row["tfrom"];
		$standard_result["data"][$n]["tto"]=$row["tto"];
		$standard_result["data"][$n]["user"]=$row["user"];
		$standard_result["data"][$n]["TS"]=$row["TS"];
		$n++;
	}
	mysql_close($con);
	
	// $standard_result["data"]=$data;
	echo json_encode($standard_result);