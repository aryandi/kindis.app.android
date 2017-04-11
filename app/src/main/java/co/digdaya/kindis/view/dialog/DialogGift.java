package co.digdaya.kindis.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;

/**
 * Created by DELL on 4/9/2017.
 */

public class DialogGift implements View.OnClickListener {
    Dialog dialog;
    Dialog dialogGift;
    Activity activity;
    AlertDialog.Builder alertDialog;
    LayoutInflater li;
    View dialogView;
    TextView btnCancel, btnSend;
    EditText inputCode;
    SessionHelper sessionHelper;;

    public DialogGift(Dialog dialogGift, Activity activity) {
        this.dialogGift = dialogGift;
        this.activity = activity;

        sessionHelper = new SessionHelper();

        li = LayoutInflater.from(activity);
        dialogView = li.inflate(R.layout.dialog_gift, null);

        alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(dialogView);

        btnCancel = (TextView) dialogView.findViewById(R.id.btn_cancel);
        btnSend = (TextView) dialogView.findViewById(R.id.btn_send);
        inputCode = (EditText) dialogView.findViewById(R.id.input_code);

        btnCancel.setOnClickListener(this);
        btnSend.setOnClickListener(this);
    }

    public void showDialog(){
        if (dialogView.getParent()!=null){
            ((ViewGroup)dialogView.getParent()).removeView(dialogView);
        }
        dialog = alertDialog.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        dialog.dismiss();
        if (v.getId() == R.id.btn_send){
            checkGift();
        }
    }

    private void checkGift(){
        if (inputCode.getText().length()<1){
            Toast.makeText(activity, "Please Enter Code", Toast.LENGTH_SHORT).show();
        }else {
            HashMap<String, String> param = new HashMap<>();
            param.put("uid", sessionHelper.getPreferences(activity, "user_id"));
            param.put("token_access", sessionHelper.getPreferences(activity, "token_access"));
            param.put("gift_code", inputCode.getText().toString());
            param.put("dev_id", "2");
            new VolleyHelper().post(ApiHelper.GIFT, param, new VolleyHelper.HttpListener<String>() {
                @Override
                public void onReceive(boolean status, String message, String response) {
                    System.out.println("GiftResponse: "+response);
                }
            });
        }
    }
}
