package co.digdaya.kindis.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.SdkCoreFlowBuilder;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;

import co.digdaya.kindis.BuildConfig;
import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;

/**
 * Created by DELL on 3/11/2017.
 */

public class DialogGetPremium implements View.OnClickListener{
    Dialog premium;
    Activity activity;
    AlertDialog.Builder alertDialog;
    LayoutInflater li;
    View dialogView;
    TextView btnCancel, btnPremium;

    MidtransSDK midtransSDK;
    TransactionRequest transactionRequest;
    BillInfoModel billInfoModel;

    public DialogGetPremium(Activity activity, Dialog premium){
        this.activity = activity;
        this.premium = premium;

        li = LayoutInflater.from(activity);
        dialogView = li.inflate(R.layout.dialog_premium, null);

        alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(dialogView);

        btnCancel = (TextView) dialogView.findViewById(R.id.btn_cancel);
        btnPremium = (TextView) dialogView.findViewById(R.id.btn_premium);

        btnCancel.setOnClickListener(this);
        btnPremium.setOnClickListener(this);

        initMidtransSDK();
        preparePayment();
    }

    public void showDialog(){
        if (dialogView.getParent()!=null){
            ((ViewGroup)dialogView.getParent()).removeView(dialogView);
        }
        premium = alertDialog.create();
        premium.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                premium.dismiss();
                break;
            case R.id.btn_premium:
                MidtransSDK.getInstance().startPaymentUiFlow(activity);
                break;
        }
    }

    private void initMidtransSDK() {
        SdkUIFlowBuilder.init(activity.getApplicationContext(), activity.getString(R.string.md_client_key), ApiHelper.BASE_URL, new TransactionFinishedCallback() {
            @Override
            public void onTransactionFinished(TransactionResult result) {
                // Handle finished transaction here.
            }
        }).buildSDK();
    }

    private void preparePayment(){
        transactionRequest = new TransactionRequest("001", 10000);

        CustomerDetails customer = new CustomerDetails("Vincent", "Tolanda","tolanda.vincent@gmail.com", "081243009482");

        transactionRequest.setCustomerDetails(customer);

        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
    }
}