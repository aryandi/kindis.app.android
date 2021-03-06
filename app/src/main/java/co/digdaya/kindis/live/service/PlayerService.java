package co.digdaya.kindis.live.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.database.KindisDBHelper;
import co.digdaya.kindis.live.database.KindisDBname;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.CheckAppRunning;
import co.digdaya.kindis.live.helper.Constanta;
import co.digdaya.kindis.live.helper.PlayerActionHelper;
import co.digdaya.kindis.live.helper.PlayerSessionHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.util.BackgroundProses.ParseJsonPlaylist;
import co.digdaya.kindis.live.view.activity.Player.Player;

public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener{
    PlayerSessionHelper playerSessionHelper;
    SessionHelper sessionHelper;
    VolleyHelper volleyHelper;
    MediaPlayer mediaPlayer = null;

    Notification.Builder noti;
    NotificationManager notificationManager;
    RemoteViews views;

    ParseJsonPlaylist parseJsonPlaylist;
    boolean isDataSources = false;

    ArrayList<String> songPlaylist = new ArrayList<>();
    int playlistPosition = 0;

    String title, subtitle;
    int countAds;

    public PlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();

        playerSessionHelper = new PlayerSessionHelper();
        sessionHelper = new SessionHelper();
        volleyHelper = new VolleyHelper();

        parseJsonPlaylist = new ParseJsonPlaylist(getApplicationContext(), false);
        if (playerSessionHelper.getPreferences(getApplicationContext(), "index").equals("1")){
            songPlaylist = new ArrayList<>();
        }else {
            songPlaylist = parseJsonPlaylist.getSongPlaylist();
            if (!playerSessionHelper.getPreferences(getApplicationContext(), "playlistPosition").isEmpty()){
                playlistPosition = Integer.parseInt(playerSessionHelper.getPreferences(getApplicationContext(), "playlistPosition"));
            }
        }

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        views = new RemoteViews(getPackageName(), R.layout.layout_notification);
        views.setImageViewResource(R.id.image, R.drawable.ic_launcher);
        views.setTextViewText(R.id.title, playerSessionHelper.getPreferences(getApplicationContext(), "title"));
        views.setTextViewText(R.id.subtitle, playerSessionHelper.getPreferences(getApplicationContext(), "subtitle"));
        views.setOnClickPendingIntent(R.id.btn_play, retreivePlaybackAction(1));
        views.setOnClickPendingIntent(R.id.btn_next, retreivePlaybackAction(2));
        views.setOnClickPendingIntent(R.id.btn_close, retreivePlaybackAction(3));
        views.setOnClickPendingIntent(R.id.notif_body, retreivePlaybackAction(4));

        notification();

        countAds = new Random().nextInt(6)+1;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        parseJsonPlaylist = new ParseJsonPlaylist(getApplicationContext(), false);

        if (intent!=null){
            String single_id = intent.getStringExtra("single_id");
            playerSessionHelper.setPreferences(getApplicationContext(), "single_id", single_id);
            switch (intent.getAction()){
                case PlayerActionHelper.UPDATE_RESOURCE:
                    playerSessionHelper.setPreferences(getApplicationContext(), "is_offline_mode", "false");
                    songPlaylist = new ArrayList<>();
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
                    }
                    System.out.println("getSongResources update");
                    getSongResources(single_id);
                    break;
                case PlayerActionHelper.ACTION_PLAY:
                    Boolean isOfflineMode = Boolean.parseBoolean(playerSessionHelper.getPreferences(getApplicationContext(), "is_offline_mode"));
                    if (isDataSources){
                        onPrepared(mediaPlayer);
                    }else {
                        if (isOfflineMode){
                            playOffline();
                        }else {
                            playMediaPlayer();
                        }
                    }
                    views.setImageViewResource(R.id.btn_play, R.drawable.ic_pause);
                    notificationManager.notify(1, noti.build());
                    break;
                case PlayerActionHelper.ACTION_PAUSE:
                    mediaPlayer.pause();
                    sendBroadcest(mediaPlayer.getDuration(), mediaPlayer.getCurrentPosition());
                    playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
                    playerSessionHelper.setPreferences(getApplicationContext(), "pause", "true");
                    playerSessionHelper.setPreferences(getApplicationContext(), "current_pos", ""+mediaPlayer.getCurrentPosition());
                    updateProgressBar();

                    views.setImageViewResource(R.id.btn_play, R.drawable.ic_play);
                    notificationManager.notify(1, noti.build());
                    break;
                case PlayerActionHelper.ACTION_SEEK:
                    mediaPlayer.seekTo(intent.getIntExtra("progress", mediaPlayer.getCurrentPosition()));
                    break;
                case PlayerActionHelper.PLAY_MULTYSOURCE:
                    System.out.println("playall intent: "+ single_id);
                    playlistPosition = 0;
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
                    }
                    songPlaylist = intent.getStringArrayListExtra("list_uid");
                    System.out.println("getSongResources multi");
                    getSongResources(single_id);
                    break;
                case PlayerActionHelper.ACTION_PLAYBACK:
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        views.setImageViewResource(R.id.btn_play, R.drawable.ic_play);
                    }else {
                        views.setImageViewResource(R.id.btn_play, R.drawable.ic_pause);
                        if (isDataSources){
                            onPrepared(mediaPlayer);
                        }else {
                            playMediaPlayer();
                        }
                    }
                    notificationManager.notify(1, noti.build());
                    break;
                case PlayerActionHelper.PLAY_PLAYLIST:
                    System.out.println("nextplay action: "+intent.getStringArrayListExtra("list_uid"));
                    playlistPosition = intent.getIntExtra("position", 0);
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
                    }
                    songPlaylist = intent.getStringArrayListExtra("list_uid");
                    System.out.println("getSongResources playlist");
                    getSongResources(single_id);
                    break;
                case PlayerActionHelper.ACTION_NEXT:
                    if (cekSizePlaylist()){
                        playNext();
                    }
                    break;
                case PlayerActionHelper.ACTION_CLOSE:
                    notificationManager.cancel(1);
                    if (!CheckAppRunning.isAppRunning(getApplicationContext())){
                        System.out.println("isAppRunning false");
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                    }else {
                        System.out.println("isAppRunning true");
                    }
                    break;
                case PlayerActionHelper.ACTION_LOG_OUT:
                    notificationManager.cancel(1);
                    mediaPlayer.pause();
                    System.out.println("ACTION_LOG_OUT ok");
                    break;
                case PlayerActionHelper.ACTION_OPEN:
                    Intent player = new Intent(this, Player.class);
                    player.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(player);
                    break;
                case PlayerActionHelper.ACTION_LOOPING:
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                        onPrepared(mediaPlayer);
                    }
                    break;
                case PlayerActionHelper.ACTION_PLAY_OFFLINE:
                    playerSessionHelper.setPreferences(getApplicationContext(), "is_offline_mode", "true");
                    songPlaylist = new ArrayList<>();
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
                    }
                    playerSessionHelper.setPreferences(getApplicationContext(), "file", intent.getStringExtra("songresource"));
                    playOffline();
                    break;
                case PlayerActionHelper.ACTION_PLAY_OFFLINE_ALL:
                    playlistPosition = 0;
                    playerSessionHelper.setPreferences(getApplicationContext(), "is_offline_mode", "true");
                    songPlaylist = new ArrayList<>();
                    songPlaylist = intent.getStringArrayListExtra("list_uid");
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
                    }
                    playerSessionHelper.setPreferences(getApplicationContext(), "file", intent.getStringExtra("songresource"));
                    playOffline();
                    break;
                case PlayerActionHelper.ACTION_PLAY_OFFLINE_NEXT:
                    playerSessionHelper.setPreferences(getApplicationContext(), "is_offline_mode", "true");
                    songPlaylist = intent.getStringArrayListExtra("songPlaylist");
                    playlistPosition = intent.getIntExtra("position", playlistPosition);
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
                    }
                    getSongOffline();
                    break;
                case PlayerActionHelper.ACTION_SHUFFLE:
                    playlistPosition = 0;
                    songPlaylist = new ArrayList<>();
                    songPlaylist = intent.getStringArrayListExtra("list_uid");
                    System.out.println("ACTION_SHUFFLE: "+songPlaylist);
                    break;
            }
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        notificationManager.cancel(1);
        stopSelf();
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

    private void sendBroadcestInfo(String title, String subtitle, int playlistPos) {
        Intent intent = new Intent(PlayerActionHelper.BROADCAST_INFO);
        intent.putExtra(PlayerActionHelper.BROADCAST_TITLE, title);
        intent.putExtra(PlayerActionHelper.BROADCAST_SUBTITLE, subtitle);
        intent.putExtra(PlayerActionHelper.BROADCAST_POSITION, playlistPos);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void playMediaPlayer(){
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isDataSources = true;
        String song = playerSessionHelper.getPreferences(getApplicationContext(), "file").replace(" ", "%20");

        try {
            mediaPlayer.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        try {
            mediaPlayer.setDataSource(song);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSongResources(final String single_id) {

        String expiredIn = sessionHelper.getPreferences(getApplicationContext(), "expires_in");
        long currentTimeMillis = System.currentTimeMillis()/1000;
        if (!TextUtils.isEmpty(expiredIn)) {
            if (currentTimeMillis > Long.parseLong(expiredIn)) {
                refreshToken(single_id);
                return;
            }
        }
        System.out.println("getSongResources real");
        System.out.println("nextplay getSongResources: "+single_id);
        System.out.println("playall getSongResources: "+single_id);
        Map<String, String> param = new HashMap<String, String>();
        param.put("single_id", single_id);
        param.put("uid", sessionHelper.getPreferences(getApplicationContext(), "user_id"));
        param.put("token_access", sessionHelper.getPreferences(getApplicationContext(), "token_access"));

        System.out.println("Paramssongresource : "+single_id+"\n"+sessionHelper.getPreferences(getApplicationContext(), "user_id")+"\n"+sessionHelper.getPreferences(getApplicationContext(), "token_access"));

        playerSessionHelper.setPreferences(getApplicationContext(), "single_id", single_id);
        volleyHelper.post(ApiHelper.ITEM_SINGLE, param, new VolleyHelper.HttpListener<String>() {
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
                                playerSessionHelper.setPreferences(getApplicationContext(), "artist_id", result.getString("artist_id"));
                                playerSessionHelper.setPreferences(getApplicationContext(), "share_link", result.getString("share_link"));
                                playerSessionHelper.setPreferences(getApplicationContext(), Constanta.PLAYERSESSION_ALBUM_ID, result.getString("album_id"));
                                if (sessionHelper.getPreferences(getApplicationContext(), "is_premium").equals(result.getString("is_premium")) || sessionHelper.getPreferences(getApplicationContext(), "is_premium").equals("1")){
                                    playMediaPlayer();
                                    title = result.getString("title");
                                    subtitle = result.getString("artist") +" | "+result.getString("album");
                                    sendBroadcestInfo(title, subtitle, playlistPosition);
                                    if (noti != null) {
                                        updateNotification();
                                    }
                                }else {
                                    if (cekSizePlaylist()){
                                        playNext();
                                    }
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "Song can't played", Toast.LENGTH_SHORT).show();
                                playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
                                if (cekSizePlaylist()){
                                    playNext();
                                }
                            }
                        }else {
//                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                            refreshToken(single_id);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
                        Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    //ToDo create reload
                }
            }
        });
    }

    private void refreshToken(final String single_id) {
        HashMap<String, String> param = new HashMap<>();
        param.put("token_access", sessionHelper.getPreferences(getApplicationContext(), "token_access"));
        param.put("token_refresh", sessionHelper.getPreferences(getApplicationContext(), "token_refresh"));
        param.put("uid", sessionHelper.getPreferences(getApplicationContext(), "user_id"));
        new VolleyHelper().post(ApiHelper.REFRESH_TOKEN, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                Log.d("refreshtoken", response);
                if (status) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
                            JSONObject result = object.getJSONObject("result");
                            sessionHelper.setPreferences(getApplicationContext(), "token_access", result.getString("access_token"));
                            sessionHelper.setPreferences(getApplicationContext(), "refresh_token", result.getString("refresh_token"));
                            sessionHelper.setPreferences(getApplicationContext(), "expires_in", String.valueOf(result.optInt("expires_in")));
                            getSongResources(single_id);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean cekSizePlaylist(){
        return playlistPosition < songPlaylist.size() - 1;
    }

    private void playNext(){
        countAds--;
        System.out.println("playAds: "+countAds);
        if (countAds==0 && sessionHelper.getPreferences(getApplicationContext(), "is_premium").equals("0")){
            playAds();
            countAds = new Random().nextInt(6)+1;
        }else {
            playlistPosition++;
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            if (Boolean.parseBoolean(playerSessionHelper.getPreferences(getApplicationContext(), "is_offline_mode"))){
                getSongOffline();
            }else {
                System.out.println("getSongResources playnext");
                getSongResources(songPlaylist.get(playlistPosition));
            }
        }
    }

    private void notification(){
        noti = new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_play)
                .setContent(views)
                .setOngoing(true);

        notificationManager.notify(1, noti.build());
    }

    private void updateNotification(){
        views.setTextViewText(R.id.title, title);
        views.setTextViewText(R.id.subtitle, subtitle);
        notificationManager.notify(1, noti.build());
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
                action = new Intent(PlayerActionHelper.ACTION_NEXT);
                action.setComponent(serviceName);
                pendingIntent = PendingIntent.getService(this, 2, action, 0);
                return pendingIntent;
            case 3:
                action = new Intent(PlayerActionHelper.ACTION_CLOSE);
                action.setComponent(serviceName);
                pendingIntent = PendingIntent.getService(this, 2, action, 0);
                return pendingIntent;
            case 4:
                action = new Intent(PlayerActionHelper.ACTION_OPEN);
                action.setComponent(serviceName);
                pendingIntent = PendingIntent.getService(this, 4, action, 0);
                return pendingIntent;
            default:
                break;
        }
        return null;
    }

    private void playAds(){
        volleyHelper.get(ApiHelper.ADS_SONG+sessionHelper.getPreferences(getApplicationContext(), "user_id")+"&token_access="+sessionHelper.getPreferences(getApplicationContext(), "token_access"), new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                System.out.println("playAds: "+response);
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");
                            playerSessionHelper.setPreferences(getApplicationContext(), "title", result.getString("title"));
                            playerSessionHelper.setPreferences(getApplicationContext(), "subtitle", result.getString("artist"));
                            playerSessionHelper.setPreferences(getApplicationContext(), "file", result.getString("file"));
                            playerSessionHelper.setPreferences(getApplicationContext(), "image", result.getString("image"));
                            title = result.getString("title");
                            subtitle = result.getString("artist");
                            sendBroadcestInfo(result.getString("title"), result.getString("artist"), playlistPosition);
                            playMediaPlayer();
                        }else {
                            if (cekSizePlaylist()){
                                playNext();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    if (cekSizePlaylist()){
                        playNext();
                    }
                }
            }
        });
    }

    private void playOffline(){
        isDataSources = true;
        FileDescriptor fd;
        mediaPlayer.reset();
        try {
            FileInputStream fis = new FileInputStream(playerSessionHelper.getPreferences(getApplicationContext(), "file"));
            fd = fis.getFD();
            mediaPlayer.setDataSource(fd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnErrorListener(this);

        title = playerSessionHelper.getPreferences(getApplicationContext(), "title");
        subtitle = playerSessionHelper.getPreferences(getApplicationContext(), "subtitle");

        sendBroadcestInfo(title, subtitle, playlistPosition);
        if (noti != null) {
            updateNotification();
        }
    }

    private void getSongOffline(){
        KindisDBHelper kindisDBHelper = new KindisDBHelper(getApplicationContext());
        SQLiteDatabase db = kindisDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + KindisDBname.TABLE_SINGLE + " WHERE " +KindisDBname.COLUMN_ID+ " = " +songPlaylist.get(playlistPosition),null);
        if (cursor.moveToFirst()){
            while (cursor.isAfterLast()==false){
                playerSessionHelper.setPreferences(getApplicationContext(), "file", cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_PATH)));
                playerSessionHelper.setPreferences(getApplicationContext(), "title", cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_TITLE)));
                playerSessionHelper.setPreferences(getApplicationContext(), "subtitle", cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ARTIST)));
                playerSessionHelper.setPreferences(getApplicationContext(), "image", cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_IMAGE)));
                cursor.moveToNext();
            }
        }
        playOffline();
    }
}
