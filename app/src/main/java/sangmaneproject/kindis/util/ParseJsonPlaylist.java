package sangmaneproject.kindis.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.helper.PlayerSessionHelper;

/**
 * Created by vincenttp on 3/2/2017.
 */

public class ParseJsonPlaylist {
    Context context;
    String type, json;

    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    ArrayList<String> songPlaylist = new ArrayList<>();

    public ParseJsonPlaylist(Context context){
        this.context = context;
        type = new PlayerSessionHelper().getPreferences(context, "type");
        json = new PlayerSessionHelper().getPreferences(context, "json");
    }

    public void setPlaylist(){
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
                    map.put("subtitle", "");
                    listSong.add(map);
                    songPlaylist.add(summary.optString("uid"));
                }
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
                                maps.put("subtitle", "");
                                listSong.add(maps);
                                songPlaylist.add(song.optString("uid"));
                            }
                        }
                    }
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
                    map.put("subtitle", data.optString("description"));
                    listSong.add(map);
                    songPlaylist.add(data.optString("uid"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<HashMap<String, String>> getListSong(){
        setPlaylist();
        return listSong;
    }

    public ArrayList<String> getSongPlaylist(){
        setPlaylist();
        return songPlaylist;
    }
}
