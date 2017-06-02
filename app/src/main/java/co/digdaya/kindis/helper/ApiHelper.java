package co.digdaya.kindis.helper;

/**
 * Created by vincenttp on 1/28/2017.
 */

public class ApiHelper {
    public static final String BASE_URL = "http://api.stg.kindis.co/";
    public static final String BASE_URL_IMAGE = "https://cdn.kindis.co";

    //ads
    public static final String ADS_INFAQ = BASE_URL + "infaq/highlight";
    public static final String ADS_BANNER = BASE_URL + "ads/position/banner?dev_id=2&uid=";
    public static final String ADS_INTERTSTITIAL = BASE_URL + "ads/pop?code=71&uid=";
    public static final String ADS_SONG = BASE_URL + "ads/sound?dev_id=2&uid=";

    //sign
    public static final String LOGOUT = BASE_URL+ "sign/logout";
    public static final String REGISTER = BASE_URL + "sign/register";
    public static final String LOGIN = BASE_URL + "sign/login";
    public static final String REGISTER_SOCIAL = BASE_URL + "sign/register_social";
    public static final String LOGIN_SOCIAL = BASE_URL + "sign/login_social";
    public static final String TRANSACTION_HISTORY = BASE_URL + "payment/history?uid=";

    public static final String PROFILE = BASE_URL + "profile/info";
    public static final String UPDATE_AVATAR = BASE_URL + "profile/update_avatar";

    public static final String GENRE_LIST = BASE_URL + "genre/list";
    public static final String TERMS = BASE_URL + "statics/page/terms_of_use";
    public static final String PRIVACY = BASE_URL + "statics/page/privacy_policy";
    public static final String FAQ = BASE_URL + "statics/page/faq";
    public static final String COOKIES = BASE_URL + "statics/page/cookies";
    public static final String INTELECTUAL = BASE_URL + "statics/page/rights";
    public static final String DETAIL_GENRE = BASE_URL + "genre/content_by_genre?genre_id=";

    public static final String MUSIQ = BASE_URL + "home/channel_andro?channel_id=1&uid=";
    public static final String TAKLIM = BASE_URL + "home/channel_andro?channel_id=9&uid=";

    public static final String ITEM_ARTIST = BASE_URL + "artist/items?artist_id=";
    public static final String ITEM_ALBUM = BASE_URL + "album/items?album_id=";
    public static final String ITEM_SINGLE = BASE_URL + "single/single_item";
    public static final String ITEM_SINGLE_GET = BASE_URL + "single/items?single_id=";

    public static final String NOTIFICATION = BASE_URL + "notification/list";
    public static final String FORGOT = BASE_URL + "sign/password_recovery";
    public static final String SEARCH = BASE_URL + "search?q=";
    public static final String SINGLE_GENRE = BASE_URL + "genre/content_by_genre?genre_id=";
    public static final String CONTENT_GENRE = BASE_URL + "genre/content_home_genre?genre_id=";

    public static final String PLAYLIST = BASE_URL + "playlist/user";
    public static final String CREATE_PLAYLIST = BASE_URL + "playlist/create";
    public static final String ITEM_PLAYLIST = BASE_URL + "playlist/items?playlist_id=";
    public static final String INSERT_ITEM_PLAYLIST = BASE_URL + "playlist/insert_item";
    public static final String REMOVE_ITEM_PLAYLIST = BASE_URL + "playlist/remove_item";
    public static final String DELETE_PLAYLIST = BASE_URL + "playlist/delete";

    public static final String DETAIL_PLAYLIST_PREMIUM = BASE_URL + "playlist/pre_item";
    public static final String LIST_PLAYLIST_PREMIUM = BASE_URL + "playlist/pre_list_user?uid=";

    public static final String REFRESH_TOKEN = BASE_URL + "sign/refresh_token";

    public static final String UPDATE_PROFILE = BASE_URL + "profile/update_info";
    public static final String CHANGE_EMAIL = BASE_URL + "profile/update_email";
    public static final String CHANGE_PASSWORD = BASE_URL + "profile/update_password";

    public static final String LIST_INFAQ = BASE_URL + "infaq/list";
    public static final String ITEM_INFAQ = BASE_URL + "infaq/items?infaq_id=";

    public static final String GIFT = BASE_URL + "gift/items";

    public static final String PAYMENT = BASE_URL + "payment/pay";
    public static final String PRICE = BASE_URL + "payment/base_premium?uid=";

    //offline
    public static final String TOKEN = BASE_URL + "offline/json_token";
    public static final String GET_DATA = BASE_URL + "offline/offline_data";
}
