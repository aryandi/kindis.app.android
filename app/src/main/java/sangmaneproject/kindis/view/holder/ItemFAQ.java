package sangmaneproject.kindis.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import sangmaneproject.kindis.R;

/**
 * Created by DELL on 2/6/2017.
 */

public class ItemFAQ extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView subTitle;

    public ItemFAQ(View itemView) {
        super(itemView);
        this.title = (TextView) itemView.findViewById(R.id.title_faq);
        this.subTitle = (TextView) itemView.findViewById(R.id.subtitle_faq);
    }
}
