package co.digdaya.kindis.view.fragment.bottomnavigation.musiq;


import android.app.Dialog;
import android.content.Intent;
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
import co.digdaya.kindis.view.activity.Detail.More;
import co.digdaya.kindis.view.adapter.item.AdapterPlaylistHorizontal;
import co.digdaya.kindis.view.adapter.item.AdapterAlbum;
import co.digdaya.kindis.view.adapter.item.AdapterArtist;
import co.digdaya.kindis.view.adapter.item.AdapterSongHorizontal;


/**
 * A simple {@link Fragment} subclass.
 */
public class Discover extends Fragment implements View.OnClickListener {
    AdapterAlbum adapterAlbum;
    AdapterArtist adapterArtist;
    AdapterSongHorizontal adapterSong;
    AdapterPlaylistHorizontal adapterPlaylistHorizontal;

    RelativeLayout labelPremium, labelTop, labelAlbum, labelArtist, labelSingle, labelRandom1, labelRandom2;
    TextView textRandom1, textRandom2;
    RecyclerView recyclerViewPremium, recyclerViewTop, recyclerViewAlbum, recyclerViewArtist, recyclerViewSingle, recyclerViewRandom1, recyclerViewRandom2;
    TextView btnMorePremium, btnMoreTop, btnMoreAlbum, btnMoreArtist, btnMoreSingle, btnMoreRandom1, btnMoreRandom2;


    ArrayList<PlaylistModel> listPremium = new ArrayList<PlaylistModel>();
    ArrayList<PlaylistModel> listTop = new ArrayList<PlaylistModel>();
    ArrayList<HashMap<String, String>> listAlbum = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listArtist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listRandom1 = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listRandom2 = new ArrayList<HashMap<String, String>>();

    String json;
    Dialog dialogPlaylis;

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

        labelPremium = (RelativeLayout) view.findViewById(R.id.label_premium);
        labelTop = (RelativeLayout) view.findViewById(R.id.label_top);
        labelAlbum = (RelativeLayout) view.findViewById(R.id.label_album);
        labelArtist = (RelativeLayout) view.findViewById(R.id.label_artist);
        labelSingle = (RelativeLayout) view.findViewById(R.id.label_single);
        labelRandom1 = (RelativeLayout) view.findViewById(R.id.label_random1);
        labelRandom2 = (RelativeLayout) view.findViewById(R.id.label_random2);

        textRandom1 = (TextView) view.findViewById(R.id.text_random1);
        textRandom2 = (TextView) view.findViewById(R.id.text_random2);

        btnMorePremium = (TextView) view.findViewById(R.id.btn_more_premium);
        btnMoreTop = (TextView) view.findViewById(R.id.btn_more_top);
        btnMoreAlbum = (TextView) view.findViewById(R.id.btn_more_album);
        btnMoreArtist = (TextView) view.findViewById(R.id.btn_more_artist);
        btnMoreSingle = (TextView) view.findViewById(R.id.btn_more_single);
        btnMoreRandom1 = (TextView) view.findViewById(R.id.btn_more_random1);
        btnMoreRandom2 = (TextView) view.findViewById(R.id.btn_more_random2);

        recyclerViewPremium = (RecyclerView) view.findViewById(R.id.list_premium);
        recyclerViewTop = (RecyclerView) view.findViewById(R.id.list_top);
        recyclerViewAlbum = (RecyclerView) view.findViewById(R.id.list_album);
        recyclerViewArtist = (RecyclerView) view.findViewById(R.id.list_artist);
        recyclerViewSingle = (RecyclerView) view.findViewById(R.id.list_single);
        recyclerViewRandom1 = (RecyclerView) view.findViewById(R.id.list_random1);
        recyclerViewRandom2 = (RecyclerView) view.findViewById(R.id.list_random2);

        recyclerViewPremium.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTop.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAlbum.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewArtist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSingle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRandom1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRandom2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        btnMorePremium.setOnClickListener(this);
        btnMoreTop.setOnClickListener(this);
        btnMoreAlbum.setOnClickListener(this);
        btnMoreArtist.setOnClickListener(this);
        btnMoreSingle.setOnClickListener(this);
        btnMoreRandom1.setOnClickListener(this);
        btnMoreRandom2.setOnClickListener(this);

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

                JSONArray top = tab1.getJSONArray("top10premium");
                if (top.length()>0){
                    labelTop.setVisibility(View.VISIBLE);
                    recyclerViewTop.setVisibility(View.VISIBLE);

                    for (int i=0; i<top.length(); i++){
                        JSONObject data = top.getJSONObject(i);
                        PlaylistModel playlistModel = new PlaylistModel();
                        playlistModel.setUid(data.optString("uid"));
                        playlistModel.setName(data.optString("name"));
                        playlistModel.setImage(data.optString("image"));
                        listTop.add(playlistModel);
                    }
                    adapterPlaylistHorizontal = new AdapterPlaylistHorizontal(getActivity(), listTop);
                    recyclerViewTop.setAdapter(adapterPlaylistHorizontal);
                    recyclerViewTop.setNestedScrollingEnabled(false);
                }

