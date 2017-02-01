package sangmaneproject.kindis.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sangmaneproject.kindis.PlayerService;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.view.fragment.Home;
import sangmaneproject.kindis.view.fragment.Profile;

public class Main extends AppCompatActivity implements View.OnClickListener {
    SessionHelper sessionHelper;
    DrawerLayout drawer;

    Fragment profileFragment;
    Fragment homeFragment;

    ImageButton expand;

    RelativeLayout btnPlay;
    ImageView icPlay;
    Boolean isPlaying = false;
    ProgressBar progressBar;

    //sidebar
    TextView fullname;
    LinearLayout menuHome;
    LinearLayout menuProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        homeFragment = new Home(drawer);
        profileFragment = new Profile(drawer);

        btnPlay = (RelativeLayout) findViewById(R.id.btn_play);
        icPlay = (ImageView) findViewById(R.id.ic_play);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        sessionHelper = new SessionHelper();

        //sidebar
        fullname = (TextView) findViewById(R.id.fullname);
        menuHome = (LinearLayout) findViewById(R.id.menu_home);
        menuProfile = (LinearLayout) findViewById(R.id.menu_profile);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cont_main, homeFragment);
        transaction.commit();

        initSidebar();

        /*expand = (ImageButton) findViewById(R.id.btn_expand);
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main.this, Player.class);
                startActivity(intent);
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initBottomPlayer(){
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlaying){
                    icPlay.setImageResource(R.drawable.ic_pause);
                    Intent intent = new Intent(getApplicationContext(), PlayerService.class);
                    intent.setAction(PlayerService.ACTION_PLAY);
                    startService(intent);
                    isPlaying = true;
                }else {
                    icPlay.setImageResource(R.drawable.ic_play);
                    Intent intent = new Intent(getApplicationContext(), PlayerService.class);
                    intent.setAction(PlayerService.ACTION_PAUSE);
                    startService(intent);
                    isPlaying = false;
                }
            }
        });
    }

    private void initSidebar(){
        fullname.setText(sessionHelper.getPreferences(getApplicationContext(), "fullname"));
        menuHome.setOnClickListener(this);
        menuProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (view.getId() == R.id.menu_home){
            transaction.replace(R.id.cont_main, homeFragment);
        }else if (view.getId() == R.id.menu_profile){
            transaction.replace(R.id.cont_main, profileFragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
        drawer.closeDrawers();
    }
}
