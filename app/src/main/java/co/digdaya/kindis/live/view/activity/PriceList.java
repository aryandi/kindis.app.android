package co.digdaya.kindis.live.view.activity;

import android.app.Dialog;
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
import co.digdaya.kindis.live.view.dialog.DialogAlertPremium;
import co.digdaya.kindis.live.view.dialog.DialogGetPremium;
import co.digdaya.kindis.live.view.dialog.DialogMessage;
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
    AdapterPriceList adapterPriceList;
    GooglePayment googlePayment;
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
    private Dialog dialog;
    private DialogMessage dialogMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_price_list);
        ButterKnife.bind(this);

        list = findViewById(R.id.list);
        btnClose = findViewById(R.id.btn_close);

        sessionHelper = new SessionHelper();
        loading = new ProgressDialog(this, R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);
        apiCall = ApiUtil.callService();

        checkPremium();

        TextViewHelper.setSpanColor(textVerify, "Kindis Premium",
                ContextCompat.getColor(this, R.color.yellow));

        dialog = new Dialog(this);
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onSelected(int i) {
    }

    @Override
    public void onClickGooglePay(final int i) {
        googlePayment = new GooglePayment(PriceList.this, datas.get(i).package_id, orderID);
//        googlePayment = new GooglePayment(PriceList.this, ITEM_SKU, orderID);
        dialogMessage = new DialogMessage(this, dialog,
                "Silahkan memilih pembayaran melalui pulsa atau credit card pada pengaturan Google Pay",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        googlePayment.buyClick();
                    }
                }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
            }
        });
        dialogMessage.setButtonPositiveHighLight();
        dialogMessage.showDialog();
    }

    @Override
    public void onClickGoPay(final int i) {
        final String transID = "PRE" + sessionHelper.getPreferences(getApplicationContext(), "user_id")
                + new Random().nextInt(89) + 10;
        int price = Integer.parseInt(datas.get(i).price);
        price = price + (2 * price / 100);
        midtransPayment = new MidtransPayment(PriceList.this, transID,
                price, datas.get(i).name, orderID,
                "", "1");
        dialogMessage = new DialogMessage(this, dialog,
                "Transaksi menggunakan GoPay akan dikenai biaya 2% dari tiap transaksi",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        midtransPayment.startGopayPayment();
                    }
                }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
            }
        });
        dialogMessage.setButtonPositiveHighLight();
        dialogMessage.showDialog();
    }

    @Override
    public void onClickTransfer(final int i) {
        final String transID = "PRE" + sessionHelper.getPreferences(getApplicationContext(), "user_id")
                + new Random().nextInt(89) + 10;
        int price = Integer.parseInt(datas.get(i).price) + 4000;
        midtransPayment = new MidtransPayment(PriceList.this, transID,
                price, datas.get(i).name, orderID,
                "", "1");
        dialogMessage = new DialogMessage(this, dialog,
                "Transaksi menggunakan transfer bank akan dikenai biaya 4.000 per transaksi",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        midtransPayment.startTransferBankPayment();
                    }
                }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogMessage.dismiss();
            }
        });
        dialogMessage.setButtonPositiveHighLight();
        dialogMessage.showDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
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
                            setVerifiedLayout(View.GONE, true);
                        } else {
                            setVerifiedLayout(View.VISIBLE, false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    setVerifiedLayout(View.GONE, true);
                }
                getPriceList();
            }
        });
    }

    private void setVerifiedLayout(int visible, boolean b) {
        textVerify.setVisibility(visible);
        btnOk.setVisibility(visible);
        isVerified = b;
    }
}
