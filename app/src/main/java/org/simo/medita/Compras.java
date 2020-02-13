package org.simo.medita;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.util.IabException;
import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Purchase;

import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.extras.Basics;

public class Compras extends Activity implements IabHelper.OnIabSetupFinishedListener, IabHelper.OnIabPurchaseFinishedListener{
	private static final String TAG = null;
	protected SharedPreferences prefs;
	protected LinearLayout atras;
	protected Typeface font;
	protected JSONObject pack;
	protected TextView titulo;
	protected TextView texto;
	protected TextView btn;
	protected ImageView ico;
	protected ImageView ico1;
	protected ImageView ico2;
	protected ImageView ico3;
	protected RelativeLayout fondo;

	private IabHelper billingHelper;
	
	private String Producto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_compras);
		
		font = Typeface.createFromAsset(getAssets(), "tipo/Dosis-Regular.otf");
//		prefs = getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		prefs = getSharedPreferences(getString(R.string.sharedpref_name),Context.MODE_PRIVATE);
		atras = (LinearLayout)findViewById(R.id.id_compras_atras);		
		ico = (ImageView)findViewById(R.id.id_compras_ico);	
		ico1 = (ImageView)findViewById(R.id.id_compas_icono1);	
		ico2 = (ImageView)findViewById(R.id.id_compas_icono2);	
		ico3 = (ImageView)findViewById(R.id.id_compas_icono3);	
		fondo = (RelativeLayout)findViewById(R.id.id_compras_header);	
		titulo = (TextView)findViewById(R.id.id_compras_titulo);
		texto = (TextView)findViewById(R.id.id_compras_text1);
		btn = (TextView)findViewById(R.id.id_compras_btn);
		titulo.setTypeface(font);
		btn.setTypeface(font);
		texto.setTypeface(font);
		
		
		Intent i = getIntent();
		if(i != null) {
			Bundle extras = getIntent().getExtras();
			if(extras != null) {
				 try {
				 		pack = new JSONObject(extras.getString("pack"));

					 // texto en funcion si esta SUSCRITO o debe SUSCRIBIRSE
					 if (prefs.getBoolean(getString(R.string.suscrito),false)){
					 	btn.setText(getString(R.string.ya_suscrito));
					 }else{
						 btn.setText(getString(R.string.suscribirse));
					 }

						texto.setText(pack.optString("descripcion"));
						
						titulo.setText("· "+pack.optString("pack_titulo")+" ·");
						Bitmap bitmap = Basics.readFileInternal(this,"iconos",pack.getString("pack_icono"));
						if (bitmap!=null){ 
				  	    	 ico.setImageBitmap(bitmap);
			  	        }
 			  	        bitmap = Basics.readFileInternal(this,"fondos",pack.getString("pack_fondo_med"));
			  	       
			  	        if (bitmap!=null){ 
			  	    	  fondo.setBackground(new BitmapDrawable(this.getResources(), bitmap));
			  	        }
			  	        else{
			  	        	fondo.setBackgroundColor(Color.parseColor(pack.getString("pack_color").trim()));
			  	        }
				 } catch (JSONException e) {
						Log.i("medita","Compras.");
				}
			}			
		}
		
		
		atras.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(Compras.this, MainActivity.class);   
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		   		startActivity(i);
		   		finish();
			}
		});		
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.i("medita_compras", "Compra apretada!");
				Log.i("medita_compras", "prefs.getBoolean(getString(R.string.registrado),false) == "
						+ prefs.getBoolean(getString(R.string.registrado),false));
