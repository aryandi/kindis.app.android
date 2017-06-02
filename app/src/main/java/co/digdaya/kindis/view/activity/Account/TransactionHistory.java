package co.digdaya.kindis.view.activity.Account;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.model.TransactionHistoryModel;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerActivity;
import co.digdaya.kindis.view.adapter.AdapterTransactionHistory;

public class TransactionHistory extends BottomPlayerActivity implements View.OnClickListener {
    ImageButton btnBack;
    TextView titleBar, title, subtitle;
    SessionHelper sessionHelper;
    ListView recyclerView;
    Gson gson;
    LinearLayout emptyState;
    Button refresh;

    public TransactionHistory() {
        layout = R.layout.fragment_notification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btnBack = (ImageButton) findViewById(R.id.btn_drawer);
        titleBar = (TextView) findViewById(R.id.title_bar);
        recyclerView = (ListView) findViewById(R.id.listview);

        emptyState = (LinearLayout) findViewById(R.id.empty_state);
        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
        refresh = (Button) findViewById(R.id.btn_refresh);

        btnBack.setImageResource(R.drawable.ic_arrow_back);
        titleBar.setText("Transaction History");

        sessionHelper = new SessionHelper();
        gson = new Gson();
        getTransaction();

        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_drawer:
                finish();
                break;
            case R.id.btn_refresh:
                getTransaction();
                break;
        }
    }

    private void getTransaction(){
        String url = ApiHelper.TRANSACTION_HISTORY+sessionHelper.getPreferences(getApplicationContext(), "user_id") +
                "&token_access="+sessionHelper.getPreferences(getApplicationContext(), "token_access") +
                "&dev_id=2&client_id=xBc3w11";

        new VolleyHelper().get(url, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                TransactionHistoryModel transactionHistoryModel = gson.fromJson(response, TransactionHistoryModel.class);
                if (transactionHistoryModel.result.isEmpty()){
                    emptyState.setVisibility(View.VISIBLE);
                    title.setText("No Transaction History");
                    subtitle.setText(transactionHistoryModel.message);
                    refresh.setOnClickListener(TransactionHistory.this);
                }else {
                    AdapterTransactionHistory adapterTransactionHistory = new AdapterTransactionHistory(transactionHistoryModel, getApplicationContext());
                    recyclerView.setAdapter(adapterTransactionHistory);
                }
            }
        });
    }
}
