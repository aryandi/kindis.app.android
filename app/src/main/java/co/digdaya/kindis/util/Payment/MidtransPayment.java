package co.digdaya.kindis.util.Payment;

import android.app.Activity;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import java.util.ArrayList;
import java.util.Random;

import co.digdaya.kindis.BuildConfig;
import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.SessionHelper;

/**
 * Created by DELL on 4/9/2017.
 */

public class MidtransPayment {
    TransactionRequest transactionRequest;
    Activity activity;
    SessionHelper sessionHelper;
    Random random;

    public MidtransPayment(Activity activity) {
        this.activity = activity;

        sessionHelper = new SessionHelper();
        random = new Random();
        initMidtransSDK();
        preparePayment();
    }

    private void initMidtransSDK() {
        SdkUIFlowBuilder.init(activity.getApplicationContext(), BuildConfig.CLIENT_KEY, BuildConfig.BASE_URL, new TransactionFinishedCallback() {
            @Override
            public void onTransactionFinished(TransactionResult result) {
            }
        })
                .enableLog(true)
                .buildSDK();
    }

    private void preparePayment(){
        String transID = "PRE"+sessionHelper.getPreferences(activity, "user_id")+random.nextInt(50) + 1;
        transactionRequest = new TransactionRequest(transID, 10000);

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

        /*ItemDetails itemDetails = new ItemDetails("1", 10000, 1, "Trekking Shoes");

        // Add item details into item detail list.
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        transactionRequest.setItemDetails(itemDetailsArrayList);*/

        /*BillInfoModel billInfoModel = new BillInfoModel("004", "Premium User");
        transactionRequest.setBillInfoModel(billInfoModel);*/

        CreditCard creditCardOptions = new CreditCard();
        creditCardOptions.setSaveCard(false);
        creditCardOptions.setSecure(false);
        creditCardOptions.setBank(BankType.MANDIRI);

        transactionRequest.setCreditCard(creditCardOptions);
        transactionRequest.setCardPaymentInfo(activity.getString(R.string.card_click_type_none), true);

        MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
    }

    public void startPayment(){
        MidtransSDK.getInstance().startPaymentUiFlow(activity);
    }
}
