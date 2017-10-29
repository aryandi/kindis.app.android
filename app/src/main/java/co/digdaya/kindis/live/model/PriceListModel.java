package co.digdaya.kindis.live.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vincenttp on 10/24/17.
 */

public class PriceListModel {

    @SerializedName("status")
    public boolean status;
    @SerializedName("message")
    public String message;
    @SerializedName("result")
    public Result result;

    public static class Data {
        @SerializedName("name")
        public String name;
        @SerializedName("price")
        public String price;
        @SerializedName("is_discount")
        public String is_discount;
        @SerializedName("last_update")
        public String last_update;
        @SerializedName("package_id")
        public String package_id;
    }

    public static class Result {
        @SerializedName("data")
        public List<Data> data;
        @SerializedName("order_id")
        public String order_id;
    }
}
