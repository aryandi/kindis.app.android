package sangmaneproject.kindis.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import sangmaneproject.kindis.helper.ApiHelper;
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
        param.put("token", new SessionHelper().getPreferences(context, "token"));

        new VolleyHelper().post(ApiHelper.ITEM_SINGLE, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    Log.d("songplayresponse", response);
                }
            }
        });
        return null;
    }
}
