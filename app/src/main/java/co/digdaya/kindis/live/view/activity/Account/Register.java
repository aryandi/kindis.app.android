package co.digdaya.kindis.live.view.activity.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.view.dialog.DialogLoading;
import co.digdaya.kindis.live.util.BackgroundProses.ProfileInfo;
import co.digdaya.kindis.live.view.activity.Splash.Bismillah;

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText inputFullname;
    EditText inputEmail;
   /* EditText inputPhone;
    EditText inputBirtday;
    RadioGroup radioGroup;
    RadioButton male;
    RadioButton female;*/
    Button signUp;
    ImageButton btnBack;

    String fullname;
    String gender;
    String birth_date;
    String type_social;
    String app_id;
    String email;
    String phone;

    VolleyHelper volleyHelper;
    DialogLoading dialogLoading;
    private SessionHelper sessionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sessionHelper = new SessionHelper();

        inputFullname = (EditText) findViewById(R.id.input_nama);
        inputEmail = (EditText) findViewById(R.id.input_email);
      /*  inputPhone = (EditText) findViewById(R.id.input_phone);
        inputBirtday = (EditText) findViewById(R.id.input_birthday);
        radioGroup = (RadioGroup) findViewById(R.id.gender);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);*/
        signUp = (Button) findViewById(R.id.btn_sign_up);
        btnBack = (ImageButton) findViewById(R.id.btn_back);

        fullname = getIntent().getStringExtra("fullname");
        gender = getIntent().getStringExtra("gender");
        birth_date = getIntent().getStringExtra("birth_date");
        type_social = getIntent().getStringExtra("type_social");
        app_id = getIntent().getStringExtra("app_id");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");

        volleyHelper = new VolleyHelper();
        dialogLoading = new DialogLoading(this);

        String eml = ""+email;
        if (eml.length()>10){
            inputEmail.setEnabled(false);
        }

        inputFullname.setText(fullname);
        inputEmail.setText(email);
        /*inputBirtday.setText(birth_date);

        inputBirtday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    calenderDialog();
                }
            }
        });

        if (gender.equals("male")){
            male.setChecked(true);
            male.setTextColor(Color.parseColor("#000000"));
            female.setTextColor(Color.parseColor("#ffffff"));
        }else {
            female.setChecked(true);
            male.setTextColor(Color.parseColor("#ffffff"));
            female.setTextColor(Color.parseColor("#000000"));
        }

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
*/
        signUp.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_sign_up){
            dialogLoading.showLoading();
            registerSocial();
        }else if (view.getId() == R.id.btn_back){
            finish();
        }
    }

    /*private void calenderDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        inputBirtday.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, 1990, 0, 1);
        datePickerDialog.show();
    }*/

    private void registerSocial(){
       /* if (male.isChecked()){
            gender = "male";
        }else {
            gender = "female";
        }*/

        HashMap<String, String> param = new HashMap<>();
        param.put("fullname", inputFullname.getText().toString());
//        param.put("gender", gender);
//        param.put("birth_date", inputBirtday.getText().toString());
        param.put("type_social", type_social);
        param.put("app_id", app_id);
        param.put("email", inputEmail.getText().toString());
//        param.put("phone", inputPhone.getText().toString());

        volleyHelper.post(ApiHelper.REGISTER_SOCIAL, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                dialogLoading.dismisLoading();
                if (status){
                    Log.d("response", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");
                            sessionHelper.setPreferences(getApplicationContext(), "token", result.getString("token"));
                            sessionHelper.setPreferences(getApplicationContext(), "token_access", result.getString("token_access"));
                            sessionHelper.setPreferences(getApplicationContext(), "token_refresh", result.getString("token_refresh"));
                            sessionHelper.setPreferences(getApplicationContext(), "expires_in", String.valueOf(result.optInt("expires_in")));
                            new ProfileInfo(getApplicationContext()).execute(result.getString("user_id"));
                            Intent intent = new Intent(Register.this, Bismillah.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
