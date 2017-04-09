package co.digdaya.kindis.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.SdkCoreFlowBuilder;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ExpiryModel;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;
import java.util.Random;

import co.digdaya.kindis.BuildConfig;
import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;

/**
 * Created by DELL on 3/11/2017.
 */

public class DialogGetPremium implements View.OnClickListener{
    Dialog premium;
    Dialog dialogPay;
    DialogPayment dialogPayment;
    Activity activity;
    AlertDialog.Builder alertDialog;
    LayoutInflater li;
    View dialogView;
    TextView btnCancel, btnPremium;

    SessionHelper sessionHelper;
    Random random;

    public DialogGetPremium(Activity activity, Dialog premium){
        this.activity = activity;
        this.premium = premium;

        li = LayoutInflater.from(activity);
        dialogView = li.inflate(R.layout.dialog_premium, null);

        alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(dialogView);

        sessionHelper = new SessionHelper();
        random = new Random();

        btnCancel = (TextView) dialogView.findViewById(R.id.btn_cancel);
        btnPremium = (TextView) dialogView.findViewById(R.id.btn_premium);

        btnCancel.setOnClickListener(this);
        btnPremium.setOnClickListener(this);
    }

    public void showDialog(){
        if (dialogView.getParent()!=null){
            ((ViewGroup)dialogView.getParent()).removeView(dialogView);
        }
        premium = alertDialog.create();
        if(!activity.isFinishing()){
            premium.show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                premium.dismiss();
                break;
            case R.id.btn_premium:
                premium.dismiss();
                dialogPayment = new DialogPayment(dialogPay, activity);
                dialogPayment.showDialog();
                break;
            default:
                premium.dismiss();
                break;
        }
    }
}