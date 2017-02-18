package sangmaneproject.kindis;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.PlayerActionHelper;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{
    MediaPlayer mediaPlayer = null;
    String file;

    public PlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(PlayerActionHelper.UPDATE_RESOURCE)){
            startMediaPlayer(intent.getStringExtra("single_id"));
        }

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
            Log.d("playerservice", "onprepared");
            sendBroadcest(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
            updateProgressBar();
            mediaPlayer.setOnCompletionListener(this);
        }
    }

    private void updateProgressBar(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                while (new PlayerSessionHelper().getPreferences(getApplicationContext(), "isplaying").equals("true")){
                    sendBroadcest(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
                    Log.d("playerservice", "updateprogres");
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
        Log.d("playerservice", "onCompleted");
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

    private void playMediaPlayer(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        String song = new PlayerSessionHelper().getPreferences(getApplicationContext(), "file").replace(" ", "%20");
        try {
            mediaPlayer.setDataSource(song);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync();
        onPrepared(mediaPlayer);
        new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "true");
    }

    private void startMediaPlayer(String singgleId){
        Map<String, String> param = new HashMap<String, String>();
        param.put("single_id", singgleId);

        new VolleyHelper().post(ApiHelper.ITEM_SINGLE, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    Log.d("songplayresponse", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");
                            if (!result.getString("file").equals("null")){
                                new PlayerSessionHelper().setPreferences(getApplicationContext(), "title", result.getString("title"));
                                new PlayerSessionHelper().setPreferences(getApplicationContext(), "album", result.getString("album"));
                                new PlayerSessionHelper().setPreferences(getApplicationContext(), "file", result.getString("file"));
                                playMediaPlayer();
                            }else {
                                Toast.makeText(getApplicationContext(), "Song can't played", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
