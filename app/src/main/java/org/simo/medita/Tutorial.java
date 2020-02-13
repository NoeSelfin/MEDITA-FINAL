package org.simo.medita;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class Tutorial extends Activity{
	protected SharedPreferences prefs;
	protected Typeface font;
	private ImageView imageSwitcher;
	private ImageView indice;
	private ImageView close;
	int imagenes = 5;
	int i = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tutorial);	
		font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
//		prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
		imageSwitcher = (ImageView)findViewById(R.id.imageSwitcher1);
		indice = (ImageView)findViewById(R.id.indicador);
		close = (ImageView)findViewById(R.id.close_tuto);

		imageSwitcher.setImageResource(R.drawable.medita_tutorial_0);
		indice.setImageResource(R.drawable.swipe1);
		int color = Color.parseColor("#FFFFFF");          
    	indice.setColorFilter(color);

		prefs.edit().putLong("fecha_tutorial",System.currentTimeMillis()).commit();
		prefs.edit().putBoolean("fecha_tutorial_5", false).commit();
		prefs.edit().putBoolean("fecha_tutorial_10", false).commit();

    	
    	close.setOnClickListener(new OnClickListener() {
		    public void onClick(View arg0)
		    {
				alert(getString(R.string.alert_saltar_tutorial));

		    }
    	
    	});
    	
		imageSwitcher.setOnTouchListener(new OnSwipeTouchListener(Tutorial.this) {
		    public void onSwipeTop() {
		    }
		    public void onSwipeRight() {
		    	
		    	
		        if (i > 1){
		        	i--;
		        }
		        
		        if (i==1){
		        	indice.setImageResource(R.drawable.swipe1);
		        	imageSwitcher.setImageResource(R.drawable.medita_tutorial_0);
		        	int color = Color.parseColor("#FFFFFF");          
		        	indice.setColorFilter(color);
		        }
		        else if (i==2){
		        	indice.setImageResource(R.drawable.swipe2);
		        	imageSwitcher.setImageResource(R.drawable.medita_tutorial_1);
		        	int color = Color.parseColor("#293c4f");
		        	indice.setColorFilter(color);
		        }
		        else if (i==3){
		        	indice.setImageResource(R.drawable.swipe3);
		        	imageSwitcher.setImageResource(R.drawable.medita_tutorial_2);
		        	int color = Color.parseColor("#FFFFFF");
		        	indice.setColorFilter(color);
		        }
		        else if (i==4){
		        	indice.setImageResource(R.drawable.swipe4);
		        	imageSwitcher.setImageResource(R.drawable.medita_tutorial_3);
		        	int color = Color.parseColor("#293c4f");          
		        	indice.setColorFilter(color);
		        }
/*		        else if (i==5){
		        	indice.setImageResource(R.drawable.swipe5);
		        	imageSwitcher.setImageResource(R.drawable.medita_tutorial_4);
		        	int color = Color.parseColor("#293c4f");          
		        	indice.setColorFilter(color);
		        }*/
		        
		        
		       // Toast.makeText(Tutorial.this, String.valueOf(i), Toast.LENGTH_SHORT).show();

		    }
		    public void onSwipeLeft() {		        
		        i++;
		        // Toast.makeText(Tutorial.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
		        if (i==1){
		        	indice.setImageResource(R.drawable.swipe1);
		        	imageSwitcher.setImageResource(R.drawable.medita_tutorial_0);
		        	int color = Color.parseColor("#FFFFFF");          
		        	indice.setColorFilter(color);
		        }
		        else if (i==2){
		        	indice.setImageResource(R.drawable.swipe2);
		        	imageSwitcher.setImageResource(R.drawable.medita_tutorial_1);
		        	int color = Color.parseColor("#293c4f");
		        	indice.setColorFilter(color);
		        }
		        else if (i==3){
		        	indice.setImageResource(R.drawable.swipe3);
		        	imageSwitcher.setImageResource(R.drawable.medita_tutorial_2);
		        	int color = Color.parseColor("#FFFFFF");
		        	indice.setColorFilter(color);
		        }
		        else if (i==4){
		        	indice.setImageResource(R.drawable.swipe4);
		        	imageSwitcher.setImageResource(R.drawable.medita_tutorial_3);
		        	int color = Color.parseColor("#293c4f");          
		        	indice.setColorFilter(color);
		        }
/*		        else if (i==5){
		        	indice.setImageResource(R.drawable.swipe5);
		        	imageSwitcher.setImageResource(R.drawable.medita_tutorial_4);
		        	int color = Color.parseColor("#293c4f");          
		        	indice.setColorFilter(color);
		        }*/
		        
		        
		        if (i==(imagenes+1)){
		        	Intent i = new Intent(Tutorial.this, MainActivity.class);   
	       			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	       	   		startActivity(i);
	       	   		finish();
		        }
		    }
		    public void onSwipeBottom() {
		    }

		});
		
	}
	
	@Override
	public void onBackPressed(){
		alert("Está seguro que quiere saltarse el tutorial? Desplace hacia la derecha la imagen para continuar con el tutorial.");
	}
	
	 protected void alert(String mensaje){
	 		
		final Dialog dialog = new Dialog(Tutorial.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_generico);
		dialog.setCancelable(true);

		// set the custom dialog components - text, image and button
		TextView ver = (TextView) dialog.findViewById(R.id.id_alert_btn);
		TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
		TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);
		
		titulo.setText("Tutorial");
		text.setText(mensaje);
		ver.setText("Saltar tutorial");
		
		
		text.setTypeface(font);
		ver.setTypeface(font);
		titulo.setTypeface(font);

		// if button is clicked, close the custom dialog
		ver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent i = new Intent(Tutorial.this, MainActivity.class);   
       			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
       	   		startActivity(i);
       	   		finish();
			}
		});

		dialog.show();
	  }
	

}
