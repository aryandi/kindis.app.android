package co.digdaya.kindis.live.view.activity.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.PlayerSessionHelper;
import co.digdaya.kindis.live.view.activity.Main;

public class Bismillah extends AppCompatActivity {
    Button enter;
    PlayerSessionHelper playerSessionHelper;

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
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Bismillah.this, Main.class);
                startActivity(intent);
            }
        });

        playerSessionHelper.setPreferences(getApplicationContext(), "isplaying", "false");
        playerSessionHelper.setPreferences(getApplicationContext(), "pause", "false");

        if (playerSessionHelper.getPreferences(getApplicationContext(), "isShuffle").isEmpty()){
            playerSessionHelper.setPreferences(getApplicationContext(), "isShuffle", "false");
        }
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
