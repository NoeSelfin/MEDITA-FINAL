package org.simo.medita;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class Opciones extends Activity{
	protected SharedPreferences prefs;
	protected Functions functions;
	protected SlidingMenu menu_lateral;
	protected Typeface font;
	protected ToggleButton molestar;
	protected ToggleButton intros;
	protected ToggleButton alarmas;
	
	protected ImageView alarma1_cerrar;
	protected ImageView alarma2_cerrar;
	protected ImageView new_alarm;
	
	protected TextView hora1;
	protected TextView hora1_text;
	protected TextView hora2;
	protected TextView hora2_text;
	
	protected LinearLayout ll1;
	protected LinearLayout ll2;
	
	protected boolean alarm_active = false;
	protected boolean alarm1_active = false;
	protected boolean alarm2_active = false;
	protected int selected = 0;
	
	protected View linea1;
	//protected View linea2;
	
	protected ImageView menu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_opciones);
//		prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
		functions = new Functions(this);

		font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
		LinearLayout atras = (LinearLayout) findViewById(R.id.id_opciones_atras);		
		((TextView) findViewById(R.id.id_opciones_inicio)).setTypeface(font);
		((TextView) findViewById(R.id.id_opciones_nomolestar)).setTypeface(font);
		((TextView) findViewById(R.id.id_opciones_intros)).setTypeface(font);	
		((TextView) findViewById(R.id.id_opciones_alarmas)).setTypeface(font);
		hora1 = (TextView) findViewById(R.id.id_opciones_alarma1_text1);
		hora1.setTypeface(font);
		hora1_text = (TextView) findViewById(R.id.id_opciones_alarma1_text2);
		hora1_text.setTypeface(font);
		hora2 = (TextView) findViewById(R.id.id_opciones_alarma2_text1);
		hora2.setTypeface(font);
		hora2_text = (TextView) findViewById(R.id.id_opciones_alarma2_text2);
		hora2_text.setTypeface(font);		
		ll1 = (LinearLayout) findViewById(R.id.id_opciones_alarma1_ll);
		ll2 = (LinearLayout) findViewById(R.id.id_opciones_alarma2_ll);
		linea1 = (View) findViewById(R.id.id_opciones_view1);
		//linea2 = (View) findViewById(R.id.id_opciones_view2);
		
		molestar = (ToggleButton) findViewById(R.id.toggleButton1);
		intros = (ToggleButton) findViewById(R.id.toggleButton2);
		alarmas = (ToggleButton) findViewById(R.id.toggleButton3);
		
		new_alarm = (ImageView) findViewById(R.id.id_opciones_alarma_new);
		alarma1_cerrar = (ImageView) findViewById(R.id.id_opciones_alarma1_cerrar);
		alarma2_cerrar = (ImageView) findViewById(R.id.id_opciones_alarma2_cerrar);			
		
		
		
		
		
		if (!prefs.contains("opciones_nomolestar")){
			molestar.setChecked(false);
			molestar.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));	
		}
		else{
			molestar.setChecked(prefs.getBoolean("opciones_nomolestar", false));
			if (prefs.getBoolean("opciones_nomolestar", false))
				molestar.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_off/checkbox_00013.png")));	   
			else
				molestar.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));	  


		}
		if (!prefs.contains("opciones_intros")){
			intros.setChecked(true);
			intros.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_off/checkbox_00013.png")));	        

		}	
		else{
			intros.setChecked(prefs.getBoolean("opciones_intros", true));
			if (prefs.getBoolean("opciones_intros", true))
				intros.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_off/checkbox_00013.png")));

			else
				intros.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));


		}
		
		setAlarms();
	
		
		molestar.setOnClickListener(new OnClickListener() {
		    public void onClick(View arg0)
		    {
		        if(molestar.isChecked()){
		        	prefs.edit().putBoolean("opciones_nomolestar", true).commit();
		        	AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		molestar.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_off/checkbox_00013.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOn();
		            loadingAnimation = af.getAnimation();
		            molestar.setBackground(loadingAnimation);
		            loadingAnimation.start();
		        }
		        else{
		        	prefs.edit().putBoolean("opciones_nomolestar", false).commit();
		        	AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		molestar.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOff();
		            loadingAnimation = af.getAnimation();
		            molestar.setBackground(loadingAnimation);
		            loadingAnimation.start();
		        	
		        	
		        }
		    }
		});
		intros.setOnClickListener(new OnClickListener() {
		    public void onClick(View arg0)
		    {
		        if(intros.isChecked()){
		        	prefs.edit().putBoolean("opciones_intros", true).commit();
		        	AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		intros.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_off/checkbox_00013.png")));	
		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOn();
		            loadingAnimation = af.getAnimation();
		            intros.setBackground(loadingAnimation);
		            loadingAnimation.start();
		        }
		        else{
		        	prefs.edit().putBoolean("opciones_intros", false).commit();
		        	AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		intros.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));	
		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOff();
		            loadingAnimation = af.getAnimation();
		            intros.setBackground(loadingAnimation);
		            loadingAnimation.start();
		        }
		    }
		});
		alarmas.setOnClickListener(new OnClickListener() {
		    public void onClick(View arg0)
		    {
		        if(alarmas.isChecked()){
		        	prefs.edit().putBoolean("opciones_alarmas", true).commit();
		        	AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		alarmas.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_off/checkbox_00000.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOn();
		            loadingAnimation = af.getAnimation();
		            alarmas.setBackground(loadingAnimation);
		            loadingAnimation.start();
		        	
		        	if ((alarm1_active)&&(alarm2_active)){
		        		new_alarm.setVisibility(View.INVISIBLE);
					}else{
			        	new_alarm.setVisibility(View.VISIBLE);
					}
		        	alarm_active = true;
		        	hora1.setTextColor(Color.parseColor("#0c465e"));
		        	hora1_text.setTextColor(Color.parseColor("#0c465e"));
		        	hora2.setTextColor(Color.parseColor("#0c465e"));
		        	hora2_text.setTextColor(Color.parseColor("#0c465e"));	
		        	
		        	//Toast.makeText(Opciones.this,  prefs.getString("alarma1_dias",""), Toast.LENGTH_LONG).show();
		        	//Toast.makeText(Opciones.this,  hora1.getText().toString(), Toast.LENGTH_LONG).show();
		        	
		        	if (alarm1_active){
		        		String dias_s = prefs.getString("alarma1_dias","");
		        		JSONArray dias;
						try {
							dias = new JSONArray(dias_s);
			        		new Alarma().SetAlarm(Opciones.this, hora1.getText().toString(),0,dias);

						} catch (JSONException e) {
						}
		        	}
		        	if (alarm2_active){
		        		String dias_s = prefs.getString("alarma2_dias","");
		        		JSONArray dias;
						try {
							dias = new JSONArray(dias_s);
			        		new Alarma().SetAlarm(Opciones.this, hora2.getText().toString(),1,dias);

						} catch (JSONException e) {
						}
		        	}
		        }
		        else{
		        	prefs.edit().putBoolean("opciones_alarmas", false).commit();
		        	AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		alarmas.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOff();
		            loadingAnimation = af.getAnimation();
		            alarmas.setBackground(loadingAnimation);
		            loadingAnimation.start();
		        	new_alarm.setVisibility(View.INVISIBLE);
		        	alarm_active = false;
		        	hora1.setTextColor(Color.parseColor("#d8d8d8"));
		        	hora1_text.setTextColor(Color.parseColor("#d8d8d8"));
		        	hora2.setTextColor(Color.parseColor("#d8d8d8"));
		        	hora2_text.setTextColor(Color.parseColor("#d8d8d8"));	
		        	
		        	if (alarm1_active)
		        		new Alarma().CancelAlarm(Opciones.this, 0);
		        	if (alarm2_active)
		        		new Alarma().CancelAlarm(Opciones.this, 1);
		        			        	
		        }
		    }
		});
		new_alarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {							
				
				if ((alarm1_active)&&(!alarm2_active)){	
					setTime(1,View.VISIBLE,View.VISIBLE,View.VISIBLE,View.VISIBLE,View.INVISIBLE, true,true);
				}
				else if ((!alarm1_active)&&(alarm2_active)){
				
					setTime(0,View.VISIBLE,View.VISIBLE,View.VISIBLE,View.VISIBLE,View.INVISIBLE,true,true);
				}
				else if ((!alarm1_active)&&(!alarm2_active)){
					
					setTime(0,View.VISIBLE,View.VISIBLE,View.GONE,View.GONE,View.VISIBLE,true,false);
				}

				 
			}
		});
		alarma1_cerrar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (alarm_active){
					new_alarm.setVisibility(View.VISIBLE);
					alarm1_active = false;
					ll1.setVisibility(View.GONE);
					linea1.setVisibility(View.GONE);
					prefs.edit().putBoolean("alarma1_active", alarm1_active);
					prefs.edit().remove("alarma1_texto").commit();
					prefs.edit().remove("alarma1_hora").commit();
					prefs.edit().remove("alarma1_activa").commit();
					prefs.edit().remove("alarma1_dias").commit();
					new Alarma().CancelAlarm(Opciones.this, 0);
					
				}
				
			}
		});
		alarma2_cerrar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (alarm_active){
					new_alarm.setVisibility(View.VISIBLE);
					alarm2_active = false;
					ll2.setVisibility(View.GONE);
					//linea2.setVisibility(View.GONE);
					prefs.edit().putBoolean("alarma2_active", alarm2_active);
					prefs.edit().remove("alarma2_texto").commit();
					prefs.edit().remove("alarma2_hora").commit();
					prefs.edit().remove("alarma2_activa").commit();
					prefs.edit().remove("alarma2_dias").commit();
					
					new Alarma().CancelAlarm(Opciones.this, 1);

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

		if(functions.shouldShowMenu()){
			functions.showMenu();
		}
	}
	
	public void getDays(final int alarm, final String time,final int view1, final int view2, final int view3 ,final int view4, final int view5, final boolean alarm_bool1, final boolean alarm_bool2){
		final Dialog dialog = new Dialog(Opciones.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_selector_dias);		
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCancelable(false);

		final ToggleButton lunes = (ToggleButton) dialog.findViewById(R.id.toggleButtonLunes);
		final ToggleButton martes = (ToggleButton) dialog.findViewById(R.id.toggleButtonMartes);
		final ToggleButton miercoles = (ToggleButton) dialog.findViewById(R.id.toggleButtonMiercoles);
		final ToggleButton jueves = (ToggleButton) dialog.findViewById(R.id.toggleButtonJueves);
		final ToggleButton viernes = (ToggleButton) dialog.findViewById(R.id.toggleButtonViernes);
		final ToggleButton sabado = (ToggleButton) dialog.findViewById(R.id.toggleButtonSabado);
		final ToggleButton domingo = (ToggleButton) dialog.findViewById(R.id.toggleButtonDomingo);
		
		lunes.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));
		martes.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));
		miercoles.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));
		jueves.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));
		viernes.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));
		sabado.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));
		domingo.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));

		
		
		lunes.setOnClickListener(new OnClickListener() {
		    public void onClick(View arg0)
		    {
		    	if(lunes.isChecked()){
		    		AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		lunes.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_off/checkbox_00013.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOn();
		            loadingAnimation = af.getAnimation();
		            lunes.setBackground(loadingAnimation);
		            loadingAnimation.start();
		    	}
		    	else{
		        	AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		lunes.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOff();
		            loadingAnimation = af.getAnimation();
		            lunes.setBackground(loadingAnimation);
		            loadingAnimation.start();
		        	
		    	}
		    }
		});
		martes.setOnClickListener(new OnClickListener() {
		    public void onClick(View arg0)
		    {
		    	if(martes.isChecked()){
		    		AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		martes.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_off/checkbox_00013.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOn();
		            loadingAnimation = af.getAnimation();
		            martes.setBackground(loadingAnimation);
		            loadingAnimation.start();
		    	}
		    	else{
		        	AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		martes.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOff();
		            loadingAnimation = af.getAnimation();
		            martes.setBackground(loadingAnimation);
		            loadingAnimation.start();
		        	
		    	}
		    }
		});
		miercoles.setOnClickListener(new OnClickListener() {
		    public void onClick(View arg0)
		    {
		    	if(miercoles.isChecked()){
		    		AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		miercoles.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_off/checkbox_00013.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOn();
		            loadingAnimation = af.getAnimation();
		            miercoles.setBackground(loadingAnimation);
		            loadingAnimation.start();
		    	}
		    	else{
		        	AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		miercoles.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOff();
		            loadingAnimation = af.getAnimation();
		            miercoles.setBackground(loadingAnimation);
		            loadingAnimation.start();
		        	
		    	}
		    }
		});
		jueves.setOnClickListener(new OnClickListener() {
		    public void onClick(View arg0)
		    {
		    	if(jueves.isChecked()){
		    		AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		jueves.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_off/checkbox_00013.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOn();
		            loadingAnimation = af.getAnimation();
		            jueves.setBackground(loadingAnimation);
		            loadingAnimation.start();
		    	}
		    	else{
		        	AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		jueves.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOff();
		            loadingAnimation = af.getAnimation();
		            jueves.setBackground(loadingAnimation);
		            loadingAnimation.start();
		        	
		    	}
		    }
		});
		viernes.setOnClickListener(new OnClickListener() {
		    public void onClick(View arg0)
		    {
		    	if(viernes.isChecked()){
		    		AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		viernes.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_off/checkbox_00013.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOn();
		            loadingAnimation = af.getAnimation();
		            viernes.setBackground(loadingAnimation);
		            loadingAnimation.start();
		    	}
		    	else{
		        	AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		viernes.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOff();
		            loadingAnimation = af.getAnimation();
		            viernes.setBackground(loadingAnimation);
		            loadingAnimation.start();
		        	
		    	}
		    }
		});
		sabado.setOnClickListener(new OnClickListener() {
		    public void onClick(View arg0)
		    {
		    	if(sabado.isChecked()){
		    		AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		sabado.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_off/checkbox_00013.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOn();
		            loadingAnimation = af.getAnimation();
		            sabado.setBackground(loadingAnimation);
		            loadingAnimation.start();
		    	}
		    	else{
		        	AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		sabado.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOff();
		            loadingAnimation = af.getAnimation();
		            sabado.setBackground(loadingAnimation);
		            loadingAnimation.start();
		        	
		    	}
		    }
		});
		domingo.setOnClickListener(new OnClickListener() {
		    public void onClick(View arg0)
		    {
		    	if(domingo.isChecked()){
		    		AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		domingo.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_off/checkbox_00013.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOn();
		            loadingAnimation = af.getAnimation();
		            domingo.setBackground(loadingAnimation);
		            loadingAnimation.start();
		    	}
		    	else{
		        	AnimationDrawable loadingAnimation = new AnimationDrawable();
		    		loadingAnimation.setOneShot(true);
		    		domingo.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));	        

		            AddFrames af =  new AddFrames(Opciones.this, loadingAnimation);
		            af.addSwitchOff();
		            loadingAnimation = af.getAnimation();
		            domingo.setBackground(loadingAnimation);
		            loadingAnimation.start();
		        	
		    	}
		    }
		});		
		

		TextView cancelar = (TextView) dialog.findViewById(R.id.id_selector_cancel);
		cancelar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		TextView aceptar = (TextView) dialog.findViewById(R.id.id_selector_aceptar);
		aceptar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				
				JSONArray dias = new JSONArray();
				if (lunes.isChecked())
					dias.put("Lunes");
				if (martes.isChecked())
					dias.put("Martes");
				if (miercoles.isChecked())
					dias.put("Miércoles");
				if (jueves.isChecked())
					dias.put("Jueves");
				if (viernes.isChecked())
					dias.put("Viernes");
				if (sabado.isChecked())
					dias.put("Sábado");
				if (domingo.isChecked())
					dias.put("Domingo");	
				
				
				Log.i("medita", dias.toString());
				
				if (dias.length() > 0){					
                	
                	ll1.setVisibility(view1);
					linea1.setVisibility(view2);
					ll2.setVisibility(view3);
					//linea2.setVisibility(view4);
					new_alarm.setVisibility(view5);

					alarm1_active = alarm_bool1;
					alarm2_active = alarm_bool2;

					prefs.edit().putBoolean("alarma1_active", alarm1_active);
					prefs.edit().putBoolean("alarma2_active", alarm2_active);
					
					String texto = "";
					if (lunes.isChecked() && martes.isChecked() && miercoles.isChecked() && jueves.isChecked() && viernes.isChecked() && sabado.isChecked() && domingo.isChecked())
						texto = "Todos los días";
					else if (lunes.isChecked() && martes.isChecked() && miercoles.isChecked() && jueves.isChecked() && viernes.isChecked()&& !sabado.isChecked() && !domingo.isChecked()) 
						texto = "Entre semana";
					else if (!lunes.isChecked() && !martes.isChecked() && !miercoles.isChecked() && !jueves.isChecked() && !viernes.isChecked()&& sabado.isChecked() && domingo.isChecked())
						texto = "Fin de semana";
					else{
						for (int i=0; i<dias.length();i++){
							if (i == (dias.length() - 1))
								texto = texto + dias.optString(i);
							else
								texto = texto + dias.optString(i)+",";						
						}
					}						
					
					 Alarma alarma = new Alarma();
					 Log.i("medita",dias.toString());					 
					
					
					if (alarm == 0){
						 
						hora1.setText(time); 						 
						hora1_text.setText(texto);
						
						prefs.edit().putString("alarma1_texto", texto).commit();
						prefs.edit().putString("alarma1_hora", hora1.getText().toString()).commit();
						prefs.edit().putBoolean("alarma1_activa", true).commit();		
						prefs.edit().putString("alarma1_dias", dias.toString()).commit();

						alarma.SetAlarm(Opciones.this, hora1.getText().toString(),0,dias);

					}
					else if (alarm == 1){
						 hora2.setText(time);
						 hora2_text.setText(texto);
						 
						prefs.edit().putString("alarma2_texto", texto).commit();
						prefs.edit().putString("alarma2_hora", hora2.getText().toString()).commit();
						prefs.edit().putBoolean("alarma2_activa", true).commit();		
						prefs.edit().putString("alarma2_dias", dias.toString()).commit();

						alarma.SetAlarm(Opciones.this, hora2.getText().toString(),1,dias);

					}	
					
					dialog.dismiss();
				}				
			
				else{
					Toast.makeText(Opciones.this, "Ha de seleccionar como mínimo un día.", Toast.LENGTH_LONG).show();
				}				
			
			}
		});

		dialog.show();
	}
	
	protected void setTime(final int alarm, final int view1, final int view2, final int view3 ,final int view4, final int view5,  final boolean alarm_bool1, final boolean alarm_bool2){
		if (alarm_active){
			Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(Opciones.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                	String h ="";
                	String t="";
                	
                	if (selectedHour < 10)
                		h = "0"+String.valueOf(selectedHour);
                	else
                		h = String.valueOf(selectedHour);
                	if (selectedMinute < 10)
                		t = "0"+String.valueOf(selectedMinute);
                	else
                		t = String.valueOf(selectedMinute);
                	
                	String time = h+":"+t;
                	                	
                	getDays(alarm, time, view1, view2, view3, view4, view5,alarm_bool1,alarm_bool2);
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Selecciona la hora");
            mTimePicker.setCancelable(false);
            mTimePicker.show();
		}
		
	}
	
	
	protected void setAlarms(){
		
		
		if ((!prefs.contains("opciones_alarmas"))){
			alarmas.setChecked(prefs.getBoolean("opciones_alarmas", false));
			alarmas.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));	        
			
			alarm_active = false;
			new_alarm.setVisibility(View.INVISIBLE);
			hora1.setTextColor(Color.parseColor("#d8d8d8"));
        	hora1_text.setTextColor(Color.parseColor("#d8d8d8"));
        	hora2.setTextColor(Color.parseColor("#d8d8d8"));
        	hora2_text.setTextColor(Color.parseColor("#d8d8d8"));
		}	
		else{
			if (prefs.getBoolean("opciones_alarmas", false)){		
				alarmas.setChecked(prefs.getBoolean("opciones_alarmas", true));			
				alarmas.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_off/checkbox_00013.png")));	        

				alarm_active = true;
				new_alarm.setVisibility(View.VISIBLE);
				hora1.setTextColor(Color.parseColor("#0c465e"));
	        	hora1_text.setTextColor(Color.parseColor("#0c465e"));
	        	hora2.setTextColor(Color.parseColor("#0c465e"));
	        	hora2_text.setTextColor(Color.parseColor("#0c465e"));
			}
			else{
				alarmas.setBackground( new BitmapDrawable(Opciones.this.getResources(), loadBitmapFromAsset("switch_on/checkbox_00000.png")));	        

				alarm_active = false;
				alarmas.setChecked(prefs.getBoolean("opciones_alarmas", false));		
				new_alarm.setVisibility(View.INVISIBLE);
				hora1.setTextColor(Color.parseColor("#d8d8d8"));
	        	hora1_text.setTextColor(Color.parseColor("#d8d8d8"));
	        	hora2.setTextColor(Color.parseColor("#d8d8d8"));
	        	hora2_text.setTextColor(Color.parseColor("#d8d8d8"));
			}
						
		}
		
		
		if (prefs.contains("alarma1_hora"))
			hora1.setText(prefs.getString("alarma1_hora", ""));
		if (prefs.contains("alarma1_texto"))
			hora1_text.setText(prefs.getString("alarma1_texto", ""));
		
		if (prefs.contains("alarma2_hora"))
			hora2.setText(prefs.getString("alarma2_hora", ""));
		if (prefs.contains("alarma2_texto"))
			hora2_text.setText(prefs.getString("alarma2_texto", ""));		

		
		if (prefs.contains("alarma1_activa")){
			if (prefs.getBoolean("alarma1_activa", false)){
				alarm1_active = true;
				ll1.setVisibility(View.VISIBLE);
				linea1.setVisibility(View.VISIBLE);					
			}
			
		}
		
		if (prefs.contains("alarma2_activa")){
			if (prefs.getBoolean("alarma2_activa", false)){
				alarm2_active = true;
				ll2.setVisibility(View.VISIBLE);
				//linea2.setVisibility(View.VISIBLE);
			}
			
		}			
		
	}	
	
	@Override
	public void onBackPressed()
	{	
		Intent i = new Intent(Opciones.this, MainActivity.class);   
		i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(i);		
		finish();
	}
	
	public void setMenu(){
		menu_lateral = new SlidingMenu(Opciones.this);
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
		((View) menu_lateral.findViewById(R.id.id_menu_view_vision)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_opciones)).setVisibility(View.VISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_sincro)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_sincro)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_suscription)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_contact)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_news)).setVisibility(View.INVISIBLE);


		LinearLayout contacto = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_contact_ll);
		contacto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Opciones.this, LogIn.class);
				startActivity(i);
				finish();
			}
		});
		LinearLayout news = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_news_ll);
		news.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Opciones.this, Novedades.class);
				startActivity(i);
				finish();
			}
		});
		LinearLayout acercade = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_acercade_ll);
		acercade.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Opciones.this, Acercade.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});

		LinearLayout vision = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_vision_ll);
		vision.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Opciones.this, Vision.class);   
				 i.setAction("fromMenu");
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout favoritos = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_fav_ll);
		favoritos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Opciones.this, Favoritos.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout progreso = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_progreso_ll);
		progreso.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Opciones.this, Charts.class);
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout inicio = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_inicio_ll);
		inicio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(functions.shouldShowMenu()){
					Intent i = new Intent(Opciones.this, Home.class);
					startActivity(i);
					finish();
				}else{
					Intent i = new Intent(Opciones.this, MainActivity.class);
					startActivity(i);
					finish();
				}
			}
		});
		LinearLayout sincro = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_sincro_ll);
		sincro.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (prefs.contains("sincronizado")){
					if (prefs.getBoolean("sincronizado", false)){
						Intent i = new Intent(Opciones.this, Sincro2.class);   
			    		 startActivity(i);
			    		 finish();
					}
					else{
						Intent i = new Intent(Opciones.this, Sincro.class);   
			    		 startActivity(i);
			    		 finish();
					}
				}
				else{
					Intent i = new Intent(Opciones.this, Sincro.class);   
		    		startActivity(i);
		    		finish();
				}
			}
		});
		LinearLayout compras = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_compras_ll);
		compras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new RecargarCompras(Opciones.this);
			}
		});

		LinearLayout suscripcion = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_suscription_ll);
		suscripcion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Opciones.this, Suscripcion.class);
				startActivity(i);
				finish();
				// si esta registrado, va a la suscripcion. En caso contrario al login
				/*if (prefs.getBoolean(getString(R.string.registrado),false)){
					Intent i = new Intent(Opciones.this, Suscripcion.class);
					startActivity(i);
					finish();
				}else{
					Intent i = new Intent(Opciones.this, LogIn.class);
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

}
