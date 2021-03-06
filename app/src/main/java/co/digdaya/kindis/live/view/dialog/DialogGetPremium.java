package co.digdaya.kindis.live.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;

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

    boolean isGetPrice = false;
    int price;
    String googleCode;

    public DialogGetPremium(Activity activity, Dialog premium){
        this.activity = activity;
        this.premium = premium;

        li = LayoutInflater.from(activity);
        dialogView = li.inflate(R.layout.dialog_premium, null);

        alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(dialogView);

        sessionHelper = new SessionHelper();
        random = new Random();

        btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnPremium = dialogView.findViewById(R.id.btn_confirm);

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
            case R.id.btn_confirm:
                premium.dismiss();
                String transID = "PRE"+sessionHelper.getPreferences(activity, "user_id")+random.nextInt(89)+10;
                dialogPayment = new DialogPayment(dialogPay, activity, transID, price, "Akun Premium", googleCode, "", "", "1", "");
                dialogPayment.showDialog();
                break;
            default:
                premium.dismiss();
                break;
        }
    }

    private class GetPrice extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = sessionHelper.getPreferences(activity, "user_id") +
                    "&token_access="+sessionHelper.getPreferences(activity, "token_access") +
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