package co.digdaya.kindis.live.view.fragment.signin;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import co.digdaya.kindis.live.view.activity.Splash.Bismillah;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnFocusChangeListener {
    AppBarLayout appBarLayout;
    EditText fullname;
    EditText email;
    EditText password;
    /*EditText retypePassword;
    EditText birtday;
    RadioGroup radioGroup;
    RadioButton male;
    RadioButton female;*/

    ImageButton loginFacebook;
    ImageButton loginTwitter;
    ImageButton loginGoogle;
    Button signUp;
    VolleyHelper volleyHelper;
    SessionHelper sessionHelper;
    ProgressDialog loading;
    TabLayout tabLayout;
    private OnClickLoginTwitterListener onClickLoginTwitterListener;
    private OnClickLoginGoogleListener onClickLoginGoogleListener;
    LoginManager loginManager;
    TwitterAuthClient client;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public SignUpFragment(AppBarLayout appBarLayout){
        this.appBarLayout = appBarLayout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginManager = LoginManager.getInstance();
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        volleyHelper = new VolleyHelper();
        sessionHelper = new SessionHelper();

        loading = new ProgressDialog(getActivity(), R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);

        tabLayout = (TabLayout) getActivity().findViewById(R.id.htab_tabs);

        fullname = (EditText) view.findViewById(R.id.input_nama);
        email = (EditText) view.findViewById(R.id.input_email);
        password = (EditText) view.findViewById(R.id.input_password);
/*        retypePassword = (EditText) view.findViewById(R.id.input_retype);
        birtday = (EditText) view.findViewById(R.id.input_birthday);
        radioGroup = (RadioGroup) view.findViewById(R.id.gender);
        male = (RadioButton) view.findViewById(R.id.male);
        female = (RadioButton) view.findViewById(R.id.female);*/
        signUp = (Button) view.findViewById(R.id.btn_sign_up);
        loginFacebook = (ImageButton) view.findViewById(R.id.login_facebook);
        loginTwitter = (ImageButton) view.findViewById(R.id.login_twitter);
        loginGoogle = (ImageButton) view.findViewById(R.id.login_google);

        fullname.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
//        retypePassword.setOnFocusChangeListener(this);

        /*birtday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    calenderDialog();
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (male.isChecked()){
                    male.setTextColor(Color.parseColor("#000000"));
                    female.setTextColor(Color.parseColor("#ffffff"));
                }else {
                    male.setTextColor(Color.parseColor("#ffffff"));
                    female.setTextColor(Color.parseColor("#000000"));
                }
            }
        });*/

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formValidation()){
                    if (password.getText().length()<8){
                        Toast.makeText(getActivity(), "Password must be 8 characters", Toast.LENGTH_SHORT).show();
                    }else {
                        register();
                    }
                }else {
                    Toast.makeText(getActivity(), "Please fill the form", Toast.LENGTH_SHORT).show();
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

    /*private void calenderDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        birtday.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, 1990, 0, 1);
        datePickerDialog.show();
    }*/

    private boolean formValidation(){
        return !(fullname.getText().length() < 1 || email.getText().length() < 1 || password.getText().length() < 1);

    }

    private void register(){
        /*String gender;
        if (male.isChecked()){
            gender = "male";
        }else {
            gender = "female";
        }*/

//        if (password.getText().toString().equals(retypePassword.getText().toString())){
            loading.show();
            Map<String, String> params = new HashMap<>();
            params.put("fullname", fullname.getText().toString());
            params.put("email", email.getText().toString());
            params.put("password", password.getText().toString());
//            params.put("gender", gender);
//            params.put("birth_date", birtday.getText().toString());

            volleyHelper.post(ApiHelper.REGISTER, params, new VolleyHelper.HttpListener<String>() {
                @Override
                public void onReceive(boolean status, String message, String response) {
                    if (status){
                        loading.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("status")){
                                JSONObject result = object.getJSONObject("result");
                                sessionHelper.setPreferences(getActivity(), "expires_in", String.valueOf(result.optInt("expires_in")));
                                sessionHelper.setPreferences(getActivity(), "token", result.getString("token"));
                                sessionHelper.setPreferences(getActivity(), "token_access", result.getString("token_access"));
                                sessionHelper.setPreferences(getActivity(), "token_refresh", result.getString("token_refresh"));
                                new ProfileInfo(getContext()).execute(result.getString("user_id"));
//                                Toast.makeText(getActivity(), "Please check your email for verification", Toast.LENGTH_SHORT).show();
//                                tabLayout.getTabAt(0).select();
                                Intent intent = new Intent(getActivity(), Bismillah.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        /*}
        else {
            Toast.makeText(getActivity(), "Password not match", Toast.LENGTH_SHORT).show();
        }*/
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
                if (onClickLoginTwitterListener != null) onClickLoginTwitterListener.onLoginTwitterClick(client);
            }
        });
    }

    private void loginGoogle(){
        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickLoginGoogleListener != null) onClickLoginGoogleListener.onLoginGoogleClick();
            }
        });
    }

    public void setOnClickLoginTwitterListener(SignUpFragment.OnClickLoginTwitterListener onClickLoginTwitterListener){
        this.onClickLoginTwitterListener = onClickLoginTwitterListener;
    }

    public interface OnClickLoginTwitterListener{
        void onLoginTwitterClick(TwitterAuthClient twitterAuthClient);
    }

    public void setOnClickLoginGoogleListener(SignUpFragment.OnClickLoginGoogleListener onClickLoginGoogleListener){
        this.onClickLoginGoogleListener = onClickLoginGoogleListener;
    }

    public interface OnClickLoginGoogleListener{
        void onLoginGoogleClick();
    }
}
