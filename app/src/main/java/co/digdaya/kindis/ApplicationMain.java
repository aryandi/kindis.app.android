package co.digdaya.kindis;

import android.app.Application;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

/**
 * Created by vincenttp on 1/28/2017.
 */

public class ApplicationMain extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "PdLzmoJOddNBQclBKDrsqYA07";
    private static final String TWITTER_SECRET = "WQ3ZM5uEFSPtpc3a2G3cfBAMZnSHgNHIwc6ljhLW7PaiRj0mez";

    private static ApplicationMain instance;

    public static synchronized ApplicationMain getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        /*PsiMethod:onCreateTwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        PsiMethod:onCreateFabric.with(this, new Twitter(authConfig));*/
        instance = this;
    }
}
