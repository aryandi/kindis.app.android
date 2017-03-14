package sangmaneproject.kindis.view.fragment.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.util.BackgroundProses.ProfileInfo;
import sangmaneproject.kindis.view.activity.Account.ForgotPassword;
import sangmaneproject.kindis.view.activity.Splash.Bismillah;

public class SignInFragment extends Fragment implements View.OnFocusChangeListener {
    EditText email;
    EditText password;
    AppBarLayout appBarLayout;
    TextView forgotPassword, errorMessage;
    Button login;

    ImageButton loginFacebook;

    LinearLayout contErrorMessage;
    VolleyHelper volleyHelper;
    ProgressDialog loading;

    CallbackManager callbackManager;
    LoginButton buttonFacebook;

    public SignInFragment(){}

    public SignInFragment(AppBarLayout appBarLayout) {
        this.appBarLayout = appBarLayout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        buttonFacebook = (LoginButton) view.findViewById(R.id.button_facebook);
        contErrorMessage = (LinearLayout) view.findViewById(R.id.cont_error_message);
        volleyHelper = new VolleyHelper();
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
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        appBarLayout.setExpanded(false, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("FacebookLogin", requestCode+" : "+resultCode);
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
                            new SessionHelper().setPreferences(getContext(), "token", result.getString("token"));
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
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // Use Access Token loginResult.getAccessToken().getToken());
                Log.d("FacebookLogin", loginResult.getAccessToken().getUserId());
                Log.d("FacebookLogin", loginResult.getAccessToken().getUserId());

                Profile profile = Profile.getCurrentProfile();
                Log.d("FacebookLogin", profile.getName());
                Log.d("FacebookLogin", profile.getProfilePictureUri(100, 100).toString());

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

        loginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("email", "public_profile", "user_friends"));
            }
        });
    }
}
