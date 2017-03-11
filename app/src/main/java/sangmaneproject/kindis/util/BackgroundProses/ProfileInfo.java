package sangmaneproject.kindis.util.BackgroundProses;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;

/**
 * Created by DELL on 2/1/2017.
 */

public class ProfileInfo extends AsyncTask<String, Void, String> {
    SessionHelper sessionHelper;
    VolleyHelper volleyHelper;
    Context context;

    public ProfileInfo(Context context){
        this.context = context;
        sessionHelper = new SessionHelper();
        volleyHelper = new VolleyHelper();
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d("profileinfo", "oke");
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", strings[0]);

        volleyHelper.post(ApiHelper.PROFILE, params, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                Log.d("profileinfo", message);
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            Log.d("profileinfo", response);
                            JSONObject result = object.getJSONObject("result");
                            sessionHelper.setPreferences(context, "status", "1");
                            sessionHelper.setPreferences(context, "user_id", result.optString("user_id"));
                            sessionHelper.setPreferences(context, "email", result.optString("email"));
                            sessionHelper.setPreferences(context, "fullname", result.optString("fullname"));
                            sessionHelper.setPreferences(context, "birth_date", result.optString("birth_date"));
                            sessionHelper.setPreferences(context, "gender", result.optString("gender"));
                            sessionHelper.setPreferences(context, "is_premium", result.getString("is_premium"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return null;
    }
}
