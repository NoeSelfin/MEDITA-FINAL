package org.simo.medita;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MeditaFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String LOGTAG = "android-fcm";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        //Se obtiene el token actualizado
        Log.d(LOGTAG, "Token actualizado: " + token);
    }
}
