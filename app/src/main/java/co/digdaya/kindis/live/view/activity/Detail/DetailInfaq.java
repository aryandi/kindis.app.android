package co.digdaya.kindis.live.view.activity.Detail;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.model.DetailInfaqModel;
import co.digdaya.kindis.live.util.BaseBottomPlayer.BottomPlayerActivity;
import co.digdaya.kindis.live.util.SpacingItem.MarginItemHorizontal;
import co.digdaya.kindis.live.view.adapter.item.AdapterGalleryInfaq;
import co.digdaya.kindis.live.view.dialog.DialogDonate;

public class DetailInfaq extends BottomPlayerActivity{
    AppBarLayout appBarLayout;
    LinearLayout contFloatingButton;
    RelativeLayout contLabel;
    Toolbar toolbar;
    TextView titleToolbar, titleDetail, description;
    WebView detailInfaq;
    ImageView backdrop;
    RecyclerView listGallery;
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
        detailInfaq = (WebView) findViewById(R.id.detail_infaq);
        backdrop = (ImageView) findViewById(R.id.backdrop);
        listGallery = (RecyclerView) findViewById(R.id.gallery_infaq);

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
                detailInfaqModel = gson.fromJson(response, DetailInfaqModel.class);
                initDetailInfaq();
            }
        });
    }

    private void initDetailInfaq(){
        detailInfaq.setBackgroundColor(Color.TRANSPARENT);
        detailInfaq.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        titleDetail.setText(getIntent().getStringExtra("title"));
        titleToolbar.setText(getIntent().getStringExtra("title"));
        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("image"))
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(backdrop);

        //String content = "<p style=\"color:#ffffff\">"+detailInfaqModel.result.main.get(0).description+"</p>";
        detailInfaq.loadDataWithBaseURL("", detailInfaqModel.result.main.get(0).description, "text/html", "UTF-8", "");
        System.out.println("detailInfaq: "+detailInfaqModel.result.main.get(0).description);
        /*new ParseHtml().parse(detailInfaqModel.result.main.get(0).description, new ParseHtml.ResultListener<String>() {
            @Override
            public void onResult(String html) {
                detailInfaq.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
                System.out.println("detailInfaq: "+html);
            }
        });*/
        imageGallery();
    }

    private void imageGallery(){
        listGallery.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        listGallery.setNestedScrollingEnabled(false);
        listGallery.addItemDecoration(new MarginItemHorizontal(this));
        AdapterGalleryInfaq adapterGalleryInfaq = new AdapterGalleryInfaq(getApplicationContext(), detailInfaqModel);
        listGallery.setAdapter(adapterGalleryInfaq);
    }
}
