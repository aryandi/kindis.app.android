package co.digdaya.kindis.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.digdaya.kindis.databse.KindisDBHelper;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.Constanta;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.model.DownloadAlbumModel;

public class DownloadService extends Service {
    SessionHelper sessionHelper;
    VolleyHelper volleyHelper;
    DownloadManager downloadManager;
    String uid;
    String dir;
    JSONObject result;
    Gson gson;
    DownloadAlbumModel downloadAlbumModel;
    List<Long> listDownloadID = new ArrayList<>();
    KindisDBHelper kindisDBHelper;

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sessionHelper = new SessionHelper();
        volleyHelper = new VolleyHelper();
        gson = new Gson();
        kindisDBHelper = new KindisDBHelper(getApplicationContext());
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dir = getApplicationContext().getExternalFilesDir("single/").toString()+"/";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String data;
        switch (intent.getAction()){
            case Constanta.INTENT_ACTION_DOWNLOAD_SINGLE:
                //singlePath = path+"single/";
                //createSubFolder(singlePath);
                uid = intent.getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_SINGLE_ID);
                data = "[{\"single_id\":"+uid+"}]";
                getToken("1", data);
                break;
            case Constanta.INTENT_ACTION_DOWNLOAD_ALBUM:
                uid = intent.getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_ALBUM_ID);
                data = "[{\"album_id\":"+uid+"}]";
                getToken("2", data);
                break;
            case Constanta.INTENT_ACTION_DOWNLOAD_PLAYLIST:
                uid = intent.getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_PLAYLIST_ID);
                data = "[{\"playlist_id\":"+uid+"}]";
                getToken("3", data);
                break;
        }
        return START_STICKY;
    }

    private void getToken(final String type, String data){
        HashMap<String, String> param = new HashMap<>();
        param.put("uid", sessionHelper.getPreferences(getApplicationContext(), "user_id"));
        param.put("token_access", sessionHelper.getPreferences(getApplicationContext(), "token_access"));
        param.put("dev_id", "2");
        param.put("type_id", type);
        param.put("data", data);
        System.out.println("downloadresponses param: "+data);

        volleyHelper.post(ApiHelper.TOKEN, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                System.out.println("downloadresponses token: "+response);
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");
                            getData(result.getString("token"), result.getString("data"), Integer.parseInt(type));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getData(String token, String data, final int type){
        HashMap<String, String> param = new HashMap<>();
        param.put("uid", sessionHelper.getPreferences(getApplicationContext(), "user_id"));
        param.put("token_access", sessionHelper.getPreferences(getApplicationContext(), "token_access"));
        param.put("dev_id", "2");
        param.put("token", token);
        param.put("data", data);

        volleyHelper.post(ApiHelper.GET_DATA, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                System.out.println("downloadresponses data: "+response);
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            result = object.getJSONObject("result");
                            switch (type){
                                case 1:
                                    downloadRequestSingle(Uri.parse(ApiHelper.BASE_URL_IMAGE+"/"+result.getString("file")), result.getString("title"));
                                    break;
                                case 2:
                                    downloadAlbumModel = gson.fromJson(response, DownloadAlbumModel.class);
                                    downloadRequestAlbum();
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void downloadRequestSingle(Uri file, String path){
        DownloadManager.Request request = new DownloadManager.Request(file);
        request.setTitle("Kindis");
        request.setDescription("Downloading "+path);
        request.setDestinationInExternalFilesDir(getApplicationContext(), "single/", path);
        long enque = downloadManager.enqueue(request);
        registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void downloadRequestAlbum(){
        kindisDBHelper.addToTableAlbum(
                uid,
                downloadAlbumModel.result.summary.album,
                downloadAlbumModel.result.summary.artist,
                downloadAlbumModel.result.summary.album_desc,
                downloadAlbumModel.result.summary.image,
                downloadAlbumModel.result.summary.banner_image
        );
        int index = 0;
        do {
            Uri file = Uri.parse(ApiHelper.BASE_URL_IMAGE+"/"+downloadAlbumModel.result.offline_single.get(index).file);
            String path = downloadAlbumModel.result.offline_single.get(index).title;
            DownloadManager.Request request = new DownloadManager.Request(file);
            request.setTitle("Kindis");
            request.setDescription("Downloading "+path);
            request.setDestinationInExternalFilesDir(getApplicationContext(), "single/", path);
            long enque = downloadManager.enqueue(request);
            listDownloadID.add(enque);
            System.out.println("broadcastReceiverAlbum enque: "+enque);
            registerReceiver(broadcastReceiverAlbum, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            index++;
            System.out.println("downloadRequestAlbum: "+index);
        }while (index!=downloadAlbumModel.result.offline_single.size());

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                try {
                    kindisDBHelper.addToTableSingle(
                            uid,
                            result.getString("title"),
                            dir+result.getString("title"),
                            result.getString("image"),
                            result.getString("album"),
                            result.getString("artist"),
                            result.getString("artist_id"),
                            "0"
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private BroadcastReceiver broadcastReceiverAlbum = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            Long downloaded_id = extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID);
            int index = listDownloadID.indexOf(downloaded_id);
            kindisDBHelper.addToTableSingle(
                    uid+index,
                    downloadAlbumModel.result.offline_single.get(index).title,
                    dir+downloadAlbumModel.result.offline_single.get(index).title,
                    downloadAlbumModel.result.offline_single.get(index).image,
                    downloadAlbumModel.result.offline_single.get(index).album,
                    downloadAlbumModel.result.offline_single.get(index).artist,
                    downloadAlbumModel.result.offline_single.get(index).artist_id,
                    uid
            );
        }
    };
}
