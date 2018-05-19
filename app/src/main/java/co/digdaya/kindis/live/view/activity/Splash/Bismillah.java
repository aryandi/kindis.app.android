package co.digdaya.kindis.live.view.activity.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.eggheadgames.siren.ISirenListener;
import com.eggheadgames.siren.Siren;
import com.eggheadgames.siren.SirenAlertType;
import com.eggheadgames.siren.SirenVersionCheckType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.AnalyticHelper;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.Constanta;
import co.digdaya.kindis.live.helper.PlayerSessionHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.view.activity.Main;

public class Bismillah extends AppCompatActivity {
    Button enter;
    PlayerSessionHelper playerSessionHelper;
    private String TAG = "Bismillah";
    private AnalyticHelper analyticHelper;
    private SessionHelper sessionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bismillah);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        enter = (Button) findViewById(R.id.btn_enter);
        playerSessionHelper = new PlayerSessionHelper();
        analyticHelper = new AnalyticHelper(this);
        sessionHelper = new SessionHelper();
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIPAddress();
                postDeviceId();
                analyticHelper.authHello("true");
                Intent intent = new Intent(Bismillah.this, Main.class);
                startActivity(intent);
            }
        });

        playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
        playerSessionHelper.setPreferences(getApplicationContext(), "pause", "false");

        if (playerSessionHelper.getPreferences(getApplicationContext(), "isShuffle").isEmpty()) {
            playerSessionHelper.setPreferences(getApplicationContext(), "isShuffle", "false");
        }

        checkCurrentAppVersion();
    }

    private void checkCurrentAppVersion() {
        Siren siren = Siren.getInstance(getApplicationContext());
        siren.setSirenListener(sirenListener);
        siren.setMajorUpdateAlertType(SirenAlertType.FORCE);
        siren.setMinorUpdateAlertType(SirenAlertType.OPTION);
        siren.setPatchUpdateAlertType(SirenAlertType.SKIP);
        siren.setRevisionUpdateAlertType(SirenAlertType.NONE);
        siren.setVersionCodeUpdateAlertType(SirenAlertType.SKIP);
        siren.checkVersion(this, SirenVersionCheckType.IMMEDIATELY,
                "https://cdn.kindis.co/assets/json/androidVer.json");
    }

    ISirenListener sirenListener = new ISirenListener() {
        @Override
        public void onShowUpdateDialog() {
            Log.d(TAG, "onShowUpdateDialog");
        }

        @Override
        public void onLaunchGooglePlay() {
            Log.d(TAG, "onLaunchGooglePlay");
        }

        @Override
        public void onSkipVersion() {
            Log.d(TAG, "onSkipVersion");
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "onCancel");
        }

        @Override
        public void onDetectNewVersionWithoutAlert(String message) {
            Log.d(TAG, "onDetectNewVersionWithoutAlert: " + message);
        }

        @Override
        public void onError(Exception e) {
            Log.d(TAG, "onError");
            e.printStackTrace();
        }
    };

    private void postDeviceId() {
        HashMap<String, String> param = new HashMap<>();
        param.put("token_access", sessionHelper.getPreferences(getApplicationContext(), Constanta.PREF_TOKEN_ACCESS));
        param.put("uid", sessionHelper.getPreferences(getApplicationContext(), Constanta.PREF_USERID));
        param.put("device_id", sessionHelper.getPreferences(getApplicationContext(), Constanta.PREF_DEVICE_ID));
        param.put("device_token_id", sessionHelper.getPreferences(getApplicationContext(), Constanta.PREF_DEVICE_TOKEN));
        new VolleyHelper().post(ApiHelper.DEVICE_ID, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status) {
                    Log.d("post device id", "onReceive: " + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.optBoolean("status")) {
                            sessionHelper.setPreferences(getApplicationContext(),
                                    Constanta.PREF_IS_DEVICE_REGISTERED, "true");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void getIPAddress() {
        new VolleyHelper().get("http://www.ip-api.com/json", new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status) {
                    Log.d("get ip address", "onReceive: " + response);
                    try {
                        JSONObject object = new JSONObject(response);
                        String city = object.optString("city");
                        if (!city.equals(sessionHelper.getPreferences(getApplicationContext(), Constanta.PREF_AUTH_LOCATION))) {
                            sessionHelper.setPreferences(getApplicationContext(), Constanta.PREF_AUTH_LOCATION, city);
                            analyticHelper.authLocation(city);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        analyticHelper.authHello("false");
        finishAffinity();
    }
}
