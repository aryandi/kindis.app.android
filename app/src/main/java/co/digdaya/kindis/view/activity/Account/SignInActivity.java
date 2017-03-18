package co.digdaya.kindis.view.activity.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.util.BackgroundProses.DialogLoading;
import co.digdaya.kindis.util.BackgroundProses.ProfileInfo;
import co.digdaya.kindis.view.activity.Splash.Bismillah;
import co.digdaya.kindis.view.fragment.signin.SignInFragment;
import co.digdaya.kindis.view.fragment.signin.SignUpFragment;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

public class SignInActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    AppBarLayout appBarLayout;

    CallbackManager callbackManager;
    LoginManager loginManager;
    DialogLoading dialogLoading;

    VolleyHelper volleyHelper;
    SessionHelper sessionHelper;

    TwitterAuthClient client;
    TwitterAuthClient authClient;

    SignInFragment signInFragment;
    GoogleApiClient mGoogleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        volleyHelper = new VolleyHelper();
        dialogLoading = new DialogLoading(this);
        sessionHelper = new SessionHelper();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_key), getString(R.string.twitter_secret));
        Fabric.with(this, new Twitter(authConfig));

        client = new TwitterAuthClient();
        authClient = new TwitterAuthClient();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        System.out.println("googlelogin "+connectionResult.getErrorMessage());
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);

        viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);

        loginFacebook();
        loginTwitter();
        loginGoogle();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        client.onActivityResult(requestCode, resultCode, data);
        authClient.onActivityResult(requestCode, resultCode, data);
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        handleSignInResult(result);
    }

    private void setupViewPager(ViewPager viewPager) {
        signInFragment = new SignInFragment(appBarLayout);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(signInFragment, "Sign In");
        adapter.addFragment(new SignUpFragment(appBarLayout), "Sign Up");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void loginFacebook(){
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                sessionHelper.setPreferences(getApplicationContext(), "profile_picture", profile.getProfilePictureUri(100, 100).toString());

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                System.out.println(object);
                                System.out.println(response);
                                try {
                                    String fullname = object.getString("name");
                                    String gender = object.getString("gender");
                                    String birth_date = object.optString("birth_date");
                                    String type_social = "1";
                                    String app_id = object.getString("id");
                                    String email = object.getString("email");
                                    String phone = "";
                                    loginSocial(fullname, gender, birth_date, type_social, app_id, email, phone);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,gender,email,birthday,cover");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // Handle cancel event
                Log.d("FacebookLogin", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                //Handle Error event
                Log.d("FacebookLogin", "error");
            }
        });
    }

    private void loginTwitter(){
        signInFragment.setOnClickLoginTwitterListener(new SignInFragment.OnClickLoginTwitterListener() {
            @Override
            public void onClick(TwitterAuthClient twitterAuthClient) {
                client.authorize(SignInActivity.this, new Callback<TwitterSession>() {
                    @Override
                    public void success(final Result<TwitterSession> twitterSessionResult) {
                        TwitterSession twitterSession = twitterSessionResult.data;

                        Call<User> call = Twitter.getApiClient(twitterSession).getAccountService().verifyCredentials(true, false);
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void success(Result<User> result) {
                                System.out.println("logintwitter"+result.data.email);
                                String fullname = result.data.name;
                                String gender = "";
                                String birth_date = "";
                                String type_social = "1";
                                String app_id = String.valueOf(twitterSessionResult.data.getUserId());
                                String email = result.data.email;
                                String phone = "";
                                loginSocial(fullname, gender, birth_date, type_social, app_id, email, phone);
                            }

                            @Override
                            public void failure(TwitterException e) {

                            }
                        });
                    }

                    @Override
                    public void failure(TwitterException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loginGoogle(){
        signInFragment.setOnClickLoginGoogleListener(new SignInFragment.OnClickLoginGoogleListener() {
            @Override
            public void onClick() {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, 3);
            }
        });
    }

    private void handleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            System.out.println("googlelogin"+acct.getDisplayName());
            System.out.println("googlelogin"+acct.getEmail());
            System.out.println("googlelogin"+acct.getId());

            String fullname = acct.getDisplayName();
            String gender = "";
            String birth_date = "";
            String type_social = "3";
            String app_id = acct.getId();
            String email = acct.getEmail();
            String phone = "";
            loginSocial(fullname, gender, birth_date, type_social, app_id, email, phone);
        } else {
            System.out.println("googlelogin "+result.getStatus());
        }
    }

    private void loginSocial(final String fullname, final String gender, final String birth_date, final String type_social, final String app_id, final String email, final String phone){
        dialogLoading.showLoading();
        HashMap<String, String> param = new HashMap<>();
        param.put("app_id", app_id);
        param.put("type_social", type_social);
        param.put("keyss", "QUTWnIZTyeMZBi0AI3IiXkgzTATH2Y8PEMACjH3ZUFE%3D");

        volleyHelper.post(ApiHelper.LOGIN_SOCIAL, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                Log.d("response", response);
                dialogLoading.dismisLoading();
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");
                            new ProfileInfo(getApplicationContext()).execute(result.getString("user_id"));
                            Intent intent = new Intent(SignInActivity.this, Bismillah.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(SignInActivity.this, Register.class);
                            intent.putExtra("fullname", fullname);
                            intent.putExtra("gender", gender);
                            intent.putExtra("birth_date", birth_date);
                            intent.putExtra("type_social", type_social);
                            intent.putExtra("app_id", app_id);
                            intent.putExtra("email", email);
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
