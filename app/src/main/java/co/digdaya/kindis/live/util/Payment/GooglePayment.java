package co.digdaya.kindis.live.util.Payment;

import android.app.Activity;
import android.util.Log;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.model.PriceListModel;
import co.digdaya.kindis.live.util.GoogleBilling.IabHelper;
import co.digdaya.kindis.live.util.GoogleBilling.IabResult;
import co.digdaya.kindis.live.util.GoogleBilling.Inventory;
import co.digdaya.kindis.live.util.GoogleBilling.Purchase;

/**
 * Created by DELL on 4/9/2017.
 */

public class GooglePayment {
    private Activity activity;
    private IabHelper mHelper;
    private String googleCode;
    private String dataExtra;
    private PriceListModel.Data data;

    public GooglePayment(Activity activity, String googleCode, String dataExtra) {
        this.activity = activity;
        this.googleCode = googleCode;
        this.dataExtra = dataExtra;
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

    public void buyClick() {
        mHelper.launchPurchaseFlow(activity, googleCode, 10001,
                mPurchaseFinishedListener, dataExtra);
    }

    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
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

    private void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

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
}
