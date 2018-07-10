package co.digdaya.kindis.live.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.MobileAds;

import org.w3c.dom.Text;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.AnalyticHelper;
import co.digdaya.kindis.live.helper.Constanta;
import co.digdaya.kindis.live.helper.PlayerActionHelper;
import co.digdaya.kindis.live.helper.PlayerSessionHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.CheckPermission;
import co.digdaya.kindis.live.service.PlayerService;
import co.digdaya.kindis.live.view.activity.Detail.Detail;
import co.digdaya.kindis.live.view.activity.Detail.DetailArtist;
import co.digdaya.kindis.live.view.activity.Player.Player;
import co.digdaya.kindis.live.view.dialog.DialogBanner;
import co.digdaya.kindis.live.view.dialog.DialogGift;
import co.digdaya.kindis.live.view.fragment.navigationview.Cookies;
import co.digdaya.kindis.live.view.fragment.navigationview.FAQ;
import co.digdaya.kindis.live.view.fragment.navigationview.Home;
import co.digdaya.kindis.live.view.fragment.navigationview.Intelectual;
import co.digdaya.kindis.live.view.fragment.navigationview.Notification;
import co.digdaya.kindis.live.view.fragment.navigationview.Privacy;
import co.digdaya.kindis.live.view.fragment.navigationview.Profile;
import co.digdaya.kindis.live.view.fragment.navigationview.SaveOffline;
import co.digdaya.kindis.live.view.fragment.navigationview.Terms;

public class Main extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Main";
    SessionHelper sessionHelper;
    DrawerLayout drawer;
    CheckPermission checkPermission;

    FragmentTransaction transaction;
    Fragment profileFragment;
    Fragment saveOffline;
    Fragment homeFragment;
    Fragment notifFragment;
    Fragment faqFragment;
    Fragment privacyFragment;
    Fragment termsFragment;
    Fragment cookiesFragment;
    Fragment intelectualFragment;

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
    TextView menuIR;
    Button profileStatus;

    ImageView icMenuHome, icMenuNotif, icMenuProfile, icMenuOffline;
    TextView labelMenuHome, labelMenuNotif, labelMenuProfile, labelMenuOffline;
    Boolean exit = false;

    Dialog dialogPremium, dialogGft, dialogBnnr;

    DialogGift dialogGift;
    DialogBanner dialogBanner;
    private boolean is_premium;
    private AnalyticHelper analyticHelper;

    public Main() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_layout);

        checkPermission = new CheckPermission(this);
        dialogBanner = new DialogBanner(this, dialogBnnr);
        dialogBanner.showDialog();

        MobileAds.initialize(this, getString(R.string.ads_app_id));

        homeFragment = new Home(drawer);
        profileFragment = new Profile(drawer);
        saveOffline = new SaveOffline(drawer);
        notifFragment = new Notification(drawer);
        faqFragment = new FAQ(drawer);
        privacyFragment = new Privacy(drawer);
        termsFragment = new Terms(drawer);
        cookiesFragment = new Cookies(drawer);
        intelectualFragment = new Intelectual(drawer);
        sessionHelper = new SessionHelper();
        analyticHelper = new AnalyticHelper(this);

        //sidebar
        profilePicture = findViewById(R.id.profile_picture);
        fullname = findViewById(R.id.fullname);
        menuHome = findViewById(R.id.menu_home);
        menuProfile = findViewById(R.id.menu_profile);
        menuOffline = findViewById(R.id.menu_offline);
        menuNotif = findViewById(R.id.menu_notif);
        menuPremium = findViewById(R.id.menu_premium);
        menuGift = findViewById(R.id.menu_gift);
        menuFAQ = findViewById(R.id.menu_faq);
        menuPrivacy = findViewById(R.id.menu_privacy);
        menuTerms = findViewById(R.id.menu_terms);
        menuCookies = findViewById(R.id.menu_cookies);
        menuIR = findViewById(R.id.menu_ir);

        icMenuHome = findViewById(R.id.ic_menu_home);
        icMenuNotif = findViewById(R.id.ic_menu_notif);
        icMenuProfile = findViewById(R.id.ic_menu_profile);
        icMenuOffline = findViewById(R.id.ic_menu_offline);

        labelMenuHome = findViewById(R.id.label_menu_home);
        labelMenuNotif = findViewById(R.id.label_menu_notif);
        labelMenuProfile = findViewById(R.id.label_menu_profile);
        labelMenuOffline = findViewById(R.id.label_menu_offline);

        profileStatus = findViewById(R.id.profile_status);

        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cont_main, homeFragment, "home");
        transaction.commit();

        is_premium = sessionHelper.getPreferences(getApplicationContext(), "is_premium").equals("1");

        initSidebar();

