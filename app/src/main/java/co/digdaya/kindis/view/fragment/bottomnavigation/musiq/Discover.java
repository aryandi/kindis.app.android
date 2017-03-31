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

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.model.DataPlaylist;
import co.digdaya.kindis.model.PlaylistModel;
import co.digdaya.kindis.model.TabModel;
import co.digdaya.kindis.view.activity.Detail.More;
import co.digdaya.kindis.view.adapter.AdapterListTab;
import co.digdaya.kindis.view.adapter.item.AdapterPlaylistHorizontal;
import co.digdaya.kindis.view.adapter.item.AdapterAlbum;
import co.digdaya.kindis.view.adapter.item.AdapterArtist;
import co.digdaya.kindis.view.adapter.item.AdapterSongHorizontal;


/**
 * A simple {@link Fragment} subclass.
 */
public class Discover extends Fragment {
    AdapterAlbum adapterAlbum;
    AdapterArtist adapterArtist;
    AdapterSongHorizontal adapterSong;
    AdapterPlaylistHorizontal adapterPlaylistHorizontal;

    RelativeLayout labelPremium, labelTop, labelAlbum, labelArtist, labelSingle, labelRandom1, labelRandom2;
    TextView textPremium, textTop, textAlbum, textArtist, textSingle, textRandom1, textRandom2;
    RecyclerView recyclerViewPremium, recyclerViewTop, recyclerViewAlbum, recyclerViewArtist, recyclerViewSingle, recyclerViewRandom1, recyclerViewRandom2;
    TextView btnMorePremium, btnMoreTop, btnMoreAlbum, btnMoreArtist, btnMoreSingle, btnMoreRandom1, btnMoreRandom2;


    ArrayList<PlaylistModel> listPremium = new ArrayList<PlaylistModel>();
    ArrayList<PlaylistModel> listTop = new ArrayList<PlaylistModel>();
    ArrayList<HashMap<String, String>> listAlbum = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listArtist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listRandom1 = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listRandom2 = new ArrayList<HashMap<String, String>>();

    AdapterListTab adapterListTab;
    RecyclerView recyclerView;

    String json;
    Gson gson;

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
        /*labelPremium = (RelativeLayout) view.findViewById(R.id.label_premium);
        labelTop = (RelativeLayout) view.findViewById(R.id.label_top);
        labelAlbum = (RelativeLayout) view.findViewById(R.id.label_album);
        labelArtist = (RelativeLayout) view.findViewById(R.id.label_artist);
        labelSingle = (RelativeLayout) view.findViewById(R.id.label_single);
        labelRandom1 = (RelativeLayout) view.findViewById(R.id.label_random1);
        labelRandom2 = (RelativeLayout) view.findViewById(R.id.label_random2);

        textPremium = (TextView) view.findViewById(R.id.text_premium);
        textTop = (TextView) view.findViewById(R.id.text_top);
        textAlbum = (TextView) view.findViewById(R.id.text_album);
        textArtist = (TextView) view.findViewById(R.id.text_artist);
        textSingle = (TextView) view.findViewById(R.id.text_single);
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
        recyclerViewRandom2 = (RecyclerView) view.findViewById(R.id.list_random2);*/

        /*recyclerViewPremium.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewTop.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAlbum.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewArtist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSingle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRandom1.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewRandom2.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));*/

        /*btnMorePremium.setOnClickListener(this);
        btnMoreTop.setOnClickListener(this);
        btnMoreAlbum.setOnClickListener(this);
        btnMoreArtist.setOnClickListener(this);
        btnMoreSingle.setOnClickListener(this);
        btnMoreRandom1.setOnClickListener(this);
        btnMoreRandom2.setOnClickListener(this);*/

        gson = new Gson();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        getJSON();
    }

    private void getJSON(){
        try {
            JSONObject object = new JSONObject(json);
            if (object.getBoolean("status")){
                JSONObject result = object.getJSONObject("result");
                TabModel tabModel = gson.fromJson(result.toString(), TabModel.class);
                System.out.println(tabModel.tab1.get(0).name);

                adapterListTab = new AdapterListTab(getContext(), tabModel);
                recyclerView.setAdapter(adapterListTab);
                recyclerView.setNestedScrollingEnabled(false);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
