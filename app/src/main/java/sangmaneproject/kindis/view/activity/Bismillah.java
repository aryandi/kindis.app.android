package sangmaneproject.kindis.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.PlayerSessionHelper;

public class Bismillah extends AppCompatActivity {
    Button enter;

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
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Bismillah.this, Main.class);
                startActivity(intent);
            }
        });

        new PlayerSessionHelper().setPreferences(getApplicationContext(), "isplaying", "false");
        new PlayerSessionHelper().setPreferences(getApplicationContext(), "pause", "false");
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
