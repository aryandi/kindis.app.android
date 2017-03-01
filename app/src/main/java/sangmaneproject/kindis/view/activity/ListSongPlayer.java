package sangmaneproject.kindis.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.view.adapter.AdapterSong;

public class ListSongPlayer extends AppCompatActivity {
    RecyclerView listViewSong;
    AdapterSong adapterSong;
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();

    String songPos, type, json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song_player);

        listViewSong = (RecyclerView) findViewById(R.id.list_songs);
        listViewSong.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        type = new PlayerSessionHelper().getPreferences(getApplicationContext(), "type");
        json = new PlayerSessionHelper().getPreferences(getApplicationContext(), "json");

        Log.d("kontoljson", json);
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
                }
                adapterSong = new AdapterSong(ListSongPlayer.this, listSong);
                listViewSong.setAdapter(adapterSong);
                listViewSong.setNestedScrollingEnabled(true);
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
                }

                adapterSong = new AdapterSong(ListSongPlayer.this, listSong);
                listViewSong.setAdapter(adapterSong);
                listViewSong.setNestedScrollingEnabled(true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
