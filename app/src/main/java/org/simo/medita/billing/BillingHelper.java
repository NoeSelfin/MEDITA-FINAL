package org.simo.medita.billing;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import org.simo.medita.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;


public class BillingHelper  implements PurchasesUpdatedListener {

    private BillingClient billingClient;
    private Context ctx;

    public BillingHelper(Context ctx){
        this.ctx = ctx;
        billingClient = BillingClient.newBuilder(ctx).enablePendingPurchases().setListener(this).build();
    }

    //initiate purchase on button click
    public void purchase(final String Producto) {
    //check if service is already connected
        if (billingClient.isReady()) {
            Log.i("medita_","purchase isReady "+ Producto);

            initiatePurchase(Producto);
        }
    //else reconnect service
        else{
            Log.i("medita_","purchase reconnect service "+ Producto);
            billingClient = BillingClient.newBuilder(ctx).enablePendingPurchases().setListener(this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase(Producto);
                    } else {
                        Toast.makeText(ctx,"Error "+billingResult.getDebugMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onBillingServiceDisconnected() {
                    Log.i("medita_","onBillingServiceDisconnected "+ Producto);
                }
            });
        }
    }

    private void initiatePurchase(String Producto) {
        Log.i("medita_","initiatePurchase "+ Producto);
        final String[] skuIds = ctx.getResources().getStringArray(R.array.suscriptions_sku);
        final List<String> skuIdsList = Arrays.asList(Producto);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuIdsList).setType(BillingClient.SkuType.SUBS);
        /*List<String> skuList = new ArrayList<>();
        skuList.add("Producto");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);*/

        billingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult, List<com.android.billingclient.api.SkuDetails> skuDetailsList) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            if (skuDetailsList != null && skuDetailsList.size() > 0) {
                                Log.i("medita_","onSkuDetailsResponse");
                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetailsList.get(0))
                                        .build();
                                billingClient.launchBillingFlow((Activity)ctx, flowParams);
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
            handlePurchases(purchases);
        }
        //if item already purchased then check and reflect changes
        else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            Purchase.PurchasesResult queryAlreadyPurchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
            List<Purchase> alreadyPurchases = queryAlreadyPurchasesResult.getPurchasesList();
            if(alreadyPurchases!=null){
                handlePurchases(alreadyPurchases);
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

    void handlePurchases(List<Purchase>  purchases) {
        Log.i("medita_","handlePurchases ");
        for(Purchase purchase:purchases) {
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
            }*/
        }
    }
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
}
