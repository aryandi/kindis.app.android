package co.digdaya.kindis.view.fragment.bottomnavigation.musiq;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.model.PlaylistModel;
import co.digdaya.kindis.view.adapter.item.AdapterAlbum;
import co.digdaya.kindis.view.adapter.item.AdapterPlaylistHorizontal;
import co.digdaya.kindis.view.adapter.item.AdapterSongHorizontal;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecentlyAdded extends Fragment {
    AdapterAlbum adapterAlbum;
    AdapterSongHorizontal adapterSongHorizontal;
    AdapterPlaylistHorizontal adapterPlaylistHorizontal;

    RelativeLayout labelSingle, labelPlay, labelAlbum, labelPremium;

    RecyclerView recyclerViewSingle, recyclerViewPLay, recyclerViewAlbum, recyclerViewPremium;

    ArrayList<HashMap<String, String>> listAlbum = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listPlay = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listSingle = new ArrayList<HashMap<String, String>>();
    ArrayList<PlaylistModel> listPremium = new ArrayList<PlaylistModel>();

    String json;
    Dialog dialogPlaylis;

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

        labelSingle = (RelativeLayout) view.findViewById(R.id.label_single);
        labelPlay = (RelativeLayout) view.findViewById(R.id.label_play);
        labelAlbum = (RelativeLayout) view.findViewById(R.id.label_album);
        labelPremium = (RelativeLayout) view.findViewById(R.id.label_premium);

        recyclerViewSingle = (RecyclerView) view.findViewById(R.id.list_single);
        recyclerViewPLay = (RecyclerView) view.findViewById(R.id.list_play);
        recyclerViewAlbum = (RecyclerView) view.findViewById(R.id.list_album);
        recyclerViewPremium = (RecyclerView) view.findViewById(R.id.list_premium);

        recyclerViewSingle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPLay.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAlbum.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPremium.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        getJSON();
    }

    private void getJSON(){
        try {
            JSONObject object = new JSONObject(json);
            if (object.getBoolean("status")){
                JSONObject result = object.getJSONObject("result");
                JSONObject tab2 = result.getJSONObject("tab2");

                //single
                JSONArray single = tab2.getJSONArray("single");
                if (single.length()>0){
                    labelSingle.setVisibility(View.VISIBLE);

                    for (int i=0; i<single.length(); i++){
                        JSONObject data = single.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("uid", data.optString("uid"));
                        map.put("title", data.optString("title"));
                        map.put("image", data.optString("image"));
                        map.put("subtitle", data.optString("artist"));
                        map.put("is_premium", data.optString("is_premium"));
                        listSingle.add(map);
                    }

                    adapterSongHorizontal = new AdapterSongHorizontal(getActivity(), listSingle);
                    recyclerViewSingle.setAdapter(adapterSongHorizontal);
                    recyclerViewSingle.setNestedScrollingEnabled(false);
                }

                JSONArray play = tab2.getJSONArray("play");
                if (play.length()>0){
                    labelPlay.setVisibility(View.VISIBLE);

                    for (int i=0; i<play.length(); i++){
                        JSONObject data = play.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("uid", data.optString("uid"));
                        map.put("title", data.optString("title"));
                        map.put("image", data.optString("image"));
                        map.put("subtitle", data.optString("artist"));
                        map.put("is_premium", data.optString("is_premium"));
                        System.out.println("kontolll: "+data.optString("title")+" = "+data.optString("is_premium"));
                        listPlay.add(map);
                    }

                    adapterSongHorizontal = new AdapterSongHorizontal(getActivity(), listPlay);
                    recyclerViewPLay.setAdapter(adapterSongHorizontal);
                    recyclerViewPLay.setNestedScrollingEnabled(false);
                }

                //album
                JSONArray album = tab2.getJSONArray("album");
                if (album.length()>0){
                    labelAlbum.setVisibility(View.VISIBLE);
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
                    recyclerViewAlbum.setAdapter(adapterAlbum);
                    recyclerViewAlbum.setNestedScrollingEnabled(false);
                }

                JSONArray premium = tab2.getJSONArray("top10premium");
                if (premium.length()>0){
                    labelPremium.setVisibility(View.VISIBLE);
                    recyclerViewPremium.setVisibility(View.VISIBLE);
                    for (int i=0; i<premium.length(); i++){
                        JSONObject data = premium.getJSONObject(i);
                        PlaylistModel playlistModel = new PlaylistModel();
                        playlistModel.setUid(data.optString("uid"));
                        playlistModel.setName(data.optString("name"));
                        playlistModel.setImage(data.optString("image"));
                        listPremium.add(playlistModel);
                    }
                    adapterPlaylistHorizontal = new AdapterPlaylistHorizontal(getActivity(), listPremium);
                    recyclerViewPremium.setAdapter(adapterPlaylistHorizontal);
                    recyclerViewPremium.setNestedScrollingEnabled(false);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*private void onClickMenuSong(){
        adapterSong.setOnClickMenuListener(new AdapterSong.OnClickMenuListener() {
            @Override
            public void onClick(String uid, ImageButton imageButton) {
                Log.d("uidkontol", uid);
                new DialogPlaylist(getActivity(), dialogPlaylis, uid).showDialog();
            }
        });
    }*/
}
