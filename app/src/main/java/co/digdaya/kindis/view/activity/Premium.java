package co.digdaya.kindis.view.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.view.dialog.DialogPayment;

public class Premium extends AppCompatActivity {
    TextView btnCancel, btnOk;
    SessionHelper sessionHelper;
    Random random;
    DialogPayment dialogPayment;
    Dialog dialogPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        btnCancel = (TextView) findViewById(R.id.btn_cancel);
        btnOk = (TextView) findViewById(R.id.btn_ok);
        sessionHelper = new SessionHelper();
        random = new Random();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transID = "PRE"+sessionHelper.getPreferences(getApplicationContext(), "user_id")+random.nextInt(89)+10;
                dialogPayment = new DialogPayment(dialogPay, Premium.this, transID, 10000, "Akun Premium");
                dialogPayment.showDialog();
            }
        });
    }
}
