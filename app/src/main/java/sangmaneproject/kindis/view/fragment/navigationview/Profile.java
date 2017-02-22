package sangmaneproject.kindis.view.fragment.navigationview;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.SessionHelper;
import sangmaneproject.kindis.view.activity.SignInActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment implements View.OnClickListener {
    DrawerLayout drawer;
    ImageButton btnDrawer;
    ImageButton btnLogout;
    Button btnEdit;
    Button btnSave;

    EditText inputNama;
    EditText inputEmail;
    EditText inputPassword;

    InputMethodManager imm;
    SessionHelper sessionHelper;

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
        btnLogout = (ImageButton) view.findViewById(R.id.btn_logout);
        btnEdit = (Button) view.findViewById(R.id.btn_edit);
        btnSave = (Button) view.findViewById(R.id.btn_save);

        inputNama = (EditText) view.findViewById(R.id.input_nama);
        inputEmail = (EditText) view.findViewById(R.id.input_email);
        inputPassword = (EditText) view.findViewById(R.id.input_password);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        sessionHelper = new SessionHelper();

        inputNama.setText(sessionHelper.getPreferences(getContext(), "fullname"));
        inputEmail.setText(sessionHelper.getPreferences(getContext(), "email"));
        inputPassword.setText(sessionHelper.getPreferences(getContext(), "fullname"));

        inputNama.setEnabled(false);
        inputEmail.setEnabled(false);
        inputPassword.setEnabled(false);

        btnDrawer.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_drawer:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.btn_edit:
                inputNama.setEnabled(true);
                inputEmail.setEnabled(true);
                inputNama.setFocusable(true);
                inputPassword.setEnabled(true);
                inputNama.setSelection(inputNama.getText().length());
                imm.showSoftInput(inputNama, InputMethodManager.SHOW_IMPLICIT);
                btnSave.setVisibility(View.VISIBLE);
                break;
            /*case R.id.edit_email:
                inputEmail.setFocusable(true);
                inputEmail.setSelection(inputEmail.getText().length());
                imm.showSoftInput(inputEmail, InputMethodManager.SHOW_IMPLICIT);
                break;
            case R.id.edit_password:
                inputPassword.setFocusable(true);
                inputPassword.setSelection(inputPassword.getText().length());
                imm.showSoftInput(inputPassword, InputMethodManager.SHOW_IMPLICIT);
                break;*/
            case R.id.btn_logout:
                sessionHelper.setPreferences(getContext(), "status", "0");
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                startActivity(intent);
        }
    }
}