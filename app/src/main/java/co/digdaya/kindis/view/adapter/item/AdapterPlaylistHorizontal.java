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

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.model.PlaylistModel;
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
    ArrayList<PlaylistModel> listPremium = new ArrayList<PlaylistModel>();
    HashMap<String, String> dataItem;
    PlaylistModel playlistModel;

    public AdapterPlaylistHorizontal(Activity context, ArrayList<PlaylistModel> listPremium){
        this.context = context;
        this.listPremium = listPremium;
        dialogGetPremium = new DialogGetPremium(context, dialogPremium);
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_album, parent, false);
        Item item= new Item(view);
        return item;
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {
        ImageView imageView = holder.imageView;
        ImageView badgePremium = holder.badgePremium;
        TextView title = holder.title;
        TextView subTitle = holder.subtitle;
        RelativeLayout click = holder.click;

        playlistModel = listPremium.get(position);

        title.setText(playlistModel.getName());
        Glide.with(context)
                .load(ApiHelper.BASE_URL_IMAGE+playlistModel.getImage())
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Detail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", playlistModel.getUid());
                intent.putExtra("type", "premium");
                intent.putExtra("isMyPlaylist", "");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPremium.size();
    }
}
