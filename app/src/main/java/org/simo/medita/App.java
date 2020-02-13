package org.simo.medita;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class App extends Application {

    private SharedPreferences prefs;
    private boolean user_info_updated = false;
    private boolean store_info_updated = false;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = getSharedPreferences(getString(R.string.sharedpref_name), Context.MODE_PRIVATE);

        if(prefs.getBoolean(getString(R.string.registrado),false)){


        }

    }

    private void checkLogIn(){

        /* if ok
        *   getUserInfo();
        *   getStoreInfo();
        * */


    }

    private void getUserInfo(){
        // user_info_updated = true;
    }

    private void getStoreInfo(){
        // store_info_updated = true;
    }

    private void compareInfo(){

    }
}
