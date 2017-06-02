package co.digdaya.kindis.helper;

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

    public void logEvent(){
        Bundle bundle = new Bundle();
    }
}
