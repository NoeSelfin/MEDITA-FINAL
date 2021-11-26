package org.simo.medita;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.extras.FilterData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Favoritos extends Activity{
	protected AdapterFavoritos adapterfavoritos;
	protected SlidingMenu menu_lateral;
	protected ListView listview;
	protected JSONArray favoritos;
	protected SharedPreferences prefs;
	protected Typeface font;
	protected LinearLayout atras;
	protected TextView nofav_btn;
	protected TextView titulo;
	protected RelativeLayout confav;
	protected RelativeLayout sinfav;
	protected ImageView atras_img;
	protected ImageView menu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_favoritos);
//		prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
		font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
		atras =  (LinearLayout)findViewById(R.id.id_fav_atras);
		titulo = (TextView)findViewById(R.id.id_main_inicio);
		((TextView)findViewById(R.id.id_fav_text1)).setTypeface(font);
		((TextView)findViewById(R.id.id_fav_text2)).setTypeface(font);
		titulo.setTypeface(font);
		nofav_btn = (TextView)findViewById(R.id.id_fav_btn);
		nofav_btn.setTypeface(font);
		confav = (RelativeLayout)findViewById(R.id.id_fav_confavoritos);
		sinfav = (RelativeLayout)findViewById(R.id.id_fav_sinfavoritos);
		listview = (ListView)findViewById(R.id.id_favoritos_listview);
		atras_img = (ImageView)findViewById(R.id.id_fav_back_img);
		
		if (prefs.contains("favoritos")){
			try {
				favoritos = new JSONArray (prefs.getString("favoritos", ""));
				if (Config.log)
					Log.i("medita_favoritos",favoritos.toString());

				//En verdad tendirmaos que mirar que el pack estubiera también comprado o con suscripción.
				if (checkpromo() || prefs.getBoolean(getString(R.string.suscrito), false)){
					Log.i("medita_favoritos","Opción A;");
					if (favoritos.length() > 0){
						titulo.setVisibility(View.INVISIBLE);
						confav.setVisibility(View.VISIBLE);
						sinfav.setVisibility(View.INVISIBLE);
						favoritos = new FilterData().prepareFavoritos(favoritos);
						if (Config.log)
							Log.i("medita",favoritos.toString());
						adapterfavoritos = new AdapterFavoritos(this, favoritos);
						listview.setAdapter(adapterfavoritos);
					}
					else{
						titulo.setVisibility(View.VISIBLE);
						confav.setVisibility(View.INVISIBLE);
						sinfav.setVisibility(View.VISIBLE);
					}
				}else{
					Log.i("medita_favoritos","Opción B;");
					Log.i("medita_favoritos",String.valueOf(favoritos.length()));
					JSONArray favoritos_aux = new JSONArray();
					for (int i=0;i<favoritos.length();i++){
						Log.i("medita_favoritos","In for loop;");
                        Log.i("medita_favoritos_gratis",favoritos.getJSONObject(i).getJSONObject("pack").optString("pack_precio"));
						if (favoritos.getJSONObject(i).getJSONObject("pack").optString("pack_precio").compareToIgnoreCase("0") == 0){
							favoritos_aux.put(favoritos.getJSONObject(i));
						}
					}
                    favoritos = favoritos_aux;
                    if (favoritos.length() > 0){
                        titulo.setVisibility(View.INVISIBLE);
                        confav.setVisibility(View.VISIBLE);
                        sinfav.setVisibility(View.INVISIBLE);
                        favoritos = new FilterData().prepareFavoritos(favoritos);
                        if (Config.log)
                            Log.i("medita",favoritos.toString());
                        adapterfavoritos = new AdapterFavoritos(this, favoritos);
                        listview.setAdapter(adapterfavoritos);
                    }
                    else{
                        titulo.setVisibility(View.VISIBLE);
                        confav.setVisibility(View.INVISIBLE);
                        sinfav.setVisibility(View.VISIBLE);
                    }
				}


				
			} catch (JSONException e) {
				titulo.setVisibility(View.VISIBLE);
				confav.setVisibility(View.INVISIBLE);
				sinfav.setVisibility(View.VISIBLE);
				Log.i("medita_favoritos",e.getMessage());
			}
		}
		else{
			titulo.setVisibility(View.VISIBLE);
			confav.setVisibility(View.INVISIBLE);
			sinfav.setVisibility(View.VISIBLE);
		}
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	 @Override
	         public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
	    		 
	    		 if (favoritos.optJSONObject(position).optString("tipo").compareTo("pack") != 0){
	    			 JSONObject meditacion = new JSONObject();
	    			 JSONObject pack = new JSONObject();
	    			 meditacion = favoritos.optJSONObject(position);
	    			 
	    			 try {
	    					 JSONArray fav = new JSONArray (prefs.getString("favoritos", ""));
	    					 pack = new FilterData().getPack(fav,meditacion.optString("id_meditacion"));	    	    			 
	    					 
	    	    			 Intent i = new Intent(Favoritos.this, Reproductor.class);  	    			
	    					 i.putExtra("pack",pack.toString());	    			
	    					 i.putExtra("meditacion", meditacion.toString());	    			
	    					 i.putExtra("duracion",meditacion.optString("duracion"));	    			
	    					 i.putExtra("dur", 0);	  
	    					 i.putExtra("intros", false);
	    					 startActivity(i);
	    		    		 finish();
	    			 } catch (JSONException e) {	    				 
	    			 }   			 
	    		 }			
	    	 }

       });		
					
		
		nofav_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Favoritos.this, MainActivity.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		
		atras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				menu_lateral.showMenu(true);
			}
		});
		
		if (favoritos != null){
			if (favoritos.length() > 0){
				
				Drawable draw = getResources().getDrawable(R.drawable.atras_ico);
				draw.setColorFilter(Color.parseColor(favoritos.optJSONObject(0).optString("pack_color_secundario")), PorterDuff.Mode.MULTIPLY );
				atras_img.setImageDrawable(draw);
				
			}
			
		}
	
	
		 setMenu();
	}
	
	
	@Override
	public void onBackPressed()
	{	
		Intent i = new Intent(Favoritos.this, MainActivity.class);   
		i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(i);		
		finish();
	}
	
	public void setMenu(){
		menu_lateral = new SlidingMenu(Favoritos.this);
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
		((View) menu_lateral.findViewById(R.id.id_menu_view_fav)).setVisibility(View.VISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_progreso)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_acercade)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_vision)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_opciones)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_sincro)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_sincro)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_suscription)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_contact)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_news)).setVisibility(View.INVISIBLE);


		LinearLayout contacto = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_contact_ll);
		contacto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Favoritos.this, LogIn.class);
				startActivity(i);
				finish();
			}
		});
		LinearLayout news = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_news_ll);
		news.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Favoritos.this, Novedades.class);
				startActivity(i);
				finish();
			}
		});
		LinearLayout acercade = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_acercade_ll);
		acercade.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Favoritos.this, Acercade.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout opciones = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_opciones_ll);
		opciones.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Favoritos.this, Opciones.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout vision = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_vision_ll);
		vision.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Favoritos.this, Vision.class);   
				 i.setAction("fromMenu");
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout progreso = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_progreso_ll);
		progreso.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Favoritos.this, Charts.class);
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout inicio = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_inicio_ll);
		inicio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Favoritos.this, MainActivity.class);   
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
						Intent i = new Intent(Favoritos.this, Sincro2.class);   
			    		 startActivity(i);
			    		 finish();
					}
					else{
						Intent i = new Intent(Favoritos.this, Sincro.class);   
			    		 startActivity(i);
			    		 finish();
					}
				}
				else{
					Intent i = new Intent(Favoritos.this, Sincro.class);   
		    		startActivity(i);
		    		finish();
				}
			}
		});
		LinearLayout compras = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_compras_ll);
		compras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new RecargarCompras(Favoritos.this);
			}
		});

		LinearLayout suscripcion = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_suscription_ll);
		suscripcion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Favoritos.this, Suscripcion.class);
				startActivity(i);
				finish();
				// si esta registrado, va a la suscripcion. En caso contrario al login
				/*if (prefs.getBoolean(getString(R.string.registrado),false)){
					Intent i = new Intent(Favoritos.this, Suscripcion.class);
					startActivity(i);
					finish();
				}else{
					Intent i = new Intent(Favoritos.this, LogIn.class);
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

		ImageView rs1 = menu_lateral.findViewById(R.id.id_rs1);
		rs1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String urlPage = "https://www.youtube.com/channel/UCOKXZZHPxigzEvJPd8vGvNw";
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
			}
		});
		ImageView rs2 = menu_lateral.findViewById(R.id.id_rs2);
		rs2.setOnClickListener(new OnClickListener() {
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
		rs3.setOnClickListener(new OnClickListener() {
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
		rs4.setOnClickListener(new OnClickListener() {
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
	private boolean checkpromo(){
		//[{"id_promo":"1","from":"2020-01-01 00:00:00","to":"2020","code":"abcd","type":"1","consumed":"0","deleted_at":null}]
		try {
			JSONObject jo = new JSONObject(prefs.getString("promo","{}"));
			if (jo.length() > 3){
				try {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					Date to = formatter.parse(jo.optString("to","2040-01-01 00:00:00"));
					Date from = formatter.parse(jo.optString("from","2010-01-01 00:00:00"));
					Date now = new Date();

					if (now.after(from) && now.before(to)){

						if(jo.optInt("type") == 2){
							return true;
						}else if ((jo.optInt("type") == 1)&& (jo.optInt("consumed") == 0)){
							return true;
						}
					}
				} catch (ParseException e) {
					//printLog("ParseException:" + e.getMessage());
				}
			}
		} catch (JSONException e) {
		}

		return false;
	}
}
