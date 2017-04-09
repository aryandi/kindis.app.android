package co.digdaya.kindis.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.digdaya.kindis.R;

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
    TextView btnCancel, btnPremium;

    public DialogGift(Dialog dialogGift, Activity activity) {
        this.dialogGift = dialogGift;
        this.activity = activity;

        li = LayoutInflater.from(activity);
        dialogView = li.inflate(R.layout.dialog_gift, null);

        alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(dialogView);

        btnCancel = (TextView) dialogView.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(this);
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
    }
}
