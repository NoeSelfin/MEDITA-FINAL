package org.simo.medita;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.util.IabException;
import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;
import com.android.vending.billing.util.SkuDetails;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.extras.Basics;
import org.simo.medita.extras.HttpConnection;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class SuscripcionOld extends Activity implements IabHelper.OnIabSetupFinishedListener, IabHelper.OnIabPurchaseFinishedListener{

    protected SharedPreferences prefs;
    protected Typeface font;
    protected SlidingMenu menu_lateral;
    protected ImageView menu;
    protected TextView btn_suscripcion;
    protected boolean bloqueo = false;
    private IabHelper billingHelper;
    private TextView tvCancelar;
    private TextView tvNombreSuscripcion;

    private String Producto;
    private String tipoSuscripcion;
    private TextView tvCerrarSesion;
    private TextView tvTitulo;

    private JSONArray listaSuscripciones = new JSONArray();
//    private AdapterSuscripciones adapter;

    private Inventory subscriptionInventory;
    private boolean subGooglePlayUpdated = false;
    private boolean subDDBBUpdated = false;
    private JSONObject userInfo = null;
    private JSONObject subcripcionDDBB = null;

    private LinearLayout infoLinearLayout;
    private LinearLayout linearListaSuscripciones;
    private Button btMensual;
    private Button btSemestral;
    private Button btAnual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_suscripcion);
        font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
