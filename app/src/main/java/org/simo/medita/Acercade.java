package org.simo.medita;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.simo.medita.extras.Basics;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Acercade extends Activity{
	protected SharedPreferences prefs;
	protected Typeface font;
	protected SlidingMenu menu_lateral;
	protected ImageView menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_acercade);
		font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
//		prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);

		((TextView)findViewById(R.id.id_acercade_inicio)).setTypeface(font);
		((TextView)findViewById(R.id.id_acercade_texto1)).setTypeface(font);
		((TextView)findViewById(R.id.id_acercade_texto2)).setTypeface(font);
		((TextView)findViewById(R.id.id_acercade_texto3)).setTypeface(font);
		((TextView)findViewById(R.id.id_acercade_texto4)).setTypeface(font);
		TextView texto5 = ((TextView)findViewById(R.id.id_acercade_texto5));
		texto5.setTypeface(font);
		((TextView)findViewById(R.id.id_acercade_nuestrolibro)).setTypeface(font);
		TextView texto6 = ((TextView)findViewById(R.id.id_acercade_texto6));
		texto6.setTypeface(font);
		((TextView)findViewById(R.id.id_acercade_siguenos)).setTypeface(font);
		((TextView)findViewById(R.id.id_acercade_texto7)).setTypeface(font);
		((TextView)findViewById(R.id.id_acercade_newsletter)).setTypeface(font);
		((TextView)findViewById(R.id.id_acercade_texto8)).setTypeface(font);
		TextView web = (TextView)findViewById(R.id.id_acercade_web);
		TextView mail = (TextView)findViewById(R.id.id_acercade_mail);
		mail.setTypeface(font);
		web.setTypeface(font);
		ImageView youtube = (ImageView)findViewById(R.id.id_acercade_youtube);
		ImageView facebook = (ImageView)findViewById(R.id.id_acercade_facebook);
		ImageView instagram = (ImageView)findViewById(R.id.id_acercade_instagram);
		LinearLayout atras = (LinearLayout)findViewById(R.id.id_acercade_atras);
		
		TextView newsletter_btn = (TextView)findViewById(R.id.id_acercade_newsletter_btn);
		newsletter_btn.setTypeface(font);	
		Spanned sp = Html.fromHtml(getResources().getString(R.string.acercade_newsletter_btn));
		newsletter_btn.setText(sp);
		
		sp = Html.fromHtml(getResources().getString(R.string.acercade_texto5));
		texto5.setText(sp);
		texto5.setMovementMethod(LinkMovementMethod.getInstance());

		sp = Html.fromHtml(getResources().getString(R.string.acercade_texto6));
		texto6.setText(sp);
		texto6.setMovementMethod(LinkMovementMethod.getInstance());

		newsletter_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				alertNewsletter();
			}
		});
		
		Intent i = getIntent();
		if (i != null){
			if (Intent.ACTION_GET_CONTENT.equals(i.getAction())){
				
				newsletter_btn.performClick();
			}
		}
		
		mail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{Config.mail_atentamente});
				i.putExtra(Intent.EXTRA_SUBJECT, "Medita");
				try {
				    startActivity(Intent.createChooser(i, "Enviar email..."));
				} catch (android.content.ActivityNotFoundException ex) {
				    Toast.makeText(Acercade.this, "No tiene instalado ningun cliente de email.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		web.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(Config.url_atentamente));
				startActivity(i);
			}
		});
		
		youtube.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.youtube_atentamente)));
			}
		});
		facebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.facebook_atentamente)));
			}
		});
		instagram.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.instagram_atentamente)));
			}
		});

		atras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				menu_lateral.showMenu(true);
			}
		});
		mail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("plain/text");
				i.putExtra(Intent.EXTRA_EMAIL, new String[]{Config.mail_atentamente});
				startActivity(Intent.createChooser(i, "Send mail..."));
			}
		});
		web.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				Intent i = new Intent(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.url_atentamente)));   
	    		startActivity(i);
				
			}
		});
		
		prefs.edit().putBoolean("Premios_1", true).commit();
		 setMenu();
		
	}
	
	@Override
	public void onBackPressed()
	{	
		Intent i = new Intent(Acercade.this, MainActivity.class);   
		i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(i);		
		finish();
	}
	
	public void setMenu(){
		menu_lateral = new SlidingMenu(Acercade.this);
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

		((View) menu_lateral.findViewById(R.id.id_menu_view_ini)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_fav)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_progreso)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_acercade)).setVisibility(View.VISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_vision)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_opciones)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_sincro)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_suscription)).setVisibility(View.INVISIBLE);
		((View) menu_lateral.findViewById(R.id.id_menu_view_contact)).setVisibility(View.INVISIBLE);


		LinearLayout opciones = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_opciones_ll);
		opciones.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Acercade.this, Opciones.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout vision = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_vision_ll);
		vision.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Acercade.this, Vision.class);   
				 i.setAction("fromMenu");
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout favoritos = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_fav_ll);
		favoritos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Acercade.this, Favoritos.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout progreso = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_progreso_ll);
		progreso.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Acercade.this, Progreso.class);   
	    		 startActivity(i);
	    		 finish();
			}
		});
		LinearLayout inicio = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_inicio_ll);
		inicio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				 Intent i = new Intent(Acercade.this, MainActivity.class);   
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
						Intent i = new Intent(Acercade.this, Sincro2.class);   
			    		 startActivity(i);
			    		 finish();
					}
					else{
						Intent i = new Intent(Acercade.this, Sincro.class);   
			    		 startActivity(i);
			    		 finish();
					}
				}
				else{
					Intent i = new Intent(Acercade.this, Sincro.class);   
		    		startActivity(i);
		    		finish();
				}
			}
		});
		LinearLayout compras = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_compras_ll);
		compras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				new RecargarCompras(Acercade.this);
			}
		});

		LinearLayout suscripcion = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_suscription_ll);
		suscripcion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Acercade.this, Suscripcion.class);
				startActivity(i);
				finish();
				// si esta registrado, va a la suscripcion. En caso contrario al login
				/*if (prefs.getBoolean(getString(R.string.registrado),false)){
					Intent i = new Intent(Acercade.this, Suscripcion.class);
					startActivity(i);
					finish();
				}else{
					Intent i = new Intent(Acercade.this, LogIn.class);
					startActivity(i);
					finish();
				}*/
			}
		});
		LinearLayout contacto = (LinearLayout) menu_lateral.findViewById(R.id.id_menu_contact_ll);
		contacto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Acercade.this, Contacto.class);
				startActivity(i);
				finish();
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
		
	}
	
	  protected void alertNewsletter(){
		 
	 		
		final Dialog dialog = new Dialog(Acercade.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_registro);
		dialog.setCancelable(true);

		// set the custom dialog components - text, image and button
		TextView ver = (TextView) dialog.findViewById(R.id.id_alert_btn);
		TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
		TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);
		final EditText email = (EditText) dialog.findViewById(R.id.id_alert_editext);
		final EditText name = (EditText) dialog.findViewById(R.id.id_alert_editext_name);
		final CheckBox  check = (CheckBox) dialog.findViewById(R.id.checkBoxCustomized);
		TextView check_text = (TextView) dialog.findViewById(R.id.checkBoxCustomized_text);
		  check_text.setMovementMethod(LinkMovementMethod.getInstance());
		  TextView tvPolitica = dialog.findViewById(R.id.tv_politica);
		  tvPolitica.setMovementMethod(LinkMovementMethod.getInstance());
		
		text.setTypeface(font);
		ver.setTypeface(font);
		titulo.setTypeface(font);
		check_text.setTypeface(font);
		  tvPolitica.setTypeface(font);
		
		check_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AssetManager assetManager = getAssets();

		        InputStream in = null;
		        OutputStream out = null;
		        File file = new File( getFilesDir(), "abc.pdf");
		        try
		        {
		            in = assetManager.open("pdf/privacidad.pdf");
		            out =  openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

		            copyFile(in, out);
		            in.close();
		            in = null;
		            out.flush();
		            out.close();
		            out = null;
		        } catch (Exception e)
		        {
		            Log.e("tag", e.getMessage());
		        }

		        Intent intent = new Intent(Intent.ACTION_VIEW);
