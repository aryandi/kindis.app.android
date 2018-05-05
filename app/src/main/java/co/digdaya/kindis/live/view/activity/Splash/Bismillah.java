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

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.AnalyticHelper;
import co.digdaya.kindis.live.helper.PlayerSessionHelper;
import co.digdaya.kindis.live.view.activity.Main;

public class Bismillah extends AppCompatActivity {
    Button enter;
    PlayerSessionHelper playerSessionHelper;
    private String TAG = "Bismillah";
    private AnalyticHelper analyticsHelper;

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
        analyticsHelper = new AnalyticHelper(this);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                analyticsHelper.authHello("true");
                Intent intent = new Intent(Bismillah.this, Main.class);
                startActivity(intent);
            }
        });

        playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
        playerSessionHelper.setPreferences(getApplicationContext(), "pause", "false");

        if (playerSessionHelper.getPreferences(getApplicationContext(), "isShuffle").isEmpty()){
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


    @Override
    public void onBackPressed() {
        analyticsHelper.authHello("false");
        finishAffinity();
    }
}
