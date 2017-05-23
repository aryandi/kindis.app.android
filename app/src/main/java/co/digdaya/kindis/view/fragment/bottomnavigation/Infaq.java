package co.digdaya.kindis.view.fragment.bottomnavigation;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.model.InfaqModel;
import co.digdaya.kindis.view.dialog.DialogLoading;
import co.digdaya.kindis.util.SpacingItem.SpacingItemInfaq;
import co.digdaya.kindis.view.adapter.AdapterBanner;
import co.digdaya.kindis.view.adapter.AdapterBannerEmpty;
import co.digdaya.kindis.view.adapter.item.AdapterInfaq;
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

    ArrayList<HashMap<String, String>> listBanner = new ArrayList<>();
    Gson gson;

    String responses = null;
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
    }

    private void getInfaq(){
        loading.showLoading();
        new VolleyHelper().get(ApiHelper.LIST_INFAQ, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismisLoading();
                if (status){
                    InfaqModel infaqModel = gson.fromJson(response, InfaqModel.class);
                    adapterInfaq = new AdapterInfaq(infaqModel, getContext());
                    listViewInfaq.setAdapter(adapterInfaq);
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
}
