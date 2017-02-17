package sangmaneproject.kindis.view.fragment.bottomnavigation;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import me.relex.circleindicator.CircleIndicator;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.CheckConnection;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.view.adapter.AdapterMusiq;
import sangmaneproject.kindis.view.adapter.AdapterMusiqSlider;

/**
 * A simple {@link Fragment} subclass.
 */
public class Taklim extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPager imageSlider;
    AdapterMusiqSlider adapterMusiqSlider;
    AdapterMusiq adapter;

    NestedScrollView emptyState;
    Button refresh;
    ProgressDialog loading;

    String responses = null;
    String[] title = {"MOST PLAYED","RECENTLY","CATEGORY"};

    public Taklim() {
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
        tabLayout.addTab(tabLayout.newTab().setText("RECENTLY"));
        tabLayout.addTab(tabLayout.newTab().setText("CATEGORY"));

        emptyState = (NestedScrollView) view.findViewById(R.id.empty_state);
        refresh = (Button) view.findViewById(R.id.btn_refresh);
        loading = new ProgressDialog(getActivity());
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage("Loading. Please wait...");

        if (responses != null){
            adapter = new AdapterMusiq(getChildFragmentManager(), getContext(), tabLayout.getTabCount(), responses, title);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(3);
            tabLayout.setupWithViewPager(viewPager);

            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.setCustomView(adapter.getTabView(i));
            }
        }else {
            setLayout();
        }

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLayout();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setLayout(){
        if (new CheckConnection().isInternetAvailable(getContext())){
            getJSON();
            viewPager.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }else {
            viewPager.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        }
    }

    private void getJSON(){
        loading.show();
        new VolleyHelper().get(ApiHelper.TAKLIM, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                if (status){
                    responses = response;
                    adapter = new AdapterMusiq(getChildFragmentManager(), getContext(), tabLayout.getTabCount(), response, title);
                    viewPager.setAdapter(adapter);
                    viewPager.setOffscreenPageLimit(3);
                    tabLayout.setupWithViewPager(viewPager);

                    for (int i = 0; i < tabLayout.getTabCount(); i++) {
                        TabLayout.Tab tab = tabLayout.getTabAt(i);
                        tab.setCustomView(adapter.getTabView(i));
                    }
                }else {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
