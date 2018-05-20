package co.digdaya.kindis.live.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.custom.TextViewSemiBold;
import co.digdaya.kindis.live.helper.AnalyticHelper;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.TextViewHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.model.PriceListModel;
import co.digdaya.kindis.live.network.ApiCall;
import co.digdaya.kindis.live.network.ApiUtil;
import co.digdaya.kindis.live.util.BackgroundProses.ResultPayment;
import co.digdaya.kindis.live.view.dialog.DialogPayment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Premium extends AppCompatActivity {
    TextView btnCancel, btnOk;
    SessionHelper sessionHelper;
    Random random;
    DialogPayment dialogPayment;
    Dialog dialogPay;

    boolean isGetPrice = false;
    int price;
    String googleCode, transID, order;
    List<PriceListModel.Data> priceList = new ArrayList<>();

    ApiCall apiCall;
    @BindView(R.id.text_message)
    TextViewSemiBold textMessage;
    private AnalyticHelper analyticHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
        ButterKnife.bind(this);
        //new GetPrice().execute();

        btnCancel = findViewById(R.id.btn_cancel);
        btnOk = findViewById(R.id.btn_ok);
        sessionHelper = new SessionHelper();
        analyticHelper = new AnalyticHelper(this);
        random = new Random();
        apiCall = ApiUtil.callService();

        TextViewHelper.setSpanColor(textMessage, "Kindis Premium", ContextCompat
                .getColor(this, R.color.yellow));

        getPriceList();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyticHelper.premiumToogle("false");
                finish();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyticHelper.premiumToogle("true");
                /*if (isGetPrice){
                    transID = "PRE"+sessionHelper.getPreferences(getApplicationContext(), "user_id")+random.nextInt(89)+10;

                    sessionHelper.setPreferences(getApplicationContext(), "transID", transID);
                    sessionHelper.setPreferences(getApplicationContext(), "price", String.valueOf(price));
                    sessionHelper.setPreferences(getApplicationContext(), "order", String.valueOf(order));

                    dialogPayment = new DialogPayment(dialogPay, Premium.this, transID, price, "Akun Premium", googleCode, order, "", "1", "");
                    dialogPayment.showDialog();
                }*/
                //new DialogPriceList(Premium.this, priceList, order).show();
                startActivity(new Intent(Premium.this, PriceList.class));
            }
        });
    }

//    private class GetPrice extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            String url = sessionHelper.getPreferences(getApplicationContext(), "user_id") +
//                    "&token_access=" + sessionHelper.getPreferences(getApplicationContext(), "token_access") +
//                    "&dev_id=2" +
//                    "&client_id=xBc3w11";
//            new VolleyHelper().get(ApiHelper.PRICE + url, new VolleyHelper.HttpListener<String>() {
//                @Override
//                public void onReceive(boolean status, String message, String response) {
//                    System.out.println("GetPrice: " + response);
//                    if (status) {
//                        try {
//                            JSONObject object = new JSONObject(response);
//                            if (object.getBoolean("status")) {
//                                JSONObject result = object.getJSONObject("result");
//                                price = result.getInt("price");
//                                googleCode = result.getString("google_code");
//                                order = result.getString("order_id");
//                                isGetPrice = true;
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//            return null;
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            System.out.println("onActivityResult: " + responseCode);
            System.out.println("onActivityResult: " + purchaseData);
            System.out.println("onActivityResult: " + dataSignature);

            if (resultCode == RESULT_OK) {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("uid", sessionHelper.getPreferences(getApplicationContext(), "user_id"));
                param.put("token_access", sessionHelper.getPreferences(getApplicationContext(), "token_access"));
                param.put("dev_id", "2");
                param.put("client_id", "xBc3w11");
                param.put("package", "1");
                final String transID = sessionHelper.getPreferences(getApplicationContext(), "transID");
                param.put("trans_id", transID);
//                param.put("order_id", sessionHelper.getPreferences(getApplicationContext(), "transID"));
                param.put("order", "[]");
                String order_id = sessionHelper.getPreferences(getApplicationContext(), "order");
                param.put("order_id", order_id);
                param.put("payment_type", "google_play");
                param.put("payment_status", "200");
                param.put("payment_status_msg", "success");
                final String price = sessionHelper.getPreferences(getApplicationContext(), "price");
                param.put("price", price);
                param.put("trans_time", "");
                System.out.println("onActivityResult: " + param);

                String productName = "";
                for (PriceListModel.Data data1 : priceList) {
                    if (data1.price.equals(price)){
                        try {
                            productName = data1.name;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                }
                final String finalProductName = productName;
                new VolleyHelper().post(ApiHelper.PAYMENT, param, new VolleyHelper.HttpListener<String>() {
                    @Override
                    public void onReceive(boolean status, String message, String response) {
                        System.out.println("onActivityResult: " + response);
                        if (status) {
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.getBoolean("status")) {
                                    analyticHelper.premiumSubscribeClick(transID, "standart",order,
                                            finalProductName, "" ,price, "google_play");
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            new ResultPayment(Premium.this).execute(response);
                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getPriceList() {
        String url = ApiHelper.PRICE_LIST + sessionHelper.getPreferences(getApplicationContext(), "user_id") + "&dev_id=2&client_id=xBc3w11&token_access=" + sessionHelper.getPreferences(getApplicationContext(), "token_access");
        apiCall.priceList(url).enqueue(new Callback<PriceListModel>() {
            @Override
            public void onResponse(@NonNull Call<PriceListModel> call, @NonNull Response<PriceListModel> response) {
                if (response != null && response.body() != null && response.body().status) {
                    priceList = response.body().result.data;
                    order = response.body().result.order_id;
                }
            }

            @Override
            public void onFailure(@NonNull Call<PriceListModel> call, @NonNull Throwable t) {

            }
        });
    }
}
