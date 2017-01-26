package sangmaneproject.kindis.view.fragment;

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
import android.widget.TextView;

import sangmaneproject.kindis.view.activity.Bismillah;
import sangmaneproject.kindis.view.activity.ForgotPassword;
import sangmaneproject.kindis.R;

public class SignInFragment extends Fragment implements View.OnFocusChangeListener {
    EditText email;
    EditText password;
    AppBarLayout appBarLayout;
    TextView forgotPassword;
    Button login;

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
                Intent intent = new Intent(getActivity(), Bismillah.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        appBarLayout.setExpanded(false, true);
    }
}
