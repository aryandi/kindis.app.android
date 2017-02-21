package sangmaneproject.kindis.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import sangmaneproject.kindis.R;

/**
 * Created by DELL on 2/17/2017.
 */

public class ItemPlaylist extends RecyclerView.ViewHolder {
    public TextView title;
    public RelativeLayout click;

    public ItemPlaylist(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        click = (RelativeLayout) itemView.findViewById(R.id.adapter_playlist);
    }
}
