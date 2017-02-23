package sangmaneproject.kindis.util;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sangmaneproject.kindis.PlayerService;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.PlayerActionHelper;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;

/**
 * Created by DELL on 2/14/2017.
 */

public class SongPlay extends AsyncTask<String, Void, String> {
    Context context;

    public SongPlay(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        Map<String, String> param = new HashMap<String, String>();
        param.put("single_id", params[0]);

        new VolleyHelper().post(ApiHelper.ITEM_SINGLE, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    Log.d("songplayresponse", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");
                            if (!result.getString("file").equals("null")){
                                new PlayerSessionHelper().setPreferences(context, "title", result.getString("title"));
                                new PlayerSessionHelper().setPreferences(context, "album", result.getString("album"));
                                new PlayerSessionHelper().setPreferences(context, "file", result.getString("file"));
                                Intent intent = new Intent(context, PlayerService.class);
                                intent.setAction(PlayerActionHelper.UPDATE_RESOURCE);
                                context.startService(intent);
                            }else {
                                Toast.makeText(context, "Song can't played", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        /*String token = null;
        try {
            token = URLEncoder.encode(new SessionHelper().getPreferences(context, "token"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = ApiHelper.ITEM_SINGLE_GET+params[0]+ApiHelper.TOKEN+token;
        Log.d("songplayurl", url);
        new VolleyHelper().get(url, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    Log.d("songplayresponse", response);
                }else {

                }
            }
        });*/
        return null;
    }

    private void refreshToken(){
        Map<String, String> param = new HashMap<String, String>();
        param.put("token", new SessionHelper().getPreferences(context, "token"));

        new VolleyHelper().post(ApiHelper.REFRESH_TOKEN, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    Log.d("songplayresponse", response);
                }
            }
        });
    }
}
