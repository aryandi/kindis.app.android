package co.digdaya.kindis.live;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by vincenttp on 1/28/2017.
 */

public class MainApplication extends MultiDexApplication {

    private static MainApplication instance;

    public static synchronized MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_key), getString(R.string.twitter_secret));
        Fabric.with(this, new Twitter(authConfig), new Crashlytics());
        instance = this;
    }
}
