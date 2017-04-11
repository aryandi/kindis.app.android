package co.digdaya.kindis.view.fragment.navigationview;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.PopupMenu;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.Twitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.view.activity.Account.ChangeEmail;
import co.digdaya.kindis.view.activity.Account.ChangePassword;
import co.digdaya.kindis.view.activity.Account.SignInActivity;
import co.digdaya.kindis.view.activity.Account.TransactionHistory;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    DrawerLayout drawer;
    ImageButton btnDrawer;
    ImageButton btnMenu;
    Button btnSave;

    EditText inputNama;
    EditText inputBirtday;

    RadioGroup radioGroup;
    RadioButton male;
    RadioButton female;

    InputMethodManager imm;
    SessionHelper sessionHelper;
    ProgressDialog loading;

    String loginType;
    GoogleApiClient mGoogleApiClient;

    boolean isEditButton = true;

    public Profile(DrawerLayout drawer) {
        // Required empty public constructor
        this.drawer = drawer;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDrawer = (ImageButton) view.findViewById(R.id.btn_drawer);
        btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
        btnSave = (Button) view.findViewById(R.id.btn_save);

        inputNama = (EditText) view.findViewById(R.id.input_nama);
        inputBirtday = (EditText) view.findViewById(R.id.input_birthday);
        radioGroup = (RadioGroup) view.findViewById(R.id.gender);
        male = (RadioButton) view.findViewById(R.id.male);
        female = (RadioButton) view.findViewById(R.id.female);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        sessionHelper = new SessionHelper();
        loading = new ProgressDialog(getActivity(), R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);

        loginType = sessionHelper.getPreferences(getContext(), "login_type");

        if (loginType.equals("3")){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity() /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            System.out.println("googlelogin "+connectionResult.getErrorMessage());
                        }
                    } /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }

        inputNama.setText(sessionHelper.getPreferences(getContext(), "fullname"));
        inputBirtday.setText(sessionHelper.getPreferences(getContext(), "birth_date"));
        if (sessionHelper.getPreferences(getContext(), "gender").equals("male")){
            male.setChecked(true);
            male.setTextColor(Color.parseColor("#000000"));
            female.setTextColor(Color.parseColor("#ffffff"));
        }else {
            female.setChecked(true);
            male.setTextColor(Color.parseColor("#ffffff"));
            female.setTextColor(Color.parseColor("#000000"));
        }

        btnDrawer.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
                if (isEditButton){
                    inputNama.setEnabled(true);
                    inputBirtday.setEnabled(true);
                    male.setEnabled(true);
                    female.setEnabled(true);
                    inputNama.setSelection(inputNama.getText().length());
                    imm.showSoftInput(inputNama, InputMethodManager.SHOW_IMPLICIT);

                    btnSave.setText("SAVE");
                    isEditButton = false;
                }else {
                    isEditButton = true;
                    inputNama.setEnabled(false);
                    inputBirtday.setEnabled(false);
                    male.setEnabled(false);
                    female.setEnabled(false);

                    btnSave.setText("EDIT");
                    saveProfileInfo();
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
                if (male.isChecked()){
                    male.setTextColor(Color.parseColor("#000000"));
                    female.setTextColor(Color.parseColor("#ffffff"));
                }else {
                    male.setTextColor(Color.parseColor("#ffffff"));
                    female.setTextColor(Color.parseColor("#000000"));
                }
            }
        });

        inputBirtday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    calenderDialog();
                }
            }
        });
    }



    private void calenderDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        inputBirtday.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, 1990, 0, 1);
        datePickerDialog.show();
    }

    private void saveProfileInfo(){
        loading.show();
        final String gender;
        if (male.isChecked()){
            gender = "male";
        }else {
            gender = "female";
        }

        Log.d("profileinfo", "Nama : "+inputNama.getText()+"\nTTL : "+inputBirtday.getText()+"\nGender : "+gender);

        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", sessionHelper.getPreferences(getContext(), "user_id"));
        param.put("fullname", inputNama.getText().toString());
        param.put("birth_date", inputBirtday.getText().toString());
        param.put("gender", gender);
        param.put("token_access", sessionHelper.getPreferences(getContext(), "token_access"));

        new VolleyHelper().post(ApiHelper.UPDATE_PROFILE, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                Log.d("update_profile", response);
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        if (object.getBoolean("status")){
                            sessionHelper.setPreferences(getContext(), "fullname", inputNama.getText().toString());
                            sessionHelper.setPreferences(getContext(), "birth_date", inputBirtday.getText().toString());
                            sessionHelper.setPreferences(getContext(), "gender", gender.toString());
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
        switch (item.getItemId()){
            case R.id.logout:
                if (loginType.equals("1")){
                    LoginManager.getInstance().logOut();
                }else if (loginType.equals("2")){
                    Twitter.getSessionManager().clearActiveSession();
                    Twitter.logOut();
                }else if (loginType.equals("3")){
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    System.out.println("logoutsosmed : "+status);
                                }
                            });
                }
                sessionHelper.setPreferences(getContext(), "status", "0");
                sessionHelper.setPreferences(getContext(), "profile_picture", null);
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
                new LogOut().execute();
                break;
            case R.id.email:
                Intent intent1 = new Intent(getActivity(), ChangeEmail.class);
                startActivity(intent1);
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
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    public class LogOut extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> param = new HashMap<>();
            param.put("uid", sessionHelper.getPreferences(getContext(), "user_id"));
            param.put("token_access", sessionHelper.getPreferences(getContext(), "token_access"));
            param.put("token_refresh", sessionHelper.getPreferences(getContext(), "token_refresh"));

            new VolleyHelper().post(ApiHelper.LOGOUT, param, new VolleyHelper.HttpListener<String>() {
                @Override
                public void onReceive(boolean status, String message, String response) {
                    System.out.println("logoutresponse: "+response);
                }
            });
            return null;
        }
    }
}