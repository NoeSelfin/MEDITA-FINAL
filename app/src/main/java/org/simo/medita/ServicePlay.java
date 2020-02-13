package org.simo.medita;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class ServicePlay extends Service {
    // Binder given to clients
    protected static MediaPlayer mp;
    protected static Context ctx;
    protected static Activity act;
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	 //Toast.makeText(this, String.valueOf(startId), Toast.LENGTH_SHORT).show();
    	
        return START_STICKY;
    }

    

    public static void setMediaplayer(MediaPlayer mp, Context ctx){		
    	ServicePlay.ctx = ctx;
        ServicePlay.mp = mp;
	  }
	  
	  public static MediaPlayer getMediaplayer(){
		  return mp;
	  }
	  public static void setActivity(Activity act){		
	    	ServicePlay.act = act;
		  }
		  
		  public static Activity getActivity(){
			  return act;
		  }




	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
	
	
	
	
	
	

	

		



