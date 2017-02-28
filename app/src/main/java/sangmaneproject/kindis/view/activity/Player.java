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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import sangmaneproject.kindis.PlayerService;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.PlayerActionHelper;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.util.DialogPlaylist;
import sangmaneproject.kindis.view.adapter.AdapterListSong;

public class Player extends AppCompatActivity implements View.OnClickListener {
    ImageButton hide, btnNext, btnBack, btnLooping, btnMenu;
    ImageView icPlay;
    ViewPager viewPager;
    AdapterListSong adapterListSong;
    RelativeLayout btnPlay;
    TextView txtDuration, txtProgress, title, subtitle;
    AppCompatSeekBar seekBar;

    Dialog dialogPlaylis;

    int index;
    int duration, progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        hide = (ImageButton) findViewById(R.id.btn_hide);
        btnNext = (ImageButton) findViewById(R.id.btn_next);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnLooping = (ImageButton) findViewById(R.id.btn_looping);
        btnMenu = (ImageButton) findViewById(R.id.btn_songlis);
        icPlay = (ImageView) findViewById(R.id.btn_play);
        viewPager = (ViewPager) findViewById(R.id.list_player);
        btnPlay = (RelativeLayout) findViewById(R.id.cont_play);
        txtDuration = (TextView) findViewById(R.id.duration);
        txtProgress = (TextView) findViewById(R.id.current_time);
        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
        seekBar = (AppCompatSeekBar) findViewById(R.id.seek_bar);

        index = Integer.parseInt(new PlayerSessionHelper().getPreferences(getApplicationContext(), "index"));

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapterListSong = new AdapterListSong(getApplicationContext(), Integer.parseInt(new PlayerSessionHelper().getPreferences(getApplicationContext(), "index")));
        viewPager.setAdapter(adapterListSong);

        if (new PlayerSessionHelper().getPreferences(getApplicationContext(), "isplaying").equals("true")){
            icPlay.setImageResource(R.drawable.ic_pause);
        }else {
            icPlay.setImageResource(R.drawable.ic_play);
        }

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!new PlayerSessionHelper().getPreferences(getApplicationContext(), "isplaying").equals("true")){
                    new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "true");
                    icPlay.setImageResource(R.drawable.ic_pause);
                    Intent intent = new Intent(Player.this, PlayerService.class);
                    intent.setAction(PlayerActionHelper.ACTION_PLAY);
                    startService(intent);
                }else {
                    new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "false");
                    icPlay.setImageResource(R.drawable.ic_play);
                    Intent intent = new Intent(Player.this, PlayerService.class);
                    intent.setAction(PlayerActionHelper.ACTION_PAUSE);
                    startService(intent);
                }
            }
        });

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
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Player.this, PlayerService.class);
                intent.setAction(PlayerActionHelper.ACTION_SKIP);
                startService(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Player.this, PlayerService.class);
                intent.setAction(PlayerActionHelper.ACTION_REWIND);
                startService(intent);
            }
        });

        btnLooping.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        title.setText(new PlayerSessionHelper().getPreferences(getApplicationContext(), "title"));
        subtitle.setText(new PlayerSessionHelper().getPreferences(getApplicationContext(), "title"));

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
                icPlay.setImageResource(R.drawable.ic_play);
            }
        }
    };

    private BroadcastReceiver receiverBroadcastInfo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            title.setText(intent.getStringExtra(PlayerActionHelper.BROADCAST_TITLE));
            subtitle.setText(PlayerActionHelper.BROADCAST_SUBTITLE);
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
