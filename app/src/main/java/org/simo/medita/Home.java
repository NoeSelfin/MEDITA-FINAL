package org.simo.medita;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;


public class Home extends Activity {
    protected SharedPreferences prefs;
    protected Typeface font;
    protected SlidingMenu menu_lateral;
    protected LinearLayout menu;
    protected RelativeLayout option1;
    protected RelativeLayout option2;
    protected LinearLayout option3;
    protected LinearLayout option4;
    protected LinearLayout option5;
    protected TextView home_option2_title;
    protected TextView home_name;
    JSONObject last_version;
    JSONArray cats = null;
    JSONArray cats2 = null;
    JSONArray cats3 = null;

    MeditationFunctions med_funcs;
    JSONObject meditation_opt2;
    JSONObject pack_opt2;

    AdapterHomeMoreContents adapterHomeMoreContents;
    AdapterHomeNeeds adapterHomeNeeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
        prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
        med_funcs = new MeditationFunctions(getApplicationContext());

        menu = (LinearLayout)findViewById(R.id.id_opciones_atras);
        option1 = (RelativeLayout)findViewById(R.id.id_home_option1);
        option2 = (RelativeLayout)findViewById(R.id.id_home_option2);
        option3 = (LinearLayout)findViewById(R.id.id_home_option3);
        option4 = (LinearLayout)findViewById(R.id.id_home_option4);
        option5 = (LinearLayout)findViewById(R.id.id_home_option5);

        home_option2_title = (TextView)findViewById(R.id.id_home_option2_title);
        home_name = (TextView)findViewById(R.id.id_two_text_1);

        String options_string = prefs.getString("home_options","");

        Log.i(Config.tag, options_string);

        home_name.setText("Hola "+prefs.getString("nombre_usuario",""));
        home_name.setTypeface(font);
        if(prefs.contains("meditations_download") == false){
            option5.setVisibility(View.GONE);
        }

        try {
            JSONObject options = new JSONObject(options_string);
            //Notes and meditations last version
            last_version = options.optJSONObject("last_version");

            cats = options.optJSONArray("cats");
            cats2 = options.optJSONArray("cats2");
            cats3 = options.optJSONArray("cats3");

            getFullInfoCat3();
            home_option2_title.setText(meditation_opt2.optString("med_titulo",""));


            Log.i(Config.tag, options.optJSONArray("cats3").toString());
        } catch (JSONException e) {
            Log.i(Config.tag, "Error parse options.");
        }

        // set up the RecyclerView
        RecyclerView recyclerView_more_contents = findViewById(R.id.id_recycler_more_contents);
        RecyclerView recyclerView_needs = findViewById(R.id.id_recycler_needs);
        recyclerView_more_contents.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_needs.setLayoutManager(new LinearLayoutManager(this));
        adapterHomeMoreContents = new AdapterHomeMoreContents(this, cats2);
        adapterHomeNeeds = new AdapterHomeNeeds(this, cats);

