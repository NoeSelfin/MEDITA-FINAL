package org.simo.medita;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.vending.billing.util.IabException;
import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Purchase;

public class RecargarCompras extends Activity implements IabHelper.OnIabSetupFinishedListener, IabHelper.OnIabPurchaseFinishedListener{
	private static final String TAG = null;
	protected SharedPreferences prefs;
	private IabHelper billingHelper;
	protected JSONArray packs;
	protected JSONObject pack;
	private String Producto;
	private int numero_pack=0;

	protected Context ctx;
	
	public RecargarCompras(Context ctx){
		this.ctx = ctx;
//		prefs = ctx.getSharedPreferences("Preferencias", Context.CONTEXT_INCLUDE_CODE | Context.CONTEXT_IGNORE_SECURITY);
		if (ctx == null){
			Log.i("text_medita", "ctx null");
		}
		if (this.ctx == null){
			Log.i("text_medita", "this.ctx null");
		}
		prefs = ctx.getSharedPreferences(ctx.getString(R.string.sharedpref_name), Context.MODE_PRIVATE);
		packs = new JSONArray();
		if (prefs.contains("packs")){
			try {					
				packs = new JSONArray(prefs.getString("packs", ""));
				
				/*for (int i=0; i < packs.length() ; i++){	
					
					pack = packs.optJSONObject(i);					
					startBuyProcess();	
				}*/								
				startBuyProcess();	
				
			} catch (JSONException e) {
			}
		}

	}
	
	 private void startBuyProcess(){
	        String clave = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr8NEwmiClasL7JUgmHaSXmRPOcI6z5CZtyOVM7WsU2y2tyTgTmFt0FqtAKh9U8YZHtFAKYRzmdhLWH4MSnsgF//ZTWCTU+NtokexISaTwz3j3ch17jRevhkoVf70NV5ACVzIYRBJoGcU3padErLQaFixTP2RdsLFiXez8SaDOdbRqKcx2bjZnMzezOGwoGj8OMOe0+5zJmmRvyDLQHeznxw/urabLZxIKTQhS9riMMA151KF65B/zW83KRL9kkfPudE8SfZ1O4YqW3GpFDQyT43KjM4rb8rYBFvU6QuBFcTbm+BvW6nkL6PXDlFGyXBub6VS7Cqjt/tucez/7V3/kQIDAQAB";
	 
	        billingHelper = new IabHelper(ctx, clave);
	        billingHelper.startSetup(this);
	        Log.i("medita_compras", "startBuyProcess");
	    }
	    @Override
	   public void onIabSetupFinished(IabResult result) {
	    	Log.i("medita_compras", "onIabSetupFinished");
	    	Log.i("medita_compras", result.getMessage());
	    	Log.i("medita_compras", result.toString());
	    	
	    	pack = packs.optJSONObject(numero_pack);
	    	Producto = pack.optString("id_pack_compra");
	    	
	       if (result.isSuccess()) {

	           try{
	               if(billingHelper.queryInventory(true, null).hasPurchase(Producto)){
	            	   prefs.edit().putBoolean("comprado_"+String.valueOf(pack.optInt("id_pack")), true).commit();
	                   //Toast.makeText(ctx, "Ya tienes este elemento!", Toast.LENGTH_SHORT).show();

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

	               } else {
	                   //compraElemento(Producto);
	               }

	           } catch(IabException e){
	        	   Log.i("medita_compras", "Error comprando");
	           }
	           numero_pack++;
			   Log.i("medita_compras", "numero_pack: " + numero_pack);
			   Log.i("medita_compras", "packs.length(): " + packs.length());
	           if (numero_pack < packs.length()){
				   startBuyProcess();
	           }
	           else{
	        	   Intent i = new Intent(ctx, MainActivity.class);   
	       		   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	       	   	   ctx.startActivity(i);
	       	   	   ((Activity)ctx).finish();
	       	   	   disposeBillingHelper();
	           }
	          	           
	           

	       } else {
	    	   numero_pack++;
	           errorAlIniciar();
	       }

	   }
	    protected void errorAlIniciar() {
	        Toast.makeText(ctx, "Error al intentar iniciar la compra", Toast.LENGTH_SHORT).show();
	    }
	   /* protected void compraElemento(String producto) {
	        purchaseItem(producto);
	    }
	    protected void purchaseItem(String sku) {
	        billingHelper.launchPurchaseFlow(this, sku, 123, this);
	    }*/
	    
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
	        

	 
	    }
	    /*
	     * COSAS QUE QUERAMOS HACER CUANDO EL PRODUCTO
	     * NO HAYA SIDO ADQUIRIDO
	     */
	 
	    protected void compraFallida(){
	    	Toast.makeText(ctx, "Ha habido un error restaurando sus compras.", Toast.LENGTH_SHORT).show();
	    }
	    
	    private void disposeBillingHelper() {
	        if (billingHelper != null) {
	            billingHelper.dispose();
	        }
	        billingHelper = null;
	    }
	    
	
	
	

}
