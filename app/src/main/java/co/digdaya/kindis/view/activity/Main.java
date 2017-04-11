package co.digdaya.kindis.view.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import co.digdaya.kindis.view.fragment.navigationview.Terms;

public class Main extends AppCompatActivity implements View.OnClickListener {
    SessionHelper sessionHelper;
    DrawerLayout drawer;

    Fragment profileFragment;
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
    LinearLayout menuNotif;
    LinearLayout menuPremium;
    LinearLayout menuGift;
    TextView menuFAQ;
    TextView menuPrivacy;
    TextView menuTerms;
    TextView menuCookies;

    ImageView icMenuHome, icMenuNotif, icMenuProfile;
    TextView labelMenuHome, labelMenuNotif, labelMenuProfile;

    Dialog dialogPremium, dialogGft, dialogBnnr;

    DialogGetPremium dialogGetPremium;
    DialogGift dialogGift;
    DialogBanner dialogBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        dialogGetPremium = new DialogGetPremium(this, dialogPremium);
        dialogBanner = new DialogBanner(this, dialogBnnr);
        dialogBanner.showDialog();

        homeFragment = new Home(drawer);
        profileFragment = new Profile(drawer);
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

        labelMenuHome = (TextView) findViewById(R.id.label_menu_home);
        labelMenuNotif = (TextView) findViewById(R.id.label_menu_notif);
        labelMenuProfile = (TextView) findViewById(R.id.label_menu_profile);

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
            dialogGetPremium.showDialog();
        }else if (view.getId() == R.id.menu_profile){
            transaction.replace(R.id.cont_main, profileFragment);

            icMenuProfile.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
            labelMenuProfile.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
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

        menuFAQ.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        menuPrivacy.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        menuTerms.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        menuCookies.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
    }
}
