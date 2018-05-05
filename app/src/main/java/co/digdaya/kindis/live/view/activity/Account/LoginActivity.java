package co.digdaya.kindis.live.view.activity.Account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.custom.ButtonSemiBold;
import co.digdaya.kindis.live.custom.EditTextRegular;
import co.digdaya.kindis.live.custom.ShowHidePasswordEditTextRegular;
import co.digdaya.kindis.live.custom.TextViewRegular;
import co.digdaya.kindis.live.helper.AnalyticHelper;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.util.BackgroundProses.ProfileInfo;
import co.digdaya.kindis.live.view.activity.Splash.Bismillah;

/**
 * Created by ryandzhunter on 10/03/18.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.input_email)
    EditTextRegular inputEmail;
    @BindView(R.id.input_password)
    ShowHidePasswordEditTextRegular inputPassword;
    @BindView(R.id.btn_login)
    ButtonSemiBold btnLogin;
    @BindView(R.id.error_message)
    TextViewRegular errorMessage;
    @BindView(R.id.cont_error_message)
    LinearLayout contErrorMessage;
    @BindView(R.id.text_register)
    TextViewRegular textRegister;
    @BindView(R.id.text_forgot)
    TextViewRegular textForgot;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.layout_sign_up)
    LinearLayout layoutSignUp;
    @BindView(R.id.activity_forgot_password)
    LinearLayout activityForgotPassword;
    private LoginActivity mContext;
    private ProgressDialog loading;
    private VolleyHelper volleyHelper;
    private SessionHelper sessionHelper;
    private AnalyticHelper analyticsHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mContext = LoginActivity.this;
        volleyHelper = new VolleyHelper();
        sessionHelper = new SessionHelper();
        analyticsHelper = new AnalyticHelper(this);

        loading = new ProgressDialog(mContext, R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyticsHelper.loginAuth("true");
                if (formValidation()) {
                    login();
                } else {
                    Toast.makeText(mContext, "Username or password can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        inputEmail.setText(sessionHelper.getPreferences(mContext, "email"));

        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivity(intent);
            }
        });

        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ForgotPassword.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private boolean formValidation() {
        return !(inputEmail.getText().length() < 1 || inputPassword.getText().length() < 1);
    }

    private void login() {
        loading.show();
        Map<String, String> params = new HashMap<>();
        params.put("email", inputEmail.getText().toString());
        params.put("password", inputPassword.getText().toString());

        volleyHelper.post(ApiHelper.LOGIN, params, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status) {
                    loading.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
                            JSONObject result = object.getJSONObject("result");
                            sessionHelper.setPreferences(mContext, "token", result.getString("token"));
                            sessionHelper.setPreferences(mContext, "token_access", result.getString("token_access"));
                            sessionHelper.setPreferences(mContext, "token_refresh", result.getString("token_refresh"));
                            sessionHelper.setPreferences(mContext, "login_type", "0");
                            sessionHelper.setPreferences(mContext, "expires_in", String.valueOf(result.optInt("expires_in")));
                            new ProfileInfo(mContext).execute(result.getString("user_id"));
                            analyticsHelper.loginStep("email", "true");
                            Intent intent = new Intent(mContext, Bismillah.class);
                            startActivity(intent);
                        } else {
                            contErrorMessage.setVisibility(View.VISIBLE);
                            errorMessage.setText(object.getString("message"));
                            if (Build.VERSION.SDK_INT >= 21) {
                                inputEmail.setBackground(getResources().getDrawable(R.drawable.edittext_error, null));
                            } else {
                                inputEmail.setBackground(getResources().getDrawable(R.drawable.edittext_error));
                            }
                            analyticsHelper.loginStep("email", "false");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    loading.dismiss();
                    contErrorMessage.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= 21) {
                        inputEmail.setBackground(getResources().getDrawable(R.drawable.edittext_error, null));
                    } else {
                        inputEmail.setBackground(getResources().getDrawable(R.drawable.edittext_error));
                    }
                    analyticsHelper.loginStep("email", "false");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        analyticsHelper.loginAuth("false");
        super.onBackPressed();
    }
}
