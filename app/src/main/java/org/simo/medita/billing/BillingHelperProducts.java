package org.simo.medita.billing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simo.medita.MainActivity;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BillingHelperProducts  implements PurchasesUpdatedListener {

    private BillingClient billingClient;
    private Context ctx;
    private SharedPreferences prefs;

    public BillingHelperProducts(Context ctx, SharedPreferences prefs){
        this.ctx = ctx;
        this.prefs = prefs;
        billingClient = BillingClient.newBuilder(ctx).enablePendingPurchases().setListener(this).build();

    }

    public void purchaseRestore() {
        //check if service is already connected
        if (billingClient.isReady()) {
            Log.i("medita_","restore isReady.");
            restore();
        }
        //else reconnect service
        else{
            Log.i("medita_","restore reconnect service.");
            billingClient = BillingClient.newBuilder(ctx).enablePendingPurchases().setListener(this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        Log.i("medita_","restore isReady.");
                        restore();
                    } else {
                        Toast.makeText(ctx,"Error "+billingResult.getDebugMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onBillingServiceDisconnected() {
                    Log.i("medita_","onBillingServiceDisconnected");
                }
            });
        }
    }

    public void restore(){
        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP, new PurchaseHistoryResponseListener() {
            @Override
            public void onPurchaseHistoryResponse(@NonNull BillingResult billingResult, @Nullable List<PurchaseHistoryRecord> list) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                    Log.i("medita_", "onPurchaseHistoryResponse:" + list.toString());
                    handlePurchases(list);
                } else {
                    Log.i("medita_", "onPurchaseHistoryResponse failed with responseCode: " + billingResult.getResponseCode() );
                    Log.i("medita_", "onPurchaseHistoryResponse failed with responseCode: " + billingResult.getResponseCode());
                    Log.i("medita_", "onPurchaseHistoryResponse failed with responseCode: " + billingResult.getDebugMessage());

                }
            }
        });
    }


    void handlePurchases(@Nullable List<PurchaseHistoryRecord> purchases) {
        Log.i("medita_","handlePurchases ");
        Log.i("medita_",purchases.toString());
        boolean buyedProducts = false;

        for(PurchaseHistoryRecord purchase:purchases) {
            Log.i("medita_",purchase.getSkus().get(0).toString());
            JSONObject pack;

            if (prefs.getString("packs", "").compareToIgnoreCase("") != 0){
                JSONArray packs = null;
                try {
                    packs = new JSONArray(prefs.getString("packs", ""));
                    for (int i = 0; i < packs.length(); i++) {
                        pack = packs.optJSONObject(i);
                        String Producto = pack.optString("id_pack_compra");

                        if (Producto.compareToIgnoreCase(purchase.getSkus().get(0).toString()) == 0){
                            printLog("saveBuys elemento con sku: " + Producto);
                            prefs.edit().putBoolean("comprado_"+String.valueOf(pack.optInt("id_pack")), true).commit();
                            //Toast.makeText(ctx, "Ya tienes este elemento!", Toast.LENGTH_SHORT).show();

                            //If from main
                            //adapterpacks.notifyDataSetChanged();
                            buyedProducts = true;
                        }
                    }
                } catch (JSONException e) {
                }
            }
        }
        if (buyedProducts){
            Intent intent = new Intent(ctx, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ctx.startActivity(intent);
        }
    }
    AcknowledgePurchaseResponseListener ackPurchase = new AcknowledgePurchaseResponseListener() {
        @Override
        public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
            if(billingResult.getResponseCode()== BillingClient.BillingResponseCode.OK){
                //if purchase is acknowledged
                // Grant entitlement to the user. and restart activity
                ////savePurchaseValueToPref(true);
                Toast.makeText(ctx, "Item Purchased", Toast.LENGTH_SHORT).show();
                ////MainActivity.this.recreate();
            }
        }
    };
    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     * <p>Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     * </p>
     */
    private boolean verifyValidSignature(String signedData, String signature) {
        try {
// To get key go to Developer Console > Select your app > Development Tools > Services & APIs.
            String base64Key = Config.license_key;
            return Security.verifyPurchase(base64Key, signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }
    private void printLog(String text){
        if (org.simo.medita.Config.log){
            Log.i(org.simo.medita.Config.tag, text);
        }
    }


    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

    }
}

