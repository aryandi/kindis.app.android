package co.digdaya.kindis.live.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;
import java.util.Random;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.SessionHelper;
import co.digdaya.kindis.live.model.PriceListModel;
import co.digdaya.kindis.live.util.Payment.GooglePayment;
import co.digdaya.kindis.live.util.Payment.MidtransPayment;
import co.digdaya.kindis.live.view.adapter.AdapterPriceList;

/**
 * Created by vincenttp on 10/24/17.
 */

public class DialogPriceList extends Dialog implements View.OnClickListener, AdapterPriceList.OnSelectedItem {
    private Context mContext;
    List<PriceListModel.Data> datas;
    String orderID;

    RecyclerView list;
    ImageButton btnClose;
    Button btnGoogle, btnMidtrans;

    AdapterPriceList adapterPriceList;
    GooglePayment googlePayment;
    MidtransPayment midtransPayment;
    SessionHelper sessionHelper;

    public DialogPriceList(@NonNull Context context, List<PriceListModel.Data> datas, String orderID) {
        super(context);
        mContext = context;
        this.datas = datas;
        this.orderID = orderID;
        setOwnerActivity((Activity) context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_price_list);
        witdhDialog();

        list = (RecyclerView) findViewById(R.id.list);
        btnClose = (ImageButton) findViewById(R.id.btn_close);
        btnGoogle = (Button) findViewById(R.id.btn_google);
        btnMidtrans = (Button) findViewById(R.id.btn_midtrans);

        adapterPriceList = new AdapterPriceList(mContext, datas);
        sessionHelper = new SessionHelper();

        list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(adapterPriceList);
        adapterPriceList.setOnSelectedItem(DialogPriceList.this);

        btnClose.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnMidtrans.setOnClickListener(this);
    }

    private void witdhDialog(){
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = getWindow();
        layoutParams.copyFrom(window.getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_close:
                dismiss();
                break;
            case R.id.btn_google:
                googlePayment.buyClick();
                break;
            case R.id.btn_midtrans:
                midtransPayment.startPayment();
                break;
        }
    }

    @Override
    public void onSelected(int i) {
        String transID = "PRE"+sessionHelper.getPreferences(mContext, "user_id")+new Random().nextInt(89)+10;
        googlePayment = new GooglePayment(getOwnerActivity(), datas.get(i).package_id, orderID);
        midtransPayment = new MidtransPayment(getOwnerActivity(), transID, Integer.parseInt(datas.get(i).price), datas.get(i).name, orderID, "", "1");
    }
}
