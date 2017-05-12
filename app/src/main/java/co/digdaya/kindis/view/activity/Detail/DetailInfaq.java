package co.digdaya.kindis.view.activity.Detail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.model.DetailInfaqModel;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerActivity;
import co.digdaya.kindis.util.GoogleBilling.IabHelper;
import co.digdaya.kindis.util.GoogleBilling.IabResult;
import co.digdaya.kindis.util.GoogleBilling.Inventory;
import co.digdaya.kindis.util.GoogleBilling.Purchase;
import co.digdaya.kindis.view.dialog.DialogDonate;

public class DetailInfaq extends BottomPlayerActivity{
    AppBarLayout appBarLayout;
    LinearLayout contFloatingButton;
    RelativeLayout contLabel;
    Toolbar toolbar;
    TextView titleToolbar, titleDetail, description, detailInfaq;
    ImageView backdrop;
    RecyclerView gallery;
    DialogDonate dialogDonate;
    Dialog dialog;
    Gson gson;
    DetailInfaqModel detailInfaqModel;

    public DetailInfaq(){
        layout = R.layout.activity_detail_infaq;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);
        //contFloatingButton = (LinearLayout) findViewById(R.id.cont_floating_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contLabel = (RelativeLayout) findViewById(R.id.cont_label);
        titleToolbar = (TextView) toolbar.findViewById(R.id.title_toolbar);
        titleDetail = (TextView) findViewById(R.id.title_detail);
        description = (TextView) findViewById(R.id.description);
        detailInfaq = (TextView) findViewById(R.id.detail_infaq);
        backdrop = (ImageView) findViewById(R.id.backdrop);
        gallery = (RecyclerView) findViewById(R.id.gallery_infaq);

        dialogDonate = new DialogDonate(this, dialog);
        gson = new Gson();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar.setVisibility(View.INVISIBLE);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;
            boolean isShow = false;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    contLabel.setVisibility(View.INVISIBLE);
                    //contFloatingButton.setVisibility(View.INVISIBLE);
                    titleToolbar.setVisibility(View.VISIBLE);
                    isShow = true;
                } else if (isShow) {
                    contLabel.setVisibility(View.VISIBLE);
                    //contFloatingButton.setVisibility(View.VISIBLE);
                    titleToolbar.setVisibility(View.INVISIBLE);
                    isShow = false;
                }
            }
        });

        getDetailInfaq();
    }



    private void getDetailInfaq(){
        new VolleyHelper().get(ApiHelper.ITEM_INFAQ + getIntent().getStringExtra("uid"), new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                /*try {
                    JSONObject object = new JSONObject(response);
                    JSONObject result = object.getJSONObject("result");
                    JSONArray main = result.getJSONArray("main");
                    JSONObject data = main.getJSONObject(0);
                    String desc = data.getString("description");
                    detailInfaq.setText(Html.fromHtml(desc));
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                detailInfaqModel = gson.fromJson(response, DetailInfaqModel.class);
                initDetailInfaq();
            }
        });
    }

    private void initDetailInfaq(){
        titleDetail.setText(getIntent().getStringExtra("title"));
        titleToolbar.setText(getIntent().getStringExtra("title"));
        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("image"))
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(backdrop);

        detailInfaq.setText(Html.fromHtml(detailInfaqModel.result.main.get(0).description));
    }
}
