package co.digdaya.kindis.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.view.dialog.DialogBanner;
import co.digdaya.kindis.view.dialog.DialogGetPremium;
import co.digdaya.kindis.view.dialog.DialogGift;
import co.digdaya.kindis.view.fragment.navigationview.Cookies;
import co.digdaya.kindis.view.fragment.navigationview.FAQ;
import co.digdaya.kindis.view.fragment.navigationview.Home;
import co.digdaya.kindis.view.fragment.navigationview.Notification;
import co.digdaya.kindis.view.fragment.navigationview.Privacy;
import co.digdaya.kindis.view.fragment.navigationview.Profile;
import co.digdaya.kindis.view.fragment.navigationview.SaveOffline;
import co.digdaya.kindis.view.fragment.navigationview.Terms;

public class Main extends AppCompatActivity implements View.OnClickListener {
    SessionHelper sessionHelper;
    DrawerLayout drawer;

    FragmentTransaction transaction;
    Fragment profileFragment;
    Fragment saveOffline;
    Fragment homeFragment;
    Fragment notifFragment;
    Fragment faqFragment;
    Fragment privacyFragment;
    Fragment termsFragment;
    Fragment cookiesFragment;

    //sidebar
    ImageView profilePicture;
    TextView fullname;
    LinearLayout menuHome;
    LinearLayout menuProfile;
    LinearLayout menuOffline;
    LinearLayout menuNotif;
    LinearLayout menuPremium;
    LinearLayout menuGift;
    TextView menuFAQ;
    TextView menuPrivacy;
    TextView menuTerms;
    TextView menuCookies;
    Button profileStatus;

    ImageView icMenuHome, icMenuNotif, icMenuProfile, icMenuOffline;
    TextView labelMenuHome, labelMenuNotif, labelMenuProfile, labelMenuOffline;
    Boolean exit = false;

    Dialog dialogPremium, dialogGft, dialogBnnr;

    DialogGift dialogGift;
    DialogBanner dialogBanner;

    public Main() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        dialogBanner = new DialogBanner(this, dialogBnnr);
        dialogBanner.showDialog();

        homeFragment = new Home(drawer);
        profileFragment = new Profile(drawer);
        saveOffline = new SaveOffline(drawer);
        notifFragment = new Notification(drawer);
        faqFragment = new FAQ(drawer);
        privacyFragment = new Privacy(drawer);
        termsFragment = new Terms(drawer);
        cookiesFragment = new Cookies(drawer);
        sessionHelper = new SessionHelper();

        //sidebar
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        fullname = (TextView) findViewById(R.id.fullname);
        menuHome = (LinearLayout) findViewById(R.id.menu_home);
        menuProfile = (LinearLayout) findViewById(R.id.menu_profile);
        menuOffline = (LinearLayout) findViewById(R.id.menu_offline);
        menuNotif = (LinearLayout) findViewById(R.id.menu_notif);
        menuPremium = (LinearLayout) findViewById(R.id.menu_premium);
        menuGift = (LinearLayout) findViewById(R.id.menu_gift);
        menuFAQ = (TextView) findViewById(R.id.menu_faq);
        menuPrivacy = (TextView) findViewById(R.id.menu_privacy);
        menuTerms = (TextView) findViewById(R.id.menu_terms);
        menuCookies = (TextView) findViewById(R.id.menu_cookies);

        icMenuHome = (ImageView) findViewById(R.id.ic_menu_home);
        icMenuNotif = (ImageView) findViewById(R.id.ic_menu_notif);
        icMenuProfile = (ImageView) findViewById(R.id.ic_menu_profile);
        icMenuOffline = (ImageView) findViewById(R.id.ic_menu_offline);

        labelMenuHome = (TextView) findViewById(R.id.label_menu_home);
        labelMenuNotif = (TextView) findViewById(R.id.label_menu_notif);
        labelMenuProfile = (TextView) findViewById(R.id.label_menu_profile);
        labelMenuOffline = (TextView) findViewById(R.id.label_menu_offline);

        profileStatus = (Button) findViewById(R.id.profile_status);

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cont_main, homeFragment, "home");
        transaction.commit();

        initSidebar();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().findFragmentByTag("home").isVisible()){
                if (exit) {
                    this.finishAffinity();
                } else {
                    Toast.makeText(this, "Press Back again to Exit.",
                            Toast.LENGTH_SHORT).show();
                    exit = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exit = false;
                        }
                    }, 3 * 1000);

                }
            }else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionHelper.getPreferences(getApplicationContext(), "is_premium").equals("1")){
            profileStatus.setText("PREMIUM");
            profileStatus.setBackground(getDrawable(R.drawable.button_rounded_orange));
        }
    }

    private void initSidebar(){
        System.out.println("profile_picture: "+sessionHelper.getPreferences(getApplicationContext(), "profile_picture"));

        if (sessionHelper.getPreferences(getApplicationContext(), "profile_picture").length()>10){
            Glide.with(getApplicationContext())
                    .load(sessionHelper.getPreferences(getApplicationContext(), "profile_picture"))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(profilePicture);
        }

        icMenuHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        labelMenuHome.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));

        fullname.setText(sessionHelper.getPreferences(getApplicationContext(), "fullname"));
        menuHome.setOnClickListener(this);
        menuNotif.setOnClickListener(this);
        menuPremium.setOnClickListener(this);
        menuGift.setOnClickListener(this);
        menuProfile.setOnClickListener(this);
        menuOffline.setOnClickListener(this);
        menuFAQ.setOnClickListener(this);
        menuPrivacy.setOnClickListener(this);
        menuTerms.setOnClickListener(this);
        menuCookies.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        setDefaultSelectedMenuColor();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (view.getId() == R.id.menu_home){
            transaction.replace(R.id.cont_main, homeFragment);

            icMenuHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
            labelMenuHome.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        }else if (view.getId() == R.id.menu_notif){
            transaction.replace(R.id.cont_main, notifFragment);

            icMenuNotif.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
            labelMenuNotif.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        }else if(view.getId() == R.id.menu_premium) {
            Intent intent = new Intent(this, Premium.class);
            startActivity(intent);
        }else if (view.getId() == R.id.menu_profile){
            transaction.replace(R.id.cont_main, profileFragment);

            icMenuProfile.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
            labelMenuProfile.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        }else if (view.getId() == R.id.menu_offline){
            transaction.replace(R.id.cont_main, saveOffline);

            icMenuOffline.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
            labelMenuOffline.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        }else if (view.getId() == R.id.menu_gift) {
            dialogGift = new DialogGift(dialogGft, this);
            dialogGift.showDialog();
        }else if (view.getId() == R.id.menu_faq){
            transaction.replace(R.id.cont_main, faqFragment);

            menuFAQ.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        }else if (view.getId() == R.id.menu_privacy){
            transaction.replace(R.id.cont_main, privacyFragment);

            menuPrivacy.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        }else if (view.getId() == R.id.menu_terms){
            transaction.replace(R.id.cont_main, termsFragment);

            menuTerms.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        }else if (view.getId() == R.id.menu_cookies){
            transaction.replace(R.id.cont_main, cookiesFragment);

            menuCookies.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        }
        transaction.addToBackStack(null);
        transaction.commit();
        drawer.closeDrawers();
    }

    private void setDefaultSelectedMenuColor(){
        icMenuHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
        labelMenuHome.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        icMenuNotif.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
        labelMenuNotif.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        icMenuProfile.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
        labelMenuProfile.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        icMenuOffline.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white));
        labelMenuOffline.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        menuFAQ.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        menuPrivacy.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        menuTerms.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        menuCookies.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
    }


}
