package sangmaneproject.kindis;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.PlayerActionHelper;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.util.BackgroundProses.GetBitmapImage;
import sangmaneproject.kindis.util.BackgroundProses.ParseJsonPlaylist;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener{
    PlayerSessionHelper playerSessionHelper;
    MediaPlayer mediaPlayer = null;
    MediaSession mediaSession;

    Notification.Builder noti;
    NotificationManager notificationManager;

    ParseJsonPlaylist parseJsonPlaylist;
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

        mediaSession = new MediaSession(this, "debug tag");
        mediaSession.setMetadata(new MediaMetadata.Builder().build());
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        playerSessionHelper = new PlayerSessionHelper();

        parseJsonPlaylist = new ParseJsonPlaylist(getApplicationContext());
        if (playerSessionHelper.getPreferences(getApplicationContext(), "index").equals("1")){
            songPlaylist = new ArrayList<>();;
        }else {
            songPlaylist = parseJsonPlaylist.getSongPlaylist();

            if (!playerSessionHelper.getPreferences(getApplicationContext(), "playlistPosition").isEmpty()){
                playlistPosition = Integer.parseInt(playerSessionHelper.getPreferences(getApplicationContext(), "playlistPosition"));
            }
        }

        notification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(PlayerActionHelper.UPDATE_RESOURCE)){
            songPlaylist = new ArrayList<>();
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
            }
            getSongResources(intent.getStringExtra("single_id"));
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
            playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
            playerSessionHelper.setPreferences(getApplicationContext(), "pause", "true");
            playerSessionHelper.setPreferences(getApplicationContext(), "current_pos", ""+mediaPlayer.getCurrentPosition());
            updateProgressBar();
        }

        if (intent.getAction().equals(PlayerActionHelper.ACTION_SEEK)){
            mediaPlayer.seekTo(intent.getIntExtra("progress", mediaPlayer.getCurrentPosition()));
        }

        if (intent.getAction().equals(PlayerActionHelper.PLAY_MULTYSOURCE)){
            playlistPosition = 0;
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
            }
            getSongResources(intent.getStringExtra("single_id"));
            songPlaylist = intent.getStringArrayListExtra("list_uid");
        }

        if (intent.getAction().equals(PlayerActionHelper.ACTION_PLAYBACK)){
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }else {
                if (isDataSources){
                    onPrepared(mediaPlayer);
                }else {
                    playMediaPlayer();
                }
            }
        }

        if (intent.getAction().equals(PlayerActionHelper.PLAY_PLAYLIST)){
            playlistPosition = intent.getIntExtra("position", 0);
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
                playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
            }
            getSongResources(intent.getStringExtra("single_id"));
            songPlaylist = intent.getStringArrayListExtra("list_uid");
        }

        if (intent.getAction().equals(PlayerActionHelper.ACTION_NEXT)){
            playNext();
        }

        if (intent.getAction().equals(PlayerActionHelper.ACTION_PREV)){
            playBack();
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
        String isLooping = ""+playerSessionHelper.getPreferences(getApplicationContext(), "isLooping");
        playerSessionHelper.setPreferences(getApplicationContext(), "playlistPosition", ""+playlistPosition);
        Log.d("islooping", isLooping);
        if (isLooping.equals("true")){
            mp.setLooping(true);
        }else {
            mp.setLooping(false);
        }
        mp.start();
        playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "true");
        playerSessionHelper.setPreferences(getApplicationContext(), "pause", "false");
        if (mp.isPlaying()){
            playerSessionHelper.setPreferences(getApplicationContext(), "duration", ""+mp.getDuration());
            sendBroadcest(mp.getDuration(), mp.getCurrentPosition());
            updateProgressBar();
            mp.setOnCompletionListener(this);
        }
    }

    private void updateProgressBar(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                while (playerSessionHelper.getPreferences(getApplicationContext(), "isplaying").equals("true")){
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
            playNext();
        }else {
            Log.d("playerservice", "false");
            sendBroadcest(mp.getDuration(), mp.getDuration());
            playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
            mp.release();
            stopSelf();
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        if (i!=-38){
            playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
            Log.d("playerservice", "error "+i);
            Toast.makeText(getApplicationContext(), "Something Error", Toast.LENGTH_SHORT).show();

            if (cekSizePlaylist()){
                playNext();
            }else {
                mediaPlayer.reset();
            }

            return false;
        }else {
            return true;
        }
    }

    private void sendBroadcest(int duration, int current) {
        Intent intent = new Intent(PlayerActionHelper.BROADCAST);
        intent.putExtra(PlayerActionHelper.BROADCAST_MAX_DURATION, duration);
        intent.putExtra(PlayerActionHelper.BROADCAST_CURRENT_DURATION, current);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendBroadcestInfo(String title, String subtitle, int playistPos) {
        Intent intent = new Intent(PlayerActionHelper.BROADCAST_INFO);
        intent.putExtra(PlayerActionHelper.BROADCAST_TITLE, title);
        intent.putExtra(PlayerActionHelper.BROADCAST_SUBTITLE, subtitle);
        intent.putExtra(PlayerActionHelper.BROADCAST_POSITION, playistPos);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void playMediaPlayer(){
        isDataSources = true;
        String song = playerSessionHelper.getPreferences(getApplicationContext(), "file").replace(" ", "%20");
        Log.d("songresource", song);

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(song);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnErrorListener(this);
    }

    private void getSongResources (String uid){
        Log.d("playerservice", "getSongResource");
        Map<String, String> param = new HashMap<String, String>();
        param.put("single_id", uid);
        playerSessionHelper.setPreferences(getApplicationContext(), "uid", uid);
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
                                playerSessionHelper.setPreferences(getApplicationContext(), "title", result.getString("title"));
                                playerSessionHelper.setPreferences(getApplicationContext(), "subtitle", result.getString("artist") +" | "+result.getString("album"));
                                playerSessionHelper.setPreferences(getApplicationContext(), "album", result.getString("album"));
                                playerSessionHelper.setPreferences(getApplicationContext(), "file", result.getString("file"));
                                playerSessionHelper.setPreferences(getApplicationContext(), "image", result.getString("image"));
                                sendBroadcestInfo(result.getString("title"), result.getString("album"), playlistPosition);
                                Log.d("titlesongplay", result.getString("title"));
                                playMediaPlayer();
                            }else {
                                Toast.makeText(getApplicationContext(), "Song can't played", Toast.LENGTH_SHORT).show();
                                playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
                                if (cekSizePlaylist()){
                                    playNext();
                                }
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
                        Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (noti != null){
            updateNotification();
        }
    }

    private boolean cekSizePlaylist(){
        if (playlistPosition < songPlaylist.size()-1){
            return true;
        }else {
            return false;
        }
    }

    private void playNext(){
        playlistPosition++;
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        getSongResources(songPlaylist.get(playlistPosition));
    }

    private void playBack(){
        Log.d("playerservice", "playNext");
        Log.d("playerservice", ""+playlistPosition);

        playlistPosition--;

        Log.d("playerservice", ""+playlistPosition);
        Log.d("playerservice", ""+songPlaylist.size());

        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        getSongResources(songPlaylist.get(playlistPosition));
    }

    private void notification(){
        new GetBitmapImage(getApplicationContext(), ApiHelper.BASE_URL_IMAGE + playerSessionHelper.getPreferences(getApplicationContext(), "image"), new GetBitmapImage.OnFetchFinishedListener() {
            @Override
            public void onFetchFinished(Bitmap bitmap) {
                System.out.println("kontolnotif"+bitmap);
                noti = new Notification.Builder(getApplicationContext())
                        .setShowWhen(false)
                        .setStyle(new Notification.MediaStyle()
                                .setMediaSession(mediaSession.getSessionToken())
                                .setShowActionsInCompactView(0, 1, 2))
                        .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                        .setSmallIcon(R.drawable.ic_play)
                        .setLargeIcon(bitmap)
                        .setContentText(playerSessionHelper.getPreferences(getApplicationContext(), "subtitle"))
                        .setContentTitle(playerSessionHelper.getPreferences(getApplicationContext(), "title"))
                        .addAction(R.drawable.ic_back, "prev", retreivePlaybackAction(3))
                        .addAction(R.drawable.ic_pause, "pause", retreivePlaybackAction(1))
                        .addAction(R.drawable.ic_next, "next", retreivePlaybackAction(2))
                        .setOngoing(true);

                notificationManager.notify(1, noti.build());
            }
        }).execute();
    }

    private void updateNotification(){
        new GetBitmapImage(getApplicationContext(), ApiHelper.BASE_URL_IMAGE + playerSessionHelper.getPreferences(getApplicationContext(), "image"), new GetBitmapImage.OnFetchFinishedListener() {
            @Override
            public void onFetchFinished(Bitmap bitmap) {
                System.out.println("kontolnotifupdate"+bitmap);
                noti.setContentText(playerSessionHelper.getPreferences(getApplicationContext(), "subtitle"))
                .setContentTitle(playerSessionHelper.getPreferences(getApplicationContext(), "title"))
                .setLargeIcon(bitmap);

        notificationManager.notify(1, noti.build());
            }
        }).execute();

    }

    private PendingIntent retreivePlaybackAction(int which) {
        Intent action;
        PendingIntent pendingIntent;
        final ComponentName serviceName = new ComponentName(this, PlayerService.class);
        switch (which) {
            case 1:
                action = new Intent(PlayerActionHelper.ACTION_PLAYBACK);
                action.setComponent(serviceName);
                pendingIntent = PendingIntent.getService(this, 1, action, 0);
                return pendingIntent;
            case 2:
                if (cekSizePlaylist()){
                    action = new Intent(PlayerActionHelper.ACTION_NEXT);
                    action.setComponent(serviceName);
                    pendingIntent = PendingIntent.getService(this, 2, action, 0);
                    return pendingIntent;
                }
            case 3:
                if (playlistPosition!=0){
                    action = new Intent(PlayerActionHelper.ACTION_PREV);
                    action.setComponent(serviceName);
                    pendingIntent = PendingIntent.getService(this, 3, action, 0);
                    return pendingIntent;
                }
            default:
                break;
        }
        return null;
    }
}
