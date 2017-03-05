package sangmaneproject.kindis.util.BackgroundProses;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;

/**
 * Created by vincenttp on 2/24/2017.
 */

public class InsertItemPlaylist {
    Context context;

    public InsertItemPlaylist(Context context){
        this.context = context;
    }

    public void insertItem(String single, String playlist){
        Log.d("kontolinsert", single+" : "+playlist);
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", new SessionHelper().getPreferences(context, "user_id"));
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
                            Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "Something Error", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(context, "Something Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
