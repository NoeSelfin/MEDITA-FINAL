package org.simo.medita;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.extras.Basics;
import org.simo.medita.extras.HttpConnection;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

public class Sincro2 extends Activity{


	protected SharedPreferences prefs;
	protected Typeface font;
	protected SlidingMenu menu_lateral;
	protected ImageView menu;
	protected TextView btn_enviar;
	protected TextView btn_recibir;
	protected TextView btn_recuperar;
	protected boolean bloqueo = false;
	protected ImageView loading;
	protected Dialog dialogLoading; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sincro_dos);
		font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
//		prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
		LinearLayout atras = (LinearLayout)findViewById(R.id.id_sinc2_atras);
		
		btn_enviar = (TextView) findViewById(R.id.id_enviar_btn);
		btn_recibir = (TextView) findViewById(R.id.id_recibir_btn);
		btn_recuperar = (TextView) findViewById(R.id.id_sinc2_tv3);
		
		atras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				menu_lateral.showMenu(true);
			}
		});
		btn_enviar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!bloqueo)
					alertEnviar();
			}
		});
		btn_recibir.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!bloqueo)
					alertRecibir();
			}
		});
		btn_recuperar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String aux = "";
				  Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
				  Account[] accounts = AccountManager.get(Sincro2.this).getAccounts();
				  for (Account account : accounts) {
				      if (emailPattern.matcher(account.name).matches()) {
				    	  if (account.name.contains("gmail"))
				    		  aux = account.name;
				          
				      }
				  }
					if (!bloqueo)
						new recuperarCodigo().execute(aux);
			}
		});
		
		 setMenu();
		
	}

	@Override
	public void onBackPressed()
	{	
		Intent i = new Intent(Sincro2.this, MainActivity.class);   
		i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(i);		
		finish();
	}
	
	public void setMenu(){
		menu_lateral = new SlidingMenu(Sincro2.this);
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
		
		((View) menu_lateral.findViewById(R.id.id_menu_view_ini)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_fav)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_progreso)).setVisibility(View.VISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_acercade)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_vision)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_opciones)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_sincro)).setVisibility(View.INVISIBLE);
		
		LinearLayout acercade = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_acercade_ll);
		acercade.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Sincro2.this, Acercade.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout opciones = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_opciones_ll);
		opciones.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Sincro2.this, Opciones.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout vision = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_vision_ll);
		vision.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Sincro2.this, Vision.class); 
				 i.setAction("fromMenu");
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout favoritos = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_fav_ll);
		favoritos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Sincro2.this, Favoritos.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});

		LinearLayout inicio = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_inicio_ll);
		inicio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Sincro2.this, MainActivity.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout progreso = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_progreso_ll);
		progreso.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Sincro2.this, Progreso.class);   
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
	
	 protected void alert(String mensaje){
	 		
			final Dialog dialog = new Dialog(Sincro2.this);
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
		ver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});

		dialog.show();
	  }
	 protected void alertLoading(){
	 		
		dialogLoading = new Dialog(Sincro2.this);
		dialogLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogLoading.setContentView(R.layout.alert_loading);
		dialogLoading.setCancelable(false);

		// set the custom dialog components - text, image and button
		TextView titulo = (TextView) dialogLoading.findViewById(R.id.id_alert_titulo);
		loading = (ImageView)dialogLoading.findViewById(R.id.id_loading_alert);
		titulo.setText("Procesando");	
		
		titulo.setTypeface(font);

		AnimationDrawable loadingAnimation = new AnimationDrawable();
		loadingAnimation.setOneShot(false);
		loading.setBackground( new BitmapDrawable(getResources(), loadBitmapFromAsset("loading/loading_00000.png")));	        
        AddFrames af =  new AddFrames(Sincro2.this, loadingAnimation);
        af.addLoadingFrames();
        loadingAnimation = af.getAnimation();
        loading.setBackground(loadingAnimation);
        loadingAnimation.start();

        dialogLoading.show();
	  }
	 protected void alertReturnMain(String mensaje){
	 		
			final Dialog dialog = new Dialog(Sincro2.this);
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
		ver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent i = new Intent(Sincro2.this, MainActivity.class);   
				i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				startActivity(i);		
				finish();
				
			}
		});

		dialog.show();
	  }
	  
	  protected void alertEnviar(){
		  String possibleEmail = "";
		  Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		  Account[] accounts = AccountManager.get(Sincro2.this).getAccounts();
		  for (Account account : accounts) {
		      if (emailPattern.matcher(account.name).matches()) {
		    	  if (account.name.contains("gmail"))
		    		  possibleEmail = account.name;
		          
		      }
		  }		  
	 		
		final Dialog dialog = new Dialog(Sincro2.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_generico);
		dialog.setCancelable(true);

		// set the custom dialog components - text, image and button
		TextView ver = (TextView) dialog.findViewById(R.id.id_alert_btn);
		TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
		TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);		
		
		ver.setText("Aceptar");
				
		titulo.setText("Sincronizar");
		text.setText("Quiere enviar su progreso y compras a sus otros dispositivos?");	
		
		text.setTypeface(font);
		ver.setTypeface(font);
		titulo.setTypeface(font);

		final String aux_email = possibleEmail;
			
		// if button is clicked, close the custom dialog
		ver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			 if (Basics.checkConn(Sincro2.this)){
				 				 				 
				 new setEnviar().execute(aux_email);
				
			 }
			 else{
				 alert("No hay conexión a Internet.");
			 }
					
				
				dialog.dismiss();
				
			}
		});

		dialog.show();
	  }
	  
	  protected void alertRecibir(){
		  String aux = "";
		  Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		  Account[] accounts = AccountManager.get(Sincro2.this).getAccounts();
		  for (Account account : accounts) {
		      if (emailPattern.matcher(account.name).matches()) {
		    	  if (account.name.contains("gmail"))
		    		  aux = account.name;
		          
		      }
		  }
		  final String possibleEmail = aux;
	 		
			final Dialog dialog = new Dialog(Sincro2.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_generico);
		dialog.setCancelable(true);

		// set the custom dialog components - text, image and button
		TextView ver = (TextView) dialog.findViewById(R.id.id_alert_btn);
		TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
		TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);		
		
		titulo.setText("Sincronizar");
		text.setText("Quiere recibir su progreso y compras de sus otros dispositivos?");	
		
		ver.setText("Aceptar");
		
		text.setTypeface(font);
		ver.setTypeface(font);
		titulo.setTypeface(font);
				

		// if button is clicked, close the custom dialog
		ver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			 if (Basics.checkConn(Sincro2.this)){
				 new setRecibir().execute(possibleEmail);
				 
			 }
			 else{
				 alert("No hay conexión a Internet.");
			 }				
				dialog.dismiss();
				
			}
		});

		dialog.show();
	  }
	  
	  private class setEnviar extends AsyncTask<String, String, String> {
		  @Override
		  protected void onPreExecute() { 
			  bloqueo = true;
			  alertLoading();
		    }
		    @Override
		    protected String doInBackground(String... params) {
		    	String mac = Basics.getWifiMac(Sincro2.this);
		           HttpConnection http = null;	 					           
		           String result="";
		        	   try {
		        		   http = new HttpConnection();	   
		        		

						   JSONObject jo =  new JSONObject();
						   jo.put("token",Config.token);
						   jo.put("mac",mac);
						   jo.put("email",params[0]);
						   jo.put("progreso",getPremios());
						   //jo.put("favoritos",getFavoritos());
						   jo.put("favoritos","");
						   jo.put("packs_comprados",getCompras());
						   
						   //Log.i("medita_registro", jo.toString());
						   								   
						   http = new HttpConnection();
				           result = http.postData(Config.url_set_sync, jo.toString());
				           
				           if (result != null)
				        	   Log.i("medita_registro", result);
				   		    
				         			           
						} catch (JSONException e) {
						}          
		       
		 
		        return result;
		    }		   
		 
		    @Override
		    protected void onPostExecute(String result) {
		    	dialogLoading.cancel();
		    	bloqueo = false;
		    	if(result != null){
					if (result.trim().compareTo("1") == 0){
						
						//prefs.edit().putBoolean("sincronizado", true).commit();
						alertReturnMain("Datos sincronizados correctamente.");
						
					}
					else if(result.trim().compareTo("2")==0){
						alert("Cuenta errónea.");
					}
					else if(result.trim().compareTo("0")==0){
						alert("Los datos ya estaban sincronizados.");
					}
					else if(result.trim().compareTo("-1")==0){
						alert("Ha ocurrido un error. Inténtelo más tarde.");
					}					
					else{
						alert("Ha ocurrido un error. Inténtelo más tarde.");
					}
		        	
		        }	
		    	else{
		    		alert("Ha ocurrido un error. Inténtelo más tarde.");
		    	}
		    }
	  }
	  private class setRecibir extends AsyncTask<String, String, String> {
		  @Override
		  protected void onPreExecute() { 
			  bloqueo = true;
			  alertLoading();
		    }
		    @Override
		    protected String doInBackground(String... params) {
		    	String mac = Basics.getWifiMac(Sincro2.this);
		           HttpConnection http = null;	 					           
		           String result="";
		        	   try {
						   http = new HttpConnection();
						   
						   JSONObject jo =  new JSONObject();
						   jo.put("token",Config.token);
						   jo.put("mac",mac);
						   jo.put("email",params[0]);
						   
						   http = new HttpConnection();
				           result = http.postData(Config.url_get_sync, jo.toString());
				           
				           if (result != null)
				        	   Log.i("medita_registro", result);
				   		    
				         			           
						} catch (JSONException e) {
						}          
		       
		 
		        return result;
		    }		   
		 
		    @Override
		    protected void onPostExecute(String result) {
		    	bloqueo = false;
		    	dialogLoading.cancel();
		        if(result != null){
				        	
		        	if (result.trim().compareTo("-1") == 0){
		        		alert("Ha ocurrido un error. Inténtelo más tarde.");
		        	}
		        	else{
		        		
		        		JSONArray datos;
						try {
							datos = new JSONArray (result);
				    					        		
			        		setCompras(datos.optJSONObject(0).optString("packs_comprados"));
			        		setPremios(datos.optJSONObject(0).optString("progreso"));
			        		//setFavoritos(datos.optJSONObject(0).optString("favoritos"));
			        		
			        		alertReturnMain("Datos sincronizados correctamente.");
							
						} catch (JSONException e) {
						}
		    	        		
		        	}
				        	
		        }
		        else{
		    		alert("Ha ocurrido un error. Inténtelo más tarde.");
		    	}
		            
		    }
		 
		    
		}
	  private class recuperarCodigo extends AsyncTask<String, String, String> {
		  @Override
		  protected void onPreExecute() { 
			  bloqueo = true;
			  alertLoading();

		    }
		    @Override
		    protected String doInBackground(String... params) {
		    	String mac = Basics.getWifiMac(Sincro2.this);
		           HttpConnection http = null;	 					           
		           String result="";
		        	   try {
						   http = new HttpConnection();
						   
						   JSONObject jo =  new JSONObject();
						   jo.put("token",Config.token);
						   jo.put("mac",mac);
						   jo.put("email",params[0]);
						   
						   http = new HttpConnection();
				           result = http.postData(Config.url_recuperar_code, jo.toString());
				           
				           if (result != null)
				        	   Log.i("medita_registro", result);
				   		    
				         			           
						} catch (JSONException e) {
						}          
		       
		 
		        return result;
		    }		   
		 
		    @Override
		    protected void onPostExecute(String result) {
		    	dialogLoading.cancel();
		    	bloqueo = false;
		        if(result != null){
		        	
		        	if (result.trim().contains("1")){
		        		alert("Se ha enviado de nuevo el código al correo con el que se registró.");
		        	}
		        	else{
		        		alert("Ha ocurrido un error. Inténtelo más tarde.");
		        	}
							        	
		        }
		        else{
		    		alert("Ha ocurrido un error. Inténtelo más tarde.");
		    	}
		            
		    }

		 
		    
		}

	  
	  protected String getPremios(){
		  int nivel = 0;
		  if (prefs.contains("Premios_1"))
				nivel = nivel + 1;
			if (prefs.contains("Premios_2"))
				nivel = nivel+1;
			if (prefs.contains("Premios_3"))
				nivel = nivel+1;
			if (prefs.contains("Premios_4"))
				nivel = nivel+1;
			if (prefs.contains("Premios_5"))
				nivel = nivel+1;
			if (prefs.contains("Premios_6"))
				nivel = nivel+1;
			if (prefs.contains("Premios_7"))
				nivel = nivel+1;
			if (prefs.contains("Premios_8"))
				nivel = nivel+1;
			if (prefs.contains("Premios_9"))
				nivel = nivel+1;
			if (prefs.contains("Premios_10"))
				nivel = nivel+1;
			
			return String.valueOf(nivel);
	  }

	  protected void setPremios(String progreso){
		  
		  
		  if ((progreso.trim().compareTo("") != 0) || (progreso == null)){
			  int nivel = Integer.valueOf(progreso);
			  
			  
			  if (nivel == 1 ){
				  prefs.edit().putBoolean("Premios_1", true).commit();
			  }
			  else if (nivel == 2 ){
				  prefs.edit().putBoolean("Premios_1", true).commit();
				  prefs.edit().putBoolean("Premios_2", true).commit();
			  }
			  else if (nivel == 3 ){
				  prefs.edit().putBoolean("Premios_1", true).commit();
				  prefs.edit().putBoolean("Premios_2", true).commit();
				  prefs.edit().putBoolean("Premios_3", true).commit();
			  }
			  else if (nivel == 4 ){
				  prefs.edit().putBoolean("Premios_1", true).commit();
				  prefs.edit().putBoolean("Premios_2", true).commit();
				  prefs.edit().putBoolean("Premios_3", true).commit();
				  prefs.edit().putBoolean("Premios_4", true).commit();
			  }
			  else if (nivel == 5 ){
				  prefs.edit().putBoolean("Premios_1", true).commit();
				  prefs.edit().putBoolean("Premios_2", true).commit();
				  prefs.edit().putBoolean("Premios_3", true).commit();
				  prefs.edit().putBoolean("Premios_4", true).commit();
				  prefs.edit().putBoolean("Premios_5", true).commit();
			  }
			  else if (nivel == 6 ){
				  prefs.edit().putBoolean("Premios_1", true).commit();
				  prefs.edit().putBoolean("Premios_2", true).commit();
				  prefs.edit().putBoolean("Premios_3", true).commit();
				  prefs.edit().putBoolean("Premios_4", true).commit();
				  prefs.edit().putBoolean("Premios_5", true).commit();
				  prefs.edit().putBoolean("Premios_6", true).commit();
			  }
			  else if (nivel == 7 ){
				  prefs.edit().putBoolean("Premios_1", true).commit();
				  prefs.edit().putBoolean("Premios_2", true).commit();
				  prefs.edit().putBoolean("Premios_3", true).commit();
				  prefs.edit().putBoolean("Premios_4", true).commit();
				  prefs.edit().putBoolean("Premios_5", true).commit();
				  prefs.edit().putBoolean("Premios_6", true).commit();
				  prefs.edit().putBoolean("Premios_7", true).commit();
			  }
			  else if (nivel == 8 ){
				  prefs.edit().putBoolean("Premios_1", true).commit();
				  prefs.edit().putBoolean("Premios_2", true).commit();
				  prefs.edit().putBoolean("Premios_3", true).commit();
				  prefs.edit().putBoolean("Premios_4", true).commit();
				  prefs.edit().putBoolean("Premios_5", true).commit();
				  prefs.edit().putBoolean("Premios_6", true).commit();
				  prefs.edit().putBoolean("Premios_7", true).commit();
				  prefs.edit().putBoolean("Premios_8", true).commit();
			  }
			  else if (nivel == 9 ){
				  prefs.edit().putBoolean("Premios_1", true).commit();
				  prefs.edit().putBoolean("Premios_2", true).commit();
				  prefs.edit().putBoolean("Premios_3", true).commit();
				  prefs.edit().putBoolean("Premios_4", true).commit();
				  prefs.edit().putBoolean("Premios_5", true).commit();
				  prefs.edit().putBoolean("Premios_6", true).commit();
				  prefs.edit().putBoolean("Premios_7", true).commit();
				  prefs.edit().putBoolean("Premios_8", true).commit();
				  prefs.edit().putBoolean("Premios_9", true).commit();
			  }
			  else if (nivel == 10 ){
				  prefs.edit().putBoolean("Premios_1", true).commit();
				  prefs.edit().putBoolean("Premios_2", true).commit();
				  prefs.edit().putBoolean("Premios_3", true).commit();
				  prefs.edit().putBoolean("Premios_4", true).commit();
				  prefs.edit().putBoolean("Premios_5", true).commit();
				  prefs.edit().putBoolean("Premios_6", true).commit();
				  prefs.edit().putBoolean("Premios_7", true).commit();
				  prefs.edit().putBoolean("Premios_8", true).commit();
				  prefs.edit().putBoolean("Premios_9", true).commit();
				  prefs.edit().putBoolean("Premios_10", true).commit();
			  }
		  }
		  
		 
		
		  
	  }
	 /* protected void setFavoritos(String fav_string){
		  boolean exist = false;	
		  try {
			  JSONArray favoritos = new JSONArray(fav_string);
			  
			  Log.i("medita_setFavoritos", favoritos.toString());
			  
			  JSONArray meditaciones_insert = new JSONArray();
			  JSONArray favs = null;					  
			  JSONArray meditaciones = new JSONArray(prefs.getString("meditaciones",""));
			  JSONArray packs = new JSONArray(prefs.getString("packs",""));
			  
			  if (prefs.contains("favoritos")){	
				  Log.i("medita_setFavoritos", "EXISTE");
				  exist = true;
				  favs = new JSONArray(prefs.getString("favoritos", ""));
			  }
			  
			  
			  for (int i=0; i< meditaciones.length();i++){
				  for (int j=0; j< favoritos.length();j++){
					  Log.i("medita_setFavoritos", "DENTRO");
					  Log.i("medita_setFavoritos", favoritos.optJSONObject(j).optString("id_meditacion"));
					  Log.i("medita_setFavoritos", meditaciones.optJSONObject(i).optString("id_meditacion"));
					  if (favoritos.optJSONObject(j).optString("id_meditacion").compareTo(meditaciones.optJSONObject(i).optString("id_meditacion")) == 0){
						  meditaciones_insert.put(meditaciones.optJSONObject(i));
					  }
					  
				  }	  
				  
			  }
			  
			  Log.i("medita_setFavoritos", String.valueOf(meditaciones_insert.length()));
			  Log.i("medita_setFavoritos", meditaciones_insert.toString());
					  
			  
			  for (int i=0; i< meditaciones_insert.length();i++){
				  
				  if (exist){						  
					  
					  Log.i("medita_setFavoritos_duracion", favoritos.optJSONObject(i).optString("duracion"));
					  Log.i("medita_setFavoritos_id_pack", (new FilterData().getPackSimple(packs, favoritos.optJSONObject(i).optString("id_pack")).toString()));
					  
					  favs = new FilterData().setFavoritoDur(favs,meditaciones_insert.optJSONObject(i),new FilterData().getPackSimple(packs, favoritos.optJSONObject(i).optString("id_pack")),favoritos.optJSONObject(0).optString("duracion"));
						
					}	
				  else{		  					  
					   
					    favs = new JSONArray();	
						JSONObject packs_fav = new JSONObject();
						JSONArray med_fav = new JSONArray();
					  
					    packs_fav.put("pack", new FilterData().getPackSimple(packs, favoritos.optJSONObject(i).optString("id_pack")));
					    meditaciones_insert.optJSONObject(i).put("duracion", favoritos.optJSONObject(i).optString("duracion"));
						med_fav.put(meditaciones_insert.optJSONObject(i));						
						packs_fav.put("meditaciones", med_fav);
						
						favs.put(packs_fav); 					
						//prefs.edit().putString("favoritos", favs.toString()).commit();
						exist = true;
				  }
				  
			  }
		  
			  prefs.edit().putString("favoritos", favs.toString()).commit();
		  }catch(Exception e){
			  Log.i("medita_setFavoritos", "Algo ha ido mal.");
		  }
		  
	  }
	  protected String getFavoritos(){
		  
		  JSONArray fav = new JSONArray();
		  JSONObject aux = new JSONObject();
		  
		   if (prefs.contains("favoritos")){
			   try {
					JSONArray favoritos = new JSONArray(prefs.getString("favoritos", ""));
					favoritos = new FilterData().prepareFavoritos(favoritos);
					for (int i=0; i< favoritos.length();i++){						
						if (favoritos.optJSONObject(i).optString("tipo").compareTo("pack") != 0){
								aux = new JSONObject();
								aux.put("id_meditacion",favoritos.optJSONObject(i).optString("id_meditacion"));
								aux.put("duracion",favoritos.getJSONObject(i).getString("duracion").trim());
								aux.put("id_pack",favoritos.getJSONObject(i).getString("id_pack"));
								
								fav.put(aux);
							
						}
						
					}
					
				} catch (JSONException e) {
				}			   
		   }
		 
		  
		  return fav.toString();
		  
	  }*/
	  protected String getCompras(){
		  
		  JSONArray compras = new JSONArray();
		  
		  try {
			JSONArray packs = new JSONArray(prefs.getString("packs", ""));
			for (int i=0; i< packs.length();i++){
				if (prefs.contains("comprado_"+String.valueOf(packs.optJSONObject(i).optInt("id_pack")))){					
					
					compras.put(String.valueOf(packs.optJSONObject(i).optInt("id_pack")));
					
					 if ((packs.optJSONObject(i).optInt("id_pack") == 2) || (packs.optJSONObject(i).optInt("id_pack") == 3) ||  (packs.optJSONObject(i).optInt("id_pack") == 4) || (packs.optJSONObject(i).optInt("id_pack") == 5)){
		       	        	
		       	        	if (!prefs.contains("Premios_7")){
		       		        	prefs.edit().putBoolean("Premios_7", true).commit();		
		       	        	}
		       	        	else if (!prefs.contains("Premios_8")){
		       	        		prefs.edit().putBoolean("Premios_8", true).commit();	
		       	        	}
		       	        	else if (!prefs.contains("Premios_9")){
		       	        		prefs.edit().putBoolean("Premios_9", true).commit();	
		       	        	}
		       	        	else if (!prefs.contains("Premios_10")){
		       	        		prefs.edit().putBoolean("Premios_10", true).commit();	
		       	        	}
		       	        	
					 }
					
				}
			}
			
		} catch (JSONException e) {
		}
		  
		  return compras.toString();
		  
	  }
	  protected void setCompras(String comprados){	
				  
		  try {
			  JSONArray packs = new JSONArray(comprados);
			
			for (int i=0; i< packs.length();i++){
				  if (((Integer.valueOf(packs.getString(i))) == 2) || ((Integer.valueOf(packs.getString(i))) == 3) ||  ((Integer.valueOf(packs.getString(i))) == 4) || ((Integer.valueOf(packs.getString(i))) == 5)){
			        	
			        	if (!prefs.contains("Premios_7")){
				        	prefs.edit().putBoolean("Premios_7", true).commit();		
			        	}
			        	else if (!prefs.contains("Premios_8")){
			        		prefs.edit().putBoolean("Premios_8", true).commit();	
			        	}
			        	else if (!prefs.contains("Premios_9")){
			        		prefs.edit().putBoolean("Premios_9", true).commit();	
			        	}
			        	else if (!prefs.contains("Premios_10")){
			        		prefs.edit().putBoolean("Premios_10", true).commit();	
			        	}
			        	
			        }
				  prefs.edit().putBoolean("comprado_" + packs.getString(i), true).commit();
			}
			
		  } catch (JSONException e) {}		  
		
		  
	  }
	  
	  public Bitmap loadBitmapFromAsset(String file) {
		   Bitmap bm = null;
	        // load image
	        try {
	            // get input stream
	            InputStream ims = getAssets().open(file);
	            // load image as Drawable
	             bm =  BitmapFactory.decodeStream(ims);
	        }
	        catch(IOException ex) {
	            return null;
	        }
	        return bm;
	 }

}

