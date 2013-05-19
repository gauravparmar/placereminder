package com.example.first;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MainActivity extends Activity {
	BMapManager mBMapMan = null;
	MapView mMapView = null;
	MKSearch mkSearch = null;
	ListView mSuggestionList = null;
	private EditText editText; //
	private Button button;
	private double aaa=0;
	private double bbb=0;
	//private clockSQLiteHelper SQLiteHelper;
	private final String database_name="clockstore.db";
	private boolean boolForL=true;
	public LocationClient mLocationClient = null;
	BDLocationListener myListener = new BDLocationListener(){

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if (location == null)
				return ;
			aaa=location.getLatitude();
			bbb=location.getLongitude();
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// TODO Auto-generated method stub
			
		}};//位置动作监听器  
		
    MyLocationOverlay mLocationOverlay;//地图覆盖物  
    
	
	Button VT;
	Button AT;
	Button ST,SPEAKING;
	TextView tempt;
	double nowx;
	double nowy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        

      //  SQLiteHelper =new clockSQLiteHelper(this,database_name,null,1);
       Intent intent= new Intent();
        intent.setClass(this, taskService.class);
        this.stopService(intent);
        this.startService(intent);
      //  intent.putExtra("isChanged", true);
       // intent.putExtra("isSQLsingle", false);    
       // SQLiteHelper.insert(20, 20, 150, 160, "hello 1");
        //Cursor cursor=SQLiteHelper.query(20, 20, 0.00001);
       // if(cursor!=null&&cursor.getCount()!=0)
        //  {
        //    	while(!cursor.isAfterLast())
         //   	{
          //  		Log.v("out", cursor.toString());
          //  		cursor.moveToNext();
         //   	}
            	
        //  }   
            
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setAddrType("all");//返回的定位结果包含地址信息
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);//设置发起定位请求的间隔时间为5000ms
        option.disableCache(true);//禁止启用缓存定位
        option.setPoiNumber(5);	//最多返回POI个数	
        option.setPoiDistance(1000); //poi查询距离		
        option.setPoiExtraInfo(false); //是否需要POI的电话和地址等详细信息		
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        if (mLocationClient != null && mLocationClient.isStarted())
        	mLocationClient.requestLocation();
        else 
        	Log.d("LocSDK3", "locClient is null or not started");
       
        
       // mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
      //  mLocationClient.registerLocationListener( myListener );    //注册监听函数
    
       // option.setPoiExtraInfo(true); //是否需要POI的电话和地址等详细信息		
      //  mLocationClient.setLocOption(option);
      //  mLocationClient.start(); 
       // mLocationClient.requestLocation();
      //  mLocationClient.getLastKnownLocation().get;
       // MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
       // LocationData locData = new LocationData();
        
        mBMapMan=new BMapManager(getApplication());
        mkSearch = new MKSearch();
        mBMapMan.init("FB53AA22A150D1AE3DB40F6AD5DCCE2B3A956852", null);  
        setContentView(R.layout.activity_main);
        mMapView=(MapView)findViewById(R.id.bmapsView);
        mMapView.setBuiltInZoomControls(true);
        mkSearch.init(mBMapMan, mkSearchListener);
        editText = (EditText) findViewById(R.id.searchText);
        button = (Button) findViewById(R.id.searchbutton);
        button.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v) {
        	    // TODO Auto-generated method stub
        	    String key = editText.getText().toString();
        	    // 如果关键字为空则不进入搜索
        	    if(key.equals(""))
        	    {
        	        Toast.makeText(MainActivity.this, "请输入搜索关键词！", Toast.LENGTH_SHORT).show();
        	    }
        	    else
        	    {
        	        // 这里Poi搜索以城市内Poi检索为例，开发者可根据自己的实际需求，灵活使用
        	    	//Toast.makeText(MainActivity.this, "搜索！", Toast.LENGTH_SHORT).show();
        	        mkSearch.poiSearchInCity("北京",key);
        	        
        	    }
        	    
        	}
        }); 
            
        mMapView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				boolForL=true;
				int old_zoom = mMapView.getZoomLevel();
				// TODO Auto-generated method stub
				int x=(int)arg1.getX();
				int y=(int)arg1.getY();
				GeoPoint geoPoint = mMapView.getProjection().fromPixels(x, y); 
				int xx = geoPoint.getLongitudeE6();  
		        int yy = geoPoint.getLatitudeE6();  
		        double nx=xx/(double)1000000;
		        double ny=yy/(double)1000000;
		        if ((int)nx!=(int)nowx || (int)ny!=(int)nowy)
		        {
		        	boolForL=false;
		        }
		        nowx=nx;
		        nowy=ny;
		        LocationData locData = new LocationData();
		        locData.direction = 2.0f;
		        locData.latitude=aaa;
		        locData.longitude =bbb;
		      //手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要
		      //使用百度经纬度坐标（bd09ll）
		      //  mLocationClient.requestLocation();
		       // MyLocationListener tempListen = new MyLocationListener();
		     // locData.latitude =tempListen.aaa;
		    //  locData.longitude = tempListen.bbb;
		      //locData.direction = 2.0f;
		      MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
		      tempt.setText(Double.toString(aaa)+","+Double.toString(bbb));
		      myLocationOverlay.setData(locData);
		      mMapView.getOverlays().add(myLocationOverlay);
		      mMapView.refresh();
		      
		        //Log.d("xxxxxxxxxxx", Integer.toString(xx));  
		       // Log.d("yyyyyyyyyyy", Integer.toString(yy)); 
				return false;
			}});
       
	       mMapView.setOnLongClickListener(new OnLongClickListener(){
	
				@Override
				public boolean onLongClick(View arg0) {
					// TODO Auto-generated method stub
					//设置长按事件
					if (boolForL)
					{
					Intent intent = new Intent();  
					intent.putExtra("keyA", nowx);
					intent.putExtra("keyB",nowy);
					intent.setClass(MainActivity.this, AddEvent.class);
					startActivity(intent);
					}
					return false;
				}});
      //设置启用内置的缩放控件
         VT=(Button) findViewById(R.id.view_task)	;
    	// AT=(Button) findViewById(R.id.add_tag);
    	 ST=(Button) findViewById(R.id.setting);
    	 SPEAKING=(Button) findViewById(R.id.speaking);
    	 tempt=(TextView) findViewById(R.id.textGeo);
      MapController mMapController=mMapView.getController();
      // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
      GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));
      //用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
      mMapController.setCenter(point);//设置地图中心点
      mMapController.setZoom(12);//设置地图zoom级别
      mLocationOverlay=new MyLocationOverlay(mMapView);  
      
      VT.setOnClickListener(new OnClickListener()
      {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();  
			intent.setClass(MainActivity.this, ViewTask.class);
			startActivity(intent);
			
		}});
      SPEAKING.setOnClickListener(new OnClickListener()
      {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();  
			intent.setClass(MainActivity.this, SpeakingRe.class);
			startActivity(intent);
			
		}});
    /*  AT.setOnClickListener(new OnClickListener()
      {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();  
			intent.setClass(MainActivity.this, addTags.class);
			startActivity(intent);
			
		}});*/
      ST.setOnClickListener(new OnClickListener()
      {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();  
			intent.putExtra("content", "你好");
			intent.setClass(MainActivity.this, Setting.class);
			startActivity(intent);
		}});
  
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    MKSearchListener mkSearchListener = new MKSearchListener() {

        @Override
        public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
            // TODO Auto-generated method stub
            if (arg1 != 0 || arg0 == null) {

                Toast.makeText(MainActivity.this, "抱歉，未找到结果", Toast.LENGTH_LONG).show(); 

                return;

            }

            int nSize = arg0.getSuggestionNum();

            String[] mStrSuggestions = new String[nSize];

            for (int i = 0; i <nSize; i++){

                mStrSuggestions[i] = arg0.getSuggestion(i).city + arg0.getSuggestion(i).key;

            }

            ArrayAdapter<String> suggestionString = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,mStrSuggestions);
           // mSuggestionList = (ListView) findViewById(R.id.suggestlist);
        
            //actionAlertDialog(mSuggestionList);

           // mSuggestionList.setAdapter(suggestionString);

        }

        @Override
        public void onGetPoiResult(MKPoiResult res, int type, int error) { 

        	if ( error == MKEvent.ERROR_RESULT_NOT_FOUND){

        		Toast.makeText(MainActivity.this, "抱歉，未找到结果",Toast.LENGTH_LONG).show();

        		return ;

        	}

            PoiOverlay poioverlay = new PoiOverlay(MainActivity.this, mMapView);

            poioverlay.setData(res.getAllPoi());                

            mMapView.getOverlays().add(poioverlay);

            mMapView.getController().animateTo(res.getPoi(0).pt);   //手动设定地图的中心点
            mMapView.invalidate();  //手动刷新地图  
            mMapView.refresh();
   
          	
    }
        @Override
        public void onGetPoiDetailSearchResult(int arg0, int arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
            // TODO Auto-generated method stub

        }
    };  
    @Override
    protected void onDestroy(){
            mMapView.destroy();
            if(mBMapMan!=null){
                    mBMapMan.destroy();
                    mBMapMan=null;
            }
            super.onDestroy();
    }
    @Override
    protected void onPause(){
            mMapView.onPause();
            if(mBMapMan!=null){
                    mBMapMan.stop();
            }
            super.onPause();
    }
    @Override
    protected void onResume(){
            mMapView.onResume();
            if(mBMapMan!=null){
                    mBMapMan.start();
            }
            super.onResume();
    }
    class OverlayTest extends ItemizedOverlay<OverlayItem> {
        //用MapView构造ItemizedOverlay
        public OverlayTest(Drawable marker,MapView mapView){
                super(marker,mapView);
        }
        protected boolean onTap(int index) {
            //在此处理item点击事件
            System.out.println("item onTap: "+index);
            return true;
        }
            public boolean onTap(GeoPoint pt, MapView mapView){
                    //在此处理MapView的点击事件，当返回 true时
                    super.onTap(pt,mapView);
                    return false;
            }
            // 自2.1.1 开始，使用 add/remove 管理overlay , 无需重写以下接口
            /*
            @Override
            protected OverlayItem createItem(int i) {
                    return mGeoList.get(i);
            }
           
            @Override
            public int size() {
                    return mGeoList.size();
            }
            */
    }  
    
}
