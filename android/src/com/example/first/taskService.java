package com.example.first;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationOverlay;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class taskService extends Service {

	private Handler handler;
	private clockSQLiteHelper SQLiteHelper;
	private boolean isChanged=true;
	private boolean isSQLsingle=true;
	private final  String database_name="clockstore.db";
	public LocationClient mLocationClient = null;
	private double aaa;
	private double bbb;
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
    
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		try{
		SQLiteHelper =new clockSQLiteHelper(this,database_name,null,1);
		}catch(Exception e)
		{
		   Log.v("error","Service "+e.toString());
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		//if(isSQLsingle)
			try {
				SQLiteHelper.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		super.onDestroy();
	}
  public void refresh()
  {
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
      if (!mLocationClient.isStarted())
    	  mLocationClient.start();
      
      if (mLocationClient != null && mLocationClient.isStarted())
      	mLocationClient.requestLocation();
      else 
      	Log.d("LocSDK3", "locClient is null or not started");	  
  }
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//Bundle bundle= intent.getExtras();
		
		
	        
		
		//isChanged= bundle.getBoolean("isChanged",true);
		//isSQLsingle=bundle.getBoolean("isSQLsingle", true);
	//	SQLiteHelper.insert(40, 40, 450, 560, "hello 2");
		handler=new Handler();
		Runnable r=new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String sql;
				double xc=0,yc=0;
				//获取当前经纬度
				refresh();
				xc=aaa;yc=bbb;
				//Log.d("CIRING", Double.toString(aaa)+"  "+Double.toString(bbb));
				Cursor temp;
				temp=SQLiteHelper.query(xc,yc,0.01);
				if(temp.getCount()!=0)
				{
					temp.moveToFirst();
					while(!temp.isAfterLast())
					{
						try{
							Intent intent =new Intent();
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setClass(taskService.this, ClockRings.class);
							intent.putExtra("content", temp.getString(temp.getColumnIndex("content")));
							startActivity(intent);
							//Log.v("CIRING", "Colock is ringing  "+temp.getString(temp.getColumnIndex("content")));
						     SQLiteHelper.delete(temp.getInt(temp.getColumnIndex("id")));
						
						
						
						
						}
						catch (Exception e)
						{
							Log.v("EXCEPTION",e.toString());
						}
						//添加查询语句方式 和获取当前坐标
						//Toast.makeText(taskService.this, temp.getString(temp.getColumnIndex("content")), Toast.LENGTH_LONG);
						temp.moveToNext();
					
					}
				temp.close();
				}
				temp.close();
				handler.postDelayed(this, 20000);	
				
			}
		};
		handler.postDelayed(r, 10000);	
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}
}

