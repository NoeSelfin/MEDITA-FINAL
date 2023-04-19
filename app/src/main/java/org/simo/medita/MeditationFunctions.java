package org.simo.medita;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MeditationFunctions {
    protected SharedPreferences prefs;

    public MeditationFunctions(Context ctx){
        prefs = ctx.getSharedPreferences(ctx.getString(R.string.sharedpref_name), Context.MODE_PRIVATE);
    }

    public void setMeditationDownload(String id_meditation){
        String download_string = prefs.getString("download_meds",null);
        try {
            JSONArray download = null;

            if(download_string == null){
                download = new JSONArray();
                download.put(id_meditation);
                prefs.edit().putString("download_meds", download.toString()).commit();
            }else{
                download = new JSONArray(download_string);
                boolean finded = false;
                for (int i=0;i<download.length();i++){
                    if (download.optString(i).compareTo(id_meditation) == 0){
                        finded = true;
                    }
                }
                if(finded == false){
                    download.put(id_meditation);
                    prefs.edit().putString("download_meds", download.toString()).commit();
                }
            }

        } catch (JSONException e) {
        }
    }
    public void deleteMeditationDownload(String id_meditation){
        String download_string = prefs.getString("download_meds",null);
        try {
            JSONArray download = null;
            JSONArray new_download = new JSONArray();

            if(download_string != null){
                download = new JSONArray(download_string);
                boolean finded = false;
                for (int i=0;i<download.length();i++){
                    if (download.optString(i).compareTo(id_meditation) == 0){
                    }else{
                        new_download.put(download.optString(i));
                    }
                }
                prefs.edit().putString("download_meds", new_download.toString()).commit();
            }

        } catch (JSONException e) {
        }
    }
    public JSONArray getMeditationDownload(){
        String download_string = prefs.getString("download_meds","");
        try {
            JSONArray download = new JSONArray(download_string);
            return download;
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    protected JSONObject getMeditationById(String id_meditation){
        try {
            JSONArray meditations = new JSONArray (prefs.getString("meditaciones",""));
            for (int i=0;i<meditations.length();i++){
                if (meditations.optJSONObject(i).optString("id_meditacion").compareTo(id_meditation) == 0){
                    return meditations.optJSONObject(i);
                }
            }
        } catch (JSONException e) {
            return new JSONObject();
        }

        return new JSONObject();
    }
    protected JSONObject getPackById(String id_pack){
        try {
            JSONArray packs = new JSONArray (prefs.getString("packs",""));
            for (int i=0;i<packs.length();i++){
                if (packs.optJSONObject(i).optString("id_pack").compareTo(id_pack) == 0){
                    return packs.optJSONObject(i);
                }
            }
        } catch (JSONException e) {
            return new JSONObject();
        }

        return new JSONObject();
    }
    protected JSONArray getMeditationDuraciones(JSONObject meditation){
       String duraciones_string = meditation.optString("duraciones");
        try {
            JSONArray duraciones = new JSONArray(duraciones_string);
            return duraciones;
        } catch (JSONException e) {
            return new JSONArray();
        }
    }
    protected JSONArray getMeditationsFromArray(JSONArray meditations_array){
        try {
            JSONArray meditations = new JSONArray (prefs.getString("meditaciones",""));
            JSONArray meditations_final = new JSONArray();
            for (int i=0;i<meditations.length();i++){
                for (int j=0;j<meditations_array.length();j++){
                    if (meditations.optJSONObject(i).optString("id_meditacion").compareTo(meditations_array.optString(j)) == 0){
                        meditations_final.put(meditations.optJSONObject(i));
                    }
                }
            }
            return meditations_final;
        } catch (JSONException e) {
        }
        return new JSONArray();
    }
    protected int getTotalSecondsMed(){
        int total = prefs.getInt("total1",0);
        return total;
    }
    protected void setTotalSecondsMed(int seconds){
        int total = prefs.getInt("total1",0);
        total = total + seconds;
        prefs.edit().putInt("total1",total).commit();
    }
    protected int getTotalPacks(){
        JSONArray totals3 = null;
        try {
            totals3 = new JSONArray(prefs.getString("total3",""));
            return totals3.length();
        } catch (JSONException e) {
            Log.i(Config.tag,"error: "+e.getMessage());
            return 0;
        }
    }
    protected void setTotalPacks(String id_pack){
        boolean finded = false;
        JSONArray totals3 = null;
        Log.i(Config.tag,"Pack_id: "+id_pack);
        try {
            String t = prefs.getString("total3",null);
            Log.i(Config.tag,"total3: "+t);
            if (t == null){
                totals3 = new JSONArray();
            }else{
                totals3 = new JSONArray(t);
            }

            for (int i=0;i<totals3.length();i++){
                if(totals3.optString(i).compareTo(id_pack) == 0){
                    finded = true;
                }
            }
            if (finded == false){
                totals3.put(id_pack);
                Log.i(Config.tag,"totals3: "+totals3.toString());

                prefs.edit().putString("total3",totals3.toString()).commit();
            }
        } catch (JSONException e) {
            Log.i(Config.tag,"totals3 error: "+e.getMessage());
        }
    }
    protected String getTotalMedLong(){
        String totals4 = prefs.getString("total4","0");
        return totals4;
    }
    protected void setTotalMedLong(int l){
        Log.i(Config.tag,"totals4: "+String.valueOf(l));
        String totals4 = prefs.getString("total4","0");
        Log.i(Config.tag,"totals4: "+totals4);
        if (Integer.valueOf(totals4) < l){
            prefs.edit().putString("total4",String.valueOf(l)).commit();
        }
    }
    protected int getTotalDaysContinous(){
        JSONArray totals2 = null;
        try {
            totals2 = new JSONArray(prefs.getString("total2",""));
            return totals2.length();
        } catch (JSONException e) {
            Log.i(Config.tag,"error: "+e.getMessage());
            return 0;
        }
    }
    protected void setTotalDaysContinous(){
        boolean finded = false;
        JSONArray totals2 = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String strDate = sdf.format(now);
        try {
            String t = prefs.getString("total2",null);
            Log.i(Config.tag,"totals2: "+t);

            if (t == null){
                totals2 = new JSONArray();
            }else{
                totals2 = new JSONArray(t);
            }

            for (int i=0;i<totals2.length();i++){
                if(totals2.optString(i).compareTo(strDate) == 0){
                    finded = true;
                }
            }
            if (finded == false){
                totals2.put(strDate);
                Log.i(Config.tag,"totals2: "+strDate);
                prefs.edit().putString("total2",totals2.toString()).commit();

            }
        } catch (JSONException e) {
            Log.i(Config.tag,"totals2 error: "+e.getMessage());
        }
    }
    protected void setTotalDaySeconds(int seconds){
        boolean finded = false;
        JSONArray chart1 = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String now = dateFormat.format(cal.getTime());
        //cal.add(Calendar.DATE, -15);
        //String past = dateFormat.format(cal.getTime());

        try {
            String t = prefs.getString("chart1",null);
            Log.i(Config.tag,"chart1: "+t);

            if (t == null){
                chart1 = new JSONArray();
            }else{
                chart1 = new JSONArray(t);
            }

            JSONArray new_array = new JSONArray();
            for (int i=0;i<15;i++){
                Log.i(Config.tag,"chart1: " + dateFormat.format(cal.getTime()));

                for (int j=0;j<chart1.length();j++){
                    if(chart1.optJSONObject(j).optString("fecha").compareTo(dateFormat.format(cal.getTime())) == 0){
                        if(chart1.optJSONObject(j).optString("fecha").compareTo(now) == 0){
                            chart1.optJSONObject(j).put("valor",chart1.optJSONObject(j).optInt("valor") + seconds);
                            new_array.put(chart1.optJSONObject(j));
                        }else{
                            new_array.put(chart1.optJSONObject(j));
                        }
                        finded = true;
                    }
                }
                if (finded == false){
                    JSONObject jo = new JSONObject();
                    if(dateFormat.format(cal.getTime()).compareTo(now) == 0){
                        jo.put("fecha",dateFormat.format(cal.getTime()));
                        jo.put("valor",seconds);
                    }else{
                        jo.put("fecha",dateFormat.format(cal.getTime()));
                        jo.put("valor",0);
                    }
                    new_array.put(jo);
                }

                cal.add(Calendar.DATE, -1);
            }


            prefs.edit().putString("chart1",new_array.toString()).commit();

        } catch (JSONException e) {
            Log.i(Config.tag,"chart1 error: "+e.getMessage());
        }
    }
    protected JSONArray getTotalDaySeconds(){
        JSONArray chart1 = null;
        try {
            chart1 = new JSONArray(prefs.getString("chart1",""));
            return chart1;
        } catch (JSONException e) {
            Log.i(Config.tag,"error: "+e.getMessage());
        }
        return chart1;
    }
    protected String getTotalDaySecondsString(){
        return prefs.getString("chart1","");

    }
    protected void setTotalSectionsSeconds(int seconds, String id_meditation){
        try {
            JSONObject chart2 = null;
            String options_string = prefs.getString("home_options","");
            JSONObject options = new JSONObject(options_string);
            JSONArray cats2 = options.optJSONArray("cats2");

            String t = prefs.getString("chart2",null);
            Log.i(Config.tag,"chart2: "+cats2);
            if (t == null){
                chart2 = new JSONObject();
            }else{
                chart2 = new JSONObject(t);
            }

            for (int i=0;i<cats2.length();i++){
                JSONArray ja = new JSONArray(cats2.optJSONObject(i).optString("meditations"));
                for (int j=0;j<ja.length();j++){
                    if(ja.optString(j).compareTo(id_meditation) == 0){
                        //Recuperamos el valor entero de la categoría de shared preferences y le sumamos los segundos
                        String id_cat = cats2.optJSONObject(i).optString("id_cat");
                        chart2.put(id_cat,chart2.optInt(id_cat,0) + seconds);

                    }
                }
            }
            prefs.edit().putString("chart2",chart2.toString()).commit();

        } catch (JSONException e) {
            Log.i(Config.tag,"chart1 error: "+e.getMessage());
        }
    }
    protected JSONObject getTotalSectionsSeconds(){
        JSONObject chart2 = null;
        try {
            chart2 = new JSONObject(prefs.getString("chart2",""));
            return chart2;
        } catch (JSONException e) {
            Log.i(Config.tag,"error: "+e.getMessage());
        }
        return chart2;
    }
    protected String getTotalSectionsSecondsString(){
        return prefs.getString("chart2","");
    }
}
