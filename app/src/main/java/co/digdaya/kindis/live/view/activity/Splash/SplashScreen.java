package co.digdaya.kindis.live.view.activity.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.CheckConnection;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.util.BackgroundProses.RefreshToken;

import static co.digdaya.kindis.live.helper.VolleyHelper.NO_CONNECTION;

public class SplashScreen extends AppCompatActivity {
    SessionHelper sessionHelper;
    RefreshToken refreshToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (!isTaskRoot()) {
            finish();
            return;
        }

        /*PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("co.digdaya.kindis", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                System.out.println("hashkey: "+ something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }*/

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_splash_screen);

        sessionHelper = new SessionHelper();
        refreshToken = new RefreshToken(getApplicationContext());

        new Handler().postDelayed(new Runnable() {

			/*
			 * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                if (sessionHelper.getPreferences(getApplicationContext(), "status").equals("1")){
                    if (new CheckConnection().isInternetAvailable(getApplicationContext())){
                        wasLogin();
                    }else {
                        Intent i = new Intent(SplashScreen.this, Bismillah.class);
                        startActivity(i);
                    }
                }else {
                    notLogin();
                }
            }
        }, 1000);
    }

    private void notLogin(){
        Intent i = new Intent(SplashScreen.this, Walkthrough.class);
        startActivity(i);
        finish();
    }

    private void wasLogin(){
        String expired_in = sessionHelper.getPreferences(getApplicationContext(), "expires_in");
        long currentTimeMillis = System.currentTimeMillis()/1000;
        if (TextUtils.isEmpty(expired_in)) {
            refreshToken();
        } else {
            if (currentTimeMillis > Long.parseLong(expired_in)){
                refreshToken();
            } else {
                Intent i = new Intent(SplashScreen.this, Bismillah.class);
                startActivity(i);
            }
        }
    }

    private void refreshToken() {
        System.out.println(sessionHelper.getPreferences(getApplicationContext(), "token_access") + "\n" + sessionHelper.getPreferences(getApplicationContext(), "token_refresh") + "\n" + sessionHelper.getPreferences(getApplicationContext(), "user_id"));
        HashMap<String, String> param = new HashMap<>();
        param.put("token_access", sessionHelper.getPreferences(getApplicationContext(), "token_access"));
        param.put("token_refresh", sessionHelper.getPreferences(getApplicationContext(), "token_refresh"));
        param.put("uid", sessionHelper.getPreferences(getApplicationContext(), "user_id"));
        new VolleyHelper().post(ApiHelper.REFRESH_TOKEN, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status) {
                    Log.d("refreshtoken", "onReceive: " + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
                            JSONObject result = object.getJSONObject("result");
                            sessionHelper.setPreferences(getApplicationContext(), "token_access", result.getString("access_token"));
                            sessionHelper.setPreferences(getApplicationContext(), "refresh_token", result.getString("refresh_token"));
                            sessionHelper.setPreferences(getApplicationContext(), "expires_in", String.valueOf(result.optInt("expires_in")));
                            Intent i = new Intent(SplashScreen.this, Bismillah.class);
                            startActivity(i);
                        } else {
                            if (refreshToken.refreshToken()) {
                                Intent i = new Intent(SplashScreen.this, Bismillah.class);
                                startActivity(i);
                            } else {
                                Intent i = new Intent(SplashScreen.this, Walkthrough.class);
                                startActivity(i);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Intent i = new Intent(SplashScreen.this, Bismillah.class);
                        startActivity(i);
                    }
                } else {
                    if (message.equals(NO_CONNECTION)) {
                        //ToDO create retry dialog
                        Toast.makeText(SplashScreen.this, "No Connection", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent i = new Intent(SplashScreen.this, Bismillah.class);
                        startActivity(i);
                    }
                }
            }
        });
    }
}
