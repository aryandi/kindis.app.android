package co.digdaya.kindis.live.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 6/2/2017.
 */

public class TransactionHistoryModel {

    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("result")
    public List<Result> result;

    public static class Result {
        @SerializedName("title")
        public String title;
        @SerializedName("date")
        public String date;
        @SerializedName("status")
        public String status;
    }
}
