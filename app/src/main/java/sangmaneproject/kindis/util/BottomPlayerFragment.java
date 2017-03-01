package sangmaneproject.kindis.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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

public class BottomPlayerFragment extends Fragment {
    ImageButton expand;
    RelativeLayout btnPlay;
    ImageView icPlay;
    ProgressBar progressBar;
    int duration;
    int progress;
    TextView title, artist;
    LinearLayout contBottomPlayer;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expand = (ImageButton) view.findViewById(R.id.btn_expand);
        btnPlay = (RelativeLayout) view.findViewById(R.id.btn_play);
        icPlay = (ImageView) view.findViewById(R.id.ic_play);
        progressBar = (ProgressBar) view.findViewById(R.id.pb);
        title = (TextView) view.findViewById(R.id.title_player);
        artist = (TextView) view.findViewById(R.id.artist_player);
        contBottomPlayer = (LinearLayout) view.findViewById(R.id.cont_bottom_player);
    }

    @Override
    public void onResume() {
        super.onResume();
        bottomPlayer();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(PlayerActionHelper.BROADCAST));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiverBroadcastInfo, new IntentFilter(PlayerActionHelper.BROADCAST_INFO));

        if (new PlayerSessionHelper().getPreferences(getContext(), "isplaying").equals("true")){
            icPlay.setImageResource(R.drawable.ic_pause);
        }else {
            icPlay.setImageResource(R.drawable.ic_play);
        }

        if (new PlayerSessionHelper().getPreferences(getContext(), "file").isEmpty()){
            contBottomPlayer.setVisibility(View.GONE);
        }

        if (new PlayerSessionHelper().getPreferences(getContext(), "pause").equals("true")){
            int pos = Integer.parseInt(new PlayerSessionHelper().getPreferences(getContext(), "current_pos"));
            int dur = Integer.parseInt(new PlayerSessionHelper().getPreferences(getContext(), "duration"));
            progressBar.setMax(dur);
            progressBar.setProgress(pos);
        }
    }

    public void bottomPlayer(){
        title.setText(new PlayerSessionHelper().getPreferences(getContext(), "title"));
        artist.setText(new PlayerSessionHelper().getPreferences(getContext(), "album"));
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Player.class);
                startActivity(intent);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!new PlayerSessionHelper().getPreferences(getContext(), "isplaying").equals("true")){
                    new PlayerSessionHelper().setPreferences(getContext(), "isplaying", "true");
                    icPlay.setImageResource(R.drawable.ic_pause);
                    Intent intent = new Intent(getActivity(), PlayerService.class);
                    intent.setAction(PlayerActionHelper.ACTION_PLAY);
                    getActivity().startService(intent);
                }else {
                    new PlayerSessionHelper().setPreferences(getContext(), "isplaying", "false");
                    icPlay.setImageResource(R.drawable.ic_play);
                    Intent intent = new Intent(getActivity(), PlayerService.class);
                    intent.setAction(PlayerActionHelper.ACTION_PAUSE);
                    getActivity().startService(intent);
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
