package co.digdaya.kindis.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DELL on 2/1/2017.
 */

public class SessionHelper {
    public void setPreferences(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("kindis", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }
    public String getPreferences(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("kindis", Context.MODE_PRIVATE);
        String position = prefs.getString(key, "");
        return position;
    }
}
