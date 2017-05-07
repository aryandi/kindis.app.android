package co.digdaya.kindis.view.activity.Account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.view.dialog.DialogLoading;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, TextWatcher {
    SessionHelper sessionHelper;
    InputMethodManager imm;
    DialogLoading loading;

    ImageButton btnBack, btnMenu;
    EditText inputCurrentPassword, inputNewPassword, inputRetypePassword;
    Button btnSave, profileStatus;
    LinearLayout contCurrent, contNew, contRetype;
    TextView errorCurrent, errorNew, errorRetype;
    ImageView photoProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        sessionHelper = new SessionHelper();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        loading = new DialogLoading(this);

        btnBack = (ImageButton) findViewById(R.id.btn_drawer);
        btnMenu = (ImageButton) findViewById(R.id.btn_menu);
        btnSave = (Button) findViewById(R.id.btn_save);
        inputCurrentPassword = (EditText) findViewById(R.id.input_current_password);
        inputNewPassword = (EditText) findViewById(R.id.input_new_password);
        inputRetypePassword = (EditText) findViewById(R.id.input_retype_password);
        contCurrent = (LinearLayout) findViewById(R.id.cont_error_current);
        contNew = (LinearLayout) findViewById(R.id.cont_error_new);
        contRetype = (LinearLayout) findViewById(R.id.cont_error_retype);
        errorCurrent = (TextView) findViewById(R.id.error_current);
        errorNew = (TextView) findViewById(R.id.error_new);
        errorRetype = (TextView) findViewById(R.id.error_retype);
        photoProfile = (ImageView) findViewById(R.id.photo);
        profileStatus = (Button) findViewById(R.id.profile_status);

        if (sessionHelper.getPreferences(getApplicationContext(), "profile_picture").length()>10){
            Glide.with(getApplicationContext())
                    .load(sessionHelper.getPreferences(getApplicationContext(), "profile_picture"))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(photoProfile);
        }

        if (sessionHelper.getPreferences(getApplicationContext(), "is_premium").equals("1")){
            profileStatus.setText("PREMIUM");
            profileStatus.setBackground(getApplicationContext().getDrawable(R.drawable.button_rounded_orange));
        }

        btnBack.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        inputCurrentPassword.addTextChangedListener(this);
        inputRetypePassword.addTextChangedListener(this);
        inputNewPassword.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        switch (v.getId()){
            case R.id.btn_drawer:
                finish();
                break;
            case R.id.btn_menu:
                PopupMenu popup = new PopupMenu(this, btnMenu);
                popup.getMenuInflater().inflate(R.menu.profile, popup.getMenu());
                popup.setOnMenuItemClickListener(this);
                popup.show();
                break;
            case R.id.btn_save:
                if (formValidation()) {
                    loading.showLoading();
                    changePassword();
                }
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                sessionHelper.setPreferences(getApplicationContext(), "status", "0");
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                break;
            case R.id.email:
                Intent intent1 = new Intent(this, ChangeEmail.class);
                startActivity(intent1);
                break;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        inputCurrentPassword.setBackground(getResources().getDrawable(R.drawable.edittext_normal, null));
        inputRetypePassword.setBackground(getResources().getDrawable(R.drawable.edittext_normal, null));
        inputNewPassword.setBackground(getResources().getDrawable(R.drawable.edittext_normal, null));

        contCurrent.setVisibility(View.GONE);
        contNew.setVisibility(View.GONE);
        contRetype.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private boolean formValidation(){
        if (inputCurrentPassword.getText().length()<1 | inputNewPassword.getText().length()<1 | inputRetypePassword.getText().length()<1){
            if (inputCurrentPassword.getText().length()<1){
                inputCurrentPassword.setBackground(getResources().getDrawable(R.drawable.edittext_error, null));
                contCurrent.setVisibility(View.VISIBLE);
            }else if (inputNewPassword.getText().length()<1){
                inputNewPassword.setBackground(getResources().getDrawable(R.drawable.edittext_error, null));
                contNew.setVisibility(View.VISIBLE);
            }else if (inputRetypePassword.getText().length()<1){
                inputRetypePassword.setBackground(getResources().getDrawable(R.drawable.edittext_error, null));
                contRetype.setVisibility(View.VISIBLE);
            }
            return false;
        }else if (inputNewPassword.getText().toString().equals(inputRetypePassword.getText().toString())){
            return true;
        }else {
            inputNewPassword.setBackground(getResources().getDrawable(R.drawable.edittext_error, null));
            contNew.setVisibility(View.VISIBLE);
            errorNew.setText("Password not match");
            inputRetypePassword.setBackground(getResources().getDrawable(R.drawable.edittext_error, null));
            return false;
        }
    }

    private void changePassword(){
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", sessionHelper.getPreferences(getApplicationContext(), "user_id"));
        param.put("current_password", inputCurrentPassword.getText().toString());
        param.put("new_password", inputNewPassword.getText().toString());
        param.put("retype_password", inputRetypePassword.getText().toString());

        new VolleyHelper().post(ApiHelper.CHANGE_PASSWORD, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismisLoading();
                if (status){
                    Log.d("changeemailll", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                            sessionHelper.setPreferences(getApplicationContext(), "status", "0");
                            Intent intent = new Intent(ChangePassword.this, SignInActivity.class);
                            startActivity(intent);
                        }else {
                            inputCurrentPassword.setBackground(getResources().getDrawable(R.drawable.edittext_error, null));
                            contCurrent.setVisibility(View.VISIBLE);
                            errorCurrent.setText(object.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
