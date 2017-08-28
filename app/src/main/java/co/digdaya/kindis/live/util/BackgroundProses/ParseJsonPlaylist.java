package co.digdaya.kindis.live.util.BackgroundProses;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import co.digdaya.kindis.live.helper.PlayerSessionHelper;

/**
 * Created by vincenttp on 3/2/2017.
 */

public class ParseJsonPlaylist {
    Context context;
    String type, json, shuffle;
    String shfl;
    boolean update;
    PlayerSessionHelper playerSessionHelper;

    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    ArrayList<String> songPlaylist = new ArrayList<>();
    ArrayList<String> imgList = new ArrayList<>();

    ArrayList<HashMap<String, String>> shuffleListSong;
    ArrayList<String> shufflesongPlaylist;
    ArrayList<String> shuffleimgList;

    public ParseJsonPlaylist(Context context, boolean update){
        this.context = context;
        this.update = update;
        playerSessionHelper = new PlayerSessionHelper();
        type = playerSessionHelper.getPreferences(context, "type");
        json = playerSessionHelper.getPreferences(context, "json");
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
                    map.put("image", summary.optString("image"));
                    listSong.add(map);
                    songPlaylist.add(summary.optString("uid"));
                    imgList.add(summary.optString("image"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setShuffle();
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
                                maps.put("image", song.optString("image"));
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
            setShuffle();
        }else if (type.equals("premium")){
            try {
                JSONArray result = new JSONArray(json);
                for (int i=0; i<result.length(); i++){
                    JSONObject data = result.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("uid", data.optString("single_id"));
                    map.put("title", data.optString("title"));
                    map.put("subtitle", data.optString("artist"));
                    map.put("image", data.optString("image"));
                    listSong.add(map);
                    songPlaylist.add(data.optString("single_id"));
                    imgList.add(data.optString("image"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("parssssseee: "+e.getMessage());
            }
            setShuffle();
        }else {
            try {
                JSONArray result = new JSONArray(json);
                for (int i=0; i<result.length(); i++){
                    JSONObject data = result.getJSONObject(i);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("uid", data.optString("uid"));
                    map.put("title", data.optString("title"));
                    map.put("subtitle", data.optString("description"));
                    map.put("image", data.optString("image"));
                    listSong.add(map);
                    songPlaylist.add(data.optString("uid"));
                    imgList.add(data.optString("image"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setShuffle();
        }

        /*shuffleListSong = listSong;
        if (update){
            System.out.println("PLAY_MULTYSOURCE: shuffle");
            Collections.shuffle(shuffleListSong);
            shfl = new Gson().toJson(shuffleListSong);
            playerSessionHelper.setPreferences(context, "shuffle", shfl);
        }*/
        shufflePlaylist();
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

    private void setShuffle(){
        if (update){
            shuffleListSong = listSong;
            System.out.println("PLAY_MULTYSOURCE: shuffle");
            Collections.shuffle(shuffleListSong);
            shfl = new Gson().toJson(shuffleListSong);
            playerSessionHelper.setPreferences(context, "shuffle", shfl);
            shufflePlaylist();
        }
    }

    private void shufflePlaylist(){
        new PlayerSessionHelper().setPreferences(context, "setShuffle", "false");

        if (update){
            shuffle = shfl;
        }else {
            shuffle = playerSessionHelper.getPreferences(context, "shuffle");
        }

        shuffleListSong = new ArrayList<>();
        shufflesongPlaylist = new ArrayList<>();
        shuffleimgList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(shuffle);
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("uid", object.optString("uid"));
                map.put("title", object.optString("title"));
                map.put("subtitle", object.optString("subtitle"));
                map.put("image", object.optString("image"));
                shuffleListSong.add(map);
                shufflesongPlaylist.add(object.getString("uid"));
                shuffleimgList.add(object.getString("image"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("Shuffleplayyy lama: "+songPlaylist);
        System.out.println("Shuffleplayyy baru: "+shufflesongPlaylist);
    }

    public ArrayList<HashMap<String, String>> getShuffleListSong(){
        return shuffleListSong;
    }
    public ArrayList<String> getShuffleSongPlaylist(){
        return shufflesongPlaylist;
    }

    public ArrayList<String> getShuffleImageList(){
        return shuffleimgList;
    }
}
