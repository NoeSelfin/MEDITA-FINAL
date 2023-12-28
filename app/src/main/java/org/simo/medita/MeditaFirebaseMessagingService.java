package org.simo.medita;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
//import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;

public class MeditaFirebaseMessagingService extends FirebaseMessagingService {

    private static final String LOGTAG = "android-fcm";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {

            String titulo = remoteMessage.getNotification().getTitle();
            String texto = remoteMessage.getNotification().getBody();

            Log.d(LOGTAG, "NOTIFICACION RECIBIDA");
            Log.d(LOGTAG, "Título: " + titulo);
            Log.d(LOGTAG, "Texto: " + texto);

            //Opcional: mostramos la notificación en la barra de estado
            showNotification(titulo, texto);
        }
    }

    private void showNotification(String title, String text) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // Configure the channel
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1000", "Medita", importance);
            channel.setDescription("Reminders");
            // Register the channel with the notifications manager
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(channel);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, "1000")
                            .setSmallIcon(R.drawable.icono)
                            .setContentTitle(title)
                            .setContentText(text);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        } else{
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, "1000")
                            .setSmallIcon(R.drawable.icono)
                            .setContentTitle(title)
                            .setContentText(text);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }


    }
}
