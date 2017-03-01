package sangmaneproject.kindis.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sangmaneproject.kindis.PlayerService;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.PlayerActionHelper;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.view.activity.Player;

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

    public int layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        expand = (ImageButton) findViewById(R.id.btn_expand);
        btnPlay = (RelativeLayout) findViewById(R.id.btn_play);
        icPlay = (ImageView) findViewById(R.id.ic_play);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        title = (TextView) findViewById(R.id.title_player);
        artist = (TextView) findViewById(R.id.artist_player);
        contBottomPlayer = (LinearLayout) findViewById(R.id.cont_bottom_player);
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomPlayer();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter(PlayerActionHelper.BROADCAST));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverBroadcastInfo, new IntentFilter(PlayerActionHelper.BROADCAST_INFO));

        if (new PlayerSessionHelper().getPreferences(getApplicationContext(), "isplaying").equals("true")){
            icPlay.setImageResource(R.drawable.ic_pause);
        }else {
            icPlay.setImageResource(R.drawable.ic_play);
        }

        if (new PlayerSessionHelper().getPreferences(getApplicationContext(), "file").isEmpty()){
            contBottomPlayer.setVisibility(View.GONE);
        }

        if (new PlayerSessionHelper().getPreferences(getApplicationContext(), "pause").equals("true")){
            int pos = Integer.parseInt(new PlayerSessionHelper().getPreferences(getApplicationContext(), "current_pos"));
            int dur = Integer.parseInt(new PlayerSessionHelper().getPreferences(getApplicationContext(), "duration"));
            progressBar.setMax(dur);
            progressBar.setProgress(pos);
        }
    }

    public void bottomPlayer(){
        title.setText(new PlayerSessionHelper().getPreferences(getApplicationContext(), "title"));
        artist.setText(new PlayerSessionHelper().getPreferences(getApplicationContext(), "album"));
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BottomPlayerActivity.this, Player.class);
                startActivity(intent);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!new PlayerSessionHelper().getPreferences(getApplicationContext(), "isplaying").equals("true")){
                    new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "true");
                    icPlay.setImageResource(R.drawable.ic_pause);
                    Intent intent = new Intent(BottomPlayerActivity.this, PlayerService.class);
                    intent.setAction(PlayerActionHelper.ACTION_PLAY);
                    startService(intent);
                }else {
                    new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "false");
                    icPlay.setImageResource(R.drawable.ic_play);
                    Intent intent = new Intent(BottomPlayerActivity.this, PlayerService.class);
                    intent.setAction(PlayerActionHelper.ACTION_PAUSE);
                    startService(intent);
                }
            }
        });
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
