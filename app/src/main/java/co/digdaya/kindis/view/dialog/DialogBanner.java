package co.digdaya.kindis.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;

/**
 * Created by DELL on 4/11/2017.
 */

public class DialogBanner implements View.OnClickListener {
    Activity activity;
    Dialog dialog;
    LayoutInflater li;
    View dialogView;
    AlertDialog.Builder alertDialog;
    ImageButton btnClose;
    ImageView imageView;
    SessionHelper sessionHelper;
    String clickUrl;

    public DialogBanner(Activity activity, Dialog dialog) {
        this.activity = activity;
        this.dialog = dialog;

        sessionHelper = new SessionHelper();

        li = LayoutInflater.from(activity);
        dialogView = li.inflate(R.layout.dialog_ads, null);

        alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setView(dialogView);

        btnClose = (ImageButton) dialogView.findViewById(R.id.btn_close);
        imageView = (ImageView) dialogView.findViewById(R.id.image_ads);

        btnClose.setOnClickListener(this);
        imageView.setOnClickListener(this);
    }

    public void showDialog(){
        new VolleyHelper().get(ApiHelper.ADS_INTERTSTITIAL + sessionHelper.getPreferences(activity, "user_id") + "&token_access=" + sessionHelper.getPreferences(activity, "token_access"), new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                System.out.println("bannerAds: "+response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("status")){
                        JSONObject result = object.getJSONObject("result");
                        clickUrl = result.getString("click_url");
                        Glide.with(activity)
                                .load(result.getString("image"))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                                .into(imageView);

                        if (dialogView.getParent()!=null){
                            ((ViewGroup)dialogView.getParent()).removeView(dialogView);
                        }
                        dialog = alertDialog.create();
                        dialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        dialog.dismiss();
        switch (view.getId()){
            case R.id.image_ads:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickUrl));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                break;
        }
    }
}
