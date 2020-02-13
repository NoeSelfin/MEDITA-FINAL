package org.simo.medita;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class AutoStart extends BroadcastReceiver
{   
	SharedPreferences prefs;
    Alarma alarm = new Alarma();
    @Override
    public void onReceive(Context context, Intent intent)
    {   
		
	
    	
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
//        	prefs = context.getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
			prefs = context.getSharedPreferences(context.getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
    		
    		if (prefs.contains("alarma1_active")){
    			if (prefs.getBoolean("alarma1_active", false)){
    				String hora = prefs.getString("alarma1_hora","");
    				String dias = prefs.getString("alarma1_dias","");
    				
    				try {
						JSONArray dias_ja = new JSONArray (dias);
	    			    alarm.SetAlarm(context,hora,0,dias_ja);

					} catch (JSONException e) {
					}
    				
    			}			
    		}
    		if (prefs.contains("alarma2_active")){
    			if (prefs.getBoolean("alarma2_active", false)){
    				String hora = prefs.getString("alarma1_hora","");
    				String dias = prefs.getString("alarma1_dias","");
    				try {
						JSONArray dias_ja = new JSONArray (dias);
	    			    alarm.SetAlarm(context,hora,1,dias_ja);

					} catch (JSONException e) {
					}
    			}			
    		}
    		
        	
      
        }
    }
}
