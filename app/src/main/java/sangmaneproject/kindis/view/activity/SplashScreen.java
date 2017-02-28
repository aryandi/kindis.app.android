package sangmaneproject.kindis.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.SessionHelper;

public class SplashScreen extends AppCompatActivity {
    SessionHelper sessionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_splash_screen);

        sessionHelper = new SessionHelper();

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
                    Intent i = new Intent(SplashScreen.this, Bismillah.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(SplashScreen.this, Walkthrough.class);
                    startActivity(i);
                }
                finish();
            }
        }, 1000);
    }
}
