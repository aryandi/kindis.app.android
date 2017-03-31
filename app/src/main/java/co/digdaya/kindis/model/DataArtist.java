package co.digdaya.kindis.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 3/31/2017.
 */

public class DataArtist {

    @SerializedName("data")
    public List<Data> data;

    public static class Data {
        @SerializedName("uid")
        public String uid;
        @SerializedName("name")
        public String name;
        @SerializedName("image")
        public String image;
    }
}
