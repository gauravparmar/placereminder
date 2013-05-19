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
			
		}};//λ�ö���������  
		
    MyLocationOverlay mLocationOverlay;//��ͼ������  
    
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
				//��ȡ��ǰ��γ��
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
						//��Ӳ�ѯ��䷽ʽ �ͻ�ȡ��ǰ����
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

