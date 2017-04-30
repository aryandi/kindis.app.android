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
import co.digdaya.kindis.view.activity.Detail.DetailArtist;
import co.digdaya.kindis.view.holder.Item;

/**
 * Created by DELL on 4/3/2017.
 */

public class AdapterMoreArtist extends RecyclerView.Adapter<Item> {
    Activity context;
    MoreModel.ArtistsMore artistsMore;

    public AdapterMoreArtist(Activity context, MoreModel.ArtistsMore artistsMore) {
        this.context = context;
        this.artistsMore = artistsMore;
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

        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+artistsMore.result.get(position).image)
                .thumbnail( 0.1f )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
        title.setText(artistsMore.result.get(position).name);
        subTitle.setText("Artist");

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), DetailArtist.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", artistsMore.result.get(position).uid);
                intent.putExtra("type", "artist");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return artistsMore.result.size();
    }
}
