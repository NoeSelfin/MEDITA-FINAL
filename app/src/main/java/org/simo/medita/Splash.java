package org.simo.medita;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends Activity{
	protected SharedPreferences prefs;
	private static final long SPLASH_SCREEN_DELAY = 5000;
	protected Typeface font;
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
		
		ImageView background = (ImageView) findViewById(R.id.id_splash_bg);
		TextView text1 = (TextView) findViewById(R.id.id_splash_text1);
		TextView text2 = (TextView) findViewById(R.id.id_splash_text2);
		TextView text3 = (TextView) findViewById(R.id.id_splash_text3);
		TextView text4 = (TextView) findViewById(R.id.id_splash_text4);
		TextView text5 = (TextView) findViewById(R.id.id_splash_text5);
		
		text1.setTypeface(font);
		text2.setTypeface(font);
	    text3.setTypeface(font);
		text4.setTypeface(font);
		text5.setTypeface(font);
		
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		
		Bitmap bg = getBg(month);
		background.setImageBitmap(bg);
		
//	    prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
	    prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
	    /*if (!prefs.contains("version"))
	    prefs.edit().putInt("version", 0).commit();*/
	    
	    new Downloader(this,prefs).searchUpdates();
		
		/*if (prefs.contains("reproduccion")){
			if (prefs.getBoolean("reproduccion", false)){
				Intent mainIntent = new Intent().setClass(Splash.this, Reproductor.class);
				mainIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(mainIntent);
                finish();
			}
			else{
				// Simulate a long loading process on application startup.
		        Timer timer = new Timer();
		        timer.schedule(task, SPLASH_SCREEN_DELAY);
			}
		}
		else{
			// Simulate a long loading process on application startup.
	        Timer timer = new Timer();
	        timer.schedule(task, SPLASH_SCREEN_DELAY);
		}*/
	        
	        
	        
	        //ImageView img = (ImageView)findViewById(R.id.id_splash_img);
	        
	       /* AnimationDrawable pro = new AnimationDrawable();
	        pro.setOneShot(true);
	        img.setBackground( new BitmapDrawable(getResources(), loadBitmapFromAsset("splash/00001.png")));	        
	        AddFrames af =  new AddFrames(this, pro);
	        af.addSplashFrames();
	        pro = af.getAnimation();
            img.setBackground(pro); 
	        pro.start();*/
	        
	        
	        TimerTask task = new TimerTask() {
	            @Override
	            public void run() {
	 
	                // Start the next activity
	                Intent mainIntent = new Intent().setClass(Splash.this, MainActivity.class);
	                startActivity(mainIntent);
	                finish();
	                

	            }
	        };
	        
	        
		     // Simulate a long loading process on application startup.
		        Timer timer = new Timer();
		        timer.schedule(task, SPLASH_SCREEN_DELAY);
	        
			
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

	
	protected Bitmap getBg(int month){
		Bitmap bg = null;
		
		if (month == 0)
			bg = BitmapFactory.decodeResource(getResources(), R.drawable.splash1);
		else if (month == 1)
			bg = BitmapFactory.decodeResource(getResources(), R.drawable.splash2);
		else if (month == 2)
			bg = BitmapFactory.decodeResource(getResources(), R.drawable.splash3);
		else if (month == 3)
			bg = BitmapFactory.decodeResource(getResources(), R.drawable.splash4);
		else if (month == 4)
			bg = BitmapFactory.decodeResource(getResources(), R.drawable.splash5);
		else if (month == 5)
			bg = BitmapFactory.decodeResource(getResources(), R.drawable.splash6);
		else if (month == 6)
			bg = BitmapFactory.decodeResource(getResources(), R.drawable.splash7);
		else if (month == 7)
			bg = BitmapFactory.decodeResource(getResources(), R.drawable.splash8);
		else if (month == 8)
			bg = BitmapFactory.decodeResource(getResources(), R.drawable.splash9);
		else if (month == 9)
			bg = BitmapFactory.decodeResource(getResources(), R.drawable.splash10);
		else if (month == 10)
			bg = BitmapFactory.decodeResource(getResources(), R.drawable.splash11);
		else if (month == 11)
			bg = BitmapFactory.decodeResource(getResources(), R.drawable.splash12);
		else
			bg = BitmapFactory.decodeResource(getResources(), R.drawable.splash1);
		
		
		return bg;
	}
	
	@Override
	protected void onPause(){
	   super.onPause();
      overridePendingTransition(R.anim.fadein, R.anim.fadeout);

	  
	}

}
