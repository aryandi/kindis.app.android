package co.digdaya.kindis.view.fragment.bottomnavigation.musiq;


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

import co.digdaya.kindis.R;
import co.digdaya.kindis.model.TabModel;
import co.digdaya.kindis.view.adapter.AdapterListTab;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecentlyAdded extends Fragment {
    AdapterListTab adapterListTab;
    RecyclerView recyclerView;

    String json;
    Gson gson;

    public RecentlyAdded(String json) {
        this.json = json;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recently_added, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);

        gson = new Gson();

        getJSON();
    }

    private void getJSON(){
        try {
            JSONObject object = new JSONObject(json);
            if (object.getBoolean("status")){
                JSONObject result = object.getJSONObject("result");

                TabModel model = gson.fromJson(result.toString(), TabModel.class);

                adapterListTab = new AdapterListTab(getActivity(), model, 2);
                recyclerView.setAdapter(adapterListTab);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
