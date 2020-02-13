package org.simo.medita;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Regalos extends Activity{
	protected SharedPreferences prefs;
	protected Typeface font;
	protected LinearLayout atras;
	protected TextView titulo;
	protected TextView regalo1;
	protected TextView regalo2;
	protected TextView regalo3;
	protected TextView regalo4;
	protected TextView regalo5;
	protected TextView regalo6;
	protected TextView regalo7;
	protected TextView regalo8;
	protected TextView regalo9;
	protected TextView regalo10;
	protected View f1;
	protected View f2;
	protected View f3;
	protected View f4;
	protected View f5;
	protected View f6;
	protected View f7;
	protected View f8;
	protected View f9;
	protected View f10;
	protected ImageView img;
	protected boolean origen =false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regalos);
		font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");	
//		prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
		atras =  (LinearLayout)findViewById(R.id.id_regalos_atras);
		titulo =  (TextView)findViewById(R.id.id_regalos_inicio);
		regalo1 =  (TextView)findViewById(R.id.id_regalos_regalo1);
		regalo2 =  (TextView)findViewById(R.id.id_regalos_regalo2);
		regalo3 =  (TextView)findViewById(R.id.id_regalos_regalo3);
		regalo4 =  (TextView)findViewById(R.id.id_regalos_regalo4);
		regalo5 =  (TextView)findViewById(R.id.id_regalos_regalo5);
		regalo6 =  (TextView)findViewById(R.id.id_regalos_regalo6);
		regalo7 =  (TextView)findViewById(R.id.id_regalos_regalo7);
		regalo8 =  (TextView)findViewById(R.id.id_regalos_regalo8);
		regalo9 =  (TextView)findViewById(R.id.id_regalos_regalo9);
		regalo10 =  (TextView)findViewById(R.id.id_regalos_regalo10);
		f1 =  (View)findViewById(R.id.id_regalos_view1);
		f2=  (View)findViewById(R.id.id_regalos_view2);
		f3=  (View)findViewById(R.id.id_regalos_view3);
		f4=  (View)findViewById(R.id.id_regalos_view3);
		f5=  (View)findViewById(R.id.id_regalos_view4);
		f6=  (View)findViewById(R.id.id_regalos_view5);
		f7=  (View)findViewById(R.id.id_regalos_view6);
		f8=  (View)findViewById(R.id.id_regalos_view7);
		f9=  (View)findViewById(R.id.id_regalos_view8);
		f10=  (View)findViewById(R.id.id_regalos_view9);
		img = (ImageView) findViewById(R.id.id_regalos_img);
		regalo1.setTypeface(font);
		regalo2.setTypeface(font);
		regalo3.setTypeface(font);
		regalo4.setTypeface(font);
		regalo5.setTypeface(font);
		regalo6.setTypeface(font);
		regalo7.setTypeface(font);
		regalo8.setTypeface(font);
		regalo9.setTypeface(font);
		regalo10.setTypeface(font);
		
		regalo1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {		
				img.setImageResource(R.drawable.regalo1);
				img.setVisibility(View.VISIBLE);
			}
		});
		regalo2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {		
				img.setImageResource(R.drawable.regalo2);
				img.setVisibility(View.VISIBLE);
			}
		});
		regalo3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {		
				img.setImageResource(R.drawable.regalo3);
				img.setVisibility(View.VISIBLE);
			}
		});
		regalo4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {		
				img.setImageResource(R.drawable.regalo4);
				img.setVisibility(View.VISIBLE);
			}
		});
		regalo5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {		
				img.setImageResource(R.drawable.regalo5);
				img.setVisibility(View.VISIBLE);
			}
		});
		regalo6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {		
				img.setImageResource(R.drawable.regalo6);
				img.setVisibility(View.VISIBLE);
			}
		});
		regalo7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {		
				img.setImageResource(R.drawable.regalo7);
				img.setVisibility(View.VISIBLE);
			}
		});
		regalo8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {		
				img.setImageResource(R.drawable.regalo8);
				img.setVisibility(View.VISIBLE);
			}
		});
		regalo9.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {		
				img.setImageResource(R.drawable.regalo9);
				img.setVisibility(View.VISIBLE);
			}
		});
		regalo10.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {		
				Intent i = new Intent(Regalos.this, ReproductorFinal.class);   
				i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
	    		startActivity(i);
	    		finish();

			}
		});
		
		Intent i = getIntent();
		if (i != null){
			if (i.hasExtra("nivel")){
				int nivel = i.getIntExtra("nivel", 0);
				
				if (nivel == 0){
					regalo1.setVisibility(View.GONE);
					regalo2.setVisibility(View.GONE);
					regalo3.setVisibility(View.GONE);
					regalo4.setVisibility(View.GONE);
					regalo5.setVisibility(View.GONE);
					regalo6.setVisibility(View.GONE);
					regalo7.setVisibility(View.GONE);
					regalo8.setVisibility(View.GONE);
					regalo9.setVisibility(View.GONE);
					regalo10.setVisibility(View.GONE);
					f1.setVisibility(View.GONE);
					f2.setVisibility(View.GONE);
					f3.setVisibility(View.GONE);
					f4.setVisibility(View.GONE);
					f5.setVisibility(View.GONE);
					f6.setVisibility(View.GONE);
					f7.setVisibility(View.GONE);
					f8.setVisibility(View.GONE);
					f9.setVisibility(View.GONE);
					f10.setVisibility(View.GONE);
				}
				else if (nivel == 1){
					regalo1.setVisibility(View.VISIBLE);
					regalo2.setVisibility(View.GONE);
					regalo3.setVisibility(View.GONE);
					regalo4.setVisibility(View.GONE);
					regalo5.setVisibility(View.GONE);
					regalo6.setVisibility(View.GONE);
					regalo7.setVisibility(View.GONE);
					regalo8.setVisibility(View.GONE);
					regalo9.setVisibility(View.GONE);
					regalo10.setVisibility(View.GONE);
					
					f1.setVisibility(View.VISIBLE);
					f2.setVisibility(View.GONE);
					f3.setVisibility(View.GONE);
					f4.setVisibility(View.GONE);
					f5.setVisibility(View.GONE);
					f6.setVisibility(View.GONE);
					f7.setVisibility(View.GONE);
					f8.setVisibility(View.GONE);
					f9.setVisibility(View.GONE);
					f10.setVisibility(View.GONE);
				}
				else if (nivel == 2){
					regalo1.setVisibility(View.VISIBLE);
					regalo2.setVisibility(View.VISIBLE);
					regalo3.setVisibility(View.GONE);
					regalo4.setVisibility(View.GONE);
					regalo5.setVisibility(View.GONE);
					regalo6.setVisibility(View.GONE);
					regalo7.setVisibility(View.GONE);
					regalo8.setVisibility(View.GONE);
					regalo9.setVisibility(View.GONE);
					regalo10.setVisibility(View.GONE);	
					f1.setVisibility(View.VISIBLE);
					f2.setVisibility(View.VISIBLE);
					f3.setVisibility(View.GONE);
					f4.setVisibility(View.GONE);
					f5.setVisibility(View.GONE);
					f6.setVisibility(View.GONE);
					f7.setVisibility(View.GONE);
					f8.setVisibility(View.GONE);
					f9.setVisibility(View.GONE);
					f10.setVisibility(View.GONE);
								}
				else if (nivel == 3){
					regalo1.setVisibility(View.VISIBLE);
					regalo2.setVisibility(View.VISIBLE);
					regalo3.setVisibility(View.VISIBLE);
					regalo4.setVisibility(View.GONE);
					regalo5.setVisibility(View.GONE);
					regalo6.setVisibility(View.GONE);
					regalo7.setVisibility(View.GONE);
					regalo8.setVisibility(View.GONE);
					regalo9.setVisibility(View.GONE);
					regalo10.setVisibility(View.GONE);
					f1.setVisibility(View.VISIBLE);
					f2.setVisibility(View.VISIBLE);
					f3.setVisibility(View.VISIBLE);
					f4.setVisibility(View.GONE);
					f5.setVisibility(View.GONE);
					f6.setVisibility(View.GONE);
					f7.setVisibility(View.GONE);
					f8.setVisibility(View.GONE);
					f9.setVisibility(View.GONE);
					f10.setVisibility(View.GONE);
				}
				else if (nivel == 4){
					regalo1.setVisibility(View.VISIBLE);
					regalo2.setVisibility(View.VISIBLE);
					regalo3.setVisibility(View.VISIBLE);
					regalo4.setVisibility(View.VISIBLE);
					regalo5.setVisibility(View.GONE);
					regalo6.setVisibility(View.GONE);
					regalo7.setVisibility(View.GONE);
					regalo8.setVisibility(View.GONE);
					regalo9.setVisibility(View.GONE);
					regalo10.setVisibility(View.GONE);
					f1.setVisibility(View.VISIBLE);
					f2.setVisibility(View.VISIBLE);
					f3.setVisibility(View.VISIBLE);
					f4.setVisibility(View.VISIBLE);
					f5.setVisibility(View.GONE);
					f6.setVisibility(View.GONE);
					f7.setVisibility(View.GONE);
					f8.setVisibility(View.GONE);
					f9.setVisibility(View.GONE);
					f10.setVisibility(View.GONE);
				}
				else if (nivel == 5){
					regalo1.setVisibility(View.VISIBLE);
					regalo2.setVisibility(View.VISIBLE);
					regalo3.setVisibility(View.VISIBLE);
					regalo4.setVisibility(View.VISIBLE);
					regalo5.setVisibility(View.VISIBLE);
					regalo6.setVisibility(View.GONE);
					regalo7.setVisibility(View.GONE);
					regalo8.setVisibility(View.GONE);
					regalo9.setVisibility(View.GONE);
					regalo10.setVisibility(View.GONE);
					f1.setVisibility(View.VISIBLE);
					f2.setVisibility(View.VISIBLE);
					f3.setVisibility(View.VISIBLE);
					f4.setVisibility(View.VISIBLE);
					f5.setVisibility(View.VISIBLE);
					f6.setVisibility(View.GONE);
					f7.setVisibility(View.GONE);
					f8.setVisibility(View.GONE);
					f9.setVisibility(View.GONE);
					f10.setVisibility(View.GONE);
				}
				else if (nivel == 6){
					regalo1.setVisibility(View.VISIBLE);
					regalo2.setVisibility(View.VISIBLE);
					regalo3.setVisibility(View.VISIBLE);
					regalo4.setVisibility(View.VISIBLE);
					regalo5.setVisibility(View.VISIBLE);
					regalo6.setVisibility(View.VISIBLE);
					regalo7.setVisibility(View.GONE);
					regalo8.setVisibility(View.GONE);
					regalo9.setVisibility(View.GONE);
					regalo10.setVisibility(View.GONE);
					f1.setVisibility(View.VISIBLE);
					f2.setVisibility(View.VISIBLE);
					f3.setVisibility(View.VISIBLE);
					f4.setVisibility(View.VISIBLE);
					f5.setVisibility(View.VISIBLE);
					f6.setVisibility(View.VISIBLE);
					f7.setVisibility(View.GONE);
					f8.setVisibility(View.GONE);
					f9.setVisibility(View.GONE);
					f10.setVisibility(View.GONE);
				}
				else if (nivel == 7){
					regalo1.setVisibility(View.VISIBLE);
					regalo2.setVisibility(View.VISIBLE);
					regalo3.setVisibility(View.VISIBLE);
					regalo4.setVisibility(View.VISIBLE);
					regalo5.setVisibility(View.VISIBLE);
					regalo6.setVisibility(View.VISIBLE);
					regalo7.setVisibility(View.VISIBLE);
					regalo8.setVisibility(View.GONE);
					regalo9.setVisibility(View.GONE);
					regalo10.setVisibility(View.GONE);
					f1.setVisibility(View.VISIBLE);
					f2.setVisibility(View.VISIBLE);
					f3.setVisibility(View.VISIBLE);
					f4.setVisibility(View.VISIBLE);
					f5.setVisibility(View.VISIBLE);
					f6.setVisibility(View.VISIBLE);
					f7.setVisibility(View.VISIBLE);
					f8.setVisibility(View.GONE);
					f9.setVisibility(View.GONE);
					f10.setVisibility(View.GONE);
				}
				else if (nivel == 8){
					regalo1.setVisibility(View.VISIBLE);
					regalo2.setVisibility(View.VISIBLE);
					regalo3.setVisibility(View.VISIBLE);
					regalo4.setVisibility(View.VISIBLE);
					regalo5.setVisibility(View.VISIBLE);
					regalo6.setVisibility(View.VISIBLE);
					regalo7.setVisibility(View.VISIBLE);
					regalo8.setVisibility(View.VISIBLE);
					regalo9.setVisibility(View.GONE);
					regalo10.setVisibility(View.GONE);
					f1.setVisibility(View.VISIBLE);
					f2.setVisibility(View.VISIBLE);
					f3.setVisibility(View.VISIBLE);
					f4.setVisibility(View.VISIBLE);
					f5.setVisibility(View.VISIBLE);
					f6.setVisibility(View.VISIBLE);
					f7.setVisibility(View.VISIBLE);
					f8.setVisibility(View.VISIBLE);
					f9.setVisibility(View.GONE);
					f10.setVisibility(View.GONE);
				}
				else if (nivel == 9){
					regalo1.setVisibility(View.VISIBLE);
					regalo2.setVisibility(View.VISIBLE);
					regalo3.setVisibility(View.VISIBLE);
					regalo4.setVisibility(View.VISIBLE);
					regalo5.setVisibility(View.VISIBLE);
					regalo6.setVisibility(View.VISIBLE);
					regalo7.setVisibility(View.VISIBLE);
					regalo8.setVisibility(View.VISIBLE);
					regalo9.setVisibility(View.VISIBLE);
					regalo10.setVisibility(View.GONE);
					f1.setVisibility(View.VISIBLE);
					f2.setVisibility(View.VISIBLE);
					f3.setVisibility(View.VISIBLE);
					f4.setVisibility(View.VISIBLE);
					f5.setVisibility(View.VISIBLE);
					f6.setVisibility(View.VISIBLE);
					f7.setVisibility(View.VISIBLE);
					f8.setVisibility(View.VISIBLE);
					f9.setVisibility(View.VISIBLE);
					f10.setVisibility(View.GONE);
				}
				else if (nivel == 10){
					regalo1.setVisibility(View.VISIBLE);
					regalo2.setVisibility(View.VISIBLE);
					regalo3.setVisibility(View.VISIBLE);
					regalo4.setVisibility(View.VISIBLE);
					regalo5.setVisibility(View.VISIBLE);
					regalo6.setVisibility(View.VISIBLE);
					regalo7.setVisibility(View.VISIBLE);
					regalo8.setVisibility(View.VISIBLE);
					regalo9.setVisibility(View.VISIBLE);
					regalo10.setVisibility(View.VISIBLE);
					f1.setVisibility(View.VISIBLE);
					f2.setVisibility(View.VISIBLE);
					f3.setVisibility(View.VISIBLE);
					f4.setVisibility(View.VISIBLE);
					f5.setVisibility(View.VISIBLE);
					f6.setVisibility(View.VISIBLE);
					f7.setVisibility(View.VISIBLE);
					f8.setVisibility(View.VISIBLE);
					f9.setVisibility(View.VISIBLE);
					f10.setVisibility(View.VISIBLE);
				}
				
				
			}
			if (i.hasExtra("origen") && i.hasExtra("nivel")){
				origen =true;
				int nivel = i.getIntExtra("nivel",1);
				if (nivel == 1)
					regalo1.performClick();
				else if (nivel == 2)
					regalo2.performClick();
				else if (nivel == 3)
					regalo3.performClick();
				else if (nivel == 4)
					regalo4.performClick();
				else if (nivel == 5)
					regalo5.performClick();
				else if (nivel == 6)
					regalo6.performClick();
				else if (nivel == 7)
					regalo7.performClick();
				else if (nivel == 8)
					regalo8.performClick();
				else if (nivel == 9)
					regalo9.performClick();						
				else if (nivel == 10)
					regalo10.performClick();				
				
			}
		}
		
		
		
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {		
				if (origen){
					Intent i = new Intent(Regalos.this, Progreso.class);   
					i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		    		startActivity(i);
		    		finish();
				}
				
				
				img.setVisibility(View.GONE);
				
			}
		});
		
		
		atras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {		
				
				Intent i = new Intent(Regalos.this, Progreso.class);   
				i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
	    		startActivity(i);
	    		finish();
			}
		});
	}

	@Override
	public void onBackPressed()
	{	
				
		Intent i = new Intent(Regalos.this, MainActivity.class);   
		i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(i);		
		finish();
	}
	
}
