package org.simo.medita;

import java.io.IOException;

import org.simo.medita.extras.Utilities;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class Player extends Activity  implements OnCompletionListener, SeekBar.OnSeekBarChangeListener{
	private SeekBar songProgressBar;
	protected TextView time;
	protected TextView song_name;	
	
	private  MediaPlayer mp;
	private Utilities utils;
	private Handler mHandler = new Handler();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_player);
		
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		time = (TextView)findViewById(R.id.id_main_tiempo);
		song_name = (TextView)findViewById(R.id.id_main_titulo);


		
		 // Mediaplayer
        mp = new MediaPlayer();
        utils = new Utilities();
 
        // Listeners
        songProgressBar.setOnSeekBarChangeListener(this); // Important
        mp.setOnCompletionListener(this); // Important
		
      /*  play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				playSong();
			}
		});*/
        
	}
	public void  playSong(){
        // Play song
        try {
        	
        	 AssetFileDescriptor descriptor = getAssets().openFd("music/test.mp3");       	
        	
            mp.reset();
            mp.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mp.prepare();
            mp.start();
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
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
	 
	               // Displaying Total Duration time
	              // songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
	               // Displaying time completed playing
	               time.setText(""+utils.milliSecondsToTimer(currentDuration));
	 
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
	 
	    }
	
}