                JSONArray album = tab1.getJSONArray("album");
                if (album.length()>0){
                    labelAlbum.setVisibility(View.VISIBLE);
                    recyclerViewAlbum.setVisibility(View.VISIBLE);
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

                JSONArray artist = tab1.getJSONArray("artist");
                if (artist.length()>0){
                    labelArtist.setVisibility(View.VISIBLE);
                    recyclerViewArtist.setVisibility(View.VISIBLE);
                    for (int i=0; i<artist.length(); i++){
                        JSONObject data = artist.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("uid", data.optString("uid"));
                        map.put("name", data.optString("name"));
                        map.put("image", data.optString("image"));
                        listArtist.add(map);
                    }
                    labelArtist.setVisibility(View.VISIBLE);
                    adapterArtist = new AdapterArtist(getContext(), listArtist);
                    recyclerViewArtist.setAdapter(adapterArtist);
                    recyclerViewArtist.setNestedScrollingEnabled(false);
                }

                JSONArray single = tab1.getJSONArray("single");
                if (single.length()>0){
                    labelSingle.setVisibility(View.VISIBLE);
                    recyclerViewSingle.setVisibility(View.VISIBLE);
                    for (int i=0; i<single.length(); i++){
                        JSONObject data = single.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("uid", data.optString("uid"));
                        map.put("title", data.optString("title"));
                        map.put("image", data.optString("image"));
                        map.put("subtitle", data.optString("artist"));
                        map.put("is_premium", data.optString("is_premium"));
                        listSong.add(map);
                    }
                    adapterSong = new AdapterSongHorizontal(getActivity(), listSong);
                    recyclerViewSingle.setAdapter(adapterSong);
                    recyclerViewSingle.setNestedScrollingEnabled(true);
                }

                JSONArray random1 = tab1.getJSONArray("random1");
                JSONObject random1Data = random1.getJSONObject(0);
                textRandom1.setText(random1Data.getString("name"));
                JSONArray dataRandom1 = random1Data.getJSONArray("data");
                if (dataRandom1.length()>0){
                    labelRandom1.setVisibility(View.VISIBLE);
                    recyclerViewRandom1.setVisibility(View.VISIBLE);
                    for (int i=0; i<dataRandom1.length(); i++){
                        JSONObject data = dataRandom1.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("uid", data.optString("uid"));
                        map.put("title", data.optString("title"));
                        map.put("image", data.optString("image"));
                        map.put("subtitle", data.optString("artist"));
                        map.put("is_premium", data.optString("is_premium"));
                        listRandom1.add(map);
                    }
                    adapterSong = new AdapterSongHorizontal(getActivity(), listRandom1);
                    recyclerViewRandom1.setAdapter(adapterSong);
                    recyclerViewRandom1.setNestedScrollingEnabled(true);
                }

                JSONArray random2 = tab1.getJSONArray("random2");
                JSONObject random2Data = random2.getJSONObject(0);
                textRandom2.setText(random2Data.getString("name"));
                JSONArray dataRandom2 = random2Data.getJSONArray("data");
                if (dataRandom2.length()>0){
                    labelRandom2.setVisibility(View.VISIBLE);
                    recyclerViewRandom2.setVisibility(View.VISIBLE);
                    for (int i=0; i<dataRandom2.length(); i++){
                        JSONObject data = dataRandom2.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("uid", data.optString("uid"));
                        map.put("title", data.optString("title"));
                        map.put("image", data.optString("image"));
                        map.put("subtitle", data.optString("artist"));
                        map.put("is_premium", data.optString("is_premium"));
                        listRandom2.add(map);
                    }
                    adapterSong = new AdapterSongHorizontal(getActivity(), listRandom2);
                    recyclerViewRandom2.setAdapter(adapterSong);
                    recyclerViewRandom2.setNestedScrollingEnabled(true);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), More.class);
        switch (view.getId()){
            case R.id.btn_more_premium:
                intent.putExtra("title", "PREMIUM");
                intent.putExtra("url", "playlist/more?channel_id=1&uid=10051608ad5470&page=0&limit=12");
                startActivity(intent);
                break;
            case R.id.btn_more_top:
                intent.putExtra("title", "TOP 10 PREMIUM");
                intent.putExtra("url", "playlist/more?channel_id=1&uid=10051608ad5470&page=0&limit=12");
                startActivity(intent);
                break;
            case R.id.btn_more_album:
                intent.putExtra("title", "ALBUM");
                intent.putExtra("url", "home/album_more?channel_id=1&uid=10051608ad5470&page=0&limit=12");
                startActivity(intent);
                break;
            case R.id.btn_more_artist:
                intent.putExtra("title", "ARTIST");
                intent.putExtra("url", "home/album_more?channel_id=1&uid=10051608ad5470&page=0&limit=12");
                startActivity(intent);
                break;
            case R.id.btn_more_single:
                intent.putExtra("title", "SINGLE");
                intent.putExtra("url", "home/single_more?channel_id=1&uid=10051608ad5470&page=0&limit=12");
                startActivity(intent);
                break;
            case R.id.btn_more_random1:
                break;
            case R.id.btn_more_random2:
                break;
        }
    }
}
