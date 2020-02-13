package org.simo.medita;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.simo.medita.extras.Utilities;

import java.io.FileInputStream;
import java.io.IOException;

public class ReproductorFinal extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnBufferingUpdateListener{
	protected SharedPreferences prefs;
	
	protected LinearLayout atras;
	protected RelativeLayout bg;
	protected ImageView bg_img;
	protected ImageView play;
	protected ImageView play_left;
	protected ImageView  play_right;	
	protected ImageView  icono;
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
	protected  Bitmap bitmap;
	
	
	protected static MediaPlayer mp;
	private Utilities utils;
	private Handler mHandler = new Handler();	
	
	protected boolean isIntro = true;
	protected  long currentTime = 0;
	protected String med_dia;
	
	protected ImageView favoritos_img;
	protected boolean favorito = false;
	protected boolean intros = true;
	protected static boolean play_block = true;
	protected boolean back = false;
	protected boolean fromMain = false;
	
	FileInputStream fileInputStream;
			
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
		titulo_pack = (TextView)findViewById(R.id.id_reproductor_pack);
		
		introduccion.setTypeface(font);
		time.setTypeface(font);
		time_left.setTypeface(font);
		dia.setTypeface(font);
		duracion.setTypeface(font);
		titulo_pack.setTypeface(font);
		
			
		 // Mediaplayer
        mp = new MediaPlayer();
        utils = new Utilities();        
        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this);
        mp.setOnCompletionListener(this);
        mp.setOnBufferingUpdateListener(this);

		song_name.setText("· Meditación final para integrar el aprendizaje ·");
		song_name.setTypeface(font);
		
		dia.setText("DÍA 1");
		duracion.setText("DURACIÓN LARGA");	
		bg_img.setImageDrawable(getResources().getDrawable(R.drawable.fondo_final));
	  	        	 	      
		icono.setVisibility(View.INVISIBLE);	  	       
		intros = false;
		currentTime = 0;  
		titulo_pack.setText("MEDITACIÓN FINAL");
		play_left.setVisibility(View.INVISIBLE);	  	       
		play_right.setVisibility(View.INVISIBLE);
		introduccion.setVisibility(View.INVISIBLE);	
		favoritos_img.setVisibility(View.INVISIBLE);
		isIntro = false;	
		
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!play_block){
					AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
					int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
					
					if (currentVolume == 0)
						Toast.makeText(ReproductorFinal.this, "El volumen está desactivado.", Toast.LENGTH_SHORT).show();
					else if ((currentVolume == 1) || (currentVolume == 1))
						Toast.makeText(ReproductorFinal.this, "El volumen está demasiado bajo.", Toast.LENGTH_SHORT).show();
	
					if (mp.isPlaying()){
						mp.pause();
						play.setBackgroundResource(R.drawable.play_button);
						audio.setStreamMute(AudioManager.STREAM_RING,  false);	 			

					}
					else{
						mp.start();
						play.setBackgroundResource(R.drawable.pause_button);
						updateProgressBar();						
						if (prefs.contains("opciones_nomolestar")){
							if (prefs.getBoolean("opciones_nomolestar", true)){				        
			 					audio.setStreamMute(AudioManager.STREAM_RING,  true);			 			
							}    
						}
					}					
				}
				
			}
		});
		
		atras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mp.isPlaying())
					mp.stop();
				
				Intent i = new Intent(ReproductorFinal.this, MainActivity.class);   
				i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
	    		startActivity(i);
	    		bitmap = null;
	    		finish();
			}
		});
		
		prepareSong();
		play.performClick();		
					
		Drawable draw = getResources().getDrawable(R.drawable.atras_ico);
		draw.setColorFilter(Color.parseColor("#5ac8d9"), PorterDuff.Mode.MULTIPLY );
		((ImageView)findViewById(R.id.id_reproductor_atras_ico)).setImageDrawable(draw);
		
		duracion.setTextColor(Color.parseColor("#5ac8d9"));
		titulo_pack.setTextColor(Color.parseColor("#5ac8d9"));
				
	}
	
		
	public void prepareSong(){
		try {
			AssetFileDescriptor descriptor = getAssets().openFd("music/meditacion_final.mp3");  
			mp.reset();
			mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
	        mp.prepare();
	        mp.seekTo((int)currentTime); 
	        time.setText(""+utils.milliSecondsToTimer(currentTime));
	        time_left.setText(""+utils.milliSecondsToTimer(mp.getDuration()));
	        play_block = false;
	        
		} catch (IOException e) {	
			Log.i("medita","Error playing audio");
		}           
        
	}
	
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
 
        // update timer progress again
        updateProgressBar();
    }
    
    @Override
    public void onCompletion(MediaPlayer arg0) {
    	play.setBackgroundResource(R.drawable.play_button);   	
    	
    }
    
    @Override
    public void  onBufferingUpdate (MediaPlayer mp, int percent) {
    	if (Config.log)
    		Log.i("medita",String.valueOf(percent));
    	
    	if (percent == 100){
    		
    	}   	
    }  
    
       
    @Override
	public void onBackPressed()
	{
    	if (mp.isPlaying())
			mp.stop();
		
		Intent i = new Intent(ReproductorFinal.this, MainActivity.class);   
		i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(i);
		bitmap = null;
		finish();
	}
    
  
    protected void alert(String mens){
 		
			final Dialog dialog = new Dialog(ReproductorFinal.this);
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
    


}


