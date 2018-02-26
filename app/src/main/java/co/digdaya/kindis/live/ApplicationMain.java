package co.digdaya.kindis.live;

import android.app.Application;
import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by vincenttp on 1/28/2017.
 */

public class ApplicationMain extends Application {


    private static ApplicationMain instance;

    public static synchronized ApplicationMain getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_key), getString(R.string.twitter_secret));
        Fabric.with(this, new Twitter(authConfig), new Crashlytics());
        instance = this;
    }
}
