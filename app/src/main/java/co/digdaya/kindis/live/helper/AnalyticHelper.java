package co.digdaya.kindis.live.helper;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by DELL on 6/2/2017.
 */

public class AnalyticHelper {
    Activity activity;
    private FirebaseAnalytics mFirebaseAnalytics;

    public AnalyticHelper(Activity activity) {
        this.activity = activity;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    }

    public void event(String track){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "kindis");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, track);
        logEvent(bundle);
    }

    private void logEvent(Bundle bundle){
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public void logEvent(String event, Bundle bundle){
        mFirebaseAnalytics.logEvent(event, bundle);
    }

    public void journeySwipe(String journeyType, String swipeDirection){
        Bundle bundle = new Bundle();
        bundle.putString("journey_type", journeyType);
        bundle.putString("swipe_direction", swipeDirection);
        mFirebaseAnalytics.logEvent("journey_swipe", bundle);
    }

    public void journeySkip(String journeyType){
        Bundle bundle = new Bundle();
        bundle.putString("journey_type", journeyType);
        mFirebaseAnalytics.logEvent("journey_skip", bundle);
    }

    public void loginStep(String typeSocial, String isSuccess){
        Bundle bundle = new Bundle();
        bundle.putString("type", typeSocial);
        bundle.putString("is_success", isSuccess);
        mFirebaseAnalytics.logEvent("login_step", bundle);
    }

    public void loginAuth(String loginClick){
        Bundle bundle = new Bundle();
        bundle.putString("login_click", loginClick);
        mFirebaseAnalytics.logEvent("login_auth", bundle);
    }

    public void loginTerms(String termsToogle){
        Bundle bundle = new Bundle();
        bundle.putString("terms_toogle", termsToogle);
        mFirebaseAnalytics.logEvent("login_terms", bundle);
    }

    public void authRegister(String registerClick){
        Bundle bundle = new Bundle();
        bundle.putString("register_click", registerClick);
        mFirebaseAnalytics.logEvent("auth_register", bundle);
    }

    public void authHello(String helloClick){
        Bundle bundle = new Bundle();
        bundle.putString("hello_click", helloClick);
        mFirebaseAnalytics.logEvent("auth_hello", bundle);
    }

    public void clickMenu(String type, String value){
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        bundle.putString("value", value);
        mFirebaseAnalytics.logEvent("click_menu", bundle);
    }

    public void giftGet(String code, String isSuccess){
        Bundle bundle = new Bundle();
        bundle.putString("code", code);
        bundle.putString("is_success", isSuccess);
        mFirebaseAnalytics.logEvent("gift_get", bundle);
    }

    public void swipeOffline(String value){
        Bundle bundle = new Bundle();
        bundle.putString("value", value);
        mFirebaseAnalytics.logEvent("swipe_offline", bundle);
    }

    public void profileEdit(String value){
        Bundle bundle = new Bundle();
        bundle.putString("value", value);
        mFirebaseAnalytics.logEvent("profile_edit", bundle);
    }

    public void profileOption(String value){
        Bundle bundle = new Bundle();
        bundle.putString("value", value);
        mFirebaseAnalytics.logEvent("profile_option", bundle);
    }

    public void clickNavMenu(String value){
        Bundle bundle = new Bundle();
        bundle.putString("value", value);
        mFirebaseAnalytics.logEvent("click_nav_menu", bundle);
    }

    public void contentClick(String origin, String contentId, String contentType,
                             String contentName, String referrer, String contentLocation){
        Bundle bundle = new Bundle();
        bundle.putString("origin", origin);
        bundle.putString("content_id", contentId);
        bundle.putString("content_type", contentType);
        bundle.putString("content_name", contentName);
        bundle.putString("referrer", referrer);
        bundle.putString("content_location", contentLocation);
        mFirebaseAnalytics.logEvent("content_click", bundle);
    }

    public void contentSlider(String origin, String referrer, String sliderId, String sliderTitle){
        Bundle bundle = new Bundle();
        bundle.putString("origin", origin);
        bundle.putString("referrer", referrer);
        bundle.putString("slider_id", sliderId);
        bundle.putString("slider_title", sliderTitle);
        mFirebaseAnalytics.logEvent("content_slider", bundle);
    }

    public void playlistAction(String location, String type, String playlistName, String isSuccess){
        Bundle bundle = new Bundle();
        bundle.putString("location", location);
        bundle.putString("type", type);
        bundle.putString("playlist_name", playlistName);
        bundle.putString("is_success", isSuccess);
        mFirebaseAnalytics.logEvent("playlist_action", bundle);
    }

    public void searchAction(String searchItem, String isSuccess){
        Bundle bundle = new Bundle();
        bundle.putString("search_item", searchItem);
        bundle.putString("is_success", isSuccess);
        mFirebaseAnalytics.logEvent("search_action", bundle);
    }

    public void playerToogle(String pullToogle){
        Bundle bundle = new Bundle();
        bundle.putString("pull_toogle", pullToogle);
        mFirebaseAnalytics.logEvent("player_toogle", bundle);
    }

    public void playerAction(String origin, String playToogle, String contentId, String contentName, String cutListenMinutes){
        Bundle bundle = new Bundle();
        bundle.putString("origin", origin);
        bundle.putString("play_toogle", playToogle);
        bundle.putString("content_id", contentId);
        bundle.putString("content_name", contentName);
        bundle.putString("cut_listen_minutes", cutListenMinutes);
        mFirebaseAnalytics.logEvent("player_action", bundle);
    }

    public void shareAction(String origin, String contentId, String contentName, String shareType){
        Bundle bundle = new Bundle();
        bundle.putString("origin", origin);
        bundle.putString("content_id", contentId);
        bundle.putString("content_name", contentName);
        bundle.putString("share_type", shareType);
        mFirebaseAnalytics.logEvent("share_action", bundle);
    }

    public void premiumToogle(String value){
        Bundle bundle = new Bundle();
        bundle.putString("value", value);
        mFirebaseAnalytics.logEvent("premium_toogle", bundle);
    }

    public void premiumSubscribeClick(String transactionId, String productCategory,
                                      String productReferrer, String productName, String discount,
                                      String price, String paymentMethod){
        Bundle bundle = new Bundle();
        bundle.putString("transaction_id", transactionId);
        bundle.putString("product_category", productCategory);
        bundle.putString("product_referrer", productReferrer);
        bundle.putString("product_name", productName);
        bundle.putString("discount", discount);
        bundle.putString("price", price);
        bundle.putString("payment_method", paymentMethod);
        mFirebaseAnalytics.logEvent("premium_subscribe_click", bundle);
    }

}
