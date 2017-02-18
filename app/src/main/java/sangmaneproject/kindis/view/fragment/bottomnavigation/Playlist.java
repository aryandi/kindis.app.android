package sangmaneproject.kindis.view.fragment.bottomnavigation;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.CheckConnection;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.view.adapter.AdapterPlaylist;

/**
 * A simple {@link Fragment} subclass.
 */
public class Playlist extends Fragment {
    LinearLayout contCreatePlaylist;
    EditText inputPlaylist;
    Button btnCreate;

    RecyclerView listViewPlaylist;
    ArrayList<HashMap<String, String>> listPlaylist = new ArrayList<HashMap<String, String>>();
    AdapterPlaylist adapterPlaylist;

    LinearLayout contEmptyState;
    Button refresh;
    ProgressDialog loading;

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
        contCreatePlaylist = (LinearLayout) view.findViewById(R.id.layout_create_playlist);
        inputPlaylist = (EditText) view.findViewById(R.id.input_playlist);
        btnCreate = (Button) view.findViewById(R.id.btn_create);

        listViewPlaylist = (RecyclerView) view.findViewById(R.id.list_playlist);
        listViewPlaylist.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        contEmptyState = (LinearLayout) view.findViewById(R.id.empty_state);
        refresh = (Button) view.findViewById(R.id.btn_refresh);
        loading = new ProgressDialog(getActivity());
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage("Loading. Please wait...");

        if (listPlaylist.isEmpty()){
            setLayout();
        }else {
            adapterPlaylist = new AdapterPlaylist(getContext(), listPlaylist);
            listViewPlaylist.setAdapter(adapterPlaylist);
            listViewPlaylist.setNestedScrollingEnabled(true);
        }

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
    }

    private void getPlaylist(){
        loading.show();
        Map<String, String> param = new HashMap<String, String>();
        param.put("token", new SessionHelper().getPreferences(getContext(), "token"));

        Log.d("tokenn", new SessionHelper().getPreferences(getContext(), "token"));

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

                            adapterPlaylist = new AdapterPlaylist(getContext(), listPlaylist);
                            listViewPlaylist.setAdapter(adapterPlaylist);
                            listViewPlaylist.setNestedScrollingEnabled(true);
                        }else {
                            contCreatePlaylist.setVisibility(View.VISIBLE);
                            listViewPlaylist.setVisibility(View.GONE);
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

        new VolleyHelper().post(ApiHelper.CREATE_PLAYLIST, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    Log.d("createplaylist", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            contCreatePlaylist.setVisibility(View.GONE);
                            listViewPlaylist.setVisibility(View.VISIBLE);
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
            listViewPlaylist.setVisibility(View.VISIBLE);
            contEmptyState.setVisibility(View.GONE);
        }else {
            listViewPlaylist.setVisibility(View.GONE);
            contEmptyState.setVisibility(View.VISIBLE);
        }
    }
}
