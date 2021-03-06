package co.digdaya.kindis.live.view.activity.Detail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
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

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.PlayerSessionHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.model.genre.GenreAlbumModel;
import co.digdaya.kindis.live.model.genre.GenreArtistModel;
import co.digdaya.kindis.live.model.genre.GenreSingleModel;
import co.digdaya.kindis.live.util.BaseBottomPlayer.BottomPlayerActivity;
import co.digdaya.kindis.live.util.SpacingItem.MarginItemHorizontal;
import co.digdaya.kindis.live.util.SpacingItem.SpacingItemGenre;
import co.digdaya.kindis.live.view.adapter.genre.GenreAlbumAdapter;
import co.digdaya.kindis.live.view.adapter.genre.GenreArtistAdapter;
import co.digdaya.kindis.live.view.adapter.genre.GenreSingleAdapter;
import co.digdaya.kindis.live.view.adapter.item.AdapterAlbumNew;
import co.digdaya.kindis.live.view.adapter.item.AdapterSongHorizontal;

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
    TextView labelArtist, labelPlaylist, labelSingle, labelAlbum;

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

        labelArtist = (TextView) findViewById(R.id.label_artist);
        labelPlaylist = (TextView) findViewById(R.id.label_playlis);
        labelAlbum = (TextView) findViewById(R.id.label_album);
        labelSingle = (TextView) findViewById(R.id.label_single);

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
        description.setTextSize(12);

        recyclerViewArtist.setNestedScrollingEnabled(false);
        recyclerViewAlbum.setNestedScrollingEnabled(false);
        recyclerViewSingle.setNestedScrollingEnabled(false);
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
                        System.out.println("descriptiongenre: "+summary.getString("description"));
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

                                    setRecyclerViewLayout(data.getInt("type_id"), recyclerViewArtist);
                                    labelArtist.setText(dataArtist.name);

                                    GenreArtistAdapter genreArtistAdapter = new GenreArtistAdapter(DetailGenre.this, dataArtist, data.getInt("type_id"));
                                    recyclerViewArtist.setAdapter(genreArtistAdapter);

                                    if (data.getInt("is_more")!=1){
                                           btnMoreArtist.setVisibility(View.GONE);
                                    }

                                    btnMoreArtist.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailGenre.this, More.class);
                                            intent.putExtra("title", dataArtist.name);
                                            intent.putExtra("type", 1);
                                            intent.putExtra("urlMore", dataArtist.is_more_endpoint);
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                case 2:
                                    contAlbum.setVisibility(View.VISIBLE);
                                    final GenreAlbumModel albumModel = gson.fromJson(data.toString(), GenreAlbumModel.class);

                                    setRecyclerViewLayout(data.getInt("type_id"), recyclerViewAlbum);
                                    labelAlbum.setText(albumModel.name);

                                    GenreAlbumAdapter genreAlbumAdapter = new GenreAlbumAdapter(DetailGenre.this, albumModel, data.getInt("type_id"));
                                    recyclerViewAlbum.setAdapter(genreAlbumAdapter);

                                    if (data.getInt("is_more")!=1){
                                        btnMoreAlbum.setVisibility(View.GONE);
                                    }

                                    btnMoreAlbum.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailGenre.this, More.class);
                                            intent.putExtra("title", albumModel.name);
                                            intent.putExtra("type", 2);
                                            intent.putExtra("urlMore", albumModel.is_more_endpoint);
                                            startActivity(intent);
                                        }
                                    });
                                    break;
                                case 3:
                                    contSingle.setVisibility(View.VISIBLE);

                                    final GenreSingleModel singleModel = gson.fromJson(data.toString(), GenreSingleModel.class);

                                    setRecyclerViewLayout(data.getInt("type_id"), recyclerViewSingle);
                                    labelSingle.setText(singleModel.name);

                                    GenreSingleAdapter genreSingleAdapter = new GenreSingleAdapter(DetailGenre.this, singleModel, data.getInt("type_id"));
                                    recyclerViewSingle.setAdapter(genreSingleAdapter);

                                    if (data.getInt("is_more")!=1){
                                        btnMoreSingle.setVisibility(View.GONE);
                                    }

                                    btnMoreSingle.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(DetailGenre.this, More.class);
                                            intent.putExtra("title", singleModel.name);
                                            intent.putExtra("type", 3);
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
                }
            }
        });
    }

    private void setRecyclerViewLayout(int type, RecyclerView recyclerView){
        if (type==1){
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.addItemDecoration(new MarginItemHorizontal(this));
        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
            recyclerView.addItemDecoration(new SpacingItemGenre(getApplicationContext(), ""));
        }
    }
}
