package sangmaneproject.kindis.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import sangmaneproject.kindis.R;

/**
 * Created by vincenttp on 1/27/2017.
 */

public class Item extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView title;
    public TextView subtitle;
    public Item(View itemView) {
        super(itemView);
        this.imageView = (ImageView) itemView.findViewById(R.id.image);
        this.title = (TextView) itemView.findViewById(R.id.title);
        this.subtitle = (TextView) itemView.findViewById(R.id.subtitle);
    }
}
