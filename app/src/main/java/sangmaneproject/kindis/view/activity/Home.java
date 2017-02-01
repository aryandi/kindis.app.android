package sangmaneproject.kindis.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import sangmaneproject.kindis.PlayerService;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.view.fragment.Musiq;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    AHBottomNavigation bottomNavigation;
    FragmentTransaction transaction;
    SessionHelper sessionHelper;

    Fragment musiqFragment;
    ImageButton expand;

    RelativeLayout btnPlay;
    ImageView icPlay;
    Boolean isPlaying = false;
    ProgressBar progressBar;

    //sidebar
    TextView fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        btnPlay = (RelativeLayout) findViewById(R.id.btn_play);
        icPlay = (ImageView) findViewById(R.id.ic_play);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        sessionHelper = new SessionHelper();

        //sidebar
        fullname = (TextView) findViewById(R.id.fullname);

        transaction = getSupportFragmentManager().beginTransaction();

        initBottomPlayer();
        initBottomAction();
        initSidebar();

        expand = (ImageButton) findViewById(R.id.btn_expand);
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Player.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            Intent intent = new Intent(Home.this, Search.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initFragment(){
        musiqFragment = new Musiq();
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

    private void initBottomAction(){
        initFragment();

        AHBottomNavigationItem musiq = new AHBottomNavigationItem(R.string.musiq, R.drawable.ic_explore, R.color.white);
        AHBottomNavigationItem taklim = new AHBottomNavigationItem(R.string.taklim, R.drawable.ic_taklim, R.color.white);
        AHBottomNavigationItem infaq = new AHBottomNavigationItem(R.string.infaq, R.drawable.ic_infaq, R.color.white);
        AHBottomNavigationItem playlist = new AHBottomNavigationItem(R.string.playlist, R.drawable.ic_playlist, R.color.white);

        bottomNavigation.addItem(musiq);
        bottomNavigation.addItem(taklim);
        bottomNavigation.addItem(infaq);
        bottomNavigation.addItem(playlist);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#212121"));
        bottomNavigation.setAccentColor(Color.parseColor("#ffffff"));
        bottomNavigation.setInactiveColor(Color.parseColor("#626262"));
        bottomNavigation.setForceTint(true);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        bottomNavigation.setCurrentItem(0);
        transaction.replace(R.id.cont_home, musiqFragment);
        transaction.commit();
    }

    private void initSidebar(){
        fullname.setText(sessionHelper.getPreferences(getApplicationContext(), "fullname"));
    }
}
