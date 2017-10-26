package co.digdaya.kindis.live.view.fragment.bottomnavigation;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.model.InfaqModel;
import co.digdaya.kindis.live.util.SpacingItem.SpacingItemInfaq;
import co.digdaya.kindis.live.view.adapter.AdapterBanner;
import co.digdaya.kindis.live.view.adapter.AdapterBannerEmpty;
import co.digdaya.kindis.live.view.adapter.item.AdapterInfaq;
import co.digdaya.kindis.live.view.dialog.DialogLoading;
import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class Infaq extends Fragment {
    ViewPager imageSlider;
    CircleIndicator indicator;
    DialogLoading loading;

    AdapterInfaq adapterInfaq;
    AdapterBanner adapterBanner;
    AdapterBannerEmpty adapterBannerEmpty;
    RecyclerView listViewInfaq;
    NestedScrollView nestedScrollView;

    ArrayList<HashMap<String, String>> listBanner = new ArrayList<>();
    Gson gson;
    InfaqModel infaqModel;

    String responses = null;
    String urlMore;
    Boolean isLastItem = false;

    Timer timer;
    Runnable Update;
    Handler handler;

    int currentPage = 0;
    final long DELAY_MS = 2000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    int NUM_PAGES;
    boolean isSlide = false;

    public Infaq() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_infaq, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loading = new DialogLoading(getActivity());
        gson = new Gson();

        imageSlider = (ViewPager) view.findViewById(R.id.viewpager_slider);
        indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedscrollview);

        listViewInfaq = (RecyclerView) view.findViewById(R.id.list_infaq);
        listViewInfaq.setLayoutManager(new GridLayoutManager(getContext(),2));
        listViewInfaq.addItemDecoration(new SpacingItemInfaq(getContext()));
        listViewInfaq.setNestedScrollingEnabled(false);

        getBanner();

        if (responses != null){
            InfaqModel infaqModel = gson.fromJson(responses, InfaqModel.class);
            adapterInfaq = new AdapterInfaq(infaqModel, getContext());
            listViewInfaq.setAdapter(adapterInfaq);
        }else {
            getInfaq();
        }

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) && urlMore.length()>10) {
                    loadMore();
                }
            }
        });
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

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void getInfaq(){
        loading.showLoading();
        new VolleyHelper().get(ApiHelper.LIST_INFAQ, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismisLoading();
                if (status){
                    infaqModel = gson.fromJson(response, InfaqModel.class);
                    adapterInfaq = new AdapterInfaq(infaqModel, getContext());
                    listViewInfaq.setAdapter(adapterInfaq);
                    urlMore = infaqModel.next_page;
                }
            }
        });
    }

    private void getBanner(){
        listBanner.clear();
        new VolleyHelper().get(ApiHelper.ADS_INFAQ, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONArray result = object.getJSONArray("result");
                            for (int i=0; i<result.length(); i++){
                                JSONObject data = result.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("uid", data.getString("uid"));
                                map.put("title", data.getString("title"));
                                map.put("image", data.getString("banner_image"));
                                map.put("is_url", data.getString("is_url"));
                                map.put("redirect_url", data.getString("redirect_url"));
                                listBanner.add(map);
                            }
                            adapterBanner = new AdapterBanner(getActivity(), listBanner, "infaq");
                            imageSlider.setAdapter(adapterBanner);
                            if (result.length()>1){
                                indicator.setViewPager(imageSlider);
                                adapterBanner.registerDataSetObserver(indicator.getDataSetObserver());
                                NUM_PAGES = result.length();
                                autoSlide();
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
            }
        });
    }

    private void loadMore(){
        new VolleyHelper().get(urlMore, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismisLoading();
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("result");
                        for (int i=0; i<array.length(); i++){
                            JSONObject data = array.getJSONObject(i);
                            InfaqModel.Result result = gson.fromJson(data.toString(), InfaqModel.Result.class);
                            infaqModel.result.add(result);
                            adapterInfaq.notifyDataSetChanged();
                        }
                        adapterInfaq.notifyDataSetChanged();
                        urlMore = object.getString("next_page");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
}
