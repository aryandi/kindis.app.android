package sangmaneproject.kindis.view.fragment.musiq;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.view.adapter.AdapterArtist;
import sangmaneproject.kindis.view.adapter.AdapterTopListened;


/**
 * A simple {@link Fragment} subclass.
 */
public class MostPlayed extends Fragment {
    AdapterTopListened adapterTopListened;
    AdapterArtist adapterArtist;

    RecyclerView recyclerViewTopListened;
    RecyclerView recyclerViewArtist;

    ArrayList<HashMap<String, String>> listAlbum = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listArtist = new ArrayList<HashMap<String, String>>();

    public MostPlayed() {
        // Required empty public constructor
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
        recyclerViewTopListened = (RecyclerView) view.findViewById(R.id.rv_top_listened);
        recyclerViewArtist = (RecyclerView) view.findViewById(R.id.rv_artist);

        recyclerViewTopListened.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewArtist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        getJSON();
    }

    private void getJSON(){
        listAlbum.clear();
        listArtist.clear();
        new VolleyHelper().get(ApiHelper.HOME, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");

                            //album
                            JSONArray album = result.getJSONArray("album");
                            for (int i=0; i<album.length(); i++){
                                JSONObject data = album.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("uid", data.optString("uid"));
                                map.put("title", data.optString("title"));
                                map.put("description", data.optString("description"));
                                map.put("image", data.optString("image"));
                                listAlbum.add(map);
                            }

                            adapterTopListened = new AdapterTopListened(getContext(), listAlbum);
                            recyclerViewTopListened.setAdapter(adapterTopListened);
                            recyclerViewTopListened.setNestedScrollingEnabled(false);

                            JSONArray artist = result.getJSONArray("artist");
                            for (int i=0; i<artist.length(); i++){
                                JSONObject data = artist.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("uid", data.optString("uid"));
                                map.put("name", data.optString("name"));
                                map.put("image", data.optString("image"));
                                listArtist.add(map);
                            }

                            adapterArtist = new AdapterArtist(getContext(), listArtist);
                            recyclerViewArtist.setAdapter(adapterArtist);
                            recyclerViewArtist.setNestedScrollingEnabled(false);
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
}