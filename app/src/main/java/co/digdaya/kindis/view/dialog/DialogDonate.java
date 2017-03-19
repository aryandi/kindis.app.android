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
 * Created by vincenttp on 3/19/2017.
 */

public class DialogDonate implements View.OnClickListener {
    Activity activity;
    Dialog dialog;
    LayoutInflater li;
    View dialogView;
    AlertDialog.Builder alertDialog;
    TextView btnCancel, btnPremium;

    public DialogDonate(Activity activity, Dialog dialog) {
        this.activity = activity;
        this.dialog = dialog;

        li = LayoutInflater.from(activity);
        dialogView = li.inflate(R.layout.dialog_donate, null);

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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
    }
}
