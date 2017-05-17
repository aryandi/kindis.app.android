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

import java.util.List;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.PlayerActionHelper;
import co.digdaya.kindis.helper.PlayerSessionHelper;
import co.digdaya.kindis.model.DataSingleOffline;
import co.digdaya.kindis.service.PlayerService;
import co.digdaya.kindis.view.holder.Item;

/**
 * Created by DELL on 5/11/2017.
 */

public class AdapterSongOffline extends RecyclerView.Adapter<Item>{
    PlayerSessionHelper playerSessionHelper;
    Activity activity;
    List<DataSingleOffline> dataSingleOfflines;

    public AdapterSongOffline(Activity activity, List<DataSingleOffline> dataSingleOfflines) {
        this.activity = activity;
        this.dataSingleOfflines = dataSingleOfflines;
        playerSessionHelper = new PlayerSessionHelper();
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

        final String songResource = dataSingleOffline.getPath();
        final String titles = dataSingleOffline.getTitle();
        final String subtitle = dataSingleOffline.getArtist();
        final String image = dataSingleOffline.getImage();
        final String artist_id = dataSingleOffline.getArtist_id();
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PlayerSessionHelper().setPreferences(activity, "index", "1");
                Intent intent = new Intent(activity, PlayerService.class);
                intent.setAction(PlayerActionHelper.ACTION_PLAY_OFFLINE);
                intent.putExtra("songresource", songResource);
                playerSessionHelper.setPreferences(activity, "title", titles);
                playerSessionHelper.setPreferences(activity, "subtitle", subtitle);
                playerSessionHelper.setPreferences(activity, "image", image);
                playerSessionHelper.setPreferences(activity, "artist_id", artist_id);
                activity.startService(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSingleOfflines.size();
    }
}
