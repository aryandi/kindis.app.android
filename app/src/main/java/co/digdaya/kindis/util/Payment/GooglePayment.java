package co.digdaya.kindis.util.Payment;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import co.digdaya.kindis.R;
import co.digdaya.kindis.util.GoogleBilling.IabHelper;
import co.digdaya.kindis.util.GoogleBilling.IabResult;
import co.digdaya.kindis.util.GoogleBilling.Inventory;
import co.digdaya.kindis.util.GoogleBilling.Purchase;

/**
 * Created by DELL on 4/9/2017.
 */

public class GooglePayment {
    Activity activity;
    IabHelper mHelper;

    public GooglePayment(Activity activity) {
        this.activity = activity;

        init();
    }

    private void init(){
        mHelper = new IabHelper(activity, activity.getString(R.string.base64EncodedPublicKey));

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result)
            {
                if (!result.isSuccess()) {
                    Log.d("billinggoogle", "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.d("billinggoogle", "In-app Billing is set up OK");
                }
            }
        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

    public void buyClick() {
        mHelper.launchPurchaseFlow(activity, "co.digdaya.kindis.premium", 10001,
                mPurchaseFinishedListener, "kindis123");
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals("purchased")) {
                consumeItem();
            }

        }
    };

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
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

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {
                }
            };
}
