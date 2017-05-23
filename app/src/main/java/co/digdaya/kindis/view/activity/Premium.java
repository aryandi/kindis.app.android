package co.digdaya.kindis.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.util.BackgroundProses.ResultPayment;
import co.digdaya.kindis.view.dialog.DialogPayment;

public class Premium extends AppCompatActivity {
    TextView btnCancel, btnOk;
    SessionHelper sessionHelper;
    Random random;
    DialogPayment dialogPayment;
    Dialog dialogPay;
    boolean isGetPrice = false;
    int price;
    String googleCode, transID, order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
        new GetPrice().execute();

        btnCancel = (TextView) findViewById(R.id.btn_cancel);
        btnOk = (TextView) findViewById(R.id.btn_ok);
        sessionHelper = new SessionHelper();
        random = new Random();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGetPrice){
                    transID = "PRE"+sessionHelper.getPreferences(getApplicationContext(), "user_id")+random.nextInt(89)+10;
                    dialogPayment = new DialogPayment(dialogPay, Premium.this, transID, price, "Akun Premium", googleCode, order, "", "1", "");
                    dialogPayment.showDialog();
                }
            }
        });
    }

    private class GetPrice extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            String url = sessionHelper.getPreferences(getApplicationContext(), "user_id") +
                    "&token_access="+sessionHelper.getPreferences(getApplicationContext(), "token_access") +
                    "&dev_id=2" +
                    "&client_id=xBc3w11";
            new VolleyHelper().get(ApiHelper.PRICE + url, new VolleyHelper.HttpListener<String>() {
                @Override
                public void onReceive(boolean status, String message, String response) {
                    System.out.println("GetPrice: "+response);
                    if (status){
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("status")){
                                JSONObject result = object.getJSONObject("result");
                                price = result.getInt("price");
                                googleCode = result.getString("google_code");
                                order = result.getString("order_id");
                                isGetPrice = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            System.out.println("onActivityResult: "+responseCode);
            System.out.println("onActivityResult: "+purchaseData);
            System.out.println("onActivityResult: "+dataSignature);

            if (resultCode == RESULT_OK) {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("uid", sessionHelper.getPreferences(getApplicationContext(), "user_id"));
                param.put("token_access", sessionHelper.getPreferences(getApplicationContext(), "token_access"));
                param.put("dev_id", "2");
                param.put("client_id", "xBc3w11");
                param.put("package", "1");
                param.put("trans_id", transID);
                param.put("order_id", transID);
                param.put("order", "[]");
                param.put("order_id", order);
                param.put("payment_type", "google_play");
                param.put("payment_status", "200");
                param.put("payment_status_msg", "success");
                param.put("price", String.valueOf(price));
                param.put("trans_time", "");
                System.out.println("onActivityResult: "+param);

                new VolleyHelper().post(ApiHelper.PAYMENT, param, new VolleyHelper.HttpListener<String>() {
                    @Override
                    public void onReceive(boolean status, String message, String response) {
                        System.out.println("onActivityResult: "+response);
                        if (status){
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.getBoolean("status")){
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            new ResultPayment(Premium.this).execute(response);
                        }
                    }
                });
            }else {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
