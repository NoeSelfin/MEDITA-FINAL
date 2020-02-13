package org.simo.medita;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.extras.Basics;
import org.simo.medita.extras.FilterData;

import java.util.ArrayList;

public class Meditaciones extends Activity{
	protected SharedPreferences prefs;
	protected AdapterMeditaciones adaptermeditaciones;
	protected AdapterMeditacionesDuracion adaptermeditacionesduracion;
	protected ListView listview;
	protected ListView listview_duraciones;
	protected JSONObject pack;
	protected JSONArray meditaciones;
	protected JSONObject meditacion;
	protected LinearLayout atras;
	protected ImageView ico;
	protected ImageView bg;
	protected TextView titulo;
	protected TextView disponibles;
	protected TextView letrero;
	protected RelativeLayout header;
	protected LinearLayout help;
	protected Typeface font;
	protected TextView entendido;
	protected RelativeLayout rl_principal;
	protected boolean duraciones = false;
	protected ArrayList<String> durs;
	protected Display display;
	protected int width; 
	protected Point size;
	protected  Animation animation;
	
	protected TextView entendido_nl;
	protected TextView letrero_nl;
	protected LinearLayout newsletter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_meditaciones);	
		font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
//		prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
				
		atras = (LinearLayout)findViewById(R.id.id_meditaciones_atras);
		ico = (ImageView)findViewById(R.id.id_meditaciones_ico);
		titulo = (TextView)findViewById(R.id.id_meditaciones_titulo);
		header = (RelativeLayout)findViewById(R.id.id_meditaciones_header);
		entendido = (TextView)findViewById(R.id.id_meditaciones_entendido);
		help = (LinearLayout)findViewById(R.id.id_meditaciones_help);		
		listview  = (ListView)findViewById(R.id.id_meditaciones_listview);
		listview_duraciones  = (ListView)findViewById(R.id.id_meditaciones_listview_duraciones);
		rl_principal = (RelativeLayout)findViewById(R.id.id_meditaciones_rl_list);	
		disponibles = (TextView)findViewById(R.id.id_meditaciones_disponibles);		
		letrero = (TextView)findViewById(R.id.id_meditaciones_letrero);	
		entendido_nl = (TextView)findViewById(R.id.id_meditaciones_entendido_newsletter);
		letrero_nl = (TextView)findViewById(R.id.id_meditaciones_letrero_newsletter);
		newsletter = (LinearLayout)findViewById(R.id.id_meditaciones_newsletter);
		
		disponibles.setTypeface(font);
		letrero.setTypeface(font);
		letrero_nl.setTypeface(font);
		
		meditaciones = new JSONArray();
		
		 display = getWindowManager().getDefaultDisplay();
		 size = new Point();
		 display.getSize(size);
		 width = size.x;
		
		Intent i = getIntent();
		if(i != null) {
			Bundle extras = getIntent().getExtras();
			if(extras != null) {
		        try {
					pack = new JSONObject(extras.getString("pack"));
					Log.i(Config.tag,"meditaciones bundle pack -> " + pack);
					
			        titulo.setText(pack.optString("pack_titulo"));
		        	titulo.setTypeface(font);
		  		    //BitmapFactory.Options options = new BitmapFactory.Options();
		  	        //options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		  	        //Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Medita/"+pack.getString("pack_icono"), options);
		  	        Bitmap bitmap = Basics.readFileInternal(this,"iconos",pack.getString("pack_icono"));

		  			
		  	       if (bitmap!=null){ 
		  	    	 ico.setImageBitmap(bitmap);
		  	       }
		  	       //bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Medita/"+pack.getString("pack_fondo_med"), options);
		  	       bitmap = Basics.readFileInternal(this,"fondos",pack.getString("pack_fondo_med"));
		  	       
		  	       if (bitmap!=null){ 
		  	    	 header.setBackground(new BitmapDrawable(this.getResources(), bitmap));
		  	       }
		  	       else{
		  	    	 header.setBackgroundColor(Color.parseColor(pack.getString("pack_color").trim()));
		  	       }
		  	       
		  	       if (prefs.contains("meditaciones")){
		  	    	   JSONArray meditaciones_all = new JSONArray (prefs.getString("meditaciones",""));
		  	    	   meditaciones = new FilterData().filterMeditaciones(meditaciones_all, pack.getString("id_pack"));
			  	        if (Config.log)
			  	        	Log.i("medita","Meditaciones: " + meditaciones.toString()); 
		  	       }
		  	       else{
		  	    	   //Error, no hay meditaciones.
		  	       }
		  	       
		  	   

				} catch (JSONException e) {
					Log.i("medita","Meditaciones error.");
				}
		    }
		}
		
		if (prefs.contains("meditaciones_entendido_" + pack.optString("id_pack"))){
			if (prefs.getBoolean("meditaciones_entendido_" + pack.optString("id_pack"), false))
				help.setVisibility(View.GONE);
		}
			
		 setListView();	
	
		
		atras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				if (duraciones){
					 animation = new TranslateAnimation(-width, 0, 0, 0);
		    		 animation.setDuration(400);
		    		 // animation.setFillAfter(true);
		    		 animation.setAnimationListener(new AnimationListener() {
		    			    @Override
		    			    public void onAnimationEnd(Animation arg0) {
		    			    	 disponibles.setText(pack.optString("pack_titulo"));
		    					 listview_duraciones.setVisibility(View.GONE); 
		    	    			 listview.setVisibility(View.VISIBLE); 
		    	    			 duraciones = false;
		    			    }

							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub
								
							}
		    			});
		    		 listview.startAnimation(animation);
		    		 animation = new TranslateAnimation(0, width,0, 0);
		    		 animation.setDuration(400);
		    		 //animation.setFillAfter(true);
		    		 animation.setAnimationListener(new AnimationListener() {
		    			    @Override
		    			    public void onAnimationEnd(Animation arg0) {
		    			    	 disponibles.setText(pack.optString("pack_titulo"));
		    					 listview_duraciones.setVisibility(View.GONE); 
		    	    			 listview.setVisibility(View.VISIBLE); 
		    	    			 duraciones = false;

		    			    }

							@Override
							public void onAnimationStart(Animation animation) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
								// TODO Auto-generated method stub
								
							}
		    			});
		    		 listview_duraciones.startAnimation(animation);		

		    		
					
				}
				else{
					Intent i = new Intent(Meditaciones.this, MainActivity.class);   
					i.setAction(Config.from_Meditaciones);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    		startActivity(i);
		    		finish();
				}							
				
			}
		});
	    entendido.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					help.animate()
				    .translationY(help.getHeight());
				    //.alpha(0.0f);
					
					prefs.edit().putBoolean("meditaciones_entendido_" + pack.optString("id_pack"), true).commit();
				    
				}
			});
	    
	    letrero.setText(pack.optString("descripcion"));
	    Drawable draw = getResources().getDrawable(R.drawable.atras_ico);
		draw.setColorFilter(Color.parseColor(pack.optString("pack_color_secundario")), PorterDuff.Mode.MULTIPLY );
		((ImageView)findViewById(R.id.id_meditaciones_atras_ico)).setImageDrawable(draw);
		
	}
	
	public void setListView(){
		adaptermeditaciones = new AdapterMeditaciones(this, meditaciones,pack);
	    listview.setAdapter(adaptermeditaciones);
	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	 @Override
	         public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

    			 meditacion = meditaciones.optJSONObject(position);
				 Log.i(Config.tag,"listview OnItemClickListener meditacion -> " + meditacion);

	    		 if (((pack.optInt("continuo") == 1) && (new FilterData().isPrevCompleted(meditaciones, meditacion))) || (pack.optInt("continuo") == 0)){
					 Log.i(Config.tag,"entra al primer if");
					 Log.i(Config.tag,"pack id_pack -> " + pack.optInt("id_pack"));
					 Log.i(Config.tag,"pack id_meditacion -> " + meditacion.optInt("id_meditacion"));

					 // popup que habia por defecto... :S
/*	    			 	 if ((pack.optInt("id_pack") == 1) && (meditacion.optInt("id_meditacion") == 47)){
	    			 		newsletter.setVisibility(View.VISIBLE);
	    			 		entendido_nl.setOnClickListener(new OnClickListener() {
	    						@Override
	    						public void onClick(View arg0) {
	    							 Intent i = new Intent(Meditaciones.this, Acercade.class); 
			    		    		 i.setAction(Intent.ACTION_GET_CONTENT);
		    			    		 startActivity(i);
		    			    		 finish();
	    						    
	    						}
	    					});
	    			 	 }*/

						// si el usuario no esta subscrito y es el pack gratuito
						if (!prefs.getBoolean(getString(R.string.suscrito), false) && pack.optInt("id_pack") == 1) {

							// comentar / descomentar en funcion del test para mostrar los popup correspondientes
/*							prefs.edit().remove("popup_primera_semana").commit();
							prefs.edit().remove("popup_segunda_semana").commit();
							prefs.edit().remove("popup_tercera_semana").commit();*/


							// comprobamos si hemos mostrado el popup de la primera semana
							if (meditacion.optInt("id_meditacion") == 46 && !prefs.contains("popup_primera_semana")) {

								if (Config.log) {
									Log.i(Config.tag,"mostrando popup_primera_semana");
								}
								prefs.edit().putBoolean("popup_primera_semana", true).apply();
								popup2Btn(getString(R.string.popup_primera_semana),
									getString(R.string.subscribe_now),
									getString(R.string.subscribe_not_now));
							}
							// comprobamos si hemos mostrado el popup de la segunda semana
							if (meditacion.optInt("id_meditacion") == 47 && !prefs.contains("popup_segunda_semana")){

								if (Config.log) {
									Log.i(Config.tag,"mostrando popup_segunda_semana");
								}
								prefs.edit().putBoolean("popup_segunda_semana", true).apply();
								popup2Btn(getString(R.string.popup_segunda_semana),
									getString(R.string.subscribe_now),
									getString(R.string.subscribe_not_now));
							}
							// comprobamos si hemos mostrado el popup de la tercera semana
							if (meditacion.optInt("id_meditacion") == 21  && !prefs.contains("popup_tercera_semana")){

								if (Config.log) {
									Log.i(Config.tag,"mostrando popup_tercera_semana");
								}
								prefs.edit().putBoolean("popup_tercera_semana", true).apply();
								popup2Btn(getString(R.string.popup_tercera_semana),
									getString(R.string.subscribe_now),
									getString(R.string.subscribe_not_now));
							}
						}

	    		 
		    		 	 durs = new ArrayList<String>();    		
		    			 disponibles.setText(meditacion.optString("med_titulo").trim());
		    			 
		    			 JSONArray durs_json;
						try {
							durs_json = new JSONArray(meditacion.optString("duraciones"));
							 if (meditacion.optInt("med_duracion") == 2){
			    				 durs.add(durs_json.optString(0));
			    				 durs.add(durs_json.optString(1));
			    			 }
			    			 else if (meditacion.optInt("med_duracion") == 3){
			    				 durs.add(durs_json.optString(0));
			    				 durs.add(durs_json.optString(1));
			    				 durs.add(durs_json.optString(2));	    		
			    			 }
			    			 else if (meditacion.optInt("med_duracion") == 1){
			    				 durs.add(durs_json.optString(0));
			    			 }	    			
			    			 else{
			    				 durs.add(durs_json.optString(0));
			    				 durs.add(durs_json.optString(1));
			    			 }	    	
						} catch (JSONException e) {
						}   							 
		    				
						 if (((meditacion.optInt("id_pack") == 1)) && (meditacion.optInt("orden") == 0))		    			 
							 adaptermeditacionesduracion = new AdapterMeditacionesDuracion(Meditaciones.this, durs,true);
						 else
							 adaptermeditacionesduracion = new AdapterMeditacionesDuracion(Meditaciones.this, durs,false);

		    			 listview_duraciones.setAdapter(adaptermeditacionesduracion);	    		
			    		 
			    		 animation = new TranslateAnimation(0, -width,0, 0);
			    		 animation.setDuration(400);
			    		 //animation.setFillAfter(true);
			    		 listview.startAnimation(animation);
			    		 animation = new TranslateAnimation(width, 0,0, 0);
			    		 animation.setDuration(400);
			    		 // animation.setFillAfter(true);
			    		 listview_duraciones.startAnimation(animation);		    		 
		    			 
		    			 
		    			 listview_duraciones.setVisibility(View.VISIBLE); 
		    			 listview.setVisibility(View.INVISIBLE); 
		    			 duraciones = true;
		    			 
		    			 listview_duraciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    		    	 @Override
		    		         public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
		    		    		 
		    		    		
	    		    			 Intent i = new Intent(Meditaciones.this, Reproductor.class);  
		    		    		 i.putExtra("meditacion", meditacion.toString());
		    		    		 i.putExtra("pack", pack.toString());
		    		    		 i.putExtra("duracion", durs.get(position));
	    						 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    			    		 startActivity(i);
	    			    		 finish();	    		    			 
		    		    		 
		    		    		 
		    		    		
		    		    	 }
		    			 });
	    		 }
	    	 }

        });
		
	}
	
	@Override
	public void onBackPressed()
	{
		
		if (duraciones){			
			 setListView();						
			 animation = new TranslateAnimation(-width, 0, 0, 0);
	   		 animation.setDuration(400);
	   		 // animation.setFillAfter(true);
	   		 listview.startAnimation(animation);
	   		 animation = new TranslateAnimation(0, width,0, 0);
	   		 animation.setDuration(400);
	   		 // animation.setFillAfter(true);
	   		 listview_duraciones.startAnimation(animation);	
			 disponibles.setText(pack.optString("pack_titulo"));
			 listview_duraciones.setVisibility(View.GONE); 
			 listview.setVisibility(View.VISIBLE); 
			 duraciones = false;
		}
		else{
			Intent i = new Intent(Meditaciones.this, MainActivity.class);   
			i.setAction(Config.from_Meditaciones);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	   		startActivity(i);
	   		finish();
		}	
		
	}

	protected void popup2Btn(String text, String bt1, String bt2){

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		final Dialog dialog = new Dialog(Meditaciones.this);
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

	private void goToSubscription(){
		// si esta registrado, va a la suscripcion. En caso contrario al login
		Intent i = new Intent(Meditaciones.this, Suscripcion.class);
		startActivity(i);
		finish();
		/*if (prefs.getBoolean(getString(R.string.registrado),false)){
			Intent i = new Intent(Meditaciones.this, Suscripcion.class);
			startActivity(i);
			finish();
		}else{
			Intent i = new Intent(Meditaciones.this, LogIn.class);
			startActivity(i);
			finish();
		}*/
	}

}
