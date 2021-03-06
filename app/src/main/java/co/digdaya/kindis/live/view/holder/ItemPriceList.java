package co.digdaya.kindis.live.view.holder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.custom.TextViewRegular;
import co.digdaya.kindis.live.custom.TextViewSemiBold;

/**
 * Created by vincenttp on 10/28/17.
 */

public class ItemPriceList extends RecyclerView.ViewHolder{
    public TextView title;
    public TextView price;
    public ConstraintLayout layout;
    public ConstraintLayout layoutButton;
    public TextViewSemiBold btnGooglePay;
    public TextViewSemiBold btnGopay;
    public TextViewSemiBold btnTransferBank;
    public View space;
    public ConstraintLayout layoutDiscount;
    public TextViewRegular textDiscount;

    public ItemPriceList(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        price = itemView.findViewById(R.id.price);
        layout = itemView.findViewById(R.id.layout_holder);
        layoutButton = itemView.findViewById(R.id.btn_activity);
        btnGooglePay = itemView.findViewById(R.id.btn_google_pay);
        btnGopay = itemView.findViewById(R.id.btn_gopay);
        btnTransferBank = itemView.findViewById(R.id.btn_transfer_bank);
        space = itemView.findViewById(R.id.space);
        layoutDiscount = itemView.findViewById(R.id.layout_discount);
        textDiscount = itemView.findViewById(R.id.text_discount);
    }
}
