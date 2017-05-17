package co.digdaya.kindis.view.adapter.item;

import android.app.Activity;
import android.app.Dialog;
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
import co.digdaya.kindis.view.activity.Detail.Detail;
import co.digdaya.kindis.view.dialog.DialogGetPremium;
import co.digdaya.kindis.view.holder.Item;

/**
 * Created by DELL on 3/30/2017.
 */

public class AdapterPlaylistHorizontal extends RecyclerView.Adapter<Item> {
    Activity context;
    Dialog dialogPremium;
    DialogGetPremium dialogGetPremium;
    DataPlaylist dataPlaylist;
    int type;

    public AdapterPlaylistHorizontal(Activity context, DataPlaylist dataPlaylist, int type){
        this.context = context;
        this.dataPlaylist = dataPlaylist;
        this.type = type;
        dialogGetPremium = new DialogGetPremium(context, dialogPremium);
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (type == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_album, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_grid, parent, false);
        }
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

        final DataPlaylist.Data data = dataPlaylist.data.get(position);
        if (type!=5){
            badgePremium.setVisibility(View.VISIBLE);
        }
        if (getItemViewType(position) == 0){
            badgePremium.setImageResource(R.drawable.ic_badge_sponsored);
        }
        title.setText(data.name);
        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+data.image)
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Detail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", data.uid);
                intent.putExtra("type", "premium");
                intent.putExtra("isMyPlaylist", "");
                intent.putExtra("playlisttype", getItemViewType(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataPlaylist.data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.parseInt(dataPlaylist.data.get(position).is_premium);
    }
}
