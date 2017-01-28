package sangmaneproject.kindis.view.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.VolleyHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements View.OnFocusChangeListener {
    AppBarLayout appBarLayout;
    EditText fullname;
    EditText email;
    EditText password;
    EditText retypePassword;
    EditText birtday;
    RadioGroup radioGroup;
    RadioButton male;
    RadioButton female;
    Button signUp;
    VolleyHelper volleyHelper;
    ProgressDialog loading;
    TabLayout tabLayout;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public SignUpFragment(AppBarLayout appBarLayout){
        this.appBarLayout = appBarLayout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        volleyHelper = new VolleyHelper();
        loading = new ProgressDialog(getActivity());
        tabLayout = (TabLayout) getActivity().findViewById(R.id.htab_tabs);

        fullname = (EditText) view.findViewById(R.id.input_nama);
        email = (EditText) view.findViewById(R.id.input_email);
        password = (EditText) view.findViewById(R.id.input_password);
        retypePassword = (EditText) view.findViewById(R.id.input_retype);
        birtday = (EditText) view.findViewById(R.id.input_birthday);
        radioGroup = (RadioGroup) view.findViewById(R.id.gender);
        male = (RadioButton) view.findViewById(R.id.male);
        female = (RadioButton) view.findViewById(R.id.female);
        signUp = (Button) view.findViewById(R.id.btn_sign_up);

        fullname.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        retypePassword.setOnFocusChangeListener(this);

        birtday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formValidation()){
                    if (password.getText().length()<8){
                        Toast.makeText(getContext(), "Password must be 8 characters", Toast.LENGTH_SHORT).show();
                    }else {
                        register();
                    }
                }else {
                    Toast.makeText(getContext(), "Please fill the form", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage("Loading. Please wait...");
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        appBarLayout.setExpanded(false, true);
    }

    private void calenderDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        birtday.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, 1990, 0, 1);
        datePickerDialog.show();
    }

    private boolean formValidation(){
        if (fullname.getText().length()<1 || email.getText().length()<1 || password.getText().length()<1 || retypePassword.getText().length()<1 || birtday.getText().length()<1){
            return false;
        } else {
            return true;
        }

    }

    private void register(){
        String gender;
        if (male.isChecked()){
            gender = "male";
        }else {
            gender = "female";
        }

        if (password.getText().toString().equals(retypePassword.getText().toString())){
            loading.show();
            Map<String, String> params = new HashMap<String, String>();
            params.put("fullname", fullname.getText().toString());
            params.put("email", email.getText().toString());
            params.put("password", password.getText().toString());
            params.put("gender", gender);
            params.put("birth_date", birtday.getText().toString());

            volleyHelper.post(ApiHelper.REGISTER, params, new VolleyHelper.HttpListener<String>() {
                @Override
                public void onReceive(boolean status, String message, String response) {
                    if (status){
                        loading.dismiss();
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("status")){
                                Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                                tabLayout.getTabAt(0).select();
                            }else {
                                Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(getContext(), "Password not match", Toast.LENGTH_SHORT).show();
        }
    }
}
