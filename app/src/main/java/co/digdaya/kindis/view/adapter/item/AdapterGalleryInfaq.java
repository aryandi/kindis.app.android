package co.digdaya.kindis.view.adapter.item;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.model.DetailInfaqModel;
import co.digdaya.kindis.view.holder.Item;

/**
 * Created by DELL on 4/27/2017.
 */

public class AdapterGalleryInfaq extends RecyclerView.Adapter<Item> {
    Context context;
    DetailInfaqModel gallery;

    public AdapterGalleryInfaq(Context context, DetailInfaqModel gallery) {
        this.context = context;
        this.gallery = gallery;
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more, parent, false);
        Item item= new Item(view);
        return item;
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {
        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+gallery.result.gallery.get(position).media_url)
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return gallery.result.gallery.size();
    }
}
