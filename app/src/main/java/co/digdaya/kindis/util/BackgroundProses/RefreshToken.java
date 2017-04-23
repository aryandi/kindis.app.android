package co.digdaya.kindis.util.BackgroundProses;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;

/**
 * Created by DELL on 4/15/2017.
 */

public class RefreshToken {
    Context context;
    SessionHelper sessionHelper;

    public RefreshToken(Context context) {
        this.context = context;
        sessionHelper = new SessionHelper();
    }

    public boolean refreshToken(){
        boolean status = false;
        HashMap<String, String> param = new HashMap<>();
        param.put("token_access", sessionHelper.getPreferences(context, "token_access"));
        param.put("token_refresh", sessionHelper.getPreferences(context, "token_refresh"));
        param.put("uid", sessionHelper.getPreferences(context, "user_id"));

        new VolleyHelper().post(ApiHelper.REFRESH_TOKEN, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                System.out.println("refreshToken: "+response);
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            status = true;
                            JSONObject result = object.getJSONObject("result");
                            sessionHelper.setPreferences(context, "token_access", result.getString("access_token"));
                            sessionHelper.setPreferences(context, "refresh_token", result.getString("refresh_token"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return status;
    }
}
