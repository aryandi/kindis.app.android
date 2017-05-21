package co.digdaya.kindis.util.BackgroundProses;

import android.app.Activity;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import co.digdaya.kindis.helper.SessionHelper;

/**
 * Created by DELL on 5/20/2017.
 */

public class ResultPayment extends AsyncTask<String, Void, Void>{
    Activity activity;
    SessionHelper sessionHelper;

    public ResultPayment(Activity activity) {
        this.activity = activity;
        sessionHelper = new SessionHelper();
    }


    @Override
    protected Void doInBackground(String... params) {
        try {
            JSONObject object = new JSONObject(params[0]);
            if (object.getBoolean("status")){
                JSONObject result = object.getJSONObject("result");
                sessionHelper.setPreferences(activity, "token_access", result.getString("access_token"));
                sessionHelper.setPreferences(activity, "refresh_token", result.getString("refresh_token"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
