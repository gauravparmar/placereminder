package com.example.first;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class AddEvent  extends Activity{
	private SeekBar seekBar1,seekBar2;
	private TextView textView1,textView2;
	private EditText editText;
	private Button OKBT,CANBT;
 private  String NumTime(int a)
  {
	int t=(int)(a/60);
	int b=(int)(a%60);
	String aa=String.valueOf(t);
	String bb=String.valueOf(b);
	return aa+":"+bb;
	  
  }
 private double aaa=0;
 private double bbb=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_events);
Bundle budle=this.getIntent().getExtras();
 aaa=budle.getDouble("keyA");
 bbb=budle.getDouble("keyB");
		seekBar1=(SeekBar) findViewById(R.id.seekBar1);
		seekBar2=(SeekBar) findViewById(R.id.seekBar2);
		textView1=(TextView) findViewById(R.id.textGeo);
		textView2=(TextView) findViewById(R.id.textView22);
		editText=(EditText) findViewById(R.id.editTextaddEvent);
		OKBT=(Button) findViewById(R.id.OKBt);
		CANBT=(Button) findViewById(R.id.CANBT);
		OKBT.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				clockSQLiteHelper SQLiteHelper =new clockSQLiteHelper(AddEvent.this,"clockstore.db",null,1);
				SQLiteHelper.insert(aaa, bbb, 150, 160, editText.getText().toString());
				SQLiteHelper.close();
				finish();
			}});
		CANBT.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}});
		seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				textView1.setText(NumTime(arg1));
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}});
		seekBar2.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				textView2.setText(NumTime(arg1));
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}});
	}
}
