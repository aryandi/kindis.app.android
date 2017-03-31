package co.digdaya.kindis.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 3/31/2017.
 */

public class TabModel {

    @SerializedName("tab1")
    public List<Tab2> tab1;

    public static class Tab2 {
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
    }
}
