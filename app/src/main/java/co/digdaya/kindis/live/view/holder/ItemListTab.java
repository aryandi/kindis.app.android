package co.digdaya.kindis.live.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import co.digdaya.kindis.live.R;

/**
 * Created by DELL on 3/31/2017.
 */

public class ItemListTab extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView btnMore;
    public RecyclerView list;
    public ImageView imageAds;
    public TextView textAds;

    public ItemListTab(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.text_list);
        btnMore = (TextView) itemView.findViewById(R.id.btn_more_list);
        list = (RecyclerView) itemView.findViewById(R.id.list);
        imageAds = (ImageView) itemView.findViewById(R.id.image_ads);
        textAds = (TextView) itemView.findViewById(R.id.text_ads);
    }
}
