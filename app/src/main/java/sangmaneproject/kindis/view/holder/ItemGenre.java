package sangmaneproject.kindis.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import sangmaneproject.kindis.R;

/**
 * Created by DELL on 2/2/2017.
 */

public class ItemGenre extends RecyclerView.ViewHolder {
    public ImageView icon;
    public TextView title;

    public ItemGenre(View itemView) {
        super(itemView);
        this.icon = (ImageView) itemView.findViewById(R.id.ic_genre);
        this.title = (TextView) itemView.findViewById(R.id.title_genre);
    }
}
