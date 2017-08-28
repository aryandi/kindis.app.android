package co.digdaya.kindis.live.view.activity.Account;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;

public class ForgotPassword extends AppCompatActivity {
    ImageButton back;
    EditText email;
    Button login;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        back = (ImageButton) findViewById(R.id.back);
        email = (EditText) findViewById(R.id.input_email);
        login = (Button) findViewById(R.id.btn_forgot);
        loading = new ProgressDialog(this);

        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage("Loading. Please wait...");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.show();
                forgot();
            }
        });
    }

    private void forgot(){
        System.out.println("forgotpasswordssss: true");
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email.getText().toString());

        new VolleyHelper().post(ApiHelper.FORGOT, params, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                System.out.println("forgotpasswordssss: "+response);
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
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
