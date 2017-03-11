package sangmaneproject.kindis.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import sangmaneproject.kindis.R;

/**
 * Created by DELL on 3/11/2017.
 */

public class GetPremium {
    Dialog premium;
    Activity activity;
    AlertDialog.Builder alertDialog;

    public GetPremium(Activity activity, Dialog premium){
        this.activity = activity;
        this.premium = premium;

        LayoutInflater li = LayoutInflater.from(activity);
        View dialogView = li.inflate(R.layout.dialog_premium, null);

        alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(dialogView);
    }

    public void showDialog(){
        premium = alertDialog.create();
        premium.show();
    }

    public void dismisDialog(){
        premium.dismiss();
    }
}

