package sangmaneproject.kindis.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.view.fragment.navigationview.Home;
import sangmaneproject.kindis.view.fragment.navigationview.Notification;
import sangmaneproject.kindis.view.fragment.navigationview.Profile;
import sangmaneproject.kindis.view.fragment.navigationview.FAQ;
import sangmaneproject.kindis.view.fragment.navigationview.Privacy;
import sangmaneproject.kindis.view.fragment.navigationview.Terms;

public class Main extends AppCompatActivity implements View.OnClickListener {
    SessionHelper sessionHelper;
    DrawerLayout drawer;

    Fragment profileFragment;
    Fragment homeFragment;
    Fragment notifFragment;
    Fragment faqFragment;
    Fragment privacyFragment;
    Fragment termsFragment;

    //sidebar
    TextView fullname;
    LinearLayout menuHome;
    LinearLayout menuProfile;
    LinearLayout menuNotif;
    LinearLayout menuPremium;
    TextView menuFAQ;
    TextView menuPrivacy;
    TextView menuTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        homeFragment = new Home(drawer);
        profileFragment = new Profile(drawer);
        notifFragment = new Notification(drawer);
        faqFragment = new FAQ(drawer);
        privacyFragment = new Privacy(drawer);
        termsFragment = new Terms(drawer);
        sessionHelper = new SessionHelper();

        //sidebar
        fullname = (TextView) findViewById(R.id.fullname);
        menuHome = (LinearLayout) findViewById(R.id.menu_home);
        menuProfile = (LinearLayout) findViewById(R.id.menu_profile);
        menuNotif = (LinearLayout) findViewById(R.id.menu_notif);
        menuPremium = (LinearLayout) findViewById(R.id.menu_premium);
        menuFAQ = (TextView) findViewById(R.id.menu_faq);
        menuPrivacy = (TextView) findViewById(R.id.menu_privacy);
        menuTerms = (TextView) findViewById(R.id.menu_terms);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cont_main, homeFragment);
        transaction.commit();

        initSidebar();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initSidebar(){
        fullname.setText(sessionHelper.getPreferences(getApplicationContext(), "fullname"));
        menuHome.setOnClickListener(this);
        menuNotif.setOnClickListener(this);
        menuPremium.setOnClickListener(this);
        menuProfile.setOnClickListener(this);
        menuFAQ.setOnClickListener(this);
        menuPrivacy.setOnClickListener(this);
        menuTerms.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (view.getId() == R.id.menu_home){
            transaction.replace(R.id.cont_main, homeFragment);
        }else if (view.getId() == R.id.menu_notif){
            transaction.replace(R.id.cont_main, notifFragment);
        }else if(view.getId() == R.id.menu_premium) {
            Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
        }else if (view.getId() == R.id.menu_profile){
            transaction.replace(R.id.cont_main, profileFragment);
        }else if (view.getId() == R.id.menu_faq){
            transaction.replace(R.id.cont_main, faqFragment);
        }else if (view.getId() == R.id.menu_privacy){
            transaction.replace(R.id.cont_main, privacyFragment);
        }else if (view.getId() == R.id.menu_terms){
            transaction.replace(R.id.cont_main, termsFragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
        drawer.closeDrawers();
    }
}
