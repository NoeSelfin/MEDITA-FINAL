package org.simo.medita;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AlarmaServicio extends Service
{
    Alarma alarm = new Alarma();
    public void onCreate()
    {
        super.onCreate();       
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) 
    {
        alarm.SetAlarm(this,null,0,null);
        return START_STICKY;
    }

   @Override        
   public void onStart(Intent intent, int startId)
    {
        alarm.SetAlarm(this,null,0,null);
    }

    @Override
    public IBinder onBind(Intent intent) 
    {
        return null;
    }
}
