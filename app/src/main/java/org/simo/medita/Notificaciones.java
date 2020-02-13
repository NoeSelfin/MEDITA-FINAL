package org.simo.medita;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class Notificaciones extends Notification {

	private Context ctx;
	
    public  Notificaciones(){      
    }

	
	public void setNotificaciones(Context ctx){
	    
		//Uri path = Uri.parse("android.resource://org.simo.medita/" + R.raw.cuenco);
	    this.ctx=ctx;
	    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx);
	    mBuilder.setSmallIcon(R.drawable.icono);
	    mBuilder.setSound(Uri.parse("android.resource://" + ctx.getPackageName() + "/" + R.raw.cuenco ));
	    mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
	    mBuilder.setContentTitle("Medita");
	    mBuilder.setContentText("Es hora de meditar!");

	    
	    NotificationManager mNotificationManager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
	    
	 // notificationID allows you to update the notification later on.
	    mNotificationManager.notify(999999, mBuilder.build());
	    
	}


}
