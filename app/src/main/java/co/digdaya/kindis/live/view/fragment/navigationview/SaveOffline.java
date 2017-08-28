package co.digdaya.kindis.live.view.fragment.navigationview;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.view.adapter.tab.AdapterTabSaveOffline;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaveOffline extends Fragment implements View.OnClickListener {
    DrawerLayout drawer;
    ImageButton btnDrawer;
    TabLayout tabLayout;
    ViewPager viewPager;
    AdapterTabSaveOffline adapterTabSaveOffline;
    String[] title = {"SINGLE","ALBUM","PLAYLIST"};

    public SaveOffline() {
        // Required empty public constructor
    }

    public SaveOffline(DrawerLayout drawer) {
        this.drawer = drawer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_save_offline, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initTab();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_drawer:
                drawer.openDrawer(GravityCompat.START);
                break;
        }
    }

    private void initView(View view){
        btnDrawer = (ImageButton) view.findViewById(R.id.btn_drawer);
        tabLayout = (TabLayout) view.findViewById(R.id.htab_tabs);
        viewPager = (ViewPager) view.findViewById(R.id.htab_viewpager);

        btnDrawer.setOnClickListener(this);
    }

    private void initTab(){
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setText("SINGLE"));
        tabLayout.addTab(tabLayout.newTab().setText("ALBUM"));
        tabLayout.addTab(tabLayout.newTab().setText("PLAYLIST"));

        adapterTabSaveOffline = new AdapterTabSaveOffline(getChildFragmentManager(), getContext(), title);
        viewPager.setAdapter(adapterTabSaveOffline);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapterTabSaveOffline.getTabView(i));
        }
    }
}
