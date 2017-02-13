package sangmaneproject.kindis.helper;

/**
 * Created by vincenttp on 1/28/2017.
 */

public class ApiHelper {
    public static String BASE_URL = "http://api.kindis.co/";
    public static String BASE_URL_IMAGE = "http://cdn.kindis.co";
    public static String REGISTER = BASE_URL + "sign/register";
    public static String LOGIN = BASE_URL + "sign/login";
    public static String PROFILE = BASE_URL + "profile/info";
    public static String GENRE_LIST = BASE_URL + "genre/list";
    public static String TERMS = BASE_URL + "statics/page/terms_of_use";
    public static String PRIVACY = BASE_URL + "statics/page/privacy_policy";
    public static String FAQ = BASE_URL + "statics/page/faq";
    public static String DETAIL_GENRE = BASE_URL + "genre/items?genre_id=";
    public static String MUSIQ = BASE_URL + "home?channel_id=1";
    public static String ITEM_ARTIST = BASE_URL + "artist/items?artist_id=";
    public static String NOTIFICATION = BASE_URL + "notification/list";
    public static String FORGOT = BASE_URL + "sign/password_recovery";
    public static String SEARCH = BASE_URL + "search?q=";
    public static String SINGLE_GENRE = BASE_URL + "single/list_by_genre?genre_id=";
}
