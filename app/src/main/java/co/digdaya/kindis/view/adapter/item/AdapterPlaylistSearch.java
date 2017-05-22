package co.digdaya.kindis.view.adapter.item;

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
import co.digdaya.kindis.model.DataPlaylist;
import co.digdaya.kindis.model.PlaylistModelSearch;
import co.digdaya.kindis.view.activity.Detail.Detail;
import co.digdaya.kindis.view.holder.Item;

/**
 * Created by DELL on 5/22/2017.
 */

public class AdapterPlaylistSearch extends RecyclerView.Adapter<Item> {
    Activity activity;
    PlaylistModelSearch playlistModelSearch;

    public AdapterPlaylistSearch(Activity activity, PlaylistModelSearch playlistModelSearch) {
        this.activity = activity;
        this.playlistModelSearch = playlistModelSearch;
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_album, parent, false);
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

        badgePremium.setVisibility(View.VISIBLE);
        if (playlistModelSearch.playlist.get(position).is_premium.equals("0")){
            badgePremium.setImageResource(R.drawable.ic_badge_sponsored);
        }
        title.setText(playlistModelSearch.playlist.get(position).name);
        Glide.with(activity)
                .load(ApiHelper.BASE_URL_IMAGE+playlistModelSearch.playlist.get(position).image)
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, Detail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", playlistModelSearch.playlist.get(position).playlist_id);
                intent.putExtra("type", "premium");
                intent.putExtra("isMyPlaylist", "");
                intent.putExtra("playlisttype", getItemViewType(position));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistModelSearch.playlist.size();
    }
}
