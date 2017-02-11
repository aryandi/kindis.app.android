package sangmaneproject.kindis.view.fragment.bottomnavigation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.relex.circleindicator.CircleIndicator;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.view.adapter.AdapterMusiq;
import sangmaneproject.kindis.view.adapter.AdapterMusiqSlider;

/**
 * A simple {@link Fragment} subclass.
 */
public class Musiq extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPager imageSlider;
    AdapterMusiqSlider adapterMusiqSlider;

    public Musiq() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_musiq, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = (TabLayout) view.findViewById(R.id.htab_tabs);
        viewPager = (ViewPager) view.findViewById(R.id.htab_viewpager);
        imageSlider = (ViewPager) view.findViewById(R.id.viewpager_slider);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);

        //imageslider
        adapterMusiqSlider = new AdapterMusiqSlider(getActivity());
        imageSlider.setAdapter(adapterMusiqSlider);
        indicator.setViewPager(imageSlider);
        adapterMusiqSlider.registerDataSetObserver(indicator.getDataSetObserver());

        //tab
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setText("MOST PLAYED"));
        tabLayout.addTab(tabLayout.newTab().setText("RECENTLY ADDED"));
        tabLayout.addTab(tabLayout.newTab().setText("GENRES"));

        AdapterMusiq adapter = new AdapterMusiq(getFragmentManager(), getContext(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /*private void setupViewPager(ViewPager viewPager) {
        AdapterMusiq adapter = new AdapterMusiq(getFragmentManager());
        adapter.addFragment(new MostPlayed(), "Most Played");
        adapter.addFragment(new RecentlyAdded(), "Recently Added");
        adapter.addFragment(new Genres(), "Genres");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }*/
}
