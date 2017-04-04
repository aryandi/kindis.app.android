package co.digdaya.kindis.util.BackgroundProses;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import co.digdaya.kindis.helper.PlayerSessionHelper;

/**
 * Created by vincenttp on 3/2/2017.
 */

public class ParseJsonPlaylist {
    Context context;
    String type, json;

    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    ArrayList<String> songPlaylist = new ArrayList<>();
    ArrayList<String> imgList = new ArrayList<>();

    ArrayList<HashMap<String, String>> shuffleListSong = new ArrayList<HashMap<String, String>>();
    public ParseJsonPlaylist(Context context){
        this.context = context;
        type = new PlayerSessionHelper().getPreferences(context, "type");
        json = new PlayerSessionHelper().getPreferences(context, "json");
        Log.d("ParseJsonPlaylist", type+" : "+json);
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
                    imgList.add(summary.optString("image"));
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
                                imgList.add(song.optString("image"));
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
                    imgList.add(data.optString("image"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<HashMap<String, String>> getListSong(){
        return listSong;
    }

    public ArrayList<String> getSongPlaylist(){
        return songPlaylist;
    }

    public ArrayList<String> getImageList(){
        return imgList;
    }
}