//        prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
        prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
        LinearLayout atras = (LinearLayout)findViewById(R.id.id_suscripcion_atras);

        tvTitulo = findViewById(R.id.id_titulo_suscripcion);
        tvTitulo.setTypeface(font);
        btn_suscripcion = (TextView) findViewById(R.id.id_susc_tv1);
        tvCancelar = findViewById(R.id.tv_cancelar);
        tvCancelar.setMovementMethod(LinkMovementMethod.getInstance());
        tvNombreSuscripcion = findViewById(R.id.tv_nombre_suscripcion);
        tvCerrarSesion = findViewById(R.id.id_cerrar_sesion);
        infoLinearLayout = findViewById(R.id.info_ll);
        linearListaSuscripciones = findViewById(R.id.ll_lista_suscripciones);

        setMenu();

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                menu_lateral.showMenu(true);
            }
        });
        btn_suscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                /*
                if (!bloqueo){
                    alertSuscripcion();
                }
                */
            }
        });

        tvCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // registrado = false y borrar info del usuario
                prefs.edit().putBoolean(getString(R.string.registrado), false).commit();
                prefs.edit().remove(getString(R.string.user_info)).commit();
                Intent i = new Intent(SuscripcionOld.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        setButtonsListListeners();

        try {
            userInfo = new JSONObject(prefs.getString(getString(R.string.user_info),""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        getSubscriptionsDatabaseInfo();

        //inicializamos el helper para obtener las suscripciones de GP
        billingHelper = new IabHelper(SuscripcionOld.this, Config.license_key);
        billingHelper.startSetup(this);
    }

    /*
     ** Method for releasing resources (dispose of object)
     */
    public void dispose() {
        if (billingHelper != null) {
            try {
                billingHelper.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
            billingHelper = null;
        }
    }

    @Override
    public void onIabSetupFinished(IabResult result) {

        printLog("onIabSetupFinished");
        printLog(result.getMessage());
        printLog(result.toString());

        if (billingHelper.subscriptionsSupported()){
            printLog("subscriptionsSupported == true");
        }else{
            printLog("subscriptionsSupported == false");
        }
        if (result.isFailure()) {
            printLog("Problem setting up In-app Billing: " + result);
            errorAlIniciar();
            dispose();
        }else{
            try {
                final String[] skuIds = getResources().getStringArray(R.array.suscriptions_sku);
                final List<String> skuIdsList = Arrays.asList(skuIds);
                billingHelper.queryInventoryAsync(true, skuIdsList, new IabHelper.QueryInventoryFinishedListener() {
                    @Override
                    public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                        if (result.isFailure()) {
                            printLog("Problem querying inventory: " + result);
                            dispose();
                            return;
                        }
                        subscriptionInventory = inventory;
                        // informacion de las suscripciones activas
                        List<Purchase> purchaseList = inventory.getAllPurchases();
                        for (Purchase p : purchaseList){
                            printLog("PURCHASE");
                            printLog("getOrderId: " + p.getOrderId());
                        }

                        // todo ir a metodo para iniciar la lista o bien para mostrar el texto correspondiente
                        JSONArray jsonArray = new JSONArray(subscriptionInventory.getAllOwnedSkus());
                        prefs.edit().putString(getString(R.string.skus_activos), jsonArray.toString()).commit();

                        subGooglePlayUpdated = true;
                        checkAllDownload();
                    }
                });
            } catch (Exception e) {
//        } catch (IabHelper.IabAsyncInProgressException e) {
                printLog("EXCEPTION:" + e.getMessage());
            }
        }
    }

    private void getSubscriptionsDatabaseInfo(){
        printLog("id_usuario " + userInfo.optString("id_usuario"));
        new Downloader.GetSuscripciones(Basics.checkConn(this), userInfo.optString("id_usuario"),
                new Downloader.GetSuscripciones.AsyncResponse() {
                    @Override
                    public void processFinish(String respuesta) {
                        subDDBBUpdated = true;
                        subcripcionDDBB = null;
                        if (respuesta != null){
                            printLog("GetSuscripciones respuesta: " + respuesta);
                            try {
                                JSONObject jsonObject = new JSONObject(respuesta);
                                if (jsonObject.optBoolean("existe_suscripcion")){
                                    subcripcionDDBB = new JSONObject(respuesta);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        checkAllDownload();
                    }
                }).execute();
    }

    private void checkAllDownload(){

        printLog("checkAllDownload");
        if (subGooglePlayUpdated && subDDBBUpdated){
            printLog("checkAllDownload true & true");
            subGooglePlayUpdated = false;
            subDDBBUpdated = false;
            // comprobamos que la suscripcion de la DDBB no sea nula y el email de cuenta y de suscripción sean iguales.
            if (subcripcionDDBB == null || subcripcionDDBB.optString("email_usuario").compareToIgnoreCase(userInfo.optString("email_usuario"))==0){
                initListView();
            } else {
                // mostramos texto de cuenta != google play
                btn_suscripcion.setText(getString(R.string.subscription_bad_account));
                btn_suscripcion.setTypeface(btn_suscripcion.getTypeface(), Typeface.BOLD);
                tvNombreSuscripcion.setVisibility(View.GONE);
                tvCancelar.setVisibility(View.GONE);
            }
        }
    }

    /** Mostramos si tenemos suscripción activa. En caso negativo mostramos una lista de las suscripciones.
     *
     */
    private void initListView(){

        printLog("initListView");
        final String[] skuIds = getResources().getStringArray(R.array.suscriptions_sku);
        String[] skuNames = getResources().getStringArray(R.array.suscriptions_type);
        try {
            JSONArray skuActivos = new JSONArray(subscriptionInventory.getAllOwnedSkus());
            // inicializamos nombre de suscripcion a null
            tipoSuscripcion = null;
            // punto 1, comprobar si la suscripcion en Google Play esta activa
            // si hay sku activos en Google Play
            if (skuActivos.length() > 0){
//            if (skuActivos.length() == 0){
                printLog("hay skus activos de compra o suscripcion");
                // recorremos los sku activos
                for (int i = 0; i < skuActivos.length(); i++) {
                    //recorremos la lista de sku guardada en el dispositivo
                    for (int j = 0; j < skuIds.length; j++) {
                        // si un sku del inventory es iguale a alguno deseado
                        if (skuActivos.optString(i).compareToIgnoreCase(skuIds[j]) == 0){
                            // obtenemos los nombres de las suscripciones para poner en el texto
                            tipoSuscripcion = skuNames[j];
                        }
                    }
                }
            }
            // punto 2. si no hay suscripcion activa en Google Play, comprobar la base de datos
            // si no hay sku activos en Google Play y si lo hay en la ddbb
            if (tipoSuscripcion == null && subcripcionDDBB != null){
                //recorremos la lista de sku guardada en el dispositivo
                for (int j = 0; j < skuIds.length; j++) {
                    // si un sku del inventory es iguale a alguno deseado
                    if (subcripcionDDBB.optString("productId").compareToIgnoreCase(skuIds[j]) == 0){
                        try {
                            // comprobamos la fecha de finalización de la suscripción
                            if (Basics.checkFechaFin(subcripcionDDBB.optString("fecha_fin_suscripcion"))){
                                // obtenemos el nombre de la suscripcion para poner en el texto
                                tipoSuscripcion = skuNames[j];
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            // si hay nombre de suscripcion (ya sea de Google Play o de la DDBB)
            if (tipoSuscripcion != null) {
                printLog("hay skus activos de tipo -> " + tipoSuscripcion);
                String info_suscripcion = getString(R.string.suscripcion_activa);
                btn_suscripcion.setText(info_suscripcion);
                tvNombreSuscripcion.setText(tipoSuscripcion);
                tvNombreSuscripcion.setVisibility(View.VISIBLE);
                infoLinearLayout.setVisibility(View.GONE);
                // escondemos la lista de suscripciones
                linearListaSuscripciones.setVisibility(View.GONE);
            } else {
                // añadimos la informacion de las suscripciones para mostrarlas
                printLog("sin skus activos de tipo suscripcion (puede haber de compra)");
                tvNombreSuscripcion.setVisibility(View.GONE);
                for (int i = 0; i < skuIds.length; i++) {
                    // obtenemos los detalles de la suscripcion
                    SkuDetails details = subscriptionInventory.getSkuDetails(skuIds[i]);
                    JSONObject item  = new JSONObject();
                    item.put("product",skuNames[i]);
                    item.put("price",details.getPrice());
                    item.put("sku",skuIds[i]);
                    listaSuscripciones.put(item);
                }
                setButtonsListText();
//                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setButtonsListText() throws JSONException {

        btMensual.setText("SUSCRIPCIÓN " +
                listaSuscripciones.getJSONObject(0).getString("product").toUpperCase() +
                " " +
                listaSuscripciones.getJSONObject(0).getString("price"));
        btMensual.setTypeface(font);
        btSemestral.setText("SUSCRIPCIÓN " +
                listaSuscripciones.getJSONObject(1).getString("product").toUpperCase() +
                " " +
                listaSuscripciones.getJSONObject(1).getString("price"));
        btSemestral.setTypeface(font);
        btAnual.setText("SUSCRIPCIÓN " +
                listaSuscripciones.getJSONObject(2).getString("product").toUpperCase() +
                " " +
                listaSuscripciones.getJSONObject(2).getString("price"));
        btAnual.setTypeface(font);
    }

    private void setButtonsListListeners(){
        btMensual = findViewById(R.id.sus_1);
        btSemestral = findViewById(R.id.sus_2);
        btAnual = findViewById(R.id.sus_3);
        btMensual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Producto = listaSuscripciones.optJSONObject(0).optString("sku");
                tryPurchase();
            }
        });
        btSemestral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Producto = listaSuscripciones.optJSONObject(1).optString("sku");
                tryPurchase();
            }
        });
        btAnual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Producto = listaSuscripciones.optJSONObject(2).optString("sku");
                tryPurchase();
            }
        });
    }

    private void tryPurchase(){
        try {
            // si no se ha comprado el producto
            if (!billingHelper.queryInventory(true,null,null).hasPurchase(Producto)){
                printLog("no ha comprado el producto. Procediendo a la compra");
                purchaseItem(Producto);
            }
        } catch (IabException e) {
            e.printStackTrace();
        }
    }

    /** Guardas suscripcion
     *
     * @param p
     * @throws JSONException
     */
    private void saveSubscription(Purchase p) throws JSONException {
        JSONObject original = new JSONObject(p.getOriginalJson());
        new Downloader.SaveSuscription(Basics.checkConn(SuscripcionOld.this),
                prefs.getString(getString(R.string.id_usuario), ""),
                original.optString("productId"),
                original.optString("purchaseToken"),
                original.optString("orderId"),
                original.optString("purchaseTime"),
                original.optBoolean("autoRenewing"),
                Basics.getCancelDate(SuscripcionOld.this, p.getSku(), p.getPurchaseTime()),
                new Downloader.SaveSuscription.AsyncResponse() {
                    @Override
                    public void processFinish(String respuesta) {
                        if (respuesta!=null){
                            printLog("SaveSuscription respuesta: " + respuesta);
                        }
                    }
                }).execute();
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(SuscripcionOld.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        startActivity(i);
        finish();
    }

    public void setMenu(){
        menu_lateral = new SlidingMenu(SuscripcionOld.this);
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

        ((View) menu_lateral.findViewById(R.id.id_menu_view_ini)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_fav)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_progreso)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_acercade)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_vision)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_opciones)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_sincro)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_sincro)).setVisibility(View.INVISIBLE);
        ((View) menu_lateral.findViewById(R.id.id_menu_view_suscription)).setVisibility(View.VISIBLE);

        LinearLayout acercade = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_acercade_ll);
        acercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(SuscripcionOld.this, Acercade.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout opciones = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_opciones_ll);
        opciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(SuscripcionOld.this, Opciones.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout vision = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_vision_ll);
        vision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(SuscripcionOld.this, Vision.class);
                i.setAction("fromMenu");
                startActivity(i);
                finish();
            }
        });
        LinearLayout favoritos = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_fav_ll);
        favoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(SuscripcionOld.this, Favoritos.class);
                startActivity(i);
                finish();
            }
        });

        LinearLayout inicio = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_inicio_ll);
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(SuscripcionOld.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout progreso = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_progreso_ll);
        progreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(SuscripcionOld.this, Progreso.class);
                startActivity(i);
                finish();
            }
        });
        LinearLayout compras = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_compras_ll);
        compras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

            }
        });
        LinearLayout suscripcion = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_suscription_ll);
        suscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // si esta registrado, va a la suscripcion. En caso contrario al login
                if (!prefs.getBoolean(getString(R.string.registrado),false)){
                    Intent i = new Intent(SuscripcionOld.this, LogIn.class);
                    startActivity(i);
                    finish();
                }
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
    }
    protected void errorAlIniciar() {
        Toast.makeText(SuscripcionOld.this, "Error al intentar iniciar la compra", Toast.LENGTH_SHORT).show();
    }

    protected void purchaseItem(String sku) {
        //billingHelper.launchSubscriptionPurchaseFlow(this, sku, 541, this);
        billingHelper.launchSubscriptionPurchaseFlow(this, sku, 541, this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase info) {
        if (result.isFailure()) {
            compraFallida();
        } else if (Producto.equals(info.getSku())) {
            try {
                saveSubscription(info);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            compraCorrecta(result, info);
        }
    }

    /*
     * COSAS QUE QUERAMOS HACER CUANDO SE HAYA
     * ADQUIRIDO EL PRODUCTO CON EXITO
     */
    protected void compraCorrecta(IabResult result, Purchase info){
        // Consumimos los elementos a fin de poder probar varias compras
        // billingHelper.consumeAsync(info, null);

        Intent i = new Intent(SuscripcionOld.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }
    /*
     * COSAS QUE QUERAMOS HACER CUANDO EL PRODUCTO
     * NO HAYA SIDO ADQUIRIDO
     */

    protected void compraFallida(){
        alert("Ha habido un error con su compra.");
    }
    //LIMPIAMOS
    @Override
    protected void onDestroy() {
        disposeBillingHelper();
        super.onDestroy();
    }

    private void disposeBillingHelper() {
        if (billingHelper != null) {
            billingHelper.dispose();
        }
        billingHelper = null;
    }

    protected void alert(String mensaje){

        final Dialog dialog = new Dialog(SuscripcionOld.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_generico);
        dialog.setCancelable(true);

        // set the custom dialog components - text, image and button
        TextView ver = (TextView) dialog.findViewById(R.id.id_alert_btn);
        TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
        TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);

        titulo.setText("Sincronizar");
        text.setText(mensaje);


        text.setTypeface(font);
        ver.setTypeface(font);
        titulo.setTypeface(font);

        // if button is clicked, close the custom dialog
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    protected void alertSuscripcion(){

        final Dialog dialog = new Dialog(SuscripcionOld.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_registro_sync);
        dialog.setCancelable(true);

        // set the custom dialog components - text, image and button
        TextView ver = (TextView) dialog.findViewById(R.id.id_alert_btn);
        //TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
        TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);
        final CheckBox check = (CheckBox) dialog.findViewById(R.id.checkBoxCustomized);

        final TextView email = (TextView) dialog.findViewById(R.id.id_alert_editext);

        TextView check_text = (TextView) dialog.findViewById(R.id.checkBoxCustomized_text);

        //text.setTypeface(font);
        ver.setTypeface(font);
        titulo.setTypeface(font);
        check_text.setTypeface(font);

        check_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // if button is clicked, close the custom dialog
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.isChecked()){
                    if (Basics.checkConn(SuscripcionOld.this)){
                        new setRegistro().execute(email.getText().toString());
                    }
                    else{
                        alert("No hay conexi?n a Internet.");
                    }
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(SuscripcionOld.this, "Ha de aceptar la pol?tica de privacidad", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();
    }

    private class setRegistro extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            bloqueo = true;

        }
        @Override
        protected String doInBackground(String... params) {
            String mac = Basics.getWifiMac(SuscripcionOld.this);
            HttpConnection http = null;
            String result="";
            try {
                http = new HttpConnection();

                JSONObject jo =  new JSONObject();
                jo.put("token",Config.token);
                jo.put("mac",mac);
                jo.put("plataforma","Android");
                jo.put("email",params[0]);
                jo.put("hash","");

                http = new HttpConnection();
                result = http.postData(Config.url_set_registro, jo.toString());

                if (result != null)
                    printLog("setRegistro result: " + result);


            } catch (JSONException e) {
            }


            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            bloqueo = false;
            if(result != null){
                if ((result.trim().compareTo("4") == 0)||(result.trim().compareTo("0") == 0)){

                    prefs.edit().putBoolean("sincronizado", true).commit();

                    Intent i = new Intent(SuscripcionOld.this, Sincro2.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(i);
                    finish();
                }
                else if(result.trim().compareTo("3")==0){
                    alert("Ha ocurrido un error.Int?ntelo m?s tarde.");
                }
                else if(result.trim().compareTo("2")==0){
                    alert("Ha ocurrido un error. Int?ntelo m?s tarde.");
                }
                else if(result.trim().compareTo("-1")==0){
                    alert("Ha ocurrido un error. Int?ntelo m?s tarde.");
                }
                else if(result.trim().compareTo("1")==0){
                    alert("Ha ocurrido un error. Int?ntelo m?s tarde.");
                }
                else{
                    alert("Ha ocurrido un error. Int?ntelo m?s tarde.");
                }

            }
            else{
                alert("Ha ocurrido un error.Int?ntelo m?s tarde.");
            }
        }
    }
    private class setCodigo extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            bloqueo = true;

        }
        @Override
        protected String doInBackground(String... params) {
            String mac = Basics.getWifiMac(SuscripcionOld.this);
            HttpConnection http = null;
            String result="";
            try {
                http = new HttpConnection();

                JSONObject jo =  new JSONObject();
                jo.put("token",Config.token);
                jo.put("mac",mac);
                jo.put("plataforma","Android");
                jo.put("email",params[0]);
                jo.put("codigo",params[1]);

                http = new HttpConnection();
                result = http.postData(Config.url_set_code, jo.toString());

                if (result != null)
                    printLog("setCodigo result: " + result);


            } catch (JSONException e) {
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            bloqueo = false;
            if(result != null){
                if ((result.trim().compareTo("3") == 0) || (result.trim().compareTo("4") == 0)){
                    prefs.edit().putBoolean("sincronizado", true).commit();
                    Intent i = new Intent(SuscripcionOld.this, Sincro2.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    startActivity(i);
                    finish();
                }
                else if(result.trim().compareTo("2")==0){
                    alert("Ha ocurrido un error. Int?ntelo m?s tarde.");
                }
                else if(result.trim().compareTo("1")==0){
                    alert("Ya hay una cuenta asociada para esta plataforma.");
                }
                else if(result.trim().compareTo("-1")==0){
                    alert("Ha ocurrido un error.Int?ntelo m?s tarde.");
                }
                else if(result.trim().compareTo("0")==0){
                    alert("Ha ocurrido un error. Int?ntelo m?s tarde.");
                }
                else{
                    alert("Ha ocurrido un error. Int?ntelo m?s tarde.");
                }
            }
            else{
                alert("Ha ocurrido un error. Int?ntelo m?s tarde.");
            }
        }
    }

    private void printLog(String text){
        if (Config.log){
            Log.i(Config.tag, text);
        }
    }
}


