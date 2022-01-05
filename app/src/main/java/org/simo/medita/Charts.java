package org.simo.medita;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Charts  extends Activity {
    protected SharedPreferences prefs;
    protected Functions functions;
    protected Typeface font;
    protected Typeface font_negrita;
    protected SlidingMenu menu_lateral;
    protected LinearLayout menu;
    protected BarChart barchart1;
    protected BarChart barchart2;
    protected LinearLayout atras;
    protected LinearLayout ll_regalos;
    protected LinearLayout ll_progreso_puntos_texto_link;
    protected TextView title;
    protected TextView txt1;
    protected TextView txt2;
    protected TextView txt3;
    protected TextView txt4;
    protected TextView puntos_texto;
    protected ListView totals;
    String[] listItem;
    protected int nivel = 0;
    protected MeditationFunctions funcs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_charts);
        font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
        font_negrita = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Bold.otf");
        prefs = getSharedPreferences(getString(R.string.sharedpref_name), Context.MODE_PRIVATE);
        funcs= new MeditationFunctions(this);
        functions = new Functions(this);

        atras = findViewById(R.id.id_charts_atras);
        barchart1 = findViewById(R.id.barchart1);
        barchart2 = findViewById(R.id.barchart2);
        ll_regalos = findViewById(R.id.id_charts_regalos);
        ll_progreso_puntos_texto_link = findViewById(R.id.id_progreso_puntos_texto_link);
        totals = findViewById(R.id.totals);
        title = findViewById(R.id.id_opciones_inicio);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        txt4 = findViewById(R.id.txt4);
        puntos_texto = findViewById(R.id.id_progreso_puntos_texto);
        title.setTypeface(font);
        txt1.setTypeface(font);
        txt2.setTypeface(font);
        txt3.setTypeface(font);
        txt4.setTypeface(font);
        puntos_texto.setTypeface(font_negrita);

        ll_progreso_puntos_texto_link.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Charts.this, Progreso.class);
                startActivity(i);
                finish();
            }
        });

        ll_regalos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Charts.this, Progreso.class);
                startActivity(i);
                finish();
            }
        });

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                menu_lateral.showMenu(true);
            }
        });

        setTotals();
        showBarChart1();
        showBarChart2();


        //Log.i(Config.tag+"_charts",funcs.getTotalDaySecondsString());
        //Log.i(Config.tag+"_charts",funcs.getTotalDaySeconds().toString());



        setMenu();

        if(functions.shouldShowMenu()){
            functions.showMenu();
        }

        puntos_texto.setText(String.valueOf(functions.getProgresoNivel() * 100));
    }
    private void setTotals(){

        listItem = getResources().getStringArray(R.array.array_totals_progreso);
        JSONArray ja = new JSONArray();
        JSONObject jo = new JSONObject();

        try {
            jo = new JSONObject();
            jo.put("title",listItem[0]);
            jo.put("value", TimeUnit.SECONDS.toMinutes(funcs.getTotalSecondsMed()));
            jo.put("units","mins.");

            ja.put(jo);

            jo = new JSONObject();
            jo.put("title",listItem[1]);
            jo.put("value",funcs.getTotalDaysContinous());
            jo.put("units","días");

            ja.put(jo);

            jo = new JSONObject();
            jo.put("title",listItem[2]);
            jo.put("value",funcs.getTotalPacks());
            jo.put("units","-");

            ja.put(jo);

            jo = new JSONObject();
            jo.put("title",listItem[3]);
            jo.put("value",funcs.getTotalMedLong());
            jo.put("units","mins.");

            ja.put(jo);

            totals.setAdapter(new AdapterTotals(this,  ja));
        } catch (JSONException e) {
        }

    }
    private void showBarChart1(){
        JSONArray chart1 = funcs.getTotalDaySeconds();
        ArrayList<Double> valueList = new ArrayList<Double>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "";

        if (chart1 != null){
            //input data
            for(int i = 0; i < chart1.length(); i++){
                valueList.add(chart1.optJSONObject(i).optDouble("valor",0));
            }

            //fit the data into a bar
            for (int i = 0; i < valueList.size(); i++) {
                BarEntry barEntry = new BarEntry(i, valueList.get(i).floatValue());
                entries.add(barEntry);
            }

            BarDataSet barDataSet = new BarDataSet(entries, title);
            barDataSet.setColor(Color.parseColor("#10526f"));

            BarData data = new BarData(barDataSet);
            barchart1.setDrawValueAboveBar(false);
            barchart1.getDescription().setEnabled(false);
            barchart1.setData(data);
            barchart1.invalidate();
        }

    }
    private void showBarChart2(){
        JSONObject chart2 = funcs.getTotalSectionsSeconds();
        ArrayList<Double> valueList = new ArrayList<Double>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "";

        if (chart2 != null){
            BarEntry barEntry = new BarEntry(1,functions.getMinutesFromSeconds(chart2.optInt("1",0)));
            entries.add(barEntry);
            barEntry = new BarEntry(2,functions.getMinutesFromSeconds(chart2.optInt("2",0)));
            entries.add(barEntry);
            barEntry = new BarEntry(3,functions.getMinutesFromSeconds(chart2.optInt("3",0)));
            entries.add(barEntry);
            barEntry = new BarEntry(4,functions.getMinutesFromSeconds(chart2.optInt("4",0)));
            entries.add(barEntry);

            final ArrayList<String> xLabel = new ArrayList<>();
            xLabel.add("Dormir y relajarte");
            xLabel.add("Crecimiento personal");
            xLabel.add("Salud y bienestar");
            xLabel.add("Foco y productividad");


            BarDataSet barDataSet = new BarDataSet(entries, title);
            barDataSet.setColors(new int[] {Color.parseColor("#10526f"),Color.parseColor("#f9c17c"),Color.parseColor("#93f4c3"), Color.parseColor("#7d95ff")});
            barchart2.setDrawValueAboveBar(false);
            //barchart2.getXAxis().setDrawLabels(false);
            barchart2.getDescription().setEnabled(false);
            XAxis xAxis = barchart2.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    String label = "";
                    if(value == 1)
                        label = "Dormir y relajarte";
                    else if(value == 2)
                        label = "Crecimiento personal";
                    else if(value == 3)
                        label = "May";
                    else if(value == 4)
                        label = "Foco y productividad";
                    return label;
                }
            });
            BarData data = new BarData(barDataSet);
            barchart2.setData(data);
            barchart2.invalidate();
        }

    }
    public void setMenu(){
        menu_lateral = new SlidingMenu(Charts.this);
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


        LinearLayout acercade = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_acercade_ll);
        acercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Charts.this, Acercade.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout opciones = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_opciones_ll);
        opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Charts.this, Opciones.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout vision = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_vision_ll);
        vision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Charts.this, Vision.class);
                i.setAction("fromMenu");
                startActivity(i);
                finish();
            }
        });
        LinearLayout favoritos = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_fav_ll);
        favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Charts.this, Favoritos.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout progreso = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_progreso_ll);
        progreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Charts.this, Charts.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout inicio = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_inicio_ll);
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(functions.shouldShowMenu()){
                    Intent i = new Intent(Charts.this, Home.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(Charts.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        LinearLayout sincro = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_sincro_ll);
        sincro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (prefs.contains("sincronizado")){
                    if (prefs.getBoolean("sincronizado", false)){
                        Intent i = new Intent(Charts.this, Sincro2.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Intent i = new Intent(Charts.this, Sincro.class);
                        startActivity(i);
                        finish();
                    }
                }
                else{
                    Intent i = new Intent(Charts.this, Sincro.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        LinearLayout compras = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_compras_ll);
        compras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new RecargarCompras(Charts.this);
            }
        });

        LinearLayout suscripcion = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_suscription_ll);
        suscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Charts.this, Suscripcion.class);
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
        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Charts.this, LogIn.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout news = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_news_ll);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Charts.this, Novedades.class);
                startActivity(i);
                finish();
            }
        });

        LinearLayout notalegal = menu_lateral.findViewById(R.id.id_menu_legal_ll);
        notalegal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // si esta registrado, va a la suscripcion. En caso contrario al login
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Config.url_nota_legal));
                startActivity(i);
            }
        });

        ImageView rs1 = menu_lateral.findViewById(R.id.id_rs1);
        rs1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                prefs.edit().putBoolean("Premios_9", true).commit();
                String urlPage = "https://www.youtube.com/channel/UCOKXZZHPxigzEvJPd8vGvNw";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
            }
        });
        ImageView rs2 = menu_lateral.findViewById(R.id.id_rs2);
        rs2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                prefs.edit().putBoolean("Premios_9", true).commit();
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
        rs3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                prefs.edit().putBoolean("Premios_9", true).commit();
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
        rs4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                prefs.edit().putBoolean("Premios_9", true).commit();
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
}
