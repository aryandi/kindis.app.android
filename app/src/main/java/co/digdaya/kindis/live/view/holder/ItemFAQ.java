package co.digdaya.kindis.live.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import co.digdaya.kindis.live.R;

/**
 * Created by DELL on 2/6/2017.
 */

public class ItemFAQ extends RecyclerView.ViewHolder {
    public TextView title;
    public WebView subTitle;

    public ItemFAQ(View itemView) {
        super(itemView);
        this.title = (TextView) itemView.findViewById(R.id.title_faq);
        this.subTitle = (WebView) itemView.findViewById(R.id.subtitle_faq);
    }
}
