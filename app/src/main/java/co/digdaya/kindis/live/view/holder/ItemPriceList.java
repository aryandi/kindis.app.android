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
    public LinearLayout layout;
    public LinearLayout layoutButton;
    public TextViewSemiBold btnGPay;
    public TextViewSemiBold btnOther;
    public View space;
    public ConstraintLayout layoutDiscount;
    public TextViewRegular textDiscount;

    public ItemPriceList(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        price = itemView.findViewById(R.id.price);
        layout = itemView.findViewById(R.id.layout_holder);
        layoutButton = itemView.findViewById(R.id.btn_activity);
        btnGPay = itemView.findViewById(R.id.btn_gpay);
        btnOther = itemView.findViewById(R.id.btn_other);
        space = itemView.findViewById(R.id.space);
        layoutDiscount = itemView.findViewById(R.id.layout_discount);
        textDiscount = itemView.findViewById(R.id.text_discount);
    }
}
