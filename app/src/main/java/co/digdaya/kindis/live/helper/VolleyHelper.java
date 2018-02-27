package co.digdaya.kindis.live.helper;

import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import co.digdaya.kindis.live.MainApplication;

/**
 * Created by vincenttp on 1/28/2017.
 */

public class VolleyHelper {
    private String SUCCESS = "Success";
    private String NO_CONNECTION = "No Connection";
    private String FAILED = "FAILED";
    private RequestQueue requestQueue;

    private RetryPolicy retryPolicy = new RetryPolicy() {
        @Override
        public int getCurrentTimeout() {
            return 5000;
        }

        @Override
        public int getCurrentRetryCount() {
            return 5000;
        }

        @Override
        public void retry(VolleyError error) throws VolleyError {
        }
    };

    public void post(String url, final Map<String, String> parameters, final HttpListener<String> listener){
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onReceive(true, SUCCESS, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            listener.onReceive(false, NO_CONNECTION, null);
                        } else {
                            if (error!=null){
                                try {
                                    String responseBody = new String(error.networkResponse.data, "utf-8" );
                                    Log.d("volleyresponse", responseBody);
                                    JSONObject jsonObject = new JSONObject( responseBody );
                                    listener.onReceive(true, SUCCESS, jsonObject.toString());
                                } catch ( JSONException e ) {
                                    e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() {
                return parameters;
            }
        };
        request.setRetryPolicy(retryPolicy);
        request.setTag(url);
        addToRequestQueue(request);
    }

    public void get(String url, final HttpListener<String> listener){
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onReceive(true, SUCCESS, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError){
                            listener.onReceive(false, NO_CONNECTION, null);
                        }else {
                            try {
                                String responseBody = new String( error.networkResponse.data, "utf-8" );
                                JSONObject jsonObject = new JSONObject( responseBody );
                                listener.onReceive(true, SUCCESS, jsonObject.toString());
                            } catch ( JSONException e ) {
                                e.printStackTrace();
                                listener.onReceive(false, "Something Error", "");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onReceive(false, "Something Error", "");
                            }
                        }
                    }
                }
        );
        request.setRetryPolicy(retryPolicy);
        request.setTag(url);
        addToRequestQueue(request);
    }

    private <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(MainApplication.getInstance());
        }
        return requestQueue;
    }

    public interface HttpListener<T> {
        void onReceive(boolean status, String message, T response);
    }
}
