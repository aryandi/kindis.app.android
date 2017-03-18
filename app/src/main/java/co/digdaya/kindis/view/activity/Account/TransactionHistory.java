package co.digdaya.kindis.view.activity.Account;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import co.digdaya.kindis.R;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerActivity;

public class TransactionHistory extends BottomPlayerActivity implements View.OnClickListener {
    ImageButton btnBack;
    TextView titleBar;

    public TransactionHistory() {
        layout = R.layout.fragment_notification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnBack = (ImageButton) findViewById(R.id.btn_drawer);
        titleBar = (TextView) findViewById(R.id.title);

        btnBack.setImageResource(R.drawable.ic_arrow_back);
        titleBar.setText("Transaction History");

        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_drawer:
                finish();
                break;
        }
    }
}
