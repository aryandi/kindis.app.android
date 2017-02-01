package sangmaneproject.kindis.view.fragment;


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

        adapterMusiqSlider = new AdapterMusiqSlider(getActivity());

        tabLayout.setupWithViewPager(viewPager);

        imageSlider.setAdapter(adapterMusiqSlider);
        indicator.setViewPager(imageSlider);
        adapterMusiqSlider.registerDataSetObserver(indicator.getDataSetObserver());
    }

    @Override
    public void onResume() {
        super.onResume();
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        AdapterMusiq adapter = new AdapterMusiq(getFragmentManager());
        adapter.addFragment(new MostPlayed(), "Most Played");
        adapter.addFragment(new RecentlyAdded(), "Recently Added");
        adapter.addFragment(new Genres(), "Genres");
        viewPager.setAdapter(adapter);
    }
}
