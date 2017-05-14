package co.digdaya.kindis.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import co.digdaya.kindis.databse.KindisDBHelper;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.ExtraKey;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;

public class DownloadService extends Service {
    SessionHelper sessionHelper;
    VolleyHelper volleyHelper;
    DownloadManager downloadManager;
    String uid;
    String dir;
    JSONObject result;

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
        dir = getApplicationContext().getExternalFilesDir("single/").toString()+"/";
        //createBaseFolder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String data;
        switch (intent.getAction()){
            case ExtraKey.INTENT_ACTION_DOWNLOAD_SINGLE:
                //singlePath = path+"single/";
                //createSubFolder(singlePath);
                uid = intent.getStringExtra(ExtraKey.INTENT_ACTION_DOWNLOAD_SINGLE_ID);
                data = "[{\"single_id\":"+uid+"}]";
                getToken("1", data);
                break;
        }
        return START_STICKY;
    }

    private void getToken(String type, String data){
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
                            getData(result.getString("token"), result.getString("data"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void getData(String token, String data){
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
                            downloadRequest(Uri.parse(ApiHelper.BASE_URL_IMAGE+"/"+result.getString("file")), result.getString("title"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void downloadRequest(Uri file, String path){
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(file);
        request.setTitle("Kindis");
        request.setDescription("Downloading "+path);
        request.setDestinationInExternalFilesDir(getApplicationContext(), "single/", path);
        long enque = downloadManager.enqueue(request);
        registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                try {
                    new KindisDBHelper(getApplicationContext()).addToDatabase(
                            uid,
                            result.getString("title"),
                            dir+result.getString("title"),
                            result.getString("image"),
                            result.getString("album"),
                            result.getString("artist"),
                            result.getString("artist_id")
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
