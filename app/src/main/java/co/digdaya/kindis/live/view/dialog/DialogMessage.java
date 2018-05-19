package co.digdaya.kindis.live.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.digdaya.kindis.live.R;

public class DialogMessage {
    Dialog dialog;
    Activity activity;
    private AlertDialog.Builder alertDialog;
    LayoutInflater li;
    private View dialogView;
    private TextView btnCancel, btnPremium, textMessage;

    public DialogMessage(Activity activity, Dialog dialog, String message,
                         View.OnClickListener positiveButtonClickListener,
                         View.OnClickListener negativeButtonClickListener){
        this.activity = activity;
        this.dialog = dialog;

        li = LayoutInflater.from(activity);
        dialogView = li.inflate(R.layout.dialog_message, null);

        alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(dialogView);

        textMessage = dialogView.findViewById(R.id.text_message);
        btnCancel = dialogView.findViewById(R.id.btn_cancel);
        btnPremium = dialogView.findViewById(R.id.btn_confirm);

        textMessage.setText(message);
        btnCancel.setOnClickListener(negativeButtonClickListener);
        btnPremium.setOnClickListener(positiveButtonClickListener);
    }

    public void showDialog(){
        if (dialogView.getParent()!=null){
            ((ViewGroup)dialogView.getParent()).removeView(dialogView);
        }
        dialog = alertDialog.create();
        if(!activity.isFinishing()){
            dialog.show();
        }
    }

    public void setButtonPositiveHighLight(){
        btnPremium.setBackground(ContextCompat.getDrawable(activity,
                R.drawable.button_rounded_yellow_10));
    }


    public void dismiss() {
        dialog.dismiss();
    }
}
