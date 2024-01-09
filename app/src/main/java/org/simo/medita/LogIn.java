package org.simo.medita;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.extras.Basics;
import org.simo.medita.extras.HttpConnection;

public class LogIn extends Activity {

    protected SharedPreferences prefs;
    protected SlidingMenu menu_lateral;
    protected Typeface font;
    protected LinearLayout menu;
    private Button btCrearCuenta;
    private Button btIniciarSesion;
    private EditText etEmail;
    private EditText etPassword;
    private TextView tvRecoverPass;
    private TextView tvTitulo;

    protected boolean btnBusy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_log_in);

        prefs = getSharedPreferences(getString(R.string.sharedpref_name), Context.MODE_PRIVATE);
        font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
        menu = findViewById(R.id.id_login_menu);
        btCrearCuenta = findViewById(R.id.bt_crear_cuenta);
        btIniciarSesion = findViewById(R.id.bt_iniciar);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        tvRecoverPass = findViewById(R.id.tv_recovery_pass);
        tvTitulo = findViewById(R.id.id_titulo_acercade);
        tvTitulo.setTypeface(font);

        if (prefs.getBoolean(getString(R.string.registrado),false)){
            Intent i = new Intent(LogIn.this, Logout.class);
            startActivity(i);
            finish();
        }

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                menu_lateral.showMenu(true);
            }
        });
        btCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LogIn.this, Registro.class);
                startActivity(i);
                finish();
            }
        });
        btIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnBusy == false){
                    if(!TextUtils.isEmpty(etEmail.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
                        if (etPassword.getText().toString().length() < 4){
                            Basics.toastCentered(LogIn.this, "Password incorrecto.", Toast.LENGTH_LONG);
                        }else{
                            btnBusy = true;
                            new Downloader(LogIn.this, prefs)
                                    .login(Basics.checkConn(LogIn.this), etEmail.getText().toString(),
                                            etPassword.getText().toString(), new Downloader.Login.AsyncResponse() {
                                                @Override
                                                public void processFinish(String respuesta) {
                                                    if (respuesta!=null){
                                                        Log.i("medita", "login respuesta: " + respuesta);
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(respuesta);
                                                            if (jsonObject.optBoolean("logeado")){
                                                                JSONObject usuario = new JSONObject();
                                                                // {"registrado":true,"id_usuario":"8","plataforma":"android"}
                                                                usuario.put(getString(R.string.email_usuario), etEmail.getText().toString());
                                                                usuario.put(getString(R.string.id_usuario), jsonObject.optString("id_usuario"));
                                                                usuario.put(getString(R.string.plataforma), jsonObject.optString("plataforma"));

                                                                prefs.edit().putString(getString(R.string.user_info),usuario.toString()).commit();
                                                                Log.i(Config.tag,"usuario guardado en sharedpref: " + usuario.toString());
                                                                prefs.edit().putBoolean(getString(R.string.registrado), true).commit();
                                                                prefs.edit().putString("id_usuario", jsonObject.optString("id_usuario")).commit();
                                                                prefs.edit().putString("nombre_usuario", jsonObject.optString("nombre_usuario")).commit();

                                                                prefs.edit().putBoolean("Premios_8", true).commit();

                                                                //Si está suscrito voy a la home y si no a la pantalla de suscripción.
                                                                if(prefs.getBoolean(getString(R.string.suscrito), false)){
                                                                    Intent i = new Intent(getApplicationContext(), Home.class);
                                                                    startActivity(i);
                                                                    finish();
                                                                }else{
                                                                    new hasExternSuscriptionLogIn().execute();
                                                                    //menu_lateral.findViewById(R.id.id_menu_suscription_ll).performClick();
                                                                }

                                                            }else{
                                                                Basics.toastCentered(LogIn.this, "Error en el usuario o contraseña.", Toast.LENGTH_LONG);
                                                            }
                                                        } catch (JSONException e) {
                                                            //e.printStackTrace();
                                                            Basics.toastCentered(LogIn.this, "Error en el usuario o contraseña.", Toast.LENGTH_LONG);
                                                        }
                                                    }else{
                                                        Basics.toastCentered(LogIn.this, "Error en la respuesta del servidor.", Toast.LENGTH_LONG);
                                                    }
                                                    btnBusy = false;
                                                }
                                            });
                        }
                    }else{
                        Basics.toastCentered(LogIn.this, "Email incorrecto.", Toast.LENGTH_LONG);
                    }

                }

            }
        });
        tvRecoverPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LogIn.this, RecoveryPass.class);
                startActivity(i);
