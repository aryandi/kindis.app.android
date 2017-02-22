package sangmaneproject.kindis.view.fragment.musiq;


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
import sangmaneproject.kindis.view.adapter.AdapterArtist;
import sangmaneproject.kindis.view.adapter.AdapterAlbum;
import sangmaneproject.kindis.view.adapter.AdapterPlaylist;


/**
 * A simple {@link Fragment} subclass.
 */
public class MostPlayed extends Fragment {
    AdapterAlbum adapterAlbum;
    AdapterArtist adapterArtist;
    AdapterPlaylist adapterPlaylist;

    RecyclerView recyclerViewTopListened;
    RecyclerView recyclerViewArtist;
    RecyclerView recyclerViewPlaylist;

    ArrayList<HashMap<String, String>> listAlbum = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listArtist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listPlaylist = new ArrayList<HashMap<String, String>>();

    String json;
    TextView labelPlaylist;
    public MostPlayed(String json) {
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
        recyclerViewArtist = (RecyclerView) view.findViewById(R.id.rv_artist);
        recyclerViewPlaylist = (RecyclerView) view.findViewById(R.id.list_playlist);
        labelPlaylist = (TextView) view.findViewById(R.id.label_playlist);

        recyclerViewTopListened.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewArtist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPlaylist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getJSON();
    }

    private void getJSON(){
        listAlbum.clear();
        listArtist.clear();

        try {
            JSONObject object = new JSONObject(json);
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

                adapterAlbum = new AdapterAlbum(getContext(), listAlbum);
                recyclerViewTopListened.setAdapter(adapterAlbum);
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

                JSONArray playlist = result.getJSONArray("playlist");
                for (int i=0; i<playlist.length(); i++){
                    JSONObject data = playlist.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("playlist_id", data.optString("uid"));
                    map.put("title", data.optString("playlist_name"));
                    listPlaylist.add(map);
                }
                labelPlaylist.setVisibility(View.VISIBLE);
                recyclerViewPlaylist.setVisibility(View.VISIBLE);
                adapterPlaylist = new AdapterPlaylist(getContext(), listPlaylist);
                recyclerViewPlaylist.setAdapter(adapterPlaylist);
                recyclerViewPlaylist.setNestedScrollingEnabled(true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
