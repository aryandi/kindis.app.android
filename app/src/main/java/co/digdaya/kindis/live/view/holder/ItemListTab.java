package co.digdaya.kindis.live.view.holder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.custom.ImageViewRounded;
import co.digdaya.kindis.live.helper.SessionHelper;

import static co.digdaya.kindis.live.view.adapter.tab.AdapterListTab.MAX_ADS;

/**
 * Created by DELL on 3/31/2017.
 */

public class ItemListTab extends RecyclerView.ViewHolder {
    public AdView imageAds1;
    public AdView imageAds2;
    public AdView imageAds3;
    public AdView imageAds4;
    public TextView title;
    public TextView btnMore;
    public RecyclerView list;
    public View imageAds;

    public ItemListTab(View itemView, SessionHelper sessionHelper, String[] ads) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.text_list);
        btnMore = (TextView) itemView.findViewById(R.id.btn_more_list);
        list = (RecyclerView) itemView.findViewById(R.id.list);
        imageAds = itemView.findViewById(R.id.image_ads);
        imageAds1 = (AdView) itemView.findViewById(R.id.image_ads_1);
        imageAds2 = (AdView) itemView.findViewById(R.id.image_ads_2);
        imageAds3 = (AdView) itemView.findViewById(R.id.image_ads_3);
        imageAds4 = (AdView) itemView.findViewById(R.id.image_ads_4);
    }
}
