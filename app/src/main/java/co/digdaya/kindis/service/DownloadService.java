package co.digdaya.kindis.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;

import java.io.File;

import co.digdaya.kindis.helper.ExtraKey;

public class DownloadService extends Service {
    String path;

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createBaseFolder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand: "+intent.getAction());
        switch (intent.getAction()){
            case ExtraKey.INTENT_ACTION_DOWNLOAD_SINGLE:
                createSubFolder(path+"single");
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
            }else {
                System.out.println("DownloadServices: subFolder not created");
            }
        }else {
            System.out.println("DownloadServices: subFolder exist");
        }
    }
}
