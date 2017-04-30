package co.digdaya.kindis.view.adapter.more;

import android.app.Activity;
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

import static android.R.attr.data;

/**
 * Created by DELL on 4/3/2017.
 */

public class AdapterMorePlaylist extends RecyclerView.Adapter<Item> {
    Activity context;
    MoreModel.PlaylisMore playlisMore;

    public AdapterMorePlaylist(Activity context, MoreModel.PlaylisMore playlisMore) {
        this.context = context;
        this.playlisMore = playlisMore;
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_grid, parent, false);
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

        title.setText(playlisMore.result.get(position).name);
        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+playlisMore.result.get(position).image)
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        badgePremium.setVisibility(View.VISIBLE);
        if (getItemViewType(position) == 0){
            badgePremium.setImageResource(R.drawable.ic_badge_sponsored);
        }

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Detail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", playlisMore.result.get(position).uid);
                intent.putExtra("type", "premium");
                intent.putExtra("isMyPlaylist", "");
                intent.putExtra("playlisttype", getItemViewType(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlisMore.result.size();
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.parseInt(playlisMore.result.get(position).is_premium);
    }
}
