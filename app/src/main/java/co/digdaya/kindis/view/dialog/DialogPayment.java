package co.digdaya.kindis.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import co.digdaya.kindis.R;
import co.digdaya.kindis.util.Payment.GooglePayment;
import co.digdaya.kindis.util.Payment.MidtransPayment;

/**
 * Created by DELL on 4/8/2017.
 */

public class DialogPayment implements View.OnClickListener {
    Dialog dialogPayment;
    Activity activity;
    AlertDialog.Builder alertDialog;
    LayoutInflater li;
    View dialogView;
    TextView btnCancel;
    ImageButton btnPaymentGoogle, btnPaymentMidtrans;

    MidtransPayment midtransPayment;
    GooglePayment googlePayment;

    String transID, transName, googleCode, orders, packages;
    int price;

    public DialogPayment(Dialog dialogPayment, Activity activity, String transID, int price, String transName, String googleCode, String orderID, String orders, String packages) {
        this.dialogPayment = dialogPayment;
        this.activity = activity;
        this.transID = transID;
        this.price = price;
        this.transName = transName;
        this.googleCode = googleCode;
        this.orders = orders;
        this.packages = packages;

        li = LayoutInflater.from(activity);
        dialogView = li.inflate(R.layout.dialog_payment, null);

        midtransPayment = new MidtransPayment(activity, transID, price, transName, orderID, orders, packages);
        googlePayment = new GooglePayment(activity, googleCode);

        alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(dialogView);

        btnCancel = (TextView) dialogView.findViewById(R.id.btn_cancel);
        btnPaymentGoogle = (ImageButton) dialogView.findViewById(R.id.btn_payment_google);
        btnPaymentMidtrans = (ImageButton) dialogView.findViewById(R.id.btn_payment_midtrans);

        btnCancel.setOnClickListener(this);
        btnPaymentGoogle.setOnClickListener(this);
        btnPaymentMidtrans.setOnClickListener(this);
    }

    public void showDialog(){
        if (dialogView.getParent()!=null){
            ((ViewGroup)dialogView.getParent()).removeView(dialogView);
        }
        dialogPayment = alertDialog.create();
        dialogPayment.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_payment_midtrans:
                midtransPayment.startPayment();
                break;
            case R.id.btn_payment_google:
                googlePayment.buyClick();
                break;
        }
        dialogPayment.dismiss();
    }
}
