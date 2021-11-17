package org.simo.medita;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.extras.FilterData;

import java.util.ArrayList;

public class MeditacionesHome extends Activity{
    protected SharedPreferences prefs;
    MeditationFunctions med_funcs;
    protected AdapterMeditaciones adaptermeditaciones;
    protected AdapterMeditacionesHome adaptermeditacioneshome;
    protected AdapterMeditacionesDuracion adaptermeditacionesduracion;
    protected ListView listview;
    protected ListView listview_duraciones;
    protected JSONObject pack;
    protected JSONArray meditaciones;
    protected JSONObject meditacion;
    protected LinearLayout atras;
    protected TextView disponibles;
    protected TextView title;
    protected LinearLayout header;
    protected LinearLayout help;
    protected Typeface font;
    protected RelativeLayout rl_principal;
    protected boolean duraciones = false;
    protected ArrayList<String> durs;
    protected Display display;
    protected int width;
    protected Point size;
    protected  Animation animation;
    protected boolean onlyDurs = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_meditaciones_home);
        font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
//		prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
        prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
        med_funcs = new MeditationFunctions(getApplicationContext());

        atras = (LinearLayout)findViewById(R.id.id_meditaciones_atras);
        help = (LinearLayout)findViewById(R.id.id_meditaciones_help);
        listview  = (ListView)findViewById(R.id.id_meditaciones_listview);
        listview_duraciones  = (ListView)findViewById(R.id.id_meditaciones_listview_duraciones);
        rl_principal = (RelativeLayout)findViewById(R.id.id_meditaciones_rl_list);
        disponibles = (TextView)findViewById(R.id.id_meditaciones_disponibles);
        title = (TextView)findViewById(R.id.id_meditaciones_home_title);

        disponibles.setTypeface(font);
        title.setTypeface(font);

        meditaciones = new JSONArray();

        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        width = size.x;

        Intent i = getIntent();
        if(i != null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                try {
                    title.setText(extras.getString("title"));
                    onlyDurs = extras.getBoolean("onlyDurs");


                    if (extras.containsKey("pack")){
                        pack = new JSONObject(extras.getString("pack"));
                        if (prefs.contains("meditaciones")){
                            JSONArray meditaciones_all = new JSONArray (prefs.getString("meditaciones",""));
                            meditaciones = new FilterData().filterMeditaciones(meditaciones_all, pack.getString("id_pack"));
                            if (Config.log)
                                Log.i("medita","Meditaciones: " + meditaciones.toString());

                            adaptermeditaciones = new AdapterMeditaciones(this, meditaciones,pack);
                            listview.setAdapter(adaptermeditaciones);
                            setListView();
                        }
                        else{
                            //Error, no hay meditaciones.
                        }
                    }else{
                        if (extras.containsKey("meditations")){
                            meditaciones = new JSONArray (extras.getString("meditations"));
                            adaptermeditacioneshome = new AdapterMeditacionesHome(this, meditaciones);
                            listview.setAdapter(adaptermeditacioneshome);
                            setListView();
                        }

                    }

                    Log.i("medita","procesando onlydurs");
                    if (onlyDurs){
                        showDurs(new JSONObject(extras.getString("meditacion")),false);
                        Log.i("medita","onlydurs procesada");
                    }



                } catch (JSONException e) {
                    Log.i("medita","Meditaciones error.");
                }
            }
        }




        atras.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (duraciones){
                    if(onlyDurs){
                        if(onlyDurs){
                            Intent i = new Intent(MeditacionesHome.this, Home.class);
                            i.setAction(Config.from_Meditaciones_Home);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    }else{
                        animation = new TranslateAnimation(-width, 0, 0, 0);
                        animation.setDuration(400);
                        // animation.setFillAfter(true);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation arg0) {
                                disponibles.setText(pack.optString("pack_titulo"));
                                listview_duraciones.setVisibility(View.GONE);
                                listview.setVisibility(View.VISIBLE);
                                duraciones = false;
                            }

                            @Override
                            public void onAnimationStart(Animation animation) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                // TODO Auto-generated method stub

                            }
                        });
                        listview.startAnimation(animation);
                        animation = new TranslateAnimation(0, width,0, 0);
                        animation.setDuration(400);
                        //animation.setFillAfter(true);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation arg0) {
                                disponibles.setText(pack.optString("pack_titulo"));
                                listview_duraciones.setVisibility(View.GONE);
                                listview.setVisibility(View.VISIBLE);
                                duraciones = false;

                            }

                            @Override
                            public void onAnimationStart(Animation animation) {
                                // TODO Auto-generated method stub

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                // TODO Auto-generated method stub

                            }
                        });
                        listview_duraciones.startAnimation(animation);
                    }
                }
                else{
                    Intent i = new Intent(MeditacionesHome.this, Home.class);
                    i.setAction(Config.from_Meditaciones_Home);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }

            }
        });

    }
    public void setListView(){
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                meditacion = meditaciones.optJSONObject(position);
                showDurs(meditacion,true);
            }
        });
    }
    public void showDurs(final JSONObject meditacion, boolean animation_){
        Log.i(Config.tag,"listview OnItemClickListener meditacion -> " + meditacion);
        pack = med_funcs.getPackById(meditacion.optString("id_pack"));


        Log.i(Config.tag,"entra al primer if");
        Log.i(Config.tag,"pack id_pack -> " + pack.optInt("id_pack"));
        Log.i(Config.tag,"pack id_meditacion -> " + meditacion.optInt("id_meditacion"));


        durs = new ArrayList<String>();
        disponibles.setText(meditacion.optString("med_titulo").trim());

        JSONArray durs_json;
        try {
            durs_json = new JSONArray(meditacion.optString("duraciones"));
            if (meditacion.optInt("med_duracion") == 2){
                durs.add(durs_json.optString(0));
                durs.add(durs_json.optString(1));
            }
            else if (meditacion.optInt("med_duracion") == 3){
                durs.add(durs_json.optString(0));
                durs.add(durs_json.optString(1));
                durs.add(durs_json.optString(2));
            }
            else if (meditacion.optInt("med_duracion") == 1){
                durs.add(durs_json.optString(0));
            }
            else{
                durs.add(durs_json.optString(0));
                durs.add(durs_json.optString(1));
            }
        } catch (JSONException e) {
        }

        if (((meditacion.optInt("id_pack") == 1)) && (meditacion.optInt("orden") == 0))
            adaptermeditacionesduracion = new AdapterMeditacionesDuracion(MeditacionesHome.this, durs,true);
        else
            adaptermeditacionesduracion = new AdapterMeditacionesDuracion(MeditacionesHome.this, durs,false);

        listview_duraciones.setAdapter(adaptermeditacionesduracion);

        if (animation_){
            animation = new TranslateAnimation(0, -width,0, 0);
            animation.setDuration(400);
            //animation.setFillAfter(true);
            listview.startAnimation(animation);
            animation = new TranslateAnimation(width, 0,0, 0);
            animation.setDuration(400);
            // animation.setFillAfter(true);
            listview_duraciones.startAnimation(animation);
        }


        listview_duraciones.setVisibility(View.VISIBLE);
        listview.setVisibility(View.INVISIBLE);
        duraciones = true;

        listview_duraciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {


                Intent i = new Intent(MeditacionesHome.this, Reproductor.class);
                i.putExtra("meditacion", meditacion.toString());
                i.putExtra("pack", pack.toString());
                i.putExtra("duracion", durs.get(position));
                i.putExtra("fromHome", true);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();



            }
        });
    }

    @Override
    public void onBackPressed()
    {

        if (duraciones){
            setListView();
            animation = new TranslateAnimation(-width, 0, 0, 0);
            animation.setDuration(400);
            // animation.setFillAfter(true);
            listview.startAnimation(animation);
            animation = new TranslateAnimation(0, width,0, 0);
            animation.setDuration(400);
            // animation.setFillAfter(true);
            listview_duraciones.startAnimation(animation);
            disponibles.setText(pack.optString("pack_titulo"));
            listview_duraciones.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            duraciones = false;
        }
        else{
            Intent i = new Intent(MeditacionesHome.this, MainActivity.class);
            i.setAction(Config.from_Meditaciones);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

    }



}
