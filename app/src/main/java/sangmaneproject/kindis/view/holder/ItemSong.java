package sangmaneproject.kindis.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sangmaneproject.kindis.R;

/**
 * Created by vincenttp on 2/13/2017.
 */

public class ItemSong extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView subTitle;
    public RelativeLayout click;

    public ItemSong(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        subTitle = (TextView) itemView.findViewById(R.id.subtitle);
        click = (RelativeLayout) itemView.findViewById(R.id.adapter_song);
    }
}
