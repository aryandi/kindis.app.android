package sangmaneproject.kindis.view.fragment.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sangmaneproject.kindis.controller.ProfileInfo;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.view.activity.Bismillah;
import sangmaneproject.kindis.view.activity.ForgotPassword;
import sangmaneproject.kindis.R;

public class SignInFragment extends Fragment implements View.OnFocusChangeListener {
    EditText email;
    EditText password;
    AppBarLayout appBarLayout;
    TextView forgotPassword;
    Button login;
    LinearLayout errorMessage;
    VolleyHelper volleyHelper;
    ProgressDialog loading;

    public SignInFragment(){}

    public SignInFragment(AppBarLayout appBarLayout) {
        this.appBarLayout = appBarLayout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        email = (EditText) view.findViewById(R.id.input_email);
        password = (EditText) view.findViewById(R.id.input_password);
        forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
        login = (Button) view.findViewById(R.id.btn_login);
        errorMessage = (LinearLayout) view.findViewById(R.id.cont_error_message);
        volleyHelper = new VolleyHelper();
        loading = new ProgressDialog(getActivity());

        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage("Loading. Please wait...");

        email.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ForgotPassword.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formValidation()){
                    login();
                }else {
                    Toast.makeText(getContext(), "Username or password can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        appBarLayout.setExpanded(false, true);
    }

    private boolean formValidation(){
        if (email.getText().length()<1 || password.getText().length()<1){
            return false;
        }else {
            return true;
        }
    }

    private void login(){
        loading.show();
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email.getText().toString());
        params.put("password", password.getText().toString());

        volleyHelper.post(ApiHelper.LOGIN, params, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    loading.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");
                            new ProfileInfo(getContext()).execute(result.getString("user_id"));
                            Intent intent = new Intent(getActivity(), Bismillah.class);
                            startActivity(intent);
                        }else {
                            errorMessage.setVisibility(View.VISIBLE);
                            email.setBackground(getResources().getDrawable(R.drawable.edittext_error, null));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    loading.dismiss();
                    errorMessage.setVisibility(View.VISIBLE);
                    if(android.os.Build.VERSION.SDK_INT >= 21){
                        email.setBackground(getResources().getDrawable(R.drawable.edittext_error, null));
                    } else {
                        email.setBackground(getResources().getDrawable(R.drawable.edittext_error));
                    }
                }
            }
        });
    }
}
