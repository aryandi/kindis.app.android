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
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ExpiryModel;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.corekit.utilities.Utils;
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
                MidtransSDK.getInstance().startMandiriBankTransferUIFlow(activity.getApplicationContext());


                //MidtransSDK.getInstance().startPaymentUiFlow(activity);
                break;
        }
    }

    private void initMidtransSDK() {
        SdkUIFlowBuilder.init(activity.getApplicationContext(), activity.getString(R.string.md_client_key), BuildConfig.BASE_URL, new TransactionFinishedCallback() {
            @Override
            public void onTransactionFinished(TransactionResult result) {
                // Handle finished transaction here.
            }
        })
                .buildSDK();
    }

    private void preparePayment(){
        transactionRequest = new TransactionRequest("001", 10000);

        CustomerDetails customer = new CustomerDetails("Vincent", "Tolanda","tolanda.vincent@gmail.com", "081243009482");
        transactionRequest.setCustomerDetails(customer);

        ItemDetails itemDetails = new ItemDetails("1", 1000, 1, "Trekking Shoes");
        ItemDetails itemDetails1 = new ItemDetails("2", 1000, 2, "Casual Shoes");
        ItemDetails itemDetails2 = new ItemDetails("3", 1000, 3, "Formal Shoes");

        // Add item details into item detail list.
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        itemDetailsArrayList.add(itemDetails1);
        itemDetailsArrayList.add(itemDetails2);
        transactionRequest.setItemDetails(itemDetailsArrayList);

        BillInfoModel billInfoModel = new BillInfoModel("demo_label", "demo_value");
        transactionRequest.setBillInfoModel(billInfoModel);

        ExpiryModel expiryModel = new ExpiryModel();
        expiryModel.setStartTime(Utils.getFormattedTime(System.currentTimeMillis()));
        expiryModel.setDuration(1);
        transactionRequest.setExpiry(expiryModel);

        BillingAddress billingAddress1 = new BillingAddress("Vincent", "Tolanda", "jalan", "jakarta", "91811", "081243009482", "ID");
        ArrayList<BillingAddress> billingAddressList = new ArrayList<>();
        billingAddressList.add(billingAddress1);
        transactionRequest.setBillingAddressArrayList(billingAddressList);

        ShippingAddress shippingAddress1 = new ShippingAddress();
        shippingAddress1.setAddress("jalan");
        shippingAddress1.setCity("jakarta");
        shippingAddress1.setCountryCode("ID");
        shippingAddress1.setFirstName("Vincent");
        shippingAddress1.setLastName("Parinding");
        shippingAddress1.setPhone("081243009482");
        shippingAddress1.setPostalCode("91811");
        ArrayList<ShippingAddress> shippingAddressList = new ArrayList<>();
        shippingAddressList.add(shippingAddress1);
        transactionRequest.setShippingAddressArrayList(shippingAddressList);

        CreditCard creditCard = new CreditCard();
        creditCard.setSaveCard(false);
        creditCard.setSecure(false);
        creditCard.setBank(BankType.MANDIRI);
        transactionRequest.setCreditCard(creditCard);
        transactionRequest.setCardPaymentInfo("one_click", false);

        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
    }
}