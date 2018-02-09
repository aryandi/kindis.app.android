package co.digdaya.kindis.live.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import co.digdaya.kindis.live.R;

/**
 * Created by DELL on 3/14/2017.
 */

public class ItemInfaq extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView labelNama;
    public RelativeLayout relativeLayout;

    public ItemInfaq(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        labelNama = (TextView) itemView.findViewById(R.id.label_masjid_name);
        relativeLayout = (RelativeLayout) itemView.findViewById(R.id.adapter_infaq);
    }
}
