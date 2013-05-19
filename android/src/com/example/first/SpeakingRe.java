package com.example.first;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
public class SpeakingRe extends Activity implements OnClickListener{
	
	 private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	 
	 private Boolean FLAG_RESULT;
	 private String STR_PLACE;
	 public String STR_REMINDER;
		 
	 private String STR_MATCH;
	 //middlewares, only used during processes
	 public double lat;
	 public double lon;
	 
	 
	 private TextView rResult;
	 
	 Runnable processRec = new Runnable()
	 {
		 @Override
		 public void run()
		 {
			 Looper.prepare();
			 try {
				patternRecognize(STR_MATCH);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(SpeakingRe.this,"ClientProtocolException！请再试一次！",Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(SpeakingRe.this,"IOException！请再试一次！",Toast.LENGTH_LONG).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(SpeakingRe.this,"JSONException！请再试一次！",Toast.LENGTH_LONG).show();
			} catch(Exception e)
			{
				e.printStackTrace();
				Toast.makeText(SpeakingRe.this,"未知错误！请再试一次！",Toast.LENGTH_LONG).show();
			}
			 Looper.loop();
		 }
	 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speaking_re);
		
		 // Get display items for later interaction
        Button speakButton = (Button) findViewById(R.id.btn_speak);
        rResult = (TextView) findViewById(R.id.tv);
        
        // Check to see if a recognition activity is present
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            speakButton.setOnClickListener((OnClickListener) this);
        } else {
            speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }
		
	}
	
	/**
     * Handle the click on the start recognition button.
     */
    public void onClick(View v) {
        if (v.getId() == R.id.btn_speak) {
            startVoiceRecognitionActivity();
        }
    }
	 /**
     * Fire an intent to start the speech recognition activity.
     */
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "请说普通话");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    /**
     * Handle the results from the recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
//            mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
//                    matches));
            rResult.setText(matches.get(0));
            STR_MATCH=matches.get(0);
            new Thread(processRec,"abc").start();
//            if(!FLAG_RESULT)
//            	Toast.makeText(this,"模式识别失败！请再试一次！",Toast.LENGTH_LONG).show();
//            else
//            {
//            	//upload to server
//            	
//            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    
    protected void patternRecognize(String code) throws ClientProtocolException, IOException, JSONException
    {
    	//place
    	int place_s_1=code.indexOf("经过");
    	int place_s_2=code.indexOf("在");
    	int place_e_1=code.indexOf("的时候");
    	int place_e_2=code.indexOf("时");
    	//reminder
    	int reminder_s_1=code.indexOf("提醒我");
    	int reminder_s_2=code.indexOf("提醒");
    	//to comfirm the pattern position
    	int place_s=0;
    	int place_s_n=0;
    	if(place_s_1!=-1){place_s=place_s_1;place_s_n=2;}
    	else {place_s=place_s_2;place_s_n=1;} //no matter what
    	
    	int place_e=0;
    	int place_e_n=0;
    	if(place_e_1!=-1){place_e=place_e_1;place_e_n=3;}
    	else {place_e=place_e_2;place_e_n=1;} //no matter what
    	
    	int reminder_full=0;
    	int reminder_full_n=0;
    	if(reminder_s_1!=-1){reminder_full=reminder_s_1;reminder_full_n=3;}
    	else {reminder_full=reminder_s_2;reminder_full_n=2;} //no matter what
    	
    	boolean isPlace=true;
    	boolean isReminder=true;
    	//judge result
    	if(place_s==-1&&place_e==-1)isPlace=false;
    	if(reminder_full==-1)isReminder=false;
    	
    	//get things out
    	Map.Entry<Boolean,Map.Entry<String,String>> thair;
    	if(!isPlace||!isReminder)
    	{
//    		System.out.println("DEBUG:Recog False");
    		Log.v("debug","Recog False");
    		FLAG_RESULT=false;
    	}
    	else
    	{
    		FLAG_RESULT=true;
    		if(place_s>reminder_full)
    		{
    			STR_PLACE=code.substring(place_s+place_s_n, (place_e==-1)?code.length():place_e);
    			STR_REMINDER=code.substring(reminder_full+reminder_full_n, place_s);
    		}
    		else
    		{
    			STR_PLACE=code.substring(place_s+place_s_n, (place_e==-1)?reminder_full:place_e);
    			STR_REMINDER=code.substring(reminder_full+reminder_full_n, code.length());
    		}
				getLocation();
    		Log.v("debug","PLACE:"+STR_PLACE);
    		Log.v("debug","REMINDER:"+STR_REMINDER);
    	}
    }
    
    protected void getLocation() throws ClientProtocolException, IOException, JSONException
    {
    	//default keyword is STR_PLACE
    	//String httpUrl = "http://192.168.230.1/y/searchPlaces/?kw="+URLEncoder.encode(STR_PLACE,"UTF-8");
    	String httpUrl = "http://placereminder.sinaapp.com/searchPlaces/?kw="+URLEncoder.encode(STR_PLACE,"UTF-8");
    	HttpGet httpRequest = new HttpGet(httpUrl);
    	HttpClient httpclient = new DefaultHttpClient();
    	HttpResponse httpResponse = httpclient.execute(httpRequest);
    	if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
    	{
    		String strResult = EntityUtils.toString(httpResponse.getEntity());
    		JSONObject jsonobj = new JSONObject(strResult);
    		int r_result = jsonobj.getInt("status");
    		if(r_result!=0)
    			Toast.makeText(this,"获取坐标时服务器返回错误，请检查地点是否存在或联系管理员！",Toast.LENGTH_LONG).show();
    		else
    		{
    			lat=jsonobj.getJSONObject("data").getDouble("offsetlat");
    			lon=jsonobj.getJSONObject("data").getDouble("offsetlon");
    			clockSQLiteHelper SQLiteHelper =new clockSQLiteHelper(SpeakingRe.this,"clockstore.db",null,1);
    			SQLiteHelper.insert(lat, lon, 150, 160, STR_REMINDER);
    			SQLiteHelper.close();
    			Log.v("SPEAKING_ADD_SOMETHING",STR_REMINDER);
//    			uploadPair();
    		}
    	}
    	else
    	{
    		Toast.makeText(this, "网络请求出错，请检查你的网络！", Toast.LENGTH_LONG).show();
    	}
    	finish();
    }
    
    protected void uploadPair() throws ClientProtocolException, IOException, JSONException
    {
    	//upload default pairs STR_PLACE & STR_REMINDER
    	String httpUrl = "http://192.168.230.1/y/addNotes/?lat="+Double.toString(lat)+"&lon="+Double.toString(lon)+"&con="+URLEncoder.encode(STR_REMINDER,"UTF-8");
    	HttpGet httpRequest = new HttpGet(httpUrl);
    	HttpClient httpclient = new DefaultHttpClient();
    	HttpResponse httpResponse = httpclient.execute(httpRequest);
    	if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
    	{
    		String strResult = EntityUtils.toString(httpResponse.getEntity());
    		JSONObject jsonobj = new JSONObject(strResult);
    		int r_result = jsonobj.getInt("status");
    		if(r_result!=0)
    			Toast.makeText(this,"上传标签至服务器时返回错误，请检查网络或联系管理员！",Toast.LENGTH_LONG).show();
    		else
    		{
    			Toast.makeText(this,"成功上传添加你的标签！",Toast.LENGTH_LONG).show();
    		}
    	}
    	else
    	{
    		Toast.makeText(this, "网络请求出错，请检查你的网络！", Toast.LENGTH_LONG).show();
    	}
    }

}
