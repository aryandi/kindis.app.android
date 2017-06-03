package co.digdaya.kindis;

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
        instance = this;
    }
}