        adapterHomeMoreContents.setClickListener(new AdapterHomeMoreContents.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String meditations_string = cats2.optJSONObject(position).optString("meditations");
                try {
                    JSONArray meditations_array = new JSONArray(meditations_string);
                    String meditations = med_funcs.getMeditationsFromArray(meditations_array).toString();
                    Log.i(Config.tag,meditations);

                    Intent i = new Intent(Home.this, MeditacionesHomeB.class);
                    i.putExtra("onlyDurs",false);
                    i.putExtra("title", cats2.optJSONObject(position).optString("title"));
                    i.putExtra("option", 3);
                    i.putExtra("id_cat", position);
                    i.putExtra("meditations", meditations);
                    i.putExtra("fromHome", true);
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                }
            }
        });

        adapterHomeNeeds.setClickListener(new AdapterHomeNeeds.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String meditations_string = cats.optJSONObject(position).optString("meditations");
                try {
                    JSONArray meditations_array = new JSONArray(meditations_string);
                    String meditations = med_funcs.getMeditationsFromArray(meditations_array).toString();
                    Log.i(Config.tag,meditations);

                    Intent i = new Intent(Home.this, MeditacionesHome.class);
                    i.putExtra("onlyDurs",false);
                    i.putExtra("title", cats.optJSONObject(position).optString("title"));
                    i.putExtra("option", 3);
                    i.putExtra("meditations", meditations);
                    i.putExtra("fromHome", true);
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                }
            }
        });

        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(Home.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLayoutManager2 = new LinearLayoutManager(Home.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_more_contents.setLayoutManager(horizontalLayoutManager1);
        recyclerView_needs.setLayoutManager(horizontalLayoutManager2);

        recyclerView_more_contents.addItemDecoration(new ItemOffsetDecoration(getApplicationContext(), R.dimen.grid_horizontal_spacing));
        recyclerView_needs.addItemDecoration(new ItemOffsetDecoration(getApplicationContext(), R.dimen.grid_horizontal_spacing));

        recyclerView_more_contents.setAdapter(adapterHomeMoreContents);
        recyclerView_needs.setAdapter(adapterHomeNeeds);

        option1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONArray packs = null;
                try {
                    packs = new JSONArray(prefs.getString("packs", ""));
                    Intent i = new Intent(Home.this, Meditaciones.class);
                    i.putExtra("pack", packs.optJSONObject(0).toString());
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                }
            }
        });
        option2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent i = new Intent(Home.this, Reproductor.class);
                Intent i = new Intent(Home.this, MeditacionesHome.class);
                i.putExtra("pack", pack_opt2.toString());
                i.putExtra("onlyDurs",true);
                i.putExtra("meditacion", meditation_opt2.toString());
                i.putExtra("dur", 0);
                i.putExtra("duracion", med_funcs.getMeditationDuraciones(meditation_opt2).optString(0));
                i.putExtra("title", "Recomendación semanal");
                i.putExtra("option", 2);
                //i.putExtra("intros", false);

                i.putExtra("fromHome", true);
                startActivity(i);
                finish();

            }
        });
        option3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ( last_version != null){
                    /*{
                         "id_updates": "7",
                        "date": "2021-11-10 10:54:16",
                        "notes": "Nuevas meditaciones y reajustes nuevo Home app",
                        "version": "98",
                        "meditations": "[\"265\"]"
                     }*/
                    String meditations_string = last_version.optString("meditations");
                    try {
                        JSONArray meditations_array = new JSONArray(meditations_string);
                        String meditations = med_funcs.getMeditationsFromArray(meditations_array).toString();
                        Log.i(Config.tag,meditations);

                        Intent i = new Intent(Home.this, MeditacionesHomeB.class);
                        i.putExtra("onlyDurs",false);
                        i.putExtra("option", 5);
                        i.putExtra("notes", last_version.optString("notes"));
                        i.putExtra("meditations", meditations);
                        i.putExtra("fromHome", true);
                        startActivity(i);
                        finish();
                    } catch (JSONException e) {
                    }
                }
            }
        });
        option4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Home.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        option5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String meditations_download_string = prefs.getString("meditations_download","");
                try {
                    JSONArray meditations_array = new JSONArray(meditations_download_string);
                    String meditations = med_funcs.getMeditationsFromArray(meditations_array).toString();
                    Log.i(Config.tag,meditations);

                    Intent i = new Intent(Home.this, MeditacionesHome.class);
                    i.putExtra("onlyDurs",false);
                    i.putExtra("title", "Meditaciones descargadas");
                    i.putExtra("option", 3);
                    i.putExtra("meditations", meditations);
                    i.putExtra("fromHome", true);
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                    Log.i(Config.tag,"Error mostrando meditaciones descargadas.");
                }

            }
        });

        menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                menu_lateral.showMenu(true);
            }
        });
        setMenu();

    }

    protected void getFullInfoCat3(){
        Calendar now = Calendar.getInstance();
        int week_of_month = now.get(Calendar.WEEK_OF_MONTH);
        int month = now.get(Calendar.MONTH) + 1;

        if (week_of_month > 4){
            week_of_month = 4;
        }

        String m = "id_"+String.valueOf(month)+"_"+String.valueOf(week_of_month);
        JSONArray meditations = new JSONArray();
        for (int i=0; i<cats3.length();i++){
            if (cats3.optJSONObject(i).optString("id").compareTo(m) == 0){
                try {
                    meditations = new JSONArray(cats3.optJSONObject(i).optString("meditations"));
                } catch (JSONException e) {
                }
            }
        }

        meditation_opt2 = med_funcs.getMeditationById(meditations.optString(0));
        pack_opt2 = med_funcs.getPackById(meditation_opt2.optString("id_pack"));

    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(Home.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(i);
        finish();
    }

    public void setMenu(){
        menu_lateral = new SlidingMenu(Home.this);
        menu_lateral.setMode(SlidingMenu.LEFT);
        menu_lateral.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu_lateral.setShadowWidthRes(R.dimen.shadow_width);
        menu_lateral.setShadowDrawable(R.drawable.shadow);
        menu_lateral.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu_lateral.setFadeDegree(0.35f);
        menu_lateral.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu_lateral.setMenu(R.layout.fragment_menu);
        ((TextView) menu_lateral.findViewById(R.id.id_menu_titulo)).setTypeface(font);
        ((TextView) menu_lateral.findViewById(R.id.id_menu_inicio)).setTypeface(font);
        ((TextView) menu_lateral.findViewById(R.id.id_menu_fav)).setTypeface(font);
        ((TextView) menu_lateral.findViewById(R.id.id_menu_progreso)).setTypeface(font);
        ((TextView) menu_lateral.findViewById(R.id.id_menu_acerca)).setTypeface(font);
        ((TextView) menu_lateral.findViewById(R.id.id_menu_vision)).setTypeface(font);
        ((TextView) menu_lateral.findViewById(R.id.id_menu_opciones)).setTypeface(font);
        ((TextView) menu_lateral.findViewById(R.id.id_menu_sincro)).setTypeface(font);
        ((TextView) menu_lateral.findViewById(R.id.id_menu_compras)).setTypeface(font);
        ((TextView) menu_lateral.findViewById(R.id.id_menu_suscription)).setTypeface(font);
        ((TextView) menu_lateral.findViewById(R.id.id_menu_contact)).setTypeface(font);
        ((TextView) menu_lateral.findViewById(R.id.id_menu_news)).setTypeface(font);

        ((View) menu_lateral.findViewById(R.id.id_menu_view_ini)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_fav)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_progreso)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_acercade)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_vision)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_opciones)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_sincro)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_suscription)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_contact)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_news)).setVisibility(View.VISIBLE);


        LinearLayout opciones = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_opciones_ll);
        opciones.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Home.this, Opciones.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout vision = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_vision_ll);
        vision.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Home.this, Vision.class);
                i.setAction("fromMenu");
                startActivity(i);
                finish();
            }
        });
        LinearLayout favoritos = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_fav_ll);
        favoritos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Home.this, Favoritos.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout progreso = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_progreso_ll);
        progreso.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Home.this, Charts.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout inicio = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_inicio_ll);
        inicio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Home.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout sincro = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_sincro_ll);
        sincro.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (prefs.contains("sincronizado")){
                    if (prefs.getBoolean("sincronizado", false)){
                        Intent i = new Intent(Home.this, Sincro2.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Intent i = new Intent(Home.this, Sincro.class);
                        startActivity(i);
                        finish();
                    }
                }
                else{
                    Intent i = new Intent(Home.this, Sincro.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        LinearLayout compras = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_compras_ll);
        compras.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new RecargarCompras(Home.this);
            }
        });

        LinearLayout suscripcion = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_suscription_ll);
        suscripcion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Home.this, Suscripcion.class);
                startActivity(i);
                finish();
                // si esta registrado, va a la suscripcion. En caso contrario al login
				/*if (prefs.getBoolean(getString(R.string.registrado),false)){
					Intent i = new Intent(Acercade.this, Suscripcion.class);
					startActivity(i);
					finish();
				}else{
					Intent i = new Intent(Acercade.this, LogIn.class);
					startActivity(i);
					finish();
				}*/
            }
        });
        LinearLayout contacto = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_contact_ll);
        contacto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Home.this, LogIn.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout news = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_news_ll);
        news.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Home.this, Novedades.class);
                startActivity(i);
                finish();
            }
        });

        LinearLayout notalegal = menu_lateral.findViewById(R.id.id_menu_legal_ll);
        notalegal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // si esta registrado, va a la suscripcion. En caso contrario al login
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Config.url_nota_legal));
                startActivity(i);
            }
        });

        ImageView rs1 = menu_lateral.findViewById(R.id.id_rs1);
        rs1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String urlPage = "https://www.youtube.com/channel/UCOKXZZHPxigzEvJPd8vGvNw";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
            }
        });
        ImageView rs2 = menu_lateral.findViewById(R.id.id_rs2);
        rs2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Uri uri = Uri.parse("https://instagram.com/_u/medita_app");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://instagram.com/medita_app")));
                }
            }
        });
        ImageView rs3 = menu_lateral.findViewById(R.id.id_rs3);
        rs3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = null;
                try {
                    // get the Twitter app if possible
                    getApplication().getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=enriquesimo8"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/enriquesimo8"));
                }
                startActivity(intent);
            }
        });
        ImageView rs4 = menu_lateral.findViewById(R.id.id_rs4);
        rs4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String facebookId = "fb://page/appmedita";
                String urlPage = "https://www.facebook.com/appmedita";

                try {
                    getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/appmedita")));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/appmedita")));
                }
            }
        });

    }



    protected void alert(String mens, String tit){

        final Dialog dialog = new Dialog(Home.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_generico);
        dialog.setCancelable(false);

        // set the custom dialog components - text, image and button
        TextView close = (TextView) dialog.findViewById(R.id.id_alert_btn);
        TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
        TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);


        if (titulo!=null)
            titulo.setText(tit);

        if (mens != null)
            text.setText(mens);

        text.setTypeface(font);
        close.setTypeface(font);
        titulo.setTypeface(font);

        // if button is clicked, close the custom dialog
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

}
