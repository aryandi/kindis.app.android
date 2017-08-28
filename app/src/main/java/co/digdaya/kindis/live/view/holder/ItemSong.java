package co.digdaya.kindis.live.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.digdaya.kindis.live.R;

/**
 * Created by vincenttp on 2/13/2017.
 */

public class ItemSong extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView subTitle;
    public RelativeLayout click;
    public ImageButton btnMenu;

    public ItemSong(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        subTitle = (TextView) itemView.findViewById(R.id.subtitle);
        click = (RelativeLayout) itemView.findViewById(R.id.adapter_song);
        btnMenu = (ImageButton) itemView.findViewById(R.id.btn_menu);
    }
}
