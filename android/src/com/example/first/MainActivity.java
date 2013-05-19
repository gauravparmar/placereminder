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
			
		}};//λ�ö���������  
		
    MyLocationOverlay mLocationOverlay;//��ͼ������  
    
	
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
            
        mLocationClient = new LocationClient(getApplicationContext());     //����LocationClient��
        mLocationClient.registerLocationListener( myListener );    //ע���������
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setAddrType("all");//���صĶ�λ���������ַ��Ϣ
        option.setCoorType("bd09ll");//���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
        option.setScanSpan(5000);//���÷���λ����ļ��ʱ��Ϊ5000ms
        option.disableCache(true);//��ֹ���û��涨λ
        option.setPoiNumber(5);	//��෵��POI����	
        option.setPoiDistance(1000); //poi��ѯ����		
        option.setPoiExtraInfo(false); //�Ƿ���ҪPOI�ĵ绰�͵�ַ����ϸ��Ϣ		
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        if (mLocationClient != null && mLocationClient.isStarted())
        	mLocationClient.requestLocation();
        else 
        	Log.d("LocSDK3", "locClient is null or not started");
       
        
       // mLocationClient = new LocationClient(getApplicationContext());     //����LocationClient��
      //  mLocationClient.registerLocationListener( myListener );    //ע���������
    
       // option.setPoiExtraInfo(true); //�Ƿ���ҪPOI�ĵ绰�͵�ַ����ϸ��Ϣ		
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
        	    // ����ؼ���Ϊ���򲻽�������
        	    if(key.equals(""))
        	    {
        	        Toast.makeText(MainActivity.this, "�����������ؼ��ʣ�", Toast.LENGTH_SHORT).show();
        	    }
        	    else
        	    {
        	        // ����Poi�����Գ�����Poi����Ϊ���������߿ɸ����Լ���ʵ���������ʹ��
        	    	//Toast.makeText(MainActivity.this, "������", Toast.LENGTH_SHORT).show();
        	        mkSearch.poiSearchInCity("����",key);
        	        
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
		      //�ֶ���λ��Դ��Ϊ�찲�ţ���ʵ��Ӧ���У���ʹ�ðٶȶ�λSDK��ȡλ����Ϣ��Ҫ��SDK����ʾһ��λ�ã���Ҫ
		      //ʹ�ðٶȾ�γ�����꣨bd09ll��
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
					//���ó����¼�
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
      //�����������õ����ſؼ�
         VT=(Button) findViewById(R.id.view_task)	;
    	// AT=(Button) findViewById(R.id.add_tag);
    	 ST=(Button) findViewById(R.id.setting);
    	 SPEAKING=(Button) findViewById(R.id.speaking);
    	 tempt=(TextView) findViewById(R.id.textGeo);
      MapController mMapController=mMapView.getController();
      // �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
      GeoPoint point =new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));
      //�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
      mMapController.setCenter(point);//���õ�ͼ���ĵ�
      mMapController.setZoom(12);//���õ�ͼzoom����
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
			intent.putExtra("content", "���");
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

                Toast.makeText(MainActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show(); 

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

        		Toast.makeText(MainActivity.this, "��Ǹ��δ�ҵ����",Toast.LENGTH_LONG).show();

        		return ;

        	}

            PoiOverlay poioverlay = new PoiOverlay(MainActivity.this, mMapView);

            poioverlay.setData(res.getAllPoi());                

            mMapView.getOverlays().add(poioverlay);

            mMapView.getController().animateTo(res.getPoi(0).pt);   //�ֶ��趨��ͼ�����ĵ�
            mMapView.invalidate();  //�ֶ�ˢ�µ�ͼ  
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
        //��MapView����ItemizedOverlay
        public OverlayTest(Drawable marker,MapView mapView){
                super(marker,mapView);
        }
        protected boolean onTap(int index) {
            //�ڴ˴���item����¼�
            System.out.println("item onTap: "+index);
            return true;
        }
            public boolean onTap(GeoPoint pt, MapView mapView){
                    //�ڴ˴���MapView�ĵ���¼��������� trueʱ
                    super.onTap(pt,mapView);
                    return false;
            }
            // ��2.1.1 ��ʼ��ʹ�� add/remove ����overlay , ������д���½ӿ�
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
