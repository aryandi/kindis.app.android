package sangmaneproject.kindis.controller;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;

import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;

/**
 * Created by DELL on 2/23/2017.
 */

public class RefreshToken extends AsyncTask<String, String, String> {
    Context context;
    private OnFetchFinishedListener listener;

    public RefreshToken (Context context, OnFetchFinishedListener listener){
        this.context = context;
        this.listener = listener;
    }

    public interface OnFetchFinishedListener {
        void onFetchFinished(String token);
    }

    @Override
    protected String doInBackground(String... params) {
        HashMap<String, String> param = new HashMap<>();
        param.put("token", new SessionHelper().getPreferences(context, "token"));

        new VolleyHelper().post(ApiHelper.REFRESH_TOKEN, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                Log.d("refresh_token", response);
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onFetchFinished(s);
    }
}
