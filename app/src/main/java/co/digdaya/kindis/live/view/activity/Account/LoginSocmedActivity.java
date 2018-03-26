package co.digdaya.kindis.live.view.activity.Account;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.midtrans.sdk.uikit.fragments.WebviewFragment;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.custom.ButtonSemiBold;
import co.digdaya.kindis.live.custom.TextViewRegular;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.TextViewHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.util.BackgroundProses.ProfileInfo;
import co.digdaya.kindis.live.view.activity.Splash.Bismillah;
import co.digdaya.kindis.live.view.dialog.DialogLoading;
import co.digdaya.kindis.live.view.fragment.navigationview.Privacy;
import co.digdaya.kindis.live.view.fragment.navigationview.Terms;
import co.digdaya.kindis.live.view.fragment.webview.WebViewActivity;
import retrofit2.Call;

/**
 * Created by ryandzhunter on 10/03/18.
 */

public class LoginSocmedActivity extends AppCompatActivity {

    AppBarLayout appBarLayout;

    CallbackManager callbackManager;
    LoginManager loginManager;
    DialogLoading dialogLoading;

    VolleyHelper volleyHelper;
    SessionHelper sessionHelper;

    TwitterAuthClient client;
    @BindView(R.id.login_facebook)
    ImageButton loginFacebook;
    @BindView(R.id.login_twitter)
    ImageButton loginTwitter;
    @BindView(R.id.login_google)
    ImageButton loginGoogle;
    @BindView(R.id.btn_login)
    ButtonSemiBold btnLogin;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.text_term)
    TextViewRegular textTerm;
    private GoogleApiClient mGoogleApiClient;
    private LoginSocmedActivity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_socmed);
        ButterKnife.bind(this);

        mContext = LoginSocmedActivity.this;
        volleyHelper = new VolleyHelper();
        dialogLoading = new DialogLoading(this);
        sessionHelper = new SessionHelper();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);

        loginFB();
        loginTwitter();
        loginGoogle();

        checkbox.setChecked(true);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()) {
                    Intent intent;
                    if (TextUtils.isEmpty(sessionHelper.getPreferences(mContext, "email"))) {
                        intent = new Intent(mContext, RegisterActivity.class);
                    } else {
                        intent = new Intent(mContext, LoginActivity.class);
                    }
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "Please check T&A and Privacy Policy below", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ClickableSpan termClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSocmedActivity.this, WebViewActivity.class);
                intent.putExtra("title", "Terms and Agreements");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
//                ds.setColor(ContextCompat.getColor(RegisterActivity.this, R.color.blue_button_disabled_state));
            }
        };

        ClickableSpan privacyClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSocmedActivity.this, WebViewActivity.class);
                intent.putExtra("title", "Privacy Policy");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
