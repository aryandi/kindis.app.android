package co.digdaya.kindis.live.helper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by DELL on 4/30/2017.
 */

public class CheckPermission {
    Activity activity;

    public CheckPermission(Activity activity) {
        this.activity = activity;
    }

    public boolean checkPermission(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }
    }

    public boolean checkPermissionCamera(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }
    }

    public boolean checkPermissionStorage(){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }else {
            return false;
        }
    }

    public void showPermission(int code){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                code);
    }

    public void showPermissionStorage(int code){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                code);
    }
}
