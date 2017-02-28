package sangmaneproject.kindis;

import android.app.Activity;
import android.app.ProgressDialog;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.PlayerActionHelper;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener{
    MediaPlayer mediaPlayer = null;
    boolean isDataSources = false;

    ArrayList<String> songPlaylist = new ArrayList<>();
    int playlistPosition = 0;

    public PlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mediaPlayer.setOnErrorListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(PlayerActionHelper.UPDATE_RESOURCE)){
            //startMediaPlayer(intent.getStringExtra("single_id"));
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "false");
            }
            new getSongResource().execute(intent.getStringExtra("single_id"));
        }

        if (intent.getAction().equals(PlayerActionHelper.ACTION_PLAY)){
            if (isDataSources){
                onPrepared(mediaPlayer);
            }else {
                playMediaPlayer();
            }
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

        if (intent.getAction().equals(PlayerActionHelper.PLAY_MULTYSOURCE)){
            playlistPosition = 0;
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "false");
            }
            new getSongResource().execute(intent.getStringExtra("single_id"));
            songPlaylist = intent.getStringArrayListExtra("list_uid");
        }

        if (intent.getAction().equals(PlayerActionHelper.ACTION_SKIP)){
            Log.d("Positionsebelum", playlistPosition+" : "+songPlaylist.size());
            if (playlistPosition < songPlaylist.size()){
                //playlistPosition = playlistPosition+1;
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "false");
                }
                Log.d("Positionsebelum", playlistPosition+" : "+songPlaylist.size());
                new getSongResource().execute(songPlaylist.get(playlistPosition+1));
            }
        }

        if (intent.getAction().equals(PlayerActionHelper.ACTION_REWIND)){
            Log.d("backsong", "true");
            if (playlistPosition!=0){
                Log.d("backsong", "back");
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "false");
                }
                playlistPosition = playlistPosition - 1;
                Log.d("backsong", playlistPosition+" : "+songPlaylist.size());
                new getSongResource().execute(songPlaylist.get(playlistPosition));
            }
        }

        if (intent.getAction().equals(PlayerActionHelper.ACTION_LOOPING)){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                onPrepared(mediaPlayer);
            }
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        String isLooping = ""+new PlayerSessionHelper().getPreferences(getApplicationContext(), "isLooping");
        Log.d("islooping", isLooping);
        if (isLooping.equals("true")){
            mp.setLooping(true);
        }else {
            mp.setLooping(false);
        }
        mp.start();
        new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "true");
        if (mp.isPlaying()){
            sendBroadcest(mp.getDuration(), mp.getCurrentPosition());
            updateProgressBar();
            mp.setOnCompletionListener(this);
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
        Log.d("playerservice", "onCompleted");
        if (cekSizePlaylist()){
            sendBroadcest(100, 100);
            new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "false");
            mp.release();
            stopSelf();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "false");
        Log.d("playerservice", "error");
        Toast.makeText(getApplicationContext(), "Something Error", Toast.LENGTH_SHORT).show();

        if (cekSizePlaylist()){
            mediaPlayer.reset();
        }

        return false;
    }

    private void sendBroadcest(int duration, int current) {
        Intent intent = new Intent(PlayerActionHelper.BROADCAST);
        intent.putExtra(PlayerActionHelper.BROADCAST_MAX_DURATION, duration);
        intent.putExtra(PlayerActionHelper.BROADCAST_CURRENT_DURATION, current);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendBroadcestInfo(String title, String subtitle) {
        Intent intent = new Intent(PlayerActionHelper.BROADCAST_INFO);
        intent.putExtra(PlayerActionHelper.BROADCAST_TITLE, title);
        intent.putExtra(PlayerActionHelper.BROADCAST_SUBTITLE, subtitle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void playMediaPlayer(){
        isDataSources = true;
        String song = new PlayerSessionHelper().getPreferences(getApplicationContext(), "file").replace(" ", "%20");
        Log.d("songresource", song);
        try {
            mediaPlayer.setDataSource(song);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnErrorListener(this);
    }

    private class getSongResource extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> param = new HashMap<String, String>();
            param.put("single_id", params[0]);
            new PlayerSessionHelper().setPreferences(getApplicationContext(), "uid", params[0]);
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
                                    sendBroadcestInfo(result.getString("title"), result.getString("album"));
                                    Log.d("titlesongplay", result.getString("title"));
                                    mediaPlayer.reset();
                                    playMediaPlayer();
                                }else {
                                    Toast.makeText(getApplicationContext(), "Song can't played", Toast.LENGTH_SHORT).show();
                                    new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "false");
                                    cekSizePlaylist();
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "false");
                            Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            return null;
        }
    }

    private boolean cekSizePlaylist(){
        if (playlistPosition < songPlaylist.size()){
            Log.d("playlistPosition", ""+playlistPosition);
            Log.d("playlistPosition", ""+playlistPosition);
            Log.d("playlistPosition", ""+songPlaylist.size());
            playlistPosition++;
            new getSongResource().execute(songPlaylist.get(playlistPosition));
            return false;
        }else {
            return true;
        }
    }
}
