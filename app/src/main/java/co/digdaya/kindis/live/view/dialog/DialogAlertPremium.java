package co.digdaya.kindis.live.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.view.activity.Premium;

public class DialogAlertPremium implements View.OnClickListener{
    Dialog premium;
    Activity activity;
    AlertDialog.Builder alertDialog;
    LayoutInflater li;
    View dialogView;
    TextView btnCancel, btnPremium;

    public DialogAlertPremium(Activity activity, Dialog premium){
        this.activity = activity;
        this.premium = premium;

        li = LayoutInflater.from(activity);
        dialogView = li.inflate(R.layout.dialog_alert_premium, null);

        alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(dialogView);

        btnCancel = (TextView) dialogView.findViewById(R.id.btn_cancel);
        btnPremium = (TextView) dialogView.findViewById(R.id.btn_premium);

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