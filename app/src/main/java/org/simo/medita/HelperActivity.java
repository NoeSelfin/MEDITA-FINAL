package org.simo.medita;


import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;

public class HelperActivity extends Activity {

private HelperActivity ctx;
ServicePlay myService;
MediaPlayer mp;



@Override
protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    //Toast.makeText(this, "helper", Toast.LENGTH_SHORT).show();
    ctx = this;
    
    Intent intent = new Intent(this, ServicePlay.class);
	startService(intent);
    mp = ServicePlay.getMediaplayer();
    
    mp.pause();
    mp.stop();
    
    String action = (String) getIntent().getExtras().get("DO");
    if (action.equals("left")) {
    } else if (action.equals("play")) {
    } else if (action.equals("right")) {
    	if (mp.isPlaying()){
			mp.pause();
			//play.setBackgroundResource(R.drawable.play_button);
		}
		else{
			mp.start();
			//play.setBackgroundResource(R.drawable.pause_button);
		}
    	
    }
    else if (action.equals("close")) {
    	mp.stop();
    	stopService(intent);
    	NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	mNotificationManager.cancel(548853);
    	
    	ServicePlay.getActivity().finish();
    }


    if (!action.equals("reboot"))
        finish();
    
}

@Override
protected void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();
}
}