//        if (!checkPermission.checkPermission()){
//            checkPermission.showPermission(1);
//        }

        new AnalyticHelper(this);
        System.out.println("token_access: " + sessionHelper.getPreferences(getApplicationContext(), "token_access"));

        onNotificationClicked();
    }

    private void onNotificationClicked() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle extras = intent.getExtras();
            String type = extras.getString(Constanta.INTENT_EXTRA_TYPE);
            String id = extras.getString(Constanta.INTENT_EXTRA_ID);

            Log.d(TAG, "type and id: " + type + " " + id);

            if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(id))
            switch (type) {
                case "playlist":
                    intent = new Intent(this, Detail.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("uid", id);
                    intent.putExtra(Constanta.INTENT_EXTRA_TYPE, "premium");
                    intent.putExtra("isMyPlaylist", "");
//                intent.putExtra("playlisttype", getItemViewType(position));
                    startActivity(intent);
                    break;
                case "album":
                    intent = new Intent(this, Detail.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("uid", id);
                    intent.putExtra(Constanta.INTENT_EXTRA_TYPE, "album");
                    startActivity(intent);
                    break;
                case "artist":
                    intent = new Intent(this, DetailArtist.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("uid", id);
                    intent.putExtra(Constanta.INTENT_EXTRA_TYPE, "artist");
                    startActivity(intent);
                    break;
                case "single":
                    intent = new Intent(this, Player.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("uid", id);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().findFragmentByTag("home").isVisible()) {
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
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        is_premium = sessionHelper.getPreferences(getApplicationContext(), "is_premium").equals("1");
        if (is_premium) {
            profileStatus.setText("PREMIUM");
            profileStatus.setBackground(getDrawable(R.drawable.button_rounded_orange));
        }

        if (sessionHelper.getPreferences(getApplicationContext(), "profile_picture").length() > 10) {
            Glide.with(getApplicationContext())
                    .load(sessionHelper.getPreferences(getApplicationContext(), "profile_picture"))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(profilePicture);
        }
    }

    private void initSidebar() {
        if (is_premium) {
            menuPremium.setVisibility(View.GONE);
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
        menuIR.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        setDefaultSelectedMenuColor();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (view.getId() == R.id.menu_home) {
            analyticHelper.clickMenu("menu", "Home");
            transaction.replace(R.id.cont_main, homeFragment);
            icMenuHome.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
            labelMenuHome.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        } else if (view.getId() == R.id.menu_notif) {
            analyticHelper.clickMenu("menu", "Notification");
            transaction.replace(R.id.cont_main, notifFragment);

            icMenuNotif.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
            labelMenuNotif.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        } else if (view.getId() == R.id.menu_premium) {
            analyticHelper.clickMenu("menu", "Premium");
            Intent intent = new Intent(this, Premium.class);
            startActivity(intent);
        } else if (view.getId() == R.id.menu_profile) {
            analyticHelper.clickMenu("menu", "Profile");
            transaction.replace(R.id.cont_main, profileFragment);

            icMenuProfile.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
            labelMenuProfile.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        } else if (view.getId() == R.id.menu_offline) {
            analyticHelper.clickMenu("menu", "Save Offline");
            if (is_premium) {
                transaction.replace(R.id.cont_main, saveOffline);

                icMenuOffline.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
                labelMenuOffline.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
            } else {
                Intent intent = new Intent(this, Premium.class);
                startActivity(intent);
            }
        } else if (view.getId() == R.id.menu_gift) {
            analyticHelper.clickMenu("menu", "Gift");
            dialogGift = new DialogGift(dialogGft, this);
            dialogGift.showDialog();
        } else if (view.getId() == R.id.menu_faq) {
            analyticHelper.clickMenu("help", "FAQ");
            transaction.replace(R.id.cont_main, faqFragment);

            menuFAQ.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        } else if (view.getId() == R.id.menu_privacy) {
            analyticHelper.clickMenu("help", "Privacy");
            transaction.replace(R.id.cont_main, privacyFragment);

            menuPrivacy.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        } else if (view.getId() == R.id.menu_terms) {
            analyticHelper.clickMenu("help", "Terms");
            transaction.replace(R.id.cont_main, termsFragment);

            menuTerms.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        } else if (view.getId() == R.id.menu_cookies) {
            analyticHelper.clickMenu("help", "Cookies");
            transaction.replace(R.id.cont_main, cookiesFragment);

            menuCookies.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        } else if (view.getId() == menuIR.getId()) {
            analyticHelper.clickMenu("help", "Rights");
            transaction.replace(R.id.cont_main, intelectualFragment);
            menuIR.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.jungle_green));
        }
        transaction.addToBackStack(null);
        transaction.commit();
        drawer.closeDrawers();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("onRequestPermissionsResultMain: " + permissions + "\n" + grantResults + "\n" + requestCode);
        if (requestCode == 2) {
            if (checkPermission.checkPermission()) {
                new Profile().startDialogPhoto(this);
            }
        }
    }

    private void setDefaultSelectedMenuColor() {
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
        menuIR.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
                Log.d("Activity", "ON RESULT CALLED");
            }
        } catch (Exception e) {
            Log.d("ERROR", e.toString());
        }
    }
}
