package co.digdaya.kindis.live.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 5/17/2017.
 */

public class DownloadAlbumModel {

    @SerializedName("status")
    public boolean status;
    @SerializedName("result")
    public Result result;

    public static class Summary {
        @SerializedName("album")
        public String album;
        @SerializedName("artist")
        public String artist;
        @SerializedName("album_desc")
        public String album_desc;
        @SerializedName("image")
        public String image;
        @SerializedName("banner_image")
        public String banner_image;
    }

    public static class Offline_single {
        @SerializedName("title")
        public String title;
        @SerializedName("file")
        public String file;
        @SerializedName("image")
        public String image;
        @SerializedName("duration")
        public String duration;
        @SerializedName("album")
        public String album;
        @SerializedName("artist")
        public String artist;
        @SerializedName("artist_id")
        public String artist_id;
    }

    public static class Result {
        @SerializedName("summary")
        public Summary summary;
        @SerializedName("type")
        public String type;
        @SerializedName("offline_single")
        public List<Offline_single> offline_single;
    }
}
