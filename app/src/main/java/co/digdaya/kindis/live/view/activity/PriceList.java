package co.digdaya.kindis.live.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.custom.ButtonSemiBold;
import co.digdaya.kindis.live.custom.TextViewSemiBold;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.helper.TextViewHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.model.PriceListModel;
import co.digdaya.kindis.live.network.ApiCall;
import co.digdaya.kindis.live.network.ApiUtil;
import co.digdaya.kindis.live.util.GoogleBilling.IabHelper;
import co.digdaya.kindis.live.util.Payment.GooglePayment;
import co.digdaya.kindis.live.util.Payment.MidtransPayment;
import co.digdaya.kindis.live.view.activity.Account.VerifyAccount;
import co.digdaya.kindis.live.view.adapter.AdapterPriceList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static co.digdaya.kindis.live.helper.Constanta.PREF_TOKEN_ACCESS;
import static co.digdaya.kindis.live.helper.Constanta.PREF_USERID;

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
    IabHelper mHelper;
    MidtransPayment midtransPayment;
    SessionHelper sessionHelper;
    ApiCall apiCall;
    @BindView(R.id.text_verify)
    TextViewSemiBold textVerify;
    @BindView(R.id.btn_ok)
    ButtonSemiBold btnOk;

    boolean isVerified = false;
    private ProgressDialog loading;

    static final String ITEM_SKU = "android.test.purchased";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_price_list);
        ButterKnife.bind(this);

        list = (RecyclerView) findViewById(R.id.list);
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnGoogle = (Button) findViewById(R.id.btn_google);
        btnMidtrans = (Button) findViewById(R.id.btn_midtrans);

        sessionHelper = new SessionHelper();
        loading = new ProgressDialog(this, R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);
        apiCall = ApiUtil.callService();

        checkPremium();

        TextViewHelper.setSpanColor(textVerify, "Kindis Premium",
                ContextCompat.getColor(this, R.color.yellow));

        btnClose.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnMidtrans.setOnClickListener(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onSelected(int i) {
        String transID = "PRE" + sessionHelper.getPreferences(getApplicationContext(), "user_id")
                + new Random().nextInt(89) + 10;
        googlePayment = new GooglePayment(this, datas.get(i).package_id, orderID);
        midtransPayment = new MidtransPayment(this, transID, Integer.parseInt(datas.get(i).price),
                datas.get(i).name, orderID, "", "1");
    }

    @Override
    public void onClickGPay(int i) {
        googlePayment.buyClick();
    }

    @Override
    public void onClickOther(int i) {
        midtransPayment.startPayment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_google:
                googlePayment.buyClick();
                break;
            case R.id.btn_midtrans:
                midtransPayment.startPayment();
                break;
            case R.id.btn_ok:
                Intent intent = new Intent(PriceList.this, VerifyAccount.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkPremium();
    }

    private void getPriceList() {
        String url = ApiHelper.PRICE_LIST + sessionHelper.getPreferences(getApplicationContext(),
                "user_id") + "&dev_id=2&client_id=xBc3w11&token_access="
                + sessionHelper.getPreferences(getApplicationContext(), "token_access");
        apiCall.priceList(url).enqueue(new Callback<PriceListModel>() {
            @Override
            public void onResponse(Call<PriceListModel> call, Response<PriceListModel> response) {
                if (response.body().status) {
                    datas = response.body().result.data;
                    orderID = response.body().result.order_id;
                    adapterPriceList = new AdapterPriceList(getApplicationContext(), datas, isVerified);
                    list.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                            LinearLayoutManager.VERTICAL, false));
                    list.setAdapter(adapterPriceList);
                    adapterPriceList.setOnSelectedItem(PriceList.this);

                    String transID = "PRE" + sessionHelper.getPreferences(getApplicationContext(),
                            "user_id") + new Random().nextInt(89) + 10;
                    googlePayment = new GooglePayment(PriceList.this, datas.get(0).package_id, orderID);
                    midtransPayment = new MidtransPayment(PriceList.this, transID,
                            Integer.parseInt(datas.get(0).price), datas.get(0).name, orderID, "", "1");
                }
            }

            @Override
            public void onFailure(Call<PriceListModel> call, Throwable t) {

            }
        });
    }

    private void checkPremium() {
        loading.show();

        HashMap<String, String> param = new HashMap<>();
        param.put(PREF_USERID, sessionHelper.getPreferences(this, PREF_USERID));
        param.put(PREF_TOKEN_ACCESS, sessionHelper.getPreferences(this, PREF_TOKEN_ACCESS));

        new VolleyHelper().post(ApiHelper.CHECK_PREMIUM, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                Log.d("check_premium", response);
                if (status) {
                    try {
                        JSONObject object = new JSONObject(response);
//                        Toast.makeText(PriceList.this, object.getString("message"),
//                                Toast.LENGTH_SHORT).show();
                        if (object.getBoolean("status")) {
                            textVerify.setVisibility(View.GONE);
                            btnOk.setVisibility(View.GONE);
                            isVerified = true;
                        } else {
                            textVerify.setVisibility(View.VISIBLE);
                            btnOk.setVisibility(View.VISIBLE);
                            isVerified = false;
                        }
                        getPriceList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        getPriceList();
                    }
                }
            }
        });
    }
}
