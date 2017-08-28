package co.digdaya.kindis.live.view.activity.Detail;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.databse.KindisDBHelper;
import co.digdaya.kindis.live.databse.KindisDBname;
import co.digdaya.kindis.live.helper.Constanta;
import co.digdaya.kindis.live.helper.PlayerActionHelper;
import co.digdaya.kindis.live.helper.PlayerSessionHelper;
import co.digdaya.kindis.live.model.DataSingleOffline;
import co.digdaya.kindis.live.service.PlayerService;
import co.digdaya.kindis.live.util.BackgroundProses.ParseJsonPlaylist;
import co.digdaya.kindis.live.util.BaseBottomPlayer.BottomPlayerActivity;
import co.digdaya.kindis.live.view.adapter.offline.AdapterDetailSongOffline;

/**
 * Created by DELL on 5/18/2017.
 */

public class DetailOffline extends BottomPlayerActivity implements View.OnClickListener {
    TextView titleDetail, titleToolbar, desc;
    AppBarLayout appBarLayout;
    LinearLayout contFloatingButton;
    RelativeLayout contLabel;
    Toolbar toolbar;
    RecyclerView listViewSong;
    ImageView backDrop;
    Button btnPlayAll;
    AdapterDetailSongOffline adapterDetailSongOffline;
    List<DataSingleOffline> dataSingleOfflines = new ArrayList<>();
    ArrayList<String> songPlaylist = new ArrayList<>();
    PlayerSessionHelper playerSessionHelper;

    public DetailOffline() {
        layout = R.layout.activity_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleDetail = (TextView) findViewById(R.id.title_detail);
        titleToolbar = (TextView) findViewById(R.id.title_toolbar);
        desc = (TextView) findViewById(R.id.description);
        appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);
        contFloatingButton = (LinearLayout) findViewById(R.id.cont_floating_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        backDrop = (ImageView) findViewById(R.id.backdrop);
        btnPlayAll = (Button) findViewById(R.id.btn_play_all);
        contLabel = (RelativeLayout) findViewById(R.id.cont_label);
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

        listViewSong.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        listViewSong.setNestedScrollingEnabled(false);
        listViewSong.setFocusable(false);

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

        titleToolbar.setText(getIntent().getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_ALBUM));
        titleDetail.setText(getIntent().getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_ALBUM));
        desc.setText(getIntent().getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_DESC));
        backDrop.setColorFilter(Color.parseColor("#70000000"));
        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_IMAGE))
                .thumbnail( 0.1f )
                .placeholder(R.drawable.bg_sign_in)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(backDrop);
        btnPlayAll.setOnClickListener(this);
        getSong(getIntent().getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_ALBUM_ID));
    }

    private void getSong(String fkID){
        KindisDBHelper kindisDBHelper = new KindisDBHelper(getApplicationContext());
        SQLiteDatabase db = kindisDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ KindisDBname.TABLE_SINGLE +" WHERE "+KindisDBname.COLUMN_FK+" = "+fkID,null);
        if (cursor.moveToFirst()){
            while (cursor.isAfterLast()==false){
                System.out.println("DBData: "+cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_TITLE)));
                System.out.println("DBData: "+cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_PATH)));
                DataSingleOffline dataSingleOffline = new DataSingleOffline();
                dataSingleOffline.setUid(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ID)));
                dataSingleOffline.setTitle(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_TITLE)));
                dataSingleOffline.setPath(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_PATH)));
                dataSingleOffline.setImage(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_IMAGE)));
                dataSingleOffline.setAlbum(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ALBUM)));
                dataSingleOffline.setArtist(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ARTIST)));
                dataSingleOffline.setArtist_id(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ARTIST_ID)));
                dataSingleOfflines.add(dataSingleOffline);
                songPlaylist.add(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ID)));
                cursor.moveToNext();
            }
        }
        adapterDetailSongOffline = new AdapterDetailSongOffline(this, dataSingleOfflines);
        listViewSong.setAdapter(adapterDetailSongOffline);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnPlayAll.getId()){
            new ParseJsonPlaylist(getApplicationContext(), true);
            playerSessionHelper.setPreferences(getApplicationContext(), "index", String.valueOf(songPlaylist.size()));
            playerSessionHelper.setPreferences(getApplicationContext(), "fkid", (getIntent().getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_ALBUM_ID)));
            playerSessionHelper.setPreferences(getApplicationContext(), "title", dataSingleOfflines.get(0).getTitle());
            playerSessionHelper.setPreferences(getApplicationContext(), "subtitle", dataSingleOfflines.get(0).getArtist());
            playerSessionHelper.setPreferences(getApplicationContext(), "image", dataSingleOfflines.get(0).getImage());
            playerSessionHelper.setPreferences(getApplicationContext(), "artist_id", dataSingleOfflines.get(0).getArtist_id());
            Intent intent = new Intent(this, PlayerService.class);
            intent.setAction(PlayerActionHelper.ACTION_PLAY_OFFLINE_ALL);
            intent.putExtra("songresource", dataSingleOfflines.get(0).getPath());
            intent.putExtra("list_uid", songPlaylist);
            startService(intent);
        }
    }
}
