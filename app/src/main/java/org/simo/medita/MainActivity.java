package org.simo.medita;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Inventory;
import com.android.vending.billing.util.Purchase;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.extras.Basics;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
	protected SharedPreferences prefs;
	protected ImageView menu;
	protected TextView inicio;
	protected Typeface font;
	protected ImageView play;
	protected TextView song_name;
	protected TextView song_continue;
	protected ImageView loading;
	protected RelativeLayout reproducir;

	protected SlidingMenu menu_lateral;

	protected AdapterPacks adapterpacks;
	protected static JSONArray packs;
	protected ListView listview;

	//protected boolean pagado = false;
    private IabHelper iabHelper;

    private boolean suscripcionesGoogle = false;
    private boolean suscripcionesDDBB = false;
	private JSONObject suscripcionDDBB = null;
	private Inventory subscriptionInventory;
	private List<Purchase> ownedSubscriptions = new ArrayList<>();

    private JSONObject userInfo = null;
    private boolean loged = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
//		prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);

		menu = (ImageView)findViewById(R.id.id_main_hamburguesa);
		inicio = (TextView)findViewById(R.id.id_main_inicio);
		inicio.setTypeface(font);
		play = (ImageView)findViewById(R.id.id_main_play);
		song_name = (TextView)findViewById(R.id.id_main_titulo);
		song_name.setTypeface(font);
		song_continue = (TextView)findViewById(R.id.id_main_continuar);
		song_continue.setTypeface(font);
		loading = (ImageView)findViewById(R.id.id_loading);
		listview  = (ListView)findViewById(R.id.id_listview);
		reproducir  = (RelativeLayout)findViewById(R.id.id_main_reproducir);

		/*if (Reproductor.mp != null){
			if (Reproductor.mp.isPlaying())
				Reproductor.mp.stop();
		}*/

		packs = new JSONArray();
		adapterpacks = new AdapterPacks(this, packs);

		Intent i = getIntent();

		if (i.getBooleanExtra("fromSuscription",false) == true){
			alertNewsletter();
		}

		if(i != null) {
			if (prefs.contains("packs")){
				printLog("contains packs");
				if (prefs.contains("version")){
					printLog(String.valueOf(prefs.getInt("server_version",0)));
					printLog(String.valueOf(prefs.getInt("version",0)));

					// comprobamos si se ha mostrado el tutorial actualizado
					if (!prefs.contains("updated_tutorial") || !prefs.getBoolean("updated_tutorial", false)){
						printLog("actualizando... mostrando tutorial");
						// marcamos que se ha mostrado el tutorial actualizado y lo mostramos
						prefs.edit().putBoolean("updated_tutorial", true).commit();
						Intent intentTutorial = new Intent (MainActivity.this, Tutorial.class);
						startActivity(intentTutorial);
					} else {	// si se ha mostrado el tutorial, comprobamos los dias
						boolean suscrito = prefs.getBoolean(getString(R.string.suscrito), false);
						if (!suscrito){
							// obtenemos la fecha que mostramos el tutorial
							long fecha_tutorial = prefs.getLong("fecha_tutorial",0);
							// obtenemos los boolean que marcan si se han mostrado los popup correspondiente
							boolean fecha_tutorial_5 = prefs.getBoolean("fecha_tutorial_5",false);
							boolean fecha_tutorial_10 = prefs.getBoolean("fecha_tutorial_10",false);
							// obtenemos la fecha del tutorial + 5 y 10 dias
							long fecha_5 = Basics.daysAfterDate(fecha_tutorial, 5);
							long fecha_10 = Basics.daysAfterDate(fecha_tutorial, 10);
							// si estamos 5 dias despues de mostrar el tutorial y este no se ha mostrado
							if (!fecha_tutorial_5 && Basics.compareDateDMY(System.currentTimeMillis(), fecha_5)){
								// mostramos el popup correspondiente
								popup2Btn(getString(R.string.alert_dias_tutorial),
										getString(R.string.subscribe_now),
										getString(R.string.subscribe_not_now));
							}
							// si estamos 10 dias despues de mostrar el tutorial y este no se ha mostrado
							if (!fecha_tutorial_10 && Basics.compareDateDMY(System.currentTimeMillis(), fecha_10)){
								// mostramos el popup correspondiente
								popup2Btn(getString(R.string.alert_dias_tutorial),
										getString(R.string.subscribe_now),
										getString(R.string.subscribe_not_now));
							}
						}
					}
                    //printLog("Server version: "+prefs.getInt("server_version",0));
                    //printLog("Server version: "+prefs.getInt("version",0));
					if (prefs.getInt("server_version",0) > prefs.getInt("version",0)){
						//if (prefs.contains("isNoFirstTime")){

							new Downloader(this,prefs,loading,1).downloadData(reproducir, adapterpacks, listview, prefs.getInt("server_version",0));
							printLog("update 1");
						//}
						//else{
							prefs.edit().putInt("version", prefs.getInt("server_version",0)).commit();
							prefs.edit().putBoolean("isNoFirstTime", true).commit();
							prefs.edit().putBoolean("showNewContent", true).commit();

						//}
					}
					else{
						try {
							packs = new JSONArray(prefs.getString("packs", ""));
				           	loading.setVisibility(View.INVISIBLE);
				           	reproducir.setVisibility(View.VISIBLE);
				           	adapterpacks = new AdapterPacks(this, packs);
							listview.setAdapter(adapterpacks);
							adapterpacks.notifyDataSetChanged();

							if (!prefs.contains("disclaimer")){
								disclaimer();
							} else {
								printLog("checkpoint 1");
								// si no esta suscrito
								if (!prefs.getBoolean(getString(R.string.suscrito), false)){
									// comprobamos la fecha de hoy y mostramos popup informando de las suscripciones
									checkDay();
								}

								/*if(prefs.getBoolean("Premios_6", false)){
									//Lanzamos popup de finalización
									if(prefs.getBoolean("day_21_show_msg", false)){
									}else{
										String tit = "Medita";
										String mens = "Enhorabuena! has completado la última semana del plan de 21 días. Te invitamos a suscribirte para cceder a la biblioteca completa de contenidos si no lo has hecho ya.";
										alertFinishFirstPack(mens, tit);
										//Ya no lo volvemos a poner
										prefs.edit().putBoolean("day_21_show_msg", true).commit();
									}

								}else if(prefs.getBoolean("Premios_5", false)){
									//Lanzamos popup de finalización
									if(prefs.getBoolean("day_14_show_msg", false)){

									}else{
										String tit = "Medita";
										String mens = "Enhorabuena! has completado la última semana del plan de 21 días. Puedes seguir con la siguiente semana. Te invitamos a suscribirte para cceder a la biblioteca completa de contenidos si no lo has hecho ya.";
										alertFinishFirstPack(mens, tit);
										//Ya no lo volvemos a poner
										prefs.edit().putBoolean("day_14_show_msg", true).commit();
									}

								}else if(prefs.getBoolean("Premios_4", false)){
									//Lanzamos popup de finalización
									if(prefs.getBoolean("day_7_show_msg", false)){

									}else{
										String tit = "Medita";
										String mens = "Enhorabuena! has completado la última semana del plan de 21 días. Puedes seguir con la siguiente semana. Te invitamos a suscribirte para cceder a la biblioteca completa de contenidos si no lo has hecho ya.";
										alertFinishFirstPack(mens, tit);
										//Ya no lo volvemos a poner
										prefs.edit().putBoolean("day_7_show_msg", true).commit();
									}

								}*/
							}

							/*if (!prefs.contains("showNewContent")){
							} else {
								if (prefs.getBoolean("showNewContent",false)){
									alert_meditaciones_nuevas();
								}
							}*/



						} catch (JSONException e) {
							new Downloader(this,prefs,loading,1).downloadData(reproducir, adapterpacks, listview);
							printLog("update 2");
						}

					}
				}
				else{
					try {
						prefs.edit().putInt("version", prefs.getInt("server_version",0)).commit();
						packs = new JSONArray(prefs.getString("packs", ""));
			           	loading.setVisibility(View.INVISIBLE);
			           	reproducir.setVisibility(View.VISIBLE);
			           	adapterpacks = new AdapterPacks(this, packs);
						listview.setAdapter(adapterpacks);
						adapterpacks.notifyDataSetChanged();

						if (!prefs.contains("disclaimer")){
							disclaimer();
						} else {
							printLog("checkpoint 2");
							// newPopup(getString(R.string.popup_nuevo_titulo),getString(R.string.popup_nuevo_text));
							if (!prefs.getBoolean(getString(R.string.suscrito), false)){
								checkDay();
							}
						}
					} catch (JSONException e) {
						new Downloader(this,prefs,loading,1).downloadData(reproducir, adapterpacks, listview);
						printLog("update 3");
					}
				}
			}
			else{
				if (Basics.checkConn(this)){
					new Downloader(this,prefs,loading,1).downloadData(reproducir, adapterpacks, listview);
					printLog("update 4");
				}
				else
					alert("No hay conexión a Internet.");
			}
		}

		if (prefs.contains("saveState_meditacion")){
			String name = prefs.getString("saveState_meditacion", "");
			if (name.isEmpty()){
				song_name.setText(" - ");
				song_name.setVisibility(View.INVISIBLE);
				song_continue.setText("Hola, elige la meditación que quieres escuchar.");
				play.setVisibility(View.INVISIBLE);
			}
			else{
				try {
					JSONObject med = new JSONObject(name);
					//song_name.setTextColor(Color.parseColor(pack.optString("pack_color_secundario")));
					if (Integer.valueOf(med.optString("med_dia")) == 0){
						song_name.setText(med.getString("med_titulo"));
					} else {
						song_name.setText("DíA " + med.getString("med_dia")+ " - " +med.getString("med_titulo"));
					}

					song_name.setVisibility(View.VISIBLE);
					song_continue.setVisibility(View.VISIBLE);
					play.setVisibility(View.VISIBLE);
					// comprobamos si el pack es el 1 (21 dias gratuito) y si NO esta suscrito
					if (med.optString("id_pack").compareToIgnoreCase("1") == 0 &&
							!prefs.getBoolean(getString(R.string.suscrito), false)){
						// si es el pack gratuito, mostramos información en los dias 7, 14 y/o 21
						int tmp = Integer.parseInt(med.getString("med_dia"));
						int id_meditacion = Integer.parseInt(med.getString("id_meditacion"));

						if (Config.log) {
							Log.i(Config.tag,"MainActivity");
							Log.i(Config.tag,"med -> " + med);
							Log.i(Config.tag,"tmp -> " + tmp);
							Log.i(Config.tag,"contains popup_primera_semana -> " + prefs.contains("popup_primera_semana"));
							Log.i(Config.tag,"contains popup_segunda_semana -> " + prefs.contains("popup_segunda_semana"));
							Log.i(Config.tag,"contains popup_tercera_semana -> " + prefs.contains("popup_tercera_semana"));
						}

						if (id_meditacion == 46 && !prefs.contains("popup_primera_semana")){
							prefs.edit().putBoolean("popup_primera_semana", true).commit();
							popup2Btn(getString(R.string.popup_primera_semana),
									getString(R.string.subscribe_now),
									getString(R.string.subscribe_not_now));
						}
						if (id_meditacion == 47 && !prefs.contains("popup_segunda_semana")){
							prefs.edit().putBoolean("popup_segunda_semana", true).commit();
							popup2Btn(getString(R.string.popup_segunda_semana),
									getString(R.string.subscribe_now),
									getString(R.string.subscribe_not_now));
						}
						if (id_meditacion == 21  && !prefs.contains("popup_tercera_semana")){
							prefs.edit().putBoolean("popup_tercera_semana", true).commit();
							popup2Btn(getString(R.string.popup_tercera_semana),
									getString(R.string.subscribe_now),
									getString(R.string.subscribe_not_now));
						}
					}
				} catch (JSONException e) {
					song_name.setVisibility(View.INVISIBLE);
					song_continue.setText("Hola, elige la meditación que quieres escuchar.");
					play.setVisibility(View.INVISIBLE);
					song_name.setText(" - ");
				}
			}
		}else{
			song_name.setVisibility(View.INVISIBLE);
			song_continue.setText("Hola, elige la meditación que quieres escuchar.");
			play.setVisibility(View.INVISIBLE);
			song_name.setText(" - ");
		}

		listview.setAdapter(adapterpacks);
	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	 @Override
	         public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

	    		 if (packs.optJSONObject(position).optInt("proximamente") == 1){

	    		 }
	    		 else{
	    			 if (packs.optJSONObject(position).optInt("pack_gratis") == 1){
		    			 Intent i = new Intent(MainActivity.this, Meditaciones.class);
			    		 i.putExtra("pack", packs.optJSONObject(position).toString());
			    		 startActivity(i);
			    		 finish();
		    		 }
		    		 else{

	    			 	if (prefs.getBoolean(getString(R.string.suscrito), false) ||
							checkComprado(String.valueOf(packs.optJSONObject(position).optInt("id_pack")))){
							Intent i = new Intent(MainActivity.this, Meditaciones.class);
							i.putExtra("pack", packs.optJSONObject(position).toString());
							startActivity(i);
							finish();
						}else{
							Intent i = new Intent(MainActivity.this, Compras.class);
							i.putExtra("pack", packs.optJSONObject(position).toString());
							startActivity(i);
							finish();
						}
		    		 }
	    		 }
	    	 }
        });

        play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				boolean saved = true;
				Intent i = new Intent(MainActivity.this, Reproductor.class);

				if (prefs.contains("saveState_pack"))
					i.putExtra("pack", prefs.getString("saveState_pack", ""));
				else
					saved = false;
				if (prefs.contains("saveState_meditacion"))
					i.putExtra("meditacion", prefs.getString("saveState_meditacion", ""));
				else
					saved = false;
				if (prefs.contains("saveState_duracion"))
					i.putExtra("duracion", prefs.getString("saveState_duracion", ""));
				else
					saved = false;
				if (prefs.contains("saveState_time"))
					i.putExtra("dur", prefs.getLong("saveState_time", 0));

				i.putExtra("intros", false);

				if (saved){
					i.putExtra("fromMain", true);
					startActivity(i);
		    		finish();
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

        try {
//          checkDay();
            checkLoged();
        } catch (JSONException e) {
            e.printStackTrace();
			printLog("Error en la función checkLoged!!!");
        }

    }
	/** Comprueba la fecha actual y la fecha de última comprobación, en caso que sean distintas (1 vez al día),
	 * mostrara un popup
	 *
	 */
	private void checkDay(){
		// si hay fecha de última conexión
		if (prefs.contains(getString(R.string.last_check_date_millis))){
			// obtenemos la fecha
			long last_day = prefs.getLong(getString(R.string.last_check_date_millis),0);
			// comparamos la fecha guardada con la actual
			if (!Basics.compareDateDMY(last_day,System.currentTimeMillis())){
				// mostramos un popup informando de las suscripciones
				newPopup(getString(R.string.popup_nuevo_titulo),getString(R.string.popup_nuevo_text));
				// guardamos el dia actual como fecha de última conexión
				prefs.edit().putLong(getString(R.string.last_check_date_millis),System.currentTimeMillis()).apply();
			} else {
				printLog("compareDateDMY iguales!!!");
			}
		} else {
			// mostramos un popup informando de las suscripciones
			newPopup(getString(R.string.popup_nuevo_titulo),getString(R.string.popup_nuevo_text));
			// guardamos el dia actual como fecha de última conexión
			prefs.edit().putLong(getString(R.string.last_check_date_millis),System.currentTimeMillis()).apply();
		}
	}
	private void checkLoged() throws JSONException {
		//Comprobamos si hay conexión a Internet
		if(Basics.checkConn(MainActivity.this)){
			// obtenemos las suscripciones de Google Play
			getSubscriptionsInfo();
		}
	}

	private void getSubscriptionsInfo(){

        final String[] skuIds = getResources().getStringArray(R.array.suscriptions_sku);
        final List<String> skuIdsList = Arrays.asList(skuIds);
        iabHelper = new IabHelper(MainActivity.this, Config.license_key);
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (result.isFailure()) {
                    printLog("Problem setting up In-app Billing: " + result);
                    dispose();
                }else{
                    try {
                        iabHelper.queryInventoryAsync(true, skuIdsList, new IabHelper.QueryInventoryFinishedListener() {
                            @Override
                            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
                                if (result.isFailure()) {
                                    printLog("Problem querying inventory: " + result);
                                    dispose();
                                    return;
                                }
								subscriptionInventory = inventory;
								// obtenemos todas las llaves de las compras/suscripciones activas
								List<String> ownedSkus = subscriptionInventory.getAllOwnedSkus();
								ownedSubscriptions = new ArrayList<>();
								List<String> ownedSubscriptionsSku = new ArrayList<>();

								List<Purchase> ownedBuys = new ArrayList<>();

								for (Purchase p : subscriptionInventory.getAllPurchases()){
									printLog("getAllPurchases ->  " + p.getSku());

									printLog("saveSubscription -> toString -> " + p.toString());
									printLog("saveSubscription -> getOrderId -> " + p.getOrderId());
									printLog("saveSubscription -> getToken-> " + p.getToken());
									printLog("saveSubscription -> getItemType -> " + p.getItemType());
									printLog("saveSubscription -> getPurchaseTime -> " + p.getPurchaseTime());
									printLog("saveSubscription -> getPurchaseState -> " + p.getPurchaseState());
									printLog("saveSubscription -> getSignature -> " + p.getSignature());
									printLog("saveSubscription -> getOriginalJson -> " + p.getOriginalJson());

									if (p.getSku().contains("id_pack")){
										ownedBuys.add(p);
									}
									if (p.getSku().contains("suscripcion")) {
										ownedSubscriptions.add(p);
										ownedSubscriptionsSku.add(p.getSku());
									}
								}
								if (ownedBuys.size() > 0){
                                    try {
                                        saveBuys(ownedBuys);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
								// si esta registrado y hay llaves, guardamos las llaves como skus activos en sharedpreferences
                                if (ownedSubscriptions.size() > 0){
                                    JSONArray jsonArray = new JSONArray(ownedSubscriptionsSku);
                                    // marcamos que esta suscrito
                                    prefs.edit().putBoolean(getString(R.string.suscrito),true).commit();
									printLog("Subscription -> HAY SUSCRIPCIONES.");
                                    // guardamos los sku en sharedpref
                                    //prefs.edit().putString(getString(R.string.skus_activos), jsonArray.toString()).commit();
                                }else{
                                    // marcamos que no está suscrito
                                    prefs.edit().putBoolean(getString(R.string.suscrito),false).commit();
									printLog("Subscription -> NO HAY SUSCRIPCIONES.");

									//TODO
									//Bloquear progreso: NO
									//Bloquear favoritos: SI, pero mirar que no están comprados

                                    // si no hay sku activos, quitamos la clave en las SharedPref
                                    //prefs.edit().remove(getString(R.string.skus_activos)).commit();
                                }
                                // indicamos al adapter que debe actualizar los datos
                                adapterpacks.notifyDataSetChanged();

                                suscripcionesGoogle = true;

								for (int i = 0; i < ownedSkus.size(); i++) {
                                    printLog(" sku key ACTIVO: " + ownedSkus.get(i));
                                }
                            }
                        });
                    } catch (Exception e) {
//        } catch (IabHelper.IabAsyncInProgressException e) {
                        printLog("EXCEPTION:" + e.getMessage());
                    }
                }
            }
        });
	}
	private void saveBuys(List<Purchase> ownedBuys) throws JSONException {
		JSONObject pack;

        packs = new JSONArray(prefs.getString("packs", ""));
		for (int i = 0; i < packs.length(); i++) {

             pack = packs.optJSONObject(i);
             String Producto = pack.optString("id_pack_compra");

			if (subscriptionInventory.hasPurchase(Producto)){
				printLog("saveBuys elemento con sku: " + Producto);
				prefs.edit().putBoolean("comprado_"+String.valueOf(pack.optInt("id_pack")), true).commit();
				//Toast.makeText(ctx, "Ya tienes este elemento!", Toast.LENGTH_SHORT).show();
				if ((pack.optInt("id_pack") == 2) || (pack.optInt("id_pack") == 3) ||  (pack.optInt("id_pack") == 4) || (pack.optInt("id_pack") == 5)) {

					if (!prefs.contains("Premios_7")) {
						prefs.edit().putBoolean("Premios_7", true).commit();
					} else if (!prefs.contains("Premios_8")) {
						prefs.edit().putBoolean("Premios_8", true).commit();
					} else if (!prefs.contains("Premios_9")) {
						prefs.edit().putBoolean("Premios_9", true).commit();
					} else if (!prefs.contains("Premios_10")) {
						prefs.edit().putBoolean("Premios_10", true).commit();
					}
				}
			}
		}
		adapterpacks.notifyDataSetChanged();
	}

    /*
     ** Method for releasing resources (dispose of object)
     */
    public void dispose() {
        if (iabHelper != null) {
            try {
                iabHelper.dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
            iabHelper = null;
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void setMenu(){
		menu_lateral = new SlidingMenu(MainActivity.this);
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


		((View) menu_lateral.findViewById(R.id.id_menu_view_ini)).setVisibility(View.VISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_fav)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_progreso)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_acercade)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_vision)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_opciones)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_sincro)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_compras)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_suscription)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_contact)).setVisibility(View.INVISIBLE);

		LinearLayout acercade = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_acercade_ll);
		acercade.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(MainActivity.this, Acercade.class);
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout opciones = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_opciones_ll);
		opciones.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(MainActivity.this, Opciones.class);
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout vision = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_vision_ll);
		vision.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(MainActivity.this, Vision.class);
				 i.setAction("fromMenu");
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout favoritos = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_fav_ll);
		favoritos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(MainActivity.this, Favoritos.class);
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout progreso = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_progreso_ll);
		progreso.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(MainActivity.this, Progreso.class);
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
						Intent i = new Intent(MainActivity.this, Sincro2.class);
			    		 startActivity(i);
			    		 finish();
					}
					else{
						Intent i = new Intent(MainActivity.this, Sincro.class);
			    		 startActivity(i);
			    		 finish();
					}
				}
				else{
					Intent i = new Intent(MainActivity.this, Sincro.class);
		    		startActivity(i);
		    		finish();
				}
			}
		});
		LinearLayout compras = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_compras_ll);
		compras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new RecargarCompras(MainActivity.this);
			}
		});

		LinearLayout suscripcion = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_suscription_ll);
		suscripcion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				goToSubscription();

			}
		});
		LinearLayout contacto = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_contact_ll);
		contacto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, Contacto.class);
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

	}
	private void goToSubscription(){
		Intent i = new Intent(MainActivity.this, Suscripcion.class);
		startActivity(i);
		finish();
		// si esta registrado, va a la suscripcion. En caso contrario al login
		/*if (prefs.getBoolean(getString(R.string.registrado),false)){
			Intent i = new Intent(MainActivity.this, Suscripcion.class);
			startActivity(i);
			finish();
		}else{
			Intent i = new Intent(MainActivity.this, LogIn.class);
			startActivity(i);
			finish();
		}*/
	}
    protected void alert(String mens){

   			final Dialog dialog = new Dialog(MainActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.alert_generico);
			dialog.setCancelable(false);

			// set the custom dialog components - text, image and button
			TextView close = (TextView) dialog.findViewById(R.id.id_alert_btn);
			TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
			TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);
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
	 protected void disclaimer(){

		 Display display = getWindowManager().getDefaultDisplay();
		 Point size = new Point();
		 display.getSize(size);
		 int width = size.x;
		 int height = size.y;

			final Dialog dialog = new Dialog(MainActivity.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.alert_disclaimer);
//			dialog.getWindow().setLayout(width, height-10);
			dialog.setCancelable(false);

			// set the custom dialog components - text, image and button
			TextView close = (TextView) dialog.findViewById(R.id.id_alert_btn);
			TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
			TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);

			text.setTypeface(font);
			close.setTypeface(font);
			titulo.setTypeface(font);

			// if button is clicked, close the custom dialog
			close.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					prefs.edit().putBoolean("disclaimer", true).commit();
					dialog.dismiss();
					alert_meditaciones_nuevas();
				}
			});

			dialog.show();
		  }
    protected boolean checkComprado(String id_pack){
    	if (prefs.contains("comprado_"+id_pack)){
    		if (prefs.getBoolean("comprado_"+id_pack, false))
    			return true;
    		return false;
    	}
    	else
    		return false;


    }

	protected void newPopup(String title, String info){

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_nuevo);
//			dialog.getWindow().setLayout(width, height-10);
		dialog.setCancelable(false);
		// set the custom dialog components - text, image and button
		TextView close = (TextView) dialog.findViewById(R.id.id_alert_btn);
		TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
		TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);

		text.setTypeface(font);
		close.setTypeface(font);
		titulo.setTypeface(font);

		titulo.setText(title);
		text.setText(info);

		// if button is clicked, close the custom dialog
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	protected void popup2Btn(String text, String bt1, String bt2){

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_2_btn);
		dialog.setCancelable(false);
		// set the custom dialog components - text, image and button
		TextView textView = dialog.findViewById(R.id.id_alert_text);
		TextView btOk = dialog.findViewById(R.id.id_alert_btn_ok);
		TextView btCancel = dialog.findViewById(R.id.id_alert_btn_cancel);

		textView.setTypeface(font);
		btOk.setTypeface(font);
		btCancel.setTypeface(font);

		textView.setText(text);
		btOk.setText(bt1);
		btCancel.setText(bt2);

		btOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				goToSubscription();
			}
		});
		btCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	private void printLog(String text){
    	if (Config.log){
    		Log.i(Config.tag, text);
		}
	}

	protected void alertNewsletter(){


		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_registro_susc);
		dialog.setCancelable(true);

		// set the custom dialog components - text, image and button
		TextView ver = (TextView) dialog.findViewById(R.id.id_alert_btn);
		TextView close = (TextView) dialog.findViewById(R.id.id_alert_btn_close);

		TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
		TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);
		final EditText email = (EditText) dialog.findViewById(R.id.id_alert_editext);
		final EditText name = (EditText) findViewById(R.id.id_alert_editext_name);
		final CheckBox check = (CheckBox) dialog.findViewById(R.id.checkBoxCustomized);
		TextView check_text = (TextView) dialog.findViewById(R.id.checkBoxCustomized_text);
		check_text.setMovementMethod(LinkMovementMethod.getInstance());
		TextView tvPolitica = dialog.findViewById(R.id.tv_politica);
		tvPolitica.setMovementMethod(LinkMovementMethod.getInstance());

		text.setTypeface(font);
		ver.setTypeface(font);
		titulo.setTypeface(font);
		check_text.setTypeface(font);
		tvPolitica.setTypeface(font);

		check_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AssetManager assetManager = getAssets();

				InputStream in = null;
				OutputStream out = null;
				File file = new File( getFilesDir(), "abc.pdf");
				try
				{
					in = assetManager.open("pdf/privacidad.pdf");
					out =  openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

					copyFile(in, out);
					in.close();
					in = null;
					out.flush();
					out.close();
					out = null;
				} catch (Exception e)
				{
					Log.e("tag", e.getMessage());
				}

				Intent intent = new Intent(Intent.ACTION_VIEW);
