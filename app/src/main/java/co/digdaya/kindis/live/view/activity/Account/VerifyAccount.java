package co.digdaya.kindis.live.view.activity.Account;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.custom.ButtonSemiBold;
import co.digdaya.kindis.live.custom.EditTextRegular;
import co.digdaya.kindis.live.custom.PinEntryEditText;
import co.digdaya.kindis.live.custom.RadioButtonRegular;
import co.digdaya.kindis.live.custom.TextViewRegular;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.view.activity.PriceList;

import static co.digdaya.kindis.live.helper.Constanta.PREF_BIRTH_DATE;
import static co.digdaya.kindis.live.helper.Constanta.PREF_EMAIL;
import static co.digdaya.kindis.live.helper.Constanta.PREF_FULLNAME;
import static co.digdaya.kindis.live.helper.Constanta.PREF_GENDER;
import static co.digdaya.kindis.live.helper.Constanta.PREF_TOKEN;
import static co.digdaya.kindis.live.helper.Constanta.PREF_TOKEN_ACCESS;
import static co.digdaya.kindis.live.helper.Constanta.PREF_USERID;

public class VerifyAccount extends AppCompatActivity {

    @BindView(R.id.input_nama)
    EditTextRegular inputNama;
    @BindView(R.id.input_email)
    EditTextRegular inputEmail;
    @BindView(R.id.male)
    RadioButtonRegular male;
    @BindView(R.id.female)
    RadioButtonRegular female;
    @BindView(R.id.gender)
    RadioGroup gender;
    @BindView(R.id.input_birthday)
    EditTextRegular inputBirthday;
    @BindView(R.id.btn_send_code)
    ButtonSemiBold btnSendCode;
    @BindView(R.id.text_counter)
    TextViewRegular textCounter;
    @BindView(R.id.txt_pin_entry)
    PinEntryEditText txtPinEntry;
    @BindView(R.id.btn_verify)
    ButtonSemiBold btnVerify;
    @BindView(R.id.body_profile)
    LinearLayout bodyProfile;
    private SessionHelper sessionHelper;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        ButterKnife.bind(this);

        sessionHelper = new SessionHelper();

        loading = new ProgressDialog(this, R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);

        inputNama.setText(sessionHelper.getPreferences(this, PREF_FULLNAME));
        inputEmail.setText(sessionHelper.getPreferences(this, PREF_EMAIL));
        inputBirthday.setText(sessionHelper.getPreferences(this, "birth_date"));
        txtPinEntry.setTextColor(ContextCompat.getColor(this, R.color.yellow));
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
        String gender = sessionHelper.getPreferences(this, "gender");
        if (!gender.equals("male")) {
            male.setChecked(true);
        } else {
            female.setChecked(true);
        }

        inputBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    calenderDialog();
                }
            }
        });

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileInfo();
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode();
            }
        });

    }

    private void calenderDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        inputBirthday.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, 1990, 0, 1);
        datePickerDialog.show();
    }

    public void reverseTimer(int Seconds, final TextView tv) {

        new CountDownTimer(Seconds * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);

                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);
                tv.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                tv.setText("");
                btnSendCode.setEnabled(true);
                btnSendCode.setText("Resend Verification Code");
                btnSendCode.setBackground(ContextCompat.getDrawable(VerifyAccount.this,
                        R.drawable.button_rounded_yellow_10));
            }
        }.start();
    }

    private void saveProfileInfo() {
        loading.show();
        final String gender;
        if (male.isChecked()) {
            gender = "1";
        } else {
            gender = "2";
        }

        Log.d("profileinfo", "Nama : " + inputNama.getText() + "\nTTL : "
                + inputBirthday.getText() + "\nGender : " + gender);

        HashMap<String, String> param = new HashMap<>();
        param.put(PREF_USERID, sessionHelper.getPreferences(this, PREF_USERID));
        param.put(PREF_FULLNAME, inputNama.getText().toString() == null ? "" : inputNama.getText().toString());
        param.put(PREF_EMAIL, inputNama.getText().toString() == null ? "" : inputEmail.getText().toString());
        param.put(PREF_BIRTH_DATE, inputNama.getText().toString() == null ? "" : inputBirthday.getText().toString());
        param.put(PREF_GENDER, gender);
        param.put(PREF_TOKEN_ACCESS, sessionHelper.getPreferences(this, PREF_TOKEN_ACCESS));
        param.put(PREF_TOKEN, sessionHelper.getPreferences(this, PREF_TOKEN));

        new VolleyHelper().post(ApiHelper.UPDATE_PROFILE_PREMIUM, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                Log.d("update_profile", response);
                if (status) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
                            sessionHelper.setPreferences(VerifyAccount.this, PREF_FULLNAME,
                                    inputNama.getText().toString());
                            sessionHelper.setPreferences(VerifyAccount.this, PREF_EMAIL,
                                    inputEmail.getText().toString());
                            sessionHelper.setPreferences(VerifyAccount.this, PREF_BIRTH_DATE,
                                    inputBirthday.getText().toString());
                            sessionHelper.setPreferences(VerifyAccount.this, PREF_GENDER,
                                    gender);
                            reverseTimer(120, textCounter);
                            btnSendCode.setEnabled(false);
                            btnSendCode.setBackground(ContextCompat.getDrawable(VerifyAccount.this,
                                    R.drawable.button_rounded_gray));
                            txtPinEntry.setVisibility(View.VISIBLE);
                            btnVerify.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(VerifyAccount.this, object.getString("message"),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void verifyCode() {
        loading.show();

        HashMap<String, String> param = new HashMap<>();
        param.put("verification_code", txtPinEntry.getText().toString());

        new VolleyHelper().post(ApiHelper.CODE_VERIFY, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                Log.d("update_profile", response);
                if (status) {
                    try {
                        JSONObject object = new JSONObject(response);
                        Toast.makeText(VerifyAccount.this, object.getString("message"),
                                Toast.LENGTH_SHORT).show();
                        if (object.getBoolean("status")) {
                            Intent intent = new Intent(VerifyAccount.this, PriceList.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}
