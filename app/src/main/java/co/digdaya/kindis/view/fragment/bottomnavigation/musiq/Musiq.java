package co.digdaya.kindis.view.fragment.bottomnavigation.musiq;


import android.app.ProgressDialog;
import android.os.Bundle;
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

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.AnalyticHelper;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.CheckConnection;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.view.adapter.AdapterBanner;
import co.digdaya.kindis.view.adapter.AdapterBannerEmpty;
import co.digdaya.kindis.view.adapter.tab.AdapterMusiq;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class Musiq extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPager imageSlider;
    SwipeRefreshLayout swipeRefreshLayout;

    AdapterBanner adapterBanner;
    AdapterBannerEmpty adapterBannerEmpty;
    AdapterMusiq adapter;

    NestedScrollView emptyState;
    Button refresh;
    ProgressDialog loading;

    ArrayList<HashMap<String, String>> listBanner = new ArrayList<>();
    String[] title = {"DISCOVER","RECENTLY","GENRE"};

    CircleIndicator indicator;
    Gson gson;
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

        new AnalyticHelper(getActivity()).event("home/music");

        tabLayout = (TabLayout) view.findViewById(R.id.htab_tabs);
        viewPager = (ViewPager) view.findViewById(R.id.htab_viewpager);
        imageSlider = (ViewPager) view.findViewById(R.id.viewpager_slider);
        indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        gson = new Gson();

        //tab
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setText("DISCOVER"));
        tabLayout.addTab(tabLayout.newTab().setText("RECENTLY"));
        tabLayout.addTab(tabLayout.newTab().setText("GENRE"));

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
        new VolleyHelper().get(ApiHelper.MUSIQ+new SessionHelper().getPreferences(getContext(), "user_id"), new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            adapter = new AdapterMusiq(getChildFragmentManager(), getContext(), 3, response, title);
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getBanner(){
        listBanner.clear();
        new VolleyHelper().get(ApiHelper.ADS_BANNER+new SessionHelper().getPreferences(getContext(), "user_id"), new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    System.out.println("getBanner: "+response);
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
                            adapterBanner = new AdapterBanner(getActivity(), listBanner, "");
                            imageSlider.setAdapter(adapterBanner);
                            if (result.length()>1){
                                indicator.setViewPager(imageSlider);
                                adapterBanner.registerDataSetObserver(indicator.getDataSetObserver());
                            }
                        }else {
                            if (getActivity()!=null){
                                adapterBannerEmpty = new AdapterBannerEmpty(getContext());
                                imageSlider.setAdapter(adapterBannerEmpty);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    adapterBannerEmpty = new AdapterBannerEmpty(getContext());
                    imageSlider.setAdapter(adapterBannerEmpty);
                }

                if (isAdded()){
                    setLayout();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getJSON();
        swipeRefreshLayout.setRefreshing(false);
    }
}
