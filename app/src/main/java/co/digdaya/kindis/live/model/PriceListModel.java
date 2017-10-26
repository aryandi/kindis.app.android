package co.digdaya.kindis.live.model;

import com.google.gson.annotations.SerializedName;

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

    public static class M0 {
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

    public static class M1 {
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

    public static class M2 {
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

    public static class M3 {
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

    public static class M4 {
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
        @SerializedName("0")
        public M0 m0;
        @SerializedName("1")
        public M1 m1;
        @SerializedName("2")
        public M2 m2;
        @SerializedName("3")
        public M3 m3;
        @SerializedName("4")
        public M4 m4;
        @SerializedName("order_id")
        public String order_id;
    }
}
