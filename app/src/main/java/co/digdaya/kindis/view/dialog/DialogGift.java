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

public class DialogGift {
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
    }

    public void showDialog(){
        if (dialogView.getParent()!=null){
            ((ViewGroup)dialogView.getParent()).removeView(dialogView);
        }
        dialog = alertDialog.create();
        dialog.show();
    }
}
