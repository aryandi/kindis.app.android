package co.digdaya.kindis.live.util.Payment;

import android.app.Activity;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import co.digdaya.kindis.live.BuildConfig;
import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.AnalyticHelper;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.util.BackgroundProses.ResultPayment;

/**
 * Created by DELL on 4/9/2017.
 */

public class MidtransPayment {
    private final AnalyticHelper analyticHelper;
    TransactionRequest transactionRequest;
    Activity activity;
    SessionHelper sessionHelper;
    Random random;
    String transID, transName, orderID, orders, packages;
    int price;

    public MidtransPayment(Activity activity, String transID, int price, String transName, String orderID, String orders, String packages) {
        this.activity = activity;
        this.transID = transID;
        this.price = price;
        this.transName = transName;
        this.orderID = orderID;
        this.orders = orders;
        this.packages = packages;

        sessionHelper = new SessionHelper();
        analyticHelper = new AnalyticHelper(activity);
        random = new Random();
        initMidtransSDK();
        preparePayment();
    }

    private void initMidtransSDK() {
        SdkUIFlowBuilder.init(activity.getApplicationContext(), BuildConfig.CLIENT_KEY, BuildConfig.BASE_URL +"payment/", new TransactionFinishedCallback() {
            @Override
            public void onTransactionFinished(TransactionResult result) {
                if (result.getResponse() != null){
                    System.out.println("Response payment: "+result.getResponse().getPaymentType());
                    System.out.println("Response payment uid: "+sessionHelper.getPreferences(activity, "user_id"));
                    HashMap<String, String> param = new HashMap<>();
                    param.put("uid", sessionHelper.getPreferences(activity, "user_id"));
                    param.put("token_access", sessionHelper.getPreferences(activity, "token_access"));
                    param.put("dev_id", "2");
                    param.put("client_id", "xBc3w11");
                    param.put("package", packages);
                    param.put("trans_id", result.getResponse().getTransactionId());
                    param.put("order_id", orderID);
                    param.put("order", "["+orders+"]");
                    param.put("payment_type", result.getResponse().getPaymentType());
                    param.put("payment_status", result.getResponse().getStatusCode());
                    param.put("payment_status_msg", result.getResponse().getStatusMessage());
                    param.put("price", String.valueOf(price));
                    param.put("trans_time", result.getResponse().getTransactionTime());



                    new VolleyHelper().post(ApiHelper.PAYMENT, param, new VolleyHelper.HttpListener<String>() {
                        @Override
                        public void onReceive(boolean status, String message, String response) {
                            System.out.println("Response payment: "+response);
                            if (status){
                                try {
                                    JSONObject object = new JSONObject(response);
                                    if (object.getBoolean("status")&&packages.equals("1")){
                                        analyticHelper.premiumSubscribeClick(transID, "standart",orderID,
                                                transName, "" , String.valueOf(price), "midtrans");
                                        activity.finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                new ResultPayment(activity).execute(response);
                                //new ProfileInfo(activity).execute(sessionHelper.getPreferences(activity, "user_id"));
                            }

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
        customer.setPhone(sessionHelper.getPreferences(activity.getApplicationContext(), "phone"));
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
        userDetail.setPhoneNumber("081234567890");
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

    public void startGopayPayment(){
        MidtransSDK.getInstance().startPaymentUiFlow(activity, PaymentMethod.GO_PAY);
    }

    public void startTransferBankPayment(){
        MidtransSDK.getInstance().startPaymentUiFlow(activity, PaymentMethod.BANK_TRANSFER_PERMATA);
    }
}
