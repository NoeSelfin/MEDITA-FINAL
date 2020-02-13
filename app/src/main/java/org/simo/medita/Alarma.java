package org.simo.medita;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class Alarma extends BroadcastReceiver 
{    
    @Override
    public void onReceive(Context context, Intent intent) 
    {   
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        Log.i("medita_alarma","inside");

        Log.i("medita_alarma",intent.getStringExtra("dias"));
        if (intent.hasExtra("dias")){
        	
        	  JSONArray dias;
			try {
				dias = new JSONArray(intent.getStringExtra("dias"));
				Calendar now = Calendar.getInstance();
	        	int dia = now.get(Calendar.DAY_OF_WEEK);
	        	boolean isDay = false;
	        	  
	        	  for (int i=0; i < dias.length();i++){
	        		  if ((dias.getString(i).compareTo("Lunes") == 0) && (dia == 2))
	        			  isDay = true;
	        		  if ((dias.getString(i).compareTo("Martes") == 0) && (dia == 3))
	        			  isDay = true;
	        		  if ((dias.getString(i).compareTo("Miércoles") == 0) && (dia == 4))
	        			  isDay = true;
	        		  if ((dias.getString(i).compareTo("Jueves") == 0) && (dia == 5))
	        			  isDay = true;
	        		  if ((dias.getString(i).compareTo("Viernes") == 0) && (dia == 6))
	        			  isDay = true;
	        		  if ((dias.getString(i).compareTo("Sábado") == 0) && (dia == 7))
	        			  isDay = true;
	        		  if ((dias.getString(i).compareTo("Domingo") == 0) && (dia == 1))
	        			  isDay = true;      		  
	        		  
	        	  }
	        	  
	        	  if (isDay)
	        	        new Notificaciones().setNotificaciones(context);
			} catch (JSONException e) {
			}        	 

        }      

        wl.release();
    }
    
    public void SetAlarm(Context context, String time, int num_alarma, JSONArray dias)
    {
    	Log.i("medita_alarma","Alarma "+String.valueOf(num_alarma) + " SETEADA a las "+time);
    	int hora = Integer.valueOf(time.split(":")[0]);
    	int minutos = Integer.valueOf(time.split(":")[1]);
    	
    	Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.HOUR_OF_DAY, hora);
		cal.set(Calendar.MINUTE, minutos);
		Log.i("medita", cal.toString());
		
		AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, Alarma.class);
		i.putExtra("dias", dias.toString());
		PendingIntent pi = PendingIntent.getBroadcast(context, num_alarma, i, 0);
		am.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000 * 60 * 60 * 24, pi); // Millisec * Segundos * Minutos * Horas    	
    }

    public void CancelAlarm(Context context, int num_alarma)
    {
    	Log.i("medita_alarma","Alarma "+String.valueOf(num_alarma) + " CANCELADA");
        Intent intent = new Intent(context, Alarma.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, num_alarma, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
