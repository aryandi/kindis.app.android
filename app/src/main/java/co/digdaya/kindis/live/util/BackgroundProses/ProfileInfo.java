package co.digdaya.kindis.live.util.BackgroundProses;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;

/**
 * Created by DELL on 2/1/2017.
 */

public class ProfileInfo extends AsyncTask<String, Void, String> {
    SessionHelper sessionHelper;
    VolleyHelper volleyHelper;
    Context context;

    public ProfileInfo(Context context) {
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
                if (status) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
                            Log.d("profileinfo", response);
                            JSONObject result = object.getJSONObject("result");
                            sessionHelper.setPreferences(context, "status", "1");
                            sessionHelper.setPreferences(context, "user_id", result.optString("user_id"));
                            String email = result.optString("email");
                            if (!TextUtils.isEmpty(email) && !email.equals("0")){
                                sessionHelper.setPreferences(context, "email", email);
                            }
                            sessionHelper.setPreferences(context, "fullname", result.optString("fullname"));
                            sessionHelper.setPreferences(context, "birth_date", result.optString("birth_date"));
                            sessionHelper.setPreferences(context, "gender", result.optString("gender"));
                            sessionHelper.setPreferences(context, "is_premium", result.getString("is_premium"));
                            if (TextUtils.isEmpty(sessionHelper.getPreferences(context, "social_name_facebook"))){
                                sessionHelper.setPreferences(context, "social_name_facebook", result.optString("social_name_fb"));
                            }
                            if (TextUtils.isEmpty(sessionHelper.getPreferences(context, "social_name_twitter"))) {
                                sessionHelper.setPreferences(context, "social_name_twitter", result.optString("social_name_twitter"));
                            }
                            if (TextUtils.isEmpty(sessionHelper.getPreferences(context, "social_name_google"))) {
                                sessionHelper.setPreferences(context, "social_name_google", result.optString("social_name_google"));
                            }
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
