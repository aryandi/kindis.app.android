package co.digdaya.kindis.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import co.digdaya.kindis.R;

/**
 * Created by DELL on 3/14/2017.
 */

public class ItemInfaq extends RecyclerView.ViewHolder {
    public ImageView imageView;
    public TextView labelMasjid;
    public TextView labelNama;

    public ItemInfaq(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        labelMasjid = (TextView) itemView.findViewById(R.id.label_masjid);
        labelNama = (TextView) itemView.findViewById(R.id.label_masjid_name);
    }
}
