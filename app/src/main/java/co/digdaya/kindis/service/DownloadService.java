package co.digdaya.kindis.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;

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
    String path;
    String uid;

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
        createBaseFolder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String data;
        switch (intent.getAction()){
            case ExtraKey.INTENT_ACTION_DOWNLOAD_SINGLE:
                createSubFolder(path+"single");
                uid = intent.getStringExtra(ExtraKey.INTENT_ACTION_DOWNLOAD_SINGLE_ID);
                data = "[{\"single_id\":"+uid+"}]";
                System.out.println("downloadresponses param: "+data);
                getToken("1", data);
                break;
        }
        return START_STICKY;
    }

    private void createBaseFolder(){
        path = Environment.getExternalStorageDirectory().getPath().toString()+ "/Android/data/id.digdaya.kindis/files/";
        File baseFolder = new File(path);
        if (!baseFolder.exists()){
            if (baseFolder.mkdirs()){
                System.out.println("DownloadServices: Folder created");
            }else {
                System.out.println("DownloadServices: Folder not created");
            }
        }else {
            System.out.println("DownloadServices: Folder exist");
        }
    }

    private void createSubFolder(String pathFolder){
        File subFolder = new File(pathFolder);
        if (!subFolder.exists()){
            if (subFolder.mkdirs()){
                System.out.println("DownloadServices: subFolder created");
                File nomedia = new File(subFolder, ".nomedia");
                /*if (!nomedia.exists()){
                    if (nomedia.mkdirs()){
                        System.out.println("DownloadServices: nomedia created");
                    }else {
                        System.out.println("DownloadServices: nomedia not created");
                    }
                }*/
            }else {
                System.out.println("DownloadServices: subFolder not created");
            }
        }else {
            System.out.println("DownloadServices: subFolder exist");
        }
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
                            JSONObject result = object.getJSONObject("result");
                            new KindisDBHelper(getApplicationContext()).addToDatabase(
                                    uid,
                                    result.getString("title"),
                                    result.getString("file"),
                                    result.getString("image"),
                                    result.getString("album"),
                                    result.getString("artist"),
                                    result.getString("artist_id")
                            );
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}