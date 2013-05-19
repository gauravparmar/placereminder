<?php
	// function getOffsetString($lat,$lon)
	// {
	// 	$STR_offset = file_get_contents("http://www.mapdigit.com/guidebeemap/offsetinchina.php?lng=$lon&lat=$lat");
	// 	return $STR_offset;
	// }
	// function lonToPixel($lng, $zoom)
	// {
	// 	return ($lng+180)*(256<<$zoom)/360;
	// }
	// function pixelToLon($pixelX, $zoom)
	// {
	// 	return $pixelX*360/(256<<$zoom)-180;
	// }
	// function latToPixel($lat, $zoom)
	// {
	// 	$siny = sin($lat*pi()/180);
	// 	$y=log((1+$siny)/(1-$siny));
	// 	return (128<<$zoom)*(1-$y/(2*pi()));
	// }
	// function pixelToLat($pixelY,$zoom)
	// {
	// 	$y=2*pi()*(1-$pixelY/(128<<$zoom));
	// 	$z=exp($y);
	// 	$siny=($z-1)/($z+1);
	// 	return asin($siny)*180/pi();
	// }
	// function adjustLatitude($lat,$offset)
	// {
	// 	$index = stripos($offset,",");
	// 	if($index>0)
	// 	{
	// 		$latPixel=latToPixel($lat,18);
	// 		$OffsetY=trim(substr($offset,$index+1));
	// 		$adjustLatPixel=$latPixel+(double)$OffsetY;
	// 		$adjustLat=pixelToLat($adjustLatPixel,18);
	// 		return (int)($adjustLat*1000000);
	// 		// return $adjustLat;
	// 	}
	// 	return (int)(($lat-0.0025)*1000000);
	// 	// return $lat-0.0025;
	// }
	// function adjustLongitude($lng,$offset)
	// {
	// 	$index = stripos($offset,",");
	// 	if($index>0)
	// 	{
	// 		$lngPixel=lonToPixel($lng,18);
	// 		$OffsetX=trim(substr($offset,0,$index));
	// 		$adjustLngPixel=$lngPixel+(double)$OffsetX;
	// 		$adjustLng=pixelToLon($adjustLngPixel,18);
	// 		return (int)($adjustLng*1000000);
	// 		// return $adjustLng;
	// 	}
	// 	return (int)(($lng+0.0045)*1000000);
	// 	// return ($lng+0.0045);
	// }
	function getOffsetObj($lat,$lon)
	{
		$context = stream_context_create(array('http'=>array('timeout'=>0.5)));
		$STR_offset = @file_get_contents("http://lbs.juhe.cn/api/baidu.php?lngx=$lon&lngy=$lat",0,$context);
		if(!$STR_offset)return null;
		$OBJ_offset = json_decode($STR_offset);
		return $OBJ_offset;
	}

	//pre-defined
	$NOTSET_RESULT["status"]=2;
	$NOTSET_RESULT["description"]="Missing Parameters";
	$NOTSET_RESULT["data"]=null;

	//start
	header('Content-Type:application/json;charset=utf-8');
	$kw;
	if(is_array($_GET)&&count($_GET)>0)
	{
		if(isset($_GET["kw"])) $kw = $_GET["kw"];
		else die(json_encode($NOTSET_RESULT));
	}
	else
	{
		die(json_encode($NOTSET_RESULT));
	}

	// $kw = $_GET["kw"];
	$yql = urlencode("select * from geo.placefinder where text='$kw'");
	$reqstr = "http://query.yahooapis.com/v1/public/yql?q=$yql&format=json";
	$result = file_get_contents($reqstr);
	$deres = json_decode($result);

	//fix China Map Statistics
	$ori_lat = $deres->query->results->Result->latitude;
	$ori_lon = $deres->query->results->Result->longitude;
	// $fix_str = getOffsetString($ori_lat,$ori_lon);
	$fix_obj = getOffsetObj($ori_lat,$ori_lon);
	if($fix_obj==null)
	{
		//Failed to Retrieve
		//Use Experienced Data
		$standard_result["status"]=0;
		$standard_result["description"]="GOOD";
		$standard_result["data"]["longitude"]=$ori_lon;
		$standard_result["data"]["latitude"]=$ori_lat;
		$standard_result["data"]["offsetlon"]=$ori_lon+0.0045;
		$standard_result["data"]["offsetlat"]=$ori_lat-0.0025;
		die(json_encode($standard_result));
	}
	$fix_lat = $fix_obj->row->off_lat;
	$fix_lon = $fix_obj->row->off_lon;

	//replace offsetlat & offsetlon
	$deres->query->results->Result->offsetlat=$fix_lat;
	$deres->query->results->Result->offsetlon=$fix_lon;
	$result =  json_encode($deres);

	$standard_result["status"]=0;
	$standard_result["description"]="OK";
	$standard_result["data"]["longitude"]=$ori_lon;
	$standard_result["data"]["latitude"]=$ori_lat;
	$standard_result["data"]["offsetlon"]=$fix_lon;
	$standard_result["data"]["offsetlat"]=$fix_lat;
	echo json_encode($standard_result);

	// echo $ori_lat;
	// echo "<br/>";
	// echo $ori_lon;
	// echo "<br/>";
	// echo $fix_lat;
	// echo "<br/>";
	// echo $fix_lon;
	// echo "<br/>";
	// echo $result;
