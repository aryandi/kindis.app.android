package co.digdaya.kindis.live.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.TextViewHelper;
import co.digdaya.kindis.live.view.activity.Premium;

public class DialogAlertPremium implements View.OnClickListener{
    Dialog premium;
    Activity activity;
    AlertDialog.Builder alertDialog;
    LayoutInflater li;
    View dialogView;
    TextView btnCancel, btnPremium, textMessage;

    public DialogAlertPremium(Activity activity, Dialog premium){
        this.activity = activity;
        this.premium = premium;

        li = LayoutInflater.from(activity);
        dialogView = li.inflate(R.layout.dialog_alert_premium, null);

        alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(dialogView);

        btnCancel = (TextView) dialogView.findViewById(R.id.btn_cancel);
        btnPremium = (TextView) dialogView.findViewById(R.id.btn_premium);
        textMessage = (TextView) dialogView.findViewById(R.id.text_message);

        TextViewHelper.setSpanColor(textMessage, "Kindis Premium", ContextCompat
                .getColor(activity, R.color.yellow));

        btnCancel.setOnClickListener(this);
        btnPremium.setOnClickListener(this);
    }

    public DialogAlertPremium(Activity activity, Dialog premium, String message){
        this.activity = activity;
        this.premium = premium;

        li = LayoutInflater.from(activity);
        dialogView = li.inflate(R.layout.dialog_alert_premium, null);

        alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(dialogView);

        textMessage = (TextView) dialogView.findViewById(R.id.text_message);
        btnCancel = (TextView) dialogView.findViewById(R.id.btn_cancel);
        btnPremium = (TextView) dialogView.findViewById(R.id.btn_premium);

        textMessage.setText(message);
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
                Intent intent = new Intent(activity, Premium.class);
                activity.startActivity(intent);
                break;
            default:
                premium.dismiss();
                break;
        }
    }
}