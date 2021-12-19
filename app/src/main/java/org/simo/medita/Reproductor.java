package org.simo.medita;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.extras.Basics;
import org.simo.medita.extras.FilterData;
import org.simo.medita.extras.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class Reproductor extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnBufferingUpdateListener{
	protected SharedPreferences prefs;
	protected JSONObject meditacion;
	protected JSONObject pack;
	protected JSONArray meditaciones;
	
	protected LinearLayout atras;
	protected RelativeLayout bg;
	protected ImageView bg_img;
	protected ImageView play;
	protected ImageView play_left;
	protected ImageView  play_right;	
	protected ImageView  icono;
	protected ImageView  download;
	protected ImageView  music;
	protected Typeface font;
	protected ImageView loading;
	
	private SeekBar songProgressBar;
	protected TextView dia;
	protected TextView duracion;
	protected TextView time;
	protected TextView time_left;	
	protected TextView song_name;	
	protected TextView introduccion;
	protected TextView titulo_pack;
	protected TextView helper;
	protected  Bitmap bitmap;
	
	
	protected static MediaPlayer mp;
	protected static MediaPlayer mp_sound;
	private Utilities utils;
	private Handler mHandler = new Handler();
	
	private String dur = "2";
	
	protected boolean isIntro = true;
	protected  long currentTime = 0;
	protected String med_dia;
	
	protected ImageView favoritos_img;
	protected boolean favorito = false;
	protected boolean intros = true;
	protected boolean muted = false;
	protected int bg_sound = 0;
	protected static boolean play_block = true;
	protected boolean back = false;
	protected boolean fromMain = false;
	protected boolean fromHome = false;

	FileInputStream fileInputStream;
	FileInputStream fileInputStreamSound;

	protected boolean introActivated = false;

	protected MeditationFunctions funcs;
	protected double total1 = 0;
	Timer T;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_reproductor);	
