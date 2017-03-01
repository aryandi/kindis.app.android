package sangmaneproject.kindis.view.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.util.DialogPlaylist;
import sangmaneproject.kindis.view.adapter.AdapterSong;

public class ListSongPlayer extends AppCompatActivity {
    RecyclerView listViewSong;
    RelativeLayout contSingle;
    AdapterSong adapterSong;
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    ArrayList<String> songPlaylist = new ArrayList<>();

    TextView title;
    TextView subTitle;
    RelativeLayout click;
    ImageButton btnMenu;

    String type, json;
    Dialog dialogPlaylis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song_player);

        title = (TextView) findViewById(R.id.title);
        subTitle = (TextView) findViewById(R.id.subtitle);
        click = (RelativeLayout) findViewById(R.id.adapter_song);
        btnMenu = (ImageButton) findViewById(R.id.btn_menu);

        contSingle = (RelativeLayout) findViewById(R.id.cont_single);
        listViewSong = (RecyclerView) findViewById(R.id.list_songs);
        listViewSong.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        type = new PlayerSessionHelper().getPreferences(getApplicationContext(), "type");
        json = new PlayerSessionHelper().getPreferences(getApplicationContext(), "json");

        if (new PlayerSessionHelper().getPreferences(getApplicationContext(), "index").equals("1")){
            contSingle.setVisibility(View.VISIBLE);
            title.setText(new PlayerSessionHelper().getPreferences(getApplicationContext(), "title"));
            subTitle.setText("");
            btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogPlaylist(ListSongPlayer.this, dialogPlaylis, new PlayerSessionHelper().getPreferences(getApplicationContext(), "uid")).showDialog();
                }
            });
        }else {
            Log.d("kontoljson", json);
            listViewSong.setVisibility(View.VISIBLE);
            if (type.equals("album")){
                try {
                    JSONArray single = new JSONArray(json);
                    json = single.toString();
                    for (int i=0; i < single.length(); i++){
                        JSONObject data = single.getJSONObject(i);
                        JSONObject summary = data.getJSONObject("summary");
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("uid", summary.optString("uid"));
                        map.put("title", summary.optString("title"));
                        listSong.add(map);
                        songPlaylist.add(summary.optString("uid"));
                    }
                    adapterSong = new AdapterSong(ListSongPlayer.this, listSong, "list", songPlaylist);
                    listViewSong.setAdapter(adapterSong);
                    listViewSong.setNestedScrollingEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (type.equals("artist")){
                try {
                    JSONArray album = new JSONArray(json);
                    if (album.length()>=1){
                        for (int i=0; i<album.length(); i++){
                            JSONObject data = album.getJSONObject(i);
                            JSONArray single = data.getJSONArray("single");
                            if (single.length()>0){
                                for (int j=0; j<single.length(); j++){
                                    JSONObject song = single.getJSONObject(j);
                                    HashMap<String, String> maps = new HashMap<String, String>();
                                    maps.put("uid", song.optString("uid"));
                                    maps.put("title", song.optString("title"));
                                    listSong.add(maps);
                                    songPlaylist.add(song.optString("uid"));
                                }
                            }
                        }

                        adapterSong = new AdapterSong(ListSongPlayer.this, listSong, "list", songPlaylist);
                        listViewSong.setAdapter(adapterSong);
                        listViewSong.setNestedScrollingEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    JSONArray result = new JSONArray(json);
                    for (int i=0; i<result.length(); i++){
                        JSONObject data = result.getJSONObject(i);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("uid", data.optString("uid"));
                        map.put("title", data.optString("title"));
                        listSong.add(map);
                        songPlaylist.add(data.optString("uid"));
                    }

                    adapterSong = new AdapterSong(ListSongPlayer.this, listSong, "list", songPlaylist);
                    listViewSong.setAdapter(adapterSong);
                    listViewSong.setNestedScrollingEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
