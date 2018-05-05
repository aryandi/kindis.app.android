package co.digdaya.kindis.live.view.fragment.bottomnavigation.playlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.model.DataPlaylist;
import co.digdaya.kindis.live.util.SpacingItem.SpacingItemGenre;
import co.digdaya.kindis.live.view.adapter.item.AdapterPlaylistHorizontal;

/**
 * Created by DELL on 4/9/2017.
 */

public class PlaylistPremium extends Fragment{
    VolleyHelper volleyHelper;
    SessionHelper sessionHelper;
    RecyclerView recyclerView;
    Gson gson;
    AdapterPlaylistHorizontal adapterPlaylistHorizontal;

    public PlaylistPremium() {
        // Required empty public constructor
    }

    public PlaylistPremium(String s) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist_premium, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        volleyHelper = new VolleyHelper();
        sessionHelper = new SessionHelper();
        gson = new Gson();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.addItemDecoration(new SpacingItemGenre(getActivity(), ""));
        getList();
    }

    private void getList(){
        volleyHelper.get(ApiHelper.LIST_PLAYLIST_PREMIUM + sessionHelper.getPreferences(getActivity(), "user_id")+"&token_access=" + sessionHelper.getPreferences(getActivity(), "token_access"), new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (getActivity() != null && status){
                    System.out.println("listpremium : "+ApiHelper.LIST_PLAYLIST_PREMIUM + sessionHelper.getPreferences(getActivity(), "user_id")+"&token_access=" + sessionHelper.getPreferences(getActivity(), "token_access"));
                    System.out.println("listpremium : "+response);

                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONArray result = object.getJSONArray("result");
                            String json = "{ \"data\":"+result.toString()+"}";
                            DataPlaylist playlistModel = gson.fromJson(json, DataPlaylist.class);
                            adapterPlaylistHorizontal = new AdapterPlaylistHorizontal(getActivity(), playlistModel, 5, new AdapterPlaylistHorizontal.RowClickListener() {
                                @Override
                                public void onRowClick(int position) {
                                }
                            });
                            recyclerView.setAdapter(adapterPlaylistHorizontal);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
