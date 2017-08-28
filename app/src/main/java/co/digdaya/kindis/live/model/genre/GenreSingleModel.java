package co.digdaya.kindis.live.model.genre;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 5/28/2017.
 */

public class GenreSingleModel {

    @SerializedName("name")
    public String name;
    @SerializedName("type_id")
    public String type_id;
    @SerializedName("type_name")
    public String type_name;
    @SerializedName("type_content")
    public String type_content;
    @SerializedName("type_content_id")
    public String type_content_id;
    @SerializedName("is_more")
    public String is_more;
    @SerializedName("is_more_endpoint")
    public String is_more_endpoint;
    @SerializedName("data")
    public List<Data> data;

    public static class Data {
        @SerializedName("uid")
        public String uid;
        @SerializedName("title")
        public String title;
        @SerializedName("album_name")
        public String album_name;
        @SerializedName("artist")
        public String artist;
        @SerializedName("categories")
        public String categories;
        @SerializedName("file")
        public String file;
        @SerializedName("image")
        public String image;
        @SerializedName("year")
        public String year;
        @SerializedName("featuring_artist_id")
        public String featuring_artist_id;
        @SerializedName("is_premium")
        public String is_premium;
        @SerializedName("artist_id")
        public String artist_id;
    }
}