//				startBuyProcess();

				Intent i = new Intent(Compras.this, Suscripcion.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
				/*if(prefs.getBoolean(getString(R.string.registrado),false)){
					Intent i = new Intent(Compras.this, Suscripcion.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
				}else{
					Intent i = new Intent(Compras.this, LogIn.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
				}*/
			}
		});	
		/*btn.setOnTouchListener(new View.OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
	                if (event.getAction() == MotionEvent.ACTION_DOWN) {
	                	btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
	                	btn.setBackgroundResource(R.drawable.buy_down);	                	
	                	
	                } else if (event.getAction() == MotionEvent.ACTION_UP) {
	                	btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	                	btn.setBackgroundResource(R.drawable.buy_up);
	                	
	                }
	           
	            return false;
		    }
		});*/
					
		setIcons();
		
		
		Drawable draw = getResources().getDrawable(R.drawable.atras_ico);
		draw.setColorFilter(Color.parseColor(pack.optString("pack_color_secundario")), PorterDuff.Mode.MULTIPLY );
		((ImageView)findViewById(R.id.id_compras_atras_ico)).setImageDrawable(draw);
		
	}
	
	public void setIcons(){
		
		String[] props;
		try {
			Log.i("medita",pack.getString("palabras_clave"));
			props = pack.getString("palabras_clave").split("-");
			Log.i("medita",props[0]);
			Log.i("medita",props[1]);

			
			if (props[0].trim().compareTo("Presencia") == 0){
				ico1.setImageResource(R.drawable.ico_presencia);

			}
			else if (props[0].trim().compareTo("Enfoque") == 0){
				ico1.setImageResource(R.drawable.ico_enfoque);
	
			}
			else if (props[0].trim().compareTo("Relax") == 0){
				ico1.setImageResource(R.drawable.ico_relax);

			}
			else if (props[0].trim().compareTo("Descanso") == 0){
				ico1.setImageResource(R.drawable.ico_descanso);

			}
			else if (props[0].trim().compareTo("Bienestar") == 0){
				ico1.setImageResource(R.drawable.ico_bienestar);

			}
			else if (props[0].trim().compareTo("Equilibrio") == 0){
				ico1.setImageResource(R.drawable.ico_equilibrio);

			}
			
			
			 if (props[1].trim().compareTo("Presencia") == 0){
				ico2.setImageResource(R.drawable.ico_presencia);

			}
			else if (props[1].trim().compareTo("Enfoque") == 0){
				ico2.setImageResource(R.drawable.ico_enfoque);
			
			}
			else if (props[1].trim().compareTo("Relax") == 0){
				ico2.setImageResource(R.drawable.ico_relax);

			}
			else if (props[1].trim().compareTo("Descanso") == 0){
				ico2.setImageResource(R.drawable.ico_descanso);

			}
			else if (props[1].trim().compareTo("Bienestar") == 0){
				ico2.setImageResource(R.drawable.ico_bienestar);

			}
			else if (props[1].trim().compareTo("Equilibrio") == 0){
				ico2.setImageResource(R.drawable.ico_equilibrio);

			}

			if (props[2].trim().compareTo("Presencia") == 0){
				 ico3.setImageResource(R.drawable.ico_presencia);
			}
			else if (props[2].trim().compareTo("Enfoque") == 0){
				ico3.setImageResource(R.drawable.ico_enfoque);		
			}
			else if (props[2].trim().compareTo("Relax") == 0){
				ico3.setImageResource(R.drawable.ico_relax);
			}
			else if (props[2].trim().compareTo("Descanso") == 0){
				ico3.setImageResource(R.drawable.ico_descanso);
			}
			else if (props[2].trim().compareTo("Bienestar") == 0){
				ico3.setImageResource(R.drawable.ico_bienestar);
			}
			else if (props[2].trim().compareTo("Equilibrio") == 0){
				ico3.setImageResource(R.drawable.ico_equilibrio);
			}		

		} catch (JSONException e) {
		}			
		
	}
	
	@Override
	public void onBackPressed()
	{		
		Intent i = new Intent(Compras.this, MainActivity.class);   
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
   		startActivity(i);
   		finish();			
	}
	
	 private void startBuyProcess(){
//	        String clave = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr8NEwmiClasL7JUgmHaSXmRPOcI6z5CZtyOVM7WsU2y2tyTgTmFt0FqtAKh9U8YZHtFAKYRzmdhLWH4MSnsgF//ZTWCTU+NtokexISaTwz3j3ch17jRevhkoVf70NV5ACVzIYRBJoGcU3padErLQaFixTP2RdsLFiXez8SaDOdbRqKcx2bjZnMzezOGwoGj8OMOe0+5zJmmRvyDLQHeznxw/urabLZxIKTQhS9riMMA151KF65B/zW83KRL9kkfPudE8SfZ1O4YqW3GpFDQyT43KjM4rb8rYBFvU6QuBFcTbm+BvW6nkL6PXDlFGyXBub6VS7Cqjt/tucez/7V3/kQIDAQAB";
         String clave = Config.license_key;
	        billingHelper = new IabHelper(this, clave);
	        billingHelper.startSetup(this);
	        Log.i("medita_compras", "startBuyProcess");
	    }
	    @Override
	   public void onIabSetupFinished(IabResult result) {
	    	Log.i("medita_compras", "onIabSetupFinished");
	    	Log.i("medita_compras", result.getMessage());
	    	Log.i("medita_compras", result.toString());
	    	
	    	Producto = pack.optString("id_pack_compra");
	    	
	       if (result.isSuccess()) {

	           try{
	               if(billingHelper.queryInventory(true, null).hasPurchase(Producto)){
	            	   prefs.edit().putBoolean("comprado_"+String.valueOf(pack.optInt("id_pack")), true).commit();
	                   Toast.makeText(Compras.this, "Ya tienes este elemento!", Toast.LENGTH_SHORT).show();
	                   
	                   
	                   if ((pack.optInt("id_pack") == 2) || (pack.optInt("id_pack") == 3) ||  (pack.optInt("id_pack") == 4) || (pack.optInt("id_pack") == 5)){
	       	        	
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
	                   
	                   Intent i = new Intent(Compras.this, MainActivity.class);   
		       			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		       	   		startActivity(i);
		       	   		finish();	
	       	   		
	               } else {
	                   compraElemento(Producto);
	               }

	           } catch(IabException e){
	        	   Log.i("medita_compras", "Error comprando");
	           }

	       } else {

	           errorAlIniciar();
	       }

	   }
	    protected void errorAlIniciar() {
	        Toast.makeText(Compras.this, "Error al intentar iniciar la compra", Toast.LENGTH_SHORT).show();
	    }
	    protected void compraElemento(String producto) {
	        purchaseItem(producto);
	    }
	    protected void purchaseItem(String sku) {
	        billingHelper.launchPurchaseFlow(this, sku, 123, this);
	    }
	    
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	        if (!billingHelper.handleActivityResult(requestCode, resultCode, data)) {	           
	            super.onActivityResult(requestCode, resultCode, data);
	        }
	        else {
	        }
	    }
	    
	    @Override
	    public void onIabPurchaseFinished(IabResult result, Purchase info) {
	        if (result.isFailure()) {
	            compraFallida();
	        } else if (Producto.equals(info.getSku())) {
	            compraCorrecta(result, info);
	        }
	        
	    }
	    /*
	     * COSAS QUE QUERAMOS HACER CUANDO SE HAYA
	     * ADQUIRIDO EL PRODUCTO CON EXITO
	     */
	    protected void compraCorrecta(IabResult result, Purchase info){
	        // Consumimos los elementos a fin de poder probar varias compras
	       // billingHelper.consumeAsync(info, null);
	        
	        if ((pack.optInt("id_pack") == 2) || (pack.optInt("id_pack") == 3) ||  (pack.optInt("id_pack") == 4) || (pack.optInt("id_pack") == 5)){
	        	
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
	      	        
	        
	        prefs.edit().putBoolean("comprado_"+String.valueOf(pack.optInt("id_pack")), true).commit();
	       
	        Intent i = new Intent(Compras.this, MainActivity.class);   
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	   		startActivity(i);
	   		finish();        
	 
	    }
	    /*
	     * COSAS QUE QUERAMOS HACER CUANDO EL PRODUCTO
	     * NO HAYA SIDO ADQUIRIDO
	     */
	 
	    protected void compraFallida(){
	    	alert("Ha habido un error con su compra.");
	    }
	    //LIMPIAMOS
	    @Override
	    protected void onDestroy() {
	        disposeBillingHelper();
	        super.onDestroy();
	    }
	 
	    private void disposeBillingHelper() {
	        if (billingHelper != null) {
	            billingHelper.dispose();
	        }
	        billingHelper = null;
	    }
	    
	    
	    protected void alert(String mens){
	 		
				final Dialog dialog = new Dialog(Compras.this);
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
