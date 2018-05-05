package co.digdaya.kindis.live.view.fragment.bottomnavigation.taklim;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import co.digdaya.kindis.live.helper.AnalyticHelper;
import me.relex.circleindicator.CircleIndicator;
import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.CheckConnection;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.view.adapter.AdapterBannerEmpty;
import co.digdaya.kindis.live.view.adapter.AdapterBanner;
import co.digdaya.kindis.live.view.adapter.tab.AdapterTaklim;

/**
 * A simple {@link Fragment} subclass.
 */
public class Taklim extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPager imageSlider;
    SwipeRefreshLayout swipeRefreshLayout;

    AdapterBanner adapterBanner;
    AdapterBannerEmpty adapterBannerEmpty;
    AdapterTaklim adapter;

    NestedScrollView emptyState;
    Button refresh;
    ProgressDialog loading;

    ArrayList<HashMap<String, String>> listBanner = new ArrayList<>();
    String[] title = {"Syiar","Kisah","Murottal"};

    CircleIndicator indicator;

    Timer timer;
    Runnable Update;
    Handler handler;

    int currentPage = 0;
    final long DELAY_MS = 5000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 5000;
    int NUM_PAGES;
    boolean isSlide = false;
    private AnalyticHelper analyticHelper;

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

        analyticHelper = new AnalyticHelper(getActivity());
        analyticHelper.event("home/taklim");

        tabLayout = (TabLayout) view.findViewById(R.id.htab_tabs);
        viewPager = (ViewPager) view.findViewById(R.id.htab_viewpager);
        imageSlider = (ViewPager) view.findViewById(R.id.viewpager_slider);
        indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);

        //tab
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setText("Syiar"));
        tabLayout.addTab(tabLayout.newTab().setText("Kisah"));
        tabLayout.addTab(tabLayout.newTab().setText("Murottal"));

        emptyState = (NestedScrollView) view.findViewById(R.id.empty_state);
        refresh = (Button) view.findViewById(R.id.btn_refresh);
        loading = new ProgressDialog(getActivity(), R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);

        getBanner();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLayout();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isSlide){
            timer = new Timer();
            timer .schedule(new TimerTask() { // task to be scheduled

                @Override
                public void run() {
                    handler.post(Update);
                }
            }, DELAY_MS, PERIOD_MS);
        }
    }

    private void setLayout(){
        if (new CheckConnection().isInternetAvailable(getActivity())){
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
        new VolleyHelper().get(ApiHelper.TAKLIM+new SessionHelper().getPreferences(getActivity(), "user_id"), new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
                            if (getActivity() != null) {
                                adapter = new AdapterTaklim(getChildFragmentManager(), getActivity(), 3, response, title);
                                viewPager.setAdapter(adapter);
                                viewPager.setOffscreenPageLimit(3);
                                tabLayout.setupWithViewPager(viewPager);
                                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                                    tab.setCustomView(adapter.getTabView(i));
                                }
                            }
                        }else {
                            setLayout();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getBanner(){
        listBanner.clear();
        System.out.println("getBannertaklim: "+ApiHelper.ADS_BANNER+new SessionHelper().getPreferences(getActivity(), "user_id")+"&dev_id=2&channel_id=9");
        new VolleyHelper().get(ApiHelper.ADS_BANNER+new SessionHelper().getPreferences(getActivity(), "user_id")+"&dev_id=2&channel_id=9", new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    System.out.println("jsonbannertaklim: "+response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONArray result = object.getJSONArray("result");
                            for (int i=0; i<result.length(); i++){
                                JSONObject data = result.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("title", data.getString("title"));
                                map.put("image", data.getString("image"));
                                map.put("link", data.getString("click_url"));
                                listBanner.add(map);
                            }
                            if (getActivity() != null) {
                                adapterBanner = new AdapterBanner(getActivity(), listBanner, "taklim", analyticHelper);
                                imageSlider.setAdapter(adapterBanner);
                                if (result.length() > 1) {
                                    indicator.setViewPager(imageSlider);
                                    adapterBanner.registerDataSetObserver(indicator.getDataSetObserver());
                                    NUM_PAGES = result.length();
//                                    imageSlider.setOnScrollChangeListener(Taklim.this);
                                    autoSlide();
                                }
                            }
                        }else {
                            if (getActivity() != null) {
                                adapterBannerEmpty = new AdapterBannerEmpty(getActivity());
                                imageSlider.setAdapter(adapterBannerEmpty);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    if (getActivity() != null) {
                        adapterBannerEmpty = new AdapterBannerEmpty(getActivity());
                        imageSlider.setAdapter(adapterBannerEmpty);
                    }
                }
                setLayout();
            }
        });
    }

    @Override
    public void onRefresh() {
        getJSON();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer!=null){
            timer.cancel();
        }
    }

    private void autoSlide(){
        isSlide = true;
        handler = new Handler();
        Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                imageSlider.setCurrentItem(currentPage, true);
                currentPage++;
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer .schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

//    @Override
//    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
//        currentPage = imageSlider.getCurrentItem();
//    }


}
