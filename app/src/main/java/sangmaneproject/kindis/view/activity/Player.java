package sangmaneproject.kindis.view.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import sangmaneproject.kindis.PlayerService;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.PlayerActionHelper;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.util.DialogPlaylist;
import sangmaneproject.kindis.util.ParseJsonPlaylist;
import sangmaneproject.kindis.util.ZoomOutPageTransformer;
import sangmaneproject.kindis.view.adapter.AdapterListSong;

public class Player extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    ImageButton hide, btnNext, btnBack, btnLooping, btnMenu, btnList;
    ImageView icPlay;
    ViewPager viewPager;
    AdapterListSong adapterListSong;
    RelativeLayout btnPlay;
    TextView txtDuration, txtProgress, title, subtitle;
    AppCompatSeekBar seekBar;

    Dialog dialogPlaylis;
    ParseJsonPlaylist parseJsonPlaylist;

    int index, playlistPosition;
    int duration, progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        parseJsonPlaylist = new ParseJsonPlaylist(getApplicationContext());

        hide = (ImageButton) findViewById(R.id.btn_hide);
        btnNext = (ImageButton) findViewById(R.id.btn_next);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnLooping = (ImageButton) findViewById(R.id.btn_looping);
        btnMenu = (ImageButton) findViewById(R.id.btn_songlis);
        btnList = (ImageButton) findViewById(R.id.btn_list);
        icPlay = (ImageView) findViewById(R.id.btn_play);
        viewPager = (ViewPager) findViewById(R.id.list_player);
        btnPlay = (RelativeLayout) findViewById(R.id.cont_play);
        txtDuration = (TextView) findViewById(R.id.duration);
        txtProgress = (TextView) findViewById(R.id.current_time);
        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
        seekBar = (AppCompatSeekBar) findViewById(R.id.seek_bar);

        index = Integer.parseInt(new PlayerSessionHelper().getPreferences(getApplicationContext(), "index"));

        /*DisplayMetrics metrics = getResources().getDisplayMetrics();
        switch (metrics.densityDpi){
            case DisplayMetrics.DENSITY_HIGH :
                viewPager.getLayoutParams().height = 260;
                break;
        }*/

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapterListSong = new AdapterListSong(getApplicationContext(), parseJsonPlaylist.getImageList());
        viewPager.setAdapter(adapterListSong);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        if (new PlayerSessionHelper().getPreferences(getApplicationContext(), "isplaying").equals("true")){
            icPlay.setImageResource(R.drawable.ic_pause_large);
        }else {
            icPlay.setImageResource(R.drawable.ic_play_large);
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

        if (index == 1){
            btnBack.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
            btnNext.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
            btnBack.setEnabled(false);
            btnNext.setEnabled(false);
        }else if (playlistPosition == 0){
            btnBack.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
            btnBack.setEnabled(false);
        }

        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnLooping.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnList.setOnClickListener(this);
        btnPlay.setOnClickListener(this);

        viewPager.addOnPageChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        title.setText(new PlayerSessionHelper().getPreferences(getApplicationContext(), "title"));
        subtitle.setText(new PlayerSessionHelper().getPreferences(getApplicationContext(), "subtitle"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(PlayerActionHelper.BROADCAST));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverBroadcastInfo, new IntentFilter(PlayerActionHelper.BROADCAST_INFO));

        String isLooping = ""+new PlayerSessionHelper().getPreferences(getApplicationContext(), "isLooping");
        if (isLooping.equals("true")){
            btnLooping.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }else {
            btnLooping.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
        }

        if (new PlayerSessionHelper().getPreferences(getApplicationContext(), "pause").equals("true")){
            int pos = Integer.parseInt(new PlayerSessionHelper().getPreferences(getApplicationContext(), "current_pos"));
            seekBar.setProgress(pos);
            txtProgress.setText(getTimeString(pos));
        }

        if (new PlayerSessionHelper().getPreferences(getApplicationContext(), "file").isEmpty()){
            title.setText("No Song");
            subtitle.setText("");
            txtProgress.setText(getTimeString(0));
            txtDuration.setText(getTimeString(0));
        }else {
            int dur = Integer.parseInt(new PlayerSessionHelper().getPreferences(getApplicationContext(), "duration"));
            seekBar.setMax(dur);
            txtDuration.setText(getTimeString(dur));
        }

        if (!new PlayerSessionHelper().getPreferences(getApplicationContext(), "index").equals("1")){
            if (!new PlayerSessionHelper().getPreferences(getApplicationContext(), "playlistPosition").isEmpty()){
                playlistPosition = Integer.parseInt(new PlayerSessionHelper().getPreferences(getApplicationContext(), "playlistPosition"));
                viewPager.setCurrentItem(playlistPosition, true);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_looping){
            String isLooping = ""+new PlayerSessionHelper().getPreferences(getApplicationContext(), "isLooping");
            if (isLooping.equals("true")){
                btnLooping.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                new PlayerSessionHelper().setPreferences(getApplicationContext(), "isLooping", "false");

                Intent intent = new Intent(Player.this, PlayerService.class);
                intent.setAction(PlayerActionHelper.ACTION_LOOPING);
                startService(intent);
            }else{
                btnLooping.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
                new PlayerSessionHelper().setPreferences(getApplicationContext(), "isLooping", "true");

                Intent intent = new Intent(Player.this, PlayerService.class);
                intent.setAction(PlayerActionHelper.ACTION_LOOPING);
                startService(intent);
            }
        }else if (view.getId() == R.id.btn_songlis){
            new DialogPlaylist(Player.this, dialogPlaylis, new PlayerSessionHelper().getPreferences(getApplicationContext(), "uid")).showDialog();
        }else if (view.getId() == R.id.btn_list){
            Intent intent = new Intent(this, ListSongPlayer.class);
            startActivity(intent);
        }else if (view.getId() == R.id.cont_play){
            if (!new PlayerSessionHelper().getPreferences(getApplicationContext(), "isplaying").equals("true")){
                new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "true");
                icPlay.setImageResource(R.drawable.ic_pause_large);
                Intent intent = new Intent(Player.this, PlayerService.class);
                intent.setAction(PlayerActionHelper.ACTION_PLAY);
                startService(intent);
            }else {
                new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "false");
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
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Intent intent = new Intent(getApplicationContext(), PlayerService.class);
        intent.setAction(PlayerActionHelper.PLAY_PLAYLIST);
        intent.putExtra("single_id", parseJsonPlaylist.getSongPlaylist().get(position));
        intent.putExtra("position", position);
        intent.putExtra("list_uid", parseJsonPlaylist.getSongPlaylist());
        startService(intent);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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

            if (new PlayerSessionHelper().getPreferences(getApplicationContext(), "isplaying").equals("true")){
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
                btnBack.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
                btnBack.setEnabled(true);
            }

            if (playlistPosition == (index-1)){
                btnNext.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.dark_gray));
                btnNext.setEnabled(false);
            }else {
                btnNext.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
                btnNext.setEnabled(true);
            }
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
}
