package co.digdaya.kindis.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 4/3/2017.
 */

public class MoreModel {
    public static class AlbumMore{
        @SerializedName("result")
        public List<DataAlbum.Data> result;
    }
    public static class SinggleMore{
        @SerializedName("result")
        public List<DataSingle.Data> result;
    }

    public static class PlaylisMore{
        @SerializedName("result")
        public List<DataPlaylist.Data> result;
    }
}
