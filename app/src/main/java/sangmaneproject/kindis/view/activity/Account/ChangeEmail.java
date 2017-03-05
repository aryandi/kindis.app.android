package sangmaneproject.kindis.view.activity.Account;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.util.BackgroundProses.DialogLoading;

public class ChangeEmail extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, TextWatcher {
    ImageButton btnDrawer;
    ImageButton btnMenu;
    EditText inputEmail;
    Button btnEdit;
    LinearLayout contError;
    TextView errorText;

    SessionHelper sessionHelper;
    DialogLoading loading;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        sessionHelper = new SessionHelper();
        loading = new DialogLoading(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        btnDrawer = (ImageButton) findViewById(R.id.btn_drawer);
        btnMenu = (ImageButton) findViewById(R.id.btn_menu);
        inputEmail = (EditText) findViewById(R.id.input_email);
        btnEdit = (Button) findViewById(R.id.btn_save);
        contError = (LinearLayout) findViewById(R.id.cont_error_message);
        errorText = (TextView) findViewById(R.id.error_message);

        btnDrawer.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        inputEmail.addTextChangedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_drawer:
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                finish();
                break;
            case R.id.btn_menu:
                PopupMenu popup = new PopupMenu(this, btnMenu);
                popup.getMenuInflater().inflate(R.menu.profile, popup.getMenu());
                popup.setOnMenuItemClickListener(this);
                popup.show();
                break;
            case R.id.btn_save:
                if (inputEmail.getText().length()<9 || !inputEmail.getText().toString().contains("@")){
                    inputEmail.setBackground(getResources().getDrawable(R.drawable.edittext_error, null));
                    contError.setVisibility(View.VISIBLE);
                }else {
                    loading.showLoading();
                    changeEmail();
                }
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
            case R.id.password:
                Intent intent2 = new Intent(this, ChangePassword.class);
                startActivity(intent2);
                break;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        inputEmail.setBackground(getResources().getDrawable(R.drawable.edittext_normal, null));
        contError.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void changeEmail(){
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", sessionHelper.getPreferences(getApplicationContext(), "user_id"));
        param.put("new_email", inputEmail.getText().toString());
        param.put("old_email", sessionHelper.getPreferences(getApplicationContext(), "email"));

        new VolleyHelper().post(ApiHelper.CHANGE_EMAIL, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismisLoading();
                if (status){
                    Log.d("changeemailll", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            inputEmail.setBackground(getResources().getDrawable(R.drawable.edittext_error, null));
                            contError.setVisibility(View.VISIBLE);
                            errorText.setText(object.getString("message"));
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
