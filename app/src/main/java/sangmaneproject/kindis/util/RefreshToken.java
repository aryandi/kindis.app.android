package sangmaneproject.kindis.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;

/**
 * Created by DELL on 2/23/2017.
 */

public class RefreshToken {
    Context context;

    public RefreshToken (Context context){
        this.context = context;
    }

    public interface OnFetchFinishedListener {
        void onFetchFinished(Boolean status, String token);
    }

    public void getToken(String token, final OnFetchFinishedListener listener){
        HashMap<String, String> param = new HashMap<>();
        param.put("token", token);

        new VolleyHelper().post(ApiHelper.REFRESH_TOKEN, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                Log.d("refresh_token", "Status : "+status+"\nmessage : "+message+"\nresponse : "+response);
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            listener.onFetchFinished(true, response);
                        }else {
                            listener.onFetchFinished(false, response);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    listener.onFetchFinished(false, response);
                }
            }
        });
    }
}