//		prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
		font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
		play =  (ImageView)findViewById(R.id.id_reproductor_play);
		play_right =  (ImageView)findViewById(R.id.id_reproductor_play_rigth);
		play_left =  (ImageView)findViewById(R.id.id_reproductor_play_left);
		atras =  (LinearLayout)findViewById(R.id.id_reproductor_atras);
		bg =  (RelativeLayout)findViewById(R.id.id_reproductor_bg);		
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		song_name = (TextView)findViewById(R.id.id_reproductor_titulo);
		dia = (TextView)findViewById(R.id.id_reproductor_dia);
		time = (TextView)findViewById(R.id.id_reproductor_tiempo);
		time_left = (TextView)findViewById(R.id.id_reproductor_tiempo_rest);
		duracion = (TextView)findViewById(R.id.id_reproductor_dur);	
		introduccion = (TextView)findViewById(R.id.id_reproductor_intro);
		favoritos_img =  (ImageView)findViewById(R.id.id_reproductor_favoritos);		
		loading = (ImageView) findViewById(R.id.id_reproductor_play_loading);
		bg_img = (ImageView) findViewById(R.id.id_reproductor_bg_img);
		icono = (ImageView) findViewById(R.id.id_reproductor_ico);
		download = (ImageView) findViewById(R.id.id_reproductor_download);
		music = (ImageView) findViewById(R.id.id_reproductor_music);
		titulo_pack = (TextView)findViewById(R.id.id_reproductor_pack);
		helper = (TextView)findViewById(R.id.id_reproductor_helper);
		
		introduccion.setTypeface(font);
		time.setTypeface(font);
		time_left.setTypeface(font);
		dia.setTypeface(font);
		duracion.setTypeface(font);
		titulo_pack.setTypeface(font);

		funcs= new MeditationFunctions(this);
			
		 // Mediaplayer
        mp = new MediaPlayer();
		//mp_sound = new MediaPlayer();
        utils = new Utilities();
        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this);
        mp.setOnCompletionListener(this);
        mp.setOnBufferingUpdateListener(this);

		//mp_sound.setOnCompletionListener(this);
		//mp_sound.setOnBufferingUpdateListener(this);
        
        meditacion = new JSONObject();
        pack = new JSONObject();        
	
		Bundle extras = getIntent().getExtras();
		if(extras != null) {
			Log.i(Config.tag,"Reproductor -> intent extras:");

			for (String key : extras.keySet()) {
				Object value = extras.get(key);
				Log.i(Config.tag, String.format("clave: %s -> valor: %s -> value.getclass.getname: (%s)", key,
						value.toString(), value.getClass().getName()));
			}


			try {
				meditacion = new JSONObject(extras.getString("meditacion"));
				pack = new JSONObject(extras.getString("pack"));
				dur = extras.getString("duracion");
				bg_sound = Integer.valueOf(meditacion.optString("musica","0"));

				Log.i(Config.tag +"_med",meditacion.toString());
				
				if (extras.containsKey("fromMain"))
					fromMain = extras.getBoolean("fromMain", false);
				if (extras.containsKey("fromHome"))
					fromHome = extras.getBoolean("fromHome", false);

				
				song_name.setText("· " + meditacion.optString("med_titulo").toUpperCase().trim() +" ·");
				song_name.setTypeface(font);
				
				if (Integer.valueOf(meditacion.optString("med_dia")) == 0)
					dia.setText("");
				else
					dia.setText("DÍA " + meditacion.optString("med_dia"));

	  		    BitmapFactory.Options options = new BitmapFactory.Options();
	  	        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	  	        //Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Medita/"+pack.getString("pack_fondo_rep"), options);
		  	    bitmap = Basics.readFileInternal(this,"fondos",pack.getString("pack_fondo_rep"));
	  	     
	  	       if (bitmap!=null){ 
	  	    	 bg_img.setImageBitmap(bitmap); 
	  	    	 // bg.setBackground(new BitmapDrawable(this.getResources(), bitmap));
	  	       }
	  	       else{
	  	    	 bg.setBackgroundColor(Color.parseColor(pack.getString("pack_color").trim()));
	  	       }	  	      
	  	       
	  	       
	  	      bitmap = Basics.readFileInternal(this,"iconos",pack.getString("pack_icono"));
	  			
	  	       if (bitmap!=null){ 
	  	    	 icono.setImageBitmap(bitmap);
	  	       }
 	       

	  	       if (extras.containsKey("intros"))
	  	    	   intros = extras.getBoolean("intros");	  	       
	  	       
	  	    	if (extras.containsKey("dur"))
	  	    		currentTime = extras.getLong("dur", 0);
	  	    	else
	  	    		currentTime = 0;   
	  	    	
	  	    	mp.seekTo((int)currentTime);
				/*if(mp_sound != null){
					mp_sound.seekTo((int)currentTime);
				}*/

		        time.setText(""+utils.milliSecondsToTimer(currentTime));
		       
		        titulo_pack.setText(pack.optString("pack_titulo").toUpperCase());

		        if (bg_sound == 0){
					music.setVisibility(View.INVISIBLE);
				}else{
					music.setVisibility(View.VISIBLE);
				}
	  	    	
	  	       
			} catch (JSONException e) {
				Log.i("medita","Reproduccion error.");
			}
	    } 
		
		
		meditaciones = new JSONArray();
		if (prefs.contains("meditaciones")){
			try {
				meditaciones = new JSONArray (prefs.getString("meditaciones",""));
	    	    meditaciones = new FilterData().filterMeditaciones(meditaciones, pack.getString("id_pack"));

			} catch (JSONException e) {
			}
	  	
       }
		
		if (new FilterData().isFirst(meditaciones, meditacion.optString("id_meditacion")))
				play_left.setVisibility(View.INVISIBLE);
		else{
			if(fromHome){
				play_left.setVisibility(View.INVISIBLE);
			}else{
				play_left.setVisibility(View.VISIBLE);
			}
		}
		if (new FilterData().isLast(meditaciones, meditacion.optString("id_meditacion")))
			play_right.setVisibility(View.INVISIBLE);
		else{
			if(fromHome){
				play_right.setVisibility(View.INVISIBLE);
			}else{
				play_right.setVisibility(View.VISIBLE);
			}
		}

		
		/*AnimationDrawable loadingAnimation = new AnimationDrawable();
		loading.setBackground( new BitmapDrawable(getResources(), loadBitmapFromAsset("player/player_00000.png")));	        
        AddFrames af =  new AddFrames(this, loadingAnimation);
        af.addPlayerFrames();
        loadingAnimation = af.getAnimation();
        loading.setBackground(loadingAnimation); 
        loadingAnimation.start();*/
		
		((AnimationDrawable) loading.getBackground()).start();
		
		saveState();		
		
		if (meditacion.optInt("introduccion") == 0)
			intros = false;
		
		if (intros){
			if (prefs.contains("opciones_intros")){
				if (!prefs.getBoolean("opciones_intros", true)){
					introduccion.setVisibility(View.INVISIBLE);	
					isIntro = false;
				}
				else
					introduccion.setVisibility(View.VISIBLE);
			}				
		}	
		else{
			introduccion.setVisibility(View.INVISIBLE);	
			isIntro = false;
		}
		
		setFavoritos();
				
		String song = null;
		String song_sound = null;
		if (isIntro){
			duracion.setText("INTRODUCCIÓN");
			song =meditacion.optString("med_fichero") + "Intro.mp3";
			favoritos_img.setVisibility(View.INVISIBLE);
			//prepareSong(meditacion.optString("med_fichero") + "Intro.mp3");

			helper.setVisibility(View.INVISIBLE);
			helper.setVisibility(View.GONE);

			download.setVisibility(View.INVISIBLE);
			music.setVisibility(View.INVISIBLE);
		}
		else{			
			
			favoritos_img.setVisibility(View.VISIBLE);
			helper.setVisibility(View.VISIBLE);
            helper.setVisibility(View.GONE);
			
			song = meditacion.optString("med_fichero") + "M"+dur+".mp3";
			song_sound = meditacion.optString("med_fichero") + "M"+dur+"Sound.mp3";

			int d = Integer.valueOf(dur.trim());
			if (d < 5)
				duracion.setText("DURACIÓN CORTA");

			if (d == 5)
				duracion.setText("DURACIÓN MEDIA");

			if (d > 9)
				duracion.setText("DURACIÓN LARGA");
	
			
			/*if (dur.compareTo("Corta 2 MIN") == 0){
				duracion.setText("DURACIÓN CORTA");
				song =meditacion.optString("med_fichero") + "M2.mp3";
				//prepareSong(meditacion.optString("med_fichero") + "M2.mp3");
			}		
			else if (dur.compareTo("Media 5 MIN") == 0){
				duracion.setText("DURACIÓN MEDIA");
				song =meditacion.optString("med_fichero") + "M5.mp3";
				//prepareSong(meditacion.optString("med_fichero") + "M5.mp3");
			}			
			else if (dur.compareTo("Larga 10 MIN") == 0){
				duracion.setText("DURACIÓN LARGA");
				song =meditacion.optString("med_fichero") + "M10.mp3";
				//prepareSong(meditacion.optString("med_fichero") + "M10.mp3");
			}
			else if (dur.compareTo("Larga 15 MIN") == 0){
				duracion.setText("DURACIÓN LARGA");
				song =meditacion.optString("med_fichero") + "M15.mp3";
				//prepareSong(meditacion.optString("med_fichero") + "M10.mp3");
			}
			else{
				duracion.setText("DURACIÓN CORTA");
				song =meditacion.optString("med_fichero") + "M2.mp3";
			}*/
		
		}
		
	   
		
		favoritos_img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				prefs.edit().putBoolean("Premios_3", true).commit();

				JSONArray favs;
				if (prefs.contains("favoritos")){
					try {
						favs = new JSONArray(prefs.getString("favoritos", ""));
						if (favorito){
		    				favoritos_img.setBackgroundResource(R.drawable.no_favorite_down);
		    				favs = new FilterData().unSetFavoritoDur(favs,meditacion,dur);
		    				favorito = false;
		    			}
		    			else{
		    				
		    				favs = new FilterData().setFavoritoDur(favs,meditacion,pack,dur);
		    				favorito = true;		    				
		    				
		    				favoritos_img.setBackgroundResource(R.drawable.favorite);
		    				Animation animationScaleUp = AnimationUtils.loadAnimation(Reproductor.this, R.anim.popout);
		    				final Animation animationScaleDown = AnimationUtils.loadAnimation(Reproductor.this, R.anim.popin);		   				
		    		
		    				favoritos_img.startAnimation(animationScaleUp);
		    				
		    				animationScaleUp.setAnimationListener(new Animation.AnimationListener()
		    				{
		    				    @Override
		    				    public void onAnimationStart(Animation animation)
		    				    {
		    				    }
		    				     @Override
		    				    public void onAnimationEnd(Animation animation)
		    				    {
		    				    	 favoritos_img.startAnimation(animationScaleDown);
		    				    }
		    				     @Override
		    				    public void onAnimationRepeat(Animation animation)
		    				    {
		    				    }
		    				});		    				

		    			}
						prefs.edit().putString("favoritos", favs.toString()).commit();
					} catch (JSONException e) {
					}
				}		
				else{
					favs = new JSONArray();					
					JSONObject packs_fav = new JSONObject();
					JSONArray med_fav = new JSONArray();
					
					try {
						packs_fav.put("pack", pack);
						meditacion.put("duracion", dur);
						med_fav.put(meditacion);						
						packs_fav.put("meditaciones", med_fav);
						
						favs.put(packs_fav); 					
						prefs.edit().putString("favoritos", favs.toString()).commit();
						
						
						favorito = true;
						
						favoritos_img.setBackgroundResource(R.drawable.favorite);
	    				Animation animationScaleUp = AnimationUtils.loadAnimation(Reproductor.this, R.anim.popout);
	    				final Animation animationScaleDown = AnimationUtils.loadAnimation(Reproductor.this, R.anim.popin);		    				
	    				
	    				favoritos_img.startAnimation(animationScaleUp);	    				
	    				animationScaleUp.setAnimationListener(new Animation.AnimationListener()
	    				{
	    				    @Override
	    				    public void onAnimationStart(Animation animation)
	    				    {
	    				    }
	    				     @Override
	    				    public void onAnimationEnd(Animation animation)
	    				    {
	    				    	 favoritos_img.startAnimation(animationScaleDown);
	    				    }
	    				     @Override
	    				    public void onAnimationRepeat(Animation animation)
	    				    {
	    				    }
	    				});						

					
					} catch (JSONException e) {
					}
				
				}
				
			}
		});
		
		play_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				T.cancel();
				funcs.setTotalSecondsMed((int)total1);
                funcs.setTotalDaySeconds((int)total1);
                funcs.setTotalSectionsSeconds((int)total1, meditacion.optString("id_meditacion"));
				Log.i(Config.tag,String.valueOf(total1));
				total1 = 0;
				 JSONObject prev = new JSONObject();
				 prev = new FilterData().prevMed(meditaciones, meditacion.optString("id_meditacion"));
				 JSONArray d;
					try {
						d = new JSONArray(prev.optString("duraciones"));
						if (prev.optInt("med_duracion") == 1){
							 dur = d.getString(0);
						 }
					} catch (JSONException e) {
					}
				 if (prev != null){
					 if (prev.length() > 0){
						 mp.stop();
						 /*if(mp_sound != null){
							 mp_sound.stop();
						 }*/
						 bitmap = null;
						 Intent i = new Intent(Reproductor.this, Reproductor.class);  	    			
						 i.putExtra("pack",pack.toString());	    			
						 i.putExtra("meditacion", prev.toString());	    			
						 i.putExtra("duracion",dur);	    			
						 i.putExtra("dur", 0);	   			
						 startActivity(i);
			    		 finish();					 
					 }
					
				 }
				
			}
		});
		
		play_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				T.cancel();
				funcs.setTotalSecondsMed((int)total1);
				funcs.setTotalDaySeconds((int)total1);
				funcs.setTotalSectionsSeconds((int)total1, meditacion.optString("id_meditacion"));
				Log.i(Config.tag,String.valueOf(total1));
				total1 = 0;
				if (isIntro){
					introduccion.performClick();
				}
				else{
					 JSONObject next = new JSONObject();
					 next = new FilterData().nextMed(meditaciones, meditacion.optString("id_meditacion"));
					 JSONArray d;
					try {
						d = new JSONArray(next.optString("duraciones"));
						if (next.optInt("med_duracion") == 1){
							 dur = d.getString(0);
						 }
					} catch (JSONException e) {
					}
					 
					 
					 
					 if (next != null){
						 if (next.length() > 0){
							 mp.stop();
							 /*if(mp_sound != null){
								 mp_sound.stop();
							 }*/
							 time.setText("0:00");
						     time_left.setText("0:00");
							 bitmap = null;
							 Intent i = new Intent(Reproductor.this, Reproductor.class);  	    			
							 i.putExtra("pack",pack.toString());	    			
							 i.putExtra("meditacion", next.toString());	    			
							 i.putExtra("duracion",dur);	    			
							 i.putExtra("dur", 0);	   			
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
				if (!play_block){
					AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
					int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
					
					if (currentVolume == 0)
						Toast.makeText(Reproductor.this, "El volumen está desactivado.", Toast.LENGTH_SHORT).show();
					else if ((currentVolume == 1) || (currentVolume == 1))
						Toast.makeText(Reproductor.this, "El volumen está demasiado bajo.", Toast.LENGTH_SHORT).show();
	
					if (mp.isPlaying()){
						mp.pause();
						/*if(mp_sound != null){
							mp_sound.pause();
						}*/
						play.setBackgroundResource(R.drawable.play_button);

						//Lo desactivo porqué en versiones nuevas da error
						//audio.setStreamMute(AudioManager.STREAM_RING,  false);
						T.cancel();
						funcs.setTotalSecondsMed((int)total1);
                        funcs.setTotalDaySeconds((int)total1);
						funcs.setTotalSectionsSeconds((int)total1, meditacion.optString("id_meditacion"));
						Log.i(Config.tag,String.valueOf(total1));
						total1 = 0;

					}
					else{
						mp.start();
						/*if(mp_sound != null){
							mp_sound.start();
						}*/

						play.setBackgroundResource(R.drawable.pause_button);
						updateProgressBar();						
						if (prefs.contains("opciones_nomolestar")){
							if (prefs.getBoolean("opciones_nomolestar", true)){
								//Lo desactivo porqué en versiones nuevas da error
			 					//audio.setStreamMute(AudioManager.STREAM_RING,  true);
							}    
						}
						JSONArray meditaciones_aux = new JSONArray();
						if (prefs.contains("meditaciones")){
							try {
								meditaciones_aux = new JSONArray (prefs.getString("meditaciones",""));
								JSONArray ja = new FilterData().setCompleted(meditaciones_aux, meditacion);
								prefs.edit().putString("meditaciones", ja.toString()).commit();
							} catch (JSONException e) {
							}

						}
						funcs.setTotalPacks(pack.optString("id_pack"));
						funcs.setTotalMedLong(Integer.valueOf(dur));
						funcs.setTotalDaysContinous();
						T=new Timer();
						T.scheduleAtFixedRate(new TimerTask() {
							@Override
							public void run() {
								runOnUiThread(new Runnable()
								{
									@Override
									public void run()
									{
										total1 = total1 + 1;
									}
								});
							}
						}, 1000, 1000);
					}					
				}
				
			}
		});
		introduccion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!play_block){
					
					prefs.edit().putLong("saveState_time", 0).commit();  
					
					play.setBackgroundResource(R.drawable.play_button);
					play_block = true;
	
					mp.stop();
					/*if(mp_sound != null){
						mp_sound.stop();
					}*/

					favoritos_img.setVisibility(View.VISIBLE);
					String song = null;
					String song_sound = null;

					song =meditacion.optString("med_fichero") + "M"+dur+".mp3";			
					song_sound =meditacion.optString("med_fichero") + "M"+dur+"Sound.mp3";

					int d = Integer.valueOf(dur.trim());
					if (d < 5)
						duracion.setText("DURACIÓN CORTA");

					if (d == 5)
						duracion.setText("DURACIÓN MEDIA");

					if (d > 9)
						duracion.setText("DURACIÓN LARGA");
					
					/*if (dur.compareTo("Corta 2 MIN") == 0){
						duracion.setText("DURACIÓN CORTA");
						song =meditacion.optString("med_fichero") + "M2.mp3";
					}		
					else if (dur.compareTo("Media 5 MIN") == 0){
						duracion.setText("DURACIÓN MEDIA");
						song =meditacion.optString("med_fichero") + "M5.mp3";
					}			
					else if (dur.compareTo("Larga 10 MIN") == 0){
						duracion.setText("DURACIÓN LARGA");
						song =meditacion.optString("med_fichero") + "M10.mp3";
					}	
					else{
						duracion.setText("DURACIÓN CORTA");
						song =meditacion.optString("med_fichero") + "M2.mp3";
					}*/
					
					ContextWrapper cw = new ContextWrapper(Reproductor.this);
			        File directory = cw.getDir("meditaciones", Context.MODE_PRIVATE);
			        File file=new File(directory,song);
					File file_sound=new File(directory,song_sound);
					if(file.exists())   {

						download.setImageResource(R.drawable.downloaded);

						currentTime = 0;
						if(file_sound.exists()){
							prepareSong(file.getAbsolutePath(),file_sound.getAbsolutePath());
							mp.start();
							/*if(mp_sound != null){
								mp_sound.start();
							}*/
						}else{
							prepareSong(file.getAbsolutePath(), null);
							mp.start();
						}


						play.setBackgroundResource(R.drawable.pause_button);

						JSONArray meditaciones_aux = new JSONArray();
						if (prefs.contains("meditaciones")){
							try {
								meditaciones_aux = new JSONArray (prefs.getString("meditaciones",""));
								JSONArray ja = new FilterData().setCompleted(meditaciones_aux, meditacion);
								prefs.edit().putString("meditaciones", ja.toString()).commit();
							} catch (JSONException e) {
							}

						}
					}
					else{
						download.setImageResource(R.drawable.download);
						if (Basics.checkConn(Reproductor.this)){

							time.setText("0:00");
					        time_left.setText("0:00");

							Downloader downloader = new Downloader(Reproductor.this,prefs,loading,0);
							downloader.downloadMp3(song,time_left,mp,pack.toString(),play,true, meditacion.optString("id_meditacion"));
							play.setBackgroundResource(R.drawable.play_button);
						}
						else{
							alert("No hay conexión a Internet.");
						}
						
					}			
					
					introduccion.setVisibility(View.INVISIBLE);
					helper.setVisibility(View.VISIBLE);
                    helper.setVisibility(View.GONE);
					isIntro = false;
					download.setVisibility(View.VISIBLE);
					music.setVisibility(View.VISIBLE);
					
		    		/*JSONArray meditaciones_aux = new JSONArray();
		    		if (prefs.contains("meditaciones")){
		    			try {
		    				meditaciones_aux = new JSONArray (prefs.getString("meditaciones",""));
		    				JSONArray ja = new FilterData().setCompleted(meditaciones_aux, meditacion);
		    		    	prefs.edit().putString("meditaciones", ja.toString()).commit();
		    			} catch (JSONException e) {
		    			}
		    	  	
		           }*/
	        		if (prefs.contains("meditaciones")){
	        			try {
	        				JSONArray meditaciones_aux = new JSONArray();
	        				meditaciones_aux = new JSONArray (prefs.getString("meditaciones",""));
	        				if ((pack.optInt("continuo") == 1) && (!new FilterData().isCompleted(meditaciones_aux, meditacion))){
								play_right.setVisibility(View.INVISIBLE);
							}
	        				else{
	        					if(fromHome){
									play_right.setVisibility(View.INVISIBLE);
								}else{
									play_right.setVisibility(View.VISIBLE);
								}
							}
	        				if (new FilterData().isLast(new FilterData().filterMeditaciones(meditaciones_aux, pack.getString("id_pack")), meditacion.optString("id_meditacion"))){
								play_right.setVisibility(View.INVISIBLE);
							}
	        				
	        			} catch (JSONException e) {
	        				Log.i("medita", "Error isLast");
	        			}
	        	  	
	               }
			    		
				}
			}
		});

		atras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				T.cancel();
				funcs.setTotalSecondsMed((int)total1);
				funcs.setTotalDaySeconds((int)total1);
				funcs.setTotalSectionsSeconds((int)total1, meditacion.optString("id_meditacion"));
				Log.i(Config.tag,String.valueOf(total1));
				total1 = 0;

				if (mp.isPlaying()){
					mp.stop();
					/*if(mp_sound != null){
						mp_sound.stop();
					}*/
				}

				if(fromHome){
					/*Intent i = new Intent(Reproductor.this, Home.class);
					i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					i.setAction(Config.from_Reproductor);
					startActivity(i);
					bitmap = null;*/
					finish();
				}else{
					Intent i = new Intent(Reproductor.this, Meditaciones.class);
					i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					i.putExtra("pack", pack.toString());
					i.setAction(Config.from_Reproductor);
					startActivity(i);
					bitmap = null;
					finish();
				}

			}
		});
		helper.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.i("medita","alertHelper");
				alertHelper();
			}
		});

		
		/*if (fromMain)
			play.performClick();*/
		
		 	ContextWrapper cw = new ContextWrapper(Reproductor.this);
	        File directory = cw.getDir("meditaciones", Context.MODE_PRIVATE);
	        File file=new File(directory,song);


			if(file.exists())   {
				//Downloaded ico
				download.setImageResource(R.drawable.downloaded);

				if(song_sound != null){
					File file_sound = new File(directory,song_sound);
					if(file_sound.exists()){
						prepareSong(file.getAbsolutePath(),file_sound.getAbsolutePath());
					}else{
						prepareSong(file.getAbsolutePath(), null);
					}
				}

				play.performClick();

			}
			else{
				download.setImageResource(R.drawable.download);
				if (Basics.checkConn(this)){
					 time.setText("0:00");
				     time_left.setText("0:00");

					Downloader downloader = new Downloader(Reproductor.this,prefs,loading,0);
					downloader.downloadMp3(song,time_left,mp,pack.toString(), play,true, meditacion.optString("id_meditacion"));

				}
				else{
					alert("No hay conexión a Internet.");
				}

			}
		
			if (!isIntro){
				JSONArray meditaciones_aux = new JSONArray();
        		if (prefs.contains("meditaciones")){
        			try {
        				meditaciones_aux = new JSONArray (prefs.getString("meditaciones",""));
        				if ((pack.optInt("continuo") == 1) && (!new FilterData().isCompleted(meditaciones_aux, meditacion))){
							play_right.setVisibility(View.INVISIBLE);
						}
        				else{
        					if(fromHome){
								play_right.setVisibility(View.INVISIBLE);
							}else{
								play_right.setVisibility(View.VISIBLE);
							}
						}
        				if (new FilterData().isLast(new FilterData().filterMeditaciones(meditaciones_aux, pack.getString("id_pack")), meditacion.optString("id_meditacion"))){
							play_right.setVisibility(View.INVISIBLE);
						}
        			} catch (JSONException e) {
        			}
               }
						
			}				
		
		Drawable draw = getResources().getDrawable(R.drawable.atras_ico);
		draw.setColorFilter(Color.parseColor(pack.optString("pack_color_secundario")), PorterDuff.Mode.MULTIPLY );
		((ImageView)findViewById(R.id.id_reproductor_atras_ico)).setImageDrawable(draw);
		
		duracion.setTextColor(Color.parseColor(pack.optString("pack_color_secundario")));
		//introduccion.setBackgroundColor(Color.parseColor(pack.optString("pack_color_secundario")));
		titulo_pack.setTextColor(Color.parseColor(pack.optString("pack_color_secundario")));

		download.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String song = null;

				song =meditacion.optString("med_fichero") + "M"+dur+".mp3";
				ContextWrapper cw = new ContextWrapper(Reproductor.this);
				File directory = cw.getDir("meditaciones", Context.MODE_PRIVATE);
				File file=new File(directory,song);
				if(file.exists())   {
					//file.delete();
				}else{
					Log.i("medita","Download button pressed");
					Toast.makeText(getApplicationContext(),"Descargando meditación.",Toast.LENGTH_LONG).show();
					//Descargar intro si la hay. Si es intro que no aparezca descargar. y si está descargada cambiar icono.
					Downloader downloader = new Downloader(Reproductor.this,prefs,loading,0);
					downloader.downloadMp3(song,time_left,mp,pack.toString(), play,false, meditacion.optString("id_meditacion"));
					if(intros == true){
						String song_intro = meditacion.optString("med_fichero") + "Intro.mp3";
						downloader.downloadMp3(song_intro,time_left,mp,pack.toString(), play,false, meditacion.optString("id_meditacion"));
					}
					if(bg_sound == 1){
						String song_sound = meditacion.optString("med_fichero") + "M"+dur+"Sound.mp3";
						downloader.downloadMp3(song_sound,time_left,mp,pack.toString(), play,false, meditacion.optString("id_meditacion"));
					}
                    download.setImageResource(R.drawable.downloaded);
				}

			}
		});
		music.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (muted){
					music.setImageResource(R.drawable.music);
					muted = false;
				}else{
					music.setImageResource(R.drawable.muted_music);
					muted = true;
				}

			}
		});
	}
		
	public void prepareSong(String path, String path_sound){
		try {
			fileInputStream = new FileInputStream(path);
			mp.reset();
	        mp.setDataSource(fileInputStream.getFD());
	        mp.prepare();
	        mp.seekTo((int)currentTime);

	        /*if(path_sound != null){
				if(mp_sound != null){

					fileInputStreamSound = new FileInputStream(path_sound);

					mp_sound.reset();
					mp_sound.setDataSource(fileInputStreamSound.getFD());
					mp_sound.prepare();
					mp_sound.seekTo((int)currentTime);
				}
			}*/


	        time.setText(""+utils.milliSecondsToTimer(currentTime));
	        time_left.setText(""+utils.milliSecondsToTimer(mp.getDuration()));
	        play_block = false;
	        
		} catch (IOException e) {	
			Log.i("medita","Error playing audio");
		}           
        
	}
	
	/*public void  prepareSong(String song){
        try {      
        	
        	//AssetFileDescriptor descriptor = getAssets().openFd("music/test.mp3");       	
        	
            mp.reset();
         	mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        	mp.setDataSource(Config.url_meditaciones + "lorem_corta.mp3");
            //mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mp.prepare();
            //mp.start();
            // Displaying Song title
            //String songTitle = songsList.get(songIndex).get("songTitle");
            //songTitleLabel.setText(songTitle);
 
            // Changing Button Image to pause image
           // btnPlay.setImageResource(R.drawable.btn_pause);
 
            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);
 
            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
        	Log.i("medita","Error streaming audio");
        } catch (IllegalStateException e) {
        	Log.i("medita","Error streaming audio");
        } catch (SecurityException e) {
        	Log.i("medita","Error streaming audio");
		} catch (IOException e) {
        	Log.i("medita","Error streaming audio");
		}
    }*/
	public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }   
 
    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
           public void run() {

               long totalDuration = mp.getDuration();
               long currentDuration = mp.getCurrentPosition();
               currentTime = currentDuration;
               // Displaying Total Duration time
              // songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
               // Displaying time completed playing
               time.setText(""+utils.milliSecondsToTimer(currentDuration));
               time_left.setText(""+utils.milliSecondsToTimer(totalDuration-currentDuration));
               // Updating progress bar
               int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
               //Log.d("Progress", ""+progress);
               songProgressBar.setProgress(progress);
			   //funcs.setTotalSecondsMed(progress);

               if (currentDuration>15.0){
				   introActivated = true;
			   }else{
				   introActivated = false;
			   }
 
               // Running this thread after 100 milliseconds
               mHandler.postDelayed(this, 100);
           }
        };
 
    /**
     *
     * */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
 
    }
 
    /**
     * When user starts moving the progress handler
     * */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }
 
    /**
     * When user stops moving the progress hanlder
     * */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
 
        // forward or backward to certain seconds
        mp.seekTo(currentPosition);
		/*if(mp_sound != null){
			mp_sound.seekTo(currentPosition);
		}*/

        // update timer progress again
        updateProgressBar();
    }
    
    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.i(Config.tag,"onCompletion");
    	//if(arg0.getDuration() == arg0.getCurrentPosition()){
		Log.i(Config.tag+"duration",String.valueOf(arg0.getDuration()));
		if (T != null){
			T.cancel();
			funcs.setTotalSecondsMed((int)total1);
			funcs.setTotalDaySeconds((int)total1);
			funcs.setTotalSectionsSeconds((int)total1, meditacion.optString("id_meditacion"));
			Log.i(Config.tag,String.valueOf(total1));
			total1 = 0;
		}

    	play.setBackgroundResource(R.drawable.play_button);
    		if (!isIntro){

				//Miramos que sea el pack 1
				if (pack.optString("id_pack").compareTo("1") == 0){
					if (meditacion.optString("id_meditacion").compareTo("7") == 0){
						prefs.edit().putBoolean("Premios_4", true).commit();
					}else if (meditacion.optString("id_meditacion").compareTo("14") == 0){
						prefs.edit().putBoolean("Premios_5", true).commit();
					}else if (meditacion.optString("id_meditacion").compareTo("21") == 0){
						prefs.edit().putBoolean("Premios_6", true).commit();
					}
				}

    			// comprobamos si es el último elemento de la meditacion seleccionada
    			if (!new FilterData().isLast(meditaciones, meditacion.optString("id_meditacion"))){
    				// si no es el ultimo, mostramos el play
					Log.i(Config.tag,"isLast -> FALSE");
					if (fromHome){
						play_right.setVisibility(View.INVISIBLE);
					}else{
						play_right.setVisibility(View.VISIBLE);
					}
                }else{

					// en caso que sea el último elemento, añadimos puntos de premio
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

        		JSONArray meditaciones_aux = new JSONArray();
        		if (prefs.contains("meditaciones")){
        			try {
        				meditaciones_aux = new JSONArray (prefs.getString("meditaciones",""));
        				JSONArray ja = new FilterData().setCompleted(meditaciones_aux, meditacion);
        		    	prefs.edit().putString("meditaciones", ja.toString()).commit();
        			} catch (JSONException e) {
        			}
        	  	
               }
        		
        	}else{
    			if(introActivated){
					introduccion.performClick();
				}


		}
    }
    
    @Override
    public void onBufferingUpdate (MediaPlayer mp, int percent) {
    	if (Config.log)
    		Log.i("medita",String.valueOf(percent));
    	
    	if (percent == 100){
    		
    	}
    }  
    
    protected void saveState(){
    	if (Config.log)
    		Log.i("medita",dur);
    	prefs.edit().putString("saveState_pack", pack.toString()).commit();
    	prefs.edit().putString("saveState_meditacion", meditacion.toString()).commit();
    	prefs.edit().putString("saveState_duracion", dur).commit();   	
    	
    }
       
    @Override
	public void onBackPressed()
	{
    	back = true;
    	AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		T.cancel();
		funcs.setTotalSecondsMed((int)total1);
		funcs.setTotalDaySeconds((int)total1);
		funcs.setTotalSectionsSeconds((int)total1, meditacion.optString("id_meditacion"));
		Log.i(Config.tag,String.valueOf(total1));
		total1 = 0;

    	//Lo desactivo porqué en versiones nuevas da error
    	//audio.setStreamMute(AudioManager.STREAM_RING,  false);
    	if (mp.isPlaying()){
			mp.stop();
			/*if(mp_sound != null){
				mp_sound.stop();
			}*/

		}

    	if (fromHome){
			/*Intent i = new Intent(Reproductor.this, Home.class);
			i.setAction(Config.from_Reproductor);
			i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(i);*/
			finish();
		}else{
			Intent i = new Intent(Reproductor.this, Meditaciones.class);
			i.setAction(Config.from_Reproductor);
			i.putExtra("pack", pack.toString());
			i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(i);
			finish();
		}

	}
    @Override
   	public void onStop()
   	{
    	super.onStop();
    	prefs.edit().putLong("saveState_time", currentTime).commit();
    	if (T != null){
			T.cancel();
			funcs.setTotalSecondsMed((int)total1);
			funcs.setTotalDaySeconds((int)total1);
			funcs.setTotalSectionsSeconds((int)total1, meditacion.optString("id_meditacion"));
			Log.i(Config.tag,String.valueOf(total1));
			total1 = 0;
		}

    	
    	/*if (mp.isPlaying()){
    		
    		if (!back){
    			Intent intent = new Intent(this, ServicePlay.class);
            	startService(intent);
            	ServicePlay.setMediaplayer(mp,this);
            	ServicePlay.setActivity(this);
            	
            	Bitmap bitmap;
        		try {
        			bitmap = Basics.readFileInternal(this,"iconos",pack.getString("pack_icono"));    			
        			new Notificaciones().setNotificaciones(this, meditacion.optString("med_titulo"),bitmap,pack.getString("pack_color"));

        		} catch (JSONException e) {
        		}
        		
        		prefs.edit().putBoolean("reproduccion", true).commit();   
    		}
    		else{
        		prefs.edit().putBoolean("reproduccion", false).commit(); 
    		}
    	
    		
    	}
    	else{
    		prefs.edit().putBoolean("reproduccion", false).commit(); 

    	}*/
    	
   	}
    
    @Override
   	public void onDestroy()
   	{
    	super.onDestroy();
    	/*NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    	mNotificationManager.cancel(548853);
    	prefs.edit().putBoolean("reproduccion", false).commit(); */
   	}    
        
    protected void setFavoritos(){
    	JSONArray favoritos;
    	//Buscar en prefs si esta en favoritos.
    	if (prefs.contains("favoritos")){
			try {
				favoritos = new JSONArray(prefs.getString("favoritos",""));
				if (favoritos.length() > 0){
					if (new FilterData().isFavoritoDur(favoritos,meditacion,dur)) {
						favorito = true;
						favoritos_img.setBackgroundResource(R.drawable.favorite);
					} else {
						favorito = false;
					}
				}
				else{
					favorito = false;
				}
			} catch (JSONException e) {
				favorito = false;
			}
		} else {
			favorito = false;
		}
	}
    protected void alert(String mens){
 		
		final Dialog dialog = new Dialog(Reproductor.this);
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
				Toast.makeText(Reproductor.this, "Ha habido un error de conexión, intente conectarse más tarde.",Toast.LENGTH_LONG).show();

				if(fromHome){
					Intent i = new Intent(Reproductor.this, Home.class);
					i.setAction(Config.from_Reproductor);
					i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					startActivity(i);
					finish();
				}else{
					Intent i = new Intent(Reproductor.this, Meditaciones.class);
					i.setAction(Config.from_Reproductor);
					i.putExtra("pack", pack.toString());
					i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					startActivity(i);
					finish();
				}
			}
		});

		dialog.show();
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

	protected void alertHelper(){


		final Dialog dialog = new Dialog(Reproductor.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_helper);
		dialog.setCancelable(true);

		// set the custom dialog components - text, image and button
		TextView close = (TextView) dialog.findViewById(R.id.id_alert_btn);
		TextView text1 = (TextView) dialog.findViewById(R.id.id_alert_help_uno);
		TextView text2 = (TextView) dialog.findViewById(R.id.id_alert_help_dos);
		TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);

		text1.setTypeface(font);
		text2.setTypeface(font);
		close.setTypeface(font);
		titulo.setTypeface(font);

		text1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Log.i("medita","Eliminando meditación.");

				String song = null;

				song =meditacion.optString("med_fichero") + "M"+dur+".mp3";
				ContextWrapper cw = new ContextWrapper(Reproductor.this);
				File directory = cw.getDir("meditaciones", Context.MODE_PRIVATE);
				File file=new File(directory,song);
				if(file.exists())   {
					file.delete();
				}

				JSONArray d;
				try {
					d = new JSONArray(meditacion.optString("duraciones"));
					if (meditacion.optInt("med_duracion") == 1){
						dur = d.getString(0);
					}
				} catch (JSONException e) {
				}

				mp.stop();
				/*if(mp_sound != null){
					mp_sound.stop();
				}*/

				time.setText("0:00");
				time_left.setText("0:00");
				bitmap = null;
				Intent i = new Intent(Reproductor.this, Reproductor.class);
				i.putExtra("pack",pack.toString());
				i.putExtra("meditacion", meditacion.toString());
				i.putExtra("duracion",dur);
				i.putExtra("dur", 0);
				startActivity(i);
				finish();




			}
		});
		text2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		// if button is clicked, close the custom dialog
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

}