//                finish();
            }
        });
        setMenu();
    }
    private class hasExternSuscriptionLogIn extends AsyncTask<String, Void, String> {
        String id_user;
        @Override
        protected void onPreExecute() {
            Log.i("medita_susc","Buscando suscripciones externas.");

            id_user = prefs.getString("id_usuario", "");
            Log.i("medita_susc",id_user);
        }
        @Override
        protected String doInBackground(String... params) {
            String mac = Basics.getWifiMac(LogIn.this);
            String result = null;
            HttpConnection http = null;

            if (Basics.checkConn(LogIn.this)){
                try {
                    http = new HttpConnection();

                    JSONObject jo =  new JSONObject();
                    jo.put("token",Config.token);
                    jo.put("id_user",id_user);

                    http = new HttpConnection();
                    result = http.postData(Config.url_get_suscription_active, jo.toString());
                    if (Config.log){
                        if (result != null)
                            Log.i("medita_susc",result);
                    }
                } catch (JSONException e) {
                    Log.i("medita","Error descargando datos de suscripciones externas.");
                }
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            if ((result != null) && (result.compareTo("") != 0)){
                Log.i("medita_susc_result",result);
                try{
                    JSONArray ja = new JSONArray(result);
                    if(ja.length() > 0){
                        prefs.edit().putBoolean(LogIn.this.getString(R.string.suscrito_externo), true).commit();
                        prefs.edit().putBoolean(LogIn.this.getString(R.string.suscrito), true).commit();

                        Intent i = new Intent(getApplicationContext(), Home.class);
                        startActivity(i);
                        finish();

                    }else{
                        menu_lateral.findViewById(R.id.id_menu_suscription_ll).performClick();
                    }
                } catch (JSONException e) {
                    menu_lateral.findViewById(R.id.id_menu_suscription_ll).performClick();
                }
            }else{
                menu_lateral.findViewById(R.id.id_menu_suscription_ll).performClick();
            }
        }
    }

    public void setMenu(){
        menu_lateral = new SlidingMenu(LogIn.this);
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
        ((View) menu_lateral.findViewById(R.id.id_menu_view_compras)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_suscription)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_contact)).setVisibility(View.VISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_news)).setVisibility(View.INVISIBLE);


        LinearLayout inicio = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_inicio_ll);
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(LogIn.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout acercade = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_acercade_ll);
        acercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(LogIn.this, Acercade.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout opciones = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_opciones_ll);
        opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(LogIn.this, Opciones.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout vision = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_vision_ll);
        vision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(LogIn.this, Vision.class);
                i.setAction("fromMenu");
                startActivity(i);
                finish();
            }
        });
        LinearLayout favoritos = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_fav_ll);
        favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(LogIn.this, Favoritos.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout progreso = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_progreso_ll);
        progreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(LogIn.this, Charts.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout sincro = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_sincro_ll);
        sincro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (prefs.contains("sincronizado")){
                    if (prefs.getBoolean("sincronizado", false)){
                        Intent i = new Intent(LogIn.this, Sincro2.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Intent i = new Intent(LogIn.this, Sincro.class);
                        startActivity(i);
                        finish();
                    }
                }
                else{
                    Intent i = new Intent(LogIn.this, Sincro.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        LinearLayout compras = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_compras_ll);
        compras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new RecargarCompras(LogIn.this);
            }
        });

        LinearLayout suscripcion = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_suscription_ll);
        suscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // si esta registrado, va a la suscripcion. En caso contrario al login
                if (prefs.getBoolean(getString(R.string.registrado),false)){
                    Intent i = new Intent(LogIn.this, Suscripcion.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(LogIn.this, LogIn.class);
                    startActivity(i);
                    finish();
                }

            }
        });
        LinearLayout contacto = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_contact_ll);
        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(LogIn.this, LogIn.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout news = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_news_ll);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(LogIn.this, Novedades.class);
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

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(LogIn.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(i);
        finish();
    }
}
