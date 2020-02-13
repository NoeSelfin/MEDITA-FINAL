package org.simo.medita;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.widget.RemoteViews;
import android.widget.TextView;

public class NotificacionesOld extends Notification {

	private Context ctx;
	private NotificationManager mNotificationManager;
	private static NotificacionesOld instance = null;
		
	@SuppressLint("NewApi")
	   protected NotificacionesOld() {
		   super();
	   }
	   public static NotificacionesOld getInstance() {
	      if(instance == null) {
	         instance = new NotificacionesOld();
	      }
	      return instance;
	   }
	

	
	public void setNotificaciones(Context ctx, String titulo, Bitmap icono, String colorString){
	 
	    this.ctx=ctx;
	    String ns = Context.NOTIFICATION_SERVICE;
	    mNotificationManager = (NotificationManager) ctx.getSystemService(ns);
	    CharSequence tickerText = "Shortcuts";
	    long when = System.currentTimeMillis();
	    Builder builder = new Builder(ctx);
	    @SuppressWarnings("deprecation")
	    Notification notification=builder.getNotification();
	    notification.when=when;
	    notification.tickerText=tickerText;
	    notification.icon=R.drawable.ic_launcher;
	
	    RemoteViews contentView=new RemoteViews(ctx.getPackageName(), R.layout.notification_player);
	    
	    contentView.setTextViewText(R.id.id_notificacion_titulo, titulo);
	    contentView.setImageViewBitmap(R.id.notificacion_icono, icono);
	    contentView.setInt(R.id.id_notificacion_icono_rl, "setBackgroundColor", Color.parseColor(colorString));
	
	    //set the button listeners
	    setListeners(contentView);
	
	    notification.contentView = contentView;
	    notification.flags |= Notification.FLAG_ONGOING_EVENT;
	    CharSequence contentTitle = "Medita";
	    mNotificationManager.notify(548853, notification);
	}

	public void setListeners(RemoteViews view){
	    Intent left=new Intent(ctx,HelperActivity.class);
	    left.putExtra("DO", "left");
	    PendingIntent pRadio = PendingIntent.getActivity(ctx, 0, left, 0);
	    view.setOnClickPendingIntent(R.id.id_notificacion_play_left, pRadio);
	
	    Intent play=new Intent(ctx, HelperActivity.class);
	    play.putExtra("DO", "play");
	    PendingIntent pVolume = PendingIntent.getActivity(ctx, 1, play, 0);
	    view.setOnClickPendingIntent(R.id.id_notificacion_play, pVolume);
	
	    Intent right=new Intent(ctx, HelperActivity.class);
	    right.putExtra("DO", "right");
	    PendingIntent pReboot = PendingIntent.getActivity(ctx, 2, right, 0);
	    view.setOnClickPendingIntent(R.id.id_notificacion_play_rigth, pReboot);
	    
	    Intent close=new Intent(ctx, HelperActivity.class);
	    close.putExtra("DO", "close");
	    PendingIntent pclose = PendingIntent.getActivity(ctx, 3, close, 0);
	    view.setOnClickPendingIntent(R.id.id_notificacion_close, pclose);
	
	   
	}

}
