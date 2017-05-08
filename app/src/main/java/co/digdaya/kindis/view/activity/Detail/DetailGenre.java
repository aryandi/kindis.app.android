package co.digdaya.kindis.view.activity.Detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
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

import java.util.ArrayList;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.PlayerSessionHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.model.DataAlbum;
import co.digdaya.kindis.model.DataSingle;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerActivity;
import co.digdaya.kindis.util.SpacingItem.MarginItemHorizontal;
import co.digdaya.kindis.view.adapter.item.AdapterAlbumNew;
import co.digdaya.kindis.view.adapter.item.AdapterSongHorizontal;

/**
 * Created by DELL on 4/10/2017.
 */

public class DetailGenre extends BottomPlayerActivity {
    AppBarLayout appBarLayout;
    LinearLayout contFloatingButton;
    RelativeLayout contLabel;
    Toolbar toolbar;
    TextView titleToolbar,titleDetail,description;
    ImageView backDrop;
    Button btnPlayAll;
    LinearLayout contArtist, contPlaylist, contSingle, contAlbum;
    RecyclerView recyclerViewArtist, recyclerViewPlaylist, recyclerViewSingle, recyclerViewAlbum;

    ArrayList<String> songPlaylist = new ArrayList<>();
    String json;

    SessionHelper sessionHelper;
    PlayerSessionHelper playerSessionHelper;
    Gson gson;

    AdapterAlbumNew adapterAlbum;
    AdapterSongHorizontal adapterSongHorizontal;


    public DetailGenre() {
        layout = R.layout.activity_detail_genre;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        initView();
        init();
        getDetailGenre();
    }

    private void initView() {
        appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);
        contFloatingButton = (LinearLayout) findViewById(R.id.cont_floating_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contLabel = (RelativeLayout) findViewById(R.id.cont_label);
        titleToolbar = (TextView) toolbar.findViewById(R.id.title_toolbar);
        titleDetail = (TextView) findViewById(R.id.title_detail);
        description = (TextView) findViewById(R.id.description);
        backDrop = (ImageView) findViewById(R.id.backdrop);
        btnPlayAll = (Button) findViewById(R.id.btn_play_all);

        contAlbum = (LinearLayout) findViewById(R.id.cont_album);
        contArtist = (LinearLayout) findViewById(R.id.cont_artist);
        contPlaylist = (LinearLayout) findViewById(R.id.cont_playlist);
        contSingle = (LinearLayout) findViewById(R.id.cont_single);

        recyclerViewAlbum = (RecyclerView) findViewById(R.id.list_album);
        recyclerViewArtist = (RecyclerView) findViewById(R.id.list_artist);
        recyclerViewPlaylist = (RecyclerView) findViewById(R.id.list_playlist);
        recyclerViewSingle = (RecyclerView) findViewById(R.id.list_songs);

        sessionHelper = new SessionHelper();
        playerSessionHelper = new PlayerSessionHelper();
        gson = new Gson();

        btnPlayAll.setVisibility(View.GONE);
        /*btnPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Loading . . . ", Toast.LENGTH_LONG).show();
                new PlayerSessionHelper().setPreferences(getApplicationContext(), "index", String.valueOf(songPlaylist.size()));
                new PlayerSessionHelper().setPreferences(getApplicationContext(), "json", json);
                new PlayerSessionHelper().setPreferences(getApplicationContext(), "type", getIntent().getStringExtra("type"));
                Intent intent = new Intent(DetailGenre.this, PlayerService.class);
                intent.setAction(PlayerActionHelper.PLAY_MULTYSOURCE);
                intent.putExtra("single_id", songPlaylist.get(0));
                intent.putExtra("list_uid", songPlaylist);
                startService(intent);
            }
        });*/
    }

    private void init(){
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

        titleToolbar.setText(getIntent().getStringExtra("title"));
        playerSessionHelper.setPreferences(getApplicationContext(), "subtitle_player", getIntent().getStringExtra("title"));
        titleDetail.setText(getIntent().getStringExtra("title"));
        description.setText(getIntent().getStringExtra("desc"));
        backDrop.setColorFilter(Color.parseColor("#70000000"));
        Glide.with(getApplicationContext())
                .load(ApiHelper.BASE_URL_IMAGE+getIntent().getStringExtra("banner_image"))
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(backDrop);

        recyclerViewAlbum.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAlbum.addItemDecoration(new MarginItemHorizontal(DetailGenre.this));

        recyclerViewSingle.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSingle.addItemDecoration(new MarginItemHorizontal(DetailGenre.this));
    }

    private void getDetailGenre() {
        new VolleyHelper().get(ApiHelper.DETAIL_GENRE + getIntent().getStringExtra("uid") + "&page=&uid=" + sessionHelper.getPreferences(getApplicationContext(), "user_id"), new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    System.out.println("getDetailGenre: "+response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");
                            setContent(result);

                            DataAlbum dataAlbum = gson.fromJson(result.toString(), DataAlbum.class);
                            adapterAlbum = new AdapterAlbumNew(DetailGenre.this, dataAlbum, 1);
                            recyclerViewAlbum.setAdapter(adapterAlbum);
                            recyclerViewAlbum.setNestedScrollingEnabled(false);

                            DataSingle dataSingle = gson.fromJson(result.toString(), DataSingle.class);
                            adapterSongHorizontal = new AdapterSongHorizontal(DetailGenre.this, dataSingle, 1);
                            recyclerViewSingle.setAdapter(adapterSongHorizontal);
                            recyclerViewSingle.setNestedScrollingEnabled(false);

                            JSONArray singles = result.getJSONArray("single");
                            json = singles.toString();
                            for (int i=0; i<singles.length(); i++){
                                JSONObject data = singles.getJSONObject(i);
                                songPlaylist.add(data.optString("uid"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setContent(JSONObject content){
        if (content.has("albums")){
            contAlbum.setVisibility(View.VISIBLE);
        }
        if (content.has("single")){
            contSingle.setVisibility(View.VISIBLE);
        }
        if (content.has("playlist")){
            contPlaylist.setVisibility(View.VISIBLE);
        }
        if (content.has("artist")){
            contArtist.setVisibility(View.VISIBLE);
        }
    }
}
