package co.digdaya.kindis.live.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.custom.ImageViewRounded;

/**
 * Created by DELL on 3/31/2017.
 */

public class ItemListTab extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView btnMore;
    public RecyclerView list;
    public AdView imageAds;

    public ItemListTab(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.text_list);
        btnMore = (TextView) itemView.findViewById(R.id.btn_more_list);
        list = (RecyclerView) itemView.findViewById(R.id.list);
        imageAds = (AdView) itemView.findViewById(R.id.image_ads);
        imageAds.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                imageAds.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int error) {
                imageAds.setVisibility(View.GONE);
            }

        });
    }
}