/*		        String filePath = "";
		        String type = "application/pdf";
				if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N) {
					filePath = "content://" +  getFilesDir() + "/abc.pdf";
					intent.setDataAndType(Uri.parse(filePath), type);
				} else {
					filePath = "file://" +  getFilesDir() + "/abc.pdf";
					intent.setDataAndType(Uri.fromFile(new File(filePath)), type);
				}
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);*/
				intent.setData(Uri.parse(Config.url_nota_legal));
		        startActivity(intent);
			}
		});

		// if button is clicked, close the custom dialog
		ver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (check.isChecked()){
					if (isEmailValid(email.getText().toString())){
						if (isNameValid(name.getText().toString())){
							if (Basics.checkConn(Acercade.this)){
								new Downloader.setNewsletter(Basics.checkConn(Acercade.this),
										email.getText().toString(),
										email.getText().toString(),
										Basics.getWifiMac(Acercade.this),
										new Downloader.setNewsletter.AsyncResponse() {
											@Override
											public void processFinish(String respuesta) {
												if (respuesta!=null){
													alert("Se ha dado de alta correctamente.", "Información");
												}
											}
										}).execute();
							} else {
								alert("No hay conexión a Internet.", null);
							}
						}else{
							alert("Debes indicarnos tu nombre.", null);
						}
					}
					else{
						alert("Dirección de email incorrecta.", null);
					}
				}
				else{
					Toast.makeText(Acercade.this, "Ha de aceptar la política de privacidad", Toast.LENGTH_SHORT).show();
				}
			}
		});

		dialog.show();
	  }
	public boolean isNameValid(String name)
	{
		if (name.length() < 3){
			return false;
		}else{
			return true;
		}
	}
	  
	  public boolean isEmailValid(String email)
	    {
	         String regExpn =
	             "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
	                 +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
	                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
	                   +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
	                   +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
	                   +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

	     CharSequence inputStr = email;

	     Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
	     Matcher matcher = pattern.matcher(inputStr);

	     if(matcher.matches())
	        return true;
	     else
	        return false;
	}
	  
	  protected void alert(String mens, String tit){
	 		
		final Dialog dialog = new Dialog(Acercade.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.alert_generico);
		dialog.setCancelable(false);

		// set the custom dialog components - text, image and button
		TextView close = (TextView) dialog.findViewById(R.id.id_alert_btn);
		TextView text = (TextView) dialog.findViewById(R.id.id_alert_text);
		TextView titulo = (TextView) dialog.findViewById(R.id.id_alert_titulo);
		
		
		if (titulo!=null)
			titulo.setText(tit);
		
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
	 
		private void copyFile(InputStream in, OutputStream out) throws IOException
	    {
	        byte[] buffer = new byte[1024];
	        int read;
	        while ((read = in.read(buffer)) != -1)
	        {
	            out.write(buffer, 0, read);
	        }
	    }
}
