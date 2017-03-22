package co.digdaya.kindis.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.digdaya.kindis.R;

/**
 * Created by vincenttp on 1/27/2017.
 */

public class Item extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public ImageView badgePremium;
    public TextView title;
    public TextView subtitle;
    public RelativeLayout click;

    public Item(View itemView) {
        super(itemView);
        this.imageView = (ImageView) itemView.findViewById(R.id.image);
        this.title = (TextView) itemView.findViewById(R.id.title);
        this.subtitle = (TextView) itemView.findViewById(R.id.subtitle);
        this.click = (RelativeLayout) itemView.findViewById(R.id.click);
        this.badgePremium = (ImageView) itemView.findViewById(R.id.badge_premium);
    }
}
