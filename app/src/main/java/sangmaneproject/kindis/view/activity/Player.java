package sangmaneproject.kindis.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.Log;
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
import sangmaneproject.kindis.view.adapter.AdapterListSong;

public class Player extends AppCompatActivity {
    ImageButton hide;
    ImageView icPlay;
    ViewPager viewPager;
    AdapterListSong adapterListSong;
    RelativeLayout btnPlay;
    TextView txtDuration, txtProgress;
    AppCompatSeekBar seekBar;

    int duration, progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        hide = (ImageButton) findViewById(R.id.btn_hide);
        icPlay = (ImageView) findViewById(R.id.btn_play);
        viewPager = (ViewPager) findViewById(R.id.list_player);
        btnPlay = (RelativeLayout) findViewById(R.id.cont_play);
        txtDuration = (TextView) findViewById(R.id.duration);
        txtProgress = (TextView) findViewById(R.id.current_time);
        seekBar = (AppCompatSeekBar) findViewById(R.id.seek_bar);

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapterListSong = new AdapterListSong(getApplicationContext());
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(PlayerActionHelper.BROADCAST));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            duration = intent.getIntExtra(PlayerActionHelper.BROADCAST_MAX_DURATION, 100);
            progress = intent.getIntExtra(PlayerActionHelper.BROADCAST_CURRENT_DURATION, 0);
            Log.d("playerreceiver", "Got message: " + duration + " : " + progress);
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
