package org.simo.medita;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MeditationFunctions {
    protected SharedPreferences prefs;

    public MeditationFunctions(Context ctx){
        prefs = ctx.getSharedPreferences(ctx.getString(R.string.sharedpref_name), Context.MODE_PRIVATE);
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
}
