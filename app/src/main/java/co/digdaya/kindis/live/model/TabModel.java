package co.digdaya.kindis.live.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 3/31/2017.
 */

public class TabModel {

    @SerializedName("tab1")
    public List<Tab> tab1;

    @SerializedName("tab2")
    public List<Tab> tab2;

    @SerializedName("tab3")
    public List<Tab> tab3;

    public static class Tab {
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
        public String data;
    }
}
