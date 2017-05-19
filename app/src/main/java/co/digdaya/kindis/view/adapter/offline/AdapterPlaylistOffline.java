package co.digdaya.kindis.view.adapter.offline;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.Constanta;
import co.digdaya.kindis.model.DataPlaylistOffline;
import co.digdaya.kindis.view.activity.Detail.DetailOffline;
import co.digdaya.kindis.view.holder.Item;

/**
 * Created by DELL on 5/19/2017.
 */

public class AdapterPlaylistOffline extends RecyclerView.Adapter<Item> {
    Activity activity;
    List<DataPlaylistOffline> dataPlaylistOfflines;

    public AdapterPlaylistOffline(Activity activity, List<DataPlaylistOffline> dataPlaylistOfflines) {
        this.activity = activity;
        this.dataPlaylistOfflines = dataPlaylistOfflines;
    }

    @Override
    public Item onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_grid, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicked(viewType);
            }
        });
        Item item= new Item(view);
        return item;
    }

    @Override
    public void onBindViewHolder(Item holder, int position) {
        DataPlaylistOffline dataPlaylistOffline = dataPlaylistOfflines.get(position);
        ImageView imageView = holder.imageView;
        TextView title = holder.title;

        title.setText(dataPlaylistOffline.getPlaylist());
        Glide.with(activity)
                .load(dataPlaylistOffline.getImage())
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dataPlaylistOfflines.size();
    }

    private void onClicked(int pos){
        Intent intent = new Intent(activity, DetailOffline.class);
        intent.putExtra(Constanta.INTENT_ACTION_DOWNLOAD_ALBUM, dataPlaylistOfflines.get(pos).getPlaylist());
        intent.putExtra(Constanta.INTENT_ACTION_DOWNLOAD_ALBUM_ID, dataPlaylistOfflines.get(pos).getPlaylist_id());
        intent.putExtra(Constanta.INTENT_ACTION_DOWNLOAD_DESC, "");
        intent.putExtra(Constanta.INTENT_ACTION_DOWNLOAD_IMAGE, dataPlaylistOfflines.get(pos).getBanner());
        activity.startActivity(intent);
    }
}
