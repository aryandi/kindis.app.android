package co.digdaya.kindis.live.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;
import java.util.Random;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.model.PriceListModel;
import co.digdaya.kindis.live.network.ApiCall;
import co.digdaya.kindis.live.network.ApiUtil;
import co.digdaya.kindis.live.util.Payment.GooglePayment;
import co.digdaya.kindis.live.util.Payment.MidtransPayment;
import co.digdaya.kindis.live.view.adapter.AdapterPriceList;
import co.digdaya.kindis.live.view.dialog.DialogPriceList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vincenttp on 10/31/17.
 */

public class PriceList extends AppCompatActivity implements AdapterPriceList.OnSelectedItem, View.OnClickListener {
    List<PriceListModel.Data> datas;
    String orderID;

    RecyclerView list;
    ImageButton btnClose;
    Button btnGoogle, btnMidtrans;

    AdapterPriceList adapterPriceList;
    GooglePayment googlePayment;
    MidtransPayment midtransPayment;
    SessionHelper sessionHelper;
    ApiCall apiCall;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_price_list);

        list = (RecyclerView) findViewById(R.id.list);
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnGoogle = (Button) findViewById(R.id.btn_google);
        btnMidtrans = (Button) findViewById(R.id.btn_midtrans);

        sessionHelper = new SessionHelper();
        apiCall = ApiUtil.callService();

        getPriceList();

        btnClose.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnMidtrans.setOnClickListener(this);
    }

    @Override
    public void onSelected(int i) {
        String transID = "PRE"+sessionHelper.getPreferences(getApplicationContext(), "user_id")+new Random().nextInt(89)+10;
        googlePayment = new GooglePayment(this, datas.get(i).package_id, orderID);
        midtransPayment = new MidtransPayment(this, transID, Integer.parseInt(datas.get(i).price), datas.get(i).name, orderID, "", "1");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_google:
                googlePayment.buyClick();
                break;
            case R.id.btn_midtrans:
                midtransPayment.startPayment();
                break;
        }
    }

    private void getPriceList(){
        String url = ApiHelper.PRICE_LIST+sessionHelper.getPreferences(getApplicationContext(), "user_id")+"&dev_id=2&client_id=xBc3w11&token_access="+sessionHelper.getPreferences(getApplicationContext(), "token_access");
        apiCall.priceList(url).enqueue(new Callback<PriceListModel>() {
            @Override
            public void onResponse(Call<PriceListModel> call, Response<PriceListModel> response) {
                if (response.body().status){
                    datas = response.body().result.data;
                    orderID = response.body().result.order_id;
                    adapterPriceList = new AdapterPriceList(getApplicationContext(), datas);
                    list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    list.setAdapter(adapterPriceList);
                    adapterPriceList.setOnSelectedItem(PriceList.this);

                    String transID = "PRE"+sessionHelper.getPreferences(getApplicationContext(), "user_id")+new Random().nextInt(89)+10;
                    googlePayment = new GooglePayment(PriceList.this, datas.get(0).package_id, orderID);
                    midtransPayment = new MidtransPayment(PriceList.this, transID, Integer.parseInt(datas.get(0).price), datas.get(0).name, orderID, "", "1");
                }
            }

            @Override
            public void onFailure(Call<PriceListModel> call, Throwable t) {

            }
        });
    }
}
