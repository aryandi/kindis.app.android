package co.digdaya.kindis.live.view.fragment.bottomnavigation.musiq;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.model.TabModel;
import co.digdaya.kindis.live.view.adapter.tab.AdapterListTab;


/**
 * A simple {@link Fragment} subclass.
 */
public class Discover extends Fragment {
    AdapterListTab adapterListTab;
    RecyclerView recyclerView;

    String json;
    Gson gson;

    public Discover() {
    }

    public Discover(String json) {
        this.json = json;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_most_played, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_tab);
        gson = new Gson();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        if (json != null){
            getJSON();
        }
    }

    private void getJSON(){
        try {
            JSONObject object = new JSONObject(json);
            if (object.getBoolean("status")){
                JSONObject result = object.getJSONObject("result");
                TabModel tabModel = gson.fromJson(result.toString(), TabModel.class);
                System.out.println("getJSONitem: "+tabModel.tab1.get(6).name);
                adapterListTab = new AdapterListTab(getActivity(), tabModel, 1, 1, "Discover", "Musiq");
                recyclerView.setAdapter(adapterListTab);
                recyclerView.setNestedScrollingEnabled(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
