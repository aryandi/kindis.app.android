package co.digdaya.kindis.live.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 5/22/2017.
 */

public class PlaylistModelSearch {

    @SerializedName("playlist")
    public List<Playlist> playlist;

    public static class Playlist {
        @SerializedName("playlist_id")
        public String playlist_id;
        @SerializedName("name")
        public String name;
        @SerializedName("image")
        public String image;
        @SerializedName("is_premium")
        public String is_premium;
    }
}
