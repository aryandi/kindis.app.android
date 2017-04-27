package co.digdaya.kindis.view.activity.Detail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.util.SpacingItem.MarginItemHorizontal;
import me.relex.circleindicator.CircleIndicator;
import co.digdaya.kindis.PlayerService;
import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.PlayerActionHelper;
import co.digdaya.kindis.helper.PlayerSessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerActivity;
import co.digdaya.kindis.view.dialog.DialogSingleMenu;
import co.digdaya.kindis.view.activity.Search;
import co.digdaya.kindis.view.adapter.item.AdapterAlbum;
import co.digdaya.kindis.view.adapter.AdapterDetailArtist;
import co.digdaya.kindis.view.adapter.item.AdapterSong;
import co.digdaya.kindis.view.fragment.detail.DetailAbout;
import co.digdaya.kindis.view.fragment.detail.DetailMain;

public class DetailArtist extends BottomPlayerActivity implements View.OnClickListener {
    ViewPager imageSlider;
    Toolbar toolbar;
    CircleIndicator indicator;
    LinearLayout contFloatingButton;
    AppBarLayout appBarLayout;
    TextView titleToolbar;
    RelativeLayout contLabel;
    Button btnPlayAll;

    AdapterDetailArtist adapter;
    AdapterAlbum adapterAlbum;
    AdapterSong adapterSong;

    RecyclerView listViewAlbum;
    RecyclerView listViewSong;

    ArrayList<HashMap<String, String>> listAlbum = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    ArrayList<String> songPlaylist = new ArrayList<>();

    Dialog dialogPlaylis;
    String json;
    PlayerSessionHelper playerSessionHelper;
    public DetailArtist(){
        layout = R.layout.activity_detail_artist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageSlider = (ViewPager) findViewById(R.id.viewpager_slider);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contFloatingButton = (LinearLayout) findViewById(R.id.cont_floating_button);
        appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);
        titleToolbar = (TextView) findViewById(R.id.title_toolbar);
        contLabel = (RelativeLayout) findViewById(R.id.cont_label);
        btnPlayAll = (Button) findViewById(R.id.btn_play_all);
        listViewAlbum = (RecyclerView) findViewById(R.id.list_album);
        listViewSong = (RecyclerView) findViewById(R.id.list_songs);

        playerSessionHelper = new PlayerSessionHelper();

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
        listViewAlbum.addItemDecoration(new MarginItemHorizontal(this));
        listViewSong.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

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

        btnPlayAll.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search){
            Intent intent = new Intent(this, Search.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDetail(){
        new VolleyHelper().get(ApiHelper.ITEM_ARTIST + getIntent().getStringExtra("uid"), new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    System.out.println("getDetailArtist: "+response);
                    System.out.println("getDetailArtistID: "+getIntent().getStringExtra("uid"));
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject result = object.getJSONObject("result");
                        JSONObject summary = result.getJSONObject("summary");

                        String subtitles = result.getInt("total_album")+" album | "+result.getInt("total_single")+" Single";

                        adapter.addFragment(new DetailMain(summary.getString("image"), summary.getString("name"), subtitles), "Recently Added");
                        adapter.addFragment(new DetailAbout(summary.getString("about")), "Genres");
                        imageSlider.setAdapter(adapter);

                        titleToolbar.setText(summary.getString("name"));
                        playerSessionHelper.setPreferences(getApplicationContext(), "subtitle_player", summary.getString("name"));
                        indicator.setViewPager(imageSlider);
                        adapter.registerDataSetObserver(indicator.getDataSetObserver());

                        JSONArray album = result.getJSONArray("album");
                        json = album.toString();
                        if (album.length()>=1){
                            for (int i=0; i<album.length(); i++){
                                JSONObject data = album.getJSONObject(i);
                                JSONObject smry = data.getJSONObject("summary");
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("uid", smry.getString("uid"));
                                map.put("title", smry.getString("title"));
                                map.put("year", smry.getString("year"));
                                map.put("image", smry.getString("image"));
                                listAlbum.add(map);

                                JSONArray single = data.getJSONArray("single");
                                if (single.length()>0){
                                    for (int j=0; j<single.length(); j++){
                                        JSONObject song = single.getJSONObject(j);
                                        HashMap<String, String> maps = new HashMap<String, String>();
                                        maps.put("uid", song.optString("uid"));
                                        maps.put("title", song.optString("title"));
                                        maps.put("subtitle", smry.getString("title"));
                                        map.put("is_premium", data.optString("is_premium"));
                                        map.put("share_link", data.optString("share_link"));
                                        listSong.add(maps);
                                        songPlaylist.add(song.optString("uid"));
                                    }
                                }
                            }

                            adapterAlbum = new AdapterAlbum(getApplicationContext(), listAlbum);
                            listViewAlbum.setAdapter(adapterAlbum);
                            listViewAlbum.setNestedScrollingEnabled(false);

                            adapterSong = new AdapterSong(DetailArtist.this, listSong, "", null);
                            listViewSong.setAdapter(adapterSong);
                            listViewSong.setNestedScrollingEnabled(true);

                            onClickMenuSong();
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

    private void onClickMenuSong(){
        adapterSong.setOnClickMenuListener(new AdapterSong.OnClickMenuListener() {
            @Override
            public void onClick(String uid, ImageButton imageButton, String artistID, String shareLink) {
                new DialogSingleMenu(DetailArtist.this, dialogPlaylis, uid, artistID, shareLink, true).showDialog();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(getApplicationContext(), "Loading . . . ", Toast.LENGTH_LONG).show();
        Log.d("kontoljson", json);
        new PlayerSessionHelper().setPreferences(getApplicationContext(), "index", String.valueOf(songPlaylist.size()));
        new PlayerSessionHelper().setPreferences(getApplicationContext(), "json", json);
        new PlayerSessionHelper().setPreferences(getApplicationContext(), "type", "artist");
        Intent intent = new Intent(DetailArtist.this, PlayerService.class);
        intent.setAction(PlayerActionHelper.PLAY_MULTYSOURCE);
        intent.putExtra("single_id", songPlaylist.get(0));
        intent.putExtra("list_uid", songPlaylist);
        startService(intent);
    }
}