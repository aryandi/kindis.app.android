package co.digdaya.kindis.util.Payment;

import android.app.Activity;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import co.digdaya.kindis.BuildConfig;
import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;

/**
 * Created by DELL on 4/9/2017.
 */

public class MidtransPayment {
    TransactionRequest transactionRequest;
    Activity activity;
    SessionHelper sessionHelper;
    Random random;
    String transID, transName;
    int price;

    public MidtransPayment(Activity activity, String transID, int price, String transName) {
        this.activity = activity;
        this.transID = transID;
        this.price = price;
        this.transName = transName;

        sessionHelper = new SessionHelper();
        random = new Random();
        initMidtransSDK();
        preparePayment();
    }

    private void initMidtransSDK() {
        SdkUIFlowBuilder.init(activity.getApplicationContext(), BuildConfig.CLIENT_KEY, BuildConfig.BASE_URL, new TransactionFinishedCallback() {
            @Override
            public void onTransactionFinished(TransactionResult result) {
                if (result.getResponse() != null){
                    HashMap<String, String> param = new HashMap<String, String>();
                    param.put("uid", sessionHelper.getPreferences(activity, "user_id"));
                    param.put("token_access", sessionHelper.getPreferences(activity, "token_access"));
                    param.put("dev_id", "2");
                    param.put("client_id", "");
                    param.put("package", "");
                    param.put("trans_id", result.getResponse().getTransactionId());
                    param.put("order", "");
                    param.put("payment_type", result.getResponse().getPaymentType());
                    param.put("payment_status", result.getResponse().getStatusCode());
                    param.put("payment_status_msg", result.getResponse().getStatusMessage());
                    param.put("price", result.getResponse().getGrossAmount());
                    param.put("trans_time", result.getResponse().getTransactionTime());

                    new VolleyHelper().post(ApiHelper.PAYMENT, param, new VolleyHelper.HttpListener<String>() {
                        @Override
                        public void onReceive(boolean status, String message, String response) {
                            System.out.println("Response payment: "+response);
                        }
                    });
                }
            }
        })
                .enableLog(true)
                .buildSDK();
    }

    private void preparePayment(){
        transactionRequest = new TransactionRequest(transID, price);

        CustomerDetails customer = new CustomerDetails();
        customer.setFirstName(sessionHelper.getPreferences(activity.getApplicationContext(), "fullname"));
        customer.setEmail(sessionHelper.getPreferences(activity.getApplicationContext(), "email"));
        customer.setPhone("");
        transactionRequest.setCustomerDetails(customer);

        UserAddress userAddress = new UserAddress();
        userAddress.setAddress("");
        userAddress.setCity("");
        userAddress.setZipcode("");
        userAddress.setCountry("");

        ArrayList<UserAddress> userAddresses = new ArrayList<>();
        userAddresses.add(userAddress);

        UserDetail userDetail = new UserDetail();
        userDetail.setUserFullName(sessionHelper.getPreferences(activity.getApplicationContext(), "fullname"));
        userDetail.setEmail(sessionHelper.getPreferences(activity.getApplicationContext(), "email"));
        userDetail.setPhoneNumber("");
        userDetail.setUserAddresses(userAddresses);
        LocalDataHandler.saveObject("user_details", userDetail);

        ItemDetails itemDetails = new ItemDetails("1", price, 1, transName);

        // Add item details into item detail list.
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        transactionRequest.setItemDetails(itemDetailsArrayList);

        CreditCard creditCardOptions = new CreditCard();
        creditCardOptions.setSaveCard(false);
        creditCardOptions.setSecure(false);

        transactionRequest.setCreditCard(creditCardOptions);
        transactionRequest.setCardPaymentInfo(activity.getString(R.string.card_click_type_none), true);

        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
    }

    public void startPayment(){
        MidtransSDK.getInstance().startPaymentUiFlow(activity);
    }
}
