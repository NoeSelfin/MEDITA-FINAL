package org.simo.medita;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Functions {
    protected SharedPreferences prefs;
    protected Context ctx;
    public Functions(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences(ctx.getString(R.string.sharedpref_name), Context.MODE_PRIVATE);

    }
    public void showMenu(){
        LinearLayout menu_container = (LinearLayout)((Activity)ctx).findViewById(R.id.id_bottommenu);
        menu_container.setVisibility(View.VISIBLE);
    }
    public boolean shouldShowMenu(){
        boolean show = false;
        //Registrado
        //Suscrito internamente
        //Promocionado
        //Suscrito externamente
        /*if(prefs.getBoolean(ctx.getString(R.string.registrado), false)){
            show = true;
        }*/
        if(prefs.getBoolean(ctx.getString(R.string.suscrito), false)){
            show = true;
        }
        String p = prefs.getString("promo",null);
        if ( p!=null ){
            try{
                JSONObject jo = new JSONObject(p);
                if(validateCode(jo)){
                    show = true;
                }
            } catch (JSONException e) {
            }

        }
        return show;
    }
    public boolean shouldShowAllContents(){
        boolean show = false;
        //Suscrito internamente
        //Promocionado
        //Suscrito externamente
        if(prefs.getBoolean(ctx.getString(R.string.suscrito), false)){
            show = true;
        }
        String p = prefs.getString("promo",null);
        if ( p!=null ){
            try{
                JSONObject jo = new JSONObject(p);
                if(validateCode(jo)){
                    show = true;
                }
            } catch (JSONException e) {
            }

        }

        return show;
    }
    public int getProgresoNivel(){
        int nivel = 0;
        if (prefs.contains("Premios_1"))
            nivel = nivel + 1;
        if (prefs.contains("Premios_2"))
            nivel = nivel+1;
        if (prefs.contains("Premios_3"))
            nivel = nivel+1;
        if (prefs.contains("Premios_4"))
            nivel = nivel+1;
        if (prefs.contains("Premios_5"))
            nivel = nivel+1;
        if (prefs.contains("Premios_6"))
            nivel = nivel+1;
        if (prefs.contains("Premios_7"))
            nivel = nivel+1;
        if (prefs.contains("Premios_8"))
            nivel = nivel+1;
        if (prefs.contains("Premios_9"))
            nivel = nivel+1;
        if (prefs.contains("Premios_10"))
            nivel = nivel+1;

        return nivel;
    }
    private boolean validateCode(JSONObject jo){
        //[{"id_promo":"1","from":"2020-01-01 00:00:00","to":"2020","code":"abcd","type":"1","consumed":"0","deleted_at":null}]
        if (jo.length() > 3){
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date to = formatter.parse(jo.optString("to","2040-01-01 00:00:00"));
                Date from = formatter.parse(jo.optString("from","2010-01-01 00:00:00"));
                Date now = new Date();

                if (now.after(from) && now.before(to)){

                    if(jo.optInt("type") == 2){
                        return true;
                    }else if ((jo.optInt("type") == 1)&& (jo.optInt("consumed") == 0)){
                        return true;
                    }
                }
            } catch (ParseException e) {
                printLog("ParseException:" + e.getMessage());
            }
        }
        return false;
    }
    private void printLog(String text){
        if (Config.log){
            Log.i(Config.tag, text);
        }
    }
    public int getMinutesFromSeconds(int seconds){
        return Integer.valueOf(seconds/60);
    }
}
