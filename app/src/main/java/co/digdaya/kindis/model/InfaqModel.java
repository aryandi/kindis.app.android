package co.digdaya.kindis.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 3/14/2017.
 */

public class InfaqModel {

    @SerializedName("status")
    public boolean status;
    @SerializedName("result")
    public List<Result> result;
    @SerializedName("total_result")
    public String total_result;
    @SerializedName("page")
    public int page;
    @SerializedName("next_page")
    public String next_page;

    public static class Result {
        @SerializedName("uid")
        public String uid;
        @SerializedName("title")
        public String title;
        @SerializedName("main_image")
        public String main_image;
        @SerializedName("banner_image")
        public String banner_image;
        @SerializedName("date_created")
        public String date_created;
        @SerializedName("is_url")
        public String is_url;
        @SerializedName("redirect_url")
        public String redirect_url;
    }
}
