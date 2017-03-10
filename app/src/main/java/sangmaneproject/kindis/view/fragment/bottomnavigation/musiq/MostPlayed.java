package sangmaneproject.kindis.view.fragment.bottomnavigation.musiq;


import android.app.Dialog;
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
import sangmaneproject.kindis.view.adapter.item.AdapterAlbum;
import sangmaneproject.kindis.view.adapter.item.AdapterArtist;
import sangmaneproject.kindis.view.adapter.item.AdapterSongHorizontal;


/**
 * A simple {@link Fragment} subclass.
 */
public class MostPlayed extends Fragment {
    AdapterAlbum adapterAlbum;
    AdapterArtist adapterArtist;
    AdapterSongHorizontal adapterSong;

    TextView labelPremium, labelTop, labelAlbum, labelArtist, labelSingle, labelRandom1, labelRandom2;
    RecyclerView recyclerViewPremium, recyclerViewTop, recyclerViewAlbum, recyclerViewArtist, recyclerViewSingle, recyclerViewRandom1, recyclerViewRandom2;


    ArrayList<HashMap<String, String>> listAlbum = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listArtist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listRandom1 = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listRandom2 = new ArrayList<HashMap<String, String>>();

    String json;
    Dialog dialogPlaylis;

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

        labelPremium = (TextView) view.findViewById(R.id.label_premium);
        labelTop = (TextView) view.findViewById(R.id.label_top);
        labelAlbum = (TextView) view.findViewById(R.id.label_album);
        labelArtist = (TextView) view.findViewById(R.id.label_artist);
        labelSingle = (TextView) view.findViewById(R.id.label_single);
        labelRandom1 = (TextView) view.findViewById(R.id.label_random1);
        labelRandom2 = (TextView) view.findViewById(R.id.label_random2);

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
                }

                JSONArray top = tab1.getJSONArray("top10premium");
                if (top.length()>0){
                    labelTop.setVisibility(View.VISIBLE);
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
                        listSong.add(map);
                    }
                    adapterSong = new AdapterSongHorizontal(getActivity(), listSong);
                    recyclerViewSingle.setAdapter(adapterSong);
                    recyclerViewSingle.setNestedScrollingEnabled(true);
                }

                JSONArray random1 = tab1.getJSONArray("random1");
                JSONObject random1Data = random1.getJSONObject(0);
                labelRandom1.setText(random1Data.getString("name"));
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
                        listRandom1.add(map);
                    }
                    adapterSong = new AdapterSongHorizontal(getActivity(), listRandom1);
                    recyclerViewRandom1.setAdapter(adapterSong);
                    recyclerViewRandom1.setNestedScrollingEnabled(true);
                }

                JSONArray random2 = tab1.getJSONArray("random2");
                JSONObject random2Data = random2.getJSONObject(0);
                labelRandom2.setText(random2Data.getString("name"));
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
}
