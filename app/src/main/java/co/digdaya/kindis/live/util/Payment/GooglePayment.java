package co.digdaya.kindis.live.util.Payment;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.model.PriceListModel;
import co.digdaya.kindis.live.util.GoogleBilling.IabHelper;
import co.digdaya.kindis.live.util.GoogleBilling.IabResult;
import co.digdaya.kindis.live.util.GoogleBilling.Inventory;
import co.digdaya.kindis.live.util.GoogleBilling.Purchase;
import co.digdaya.kindis.live.util.GoogleBilling.SkuDetails;

/**
 * Created by DELL on 4/9/2017.
 */

public class GooglePayment {
    private Activity activity;
    private IabHelper mHelper;
    private String googleCode;
    private String dataExtra;
    private PriceListModel.Data data;
    private List<String> skuList;
    ResultListener listener;

    public GooglePayment(Activity activity, String googleCode, String dataExtra) {
        this.activity = activity;
        this.googleCode = googleCode;
        this.dataExtra = dataExtra;
        init();
    }

    public GooglePayment(Activity activity, String dataExtra, ResultListener listener) {
        this.activity = activity;
        this.dataExtra = dataExtra;
        this.listener = listener;
        init();
    }

    private void init(){
        mHelper = new IabHelper(activity, activity.getString(R.string.base64EncodedPublicKey));
        mHelper.enableDebugLogging(true, "Google Play");

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess()) {
                    listener.showListDetail();
                    Log.d("billinggoogle", "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.d("billinggoogle", "In-app Billing is set up OK");
                    getInventory();
                }
            }
        });
    }

    /*
     ** Method for releasing resources (dispose of object)
     */
//    public void dispose() {
//        if (mHelper != null) {
//            try {
//                mHelper.dispose();
//            } catch (IabHelper.IabAsyncInProgressException e) {
//                e.printStackTrace();
//            }
//            mHelper = null;
//        }
//    }

    private void getInventory() {
        if (mHelper == null) return;

        skuList = new ArrayList<>();
        skuList.add("co.digdaya.kindis.live.subscription.weekly");
        skuList.add("co.digdaya.kindis.live.subscription.month");
        skuList.add("co.digdaya.kindis.live.subscription.3months");
        skuList.add("co.digdaya.kindis.live.subscription.6months");
        skuList.add("co.digdaya.kindis.live.subscription.year");

        mHelper.queryInventoryAsync(true, skuList, mQueryFinishedListener);
    }

    public void buyClick() {
        mHelper.launchPurchaseFlow(activity, googleCode, 10001,
                mPurchaseFinishedListener, dataExtra);
    }

    public void buyClick(String googleCode) {
        mHelper.launchSubscriptionPurchaseFlow(activity, googleCode, 10001,
                mPurchaseFinishedListener, dataExtra);
    }

    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                Toast.makeText(activity, result.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            else if (purchase.getSku().equals(googleCode)) {
                Toast.makeText(activity, "Berhasil menjadi member premium", Toast.LENGTH_SHORT).show();
            }
            else if (purchase.getSku().equals("purchased")) {
                consumeItem();
            }
        }
    };

    private void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

            if (result.isFailure()) {
                // handle error
                listener.showListDetail();
                return;
            } else {
                List<SkuDetails> skuDetailsList = new ArrayList<>();
                for (String sku : skuList) {
                    SkuDetails skuDetails = inventory.getSkuDetails(sku);
                    skuDetailsList.add(skuDetails);
                }
                listener.showListDetail(skuDetailsList);
            }

        }
    };


        private IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase("purchased"),
                        mConsumeFinishedListener);
            }
        }
    };

    private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {
                }
            };

    public interface ResultListener {
        void showListDetail(List<SkuDetails> skuDetailsList);

        void showListDetail();
    }

    public IabHelper getmHelper() {
        return mHelper;
    }
}
