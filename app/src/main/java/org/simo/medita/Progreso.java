package org.simo.medita;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;

public class Progreso extends Activity{
	protected SharedPreferences prefs;
	protected SlidingMenu menu_lateral;
	protected Typeface font;
	protected Typeface font_negrita;
	protected TextView btn_ok;
	protected TextView btn_ko;
	//protected TextView texto1;
	protected TextView texto2;
	protected TextView puntos_texto;
	protected ScrollView scroll;
	protected ImageView mapa;
	protected LinearLayout txt;
	protected LinearLayout btns;
	protected RelativeLayout sugerencia;
	protected LinearLayout atras;
	protected int nivel = 0;
	protected int marcador = 1;
	protected ImageView menu;
	protected TextView regalo_txt;
	protected RelativeLayout regalo_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_progreso);
		font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
		font_negrita = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Bold.otf");
//		prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
		puntos_texto =  (TextView)findViewById(R.id.id_progreso_puntos_texto);		
		//texto1 =  (TextView)findViewById(R.id.id_progreso_texto1);	
		texto2 =  (TextView)findViewById(R.id.id_progreso_texto2);	
		scroll = (ScrollView)findViewById(R.id.id_progreso_scroll);
		txt = (LinearLayout)findViewById(R.id.id_progreso_texto);		
		btns = (LinearLayout)findViewById(R.id.id_progreso_btns);	
		btn_ok = (TextView)findViewById(R.id.id_progreso_ok);
		btn_ko = (TextView)findViewById(R.id.id_progreso_ko);	
		regalo_txt = (TextView)findViewById(R.id.id_progreso_regalo);	
		regalo_btn = (RelativeLayout)findViewById(R.id.id_progreso_regalo_rl);	
		mapa = (ImageView)findViewById(R.id.id_progreso_mapa);
		sugerencia = (RelativeLayout)findViewById(R.id.id_progreso_sugerencia);
		atras =  (LinearLayout)findViewById(R.id.id_progreso_atras);
		btn_ok.setTypeface(font);
		btn_ko.setTypeface(font);
		
		puntos_texto.setTypeface(font_negrita);
		//texto1.setTypeface(font);
		texto2.setTypeface(font);
		
		setNivel();
		
		
		
		if ((nivel==0)||(nivel==1)||(nivel==2)){
			scroll.post(new Runnable() {
		        @Override
		        public void run() {
		        	//scroll.fullScroll(ScrollView.FOCUS_DOWN);
		        }
		    });	
		}
		
		else if ((nivel==3)||(nivel==4)||(nivel==5)){
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			final int height = size.y;
			
			scroll.post(new Runnable() {
		        @Override
		        public void run() {
		        	//scroll.scrollTo(0, height);
		        }
		    });
			
		}		
		else if ((nivel==6)||(nivel==7)||(nivel==8)){
			scroll.post(new Runnable() {
		        @Override
		        public void run() {
		        	//scroll.fullScroll(ScrollView.FOCUS_UP);
		        }
		    });	
			
		}	
		else{
			scroll.post(new Runnable() {
		        @Override
		        public void run() {
		        	//scroll.fullScroll(ScrollView.FOCUS_DOWN);
		        }
		    });	
		}
		
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			
				
				 if (marcador == 1){
					 Intent i = new Intent(Progreso.this, Acercade.class);   
		    		 startActivity(i);
		    		 finish();	
				 }
				 else if (marcador == 2){
					 Intent i = new Intent(Progreso.this, Vision.class);   
					 i.setAction("fromMenu");
		    		 startActivity(i);
		    		 finish();	
				 }
				 else if (marcador == 3){
					 Intent i = new Intent(Progreso.this, MainActivity.class);   
		    		 startActivity(i);
		    		 finish();	
				 }
				 else if (marcador == 4){
					try {
						JSONArray packs = new JSONArray(prefs.getString("packs", ""));
						Intent i = new Intent(Progreso.this, Meditaciones.class);  
			    		 i.putExtra("pack", packs.optJSONObject(0).toString());
			    		 startActivity(i);
			    		 finish();	
					} catch (JSONException e) {
					}					 
				 }
				 else if (marcador == 5){
					 try {
							JSONArray packs = new JSONArray(prefs.getString("packs", ""));
							Intent i = new Intent(Progreso.this, Meditaciones.class);  
				    		 i.putExtra("pack", packs.optJSONObject(0).toString());
				    		 startActivity(i);
				    		 finish();	
						} catch (JSONException e) {
						}
				 }
				 else if (marcador == 6){
					 try {
							JSONArray packs = new JSONArray(prefs.getString("packs", ""));
							Intent i = new Intent(Progreso.this, Meditaciones.class);  
				    		 i.putExtra("pack", packs.optJSONObject(0).toString());
				    		 startActivity(i);
				    		 finish();	
						} catch (JSONException e) {
						}
				 }
				 else if (marcador == 7){
					 /*try {
							JSONArray packs = new JSONArray(prefs.getString("packs", ""));
							Intent i = new Intent(Progreso.this, Compras.class);  
				    		 i.putExtra("pack", packs.optJSONObject(1).toString());
				    		 startActivity(i);
				    		 finish();	
						} catch (JSONException e) {
						}*/
					 	Intent i = new Intent(Progreso.this, Novedades.class);
						i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
						startActivity(i);		
						finish();
				 }
				 else if (marcador == 8){
					 /*try {
							JSONArray packs = new JSONArray(prefs.getString("packs", ""));
							Intent i = new Intent(Progreso.this, Compras.class);  
				    		 i.putExtra("pack", packs.optJSONObject(2).toString());
				    		 startActivity(i);
				    		 finish();	
						} catch (JSONException e) {
						}*/
					 	Intent i = new Intent(Progreso.this, Registro.class);
						i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
						startActivity(i);		
						finish();
				 }
				 else if (marcador == 9){
					 /*try {
							JSONArray packs = new JSONArray(prefs.getString("packs", ""));
							Intent i = new Intent(Progreso.this, Compras.class);  
				    		 i.putExtra("pack", packs.optJSONObject(3).toString());
				    		 startActivity(i);
				    		 finish();	
						} catch (JSONException e) {
						}*/
					 	Intent i = new Intent(Progreso.this, MainActivity.class);   
						i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
						startActivity(i);		
						finish();
				 }
				 else if (marcador == 10){
					/* try {
							JSONArray packs = new JSONArray(prefs.getString("packs", ""));
							Intent i = new Intent(Progreso.this, Compras.class);  
				    		 i.putExtra("pack", packs.optJSONObject(4).toString());
				    		 startActivity(i);
				    		 finish();	
						} catch (JSONException e) {
						}*/
					 	Intent i = new Intent(Progreso.this, MainActivity.class);   
						i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
						startActivity(i);		
						finish();
				 }
										
			}
		});
		btn_ko.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sugerencia.animate()
			    .translationY(sugerencia.getHeight());
			    //.alpha(0.0f);	
			}
		});
		
		atras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Progreso.this, Charts.class);
				startActivity(i);
				finish();
			}
		});
		regalo_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Progreso.this, Regalos.class);  
				i.putExtra("nivel", nivel);
				//i.putExtra("origen", 1);
	    		startActivity(i);
	    		finish();	
			}
		});
		
		 setMenu();
		
	}
	
	
	protected void setNivel(){
		
		if(!prefs.contains("Premios_10")){
			marcador = 10;
			texto2.setText(Html.fromHtml("Termina uno de nuestros packs y te recompensaremos con <b>100 pts.</b>"));
			/*try {
				JSONArray packs = new JSONArray(prefs.getString("packs", ""));
				//texto2.setText(Html.fromHtml("Compra el pack "+packs.optJSONObject(4).optString("pack_titulo")+" y te recompensaremos con <b>100 pts.</b>"));
				texto2.setText(Html.fromHtml("Compra uno de nuestros packs y te recompensaremos con <b>100 pts.</b>"));
				} catch (JSONException e) {
			}*/
		}
		if(!prefs.contains("Premios_9")){
			marcador = 9;
			texto2.setText(Html.fromHtml("Termina uno de nuestros packs y te recompensaremos con <b>100 pts.</b>"));
			/*try {
				JSONArray packs = new JSONArray(prefs.getString("packs", ""));
				//texto2.setText(Html.fromHtml("Compra el pack "+packs.optJSONObject(3).optString("pack_titulo")+" y te recompensaremos con <b>100 pts.</b>"));
				texto2.setText(Html.fromHtml("Compra uno de nuestros packs y te recompensaremos con <b>100 pts.</b>"));
				} catch (JSONException e) {
			}*/
		}
		if(!prefs.contains("Premios_8")){
			marcador = 8;
			texto2.setText(Html.fromHtml("Termina uno de nuestros packs y te recompensaremos con <b>100 pts.</b>"));

			/*try {
				JSONArray packs = new JSONArray(prefs.getString("packs", ""));
				//texto2.setText(Html.fromHtml("Compra el pack "+packs.optJSONObject(2).optString("pack_titulo")+" y te recompensaremos con <b>100 pts.</b>"));
				texto2.setText(Html.fromHtml("Compra uno de nuestros packs y te recompensaremos con <b>100 pts.</b>"));
				} catch (JSONException e) {
			}*/
		}
		if(!prefs.contains("Premios_7")){
			marcador = 7;
			try {
				JSONArray packs = new JSONArray(prefs.getString("packs", ""));
				//texto2.setText(Html.fromHtml("Compra el pack "+packs.optJSONObject(1).optString("pack_titulo")+" y te recompensaremos con <b>100 pts.</b>"));
				texto2.setText(Html.fromHtml("Compra uno de nuestros packs y te recompensaremos con <b>100 pts.</b>"));
			} catch (JSONException e) {
			}
		}
		if(!prefs.contains("Premios_6")){
			marcador = 6;
			texto2.setText(Html.fromHtml(getResources().getString(R.string.progreso_text7)));
		}
		if(!prefs.contains("Premios_5")){
			marcador = 5;
			texto2.setText(Html.fromHtml(getResources().getString(R.string.progreso_text6)));
		}
		if(!prefs.contains("Premios_4")){
			marcador = 4;
			texto2.setText(Html.fromHtml(getResources().getString(R.string.progreso_text5)));
		}
		if(!prefs.contains("Premios_3")){
			marcador = 3;
			texto2.setText(Html.fromHtml(getResources().getString(R.string.progreso_text4)));
		}
		if(!prefs.contains("Premios_2")){
			marcador = 2;
			texto2.setText(Html.fromHtml(getResources().getString(R.string.progreso_text3)));
		}
		if(!prefs.contains("Premios_1")){
			marcador = 1;
			texto2.setText(Html.fromHtml(getResources().getString(R.string.progreso_text2)));
		}
		
		
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


		if (nivel == 1)
			mapa.setImageResource(R.drawable.mapa1);
		else if (nivel == 2)
			mapa.setImageResource(R.drawable.mapa2);
		else if (nivel == 3)
			mapa.setImageResource(R.drawable.mapa3);
		else if (nivel == 4)
			mapa.setImageResource(R.drawable.mapa4);
		else if (nivel == 5)
			mapa.setImageResource(R.drawable.mapa5);
		else if (nivel == 6)
			mapa.setImageResource(R.drawable.mapa6);
		else if (nivel == 7)
			mapa.setImageResource(R.drawable.mapa7);
		else if (nivel == 8)
			mapa.setImageResource(R.drawable.mapa8);
		else if (nivel == 9)
			mapa.setImageResource(R.drawable.mapa9);
		else if (nivel == 10)
			mapa.setImageResource(R.drawable.mapa10);
		
		
		puntos_texto.setText(String.valueOf(nivel * 100));	
		regalo_txt.setText(String.valueOf(nivel));
		regalo_txt.setTypeface(font);
		
		
		
		if (nivel == 1){
			if (!prefs.contains("regalo1")){
				alertRegalos(nivel);
				prefs.edit().putBoolean("regalo1", true).commit();
			}
		}
		else if (nivel == 2){
			if (!prefs.contains("regalo2")){
				alertRegalos(nivel);
				prefs.edit().putBoolean("regalo2", true).commit();
			}
		}
		else if (nivel == 3){
			if (!prefs.contains("regalo3")){
				alertRegalos(nivel);
				prefs.edit().putBoolean("regalo3", true).commit();
			}
		}
		else if (nivel == 4){
			if (!prefs.contains("regalo4")){
				alertRegalos(nivel);
				prefs.edit().putBoolean("regalo4", true).commit();
			}
			
		}
		else if (nivel == 5){
			if (!prefs.contains("regalo5")){
				alertRegalos(nivel);
				prefs.edit().putBoolean("regalo5", true).commit();
			}
		}
		else if (nivel == 6){
			if (!prefs.contains("regalo6")){
				alertRegalos(nivel);
				prefs.edit().putBoolean("regalo6", true).commit();
			}
		}
		else if (nivel == 7){
			if (!prefs.contains("regalo7")){
				alertRegalos(nivel);
				prefs.edit().putBoolean("regalo7", true).commit();
			}
		}
		else if (nivel == 8){
			if (!prefs.contains("regalo8")){
				alertRegalos(nivel);
				prefs.edit().putBoolean("regalo8", true).commit();
			}
		}
		else if (nivel == 9){
			if (!prefs.contains("regalo9")){
				alertRegalos(nivel);
				prefs.edit().putBoolean("regalo9", true).commit();
			}
		}
		else if (nivel == 10){
			if (!prefs.contains("regalo10")){
				alertRegalos(nivel);
				prefs.edit().putBoolean("regalo10", true).commit();
			}
			
			sugerencia.setVisibility(View.INVISIBLE);
		}
		
		
		
	}
	
		
	@Override
	public void onBackPressed()
	{	
		Intent i = new Intent(Progreso.this, MainActivity.class);   
		i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(i);		
		finish();
	}
	
	public void setMenu(){
		menu_lateral = new SlidingMenu(Progreso.this);
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
		((View) menu_lateral.findViewById(R.id.id_menu_view_progreso)).setVisibility(View.VISIBLE);
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
				Intent i = new Intent(Progreso.this, LogIn.class);
				startActivity(i);
				finish();
			}
		});
		LinearLayout news = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_news_ll);
		news.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Progreso.this, Novedades.class);
				startActivity(i);
				finish();
			}
		});
		LinearLayout acercade = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_acercade_ll);
		acercade.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Progreso.this, Acercade.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout opciones = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_opciones_ll);
		opciones.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Progreso.this, Opciones.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout vision = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_vision_ll);
		vision.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Progreso.this, Vision.class); 
				 i.setAction("fromMenu");
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout favoritos = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_fav_ll);
		favoritos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Progreso.this, Favoritos.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});

		LinearLayout inicio = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_inicio_ll);
		inicio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Progreso.this, MainActivity.class);   
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
						Intent i = new Intent(Progreso.this, Sincro2.class);   
			    		 startActivity(i);
			    		 finish();
					}
					else{
						Intent i = new Intent(Progreso.this, Sincro.class);   
			    		 startActivity(i);
			    		 finish();
					}
				}
				else{
					Intent i = new Intent(Progreso.this, Sincro.class);   
		    		startActivity(i);
		    		finish();
				}
			}
		});
		LinearLayout compras = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_compras_ll);
		compras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new RecargarCompras(Progreso.this);
			}
		});

		LinearLayout suscripcion = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_suscription_ll);
		suscripcion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// si esta registrado, va a la suscripcion. En caso contrario al login
				Intent i = new Intent(Progreso.this, Suscripcion.class);
				startActivity(i);
				finish();
				/*if (prefs.getBoolean(getString(R.string.registrado),false)){
					Intent i = new Intent(Progreso.this, Suscripcion.class);
					startActivity(i);
					finish();
				}else{
					Intent i = new Intent(Progreso.this, LogIn.class);
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
	
	   protected void alertRegalos(int regalo){
	 		
				final Dialog dialog = new Dialog(Progreso.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.alert_regalos);
			dialog.setCancelable(true);

			// set the custom dialog components - text, image and button
			TextView ver = (TextView) dialog.findViewById(R.id.id_alert_btn);
			TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
			TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);
		
			
			text.setTypeface(font);
			ver.setTypeface(font);
			titulo.setTypeface(font);

			// if button is clicked, close the custom dialog
			ver.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					Intent i = new Intent(Progreso.this, Regalos.class);  
					i.putExtra("nivel", nivel);
					i.putExtra("origen", 0);
		    		startActivity(i);
		    		finish();
				}
			});

			dialog.show();
		  }

}
