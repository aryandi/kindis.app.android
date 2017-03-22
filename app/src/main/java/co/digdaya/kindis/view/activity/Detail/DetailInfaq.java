package co.digdaya.kindis.view.activity.Detail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.digdaya.kindis.R;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerActivity;
import co.digdaya.kindis.util.GoogleBilling.IabHelper;
import co.digdaya.kindis.util.GoogleBilling.IabResult;
import co.digdaya.kindis.util.GoogleBilling.Inventory;
import co.digdaya.kindis.util.GoogleBilling.Purchase;
import co.digdaya.kindis.view.dialog.DialogDonate;

public class DetailInfaq extends BottomPlayerActivity implements View.OnClickListener {
    AppBarLayout appBarLayout;
    LinearLayout contFloatingButton;
    RelativeLayout contLabel;
    Toolbar toolbar;
    TextView titleToolbar;
    TextView titleDetail;
    TextView description;
    Button btnDonate;
    DialogDonate dialogDonate;
    Dialog dialog;

    IabHelper mHelper;

    public DetailInfaq(){
        layout = R.layout.activity_detail_infaq;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);
        contFloatingButton = (LinearLayout) findViewById(R.id.cont_floating_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contLabel = (RelativeLayout) findViewById(R.id.cont_label);
        titleToolbar = (TextView) toolbar.findViewById(R.id.title_toolbar);
        titleDetail = (TextView) findViewById(R.id.title_detail);
        description = (TextView) findViewById(R.id.description);
        btnDonate = (Button) findViewById(R.id.btn_donate);

        dialogDonate = new DialogDonate(this, dialog);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar.setVisibility(View.INVISIBLE);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;
            boolean isShow = false;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    contLabel.setVisibility(View.INVISIBLE);
                    contFloatingButton.setVisibility(View.INVISIBLE);
                    titleToolbar.setVisibility(View.VISIBLE);
                    isShow = true;
                } else if (isShow) {
                    contLabel.setVisibility(View.VISIBLE);
                    contFloatingButton.setVisibility(View.VISIBLE);
                    titleToolbar.setVisibility(View.INVISIBLE);
                    isShow = false;
                }
            }
        });

        initDetailInfaq();

        btnDonate.setOnClickListener(this);

        mHelper = new IabHelper(this, getString(R.string.base64EncodedPublicKey));

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

    private void initDetailInfaq(){
        titleDetail.setText(getIntent().getStringExtra("title"));
        titleToolbar.setText(getIntent().getStringExtra("title"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_donate:
                //dialogDonate.showDialog();
                buyClick(view);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void buyClick(View view) {
        mHelper.launchPurchaseFlow(this, "purchased", 10001,
                mPurchaseFinishedListener, "mypurchasetoken");
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
