package sangmaneproject.kindis.view.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.view.adapter.AdapterAlbum;
import sangmaneproject.kindis.view.adapter.AdapterDetailArtist;
import sangmaneproject.kindis.view.fragment.detail.DetailAbout;
import sangmaneproject.kindis.view.fragment.detail.DetailMain;

public class DetailArtist extends AppCompatActivity {
    ViewPager imageSlider;
    Toolbar toolbar;
    CircleIndicator indicator;
    LinearLayout contFloatingButton;
    AppBarLayout appBarLayout;
    TextView titleToolbar;
    RelativeLayout contLabel;

    AdapterDetailArtist adapter;
    AdapterAlbum adapterAlbum;

    RecyclerView listViewAlbum;

    ArrayList<HashMap<String, String>> listAlbum = new ArrayList<HashMap<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_artist);

        imageSlider = (ViewPager) findViewById(R.id.viewpager_slider);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contFloatingButton = (LinearLayout) findViewById(R.id.cont_floating_button);
        appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);
        titleToolbar = (TextView) findViewById(R.id.title_toolbar);
        contLabel = (RelativeLayout) findViewById(R.id.cont_label);
        listViewAlbum = (RecyclerView) findViewById(R.id.list_album);

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

        listViewAlbum.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

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

        adapter = new AdapterDetailArtist(getSupportFragmentManager());

        listViewAlbum.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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
                        Log.d("responseartist", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONObject result = object.getJSONObject("result");
                            JSONObject summary = result.getJSONObject("summary");

                            adapter.addFragment(new DetailMain(summary.getString("image"), summary.getString("name"), "No Description from API"), "Recently Added");
                            adapter.addFragment(new DetailAbout(), "Genres");
                            imageSlider.setAdapter(adapter);

                            titleToolbar.setText(summary.getString("name"));
                            indicator.setViewPager(imageSlider);
                            adapter.registerDataSetObserver(indicator.getDataSetObserver());

                            JSONArray album = result.getJSONArray("album");
                            if (album.length()>=1){
                                for (int i=0; i<album.length(); i++){
                                    JSONObject data = album.getJSONObject(i);
                                    JSONObject smry = data.getJSONObject("summary");
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("uid", smry.getString("uid"));
                                    map.put("title", smry.getString("title"));
                                    listAlbum.add(map);
                                }
                                adapterAlbum = new AdapterAlbum(getApplicationContext(), listAlbum);
                                listViewAlbum.setAdapter(adapterAlbum);
                                listViewAlbum.setNestedScrollingEnabled(true);
                            }

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
