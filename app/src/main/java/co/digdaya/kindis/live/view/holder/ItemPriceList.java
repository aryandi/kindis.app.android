package co.digdaya.kindis.live.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import co.digdaya.kindis.live.R;

/**
 * Created by vincenttp on 10/28/17.
 */

public class ItemPriceList extends RecyclerView.ViewHolder{
    public TextView title;
    public TextView price;


    public ItemPriceList(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        price = (TextView) itemView.findViewById(R.id.price);
    }
}
