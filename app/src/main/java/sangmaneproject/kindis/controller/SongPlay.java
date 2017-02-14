package sangmaneproject.kindis.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
        String url = ApiHelper.ITEM_SINGLE+params[0]+ApiHelper.TOKEN+new SessionHelper().getPreferences(context, "token");
        Log.d("singleurl", url);
        new VolleyHelper().get(url, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    Log.d("singleresponse", response);
                }else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return null;
    }
}
