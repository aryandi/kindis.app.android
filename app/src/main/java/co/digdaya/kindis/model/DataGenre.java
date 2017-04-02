package co.digdaya.kindis.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vincenttp on 4/2/2017.
 */

public class DataGenre {

    @SerializedName("data")
    public List<Data> data;

    public static class Data {
        @SerializedName("uid")
        public String uid;
        @SerializedName("title")
        public String title;
        @SerializedName("description")
        public String description;
        @SerializedName("image")
        public String image;
    }
}
