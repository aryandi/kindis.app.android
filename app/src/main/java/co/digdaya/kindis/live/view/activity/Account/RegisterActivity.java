package co.digdaya.kindis.live.view.activity.Account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
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
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.TextViewHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.util.BackgroundProses.ProfileInfo;
import co.digdaya.kindis.live.view.activity.Splash.Bismillah;

/**
 * Created by ryandzhunter on 10/03/18.
 */

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.input_nama)
    EditTextRegular inputNama;
    @BindView(R.id.input_email)
    EditTextRegular inputEmail;
    @BindView(R.id.input_password)
    ShowHidePasswordEditTextRegular inputPassword;
    @BindView(R.id.btn_sign_up)
    ButtonSemiBold btnSignUp;
    @BindView(R.id.text_login)
    TextViewRegular textLogin;
    private RegisterActivity mContext;
    private VolleyHelper volleyHelper;
    private SessionHelper sessionHelper;
    private ProgressDialog loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mContext = RegisterActivity.this;

        volleyHelper = new VolleyHelper();
        sessionHelper = new SessionHelper();

        loading = new ProgressDialog(mContext, R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formValidation()) {
                    if (inputPassword.getText().length() < 8) {
                        Toast.makeText(mContext, "Password must be 8 characters", Toast.LENGTH_SHORT).show();
                    } else {
                        register();
                    }
                } else {
                    Toast.makeText(mContext, "Please fill the form", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ClickableSpan registerClickSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
//                ds.setColor(ContextCompat.getColor(RegisterActivity.this, R.color.blue_button_disabled_state));
            }
        };
        TextViewHelper.makeLinks(textLogin, new String[] {"Login here"}, new ClickableSpan[] {registerClickSpan});
    }

    private boolean formValidation() {
        return !(inputNama.getText().length() < 1 || inputEmail.getText().length() < 1 || inputPassword.getText().length() < 1);
    }

    private void register() {
        loading.show();
        Map<String, String> params = new HashMap<>();
        params.put("fullname", inputNama.getText().toString());
        params.put("email", inputEmail.getText().toString());
        params.put("password", inputPassword.getText().toString());

        volleyHelper.post(ApiHelper.REGISTER, params, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status) {
                    loading.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
                            JSONObject result = object.getJSONObject("result");
                            sessionHelper.setPreferences(mContext, "expires_in", String.valueOf(result.optInt("expires_in")));
                            sessionHelper.setPreferences(mContext, "token", result.getString("token"));
                            sessionHelper.setPreferences(mContext, "token_access", result.getString("token_access"));
                            sessionHelper.setPreferences(mContext, "token_refresh", result.getString("token_refresh"));
                            new ProfileInfo(mContext).execute(result.getString("user_id"));
                            Intent intent = new Intent(mContext, Bismillah.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(mContext, object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
