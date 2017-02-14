package sangmaneproject.kindis.controller;

import android.content.Context;
import android.os.AsyncTask;

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
        /*RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, ApiHelper.ITEM_SINGLE,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        try {
                            String responseBody = new String( error.networkResponse.data, "utf-8" );
                            Log.d("kontoolll", responseBody);
                            JSONObject jsonObject = new JSONObject( responseBody );
                        } catch ( JSONException e ) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException errors){

                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("single_id", "8");
                params.put("token", "fzxqBwktCal1/kxsgjYTx976TUheNkWtsjgb4YwnjNhaojc/BV5M40+fI2Gy3N5MmvPsxLc/ZAzcpZgvEKso9g==");

                return params;
            }
        };
        queue.add(postRequest);*/

        /*Map<String, String> param = new HashMap<String, String>();
        param.put("single_id", params[0]);
        param.put("token", "fzxqBwktCal1/kxsgjYTx976TUheNkWtsjgb4YwnjNhaojc/BV5M40+fI2Gy3N5MmvPsxLc/ZAzcpZgvEKso9g==");

        Log.d("kontoolll", params[0]);
        Log.d("kontoolll", new SessionHelper().getPreferences(context, "token"));

        new VolleyHelper().post(ApiHelper.ITEM_SINGLE, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    Log.d("kontoolll", response);
                }
            }
        });*/

        /*String url = ApiHelper.ITEM_SINGLE+params[0]+ApiHelper.TOKEN+new SessionHelper().getPreferences(context, "token");
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
        });*/
        return null;
    }
}
