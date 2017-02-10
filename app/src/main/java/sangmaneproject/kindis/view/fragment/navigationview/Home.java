package sangmaneproject.kindis.view.fragment.navigationview;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import sangmaneproject.kindis.PlayerService;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.PlayerActionHelper;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.view.activity.Player;
import sangmaneproject.kindis.view.activity.Search;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener {
    DrawerLayout drawer;
    AHBottomNavigation bottomNavigation;
    FragmentTransaction transaction;
    Fragment musiqFragment;
    ImageButton btnDrawer;
    ImageButton btnSearch;

    //bottom player
    ImageButton expand;
    RelativeLayout btnPlay;
    ImageView icPlay;
    ProgressBar progressBar;
    int duration;
    int progress;

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
        transaction = getActivity().getSupportFragmentManager().beginTransaction();

        btnDrawer = (ImageButton) view.findViewById(R.id.btn_drawer);
        btnSearch = (ImageButton) view.findViewById(R.id.btn_search);

        //bottom player
        expand = (ImageButton) view.findViewById(R.id.btn_expand);
        btnPlay = (RelativeLayout) view.findViewById(R.id.btn_play);
        icPlay = (ImageView) view.findViewById(R.id.ic_play);
        progressBar = (ProgressBar) view.findViewById(R.id.pb);

        musiqFragment = new Musiq();

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

        btnDrawer.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        bottomPlayer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
        if (new PlayerSessionHelper().getPreferences(getContext(), "isplaying").equals("true")){
            icPlay.setImageResource(R.drawable.ic_pause);
        }else {
            icPlay.setImageResource(R.drawable.ic_play);
        }

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter(PlayerActionHelper.BROADCAST));
        super.onResume();
    }

    private void bottomPlayer(){
        expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Player.class);
                startActivity(intent);
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!new PlayerSessionHelper().getPreferences(getContext(), "isplaying").equals("true")){
                    new PlayerSessionHelper().setPreferences(getContext(), "isplaying", "true");
                    icPlay.setImageResource(R.drawable.ic_pause);
                    Intent intent = new Intent(getActivity(), PlayerService.class);
                    intent.setAction(PlayerActionHelper.ACTION_PLAY);
                    getActivity().startService(intent);
                }else {
                    new PlayerSessionHelper().setPreferences(getContext(), "isplaying", "false");
                    icPlay.setImageResource(R.drawable.ic_play);
                    Intent intent = new Intent(getActivity(), PlayerService.class);
                    intent.setAction(PlayerActionHelper.ACTION_PAUSE);
                    getActivity().startService(intent);
                }
            }
        });
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            duration = intent.getIntExtra(PlayerActionHelper.BROADCAST_MAX_DURATION, 100);
            progress = intent.getIntExtra(PlayerActionHelper.BROADCAST_CURRENT_DURATION, 0);
            Log.d("homereceiver", "Got message: " + duration + " : " + progress);

            progressBar.setMax(duration);
            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(progress);
                }
            });

            if (progress==duration){
                icPlay.setImageResource(R.drawable.ic_play);
            }
        }
    };
}
