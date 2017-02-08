package sangmaneproject.kindis;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import sangmaneproject.kindis.helper.PlayerActionHelper;
import sangmaneproject.kindis.helper.PlayerSessionHelper;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{
    MediaPlayer mediaPlayer = null;
    Handler mHandler;

    public PlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
        String song = "https://s3-ap-southeast-1.amazonaws.com/kindis.co/single/2017/01/15/Seventeen/transcoder/hls_de356622f70fd59b8e5e9341288692c1.m3u8";
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(song);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.prepareAsync();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(PlayerActionHelper.ACTION_PLAY)){
            onPrepared(mediaPlayer);
            new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "true");
        }

        if (intent.getAction().equals(PlayerActionHelper.ACTION_PAUSE)){
            mediaPlayer.pause();
            sendBroadcest(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
            new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "false");
            updateProgressBar();
        }

        if (intent.getAction().equals(PlayerActionHelper.ACTION_SEEK)){
            mediaPlayer.seekTo(intent.getIntExtra("progress", mediaPlayer.getCurrentPosition()));
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        if (mediaPlayer.isPlaying()){
            sendBroadcest(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
            updateProgressBar();
        }
    }

    private void updateProgressBar(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                while (new PlayerSessionHelper().getPreferences(getApplicationContext(), "isplaying").equals("true")){
                    sendBroadcest(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }.execute();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        sendBroadcest(mp.getDuration(), mp.getDuration());
        new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "false");
    }

    private void sendBroadcest(int duration, int current) {
        Log.d("kontolsender", duration + " : " + current);
        Intent intent = new Intent(PlayerActionHelper.BROADCAST);
        intent.putExtra(PlayerActionHelper.BROADCAST_MAX_DURATION, duration);
        intent.putExtra(PlayerActionHelper.BROADCAST_CURRENT_DURATION, current);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
