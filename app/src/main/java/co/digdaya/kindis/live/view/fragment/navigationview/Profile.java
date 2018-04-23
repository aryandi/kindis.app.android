package co.digdaya.kindis.live.view.fragment.navigationview;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.custom.EditTextRegular;
import co.digdaya.kindis.live.custom.TextViewBold;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.CheckPermission;
import co.digdaya.kindis.live.helper.ImageFilePath;
import co.digdaya.kindis.live.helper.PlayerActionHelper;
import co.digdaya.kindis.live.helper.PlayerSessionHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.service.PlayerService;
import co.digdaya.kindis.live.view.activity.Account.ChangePassword;
import co.digdaya.kindis.live.view.activity.Account.LoginSocmedActivity;
import co.digdaya.kindis.live.view.activity.Account.TransactionHistory;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    DrawerLayout drawer;
    ImageButton btnDrawer, btnMenu, btnEditPhoto;
    ImageView photoProfile;
    Button btnSave, profileStatus;

    EditText inputNama;
    EditText inputBirthdate;

    RadioGroup radioGroup;
    RadioButton male;
    RadioButton female;

    InputMethodManager imm;
    SessionHelper sessionHelper;
    ProgressDialog loading;

    String loginType;
    GoogleApiClient mGoogleApiClient;
    CheckPermission checkPermission;

    boolean isEditButton = true;
    @BindView(R.id.btn_edit_name)
    ImageButton btnEditName;
    @BindView(R.id.btn_edit_email)
    ImageButton btnEditEmail;
    @BindView(R.id.btn_edit_birthdate)
    ImageButton btnEditBirthday;
    @BindView(R.id.btn_edit_gender)
    ImageButton btnEditGender;
    Unbinder unbinder;
    @BindView(R.id.input_email)
    EditTextRegular inputEmail;
    @BindView(R.id.connect_facebook)
    TextViewBold buttonConnectFB;
    @BindView(R.id.connect_twitter)
    TextViewBold buttonConnectTwitter;
    @BindView(R.id.input_facebook)
    EditTextRegular inputFacebook;
    @BindView(R.id.input_twitter)
    EditTextRegular inputTwitter;
    private boolean isEditName = true;
    private boolean isEditEmail = true;
    private boolean isEditBirthday = true;
    private boolean isEditGender = true;
    private boolean isEditFacebook = true;
    private boolean isEditTwitter = true;
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private TwitterAuthClient client;

    public Profile() {
    }

    public Profile(DrawerLayout drawer) {
        // Required empty public constructor
        this.drawer = drawer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDrawer = (ImageButton) view.findViewById(R.id.btn_drawer);
        btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
        btnEditPhoto = (ImageButton) view.findViewById(R.id.btn_edit_photo);
        photoProfile = (ImageView) view.findViewById(R.id.photo);
        btnSave = (Button) view.findViewById(R.id.btn_save);
        profileStatus = (Button) view.findViewById(R.id.profile_status);

        inputNama = (EditText) view.findViewById(R.id.input_nama);
        inputBirthdate = (EditText) view.findViewById(R.id.input_birthday);
        radioGroup = (RadioGroup) view.findViewById(R.id.gender);
        male = (RadioButton) view.findViewById(R.id.male);
        female = (RadioButton) view.findViewById(R.id.female);
        inputFacebook.setClickable(false);
        inputTwitter.setClickable(false);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        sessionHelper = new SessionHelper();
        checkPermission = new CheckPermission(getActivity());
        loading = new ProgressDialog(getActivity(), R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);

        loginType = sessionHelper.getPreferences(getActivity(), "login_type");

        if (sessionHelper.getPreferences(getActivity(), "profile_picture").length() > 10) {
            Glide.with(getContext())
                    .load(sessionHelper.getPreferences(getActivity(), "profile_picture"))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(photoProfile);
        }

        if (sessionHelper.getPreferences(getActivity(), "is_premium").equals("1")) {
            profileStatus.setText("PREMIUM");
            profileStatus.setBackground(getActivity().getDrawable(R.drawable.button_rounded_orange));
        }

        if (loginType.equals("3")) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity() /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            System.out.println("googlelogin " + connectionResult.getErrorMessage());
                        }
                    } /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        } else if (loginType.equals("2")) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_key), getString(R.string.twitter_secret));
            Fabric.with(getActivity(), new Twitter(authConfig));
        }

        inputNama.setText(sessionHelper.getPreferences(getActivity(), "fullname"));
        inputEmail.setText((sessionHelper.getPreferences(getActivity(), "email")));
        inputBirthdate.setText(sessionHelper.getPreferences(getActivity(), "birth_date"));
        String social_name_facebook = sessionHelper.getPreferences(getActivity(), "social_name_facebook");
        if (!TextUtils.isEmpty(social_name_facebook) && social_name_facebook != null && !social_name_facebook.equals("null")) {
            inputFacebook.setText(social_name_facebook);
            disableFBButton();
        }
        String social_name_twitter = sessionHelper.getPreferences(getActivity(), "social_name_twitter");
        if (!TextUtils.isEmpty(social_name_twitter) && social_name_twitter != null && !social_name_twitter.equals("null")) {
            inputTwitter.setText(social_name_twitter);
            disableTwitterButton();
        }
        if (sessionHelper.getPreferences(getActivity(), "gender").equals("male")) {
            male.setChecked(true);
            male.setTextColor(Color.parseColor("#000000"));
            female.setTextColor(Color.parseColor("#ffffff"));
        } else {
            female.setChecked(true);
            male.setTextColor(Color.parseColor("#ffffff"));
            female.setTextColor(Color.parseColor("#000000"));
        }

        btnDrawer.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnEditPhoto.setOnClickListener(this);
        btnEditName.setOnClickListener(this);
        btnEditEmail.setOnClickListener(this);
        btnEditBirthday.setOnClickListener(this);
        btnEditGender.setOnClickListener(this);
        buttonConnectFB.setOnClickListener(this);
        buttonConnectTwitter.setOnClickListener(this);

        connectFB();
        connectTwitter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_drawer:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_menu:
                PopupMenu popup = new PopupMenu(getActivity(), btnMenu);
                popup.getMenuInflater().inflate(R.menu.profile, popup.getMenu());
                popup.setOnMenuItemClickListener(this);
                popup.show();
                break;
            case R.id.btn_save:
                if (isEditButton) {
                    inputNama.setEnabled(true);
                    inputBirthdate.setEnabled(true);
                    male.setEnabled(true);
                    female.setEnabled(true);
                    inputNama.setSelection(inputNama.getText().length());
                    imm.showSoftInput(inputNama, InputMethodManager.SHOW_IMPLICIT);

                    btnSave.setText("SAVE");
                    isEditButton = false;
                } else {
                    isEditButton = true;
                    inputNama.setEnabled(false);
                    inputBirthdate.setEnabled(false);
                    male.setEnabled(false);
                    female.setEnabled(false);

                    btnSave.setText("EDIT");
                    saveProfileInfo();
                }
                break;
            case R.id.btn_edit_photo:
                if (checkPermission.checkPermission()) {
                    startDialogPhoto(getActivity());
                } else {
                    checkPermission.showPermission(2);
                }
                break;
            case R.id.btn_edit_name:
                if (isEditName) {
                    inputNama.setEnabled(true);
                    inputNama.setSelection(inputNama.getText().length());
                    imm.showSoftInput(inputNama, InputMethodManager.SHOW_IMPLICIT);
                    inputNama.requestFocus();
                    isEditName = false;
                    btnEditName.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_checklist));
                } else {
                    inputNama.setEnabled(false);
                    saveProfileInfo();
                    isEditName = true;
                    btnEditName.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_edit));
                }
                break;
            case R.id.btn_edit_email:
                if (isEditEmail) {
                    inputEmail.setEnabled(true);
                    inputEmail.setSelection(inputEmail.getText().length());
                    imm.showSoftInput(inputEmail, InputMethodManager.SHOW_IMPLICIT);
                    inputEmail.requestFocus();
                    isEditEmail = false;
                    btnEditEmail.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_checklist));
                } else {
                    inputEmail.setEnabled(false);
                    saveEmailInfo();
                    isEditEmail = true;
                    btnEditEmail.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_edit));
                }
                break;
            case R.id.btn_edit_birthdate:
                if (isEditBirthday) {
                    inputBirthdate.setEnabled(true);
                    inputBirthdate.setSelection(inputBirthdate.getText().length());
                    inputBirthdate.requestFocus();
                    imm.showSoftInput(inputBirthdate, InputMethodManager.SHOW_IMPLICIT);
                    isEditBirthday = false;
                    btnEditBirthday.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_checklist));
                } else {
                    inputBirthdate.setEnabled(false);
                    saveProfileInfo();
                    isEditBirthday = true;
                    btnEditBirthday.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_edit));
                }
                break;
            case R.id.btn_edit_gender:
                if (isEditGender) {
                    male.setEnabled(true);
                    female.setEnabled(true);
                    isEditGender = false;
                    btnEditGender.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_checklist));
                } else {
                    male.setEnabled(false);
                    female.setEnabled(false);
                    saveProfileInfo();
                    isEditGender = true;
                    btnEditGender.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_edit));
                }
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (male.isChecked()) {
                    male.setTextColor(Color.parseColor("#000000"));
                    female.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    male.setTextColor(Color.parseColor("#ffffff"));
                    female.setTextColor(Color.parseColor("#000000"));
                }
            }
        });

        inputBirthdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    calenderDialog();
                }
            }
        });
    }

    private void calenderDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        inputBirthdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, 1990, 0, 1);
        datePickerDialog.show();
    }

    private void saveEmailInfo() {
        loading.show();

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", sessionHelper.getPreferences(getActivity(), "user_id"));
        param.put("new_email", inputEmail.getText().toString());
        param.put("old_email", sessionHelper.getPreferences(getActivity(), "email"));
        param.put("token_access", sessionHelper.getPreferences(getActivity(), "token_access"));

        new VolleyHelper().post(ApiHelper.CHANGE_EMAIL, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                Log.d("update_profile", response);
                if (status) {
                    try {
                        JSONObject object = new JSONObject(response);
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        if (object.getBoolean("status")) {
                            sessionHelper.setPreferences(getActivity(), "email", inputEmail.getText().toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void saveProfileInfo() {
        loading.show();
        final String gender;
        if (male.isChecked()) {
            gender = "male";
        } else {
            gender = "female";
        }

        Log.d("profileinfo", "Nama : " + inputNama.getText() + "\nTTL : " + inputBirthdate.getText() + "\nGender : " + gender);

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", sessionHelper.getPreferences(getActivity(), "user_id"));
        param.put("fullname", inputNama.getText().toString());
        param.put("birth_date", inputBirthdate.getText().toString());
        param.put("gender", gender);
        param.put("token_access", sessionHelper.getPreferences(getActivity(), "token_access"));

        new VolleyHelper().post(ApiHelper.UPDATE_PROFILE, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                Log.d("update_profile", response);
                if (status) {
                    try {
                        JSONObject object = new JSONObject(response);
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        if (object.getBoolean("status")) {
                            sessionHelper.setPreferences(getActivity(), "fullname", inputNama.getText().toString());
                            sessionHelper.setPreferences(getActivity(), "birth_date", inputBirthdate.getText().toString());
                            sessionHelper.setPreferences(getActivity(), "gender", gender.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                HashMap<String, String> param = new HashMap<>();
                param.put("uid", sessionHelper.getPreferences(getActivity(), "user_id"));
                param.put("token_access", sessionHelper.getPreferences(getActivity(), "token_access"));
                param.put("token_refresh", sessionHelper.getPreferences(getActivity(), "token_refresh"));

                new VolleyHelper().post(ApiHelper.LOGOUT, param, new VolleyHelper.HttpListener<String>() {
                    @Override
                    public void onReceive(boolean status, String message, String response) {
                        System.out.println("logoutresponse: " + response);
                        String email = sessionHelper.getPreferences(getActivity(), "email");
                        sessionHelper.clearSession(getActivity());
                        sessionHelper.setPreferences(getActivity(), "email", email);
                        new PlayerSessionHelper().clearSession(getActivity());
                        Intent in = new Intent(getActivity(), PlayerService.class);
                        in.setAction(PlayerActionHelper.ACTION_LOG_OUT);
                        getActivity().startService(in);
                        switch (loginType) {
                            case "1":
                                LoginManager.getInstance().logOut();
                                break;
                            case "2":
                                Twitter.getSessionManager().clearActiveSession();
                                Twitter.logOut();
                                break;
                            case "3":
                                if (mGoogleApiClient.isConnected()) {
                                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                            new ResultCallback<Status>() {
                                                @Override
                                                public void onResult(Status status) {
                                                }
                                            });
                                }
                                break;
                        }
                        Intent intent = new Intent(getActivity(), LoginSocmedActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.password:
                Intent intent2 = new Intent(getActivity(), ChangePassword.class);
                startActivity(intent2);
                break;
            case R.id.history:
                Intent intent3 = new Intent(getActivity(), TransactionHistory.class);
                startActivity(intent3);
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    public void startDialogPhoto(Activity activity) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(activity);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        if (isAdded()) {
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                        }
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (isAdded()) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 0);
                        }
                    }
                });
        myAlertDialog.show();
    }

    private void connectFB() {
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
                                    String type_social = "1";
                                    String app_id = object.getString("id");
                                    String social_name = object.optString("name");
                                    updateSocialMedia(type_social, app_id, social_name);
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
                Log.d("FacebookLogin", "error" + error.getMessage());
                System.out.println("FacebookLogin" + error.getMessage());
            }
        });

        buttonConnectFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManager.logInWithReadPermissions(getActivity(), Arrays.asList("email", "public_profile"));
            }
        });
    }

    private void connectTwitter() {
        client = new TwitterAuthClient();
        buttonConnectTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.authorize(getActivity(), new Callback<TwitterSession>() {
                    @Override
                    public void success(final Result<TwitterSession> twitterSessionResult) {
                        TwitterSession twitterSession = twitterSessionResult.data;

                        Call<User> call = Twitter.getApiClient(twitterSession).getAccountService().verifyCredentials(true, false);
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void success(Result<User> result) {
                                System.out.println("logintwitter" + result.data.email);

                                String type_social = "2";
                                String app_id = String.valueOf(twitterSessionResult.data.getUserId());
                                String social_name = "";
                                social_name = result.data.screenName;

                                updateSocialMedia(type_social, app_id, social_name);
                            }

                            @Override
                            public void failure(TwitterException e) {
                                Log.e("twitter login", "failure: " + e.getMessage());
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

    private void updateSocialMedia(final String type_social, String app_id, final String social_name) {

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", sessionHelper.getPreferences(getActivity(), "user_id"));
        param.put("social_type", type_social);
        param.put("social_id", app_id);
        param.put("social_name", social_name);
        param.put("token_access", sessionHelper.getPreferences(getActivity(), "token_access"));

        new VolleyHelper().post(ApiHelper.UPDATE_SOCIAL_MEDIA, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();

                Log.d("update_profile", response);
                if (status) {
                    try {
                        JSONObject object = new JSONObject(response);

                        if (object.getBoolean("status")) {
                            switch (type_social) {
                                case "1":
                                    disableFBButton();
                                    inputFacebook.setText(social_name);
                                    sessionHelper.setPreferences(getActivity(),
                                            "social_name_facebook", inputNama.getText().toString());
                                    break;
                                case "2":
                                    disableTwitterButton();
                                    inputTwitter.setText(social_name);
                                    sessionHelper.setPreferences(getActivity(),
                                            "social_name_twitter", inputNama.getText().toString());
                                    break;
                            }
                        }
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void disableTwitterButton() {
        buttonConnectTwitter.setText("Connected");
        buttonConnectTwitter.setTextColor(ContextCompat.getColor(getActivity(), R.color.tw__composer_blue));
        buttonConnectTwitter.setClickable(false);
    }

    private void disableFBButton() {
        buttonConnectFB.setText("Connected");
        buttonConnectFB.setTextColor(ContextCompat.getColor(getActivity(), R.color.com_facebook_blue));
        buttonConnectFB.setClickable(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        client.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult: " + requestCode + " " + resultCode);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                photoProfile.setImageBitmap(photo);
                Uri tempUri = getImageUri(getActivity(), photo);
                uploadImage(getRealPathFromURI(tempUri));
            }
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    photoProfile.setImageBitmap(imageBitmap);
                    uploadImage(ImageFilePath.getPath(getActivity(), uri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadImage(final String path) {

        final String id = sessionHelper.getPreferences(getActivity(), "user_id");
        final String token = sessionHelper.getPreferences(getActivity(), "token_access");

        new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... strings) {
                float totalSize = 0;
                String responseString;
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(ApiHelper.UPDATE_AVATAR);

                try {
                    MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                    File sourceFile = new File(path);
                    FileBody fb = new FileBody(sourceFile, sourceFile.getName(), "image/jpeg", "UTF-8");
                    entity.addPart("file", fb);

                    entity.addPart("user_id", new StringBody(id));
                    entity.addPart("token_access", new StringBody(token));

                    HttpParams httpparams = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpparams, 6000000);
                    HttpConnectionParams.setSoTimeout(httpparams, 6000000);
                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);

                    // Making server call
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        // Server response
                        responseString = EntityUtils.toString(r_entity);
                        System.out.print("RESPONSEedit : " + responseString);

                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }


                } catch (ClientProtocolException e) {
                    responseString = "error";
                } catch (IOException e) {
                    responseString = "error";
                }

                return responseString;
            }

            @Override
            protected void onPostExecute(String s) {
                if (getActivity() != null)
                    try {
                        JSONObject result = new JSONObject(s);
                        if (result.getBoolean("status")) {
                            JSONObject urlPhoto = result.getJSONObject("result");
                            String newPath = urlPhoto.getString("avatar").replaceAll("(?<!https:)//", "/");
                            sessionHelper.setPreferences(getActivity(), "profile_picture", newPath);
                            Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Upload Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                super.onPostExecute(s);
            }

        }.execute();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, sessionHelper.getPreferences(getActivity(), "user_id"), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}