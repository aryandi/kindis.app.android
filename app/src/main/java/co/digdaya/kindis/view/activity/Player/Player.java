package co.digdaya.kindis.view.activity.Player;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.databse.KindisDBHelper;
import co.digdaya.kindis.databse.KindisDBname;
import co.digdaya.kindis.helper.CheckPermission;
import co.digdaya.kindis.helper.Constanta;
import co.digdaya.kindis.model.DataSingleOffline;
import co.digdaya.kindis.service.DownloadService;
import co.digdaya.kindis.service.PlayerService;
import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.PlayerActionHelper;
import co.digdaya.kindis.helper.PlayerSessionHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.view.activity.Premium;
import co.digdaya.kindis.view.dialog.DialogGetPremium;
import co.digdaya.kindis.view.dialog.DialogSingleMenu;
import co.digdaya.kindis.util.BackgroundProses.ParseJsonPlaylist;
import co.digdaya.kindis.util.ZoomOutPageTransformer;
import co.digdaya.kindis.view.adapter.AdapterListSong;

public class Player extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener{
    ImageButton hide, btnNext, btnBack, btnLooping, btnMenu, btnList, btnShuffle, btnDownload;
    ImageView icPlay;
    ViewPager viewPager;
    AdapterListSong adapterListSong;
    RelativeLayout btnPlay;
    TextView txtDuration, txtProgress, title, subtitle, titleActivity, subtitleActivity;
    AppCompatSeekBar seekBar;

    Dialog dialogPlaylis, dialogPremium;
    DialogGetPremium dialogGetPremium;
    DialogSingleMenu dialogSingleMenu;
    ParseJsonPlaylist parseJsonPlaylist;

    int index, playlistPosition;
    int duration, progress;
    int isAccountPremium;

    boolean isChangeViewPager = false;
    boolean isOfflineMode;
    boolean isInFront;

    ArrayList<String> imgList = new ArrayList<>();
    ArrayList<String> songPlaylist = new ArrayList<>();

    PlayerSessionHelper playerSessionHelper = new PlayerSessionHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        parseJsonPlaylist = new ParseJsonPlaylist(getApplicationContext(), false);
        songPlaylist = new ArrayList<>();

        dialogGetPremium = new DialogGetPremium(this, dialogPremium);
        dialogSingleMenu = new DialogSingleMenu(this, dialogPlaylis, playerSessionHelper.getPreferences(getApplicationContext(), "uid"), playerSessionHelper.getPreferences(getApplicationContext(), "artist_id"), playerSessionHelper.getPreferences(getApplicationContext(), "share_link"), false);

        hide = (ImageButton) findViewById(R.id.btn_hide);
        btnNext = (ImageButton) findViewById(R.id.btn_next);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnLooping = (ImageButton) findViewById(R.id.btn_looping);
        btnMenu = (ImageButton) findViewById(R.id.btn_songlis);
        btnList = (ImageButton) findViewById(R.id.btn_list);
        btnShuffle = (ImageButton) findViewById(R.id.btn_shuffle);
        btnDownload = (ImageButton) findViewById(R.id.btn_download);
        icPlay = (ImageView) findViewById(R.id.btn_play);
        viewPager = (ViewPager) findViewById(R.id.list_player);
        btnPlay = (RelativeLayout) findViewById(R.id.cont_play);
        txtDuration = (TextView) findViewById(R.id.duration);
        txtProgress = (TextView) findViewById(R.id.current_time);
        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
        seekBar = (AppCompatSeekBar) findViewById(R.id.seek_bar);
        titleActivity = (TextView) findViewById(R.id.title_activity);
        subtitleActivity = (TextView) findViewById(R.id.subtitle_activity);

