package sangmaneproject.kindis.view.fragment.musiq;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.view.adapter.AdapterGenre;


/**
 * A simple {@link Fragment} subclass.
 */
public class Genres extends Fragment {
    RecyclerView gridView;
    VolleyHelper volleyHelper;
    ArrayList<HashMap<String, String>> listGenre = new ArrayList<HashMap<String, String>>();
    AdapterGenre adapterGenre;


    String json;
    public Genres(String json) {
        this.json = json;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genres, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        volleyHelper = new VolleyHelper();
        gridView = (RecyclerView) view.findViewById(R.id.listview_genre);
        gridView.setLayoutManager(new GridLayoutManager(getContext(),3));

        getListGenre();
    }

    void getListGenre(){
        listGenre.clear();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject result = object.getJSONObject("result");
            JSONObject tab3 = result.getJSONObject("tab3");
            JSONArray genre = tab3.getJSONArray("genre");
            for (int i=0; i<genre.length(); i++){{
                JSONObject data = genre.getJSONObject(i);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("uid", data.optString("uid"));
                map.put("title", data.optString("title"));
                map.put("image", data.optString("image"));
                listGenre.add(map);
            }}
            adapterGenre = new AdapterGenre(getContext(), listGenre);
            gridView.setAdapter(adapterGenre);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
