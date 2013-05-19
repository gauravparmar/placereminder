package com.example.first;


import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
public class ClockRings extends Activity {
  private MediaPlayer mMediaPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clock);
		TextView notice= (TextView) this.findViewById(R.id.notice);
		notice.setText(this.getIntent().getExtras().getCharSequence("content"));
		Button btnOK= (Button) this.findViewById(R.id.clock_okbtn);
		btnOK.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				mMediaPlayer.stop();
				finish();
			}
			
		});
		
		play();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mMediaPlayer!=null)
		{
			mMediaPlayer.release();
		}
	}
	
	private OnPreparedListener preListener= new OnPreparedListener()
	{

		@Override
		public void onPrepared(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			mMediaPlayer.start();
		}
	};

	private void play()
	{
		try{
			if(mMediaPlayer==null||!mMediaPlayer.isPlaying())
			{
				mMediaPlayer=MediaPlayer.create(this, R.raw.clock_music01);
				mMediaPlayer.setOnPreparedListener(preListener);
			}else
			{
				mMediaPlayer.reset();
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}


}
