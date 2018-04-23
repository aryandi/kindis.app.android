package co.digdaya.kindis.live.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.model.PriceListModel;
import co.digdaya.kindis.live.util.Utils;
import co.digdaya.kindis.live.view.holder.ItemPriceList;

/**
 * Created by vincenttp on 10/28/17.
 */

public class AdapterPriceList extends RecyclerView.Adapter<ItemPriceList> {
    Context context;
    List<PriceListModel.Data> datas;
    private boolean isVerified;
    OnSelectedItem onSelectedItem;

    int tempPositionClick = 99;
    boolean isFirstLoad = true;

    public AdapterPriceList(Context context, List<PriceListModel.Data> datas, boolean isVerified) {
        this.context = context;
        this.datas = datas;
        this.isVerified = isVerified;
    }

    @Override
    public ItemPriceList onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_price_list, parent, false);
        final ItemPriceList itemPriceList = new ItemPriceList(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClicked(viewType, itemPriceList);
            }
        });
        itemPriceList.btnGPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedItem.onClickGPay(viewType);
            }
        });
        itemPriceList.btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectedItem.onClickOther(viewType);
            }
        });
        return itemPriceList;
    }

    @Override
    public void onBindViewHolder(ItemPriceList holder, int position) {

        double percentage;
        double price1month = Double.parseDouble(datas.get(1).price);
        double price3month = Double.parseDouble(datas.get(2).price);
        double price6month = Double.parseDouble(datas.get(3).price);
        double price12month = Double.parseDouble(datas.get(4).price);
        double divider;
        switch (position) {
            case 0:
            case 1:
                holder.layoutDiscount.setVisibility(View.GONE);
                break;
            case 2:
                divider = price1month * 3;
                percentage = 100 - (price3month / divider * 100);
                holder.textDiscount.setText(String.format("%s%%", String.format("%.0f", percentage)));
                break;
            case 3:
                divider = price1month * 6;
                percentage = 100 - (price6month / divider * 100);
                holder.textDiscount.setText(String.format("%s%%", String.format("%.0f", percentage)));
                break;
            case 4:
                divider = price1month * 12;
                percentage = 100 - (price12month / divider * 100);
                holder.textDiscount.setText(String.format("%s%%", String.format("%.0f", percentage)));
                break;
        }

        if (isVerified) {
//            if (position==0 && isFirstLoad){
//                holder.layoutButton.setVisibility(View.VISIBLE);
//                holder.space.setVisibility(View.GONE);
//            } else {
            holder.layoutButton.setVisibility(View.GONE);
            holder.space.setVisibility(View.VISIBLE);
//            }

            holder.layout.setBackground(ContextCompat.getDrawable(context, R.drawable.button_rounded_green_10));
        } else {
            holder.layout.setBackground(ContextCompat.getDrawable(context, R.drawable.button_rounded_gray_10));
        }
        holder.title.setText(datas.get(position).name);
        holder.price.setText(Utils.currencyFormat(Integer.parseInt(datas.get(position).price)));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void onClicked(int i, ItemPriceList itemPriceList) {
        if (isVerified) {
//            if (isFirstLoad){
//                notifyItemChanged(0);
//                isFirstLoad = false;
//            }
            if (tempPositionClick != 99 && tempPositionClick != i) {
                notifyItemChanged(tempPositionClick);
                tempPositionClick = i;
            } else {
                tempPositionClick = i;
            }

            itemPriceList.layoutButton.setVisibility(View.VISIBLE);
            itemPriceList.space.setVisibility(View.GONE);
            onSelectedItem.onSelected(i);
        }

    }

    public interface OnSelectedItem {
        void onSelected(int i);

        void onClickGPay(int i);

        void onClickOther(int i);
    }

    public void setOnSelectedItem(OnSelectedItem onSelectedItem) {
        this.onSelectedItem = onSelectedItem;
    }
}
