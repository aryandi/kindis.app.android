package co.digdaya.kindis.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.view.activity.Detail.DetailArtist;
import co.digdaya.kindis.view.adapter.AdapterInsertItemPlaylist;

/**
 * Created by DELL on 2/24/2017.
 */

public class DialogSingleMenu implements View.OnClickListener {
    Activity activity;
    Dialog dialog;
    ArrayList<HashMap<String, String>> listPlaylist = new ArrayList<HashMap<String, String>>();
    AdapterInsertItemPlaylist adapterPlaylist;
    RecyclerView listViewPlaylist;
    String uidSingle, artistID, shareLink;
    Boolean isArtist;
    TextView btnGotoArtist, btnShare, btnAddToPlaylist;

    public DialogSingleMenu(Activity activity, Dialog dialog, String uidSingle, String artistID, String shareLink, Boolean isArtist) {
        this.activity = activity;
        this.dialog = dialog;
        this.uidSingle = uidSingle;
        this.artistID = artistID;
        this.shareLink = shareLink;
        this.isArtist = isArtist;
    }

    public void showDialog(){
        LayoutInflater li = LayoutInflater.from(activity);
        View dialogView = li.inflate(R.layout.layout_add_playlist, null);

        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(activity);
        alertDialog.setView(dialogView);
        dialog = alertDialog.create();

        initView(dialogView);
        getPlaylist();
    }

    private void initView(View view){
        listViewPlaylist = (RecyclerView) view.findViewById(R.id.list_playlist);
        listViewPlaylist.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));

        btnGotoArtist = (TextView) view.findViewById(R.id.btn_goto_artist);
        btnShare = (TextView) view.findViewById(R.id.btn_share);
        btnAddToPlaylist = (TextView) view.findViewById(R.id.btn_add_to_playlist);

        if (isArtist){
            btnGotoArtist.setVisibility(View.GONE);
        }

        btnShare.setOnClickListener(this);
        btnGotoArtist.setOnClickListener(this);
    }

    private void getPlaylist(){
        Map<String, String> param = new HashMap<String, String>();
        param.put("uid", new SessionHelper().getPreferences(activity, "user_id"));

        new VolleyHelper().post(ApiHelper.PLAYLIST, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
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

                            adapterPlaylist = new AdapterInsertItemPlaylist(activity, listPlaylist, uidSingle, dialog);
                            listViewPlaylist.setAdapter(adapterPlaylist);
                            listViewPlaylist.setNestedScrollingEnabled(true);
                        }else {
                            btnAddToPlaylist.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareLink);
                sendIntent.setType("text/plain");
                activity.startActivity(sendIntent);
                break;
            case R.id.btn_goto_artist:
                Intent intent = new Intent(activity, DetailArtist.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", artistID);
                intent.putExtra("type", "artist");
                activity.startActivity(intent);
                break;
        }
    }


}
