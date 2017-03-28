package co.digdaya.kindis.view.fragment.bottomnavigation.taklim;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.view.adapter.item.AdapterArtist;
import co.digdaya.kindis.view.adapter.item.AdapterGenre;

/**
 * A simple {@link Fragment} subclass.
 */
public class Syiar extends Fragment {
    String json;

    TextView labelPremium, labelTop, labelDai, labelTheme;
    RecyclerView recyclerViewPremium, recyclerViewTop, recyclerViewDai, recyclerViewTheme;

    AdapterArtist adapterArtist;
    AdapterGenre adapterGenre;

    ArrayList<HashMap<String, String>> listArtist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listTheme = new ArrayList<HashMap<String, String>>();

    public Syiar(String json) {
        this.json = json;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_syiar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        labelPremium = (TextView) view.findViewById(R.id.label_premium);
        labelTop = (TextView) view.findViewById(R.id.label_top);
        labelDai = (TextView) view.findViewById(R.id.label_dai);
        labelTheme = (TextView) view.findViewById(R.id.label_theme);

        recyclerViewPremium = (RecyclerView) view.findViewById(R.id.list_premium);
        recyclerViewTop = (RecyclerView) view.findViewById(R.id.list_top);
        recyclerViewDai = (RecyclerView) view.findViewById(R.id.list_dai);
        recyclerViewTheme = (RecyclerView) view.findViewById(R.id.list_theme);

        recyclerViewPremium.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTop.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewDai.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTheme.setLayoutManager(new GridLayoutManager(getContext(),3));

        getJSON();
    }

    private void getJSON(){
        try {
            JSONObject object = new JSONObject(json);
            if (object.getBoolean("status")){
                JSONObject result = object.getJSONObject("result");
                JSONObject tab1 = result.getJSONObject("tab1");

                JSONArray premium = tab1.getJSONArray("premium");
                if (premium.length()>0){
                    labelPremium.setVisibility(View.VISIBLE);
                    recyclerViewPremium.setVisibility(View.VISIBLE);
                }

                JSONArray top = tab1.getJSONArray("top10premium");
                if (top.length()>0){
                    labelTop.setVisibility(View.VISIBLE);
                    recyclerViewTop.setVisibility(View.VISIBLE);
                }

                JSONArray dai = tab1.getJSONArray("dai");
                if (dai.length()>0){
                    labelDai.setVisibility(View.VISIBLE);
                    recyclerViewDai.setVisibility(View.VISIBLE);

                    for (int i=0; i<dai.length(); i++){
                        JSONObject data = dai.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("uid", data.optString("uid"));
                        map.put("name", data.optString("name"));
                        map.put("image", data.optString("image"));
                        listArtist.add(map);
                    }
                    adapterArtist = new AdapterArtist(getContext(), listArtist);
                    recyclerViewDai.setAdapter(adapterArtist);
                    recyclerViewDai.setNestedScrollingEnabled(false);
                }

                JSONArray theme = tab1.getJSONArray("theme");
                if (theme.length()>0){
                    labelTheme.setVisibility(View.VISIBLE);
                    recyclerViewTheme.setVisibility(View.VISIBLE);

                    for (int i=0; i<theme.length(); i++){{
                        JSONObject data = theme.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("uid", data.optString("uid"));
                        map.put("title", data.optString("title"));
                        map.put("image", data.optString("image"));
                        listTheme.add(map);
                    }}
                    adapterGenre = new AdapterGenre(getContext(), listTheme);
                    recyclerViewTheme.setAdapter(adapterGenre);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
