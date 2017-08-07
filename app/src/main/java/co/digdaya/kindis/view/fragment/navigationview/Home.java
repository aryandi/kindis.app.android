package co.digdaya.kindis.view.fragment.navigationview;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.PlayerSessionHelper;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerFragment;
import co.digdaya.kindis.view.activity.Search;
import co.digdaya.kindis.view.fragment.bottomnavigation.Infaq;
import co.digdaya.kindis.view.fragment.bottomnavigation.musiq.Musiq;
import co.digdaya.kindis.view.fragment.bottomnavigation.playlist.NewPlaylist;
import co.digdaya.kindis.view.fragment.bottomnavigation.taklim.Taklim;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends BottomPlayerFragment implements View.OnClickListener, AHBottomNavigation.OnTabSelectedListener {
    DrawerLayout drawer;
    AHBottomNavigation bottomNavigation;
    FrameLayout contHome;
    TextView titleToolbar;

    Fragment musiqFragment;
    Fragment taklimFragment;
    Fragment infaqFragment;
    Fragment playlistFragment;

    ImageButton btnDrawer;
    ImageButton btnSearch;

    public Home() {
    }

    public Home(DrawerLayout drawer) {
        this.drawer = drawer;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigation = (AHBottomNavigation) view.findViewById(R.id.bottom_navigation);
        contHome = (FrameLayout) view.findViewById(R.id.cont_home);

        btnDrawer = (ImageButton) view.findViewById(R.id.btn_drawer);
        btnSearch = (ImageButton) view.findViewById(R.id.btn_search);
        titleToolbar = (TextView) view.findViewById(R.id.title);

        musiqFragment = new Musiq();
        taklimFragment = new Taklim();
        infaqFragment = new Infaq();
        playlistFragment = new NewPlaylist();

        AHBottomNavigationItem musiq = new AHBottomNavigationItem(R.string.musiq, R.drawable.ic_explore, R.color.new_white);
        AHBottomNavigationItem taklim = new AHBottomNavigationItem(R.string.taklim, R.drawable.ic_taklim, R.color.new_white);
        AHBottomNavigationItem infaq = new AHBottomNavigationItem(R.string.infaq, R.drawable.ic_infaq, R.color.new_white);
        AHBottomNavigationItem playlist = new AHBottomNavigationItem(R.string.playlist, R.drawable.ic_playlist, R.color.new_white);

        bottomNavigation.addItem(musiq);
        bottomNavigation.addItem(taklim);
        bottomNavigation.addItem(infaq);
        bottomNavigation.addItem(playlist);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#212121"));
        bottomNavigation.setAccentColor(Color.parseColor("#cccccc"));
        bottomNavigation.setInactiveColor(Color.parseColor("#626262"));
        bottomNavigation.setForceTint(true);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setOnTabSelectedListener(this);
        bottomNavigation.setCurrentItem(0);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cont_home, musiqFragment);
        transaction.commit();

        btnDrawer.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_drawer:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_search:
                Intent intent = new Intent(getActivity(), Search.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        if (new PlayerSessionHelper().getPreferences(getContext(), "file").isEmpty()) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            switch (metrics.densityDpi) {
                case DisplayMetrics.DENSITY_MEDIUM:
                    params.setMargins(0, 56, 0, 0);
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    params.setMargins(0, 80, 0, 0);
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    params.setMargins(0, 112, 0, 0);
                    break;
                default:
                    params.setMargins(0, 112, 0, 0);
                    break;
            }
            contHome.setLayoutParams(params);
        }
        super.onResume();
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        FragmentTransaction transactionBottomNavigation = getActivity().getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                titleToolbar.setText("Explore Musiq");
                transactionBottomNavigation.replace(R.id.cont_home, musiqFragment);
                transactionBottomNavigation.commit();
                break;
            case 1:
                titleToolbar.setText("Explore Taklim");
                transactionBottomNavigation.replace(R.id.cont_home, taklimFragment);
                transactionBottomNavigation.commit();
                break;
            case 2:
                titleToolbar.setText("Infaq");
                transactionBottomNavigation.replace(R.id.cont_home, infaqFragment);
                transactionBottomNavigation.commit();
                break;
            case 3:
                titleToolbar.setText("Playlist");
                transactionBottomNavigation.replace(R.id.cont_home, playlistFragment);
                transactionBottomNavigation.commit();
                break;
        }
        return true;
    }
}
