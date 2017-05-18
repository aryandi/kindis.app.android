package co.digdaya.kindis.view.adapter.item;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.PlayerActionHelper;
import co.digdaya.kindis.helper.PlayerSessionHelper;
import co.digdaya.kindis.model.DataSingleOffline;
import co.digdaya.kindis.service.PlayerService;
import co.digdaya.kindis.view.holder.ItemSong;

/**
 * Created by DELL on 5/18/2017.
 */

public class AdapterDetailSongOffline extends RecyclerView.Adapter<ItemSong> {
    Activity activity;
    List<DataSingleOffline> dataSingleOfflines;
    PlayerSessionHelper playerSessionHelper;

    public AdapterDetailSongOffline(Activity activity, List<DataSingleOffline> dataSingleOfflines) {
        this.activity = activity;
        this.dataSingleOfflines = dataSingleOfflines;
        playerSessionHelper = new PlayerSessionHelper();
    }

    @Override
    public ItemSong onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_song, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClicked(viewType);
            }
        });
        ItemSong item= new ItemSong(view);
        return item;
    }

    @Override
    public void onBindViewHolder(ItemSong holder, int position) {
        TextView title = holder.title;
        TextView subTitle = holder.subTitle;
        ImageButton btnMenu = holder.btnMenu;

        title.setText(dataSingleOfflines.get(position).getTitle());
        subTitle.setText(dataSingleOfflines.get(position).getArtist());
        btnMenu.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return dataSingleOfflines.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void onClicked(int pos){
        DataSingleOffline dataSingleOffline = dataSingleOfflines.get(pos);
        final String songResource = dataSingleOffline.getPath();
        final String titles = dataSingleOffline.getTitle();
        final String subtitle = dataSingleOffline.getArtist();
        final String image = dataSingleOffline.getImage();
        final String artist_id = dataSingleOffline.getArtist_id();
        playerSessionHelper.setPreferences(activity, "index", "1");
        Intent intent = new Intent(activity, PlayerService.class);
        intent.setAction(PlayerActionHelper.ACTION_PLAY_OFFLINE);
        intent.putExtra("songresource", songResource);
        playerSessionHelper.setPreferences(activity, "title", titles);
        playerSessionHelper.setPreferences(activity, "subtitle", subtitle);
        playerSessionHelper.setPreferences(activity, "image", image);
        playerSessionHelper.setPreferences(activity, "artist_id", artist_id);
        activity.startService(intent);
    }
}
