package co.digdaya.kindis.live.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DELL on 2/1/2017.
 */

public class SessionHelper {
    public void setPreferences(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("kindis", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }
    public String getPreferences(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("kindis", Context.MODE_PRIVATE);
        return prefs.getString(key, "");
    }

    public void clearSession(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences("kindis", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }
}
