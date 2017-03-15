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
import co.digdaya.kindis.view.adapter.item.AdapterGenre;

/**
 * A simple {@link Fragment} subclass.
 */
public class Kisah extends Fragment {
    String json;
    TextView labelDiscover, labelPremium, labelTop, labelStoryTeller, labelTheme;
    RecyclerView recyclerViewDiscover, recyclerViewPremium, recyclerViewTop, recyclerViewStoryTeller, recyclerViewTheme;
    ArrayList<HashMap<String, String>> listTheme = new ArrayList<HashMap<String, String>>();
    AdapterGenre adapterGenre;

    public Kisah(String json) {
        this.json = json;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kisah, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        labelDiscover = (TextView) view.findViewById(R.id.label_discover);
        labelPremium = (TextView) view.findViewById(R.id.label_premium);
        labelTop = (TextView) view.findViewById(R.id.label_top);
        labelStoryTeller = (TextView) view.findViewById(R.id.label_storyteller);
        labelTheme = (TextView) view.findViewById(R.id.label_theme);

        recyclerViewDiscover = (RecyclerView) view.findViewById(R.id.list_discover);
        recyclerViewPremium = (RecyclerView) view.findViewById(R.id.list_premium);
        recyclerViewTop = (RecyclerView) view.findViewById(R.id.list_top);
        recyclerViewStoryTeller = (RecyclerView) view.findViewById(R.id.list_storyteller);
        recyclerViewTheme = (RecyclerView) view.findViewById(R.id.list_theme);

        recyclerViewDiscover.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPremium.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTop.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewStoryTeller.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTheme.setLayoutManager(new GridLayoutManager(getContext(),3));

        getJSON();
    }

    private void getJSON(){
        try {
            JSONObject object = new JSONObject(json);
            if (object.getBoolean("status")){
                JSONObject result = object.getJSONObject("result");
                JSONObject tab2 = result.optJSONObject("tab2");

                JSONArray discover = tab2.getJSONArray("discover");
                if (discover.length()>0){
                    labelDiscover.setVisibility(View.VISIBLE);
                    recyclerViewDiscover.setVisibility(View.VISIBLE);
                }

                JSONArray premium = tab2.getJSONArray("premium");
                if (premium.length()>0){
                    labelPremium.setVisibility(View.VISIBLE);
                    recyclerViewPremium.setVisibility(View.VISIBLE);
                }

                JSONArray top10premium = tab2.getJSONArray("top10premium");
                if (top10premium.length()>0){
                    labelTop.setVisibility(View.VISIBLE);
                    recyclerViewTop.setVisibility(View.VISIBLE);
                }

                JSONArray storyteller = tab2.getJSONArray("storyteller");
                if (storyteller.length()>0){
                    labelStoryTeller.setVisibility(View.VISIBLE);
                    recyclerViewStoryTeller.setVisibility(View.VISIBLE);
                }

                JSONArray theme = tab2.getJSONArray("theme");
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
