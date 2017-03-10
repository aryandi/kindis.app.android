package sangmaneproject.kindis.view.fragment.bottomnavigation.taklim;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.view.adapter.item.AdapterSongHorizontal;
import sangmaneproject.kindis.view.adapter.item.AdapterArtist;

/**
 * A simple {@link Fragment} subclass.
 */
public class Murottal extends Fragment {
    String json;
    TextView labelQori, labelSurah;
    RecyclerView recyclerViewQori, recyclerViewSurah;

    ArrayList<HashMap<String, String>> listQori = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listSurah = new ArrayList<HashMap<String, String>>();

    AdapterArtist adapterArtist;
    AdapterSongHorizontal adapterSong;

    public Murottal(String json) {
        this.json = json;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_murottal, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        labelQori = (TextView) view.findViewById(R.id.label_qori);
        labelSurah = (TextView) view.findViewById(R.id.label_surah);

        recyclerViewQori = (RecyclerView) view.findViewById(R.id.list_qori);
        recyclerViewSurah = (RecyclerView) view.findViewById(R.id.list_surah);

        recyclerViewQori.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSurah.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        getJSON();
    }

    private void getJSON(){
        try {
            JSONObject object = new JSONObject(json);
            if (object.getBoolean("status")){
                JSONObject result = object.getJSONObject("result");
                JSONObject tab3 = result.getJSONObject("tab3");

                JSONArray qori = tab3.getJSONArray("qori");
                if (qori.length()>0){
                    labelQori.setVisibility(View.VISIBLE);
                    recyclerViewQori.setVisibility(View.VISIBLE);

                    for (int i=0; i<qori.length(); i++){{
                        JSONObject data = qori.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("uid", data.optString("uid"));
                        map.put("name", data.optString("name"));
                        map.put("image", data.optString("image"));
                        listQori.add(map);
                    }}
                    adapterArtist = new AdapterArtist(getContext(), listQori);
                    recyclerViewQori.setAdapter(adapterArtist);
                    recyclerViewQori.setNestedScrollingEnabled(false);
                }

                JSONArray surah = tab3.getJSONArray("surah");
                if (surah.length()>0){
                    labelSurah.setVisibility(View.VISIBLE);
                    recyclerViewSurah.setVisibility(View.VISIBLE);

                    for (int i=0; i<surah.length(); i++){
                        JSONObject data = surah.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("uid", data.optString("uid"));
                        map.put("title", data.optString("title"));
                        map.put("image", data.optString("image"));
                        map.put("subtitle", data.optString("artist"));
                        listSurah.add(map);
                    }
                    adapterSong = new AdapterSongHorizontal(getActivity(), listSurah);
                    recyclerViewSurah.setAdapter(adapterSong);
                    recyclerViewSurah.setNestedScrollingEnabled(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}