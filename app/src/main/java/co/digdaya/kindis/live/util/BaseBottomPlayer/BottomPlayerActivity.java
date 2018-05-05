package co.digdaya.kindis.live.util.BaseBottomPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.AnalyticHelper;
import co.digdaya.kindis.live.service.PlayerService;
import co.digdaya.kindis.live.helper.PlayerActionHelper;
import co.digdaya.kindis.live.helper.PlayerSessionHelper;
import co.digdaya.kindis.live.view.activity.Player.Player;

/**
 * Created by DELL on 2/28/2017.
 */

public class BottomPlayerActivity extends AppCompatActivity {
    ImageButton expand;
    RelativeLayout btnPlay;
    ImageView icPlay;
    ProgressBar progressBar;
    int duration;
    int progress;
    TextView title, artist;
    LinearLayout contBottomPlayer;

    PlayerSessionHelper playerSessionHelper;

    public int layout;
    public boolean showBottomPlayer;
    private RelativeLayout layoutBottomPlayer;
    private AnalyticHelper analyticHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);

        layoutBottomPlayer = (RelativeLayout) findViewById(R.id.layout_bottom_player);
        expand = (ImageButton) findViewById(R.id.btn_expand);
        btnPlay = (RelativeLayout) findViewById(R.id.btn_play);
        icPlay = (ImageView) findViewById(R.id.ic_play);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        title = (TextView) findViewById(R.id.title_player);
        artist = (TextView) findViewById(R.id.artist_player);
        contBottomPlayer = (LinearLayout) findViewById(R.id.cont_bottom_player);

        playerSessionHelper = new PlayerSessionHelper();
        analyticHelper = new AnalyticHelper(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomPlayer();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(PlayerActionHelper.BROADCAST));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverBroadcastInfo, new IntentFilter(PlayerActionHelper.BROADCAST_INFO));

        if (playerSessionHelper.getPreferences(getApplicationContext(), "isplaying").equals("true")){
            icPlay.setImageResource(R.drawable.ic_pause);
        }else {
            icPlay.setImageResource(R.drawable.ic_play);
        }

        if (playerSessionHelper.getPreferences(getApplicationContext(), "file").isEmpty()){
            contBottomPlayer.setVisibility(View.GONE);
            showBottomPlayer = false;
        }else {
            contBottomPlayer.setVisibility(View.VISIBLE);
            showBottomPlayer = true;
        }

        if (playerSessionHelper.getPreferences(getApplicationContext(), "pause").equals("true")){
            String current_pos = playerSessionHelper.getPreferences(getApplicationContext(), "current_pos");
            if (!TextUtils.isEmpty(current_pos)){
                int pos = Integer.parseInt(current_pos);
                progressBar.setProgress(pos);
            }
            String duration = playerSessionHelper.getPreferences(getApplicationContext(), "duration");
            if (!TextUtils.isEmpty(duration)){
                int dur = Integer.parseInt(duration);
                progressBar.setMax(dur);
            }

        }
    }

    public void bottomPlayer(){
        title.setText(playerSessionHelper.getPreferences(getApplicationContext(), "title"));
        artist.setText(playerSessionHelper.getPreferences(getApplicationContext(), "subtitle"));
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyticHelper.playerToogle("Up");
                openPlayerActivity();
            }
        });
        layoutBottomPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlayerActivity();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playerSessionHelper.getPreferences(getApplicationContext(), "isplaying").equals("true")){
                    playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "true");
                    icPlay.setImageResource(R.drawable.ic_pause);
                    Intent intent = new Intent(BottomPlayerActivity.this, PlayerService.class);
                    intent.setAction(PlayerActionHelper.ACTION_PLAY);
                    startService(intent);
                }else {
                    playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
                    icPlay.setImageResource(R.drawable.ic_play);
                    Intent intent = new Intent(BottomPlayerActivity.this, PlayerService.class);
                    intent.setAction(PlayerActionHelper.ACTION_PAUSE);
                    startService(intent);
                }
            }
        });
    }

    private void openPlayerActivity() {
        Intent intent = new Intent(BottomPlayerActivity.this, Player.class);
        startActivity(intent);
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            contBottomPlayer.setVisibility(View.VISIBLE);
            duration = intent.getIntExtra(PlayerActionHelper.BROADCAST_MAX_DURATION, 100);
            progress = intent.getIntExtra(PlayerActionHelper.BROADCAST_CURRENT_DURATION, 0);

            progressBar.setMax(duration);
            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(progress);
                }
            });

            if (progress==duration){
                icPlay.setImageResource(R.drawable.ic_play);
            }else if (playerSessionHelper.getPreferences(getApplicationContext(), "isplaying").equals("true")){
                icPlay.setImageResource(R.drawable.ic_pause);
            }
        }
    };

    public BroadcastReceiver receiverBroadcastInfo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            icPlay.setImageResource(R.drawable.ic_pause);
            title.setText(intent.getStringExtra(PlayerActionHelper.BROADCAST_TITLE));
            artist.setText(intent.getStringExtra(PlayerActionHelper.BROADCAST_SUBTITLE));
        }
    };
}
