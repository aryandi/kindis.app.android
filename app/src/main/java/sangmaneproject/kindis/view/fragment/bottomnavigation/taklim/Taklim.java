package sangmaneproject.kindis.view.fragment.bottomnavigation.taklim;


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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import me.relex.circleindicator.CircleIndicator;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.CheckConnection;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.view.adapter.AdapterBannerEmpty;
import sangmaneproject.kindis.view.adapter.AdapterMusiqSlider;
import sangmaneproject.kindis.view.adapter.tab.AdapterTaklim;

/**
 * A simple {@link Fragment} subclass.
 */
public class Taklim extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPager imageSlider;

    AdapterMusiqSlider adapterMusiqSlider;
    AdapterBannerEmpty adapterBannerEmpty;
    AdapterTaklim adapter;

    NestedScrollView emptyState;
    Button refresh;
    ProgressDialog loading;

    ArrayList<HashMap<String, String>> listBanner = new ArrayList<>();
    String responses = null;
    String[] title = {"SYIAR","KISAH","MUROTTAL"};

    CircleIndicator indicator;
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
        indicator = (CircleIndicator) view.findViewById(R.id.indicator);

        //tab
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));

        emptyState = (NestedScrollView) view.findViewById(R.id.empty_state);
        refresh = (Button) view.findViewById(R.id.btn_refresh);
        loading = new ProgressDialog(getActivity(), R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);

        getBanner();

        if (responses != null){
            adapter = new AdapterTaklim(getChildFragmentManager(), getContext(), tabLayout.getTabCount(), responses, title);
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
        new VolleyHelper().get(ApiHelper.TAKLIM+new SessionHelper().getPreferences(getContext(), "user_id"), new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            responses = response;
                            adapter = new AdapterTaklim(getChildFragmentManager(), getContext(), tabLayout.getTabCount(), response, title);
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
        new VolleyHelper().get(ApiHelper.ADS_BANNER, new VolleyHelper.HttpListener<String>() {
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
                                map.put("title", data.getString("title"));
                                map.put("image", data.getString("image_path"));
                                map.put("link", data.getString("url_link"));
                                listBanner.add(map);
                            }
                            adapterMusiqSlider = new AdapterMusiqSlider(getActivity(), listBanner);
                            imageSlider.setAdapter(adapterMusiqSlider);
                            indicator.setViewPager(imageSlider);
                            adapterMusiqSlider.registerDataSetObserver(indicator.getDataSetObserver());
                        }else {
                            adapterBannerEmpty = new AdapterBannerEmpty(getContext());
                            imageSlider.setAdapter(adapterBannerEmpty);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
