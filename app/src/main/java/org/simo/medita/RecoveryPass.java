package org.simo.medita;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.extras.Basics;

public class RecoveryPass extends Activity {

    protected LinearLayout menu;
    protected EditText etEmail;
    protected Button btRecuperar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery_pass);

        menu = findViewById(R.id.id_back);
        etEmail = findViewById(R.id.et_email);
        btRecuperar = findViewById(R.id.bt_recuperar);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(etEmail.getText().toString()) && Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
                    new Downloader.RecuperarPass(Basics.checkConn(RecoveryPass.this),
                            etEmail.getText().toString(), new Downloader.RecuperarPass.AsyncResponse() {
                        @Override
                        public void processFinish(String respuesta) {
                            if (respuesta!=null){
                                try {
                                    JSONObject object = new JSONObject(respuesta);
                                    if (object.optBoolean("respuesta") && !object.optBoolean("error")){
                                        alert(getString(R.string.recovery_pass_peticion));
                                    } else if (!object.optBoolean("respuesta") && !object.optBoolean("error")) {
                                        alert(getString(R.string.recovery_pass_wrong_email));
                                    } else {
                                        alert(getString(R.string.error_respuesta_servidor));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).execute();
                }else{
                    Basics.toastCentered(RecoveryPass.this, "Email incorrecto.", Toast.LENGTH_LONG);
                }

            }
        });
    }

    protected void alert(String mens){

        Typeface font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");

        final Dialog dialog = new Dialog(RecoveryPass.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_generico);
        dialog.setCancelable(false);

        // set the custom dialog components - text, image and button
        TextView close = (TextView) dialog.findViewById(R.id.id_alert_btn);
        TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
        TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);

        titulo.setVisibility(View.GONE);
        text.setText(mens);

        text.setTypeface(font);
        close.setTypeface(font);
        titulo.setTypeface(font);

        // if button is clicked, close the custom dialog
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                onBackPressed();
            }
        });

        dialog.show();
    }
}