/*		        String filePath = "";
		        String type = "application/pdf";
				if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N) {
					filePath = "content://" +  getFilesDir() + "/abc.pdf";
					intent.setDataAndType(Uri.parse(filePath), type);
				} else {
					filePath = "file://" +  getFilesDir() + "/abc.pdf";
					intent.setDataAndType(Uri.fromFile(new File(filePath)), type);
				}
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);*/
				intent.setData(Uri.parse(Config.url_nota_legal));
				startActivity(intent);
			}
		});

		close.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 dialog.dismiss();
			 }

		 });

		// if button is clicked, close the custom dialog
		ver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (check.isChecked()){
					if (isEmailValid(email.getText().toString())){
						if (isNameValid(name.getText().toString())){
							if (Basics.checkConn(MainActivity.this)){
								new Downloader.setNewsletter(Basics.checkConn(MainActivity.this),
										email.getText().toString(),
										name.getText().toString(),
										Basics.getWifiMac(MainActivity.this),
										new Downloader.setNewsletter.AsyncResponse() {
											@Override
											public void processFinish(String respuesta) {
												if (respuesta!=null){
													alert("Se ha dado de alta correctamente.", "Información");
													alertFinish("Se ha dado de alta correctamente.", "Información",dialog);
												}
											}
										}).execute();
							} else {
								alert("No hay conexión a Internet.", null);
							}
						}else{
							alert("Debes indicarnos tu nombre.", null);
						}

					}
					else{
						alert("Dirección de email incorrecta.", null);
					}
				}
				else{
					Toast.makeText(MainActivity.this, "Ha de aceptar la política de privacidad", Toast.LENGTH_SHORT).show();
				}
			}
		});

		dialog.show();
	}

	protected void alert(String mens, String tit){

		final Dialog dialog = new Dialog(MainActivity.this);
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

	protected void alert_meditaciones_nuevas(){

		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_meditaciones_nuevas);
		dialog.setCancelable(false);

		// set the custom dialog components - text, image and button
		TextView close = (TextView) dialog.findViewById(R.id.id_alert_btn);
		TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);

		String version_desc = prefs.getString("version_desc","");
		TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);

		if (version_desc.compareTo("")!=0){
			text.setText(version_desc);
		}

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

		prefs.edit().putBoolean("showNewContent", false).commit();
	}

	protected void alertFinishFirstPack(String mens, String tit){

		final Dialog dialog = new Dialog(MainActivity.this);
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

	protected void alertFinish(String mens, String tit, final Dialog d){

		final Dialog dialog = new Dialog(MainActivity.this);
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
				d.dismiss();
			}
		});

		dialog.show();
	}

	public boolean isNameValid(String name)
	{
		if (name.length() < 3){
			return false;
		}else{
			return true;
		}
	}
	public boolean isEmailValid(String email)
	{
		String regExpn =
				"^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
						+"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
						+"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
						+"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
						+"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
						+"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if(matcher.matches())
			return true;
		else
			return false;
	}
	private void copyFile(InputStream in, OutputStream out) throws IOException
	{
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1)
		{
			out.write(buffer, 0, read);
		}
	}


}
