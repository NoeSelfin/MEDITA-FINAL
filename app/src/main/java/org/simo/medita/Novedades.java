package org.simo.medita;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class Novedades extends Activity{
    protected SharedPreferences prefs;
    protected Functions functions;
    protected Typeface font;
    protected SlidingMenu menu_lateral;
    protected ImageView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_news);
        font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
        prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);

        LinearLayout atras = (LinearLayout) findViewById(R.id.id_opciones_atras);

        atras.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                menu_lateral.showMenu(true);
            }
        });


        prefs.edit().putBoolean("Premios_7", true).commit();
        functions = new Functions(this);

        setMenu();

        WebView mywebview = (WebView) findViewById(R.id.webViewNews);
        WebSettings webSettings = mywebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        WebViewClient webViewClient = new WebViewClient()
        {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                Log.i("medita_override1", Uri.parse(url).getHost());
                if (Uri.parse(url).getHost().equals("https://news.atentamente.net")){
                    view.loadUrl(url);
                    return true;
                }else{
                    view.stopLoading();
                    return false;
               }
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
            {
                Log.i("medita_override2", Uri.parse(request.getUrl().toString()).getHost());
                if (Uri.parse(request.getUrl().toString()).getHost().equals("news.atentamente.net")){
                    view.loadUrl(request.getUrl().toString());
                    return true;
                }else if (Uri.parse(request.getUrl().toString()).getHost().equals("news.atentamente.net")){
                    view.loadUrl(request.getUrl().toString());
                    return true;
                }
                else{
                    view.stopLoading();
                    return false;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                super.onPageFinished(view, url);
                //Toast.makeText(getApplicationContext(), "Page loaded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoadResource(WebView  view, String  url){
                if( url.equals("") ){
                    // do something
                }
            }
        };

        mywebview.setWebViewClient(webViewClient);
        mywebview.loadUrl("https://news.atentamente.net/");

        if(functions.shouldShowMenu()){
            functions.showMenu();
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(Novedades.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(i);
        finish();
    }

    public void setMenu(){
        menu_lateral = new SlidingMenu(Novedades.this);
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
                Intent i = new Intent(Novedades.this, Opciones.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout vision = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_vision_ll);
        vision.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Novedades.this, Vision.class);
                i.setAction("fromMenu");
                startActivity(i);
                finish();
            }
        });
        LinearLayout favoritos = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_fav_ll);
        favoritos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Novedades.this, Favoritos.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout progreso = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_progreso_ll);
        progreso.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Novedades.this, Charts.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout inicio = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_inicio_ll);
        inicio.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(functions.shouldShowMenu()){
                    Intent i = new Intent(Novedades.this, Home.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(Novedades.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        LinearLayout sincro = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_sincro_ll);
        sincro.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (prefs.contains("sincronizado")){
                    if (prefs.getBoolean("sincronizado", false)){
                        Intent i = new Intent(Novedades.this, Sincro2.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Intent i = new Intent(Novedades.this, Sincro.class);
                        startActivity(i);
                        finish();
                    }
                }
                else{
                    Intent i = new Intent(Novedades.this, Sincro.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        LinearLayout compras = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_compras_ll);
        compras.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new RecargarCompras(Novedades.this);
            }
        });

        LinearLayout suscripcion = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_suscription_ll);
        suscripcion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Novedades.this, Suscripcion.class);
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
                Intent i = new Intent(Novedades.this, LogIn.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout news = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_news_ll);
        news.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Novedades.this, Novedades.class);
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
                prefs.edit().putBoolean("Premios_9", true).commit();
                String urlPage = "https://www.youtube.com/channel/UCOKXZZHPxigzEvJPd8vGvNw";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
            }
        });
        ImageView rs2 = menu_lateral.findViewById(R.id.id_rs2);
        rs2.setOnClickListener(new OnClickListener() {
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
        rs3.setOnClickListener(new OnClickListener() {
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
        rs4.setOnClickListener(new OnClickListener() {
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



    protected void alert(String mens, String tit){

        final Dialog dialog = new Dialog(Novedades.this);
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
