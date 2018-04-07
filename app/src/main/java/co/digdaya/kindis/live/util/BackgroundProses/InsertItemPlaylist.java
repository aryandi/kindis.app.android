package co.digdaya.kindis.live.util.BackgroundProses;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.view.dialog.DialogAlertPremium;

/**
 * Created by vincenttp on 2/24/2017.
 */

public class InsertItemPlaylist {
    private Activity activity;
    private DialogAlertPremium dialogAlertPremium;
    private Dialog dialogPremium;

    public InsertItemPlaylist(Activity activity){
        this.activity = activity;
    }

    public void insertItem(String single, String playlist) {
        Log.d("songinsert", single + " : " + playlist);
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", new SessionHelper().getPreferences(activity, "user_id"));
        param.put("playlist_id", playlist);
        param.put("songs", "["+single+"]");

        new VolleyHelper().post(ApiHelper.INSERT_ITEM_PLAYLIST, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                Log.d("InsertItemPlaylist", response);
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            Toast.makeText(activity, object.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            dialogAlertPremium = new DialogAlertPremium(activity, dialogPremium,
                                    object.getString("message"));
                            dialogAlertPremium.showDialog();
//                            Toast.makeText(activity, "Something Error", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(activity, "Something Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
