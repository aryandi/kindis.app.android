package co.digdaya.kindis.live.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.digdaya.kindis.live.R;

/**
 * Created by DELL on 2/17/2017.
 */

public class ItemPlaylist extends RecyclerView.ViewHolder {
    public TextView title;
    public RelativeLayout click;
    public ImageButton menu;

    public ItemPlaylist(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        click = (RelativeLayout) itemView.findViewById(R.id.adapter_playlist);
        menu = (ImageButton) itemView.findViewById(R.id.btn_menu);
    }
}
