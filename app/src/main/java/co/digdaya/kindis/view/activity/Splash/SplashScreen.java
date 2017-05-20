package co.digdaya.kindis.view.activity.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.CheckConnection;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.util.BackgroundProses.RefreshToken;
import co.digdaya.kindis.view.activity.Account.SignInActivity;

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
        System.out.println(sessionHelper.getPreferences(getApplicationContext(), "token_access")+"\n"+sessionHelper.getPreferences(getApplicationContext(), "token_refresh")+"\n"+sessionHelper.getPreferences(getApplicationContext(), "user_id"));
        HashMap<String, String> param = new HashMap<>();
        param.put("token_access", sessionHelper.getPreferences(getApplicationContext(), "token_access"));
        param.put("token_refresh", sessionHelper.getPreferences(getApplicationContext(), "token_refresh"));
        param.put("uid", sessionHelper.getPreferences(getApplicationContext(), "user_id"));
        new VolleyHelper().post(ApiHelper.REFRESH_TOKEN, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                System.out.println(response);
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");
                            sessionHelper.setPreferences(getApplicationContext(), "token_access", result.getString("access_token"));
                            sessionHelper.setPreferences(getApplicationContext(), "refresh_token", result.getString("refresh_token"));
                            Intent i = new Intent(SplashScreen.this, Bismillah.class);
                            startActivity(i);
                        }else {
                            if (refreshToken.refreshToken()){
                                Intent i = new Intent(SplashScreen.this, Bismillah.class);
                                startActivity(i);
                            }else {
                                Intent i = new Intent(SplashScreen.this, Walkthrough.class);
                                startActivity(i);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Intent i = new Intent(SplashScreen.this, Bismillah.class);
                    startActivity(i);
                }
            }
        });
    }
}
