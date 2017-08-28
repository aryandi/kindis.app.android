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
}
