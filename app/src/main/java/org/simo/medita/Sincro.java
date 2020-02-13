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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.extras.Basics;
import org.simo.medita.extras.HttpConnection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Sincro extends Activity{

	protected SharedPreferences prefs;
	protected Typeface font;
	protected SlidingMenu menu_lateral;
	protected ImageView menu;
	protected TextView btn_generar;
	protected TextView btn_utilizar;
	protected boolean bloqueo = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sincro);
		font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
//		prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
		LinearLayout atras = (LinearLayout)findViewById(R.id.id_sincro_atras);
		
		btn_generar = (TextView) findViewById(R.id.id_generar_btn);
		btn_utilizar = (TextView) findViewById(R.id.id_utilizar_btn);
		
		
		atras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				menu_lateral.showMenu(true);
			}
		});
		btn_generar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!bloqueo)
					alertRegistro();
			}
		});
		btn_utilizar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!bloqueo)
					alertCodigo();
			}
		});
		
			
		
		 setMenu();
		
	}

	@Override
	public void onBackPressed()
	{	
		Intent i = new Intent(Sincro.this, MainActivity.class);   
		i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(i);		
		finish();
	}
	
	public void setMenu(){
		menu_lateral = new SlidingMenu(Sincro.this);
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
		((View) menu_lateral.findViewById(R.id.id_menu_view_progreso)).setVisibility(View.VISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_acercade)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_vision)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_opciones)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_sincro)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_sincro)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_suscription)).setVisibility(View.INVISIBLE);

		LinearLayout acercade = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_acercade_ll);
		acercade.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Sincro.this, Acercade.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout opciones = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_opciones_ll);
		opciones.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Sincro.this, Opciones.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout vision = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_vision_ll);
		vision.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Sincro.this, Vision.class); 
				 i.setAction("fromMenu");
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout favoritos = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_fav_ll);
		favoritos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Sincro.this, Favoritos.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});

		LinearLayout inicio = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_inicio_ll);
		inicio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Sincro.this, MainActivity.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout progreso = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_progreso_ll);
		progreso.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Sincro.this, Progreso.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout compras = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_compras_ll);
		compras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
			}
		});
		LinearLayout suscripcion = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_suscription_ll);
		suscripcion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Sincro.this, Suscripcion.class);
				startActivity(i);
				finish();
				// si esta registrado, va a la suscripcion. En caso contrario al login
				/*if (prefs.getBoolean(getString(R.string.registrado),false)){
					Intent i = new Intent(Sincro.this, Suscripcion.class);
					startActivity(i);
					finish();
				}else{
					Intent i = new Intent(Sincro.this, LogIn.class);
					startActivity(i);
					finish();
				}*/
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
	 		
			final Dialog dialog = new Dialog(Sincro.this);
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
	  
	  protected void alertRegistro(){
		 /* String possibleEmail = "";
		  Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		  Account[] accounts = AccountManager.get(Sincro.this).getAccounts();
		  for (Account account : accounts) {
		      if (emailPattern.matcher(account.name).matches()) {
		    	  if (account.name.contains("gmail"))
		    		  possibleEmail = account.name;
		          
		      }
		  }
		  
	 		
		final Dialog dialog = new Dialog(Sincro.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_registro_sync);
		dialog.setCancelable(true);

		// set the custom dialog components - text, image and button
		TextView ver = (TextView) dialog.findViewById(R.id.id_alert_btn);
		//TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
		TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);
		final CheckBox  check = (CheckBox) dialog.findViewById(R.id.checkBoxCustomized);

		final TextView email = (TextView) dialog.findViewById(R.id.id_alert_editext);
		email.setText(possibleEmail);

		TextView check_text = (TextView) dialog.findViewById(R.id.checkBoxCustomized_text);
		
		//text.setTypeface(font);
		ver.setTypeface(font);
		titulo.setTypeface(font);
		check_text.setTypeface(font);
		
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
		        intent.setDataAndType(
		                Uri.parse("file://" +  getFilesDir() + "/abc.pdf"),
		                "application/pdf");

		        startActivity(intent);
			}
		});
			

		// if button is clicked, close the custom dialog
		ver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			if(check.isChecked()){
				if (Basics.checkConn(Sincro.this)){
					 new setRegistro().execute(email.getText().toString());					
				 }
				 else{
					 alert("No hay conexión a Internet.");
				 }
				dialog.dismiss();
			}
			else{
				Toast.makeText(Sincro.this, "Ha de aceptar la política de privacidad", Toast.LENGTH_SHORT).show();
			}			
				
			}
		});

		dialog.show();*/
	  }
	  
	  protected void alertCodigo(){
		  /*String aux = "";
		  Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		  Account[] accounts = AccountManager.get(Sincro.this).getAccounts();
		  for (Account account : accounts) {
		      if (emailPattern.matcher(account.name).matches()) {
		    	  if (account.name.contains("gmail"))
		    		  aux = account.name;
		          
		      }
		  }
		  final String possibleEmail = aux;
	 		
			final Dialog dialog = new Dialog(Sincro.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_codigo_sync);
		dialog.setCancelable(true);

		// set the custom dialog components - text, image and button
		TextView ver = (TextView) dialog.findViewById(R.id.id_alert_btn);
		//TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
		TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);
		final EditText email = (EditText) dialog.findViewById(R.id.id_alert_editext);
	
		
		//text.setTypeface(font);
		ver.setTypeface(font);
		titulo.setTypeface(font);
				

		// if button is clicked, close the custom dialog
		ver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			 if (Basics.checkConn(Sincro.this)){
				 new setCodigo().execute(possibleEmail,email.getText().toString());
				 
			 }
			 else{
				 alert("No hay conexión a Internet.");
			 }				
				dialog.dismiss();
				
			}
		});

		dialog.show();*/
	  }
	  
	  private class setRegistro extends AsyncTask<String, String, String> {
		  @Override
		  protected void onPreExecute() { 
			  bloqueo = true;

		    }
		    @Override
		    protected String doInBackground(String... params) {
		    	String mac = Basics.getWifiMac(Sincro.this);
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
				        	   Log.i("medita_registro", result);
				   		    
				         			           
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
						
						Intent i = new Intent(Sincro.this, Sincro2.class);   
						i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
						startActivity(i);		
						finish();
					}
					else if(result.trim().compareTo("3")==0){
						alert("Ha ocurrido un error.Inténtelo más tarde.");
					}
					else if(result.trim().compareTo("2")==0){
						alert("Ha ocurrido un error. Inténtelo más tarde.");
					}
					else if(result.trim().compareTo("-1")==0){
						alert("Ha ocurrido un error. Inténtelo más tarde.");
					}
					else if(result.trim().compareTo("1")==0){
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
	  private class setCodigo extends AsyncTask<String, String, String> {
		  @Override
		  protected void onPreExecute() { 
			  bloqueo = true;

		    }
		    @Override
		    protected String doInBackground(String... params) {
		    	String mac = Basics.getWifiMac(Sincro.this);
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
				        	   Log.i("medita_registro", result);
				   		    
				         			           
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
						Intent i = new Intent(Sincro.this, Sincro2.class);   
						i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
						startActivity(i);		
						finish();
					}
					else if(result.trim().compareTo("2")==0){
						alert("Ha ocurrido un error. Inténtelo más tarde.");
					}
					else if(result.trim().compareTo("1")==0){
						alert("Ya hay una cuenta asociada para esta plataforma.");
					}
					else if(result.trim().compareTo("-1")==0){
						alert("Ha ocurrido un error. Inténtelo más tarde.");
					}
					else if(result.trim().compareTo("0")==0){
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

