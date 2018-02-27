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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

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
import java.util.HashMap;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.CheckPermission;
import co.digdaya.kindis.live.helper.ImageFilePath;
import co.digdaya.kindis.live.helper.PlayerActionHelper;
import co.digdaya.kindis.live.helper.PlayerSessionHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.service.PlayerService;
import co.digdaya.kindis.live.view.activity.Account.ChangeEmail;
import co.digdaya.kindis.live.view.activity.Account.ChangePassword;
import co.digdaya.kindis.live.view.activity.Account.SignInActivity;
import co.digdaya.kindis.live.view.activity.Account.TransactionHistory;
import io.fabric.sdk.android.Fabric;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    DrawerLayout drawer;
    ImageButton btnDrawer, btnMenu, btnEditPhoto;
    ImageView photoProfile;
    Button btnSave, profileStatus;

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
    CheckPermission checkPermission;

    boolean isEditButton = true;

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
        return inflater.inflate(R.layout.fragment_profile, container, false);
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
        inputBirtday = (EditText) view.findViewById(R.id.input_birthday);
        radioGroup = (RadioGroup) view.findViewById(R.id.gender);
        male = (RadioButton) view.findViewById(R.id.male);
        female = (RadioButton) view.findViewById(R.id.female);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        sessionHelper = new SessionHelper();
        checkPermission = new CheckPermission(getActivity());
        loading = new ProgressDialog(getActivity(), R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);

        loginType = sessionHelper.getPreferences(getActivity(), "login_type");

        if (sessionHelper.getPreferences(getActivity(), "profile_picture").length()>10){
            Glide.with(getContext())
                    .load(sessionHelper.getPreferences(getActivity(), "profile_picture"))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(photoProfile);
        }

        if (sessionHelper.getPreferences(getActivity(), "is_premium").equals("1")){
            profileStatus.setText("PREMIUM");
            profileStatus.setBackground(getActivity().getDrawable(R.drawable.button_rounded_orange));
        }

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
        }else if (loginType.equals("2")){
            TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_key), getString(R.string.twitter_secret));
            Fabric.with(getActivity(), new Twitter(authConfig));
        }

        inputNama.setText(sessionHelper.getPreferences(getActivity(), "fullname"));
        inputBirtday.setText(sessionHelper.getPreferences(getActivity(), "birth_date"));
        if (sessionHelper.getPreferences(getActivity(), "gender").equals("male")){
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
        btnEditPhoto.setOnClickListener(this);
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
            case R.id.btn_edit_photo:
                if (checkPermission.checkPermission()){
                    startDialogPhoto(getActivity());
                }else {
                    checkPermission.showPermission(2);
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
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
        param.put("user_id", sessionHelper.getPreferences(getActivity(), "user_id"));
        param.put("fullname", inputNama.getText().toString());
        param.put("birth_date", inputBirtday.getText().toString());
        param.put("gender", gender);
        param.put("token_access", sessionHelper.getPreferences(getActivity(), "token_access"));

        new VolleyHelper().post(ApiHelper.UPDATE_PROFILE, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                Log.d("update_profile", response);
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        if (object.getBoolean("status")){
                            sessionHelper.setPreferences(getActivity(), "fullname", inputNama.getText().toString());
                            sessionHelper.setPreferences(getActivity(), "birth_date", inputBirtday.getText().toString());
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
        switch (item.getItemId()){
            case R.id.logout:
                sessionHelper.clearSession(getContext());
                new PlayerSessionHelper().clearSession(getContext());
                Intent in = new Intent(getActivity(), PlayerService.class);
                in.setAction(PlayerActionHelper.ACTION_LOG_OUT);
                getActivity().startService(in);
                if (loginType.equals("1")){
                    LoginManager.getInstance().logOut();
                }else if (loginType.equals("2")){
                    Twitter.getSessionManager().clearActiveSession();
                    Twitter.logOut();
                }else if (loginType.equals("3")){
                    if(mGoogleApiClient.isConnected()){
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                    }
                                });
                    }
                }
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
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient!=null){
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
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (isAdded()){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 0);
                        }
                    }
                });
        myAlertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("onActivityResult: "+requestCode+" "+resultCode);
        if (requestCode==0){
            if (resultCode == RESULT_OK){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                photoProfile.setImageBitmap(photo);
                Uri tempUri = getImageUri(getActivity(), photo);
                uploadImage(getRealPathFromURI(tempUri));
            }
        }else if (requestCode==1){
            if (resultCode == RESULT_OK){
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

    private void uploadImage(final String path){

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
                    FileBody fb =  new FileBody(sourceFile, sourceFile.getName(), "image/jpeg", "UTF-8");
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
                try {
                    JSONObject result = new JSONObject(s);
                    if (result.getBoolean("status")){
                        JSONObject urlPhoto = result.getJSONObject("result");
                        String newPath = urlPhoto.getString("avatar").replaceAll("(?<!https:)//", "/");
                        sessionHelper.setPreferences(getActivity(), "profile_picture", newPath);
                        Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getActivity(), "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(s);
            }

        }.execute();
    }

    public class LogOut extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> param = new HashMap<>();
            param.put("uid", sessionHelper.getPreferences(getActivity(), "user_id"));
            param.put("token_access", sessionHelper.getPreferences(getActivity(), "token_access"));
            param.put("token_refresh", sessionHelper.getPreferences(getActivity(), "token_refresh"));

            new VolleyHelper().post(ApiHelper.LOGOUT, param, new VolleyHelper.HttpListener<String>() {
                @Override
                public void onReceive(boolean status, String message, String response) {
                    System.out.println("logoutresponse: "+response);
                }
            });
            return null;
        }
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
}