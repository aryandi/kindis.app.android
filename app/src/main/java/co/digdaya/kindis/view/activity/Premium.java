package co.digdaya.kindis.view.activity;

import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.view.dialog.DialogPayment;

public class Premium extends AppCompatActivity {
    TextView btnCancel, btnOk;
    SessionHelper sessionHelper;
    Random random;
    DialogPayment dialogPayment;
    Dialog dialogPay;
    boolean isGetPrice = false;
    int price;
    String googleCode;

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
                    String transID = "PRE"+sessionHelper.getPreferences(getApplicationContext(), "user_id")+random.nextInt(89)+10;
                    dialogPayment = new DialogPayment(dialogPay, Premium.this, transID, price, "Akun Premium", googleCode);
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
}
