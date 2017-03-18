package co.digdaya.kindis.view.activity.Detail;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.digdaya.kindis.R;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerActivity;

public class DetailInfaq extends BottomPlayerActivity {
    AppBarLayout appBarLayout;
    LinearLayout contFloatingButton;
    RelativeLayout contLabel;
    Toolbar toolbar;
    TextView titleToolbar;
    TextView titleDetail;
    TextView description;

    public DetailInfaq(){
        layout = R.layout.activity_detail_infaq;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);
        contFloatingButton = (LinearLayout) findViewById(R.id.cont_floating_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contLabel = (RelativeLayout) findViewById(R.id.cont_label);
        titleToolbar = (TextView) toolbar.findViewById(R.id.title_toolbar);
        titleDetail = (TextView) findViewById(R.id.title_detail);
        description = (TextView) findViewById(R.id.description);

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
                    contFloatingButton.setVisibility(View.INVISIBLE);
                    titleToolbar.setVisibility(View.VISIBLE);
                    isShow = true;
                } else if (isShow) {
                    contLabel.setVisibility(View.VISIBLE);
                    contFloatingButton.setVisibility(View.VISIBLE);
                    titleToolbar.setVisibility(View.INVISIBLE);
                    isShow = false;
                }
            }
        });

        initDetailInfaq();
    }

    private void initDetailInfaq(){
        titleDetail.setText(getIntent().getStringExtra("title"));
        titleToolbar.setText(getIntent().getStringExtra("title"));
    }
}
