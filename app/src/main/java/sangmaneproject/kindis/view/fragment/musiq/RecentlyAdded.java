package sangmaneproject.kindis.view.fragment.musiq;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.util.DialogPlaylist;
import sangmaneproject.kindis.view.adapter.AdapterAlbum;
import sangmaneproject.kindis.view.adapter.AdapterSong;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecentlyAdded extends Fragment {
    AdapterAlbum adapterAlbum;
    AdapterSong adapterSong;

    RecyclerView recyclerViewTopListened;
    RecyclerView recyclerViewSong;

    ArrayList<HashMap<String, String>> listAlbum = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();

    String json;
    Dialog dialogPlaylis;

    public RecentlyAdded(String json) {
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

        recyclerViewTopListened = (RecyclerView) view.findViewById(R.id.rv_top_listened);
        recyclerViewSong = (RecyclerView) view.findViewById(R.id.rv_single);

        recyclerViewTopListened.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSong.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getJSON();
    }

    private void getJSON(){
        try {
            JSONObject object = new JSONObject(json);
            if (object.getBoolean("status")){
                JSONObject result = object.getJSONObject("result");
                JSONObject latest = result.getJSONObject("latest");

                //album
                JSONArray album = latest.getJSONArray("album");
                for (int i=0; i<album.length(); i++){
                    JSONObject data = album.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("uid", data.optString("uid"));
                    map.put("title", data.optString("title"));
                    map.put("description", data.optString("description"));
                    map.put("image", data.optString("image"));
                    map.put("year", data.optString("year"));
                    listAlbum.add(map);
                }

                adapterAlbum = new AdapterAlbum(getContext(), listAlbum);
                recyclerViewTopListened.setAdapter(adapterAlbum);
                recyclerViewTopListened.setNestedScrollingEnabled(false);

                JSONArray single = latest.getJSONArray("single");
                for (int i=0; i<single.length(); i++){
                    JSONObject data = single.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("uid", data.optString("uid"));
                    map.put("title", data.optString("title"));
                    listSong.add(map);
                }
                adapterSong = new AdapterSong(getActivity(), listSong, "", null);
                recyclerViewSong.setAdapter(adapterSong);
                recyclerViewSong.setNestedScrollingEnabled(true);
                onClickMenuSong();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onClickMenuSong(){
        adapterSong.setOnClickMenuListener(new AdapterSong.OnClickMenuListener() {
            @Override
            public void onClick(String uid, ImageButton imageButton) {
                Log.d("uidkontol", uid);
                new DialogPlaylist(getActivity(), dialogPlaylis, uid).showDialog();
            }
        });
    }
}
