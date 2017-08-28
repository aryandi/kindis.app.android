package co.digdaya.kindis.live.view.adapter.offline;

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

import java.util.List;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.Constanta;
import co.digdaya.kindis.live.model.DataAlbumOffline;
import co.digdaya.kindis.live.view.activity.Detail.DetailOffline;
import co.digdaya.kindis.live.view.holder.Item;

/**
 * Created by DELL on 5/18/2017.
 */

public class AdapterAlbumOffline extends RecyclerView.Adapter<Item> {
    Activity activity;
    List<DataAlbumOffline> dataAlbumOfflines;

    public AdapterAlbumOffline(Activity activity, List<DataAlbumOffline> dataAlbumOfflines) {
        this.activity = activity;
        this.dataAlbumOfflines = dataAlbumOfflines;
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
        DataAlbumOffline dataAlbumOffline = dataAlbumOfflines.get(position);
        ImageView imageView = holder.imageView;
        TextView title = holder.title;
        TextView subTitle = holder.subtitle;
        RelativeLayout click = holder.click;

        title.setText(dataAlbumOffline.getAlbum());
        subTitle.setText(dataAlbumOffline.getArtist());
        Glide.with(activity)
                .load(dataAlbumOffline.getImage())
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);

    }

    @Override
    public int getItemCount() {
        return dataAlbumOfflines.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void onClicked(int pos){
        Intent intent = new Intent(activity, DetailOffline.class);
        intent.putExtra(Constanta.INTENT_ACTION_DOWNLOAD_ALBUM, dataAlbumOfflines.get(pos).getAlbum());
        intent.putExtra(Constanta.INTENT_ACTION_DOWNLOAD_ALBUM_ID, dataAlbumOfflines.get(pos).getAlbum_id());
        intent.putExtra(Constanta.INTENT_ACTION_DOWNLOAD_DESC, dataAlbumOfflines.get(pos).getDesc());
        intent.putExtra(Constanta.INTENT_ACTION_DOWNLOAD_IMAGE, dataAlbumOfflines.get(pos).getBanner());
        activity.startActivity(intent);
    }
}
