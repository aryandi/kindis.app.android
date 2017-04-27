package co.digdaya.kindis.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 4/27/2017.
 */

public class DetailInfaqModel {

    @SerializedName("status")
    public boolean status;
    @SerializedName("result")
    public Result result;

    public static class Main {
        @SerializedName("title")
        public String title;
        @SerializedName("description")
        public String description;
        @SerializedName("target")
        public String target;
        @SerializedName("main_image")
        public String main_image;
        @SerializedName("banner_image")
        public String banner_image;
        @SerializedName("date_updated")
        public String date_updated;
        @SerializedName("is_url")
        public String is_url;
        @SerializedName("redirect_url")
        public String redirect_url;
    }

    public static class Gallery {
        @SerializedName("img_name")
        public String img_name;
        @SerializedName("media_url")
        public String media_url;
    }

    public static class Transaction {
        @SerializedName("total")
        public String total;
    }

    public static class Result {
        @SerializedName("main")
        public List<Main> main;
        @SerializedName("gallery")
        public List<Gallery> gallery;
        @SerializedName("transaction")
        public List<Transaction> transaction;
    }
}
