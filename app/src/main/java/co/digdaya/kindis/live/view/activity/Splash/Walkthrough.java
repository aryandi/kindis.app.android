package co.digdaya.kindis.live.view.activity.Splash;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import co.digdaya.kindis.live.helper.AnalyticHelper;
import me.relex.circleindicator.CircleIndicator;
import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.view.adapter.AdapterWalkthrough;

public class Walkthrough extends AppCompatActivity {
    ViewPager viewPager;
    AdapterWalkthrough adapterWalkthrough;
    private AnalyticHelper analyticHelper;
    private int page = 0;
    private String[] titles = new String[]{"Musiq", "Taklim", "Infaq"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_walkthrough);

        try {
            PackageInfo info = getPackageManager().getPackageInfo("sangmaneproject.kindis",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        analyticHelper = new AnalyticHelper(this);
        viewPager = (ViewPager) findViewById(R.id.vp_walkthrough);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        adapterWalkthrough = new AdapterWalkthrough(this, analyticHelper);

        viewPager.setAdapter(adapterWalkthrough);
        indicator.setViewPager(viewPager);
        adapterWalkthrough.registerDataSetObserver(indicator.getDataSetObserver());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (page < position){
                    Log.d("scroll", "left");
                    Log.d("title", titles[page]);
                    analyticHelper.journeySwipe(titles[page], "left");
                } else if (page > position){
                    Log.d("scroll", "right");
                    Log.d("title", titles[page]);
                    analyticHelper.journeySwipe(titles[page], "right");
                }
                page = position;
                Log.d("page selected", ""+ position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("changestate", ""+state);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onBackPressed() {
        analyticHelper.journeySkip(titles[page]);
        super.onBackPressed();
    }
}
