package org.simo.medita.billing;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import org.simo.medita.AdapterPacks;
import org.simo.medita.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class BillingHelper  implements PurchasesUpdatedListener {

    private BillingClient billingClient;
    private Context ctx;
    List <String> skusList;
    private SharedPreferences prefs;
    AdapterPacks adapterpacks;

    public BillingHelper(Context ctx, SharedPreferences prefs){
        this.ctx = ctx;
        this.prefs = prefs;
        this.skusList = new ArrayList<String>();
        this.skusList.add("id_suscripcion_mensual");
        this.skusList.add("id_suscripcion_semestral");
        this.skusList.add("id_suscripcion_anual");
        billingClient = BillingClient.newBuilder(ctx).enablePendingPurchases().setListener(this).build();
    }

    //Read products
    public void getProducts() {
        //check if service is already connected
        if (billingClient.isReady()) {
            Log.i("medita_","getProducts");
            initiatePurchase(skusList, false);
        }
        //else reconnect service
        else{
            Log.i("medita_","purchase reconnect service "+ skusList.toString());
            billingClient = BillingClient.newBuilder(ctx).enablePendingPurchases().setListener(this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        Log.i("medita_","getProducts");
                        initiatePurchase(skusList, false);
                    } else {
                        Toast.makeText(ctx,"Error "+billingResult.getDebugMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onBillingServiceDisconnected() {
                    Log.i("medita_","onBillingServiceDisconnected "+ skusList.toString());
                }
            });
        }
    }

    //initiate purchase on button click
    public void purchase(final List <String> skusList) {
    //check if service is already connected
        if (billingClient.isReady()) {
            Log.i("medita_","purchase isReady "+ skusList.toString());

            initiatePurchase(skusList, true);
        }
    //else reconnect service
        else{
            Log.i("medita_","purchase reconnect service "+ skusList.toString());
            billingClient = BillingClient.newBuilder(ctx).enablePendingPurchases().setListener(this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase(skusList, true);
                    } else {
                        Toast.makeText(ctx,"Error "+billingResult.getDebugMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onBillingServiceDisconnected() {
                    Log.i("medita_","onBillingServiceDisconnected "+ skusList.toString());
                }
            });
        }
    }
    public void purchaseRestore(AdapterPacks adapterpacks) {
        this.adapterpacks = adapterpacks;
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

    public void restoreHistory(){
        billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, new PurchaseHistoryResponseListener() {
            @Override
            public void onPurchaseHistoryResponse(@NonNull BillingResult billingResult, @Nullable List<PurchaseHistoryRecord> list) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                    Log.i("medita_", "onPurchaseHistoryResponse:" + list.toString());
                    //handlePurchases(list);
                } else {
                    Log.i("medita_", "onPurchaseHistoryResponse failed with responseCode: " + billingResult.getResponseCode() );
                    Log.i("medita_", "onPurchaseHistoryResponse failed with responseCode: " + billingResult.getResponseCode());
                    Log.i("medita_", "onPurchaseHistoryResponse failed with responseCode: " + billingResult.getDebugMessage());

                }
            }
        });
    }
    public void restore(){
        billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS, new PurchasesResponseListener() {
            @Override
            public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
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
    void handlePurchases(@Nullable List<Purchase> purchases) {
        boolean suscriptions = false;
        Log.i("medita_","handlePurchases ");
        Log.i("medita_",purchases.toString());

        for(Purchase purchase:purchases) {
            Log.i("medita_", purchase.getSkus().get(0).toString());
            Log.i("medita_", purchase.getOriginalJson());

            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                suscriptions = true;

                /*if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                    // Invalid purchase
                    // show error to user
                    Toast.makeText(ctx, "Error : Invalid Purchase", Toast.LENGTH_SHORT).show();
                    return;
                }
                // else purchase is valid
                //if item is purchased and not acknowledged
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams =
                            AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.getPurchaseToken())
                                    .build();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase);
                }
                //else item is purchased and also acknowledged
                else {
                    // Grant entitlement to the user on item purchase
                    // restart activity
                    Toast.makeText(ctx.getApplicationContext(), "Item Purchased", Toast.LENGTH_SHORT).show();
                }*/


            }
        }
        if (suscriptions == true){
            prefs.edit().putBoolean(ctx.getString(R.string.suscrito),true).commit();
            printLog("Subscription -> HAY SUSCRIPCIONES.");

        } else{
            // marcamos que no está suscrito
            prefs.edit().putBoolean(ctx.getString(R.string.suscrito),false).commit();
            printLog("Subscription -> NO HAY SUSCRIPCIONES.");

            if (prefs.getBoolean( ctx.getString(R.string.suscrito_externo), false) == true){
                prefs.edit().putBoolean(ctx.getString(R.string.suscrito),true).commit();
            }
            //TODO
            //Bloquear progreso: NO
            //Bloquear favoritos: SI, pero mirar que no están comprados

            // si no hay sku activos, quitamos la clave en las SharedPref
            //prefs.edit().remove(getString(R.string.skus_activos)).commit();
        }

        if(this.adapterpacks != null){
            adapterpacks.notifyDataSetChanged();
           /*Intent intent = new Intent(ctx, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ctx.startActivity(intent);*/
        }

    }

    private void initiatePurchase(List <String> skusList, final boolean buy) {
        Log.i("medita_","initiatePurchase "+ skusList.toString());
        //final String[] skuIds = ctx.getResources().getStringArray(R.array.suscriptions_sku);
        final List<String> skuIdsList = skusList;
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuIdsList).setType(BillingClient.SkuType.SUBS);

        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult, List<com.android.billingclient.api.SkuDetails> skuDetailsList) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            if (skuDetailsList != null && skuDetailsList.size() > 0) {
                                Log.i("medita_","onSkuDetailsResponse");
                                if(buy == true){
                                     BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetailsList.get(0))
                                        .build();
                                     billingClient.launchBillingFlow((Activity)ctx, flowParams);
                                }else{
                                    Log.i("medita_",skuDetailsList.toString());
                                    //En esta parte tendriamos que devolver la información a los botones de precio y moneda. De momento lo dejamos fijo.
                                    for(SkuDetails skuDetails:skuDetailsList) {
                                        Log.i("medita_",skuDetails.getTitle());
                                        Log.i("medita_",skuDetails.getDescription());
                                        Log.i("medita_",skuDetails.getSku());
                                        Log.i("medita_",skuDetails.getPriceCurrencyCode());
                                        Log.i("medita_",skuDetails.getPrice());

                                    }
                                }
                            }
                            else{
                                //try to add item/product id "purchase" inside managed product in google play console
                                Toast.makeText(ctx,"Purchase Item not Found",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ctx," Error "+billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        Log.i("medita_","onPurchasesUpdated ");
        //if item newly purchased
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            Log.i("medita_","To buy! ");
            //handlePurchase(purchases);
        }
        //if item already purchased then check and reflect changes
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Log.i("medita_","Is buyed!");
            Purchase.PurchasesResult queryAlreadyPurchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
            List<Purchase> alreadyPurchases = queryAlreadyPurchasesResult.getPurchasesList();
            if(alreadyPurchases!=null){
                //handlePurchase(alreadyPurchases);
            }
        }
        //if purchase cancelled
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(ctx,"Purchase Canceled",Toast.LENGTH_SHORT).show();
        }
        // Handle any other error msgs
        else {
            Toast.makeText(ctx,"Error "+billingResult.getDebugMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    /*void handlePurchase(List<Purchase>  purchases) {
        Log.i("medita_","handlePurchases ");
        Log.i("medita_",purchases.toString());


        for(Purchase purchase:purchases) {
            Log.i("medita_",purchase.getSkus().toString());
            Log.i("medita_",String.valueOf(purchase.getPurchaseState()));
            //if item is purchased
           /* if (PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED)
            {
                if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
                    // Invalid purchase
                    // show error to user
                    Toast.makeText(ctx, "Error : Invalid Purchase", Toast.LENGTH_SHORT).show();
                    return;
                }
                // else purchase is valid
                //if item is purchased and not acknowledged
                if (!purchase.isAcknowledged()) {
                    AcknowledgePurchaseParams acknowledgePurchaseParams =
                            AcknowledgePurchaseParams.newBuilder()
                                    .setPurchaseToken(purchase.getPurchaseToken())
                                    .build();
                    billingClient.acknowledgePurchase(acknowledgePurchaseParams, ackPurchase);
                }
                //else item is purchased and also acknowledged
                else {
                // Grant entitlement to the user on item purchase
                // restart activity
                    if(!getPurchaseValueFromPref()){
                        savePurchaseValueToPref(true);
                        Toast.makeText(getApplicationContext(), "Item Purchased", Toast.LENGTH_SHORT).show();
                        this.recreate();
                    }
                }
            }
            //if purchase is pending
            else if( PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.PENDING)
            {
                Toast.makeText(ctx,
                        "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show();
            }
            //if purchase is unknown
            else if(PRODUCT_ID.equals(purchase.getSku()) && purchase.getPurchaseState() == Purchase.PurchaseState.UNSPECIFIED_STATE)
            {
                savePurchaseValueToPref(false);
                purchaseStatus.setText("Purchase Status : Not Purchased");
                purchaseButton.setVisibility(View.VISIBLE);
                Toast.makeText(ctx, "Purchase Status Unknown", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
    AcknowledgePurchaseResponseListener ackPurchase = new AcknowledgePurchaseResponseListener() {
        @Override
        public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
            if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
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


}
