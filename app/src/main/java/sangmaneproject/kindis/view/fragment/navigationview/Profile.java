package sangmaneproject.kindis.view.fragment.navigationview;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.util.RefreshToken;
import sangmaneproject.kindis.view.activity.SignInActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment implements View.OnClickListener {
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
        loading = new ProgressDialog(getActivity());
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage("Loading. Please wait...");

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
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                     public boolean onMenuItemClick(MenuItem item) {
                                                         if (item.getItemId()==R.id.logout){
                                                             sessionHelper.setPreferences(getContext(), "status", "0");
                                                             Intent intent = new Intent(getActivity(), SignInActivity.class);
                                                             startActivity(intent);
                                                         }
                                                         return true;
                                                     }
                                                 });
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
                    inputNama.setEnabled(false);
                    inputBirtday.setEnabled(false);
                    male.setEnabled(false);
                    female.setEnabled(false);

                    btnSave.setText("EDIT");
                    isEditButton = true;
                    //loading.show();
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
        String gender;
        if (male.isChecked()){
            gender = "male";
        }else {
            gender = "female";
        }

        Log.d("profileinfo", "Nama : "+inputNama.getText()+"\nTTL : "+inputBirtday.getText()+"\nGender : "+gender);

        new RefreshToken(getContext()).getToken(new SessionHelper().getPreferences(getContext(), "token"), new RefreshToken.OnFetchFinishedListener() {

            @Override
            public void onFetchFinished(Boolean status, String token) {
                Log.d("konssss","status : "+status+"\nToken : "+token);
            }
        });

        /*HashMap<String, String> param = new HashMap<>();
        param.put("user_id", sessionHelper.getPreferences(getContext(), "user_id"));
        param.put("fullname", inputNama.getText().toString());
        param.put("birth_date", inputBirtday.getText().toString());
        param.put("gender", gender);
        param.put("token", sessionHelper.getPreferences(getContext(), "token"));

        new VolleyHelper().post(ApiHelper.UPDATE_PROFILE, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                Log.d("update_profile", response);
            }
        });*/
    }
}