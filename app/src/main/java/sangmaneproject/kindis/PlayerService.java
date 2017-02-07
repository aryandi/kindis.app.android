package sangmaneproject.kindis;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import sangmaneproject.kindis.helper.PlayerActionHelper;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener{
    MediaPlayer mediaPlayer = null;

    public PlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String song = "https://s3-ap-southeast-1.amazonaws.com/kindis.co/single/2017/01/15/Seventeen/transcoder/hls_de356622f70fd59b8e5e9341288692c1.m3u8";
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(song);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(PlayerActionHelper.ACTION_PLAY)){
            onPrepared(mediaPlayer);
        }

        if (intent.getAction().equals(PlayerActionHelper.ACTION_PAUSE)){
            mediaPlayer.pause();
            updatesProgressBar(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
        }

        /*while (mediaPlayer.isPlaying()){
            updatesProgressBar(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }



    private void updatesProgressBar(int duration, int current) {
        Log.d("kontolsender", duration + " : " + current);
        Intent intent = new Intent(PlayerActionHelper.BROADCAST);
        intent.putExtra(PlayerActionHelper.BROADCAST_MAX_DURATION, duration);
        intent.putExtra(PlayerActionHelper.BROADCAST_CURRENT_DURATION, current);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
