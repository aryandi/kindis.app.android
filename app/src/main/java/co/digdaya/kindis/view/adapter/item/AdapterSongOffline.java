package co.digdaya.kindis.view.adapter.item;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.model.DataSingleOffline;
import co.digdaya.kindis.view.holder.Item;

/**
 * Created by DELL on 5/11/2017.
 */

public class AdapterSongOffline extends RecyclerView.Adapter<Item> {
    Activity activity;
    List<DataSingleOffline> dataSingleOfflines;

    public AdapterSongOffline(Activity activity, List<DataSingleOffline> dataSingleOfflines) {
        this.activity = activity;
        this.dataSingleOfflines = dataSingleOfflines;
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_grid, parent, false);
        Item item= new Item(view);
        return item;
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {
        DataSingleOffline dataSingleOffline = dataSingleOfflines.get(position);
        ImageView imageView = holder.imageView;
        ImageView badgePremium = holder.badgePremium;
        TextView title = holder.title;
        TextView subTitle = holder.subtitle;
        RelativeLayout click = holder.click;

        title.setText(dataSingleOffline.getTitle());
        subTitle.setText(dataSingleOffline.getArtist());
        Glide.with(activity)
                .load(dataSingleOffline.getImage())
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return dataSingleOfflines.size();
    }
}