//                ds.setColor(ContextCompat.getColor(RegisterActivity.this, R.color.blue_button_disabled_state));
            }
        };

        TextViewHelper.makeLinks(textTerm, new String[]{"Terms and Agreements", "Privacy Policy"}, new ClickableSpan[]{termClickSpan, privacyClickSpan});

    }

    private void loginGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        System.out.println("googlelogin " + connectionResult.getErrorMessage());
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()) {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, 3);
                } else {
                    Toast.makeText(mContext, R.string.tna_warning,  Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginFB() {
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                System.out.println("loginsosmed : " + object);
                                System.out.println("loginsosmed : " + response);
                                try {
                                    sessionHelper.setPreferences(getApplicationContext(), "login_type", "1");
                                    String fullname = object.getString("name");
                                    String gender = object.getString("gender");
                                    String birth_date = object.optString("birth_date");
                                    String type_social = "1";
                                    String app_id = object.getString("id");
                                    String email = object.getString("email");
                                    String phone = "";
                                    String social_name = object.optString("name");
                                    sessionHelper.setPreferences(getApplicationContext(), "social_name_facebook", social_name);
                                    loginSocial(fullname, gender, birth_date, type_social, app_id, email, phone, social_name);
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
                System.out.println("FacebookLogin" + error.getMessage());
            }
        });

        loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()) {
                    loginManager.logInWithReadPermissions(mContext, Arrays.asList("email", "public_profile"));
                } else {
                    Toast.makeText(mContext, R.string.tna_warning, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loginTwitter() {
        client = new TwitterAuthClient();
        loginTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()) {
                    client.authorize(mContext, new Callback<TwitterSession>() {
                        @Override
                        public void success(final Result<TwitterSession> twitterSessionResult) {
                            TwitterSession twitterSession = twitterSessionResult.data;

                            Call<User> call = Twitter.getApiClient(twitterSession).getAccountService().verifyCredentials(true, false);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void success(Result<User> result) {
                                    System.out.println("logintwitter" + result.data.email);

                                    sessionHelper.setPreferences(getApplicationContext(), "profile_picture", result.data.profileImageUrl);
                                    sessionHelper.setPreferences(getApplicationContext(), "login_type", "2");

                                    String fullname = result.data.name;
                                    String gender = "";
                                    String birth_date = "";
                                    String type_social = "2";
                                    String app_id = String.valueOf(twitterSessionResult.data.getUserId());
                                    String email = result.data.email;
                                    String phone = "";
                                    String social_name = result.data.screenName;
                                    sessionHelper.setPreferences(getApplicationContext(), "social_name_twitter", social_name);
                                    loginSocial(fullname, gender, birth_date, type_social, app_id, email, phone, social_name);
                                }

                                @Override
                                public void failure(TwitterException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void failure(TwitterException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(mContext, R.string.tna_warning, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestCode : " + requestCode);
        if (requestCode == 3) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode == 140) {
            client.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == 64206) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                if (acct.getPhotoUrl() != null) sessionHelper.setPreferences(getApplicationContext(), "profile_picture", acct.getPhotoUrl().toString());
                sessionHelper.setPreferences(getApplicationContext(), "login_type", "3");
                String fullname = acct.getDisplayName();
                String gender = "";
                String birth_date = "";
                String type_social = "3";
                String app_id = acct.getId();
                String email = acct.getEmail();
                String phone = "";
                String social_name = acct.getDisplayName();
                sessionHelper.setPreferences(getApplicationContext(), "social_name_google", social_name);
                loginSocial(fullname, gender, birth_date, type_social, app_id, email, phone, social_name);
            } else {
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            System.out.println("googlelogin " + result.getStatus());
        }
    }

    private void loginSocial(final String fullname, final String gender, final String birth_date,
                             final String type_social, final String app_id, final String email,
                             final String phone, final String social_name) {
        dialogLoading.showLoading();
        HashMap<String, String> param = new HashMap<>();
        param.put("app_id", app_id);
        param.put("type_social", type_social);
        param.put("social_name", social_name);
        param.put("keyss", "QUTWnIZTyeMZBi0AI3IiXkgzTATH2Y8PEMACjH3ZUFE%3D");

        System.out.println("app_id : " + app_id);
        volleyHelper.post(ApiHelper.LOGIN_SOCIAL, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                Log.d("response", response);
                dialogLoading.dismisLoading();
                if (status) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
                            JSONObject result = object.getJSONObject("result");
                            sessionHelper.setPreferences(getApplicationContext(), "token", result.getString("token"));
                            sessionHelper.setPreferences(getApplicationContext(), "token_access", result.getString("token_access"));
                            sessionHelper.setPreferences(getApplicationContext(), "token_refresh", result.getString("token_refresh"));
                            sessionHelper.setPreferences(getApplicationContext(), "expires_in", String.valueOf(result.optInt("expires_in")));
                            sessionHelper.setPreferences(getApplicationContext(), "email", email);
                            new ProfileInfo(getApplicationContext()).execute(result.getString("user_id"));
                            Intent intent = new Intent(LoginSocmedActivity.this, Bismillah.class);
                            startActivity(intent);
                        } else {
                            registerSocial(fullname, type_social, app_id, email, social_name);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void registerSocial(String fullname, String typeSocial, String appId, String email, String social_name) {

        HashMap<String, String> param = new HashMap<>();
        param.put("fullname", fullname);
        param.put("type_social", typeSocial);
        param.put("app_id", appId);
        param.put("email", email == null ? "" : email);
        param.put("social_name", social_name);

        volleyHelper.post(ApiHelper.REGISTER_SOCIAL, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                dialogLoading.dismisLoading();
                if (status) {
                    Log.d("response", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
                            JSONObject result = object.getJSONObject("result");
                            sessionHelper.setPreferences(getApplicationContext(), "token", result.getString("token"));
                            sessionHelper.setPreferences(getApplicationContext(), "token_access", result.getString("token_access"));
                            sessionHelper.setPreferences(getApplicationContext(), "token_refresh", result.getString("token_refresh"));
                            sessionHelper.setPreferences(getApplicationContext(), "expires_in", String.valueOf(result.optInt("expires_in")));
                            sessionHelper.setPreferences(getApplicationContext(), "email", result.optString("email"));
                            new ProfileInfo(getApplicationContext()).execute(result.getString("user_id"));
                            Intent intent = new Intent(mContext, Bismillah.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Register dengan Social Media Gagal", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Register dengan Social Media Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
