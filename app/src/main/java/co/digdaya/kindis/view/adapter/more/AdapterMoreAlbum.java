package co.digdaya.kindis.view.adapter.more;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.model.MoreModel;
import co.digdaya.kindis.view.activity.Detail.Detail;
import co.digdaya.kindis.view.holder.Item;
import co.digdaya.kindis.view.holder.ItemListTab;

/**
 * Created by DELL on 4/3/2017.
 */

public class AdapterMoreAlbum extends RecyclerView.Adapter<Item>{
    MoreModel.AlbumMore albumMore;
    Context context;

    public AdapterMoreAlbum(MoreModel.AlbumMore albumMore, Context context) {
        this.albumMore = albumMore;
        this.context = context;
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more, parent, false);
        Item item= new Item(view);
        return item;
    }

    @Override
    public void onBindViewHolder(Item holder, final int position) {
        ImageView imageView = holder.imageView;
        ImageView badgePremium = holder.badgePremium;
        TextView title = holder.title;
        TextView subTitle = holder.subtitle;
        RelativeLayout click = holder.click;

        title.setText(albumMore.result.get(position).title);
        subTitle.setText(albumMore.result.get(position).year);
        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+albumMore.result.get(position).image)
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", albumMore.result.get(position).uid);
                intent.putExtra("type", "album");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumMore.result.size();
    }
}