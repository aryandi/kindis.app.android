package co.digdaya.kindis.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 3/31/2017.
 */

public class DataSingle {

    @SerializedName("data")
    public List<Data> data;

    @SerializedName("single")
    public List<Data> single;

    public static class Data {
        @SerializedName("uid")
        public String uid;
        @SerializedName("title")
        public String title;
        @SerializedName("image")
        public String image;
        @SerializedName("is_premium")
        public String is_premium;
        @SerializedName("artist")
        public String artist;
    }
}
