package sangmaneproject.kindis.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import me.relex.circleindicator.CircleIndicator;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.view.adapter.AdapterDetailArtist;
import sangmaneproject.kindis.view.fragment.detail.DetailAbout;
import sangmaneproject.kindis.view.fragment.detail.DetailMain;

public class DetailArtist extends AppCompatActivity {
    ViewPager imageSlider;
    Toolbar toolbar;
    AdapterDetailArtist adapter;
    CircleIndicator indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_artist);

        imageSlider = (ViewPager) findViewById(R.id.viewpager_slider);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter = new AdapterDetailArtist(getSupportFragmentManager());

        getDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    private void getDetail(){
        if (getIntent().getStringExtra("type").equals("artist")){
            new VolleyHelper().get(ApiHelper.ITEM_ARTIST + getIntent().getStringExtra("uid"), new VolleyHelper.HttpListener<String>() {
                @Override
                public void onReceive(boolean status, String message, String response) {
                    if (status){
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject result = object.getJSONObject("result");
                            JSONObject summary = result.getJSONObject("summary");

                            adapter.addFragment(new DetailMain(summary.getString("image"), summary.getString("name"), summary.getString("description")), "Recently Added");
                            adapter.addFragment(new DetailAbout(), "Genres");
                            imageSlider.setAdapter(adapter);

                            indicator.setViewPager(imageSlider);
                            adapter.registerDataSetObserver(indicator.getDataSetObserver());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
