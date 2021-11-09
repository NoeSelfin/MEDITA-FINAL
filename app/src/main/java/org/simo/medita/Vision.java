package org.simo.medita;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class Vision extends Activity{
	protected SlidingMenu menu_lateral;
	protected RelativeLayout fundido;
	protected Typeface font;
	protected SharedPreferences prefs;
	protected VideoView videoView;
	protected AnimationSet animation;
	protected ImageView menu;
	protected ImageView img_view;
	
	@Override
	protected void onResume() {
		super.onResume();
		//img_view.setVisibility(View.VISIBLE);
		//fundido.startAnimation(animation);		
		/*videoView.start();
		fundido.setVisibility(View.GONE);*/

	}
	@Override
	protected void onPause() {
		super.onPause();
		img_view.setVisibility(View.VISIBLE);

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_vision);
		font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
//		prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);

		fundido = (RelativeLayout)findViewById(R.id.id_vision_fundido);
		
		((TextView)findViewById(R.id.id_vision_inicio)).setTypeface(font);
		((TextView)findViewById(R.id.id_vision_texto1)).setTypeface(font);
		((TextView)findViewById(R.id.id_vision_texto2)).setTypeface(font);
		
		LinearLayout atras = (LinearLayout)findViewById(R.id.id_vision_atras);
		
		ImageView whatsapp = (ImageView)findViewById(R.id.id_vision_whatsapp);
		ImageView facebook = (ImageView)findViewById(R.id.id_vision_facebook);
		ImageView twitter = (ImageView)findViewById(R.id.id_vision_twitter);
		img_view = (ImageView)findViewById(R.id.id_image_video);
		
		
		videoView = (VideoView) findViewById(R.id.your_video_view);
		videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
            	img_view.setVisibility(View.VISIBLE);
            }
        });
       
		
		Uri path = Uri.parse("android.resource://org.simo.medita/"	+ R.raw.mapa_mundo);
				 
		videoView.setVideoURI(path);
		//videoView.start();
	
		/*videoView.setOnPreparedListener(new OnPreparedListener() {
		    @Override
		    public void onPrepared(MediaPlayer mp) {
		        mp.setLooping(true);
		    }
		});*/
		
			
		
		Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
		fadeOut.setStartOffset(1000);
		fadeOut.setDuration(1000);
		fadeOut.setAnimationListener(new Animation.AnimationListener(){
		    @Override
		    public void onAnimationStart(Animation arg0) {
		    }           
		    @Override
		    public void onAnimationRepeat(Animation arg0) {
		    }           
		    @Override
		    public void onAnimationEnd(Animation arg0) {
		    	fundido.setVisibility(View.GONE);
		    }
		});

		animation = new AnimationSet(false); //change to false
		animation.addAnimation(fadeOut);
		fundido.setAnimation(animation);
		
		Intent i = getIntent();
		if(i != null){
			if (i.getAction().compareToIgnoreCase("fromMenu") == 0){
				img_view.setVisibility(View.INVISIBLE);
				fundido.startAnimation(animation);
				videoView.start();
				
			}
			else
				fundido.setVisibility(View.GONE);			
		}
		else
			fundido.setVisibility(View.GONE);
				
		
		whatsapp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				prefs.edit().putBoolean("Premios_2", true).commit();				
				PackageManager pm=getPackageManager();
			    try {
			        Intent waIntent = new Intent(Intent.ACTION_SEND);
			        waIntent.setType("text/plain");
			        String text = "Estoy aprendiendo a meditar con la app Medita. https://play.google.com/store/apps/details?id=org.simo.medita";

			        PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
			        //Check if package exists or not. If not then code 
			        //in catch block will be called
			        waIntent.setPackage("com.whatsapp");

			        waIntent.putExtra(Intent.EXTRA_TEXT, text);
			        startActivity(Intent.createChooser(waIntent, "Share with"));
					fundido.setVisibility(View.GONE);

			   } catch (NameNotFoundException e) {
			        Toast.makeText(Vision.this, "WhatsApp no está instalada.", Toast.LENGTH_SHORT).show();
			   }  

			}
		});
		facebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				prefs.edit().putBoolean("Premios_2", true).commit();
				PackageManager pm=getPackageManager();
			    try {
			        Intent waIntent = new Intent(Intent.ACTION_SEND);
			        waIntent.setType("text/plain");
			        String text = "https://play.google.com/store/apps/details?id=org.simo.medita";

			        PackageInfo info=pm.getPackageInfo("com.facebook.katana", PackageManager.GET_META_DATA);
			        //Check if package exists or not. If not then code 
			        //in catch block will be called
			        waIntent.setPackage("com.facebook.katana");

			        waIntent.putExtra(Intent.EXTRA_TEXT, text);
			        startActivity(Intent.createChooser(waIntent, "Share with"));
					fundido.setVisibility(View.GONE);

			   } catch (NameNotFoundException e) {
			        Toast.makeText(Vision.this, "Facebook no está instalado.", Toast.LENGTH_SHORT).show();
			   }  
			}
		});
		twitter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				prefs.edit().putBoolean("Premios_2", true).commit();
				PackageManager pm=getPackageManager();
			    try {
			        Intent waIntent = new Intent(Intent.ACTION_SEND);
			        waIntent.setType("text/plain");
			        String text = "Estoy aprendiendo a meditar con la app Medita. https://play.google.com/store/apps/details?id=org.simo.medita";

			        PackageInfo info=pm.getPackageInfo("com.twitter.android", PackageManager.GET_META_DATA);
			        //Check if package exists or not. If not then code 
			        //in catch block will be called
			        waIntent.setPackage("com.twitter.android");

			        waIntent.putExtra(Intent.EXTRA_TEXT, text);
			        startActivity(Intent.createChooser(waIntent, "Share with"));
					fundido.setVisibility(View.GONE);

			   } catch (NameNotFoundException e) {
			        Toast.makeText(Vision.this, "Twitter no está instalado.", Toast.LENGTH_SHORT).show();
			   }  
			}
		});
		atras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				menu_lateral.showMenu(true);
			}
		});
		
		 setMenu();
		
	}
	
	@Override
	public void onBackPressed()
	{	
		Intent i = new Intent(Vision.this, MainActivity.class);   
		i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(i);		
		finish();
	}
	
	public void setMenu(){
		menu_lateral = new SlidingMenu(Vision.this);
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
		((View) menu_lateral.findViewById(R.id.id_menu_view_progreso)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_acercade)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_vision)).setVisibility(View.VISIBLE);
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
				Intent i = new Intent(Vision.this, LogIn.class);
				startActivity(i);
				finish();
			}
		});
		LinearLayout news = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_news_ll);
		news.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Vision.this, Novedades.class);
				startActivity(i);
				finish();
			}
		});
		LinearLayout acercade = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_acercade_ll);
		acercade.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Vision.this, Acercade.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout opciones = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_opciones_ll);
		opciones.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Vision.this, Opciones.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		
		LinearLayout favoritos = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_fav_ll);
		favoritos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Vision.this, Favoritos.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout progreso = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_progreso_ll);
		progreso.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Vision.this, Progreso.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout inicio = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_inicio_ll);
		inicio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Vision.this, MainActivity.class);   
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
						Intent i = new Intent(Vision.this, Sincro2.class);   
			    		 startActivity(i);
			    		 finish();
					}
					else{
						Intent i = new Intent(Vision.this, Sincro.class);   
			    		 startActivity(i);
			    		 finish();
					}
				}
				else{
					Intent i = new Intent(Vision.this, Sincro.class);   
		    		startActivity(i);
		    		finish();
				}
			}
		});
		LinearLayout compras = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_compras_ll);
		compras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new RecargarCompras(Vision.this);
			}
		});

		LinearLayout suscripcion = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_suscription_ll);
		suscripcion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// si esta registrado, va a la suscripcion. En caso contrario al login
				Intent i = new Intent(Vision.this, Suscripcion.class);
				startActivity(i);
				finish();
				/*if (prefs.getBoolean(getString(R.string.registrado),false)){
					Intent i = new Intent(Vision.this, Suscripcion.class);
					startActivity(i);
					finish();
				}else{
					Intent i = new Intent(Vision.this, LogIn.class);
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
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		img_view.setVisibility(View.VISIBLE);		
		
		
	    return super.dispatchTouchEvent(ev);
	   
	}

}
