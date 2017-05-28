package co.digdaya.kindis.view.activity.Detail;

import android.content.Intent;
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
import co.digdaya.kindis.model.DataArtist;
import co.digdaya.kindis.model.DataSingle;
import co.digdaya.kindis.model.genre.GenreAlbumModel;
import co.digdaya.kindis.model.genre.GenreArtistModel;
import co.digdaya.kindis.model.genre.GenreSingleModel;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerActivity;
import co.digdaya.kindis.util.SpacingItem.MarginItemHorizontal;
import co.digdaya.kindis.view.adapter.genre.GenreAlbumAdapter;
import co.digdaya.kindis.view.adapter.genre.GenreArtistAdapter;
import co.digdaya.kindis.view.adapter.genre.GenreSingleAdapter;
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
    RelativeLayout contArtist, contPlaylist, contSingle, contAlbum;
    TextView btnMoreArtist, btnMorePlaylist, btnMoreSingle, btnMoreAlbum;

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

        contAlbum = (RelativeLayout) findViewById(R.id.cont_album);
        contArtist = (RelativeLayout) findViewById(R.id.cont_artist);
        contPlaylist = (RelativeLayout) findViewById(R.id.cont_playlist);
        contSingle = (RelativeLayout) findViewById(R.id.cont_single);

        recyclerViewAlbum = (RecyclerView) findViewById(R.id.list_album);
        recyclerViewArtist = (RecyclerView) findViewById(R.id.list_artist);
        recyclerViewPlaylist = (RecyclerView) findViewById(R.id.list_playlist);
        recyclerViewSingle = (RecyclerView) findViewById(R.id.list_songs);

        btnMoreArtist = (TextView) findViewById(R.id.btn_more_artist);
        btnMorePlaylist = (TextView) findViewById(R.id.btn_more_playlist);
        btnMoreAlbum = (TextView) findViewById(R.id.btn_more_album);
        btnMoreSingle = (TextView) findViewById(R.id.btn_more_single);

        sessionHelper = new SessionHelper();
        playerSessionHelper = new PlayerSessionHelper();
        gson = new Gson();

        btnPlayAll.setVisibility(View.GONE);
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
        backDrop.setColorFilter(Color.parseColor("#70000000"));
        /*titleDetail.setText(getIntent().getStringExtra("title"));
        description.setText(getIntent().getStringExtra("desc"));
        Glide.with(getApplicationContext())
                .load(ApiHelper.BASE_URL_IMAGE+getIntent().getStringExtra("banner_image"))
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(backDrop);*/

        recyclerViewArtist.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewArtist.addItemDecoration(new MarginItemHorizontal(DetailGenre.this));
        recyclerViewArtist.setNestedScrollingEnabled(false);

        recyclerViewAlbum.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAlbum.addItemDecoration(new MarginItemHorizontal(DetailGenre.this));
        recyclerViewAlbum.setNestedScrollingEnabled(false);

        recyclerViewSingle.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSingle.addItemDecoration(new MarginItemHorizontal(DetailGenre.this));
        recyclerViewSingle.setNestedScrollingEnabled(false);

        recyclerViewPlaylist.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPlaylist.addItemDecoration(new MarginItemHorizontal(DetailGenre.this));
        recyclerViewPlaylist.setNestedScrollingEnabled(false);
    }

    private void getDetailGenre() {
        System.out.println("getDetailGenre: "+getIntent().getStringExtra("uid"));
        System.out.println("getDetailGenre: "+sessionHelper.getPreferences(getApplicationContext(), "user_id"));
        new VolleyHelper().get(ApiHelper.CONTENT_GENRE + getIntent().getStringExtra("uid") + "&uid=" + sessionHelper.getPreferences(getApplicationContext(), "user_id"), new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    System.out.println("getDetailGenre: "+response);
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject result = object.getJSONObject("result");
                        JSONObject summary = result.getJSONObject("summary");
                        titleDetail.setText(summary.getString("title"));
                        description.setText(summary.getString("description"));
                        String image;
                        if (summary.isNull("banner_image")){
                            image = summary.getString("image");
                        }else {
                            image = summary.getString("banner_image");
                        }
                        Glide.with(getApplicationContext())
                                .load(ApiHelper.BASE_URL_IMAGE+image)
                                .placeholder(R.drawable.ic_default_img)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .centerCrop()
                                .into(backDrop);

                        JSONArray content = result.getJSONArray("content");
                        for (int i=0; i<content.length(); i++){
                            JSONObject data = content.getJSONObject(i);
                            switch (data.getInt("type_content_id")){
                                case 1:
                                    contArtist.setVisibility(View.VISIBLE);
                                    final GenreArtistModel dataArtist = gson.fromJson(data.toString(), GenreArtistModel.class);
                                    GenreArtistAdapter genreArtistAdapter = new GenreArtistAdapter(DetailGenre.this, dataArtist);
                                    recyclerViewArtist.setAdapter(genreArtistAdapter);

                                    if (data.getInt("is_more")!=1){
                                           btnMoreArtist.setVisibility(View.GONE);
                                    }

                                    btnMoreArtist.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailGenre.this, More.class);
                                            intent.putExtra("title", dataArtist.name);
                                            intent.putExtra("type", "1");
                                            intent.putExtra("urlMore", dataArtist.is_more_endpoint);
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                case 2:
                                    contAlbum.setVisibility(View.VISIBLE);
                                    final GenreAlbumModel albumModel = gson.fromJson(data.toString(), GenreAlbumModel.class);
                                    GenreAlbumAdapter genreAlbumAdapter = new GenreAlbumAdapter(DetailGenre.this, albumModel);
                                    recyclerViewAlbum.setAdapter(genreAlbumAdapter);

                                    if (data.getInt("is_more")!=1){
                                        btnMoreAlbum.setVisibility(View.GONE);
                                    }

                                    btnMoreAlbum.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailGenre.this, More.class);
                                            intent.putExtra("title", albumModel.name);
                                            intent.putExtra("type", "1");
                                            intent.putExtra("urlMore", albumModel.is_more_endpoint);
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                case 3:
                                    contSingle.setVisibility(View.VISIBLE);
                                    final GenreSingleModel singleModel = gson.fromJson(data.toString(), GenreSingleModel.class);
                                    GenreSingleAdapter genreSingleAdapter = new GenreSingleAdapter(DetailGenre.this, singleModel);
                                    recyclerViewSingle.setAdapter(genreSingleAdapter);

                                    if (data.getInt("is_more")!=1){
                                        btnMoreSingle.setVisibility(View.GONE);
                                    }

                                    btnMoreSingle.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailGenre.this, More.class);
                                            intent.putExtra("title", singleModel.name);
                                            intent.putExtra("type", "1");
                                            intent.putExtra("urlMore", singleModel.is_more_endpoint);
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    /*try {
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
                    }*/
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
