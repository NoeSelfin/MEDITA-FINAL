package org.simo.medita;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.billing.BillingHelperProducts;

public class RecargarCompras {
	private static final String TAG = null;
	protected SharedPreferences prefs;
	protected BillingHelperProducts bhp;
	protected JSONArray packs;
	protected JSONObject pack;
	private String Producto;
	private int numero_pack=0;

	protected Context ctx;

	public RecargarCompras(Context ctx){
		this.ctx = ctx;
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

				bhp = new BillingHelperProducts(ctx, prefs);
				bhp.purchaseRestore();
				//prefs.edit().putBoolean("comprado_"+String.valueOf(pack.optInt("id_pack")), true).commit();

			} catch (JSONException e) {
			}
		}
	}

}
