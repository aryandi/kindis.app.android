package co.digdaya.kindis.live.view.fragment.bottomnavigation.playlist;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.CheckConnection;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.view.adapter.item.AdapterPlaylist;

/**
 * A simple {@link Fragment} subclass.
 */
public class Playlist extends Fragment implements View.OnClickListener {
    ScrollView contCreatePlaylist;
    EditText inputPlaylist;
    Button btnCreate;

    RecyclerView listViewPlaylist;
    ArrayList<HashMap<String, String>> listPlaylist = new ArrayList<HashMap<String, String>>();
    AdapterPlaylist adapterPlaylist;

    LinearLayout contEmptyState;
    LinearLayout contPlaylist;
    TextView createNewPlaylist;
    Button refresh;
    ProgressDialog loading;

    SessionHelper sessionHelper;

    public Playlist() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionHelper = new SessionHelper();

        contCreatePlaylist = (ScrollView) view.findViewById(R.id.layout_create_playlist);
        inputPlaylist = (EditText) view.findViewById(R.id.input_playlist);
        btnCreate = (Button) view.findViewById(R.id.btn_create);

        listViewPlaylist = (RecyclerView) view.findViewById(R.id.list_playlist);
        listViewPlaylist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        contEmptyState = (LinearLayout) view.findViewById(R.id.empty_state);
        contPlaylist = (LinearLayout) view.findViewById(R.id.cont_list);
        createNewPlaylist = (TextView) view.findViewById(R.id.create_new_playlist);
        refresh = (Button) view.findViewById(R.id.btn_refresh);
        loading = new ProgressDialog(getActivity(), R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputPlaylist.getText().length()<1){
                    Toast.makeText(getContext(), "Playlist name can't be empty", Toast.LENGTH_SHORT).show();
                }else {
                    createPlaylist();
                }
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLayout();
            }
        });

        createNewPlaylist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.create_new_playlist){
            contPlaylist.setVisibility(View.GONE);
            contCreatePlaylist.setVisibility(View.VISIBLE);
            listPlaylist.clear();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listPlaylist.isEmpty()){
            setLayout();
        }else {
            adapterPlaylist = new AdapterPlaylist(getContext(), listPlaylist, "true");
            listViewPlaylist.setAdapter(adapterPlaylist);
            listViewPlaylist.setNestedScrollingEnabled(true);
            menuPlaylist();
        }
    }

    private void getPlaylist(){
        loading.show();
        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", new SessionHelper().getPreferences(getContext(), "user_id"));

        new VolleyHelper().post(ApiHelper.PLAYLIST, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");
                            JSONArray playlist = result.getJSONArray("playlist");
                            for (int i=0; i<playlist.length(); i++){
                                JSONObject data = playlist.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("playlist_id", data.getString("playlist_id"));
                                map.put("title", data.getString("playlist_name"));
                                listPlaylist.add(map);
                            }

                            contPlaylist.setVisibility(View.VISIBLE);
                            adapterPlaylist = new AdapterPlaylist(getContext(), listPlaylist, "true");
                            listViewPlaylist.setAdapter(adapterPlaylist);
                            listViewPlaylist.setNestedScrollingEnabled(true);
                            menuPlaylist();
                        }else {
                            contCreatePlaylist.setVisibility(View.VISIBLE);
                            contPlaylist.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {

                }
            }
        });
    }

    private void createPlaylist(){
        Map<String, String> param = new HashMap<String, String>();
        param.put("user_id", new SessionHelper().getPreferences(getContext(), "user_id"));
        param.put("playlist_name", inputPlaylist.getText().toString());
        param.put("token_access", sessionHelper.getPreferences(getContext(), "token_access"));

        new VolleyHelper().post(ApiHelper.CREATE_PLAYLIST, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    Log.d("createplaylist", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            contCreatePlaylist.setVisibility(View.GONE);
                            contPlaylist.setVisibility(View.VISIBLE);
                            inputPlaylist.setText("");
                            getPlaylist();
                        }else {
                            Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    getPlaylist();
                }
            }
        });
    }

    private void setLayout(){
        if (new CheckConnection().isInternetAvailable(getContext())){
            getPlaylist();
            contPlaylist.setVisibility(View.VISIBLE);
            contEmptyState.setVisibility(View.GONE);
        }else {
            contPlaylist.setVisibility(View.GONE);
            contEmptyState.setVisibility(View.VISIBLE);
        }
    }

    private void menuPlaylist(){
        adapterPlaylist.setOnClickMenuListener(new AdapterPlaylist.OnClickMenuListener() {
            @Override
            public void onClick(final String uid, ImageButton button) {
                PopupMenu popup = new PopupMenu(getActivity(), button);
                popup.getMenuInflater().inflate(R.menu.playlist, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId()==R.id.delete){
                            deletePlaylist(uid);
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    private void deletePlaylist(String uid){
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", sessionHelper.getPreferences(getContext(), "user_id"));
        param.put("playlist_id", uid);
        param.put("token_access", sessionHelper.getPreferences(getContext(), "token_access"));

        new VolleyHelper().post(ApiHelper.DELETE_PLAYLIST, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                Log.d("deleteplaylistresponse", response);
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            listPlaylist.clear();
                            getPlaylist();
                            Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        }else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
