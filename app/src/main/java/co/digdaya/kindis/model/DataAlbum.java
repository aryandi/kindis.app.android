package co.digdaya.kindis.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 3/31/2017.
 */

public class DataAlbum {

    @SerializedName("data")
    public List<Data> data;

    @SerializedName("albums")
    public List<Data> album;

    public static class Data {
        @SerializedName("uid")
        public String uid;
        @SerializedName("title")
        public String title;
        @SerializedName("description")
        public String description;
        @SerializedName("image")
        public String image;
        @SerializedName("year")
        public String year;
        @SerializedName("is_premium")
        public String is_premium;
    }
}
