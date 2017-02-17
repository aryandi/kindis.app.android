package sangmaneproject.kindis.helper;

/**
 * Created by vincenttp on 1/28/2017.
 */

public class ApiHelper {
    public static final String BASE_URL = "http://api.stg.kindis.co/";
    public static final String BASE_URL_IMAGE = "http://cdn.kindis.co";

    public static final String REGISTER = BASE_URL + "sign/register";
    public static final String LOGIN = BASE_URL + "sign/login";
    public static final String PROFILE = BASE_URL + "profile/info";
    public static final String GENRE_LIST = BASE_URL + "genre/list";
    public static final String TERMS = BASE_URL + "statics/page/terms_of_use";
    public static final String PRIVACY = BASE_URL + "statics/page/privacy_policy";
    public static final String FAQ = BASE_URL + "statics/page/faq";
    public static final String DETAIL_GENRE = BASE_URL + "genre/items?genre_id=";
    public static final String MUSIQ = BASE_URL + "home?channel_id=1";
    public static final String TAKLIM = BASE_URL + "home?channel_id=9";
    public static final String ITEM_ARTIST = BASE_URL + "artist/items?artist_id=";
    public static final String ITEM_ALBUM = BASE_URL + "album/items?album_id=";
    public static final String ITEM_SINGLE = BASE_URL + "single/items";
    public static final String NOTIFICATION = BASE_URL + "notification/list";
    public static final String FORGOT = BASE_URL + "sign/password_recovery";
    public static final String SEARCH = BASE_URL + "search?q=";
    public static final String SINGLE_GENRE = BASE_URL + "single/list_by_genre?genre_id=";
    public static final String PLAYLIST = BASE_URL + "playlist/user";
    public static final String CREATE_PLAYLIST = BASE_URL + "playlist/create";
}
