package sangmaneproject.kindis.view.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.view.adapter.AdapterListSong;

public class Player extends AppCompatActivity {
    ImageButton hide;
    ViewPager viewPager;
    AdapterListSong adapterListSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        hide = (ImageButton) findViewById(R.id.btn_hide);
        viewPager = (ViewPager) findViewById(R.id.list_player);

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapterListSong = new AdapterListSong(getApplicationContext());
        viewPager.setAdapter(adapterListSong);

        /*viewPager.setClipToPadding(false);
        viewPager.setPageMargin(0);
        viewPager.setPadding(0, 0, 0, 0);
        viewPager.setOffscreenPageLimit(3);*/
    }
}
