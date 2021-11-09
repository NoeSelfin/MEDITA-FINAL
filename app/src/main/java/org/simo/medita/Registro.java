package org.simo.medita;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.extras.Basics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends Activity {

    protected SharedPreferences prefs;
    protected Typeface font;
    private LinearLayout menu;
    private SlidingMenu menu_lateral;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etRepeatPassword;
    private CheckBox cbCondiciones;
    private CheckBox cbNewsletter;
    private Button btRegistrarse;
    private TextView tvCondiciones;
    private TextView tvNewsletter;
    private TextView tvTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registro);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        TextView tvPolitica = findViewById(R.id.tv_politica);
        tvPolitica.setMovementMethod(LinkMovementMethod.getInstance());

        prefs = getSharedPreferences(getString(R.string.sharedpref_name), Context.MODE_PRIVATE);
        font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
        menu = findViewById(R.id.id_registro_menu);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etRepeatPassword = findViewById(R.id.et_repeat_password);
        cbCondiciones = findViewById(R.id.cb_condiciones);
        cbNewsletter = findViewById(R.id.cb_newsletter);
        btRegistrarse = findViewById(R.id.bt_registrar);
        tvCondiciones = findViewById(R.id.tv_condiciones);
        tvTitulo = findViewById(R.id.id_titulo_registro);
        tvTitulo.setTypeface(font);
        tvNewsletter = findViewById(R.id.tv_newsletter);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                menu_lateral.showMenu(true);
            }
        });
        btRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfo();
            }
        });
        setMenu();

        tvCondiciones.setMovementMethod(LinkMovementMethod.getInstance());
        tvNewsletter.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void checkInfo(){
        if (etEmail.getText().length() == 0 || etPassword.getText().length() == 0 || etRepeatPassword.getText().length() == 0){
            Basics.toastCentered(Registro.this, getString(R.string.faltan_datos), Toast.LENGTH_LONG);
//                    Toast.makeText(Registro.this, getString(R.string.faltan_datos), Toast.LENGTH_LONG).show();
        }else if (!isValidEmail(etEmail.getText().toString())){
            Basics.toastCentered(Registro.this, getString(R.string.invalid_email), Toast.LENGTH_LONG);
//                    Toast.makeText(Registro.this, getString(R.string.invalid_email),Toast.LENGTH_LONG).show();
        }else if (!checkPassword(etPassword.getText().toString(), etRepeatPassword.getText().toString())){
            // comprobación de password. los Toast de respuesta vienen en el propio método
        }else if (!cbCondiciones.isChecked()){
            Basics.toastCentered(Registro.this, getString(R.string.faltan_condiciones), Toast.LENGTH_LONG);
//                    Toast.makeText(Registro.this, getString(R.string.faltan_condiciones), Toast.LENGTH_LONG).show();
        }else{
            new Downloader(Registro.this, prefs).registrar(Basics.checkConn(Registro.this),
                    etEmail.getText().toString(), etPassword.getText().toString(),
                    new Downloader.Registrar.AsyncResponse() {
                        @Override
                        public void processFinish(String respuesta) {
                            printLog("registro respuesta: " + respuesta);
                            if (respuesta!=null){
                                try {
                                    JSONObject jsonObject = new JSONObject(respuesta);
                                    if (jsonObject.optBoolean("registrado")){

                                        // {"registrado":true,"id_usuario":"8","plataforma":"android"}
                                        JSONObject usuario = new JSONObject();
                                        usuario.put(getString(R.string.email_usuario), etEmail.getText().toString());
                                        usuario.put(getString(R.string.id_usuario), jsonObject.optString("id_usuario"));
                                        usuario.put(getString(R.string.plataforma), jsonObject.optString("plataforma"));

                                        prefs.edit().putString(getString(R.string.user_info),usuario.toString()).commit();
                                        prefs.edit().putBoolean(getString(R.string.registrado), true).commit();
                                        Basics.toastCentered(Registro.this, "Registro realizado correctamente.", Toast.LENGTH_LONG);
                                        if (cbNewsletter.isChecked()){
                                            setNewsletter();
                                        }
                                        menu_lateral.findViewById(R.id.id_menu_suscription_ll).performClick();


                                    }else{
                                        Basics.toastCentered(Registro.this, "El usuario ya existe.", Toast.LENGTH_LONG);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Basics.toastCentered(Registro.this, "Error en la respuesta del servidor.", Toast.LENGTH_LONG);
                            }
                        }
                    });
        }

    }

    /** Devuelve true si las comprovaciones de contraseña son todas correctas.
     *
     * @param password
     * @param password2
     * @return
     */
    public boolean checkPassword(String password, String password2){

        if (password.length() < 4){
            Basics.toastCentered(Registro.this, getString(R.string.short_pass), Toast.LENGTH_LONG);
//            Toast.makeText(Registro.this, getString(R.string.short_pass), Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.length() > 20){
            Basics.toastCentered(Registro.this, getString(R.string.long_pass), Toast.LENGTH_LONG);
//            Toast.makeText(Registro.this, getString(R.string.long_pass), Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.compareToIgnoreCase(password2) != 0){
            Basics.toastCentered(Registro.this, getString(R.string.different_pass), Toast.LENGTH_LONG);
//            Toast.makeText(Registro.this, getString(R.string.different_pass), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(Registro.this, LogIn.class);
        i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(i);
        finish();
    }

    public void setMenu(){
        menu_lateral = new SlidingMenu(Registro.this);
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
        ((TextView) menu_lateral.findViewById(R.id.id_menu_news)).setTypeface(font);


        ((View) menu_lateral.findViewById(R.id.id_menu_view_ini)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_fav)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_progreso)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_acercade)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_vision)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_opciones)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_sincro)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_compras)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_suscription)).setVisibility(View.VISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_news)).setVisibility(View.INVISIBLE);



        LinearLayout inicio = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_inicio_ll);
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Registro.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        LinearLayout acercade = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_acercade_ll);
        acercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Registro.this, Acercade.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout opciones = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_opciones_ll);
        opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Registro.this, Opciones.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout vision = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_vision_ll);
        vision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Registro.this, Vision.class);
                i.setAction("fromMenu");
                startActivity(i);
                finish();
            }
        });
        LinearLayout favoritos = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_fav_ll);
        favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Registro.this, Favoritos.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout progreso = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_progreso_ll);
        progreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Registro.this, Progreso.class);
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
                        Intent i = new Intent(Registro.this, Sincro2.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Intent i = new Intent(Registro.this, Sincro.class);
                        startActivity(i);
                        finish();
                    }
                }
                else{
                    Intent i = new Intent(Registro.this, Sincro.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        LinearLayout compras = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_compras_ll);
        compras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new RecargarCompras(Registro.this);
            }
        });

        LinearLayout suscripcion = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_suscription_ll);
        suscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // si esta registrado, va a la suscripcion. En caso contrario al login
                if (prefs.getBoolean(getString(R.string.registrado),false)){
                    Intent i = new Intent(Registro.this, Suscripcion.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(Registro.this, LogIn.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        LinearLayout contacto = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_contact_ll);
        contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Registro.this, Contacto.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout news = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_news_ll);
        news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Registro.this, Novedades.class);
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
                String urlPage = "https://www.youtube.com/channel/UCOKXZZHPxigzEvJPd8vGvNw";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
            }
        });
        ImageView rs2 = menu_lateral.findViewById(R.id.id_rs2);
        rs2.setOnClickListener(new View.OnClickListener() {
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
        rs3.setOnClickListener(new View.OnClickListener() {
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
        rs4.setOnClickListener(new View.OnClickListener() {
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

    public boolean isValidEmail(CharSequence email) {
        String pat = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
                Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public void setNewsletter(){
        new Downloader.setNewsletter(Basics.checkConn(Registro.this),
                etEmail.getText().toString(),
                etEmail.getText().toString(),
                Basics.getWifiMac(Registro.this),
                new Downloader.setNewsletter.AsyncResponse() {
                    @Override
                    public void processFinish(String respuesta) {
                        if (respuesta!=null){
                            printLog("respuesta alta newsletter -> " + respuesta);
                        }
                    }
                }).execute();
    }

    private void printLog(String text){
        if (Config.log){
            Log.i(Config.tag, text);
        }
    }
}
