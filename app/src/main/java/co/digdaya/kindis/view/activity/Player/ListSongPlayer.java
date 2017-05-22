package co.digdaya.kindis.view.activity.Player;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import co.digdaya.kindis.R;
import co.digdaya.kindis.databse.KindisDBHelper;
import co.digdaya.kindis.databse.KindisDBname;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.PlayerSessionHelper;
import co.digdaya.kindis.model.DataSingleOffline;
import co.digdaya.kindis.view.activity.Detail.Detail;
import co.digdaya.kindis.view.adapter.AdapterListSong;
import co.digdaya.kindis.view.adapter.offline.AdapterDetailSongOffline;
import co.digdaya.kindis.view.adapter.offline.AdapterSongOffline;
import co.digdaya.kindis.view.dialog.DialogSingleMenu;
import co.digdaya.kindis.util.BackgroundProses.ParseJsonPlaylist;
import co.digdaya.kindis.view.adapter.item.AdapterSong;

public class ListSongPlayer extends AppCompatActivity implements View.OnClickListener {
    ParseJsonPlaylist parseJsonPlaylist;
    PlayerSessionHelper playerSessionHelper;

    RecyclerView listViewSong;
    LinearLayoutManager linearLayoutManager;
    RelativeLayout contSingle;
    AdapterSong adapterSong;
    AdapterDetailSongOffline adapterDetailSongOffline;

    List<DataSingleOffline> dataSingleOfflines = new ArrayList<>();

    String type, json;
    Dialog dialogPlaylis;
    Boolean isOfflineMode;

    ImageButton btnHide;
    TextView labelPlayNext;

    ImageView imgSong;
    ImageButton btnMenuPlay;
    TextView titlePlay;
    TextView subtitlePlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song_player);

        btnHide = (ImageButton) findViewById(R.id.btn_hide);
        labelPlayNext = (TextView) findViewById(R.id.label_play_next);

        contSingle = (RelativeLayout) findViewById(R.id.cont_single);
        listViewSong = (RecyclerView) findViewById(R.id.list_songs);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        listViewSong.setLayoutManager(linearLayoutManager);

        imgSong = (ImageView) findViewById(R.id.image_song);
        titlePlay = (TextView) findViewById(R.id.title_play);
        subtitlePlay = (TextView) findViewById(R.id.subtitle_play);
        btnMenuPlay = (ImageButton) findViewById(R.id.btn_menu_play);

        btnHide.setOnClickListener(this);
        btnMenuPlay.setOnClickListener(this);

        parseJsonPlaylist = new ParseJsonPlaylist(getApplicationContext());
        playerSessionHelper = new PlayerSessionHelper();

        isOfflineMode = Boolean.parseBoolean(playerSessionHelper.getPreferences(getApplicationContext(), "is_offline_mode"));

        type = playerSessionHelper.getPreferences(getApplicationContext(), "type");
        json = playerSessionHelper.getPreferences(getApplicationContext(), "json");

        String images;
        if (isOfflineMode){
            images = playerSessionHelper.getPreferences(getApplicationContext(), "image");
            btnMenuPlay.setVisibility(View.GONE);
        }else {
            images = ApiHelper.BASE_URL_IMAGE+playerSessionHelper.getPreferences(getApplicationContext(), "image");
        }
        Glide.with(getApplicationContext())
                .load(images)
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imgSong);

        titlePlay.setText(playerSessionHelper.getPreferences(getApplicationContext(), "title"));
        subtitlePlay.setText(playerSessionHelper.getPreferences(getApplicationContext(), "subtitle"));

        if (playerSessionHelper.getPreferences(getApplicationContext(), "index").equals("1")){
            labelPlayNext.setVisibility(View.GONE);
        }else {
            listViewSong.setVisibility(View.VISIBLE);
            if (isOfflineMode){
                listOfflineSong(playerSessionHelper.getPreferences(getApplicationContext(), "fkid"));
            }else {
                if (Boolean.parseBoolean(playerSessionHelper.getPreferences(getApplicationContext(), "isShuffle"))){
                    adapterSong = new AdapterSong(ListSongPlayer.this, parseJsonPlaylist.getShuffleListSong(), "list", parseJsonPlaylist.getShuffleSongPlaylist());
                    listViewSong.setAdapter(adapterSong);
                }else {
                    adapterSong = new AdapterSong(ListSongPlayer.this, parseJsonPlaylist.getListSong(), "list", parseJsonPlaylist.getSongPlaylist());
                    listViewSong.setAdapter(adapterSong);
                }
                adapterSong.setOnClickMenuListener(new AdapterSong.OnClickMenuListener() {
                    @Override
                    public void onClick(String uid, ImageButton imageButton, String artistID, String shareLink) {
                        new DialogSingleMenu(ListSongPlayer.this, dialogPlaylis, uid, artistID, shareLink, false).showDialog();
                    }
                });
            }
            listViewSong.setNestedScrollingEnabled(true);
            int pos = Integer.parseInt(playerSessionHelper.getPreferences(getApplicationContext(), "playlistPosition"))+1;
            linearLayoutManager.scrollToPosition(pos);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_hide:
                finish();
                break;
            case R.id.btn_menu_play:
                new DialogSingleMenu(ListSongPlayer.this, dialogPlaylis, playerSessionHelper.getPreferences(getApplicationContext(), "uid"), playerSessionHelper.getPreferences(getApplicationContext(), "artist_id"), playerSessionHelper.getPreferences(getApplicationContext(), "share_link"), false).showDialog();
                break;
        }
    }

    private void listOfflineSong(String fkID){
        KindisDBHelper kindisDBHelper = new KindisDBHelper(getApplicationContext());
        SQLiteDatabase db = kindisDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ KindisDBname.TABLE_SINGLE +" WHERE "+KindisDBname.COLUMN_FK+" = "+fkID,null);
        if (cursor.moveToFirst()){
            while (cursor.isAfterLast()==false){
                DataSingleOffline dataSingleOffline = new DataSingleOffline();
                dataSingleOffline.setUid(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ID)));
                dataSingleOffline.setTitle(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_TITLE)));
                dataSingleOffline.setPath(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_PATH)));
                dataSingleOffline.setImage(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_IMAGE)));
                dataSingleOffline.setAlbum(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ALBUM)));
                dataSingleOffline.setArtist(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ARTIST)));
                dataSingleOffline.setArtist_id(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ARTIST_ID)));
                dataSingleOfflines.add(dataSingleOffline);
                cursor.moveToNext();
            }
        }
        adapterDetailSongOffline = new AdapterDetailSongOffline(this, dataSingleOfflines);
        listViewSong.setAdapter(adapterDetailSongOffline);
    }
}