        index = Integer.parseInt(playerSessionHelper.getPreferences(getApplicationContext(), "index"));
        isAccountPremium = Integer.parseInt(new SessionHelper().getPreferences(getApplicationContext(), "is_premium"));
        isOfflineMode = Boolean.parseBoolean(playerSessionHelper.getPreferences(getApplicationContext(), "is_offline_mode"));

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                return;
            }
        });

        if (playerSessionHelper.getPreferences(getApplicationContext(), "isplaying").equals("true")){
            icPlay.setImageResource(R.drawable.ic_pause_large);
        }else {
            icPlay.setImageResource(R.drawable.ic_play_large);
        }

        if (index == 1){
            imgList.add(playerSessionHelper.getPreferences(getApplicationContext(), "image"));
            adapterListSong = new AdapterListSong(getApplicationContext(), imgList);
            viewPager.setAdapter(adapterListSong);
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            titleActivity.setText("Single");
            subtitleActivity.setText(playerSessionHelper.getPreferences(getApplicationContext(), "title"));
        }else {
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            if (isOfflineMode){
                listOfflineSong(playerSessionHelper.getPreferences(getApplicationContext(), "fkid"));
            }else {
                System.out.println("isshufflebol: "+playerSessionHelper.getPreferences(getApplicationContext(), "isShuffle"));
                if (Boolean.parseBoolean(playerSessionHelper.getPreferences(getApplicationContext(), "isShuffle"))){
                    adapterListSong = new AdapterListSong(getApplicationContext(), parseJsonPlaylist.getShuffleImageList());
                    viewPager.setAdapter(adapterListSong);
                    btnShuffle.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    songPlaylist = parseJsonPlaylist.getShuffleSongPlaylist();
                }else {
                    adapterListSong = new AdapterListSong(getApplicationContext(), parseJsonPlaylist.getImageList());
                    viewPager.setAdapter(adapterListSong);
                    btnShuffle.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                    songPlaylist = parseJsonPlaylist.getSongPlaylist();
                }
                subtitleActivity.setText(playerSessionHelper.getPreferences(getApplicationContext(), "subtitle_player"));
            }
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Intent intent = new Intent(Player.this, PlayerService.class);
                intent.setAction(PlayerActionHelper.ACTION_SEEK);
                intent.putExtra("progress", seekBar.getProgress());
                startService(intent);
            }
        });

        if (!new SessionHelper().getPreferences(getApplicationContext(), "is_premium").equals("1")){
            btnBack.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
            btnBack.setEnabled(false);
            seekBar.setEnabled(false);
        }

        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnLooping.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnList.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnShuffle.setOnClickListener(this);
        btnDownload.setOnClickListener(this);

        viewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        isInFront = true;
        title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        title.setSelected(true);
        title.setSingleLine(true);
        title.setText(playerSessionHelper.getPreferences(getApplicationContext(), "title"));
        subtitle.setText(playerSessionHelper.getPreferences(getApplicationContext(), "subtitle"));
        if (!playerSessionHelper.getPreferences(getApplicationContext(), "playlistPosition").isEmpty()){
            playlistPosition = Integer.parseInt(playerSessionHelper.getPreferences(getApplicationContext(), "playlistPosition"));
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(PlayerActionHelper.BROADCAST));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverBroadcastInfo, new IntentFilter(PlayerActionHelper.BROADCAST_INFO));

        String isLooping = ""+playerSessionHelper.getPreferences(getApplicationContext(), "isLooping");
        if (isLooping.equals("true")){
            btnLooping.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }else {
            btnLooping.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
        }

        if (playerSessionHelper.getPreferences(getApplicationContext(), "pause").equals("true")){
            int pos = Integer.parseInt(playerSessionHelper.getPreferences(getApplicationContext(), "current_pos"));
            seekBar.setProgress(pos);
            txtProgress.setText(getTimeString(pos));
        }

        if (playerSessionHelper.getPreferences(getApplicationContext(), "file").isEmpty()){
            title.setText("No Song");
            subtitle.setText("");
            txtProgress.setText(getTimeString(0));
            txtDuration.setText(getTimeString(0));
        }else {
            int dur = Integer.parseInt(playerSessionHelper.getPreferences(getApplicationContext(), "duration"));
            seekBar.setMax(dur);
            txtDuration.setText(getTimeString(dur));
        }

        if (!playerSessionHelper.getPreferences(getApplicationContext(), "index").equals("1")){
            if (!playerSessionHelper.getPreferences(getApplicationContext(), "playlistPosition").isEmpty()){
                viewPager.setCurrentItem(playlistPosition, true);
            }
        }

        if (index == 1){
            btnBack.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
            btnNext.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
            btnBack.setEnabled(false);
            btnNext.setEnabled(false);
        }else if (playlistPosition == 0){
            btnBack.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
            btnBack.setEnabled(false);
        }else if (playlistPosition == index-1){
            btnNext.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
            btnNext.setEnabled(false);
        }

        isChangeViewPager = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isInFront = false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_looping){
            String isLooping = ""+playerSessionHelper.getPreferences(getApplicationContext(), "isLooping");
            if (isLooping.equals("true")){
                btnLooping.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                playerSessionHelper.setPreferences(getApplicationContext(), "isLooping", "false");

                Intent intent = new Intent(Player.this, PlayerService.class);
                intent.setAction(PlayerActionHelper.ACTION_LOOPING);
                startService(intent);
            }else{
                btnLooping.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
                playerSessionHelper.setPreferences(getApplicationContext(), "isLooping", "true");

                Intent intent = new Intent(Player.this, PlayerService.class);
                intent.setAction(PlayerActionHelper.ACTION_LOOPING);
                startService(intent);
            }
        }else if (view.getId() == R.id.btn_songlis){
            //dialogSingleMenu.showDialog();
            Intent intent = new Intent(this, SongMenu.class);
            intent.putExtra(Constanta.INTENT_EXTRA_IMAGE, playerSessionHelper.getPreferences(getApplicationContext(), "image"));
            intent.putExtra(Constanta.INTENT_EXTRA_TITLE, playerSessionHelper.getPreferences(getApplicationContext(), "title"));
            intent.putExtra(Constanta.INTENT_EXTRA_SUBTITLE, playerSessionHelper.getPreferences(getApplicationContext(), "subtitle"));
            startActivity(intent);
        }else if (view.getId() == R.id.btn_list){
            Intent intent = new Intent(this, ListSongPlayer.class);
            intent.putExtra("title", titleActivity.getText());
            intent.putExtra("subtitle", subtitleActivity.getText());
            startActivity(intent);
        }else if (view.getId() == R.id.cont_play){
            if (!playerSessionHelper.getPreferences(getApplicationContext(), "isplaying").equals("true")){
                playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "true");
                icPlay.setImageResource(R.drawable.ic_pause_large);
                Intent intent = new Intent(Player.this, PlayerService.class);
                intent.setAction(PlayerActionHelper.ACTION_PLAY);
                startService(intent);
            }else {
                playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
                icPlay.setImageResource(R.drawable.ic_play_large);
                Intent intent = new Intent(Player.this, PlayerService.class);
                intent.setAction(PlayerActionHelper.ACTION_PAUSE);
                startService(intent);
            }
        }else if (view.getId() == R.id.btn_back){
            viewPager.setCurrentItem(playlistPosition-1, true);
            icPlay.setImageResource(R.drawable.ic_pause_large);
        }else if (view.getId() == R.id.btn_next){
            viewPager.setCurrentItem(playlistPosition+1, true);
            icPlay.setImageResource(R.drawable.ic_pause_large);
        }else if (view.getId() == R.id.btn_shuffle){
            playlistPosition = 0;
            playerSessionHelper.setPreferences(getApplicationContext(), "playlistPosition", "0");
            btnBack.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
            btnBack.setEnabled(false);
            songPlaylist = new ArrayList<>();
            if (Boolean.parseBoolean(playerSessionHelper.getPreferences(getApplicationContext(), "isShuffle"))){
                songPlaylist = parseJsonPlaylist.getSongPlaylist();
                new PlayerSessionHelper().setPreferences(getApplicationContext(), "index", String.valueOf(parseJsonPlaylist.getImageList().size()));
                btnShuffle.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                adapterListSong = new AdapterListSong(getApplicationContext(), parseJsonPlaylist.getImageList());
                adapterListSong.notifyDataSetChanged();
                viewPager.setAdapter(adapterListSong);
                playerSessionHelper.setPreferences(getApplicationContext(), "isShuffle", "false");
            }else {
                songPlaylist = parseJsonPlaylist.getShuffleSongPlaylist();
                new PlayerSessionHelper().setPreferences(getApplicationContext(), "index", String.valueOf(parseJsonPlaylist.getShuffleImageList().size()));
                btnShuffle.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
                adapterListSong = new AdapterListSong(getApplicationContext(), parseJsonPlaylist.getShuffleImageList());
                adapterListSong.notifyDataSetChanged();
                viewPager.setAdapter(adapterListSong);
                playerSessionHelper.setPreferences(getApplicationContext(), "isShuffle", "true");
            }
            nextPlay(0);
        }else if (view.getId() == R.id.btn_download){
            if (new CheckPermission(this).checkPermissionStorage()){
                startDownload();
            }else {
                new CheckPermission(this).showPermissionStorage(2);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (isChangeViewPager){
            if (isAccountPremium == 1){
                if (isOfflineMode){
                    Intent intent = new Intent(this, PlayerService.class);
                    intent.setAction(PlayerActionHelper.ACTION_PLAY_OFFLINE_NEXT);
                    intent.putExtra("position", position);
                    intent.putExtra("songPlaylist", songPlaylist);
                    startService(intent);
                }else {
                    nextPlay(position);
                }
            }else {
                if (position > playlistPosition){
                    nextPlay(position);
                }else {
                    viewPager.setCurrentItem(playlistPosition, true);
                    Intent intent = new Intent(this, Premium.class);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void nextPlay(int position){
        if (isInFront){
            System.out.println("nextplay player: "+songPlaylist.get(position));
            Intent intent = new Intent(getApplicationContext(), PlayerService.class);
            intent.setAction(PlayerActionHelper.PLAY_PLAYLIST);
            intent.putExtra("from", "Player");
            intent.putExtra("single_id", songPlaylist.get(position));
            intent.putExtra("position", position);
            intent.putExtra("list_uid", songPlaylist);
            startService(intent);
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            duration = intent.getIntExtra(PlayerActionHelper.BROADCAST_MAX_DURATION, 100);
            progress = intent.getIntExtra(PlayerActionHelper.BROADCAST_CURRENT_DURATION, 0);
            txtDuration.setText(getTimeString(duration));
            txtProgress.setText(getTimeString(progress));
            seekBar.setMax(duration);
            seekBar.post(new Runnable() {
                @Override
                public void run() {
                    seekBar.setProgress(progress);
                }
            });
            if (progress==duration){
                icPlay.setImageResource(R.drawable.ic_play_large);
            }

            if (playerSessionHelper.getPreferences(getApplicationContext(), "isplaying").equals("true")){
                icPlay.setImageResource(R.drawable.ic_pause_large);
            }
        }
    };

    private BroadcastReceiver receiverBroadcastInfo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            title.setText(intent.getStringExtra(PlayerActionHelper.BROADCAST_TITLE));
            subtitle.setText(intent.getStringExtra(PlayerActionHelper.BROADCAST_SUBTITLE));
            playlistPosition = intent.getIntExtra(PlayerActionHelper.BROADCAST_POSITION, 0);

            if (playlistPosition == 0){
                btnBack.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                btnBack.setEnabled(false);
            }else {
                if (!new SessionHelper().getPreferences(getApplicationContext(), "is_premium").equals("1")){
                    btnBack.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                    btnBack.setEnabled(false);
                }else {
                    btnBack.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    btnBack.setEnabled(true);
                }
            }

            if (playlistPosition == (index-1)){
                btnNext.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                btnNext.setEnabled(false);
            }else {
                btnNext.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
                btnNext.setEnabled(true);
            }

            viewPager.setCurrentItem(playlistPosition, true);
        }
    };

    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==2){
            if (new CheckPermission(this).checkPermissionStorage()){
                startDownload();
            }
        }
    }

    private void startDownload(){
        Toast.makeText(getApplicationContext(), "Downloading", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DownloadService.class);
        intent.setAction(Constanta.INTENT_ACTION_DOWNLOAD_SINGLE);
        intent.putExtra(Constanta.INTENT_ACTION_DOWNLOAD_SINGLE_ID, playerSessionHelper.getPreferences(getApplicationContext(), "uid"));
        startService(intent);
    }

    private void listOfflineSong(String fkID){
        KindisDBHelper kindisDBHelper = new KindisDBHelper(getApplicationContext());
        SQLiteDatabase db = kindisDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ KindisDBname.TABLE_SINGLE +" WHERE "+KindisDBname.COLUMN_FK+" = "+fkID,null);
        if (cursor.moveToFirst()){
            while (cursor.isAfterLast()==false){
                imgList.add(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_IMAGE)));
                songPlaylist.add(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ID)));
                cursor.moveToNext();
            }
        }
        adapterListSong = new AdapterListSong(getApplicationContext(), imgList);
        viewPager.setAdapter(adapterListSong);
    }
}
