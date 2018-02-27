package co.digdaya.kindis.live.view.fragment.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.util.BackgroundProses.ProfileInfo;
import co.digdaya.kindis.live.view.activity.Account.ForgotPassword;
import co.digdaya.kindis.live.view.activity.Splash.Bismillah;

public class SignInFragment extends Fragment implements View.OnFocusChangeListener {
    EditText email;
    EditText password;
    AppBarLayout appBarLayout;
    TextView forgotPassword, errorMessage;
    Button login;

    ImageButton loginFacebook;
    ImageButton loginTwitter;
    ImageButton loginGoogle;

    LinearLayout contErrorMessage;
    VolleyHelper volleyHelper;
    ProgressDialog loading;
    SessionHelper sessionHelper;

    CallbackManager callbackManager;
    LoginManager loginManager;

    TwitterAuthClient client;

    private OnClickLoginTwitterListener onClickLoginTwitterListener;
    private OnClickLoginGoogleListener onClickLoginGoogleListener;

    public SignInFragment(){}

    public SignInFragment(AppBarLayout appBarLayout) {
        this.appBarLayout = appBarLayout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = (EditText) view.findViewById(R.id.input_email);
        password = (EditText) view.findViewById(R.id.input_password);
        forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
        errorMessage = (TextView) view.findViewById(R.id.error_message);
        login = (Button) view.findViewById(R.id.btn_login);
        loginFacebook = (ImageButton) view.findViewById(R.id.login_facebook);
        loginTwitter = (ImageButton) view.findViewById(R.id.login_twitter);
        loginGoogle = (ImageButton) view.findViewById(R.id.login_google);
        contErrorMessage = (LinearLayout) view.findViewById(R.id.cont_error_message);
        volleyHelper = new VolleyHelper();
        sessionHelper = new SessionHelper();
        loading = new ProgressDialog(getActivity(), R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);

        email.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ForgotPassword.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formValidation()){
                    login();
                }else {
                    Toast.makeText(getContext(), "Username or password can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginFacebook();
        loginTwitter();
        loginGoogle();
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (appBarLayout != null) appBarLayout.setExpanded(false, true);
    }

    private boolean formValidation(){
        if (email.getText().length()<1 || password.getText().length()<1){
            return false;
        }else {
            return true;
        }
    }

    private void login(){
        loading.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());

        volleyHelper.post(ApiHelper.LOGIN, params, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    loading.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");
                            sessionHelper.setPreferences(getContext(), "token", result.getString("token"));
                            sessionHelper.setPreferences(getContext(), "token_access", result.getString("token_access"));
                            sessionHelper.setPreferences(getContext(), "token_refresh", result.getString("token_refresh"));
                            sessionHelper.setPreferences(getContext(), "login_type", "0");
                            new ProfileInfo(getContext()).execute(result.getString("user_id"));
                            Intent intent = new Intent(getActivity(), Bismillah.class);
                            startActivity(intent);
                        }else {
                            contErrorMessage.setVisibility(View.VISIBLE);
                            errorMessage.setText(object.getString("message"));
                            email.setBackground(getResources().getDrawable(R.drawable.edittext_error, null));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    loading.dismiss();
                    contErrorMessage.setVisibility(View.VISIBLE);
                    if(android.os.Build.VERSION.SDK_INT >= 21){
                        email.setBackground(getResources().getDrawable(R.drawable.edittext_error, null));
                    } else {
                        email.setBackground(getResources().getDrawable(R.drawable.edittext_error));
                    }
                }
            }
        });
    }

    private void loginFacebook(){
        loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginManager.logInWithReadPermissions(getActivity(), Arrays.asList("email", "public_profile"));
            }
        });
    }

    private void loginTwitter(){
        loginTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new TwitterAuthClient();
                onClickLoginTwitterListener.onClick(client);
            }
        });
    }

    private void loginGoogle(){
        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLoginGoogleListener.onClick();
            }
        });
    }

    public void setOnClickLoginTwitterListener(OnClickLoginTwitterListener onClickLoginTwitterListener){
        this.onClickLoginTwitterListener = onClickLoginTwitterListener;
    }

    public interface OnClickLoginTwitterListener{
        void onClick(TwitterAuthClient twitterAuthClient);
    }

    public void setOnClickLoginGoogleListener(OnClickLoginGoogleListener onClickLoginGoogleListener){
        this.onClickLoginGoogleListener = onClickLoginGoogleListener;
    }

    public interface OnClickLoginGoogleListener{
        void onClick();
    }
}