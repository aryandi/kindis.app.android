package co.digdaya.kindis.live.view.fragment.bottomnavigation.playlist;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.view.adapter.tab.AdapterTabPlaylist;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewPlaylist extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;

    AdapterTabPlaylist adapterTabPlaylist;

    String[] title = {"PRIVATE","PAID"};


    public NewPlaylist() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_playlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = (TabLayout) view.findViewById(R.id.htab_tabs);
        viewPager = (ViewPager) view.findViewById(R.id.htab_viewpager);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setText("PRIVATE"));
        tabLayout.addTab(tabLayout.newTab().setText("PAID"));

        adapterTabPlaylist = new AdapterTabPlaylist(getChildFragmentManager(), getContext(), title);
        viewPager.setAdapter(adapterTabPlaylist);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapterTabPlaylist.getTabView(i));
        }
    }
}
