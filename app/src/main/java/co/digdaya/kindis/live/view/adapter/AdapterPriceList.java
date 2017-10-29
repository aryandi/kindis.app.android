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
import co.digdaya.kindis.live.view.dialog.DialogPriceList;
import co.digdaya.kindis.live.view.holder.ItemPriceList;

/**
 * Created by vincenttp on 10/28/17.
 */

public class AdapterPriceList extends RecyclerView.Adapter<ItemPriceList> {
    Context context;
    List<PriceListModel.Data> datas;
    OnSelectedItem onSelectedItem;

    int tempPositionClick = 99;

    public AdapterPriceList(Context context, List<PriceListModel.Data> datas) {
        this.context = context;
        this.datas = datas;
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
        return itemPriceList;
    }

    @Override
    public void onBindViewHolder(ItemPriceList holder, int position) {
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

    private void onClicked(int i, ItemPriceList itemPriceList){
        if (tempPositionClick!=99){
            notifyItemChanged(tempPositionClick);
            tempPositionClick = i;
        }else {
            tempPositionClick = i;
        }

        itemPriceList.title.setTextColor(ContextCompat.getColor(context, R.color.safety_orange));
        itemPriceList.price.setTextColor(ContextCompat.getColor(context, R.color.safety_orange));
        onSelectedItem.onSelected(i);
    }

    public interface OnSelectedItem{
        void onSelected(int i);
    }

    public void setOnSelectedItem(OnSelectedItem onSelectedItem){
        this.onSelectedItem = onSelectedItem;
    }
}
